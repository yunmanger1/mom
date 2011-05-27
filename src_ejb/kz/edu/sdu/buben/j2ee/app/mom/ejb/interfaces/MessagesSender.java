package kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

public interface MessagesSender {
   public void sendMessages(IMessagingService ms, Session session, MessageProducer producer) throws JMSException;
}
