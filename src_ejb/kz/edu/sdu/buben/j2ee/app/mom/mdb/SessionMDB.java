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

import kz.bips.comps.utils.Log4JLoggerWrapper;
import kz.edu.sdu.buben.j2ee.app.mom.AppConsts;
import kz.edu.sdu.buben.j2ee.app.mom.dto.CallSessionRequestDTO;
import kz.edu.sdu.buben.j2ee.app.mom.dto.ResponseDTO;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.LIMessagingService;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.LISessionEJB;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.MessageModifier;
import kz.edu.sdu.buben.j2ee.app.mom.utils.SessionUtils;
import kz.edu.sdu.buben.j2ee.app.mom.utils.XStreamUtils;

import org.jboss.ejb3.annotation.ResourceAdapter;

@MessageDriven(activationConfig = {@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"), @ActivationConfigProperty(propertyName = "destination", propertyValue = AppConsts.REQSESS_QUEUE_NAME)})
@ResourceAdapter("hornetq-ra.rar")
public class SessionMDB implements MessageListener {
   private final Log4JLoggerWrapper log = new Log4JLoggerWrapper(getClass());

   @EJB
   LIMessagingService ms;

   @EJB
   LISessionEJB db;

   @Resource(mappedName = AppConsts.NONE_QUEUE_NAME)
   private Queue destination;

   @Resource(mappedName = AppConsts.REPSESS_QUEUE_NAME)
   private Queue replyQueue;

   private XStreamUtils ju;

   @PostConstruct
   public void init() {
      ju = new XStreamUtils();
      ju.a(CallSessionRequestDTO.ALIAS, CallSessionRequestDTO.class);
   }

   @Override
   public void onMessage(Message msg) {
      try {
         log.debug("%s got message", AppConsts.EVENT_QUEUE_NAME);
         if (!msg.propertyExists(AppConsts.MESSAGE_TYPE) || !(msg instanceof TextMessage)) {
            throw new IOError(null);
         }
         String type = msg.getStringProperty(AppConsts.MESSAGE_TYPE);
         if (type.equals(AppConsts.REQUEST_SESSION_MT)) {
            log.debug("Request session message");
            String text = ((TextMessage) msg).getText();
            CallSessionRequestDTO dto = null;
            try {
               dto = ju.fromXml(text, CallSessionRequestDTO.class);
            } catch (Exception e) {
               log.error("Could not convert xml to object", e);
               throw new IOError(null);
            }
            ResponseDTO rdto = SessionUtils.requestSession(db, dto);
            rdto.setRequestid(dto.getRequestid());
            ms.a(ResponseDTO.ALIAS, ResponseDTO.class);
            ms.sendObjectMessage(replyQueue, rdto);
         } else {
            throw new IOError(null);
         }
      } catch (IOError e) {
         forwardMessageToNone(msg);
      } catch (JMSException e) {
         log.logStackTrace(e, "Oops");
      }
   }

   private void forwardMessageToNone(Message msg) {
      log.debug("EventMDB: UNKNOWN message. forwarding needed");
      ms.forwardMessage(destination, msg, new MessageModifier() {

         @Override
         public void modify(Message message) throws JMSException {
            message.setStringProperty(AppConsts.FORWARD_FROM, AppConsts.BALANCE_QUEUE_NAME);
         }
      });
   }
}
