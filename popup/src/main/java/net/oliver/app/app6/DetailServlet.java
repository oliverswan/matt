package net.oliver.app.app6;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.oliver.app.app6.info.ContactBean;
import net.oliver.app.app6.info.ContactBeanManager;

public class DetailServlet extends HttpServlet{

	private static final long serialVersionUID = 8129068178478705115L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String id = req.getParameter("id");
		ContactBean bean = ContactBeanManager.getBean(id);
		
		req.setAttribute("ContactBean", bean);
		
		req.getRequestDispatcher("detail.jsp").forward(req, resp);
//		resp.getWriter().write(bean.toString());
//		resp.flushBuffer();
	}
	
	

}
