package net.oliver.app.app6;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import net.oliver.app.app6.info.ContactBean;
import net.oliver.app.app6.info.ContactBeanManager;
import net.oliver.app.app6.model.AbstractWiseServlet;

public class LoginServlet extends AbstractWiseServlet {

	private static final long serialVersionUID = 1L;
	private static Logger log = LoggerFactory.getLogger(LoginServlet.class);
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");

		String channel = request.getParameter("channel");
		Continuation continuation = ContinuationSupport.getContinuation(request);
		continuation.setTimeout(90000000);

		if (continuation.isInitial()) {
			this.addSubscriber(channel, continuation);
			continuation.suspend();
		}	

		if (continuation.isResumed()) {
			// slow
			List<String> tickets = (List<String>)continuation.getAttribute(Constants.MESSAGE);
			
			List<ContactBean> beans = new ArrayList<ContactBean>();
			
				for(String ticket : tickets)
				{
					ContactBean bean = ContactBeanManager.getBean(ticket);
					beans.add(bean);
				}
				String result = JSON.toJSONString(beans);
				log.info("Send Back to browser.."+result);
				response.getWriter().println(result);
				response.flushBuffer();
//			}
		}
	}

}
