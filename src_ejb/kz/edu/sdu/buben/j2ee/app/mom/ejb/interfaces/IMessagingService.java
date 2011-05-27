package kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import kz.edu.sdu.buben.j2ee.app.mom.utils.XStreamUtils;

public interface IMessagingService {
   public boolean sendTextMessage(Destination destination, String text);

   public boolean sendTextMessage(Destination destination, String text, MessageModifier modifier);

   public boolean sendObjectMessage(Destination destination, Object object);

   public boolean sendObjectMessage(Destination destination, Object object, MessageModifier modifier);

   public boolean forwardMessage(Destination destination, Message msg);

   public boolean forwardMessage(Destination destination, Message msg, MessageModifier modifier);

   public void sendMultipleObjectMessages(Destination destination, MessagesSender ms) throws JMSException;

   public Message prepareMessage(Session session, Object obj) throws JMSException;

   public XStreamUtils a(String alias, Class clazz);
}
