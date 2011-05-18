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

	/**
	 * 
	 */
	private static final long serialVersionUID = -2710324555728113741L;

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
			out.print("<html><body><form method=POST>");
			out.flush();
			cmd.sayHello(message);
			// out.println("Hi!");
			out.print("<input type=text name=number value='7024476704'/><br/>");
			out.print("<input type=text name=amount value='1000.0'/><br/>");
			out.println("<input type='submit' value='submit'/><br/>");
			out.println("</form></body></html>");
			// balance.saySthStupid();
			out.close();
		} catch (IOException e) {
			log.trace("Ooops");
		}

	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			PrintWriter out = new PrintWriter(response.getOutputStream());
			String number = request.getParameter("number");
			String amount = request.getParameter("amount");
			try {
				balance.sayToChangeBalance(number, BigDecimal.valueOf(Double
						.valueOf(amount)));
				out.print("<html><body>");
				out.println("OK");
				out.println("</body></html>");
			} catch (InvalidAttributeValueException e) {
				log.error("Could not send message");
				out.print("<html><body>");
				out.println(e.getMessage());
				out.println("</body></html>");
			}
		} catch (IOException e) {
			log.trace("Oops");
		}

	}
}
