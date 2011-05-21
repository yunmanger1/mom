package kz.edu.sdu.buben.j2ee.app.mom.mdb;

import java.io.IOError;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.TextMessage;

import kz.edu.sdu.buben.j2ee.app.mom.AppConsts;
import kz.edu.sdu.buben.j2ee.app.mom.dto.ChargeSessionDTO;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.LIMessagingService;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.LISessionEJB;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.MessageModifier;
import kz.edu.sdu.buben.j2ee.app.mom.utils.XStreamUtils;

import org.apache.log4j.Logger;
import org.jboss.ejb3.annotation.ResourceAdapter;

@MessageDriven(activationConfig = {@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"), @ActivationConfigProperty(propertyName = "destination", propertyValue = AppConsts.EVENT_QUEUE_NAME)})
@ResourceAdapter("hornetq-ra.rar")
public class EventMDB implements MessageListener {
   private final Logger log = Logger.getLogger(getClass());

   @EJB
   LIMessagingService ms;

   @EJB
   LISessionEJB db;

   @Resource(mappedName = AppConsts.NONE_QUEUE_NAME)
   private Queue destination;

   private XStreamUtils ju;

   @PostConstruct
   public void init() {
      ju = new XStreamUtils();
      ju.a(ChargeSessionDTO.ALIAS, ChargeSessionDTO.class);
   }

   @Override
   public void onMessage(Message msg) {
      try {
         if (!msg.propertyExists(AppConsts.MESSAGE_TYPE) || !(msg instanceof TextMessage)) {
            throw new IOError(null);
         }
         String type = msg.getStringProperty(AppConsts.MESSAGE_TYPE);
         if (type.equals(AppConsts.CHARGE_SESSION_MT)) {
            String text = ((TextMessage) msg).getText();
            ChargeSessionDTO dto = null;
            try {
               dto = ju.fromXml(text, ChargeSessionDTO.class);
            } catch (Exception e) {
               log.error("Could not convert xml to object", e);
               throw new IOError(null);
            }
            db.chargeSession(dto.getSession_type(), dto.getSession_id());

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
      ms.forwardMessage(destination, msg, new MessageModifier() {

         @Override
         public void modify(Message message) throws JMSException {
            message.setStringProperty(AppConsts.FORWARD_FROM, AppConsts.BALANCE_QUEUE_NAME);
         }
      });
   }
}
