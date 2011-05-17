package kz.edu.sdu.buben.j2ee.app.mom.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

import javax.ejb.EJB;
import javax.management.InvalidAttributeValueException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.LIBalanceChangeEJB;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.LISimpleEJB;

import org.apache.log4j.Logger;

public class SimpleServlet extends HttpServlet {

	private final Logger log = Logger.getLogger(getClass());

	@EJB
	LISimpleEJB cmd;

	@EJB
	LIBalanceChangeEJB balance;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			PrintWriter out = new PrintWriter(response.getOutputStream());
			String message = "Hello World!";
			cmd.sayHello(message);
			out.printf("<html><body>%s</body></html>", message);
			try {
				balance.sayToChangeBalance("7024476704", BigDecimal
						.valueOf(1000));
			} catch (InvalidAttributeValueException e) {
				log.error("Could not send message");
			}
			balance.saySthStupid();
			out.close();
		} catch (IOException e) {
			log.trace(e);
		}

	}
}
