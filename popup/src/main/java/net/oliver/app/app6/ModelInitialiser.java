package net.oliver.app.app6;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.oliver.app.app6.model.AbstractWiseServlet;
import net.oliver.app.app6.model.Subscriber;


public class ModelInitialiser implements ServletContextListener {

	public void contextInitialized(ServletContextEvent sce) {


		ServletContext appScope = sce.getServletContext();
		final Map<String, List<Subscriber>> clients = new ConcurrentHashMap<String, List<Subscriber>>();
		appScope.setAttribute(Constants.CLIENTS, clients);
	}

	public void contextDestroyed(ServletContextEvent sce) {
	}
}
