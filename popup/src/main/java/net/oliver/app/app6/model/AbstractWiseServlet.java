package net.oliver.app.app6.model;

import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.oliver.app.app6.Constants;
import net.oliver.app.app6.IMessageSender;
import net.oliver.app.app6.Worker;

public abstract class AbstractWiseServlet extends HttpServlet implements IMessageSender {

	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(AbstractWiseServlet.class);
	private BlockingQueue<Object> msgQueue = new LinkedBlockingQueue<Object>(100);
	private MsgConsumer consumer;
	private Worker worker;

	@Override
	public void init() throws ServletException {
		super.init();

		if (this.getServletName().indexOf("Login") > -1) {
			Worker worker = new Worker(this);
			worker.start();

			consumer = new MsgConsumer();
			consumer.start();
		}

	}

	@Override
	public void destroy() {
		super.destroy();
		if (this.getServletName().indexOf("Login") > -1) {
			consumer.shutdown();
			worker.stop();
		}

	}

	protected Subscriber addSubscriber(String channel, Continuation continuation) {
		log.info("Add Subscriber For " + channel);
		Subscriber subscriber = new Subscriber(channel, continuation);
		ConcurrentHashMap<String, CopyOnWriteArrayList<Subscriber>> clients = (ConcurrentHashMap<String, CopyOnWriteArrayList<Subscriber>>) getServletContext()
				.getAttribute(Constants.CLIENTS);
		CopyOnWriteArrayList<Subscriber> subs = clients.get(channel);
		if (subs == null) {
			subs = new CopyOnWriteArrayList<Subscriber>();
			clients.putIfAbsent(channel, subs);
		}
		subs = clients.get(channel);
		subs.add(subscriber);
		continuation.addContinuationListener(new ContinuationListener() {

			@Override
			public void onTimeout(Continuation continuation) {
				log.info("Delete Subscriber For Timeout...from " + channel);
				removeSubscriber(channel, subscriber);

			}

			@Override
			public void onComplete(Continuation continuation) {
				log.info("Delete Subscriber For Complete...from " + channel);
				removeSubscriber(channel, subscriber);
			}
		});
		return subscriber;
	}

	protected void removeSubscriber(String channel, Subscriber subscriber) {
		List<Subscriber> subs = getSubscribers(channel);
		subs.remove(subscriber);
		log.info("Delete subscriber..of " + channel + " left " + subs.size());
		if (subs.size() == 0) {
			ConcurrentHashMap<String, List<Subscriber>> clients = (ConcurrentHashMap<String, List<Subscriber>>) getServletContext()
					.getAttribute(Constants.CLIENTS);
			clients.remove(channel);
		}
	}

	protected List<Subscriber> getSubscribers(String channel) {
		ConcurrentHashMap<String, List<Subscriber>> clients = (ConcurrentHashMap<String, List<Subscriber>>) getServletContext()
				.getAttribute(Constants.CLIENTS);

		return clients.get(channel);
	}

	// called by worker fastly
	public void publish(Object data) {
		msgQueue.offer(data);
	}

	protected class MsgConsumer extends Thread {
		private boolean running = true;

		public void shutdown() {
			running = false;
		}

		@Override
		public void run() {

			try {
				while (running) {
					// take bunch from queue
					Object data = msgQueue.take();
					if (null != data) {
						if (data instanceof Map) {
							Map<String, List<String>> items = (Map<String, List<String>>) data;

							items.forEach((k, v) -> {
								List<Subscriber> subscribers = getSubscribers(k);
								if (subscribers == null) {
									return;
								}
								log.info("Take out data from queue: " + k + " subs: " + subscribers.size());
								for (Subscriber sub : subscribers) {
									if (sub.getContinuation().isSuspended()) {
										log.info("transfer data  to loginservlet for " + v+" sub: "+sub);
										sub.getContinuation().setAttribute(Constants.MESSAGE, v);
										sub.getContinuation().resume();
									} else {
										log.info("Jump " + k);
									}
								}
							});

						}
					} else {
						// 超过2s还没数据，认为所有生产线程都已经退出，自动退出消费线程。
						// isRunning = false;
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			} finally {
			}
		}

	}
}
