package kz.edu.sdu.buben.j2ee.app.mom.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kz.edu.sdu.buben.j2ee.app.mom.AppProps;
import kz.edu.sdu.buben.j2ee.app.mom.dto.ChangeBalanceDTO;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.LIMessagingService;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.MessageModifier;

import org.apache.log4j.Logger;

public class SimpleServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2710324555728113741L;

	private final Logger log = Logger.getLogger(getClass());

	@EJB
	LIMessagingService ms;

	@Resource(mappedName = AppProps.BALANCE_QUEUE_NAME)
	Queue destination;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			PrintWriter out = new PrintWriter(response.getOutputStream());
			out.print("<html><body><form method=POST>");
			out.print("<input type=text name=number value='7024476704'/><br/>");
			out.print("<input type=text name=amount value='1000.0'/><br/>");
			out.print("<input type='submit' value='submit'/><br/>");
			out.print("</form></body></html>");
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
			out.print("<html><body>");
			ChangeBalanceDTO dto = new ChangeBalanceDTO();
			dto.setPhoneNumber(number);
			dto.setDelta(BigDecimal.valueOf(Double.valueOf(amount)));
			boolean r = ms.sendObjectMessage(destination, dto,
					new MessageModifier() {
						@Override
						public void modify(Message message) throws JMSException {
							message.setStringProperty(AppProps.MESSAGE_TYPE,
									AppProps.CHANGE_BALANCE_MT);
							message.setJMSDeliveryMode(DeliveryMode.PERSISTENT);
						}
					});
			if (r) {
				out.print("<h1>OK</h1>");
			} else {
				out.print("<h1>FAIL</h1>");
			}
			out.println("</body></html>");
		} catch (IOException e) {
			log.trace("Oops");
		}

	}
}
