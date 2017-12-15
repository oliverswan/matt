package net.oliver.app.app6;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class SayServlet  extends HttpServlet{

	private static final long serialVersionUID = 8129068178478705115L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String id = req.getParameter("id");
		System.out.println("receive id : " +id);
		resp.getWriter().write("ok");
		resp.flushBuffer();
	}
	
	

}
