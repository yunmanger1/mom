package kz.edu.sdu.buben.j2ee.app.mom.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.LISimpleEJB;

import org.apache.log4j.Logger;

public class SimpleServlet extends HttpServlet {

	private final Logger log = Logger.getLogger(getClass());

	@EJB
	LISimpleEJB cmd;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			PrintWriter out = new PrintWriter(response.getOutputStream());
			StringBuffer sb = new StringBuffer();
			String message = "Hello World!";
			cmd.sayHello(message);
			out.printf("<html><body>%s</body></html>", message);
			out.close();
			System.out.println("im here");
		} catch (IOException e) {
			log.trace(e);
		}

	}

}
