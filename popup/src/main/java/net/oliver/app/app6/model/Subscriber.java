package net.oliver.app.app6.model;

import org.eclipse.jetty.continuation.Continuation;

public class Subscriber {

	private String channel;
	private Continuation continuation;
//	private List<String> messages = new ArrayList<String>();;

	public Subscriber(String channel, Continuation continuation) {
		this.channel = channel;
		this.continuation = continuation;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Continuation getContinuation() {
		return continuation;
	}

	public void setContinuation(Continuation continuation) {
		this.continuation = continuation;
	}

//	public List<String> getMessages() {
//		return messages;
//	}
//
//	public void setMessages(List<String> messages) {
//		this.messages = messages;
//	}

}
