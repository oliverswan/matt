package net.oliver.app.app6;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;


public class WebApp {

	public static void main(String[] args) {
		try {
//			String contextPath = "aa";
			
			String resourceBase = "./webapp";

			Server server = new Server(Integer.parseInt(Configuration.getValue("web_port")));
//			ResourceHandler resourceHandler = new ResourceHandler();
			WebAppContext context = new WebAppContext();
//			context.setContextPath(contextPath);
			context.setResourceBase(resourceBase);

	        // LoginServlet
	        ServletHolder loginholder = new ServletHolder(new LoginServlet());
	        loginholder.setAsyncSupported(true);
	        context.addServlet(loginholder, "/login");
	        
	        // PublishServlet
	        ServletHolder holder = new ServletHolder(new PublishServlet());
	        holder.setAsyncSupported(true);
	        context.addServlet(holder, "/publish");
	        
	        // DetailServlet
	        ServletHolder detailholder = new ServletHolder(new DetailServlet());
//	        detailholder.setAsyncSupported(true);
	        context.addServlet(detailholder, "/detail");
	        
	        ServletHolder sayholder = new ServletHolder(new SayServlet());
//	        detailholder.setAsyncSupported(true);
	        context.addServlet(sayholder, "/say");
	        
	        // Listener
	        context.addEventListener(new ModelInitialiser());

			server.setHandler(context);
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
		}
	}

}
