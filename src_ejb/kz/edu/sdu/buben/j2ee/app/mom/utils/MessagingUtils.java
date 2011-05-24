package kz.edu.sdu.buben.j2ee.app.mom.utils;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import kz.bips.comps.utils.Log4JLoggerWrapper;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.IMessagingService;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.MessageModifier;

public class MessagingUtils implements IMessagingService {
   private final Log4JLoggerWrapper log = new Log4JLoggerWrapper(getClass());

//   @Resource(mappedName = AppConsts.CONNECTION_FACTORY_NAME)
   private final ConnectionFactory connectionFactory;

   private XStreamUtils ju;

   public MessagingUtils(ConnectionFactory cf) {
      connectionFactory = cf;
      init();
   }

//   @PostConstruct
   public void init() {
      ju = new XStreamUtils();
   }

   @Override
   public boolean sendTextMessage(Destination destination, String text) {
      return sendTextMessage(destination, text, null);
   }

   @Override
   public boolean sendTextMessage(Destination destination, String text, MessageModifier modifier) {
      if (destination == null) {
         log.error("destination == null");
      } else if (text == null) {
         log.error("text == null");
      } else if (connectionFactory == null) {
         log.error("cf == null");
      } else {
         try {
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            try {
               MessageProducer producer = session.createProducer(destination);
               producer.setDeliveryMode(DeliveryMode.PERSISTENT);
               TextMessage message = session.createTextMessage();
               message.setText(text);
               // ObjectMessage message = session.createObjectMessage();
               // message.setObject(object);
               if (modifier != null) {
                  modifier.modify(message);
               }
               producer.send(message);
               if (destination instanceof Queue) {
                  log.debug(String.format("Success sending to: %s", ((Queue) destination).getQueueName()));
               } else {
                  log.debug("Success sending");
               }
//               producer.close();
            } finally {
//               if (session != null) {
//                  session.close();
//               }
               if (connection != null) {
                  connection.close();
               }
            }
            return true;
         } catch (JMSException e) {
            log.trace("JMS send crash");
         }
      }
      return false;
   }

   @Override
   public boolean forwardMessage(Destination destination, Message msg) {
      return forwardMessage(destination, msg, null);
   }

   @Override
   public boolean forwardMessage(Destination destination, Message msg, MessageModifier modifier) {
      try {
         Connection connection = connectionFactory.createConnection();
         // NO TRANSACTION
         Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
         try {
            MessageProducer producer = session.createProducer(destination);
            if (modifier != null) {
               modifier.modify(msg);
            }
            msg.setJMSCorrelationID(msg.getJMSMessageID());
            producer.send(msg);
            if (destination instanceof Queue) {
               log.debug(String.format("Success forwarding to: %s", ((Queue) destination).getQueueName()));
            } else {
               log.debug("Success forwarding");
            }
            producer.close();
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
         log.trace("JMS send crash");
      }
      return false;
   }

   @Override
   public boolean sendObjectMessage(Destination destination, Object object) {
      return sendObjectMessage(destination, object, null);
   }

   @Override
   public boolean sendObjectMessage(Destination destination, Object object, MessageModifier modifier) {
      String text = null;
      try {
         text = ju.toXml(object);
      } catch (Exception e) {
         log.error(String.format("Could not convert object: %s", object.getClass().getName()));
         return false;
      }
      return sendTextMessage(destination, text, modifier);
   }

}
