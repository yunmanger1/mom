package kz.edu.sdu.buben.j2ee.app.mom.ejb;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import kz.edu.sdu.buben.j2ee.app.mom.AppProps;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.LIMessagingService;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.MessageModifier;

import org.apache.log4j.Logger;

@Stateless
public class MessagingService implements LIMessagingService {
	private final Logger log = Logger.getLogger(getClass());

	@Resource(mappedName = AppProps.CONNECTION_FACTORY_NAME)
	private ConnectionFactory connectionFactory;

	@Override
	public boolean sendObjectMessage(Destination destination,
			Serializable object) {
		return sendObjectMessage(destination, object, null);
	}

	@Override
	public boolean sendObjectMessage(Destination destination,
			Serializable object, MessageModifier modifier) {
		try {
			Connection connection = connectionFactory.createConnection();
			Session session = connection.createSession(false,
					Session.AUTO_ACKNOWLEDGE);
			try {
				MessageProducer producer = session.createProducer(destination);
				ObjectMessage message = session.createObjectMessage();
				message.setObject(object);
				if (modifier != null) {
					modifier.modify(message);
				}
				producer.send(message);
				producer.close();
				log.debug("Success sending");
			} finally {
				if (session != null) {
					session.close();
				}
				if (connection != null) {
					connection.close();
				}
			}
			return true;
		} catch (JMSException e) {
			log.error("JMS send crash");
		}
		return false;
	}

}
