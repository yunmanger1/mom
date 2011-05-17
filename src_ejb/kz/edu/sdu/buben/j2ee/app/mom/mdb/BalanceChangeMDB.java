package kz.edu.sdu.buben.j2ee.app.mom.mdb;

import java.io.IOError;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import kz.edu.sdu.buben.j2ee.app.mom.AppProps;
import kz.edu.sdu.buben.j2ee.app.mom.dto.ChangeBalanceDTO;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.LIBalanceChangeEJB;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.ResourceAdapter;

@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = AppProps.BALANCE_QUEUE_NAME) })
@ResourceAdapter("hornetq-ra.rar")
public class BalanceChangeMDB implements MessageListener {
	private final Logger log = Logger.getLogger(getClass());

	@EJB
	LIBalanceChangeEJB util;

	// @EJB
	// LIMessagingService ms;

	// @Resource(mappedName = AppProps.NONE_QUEUE_NAME)
	// private Queue destination;

	@Override
	public void onMessage(Message msg) {
		try {
			if (!msg.propertyExists(AppProps.MESSAGE_TYPE)) {
				throw new IOError(null);
			}
			String type = msg.getStringProperty(AppProps.MESSAGE_TYPE);
			if (type.equals(AppProps.CHANGE_BALANCE_MT)) {
				ChangeBalanceDTO dto = (ChangeBalanceDTO) ((ObjectMessage) msg)
						.getObject();
				if (!util.changeBalance(dto)) {
					throw new IOError(null);
				}

			} else {
				throw new IOError(null);
			}
		} catch (IOError e) {
			forwardMessageToNone(msg);
		} catch (JMSException e) {
			log.trace(e.getMessage(), e);
		}
	}

	private void forwardMessageToNone(Message msg) {
		log.debug("UNKNOWN message. forwarding needed");
		// TODO: implemend forwarding to AppProps.NONE_QUEUE_NAME
	}
}
