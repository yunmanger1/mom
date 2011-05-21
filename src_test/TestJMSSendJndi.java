import java.math.BigDecimal;
import java.util.Hashtable;

import javax.jms.JMSException;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.RIBalanceEJB;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.RISessionEJB;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestJMSSendJndi {

   public static RISessionEJB sessionEjb;
   public static RIBalanceEJB balanceEjb;

   @BeforeClass
   public static void init() {
      // Connection connection = null;
      InitialContext ic = null;

      try {
         // Step 1. Create an initial context to perform the JNDI lookup.
         // This JNDI is performing auto-discovery of the servers, by using
         // the default UDP properties.
         // You will find more information at the JBoss Application Server
         // Documentation:
         // http://www.jboss.org/file-access/default/members/jbossas/freezone/docs/Clustering_Guide/5/html/clustering-jndi.html

         Hashtable<String, String> jndiParameters = new Hashtable<String, String>();
         jndiParameters.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
         jndiParameters.put("java.naming.factory.url.pkgs=", "org.jboss.naming:org.jnp.interfaces");

         ic = new InitialContext(jndiParameters);
         sessionEjb = (RISessionEJB) ic.lookup("mom/SessionEJB/remote");
         balanceEjb = (RIBalanceEJB) ic.lookup("mom/BalanceEJB/remote");
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   @AfterClass
   public static void destroy() {

   }

   @Test
   public void testCheckReserve() throws NamingException, JMSException, InterruptedException {
      System.out.println(sessionEjb.reserveCallSession("7024476704", "7013682535", 30));
   }

   @Test
   public void testAddBalance() throws NamingException, JMSException, InterruptedException {
      balanceEjb.changeBalance("7024476704", BigDecimal.valueOf(100));
   }

   @Test
   public void testOverCallSession() throws NamingException, JMSException, InterruptedException {
      sessionEjb.overCallSession("7024476704", "7013682535");
   }

//   @Test
//   public void testSendJMS() throws NamingException, JMSException, InterruptedException {
//      Connection connection = null;
//      InitialContext initialContext = null;
//
//      try {
//         // Step 1. Create an initial context to perform the JNDI lookup.
//         // This JNDI is performing auto-discovery of the servers, by using
//         // the default UDP properties.
//         // You will find more information at the JBoss Application Server
//         // Documentation:
//         // http://www.jboss.org/file-access/default/members/jbossas/freezone/docs/Clustering_Guide/5/html/clustering-jndi.html
//
//         Hashtable<String, String> jndiParameters = new Hashtable<String, String>();
//         jndiParameters.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
//         jndiParameters.put("java.naming.factory.url.pkgs=", "org.jboss.naming:org.jnp.interfaces");
//
//         initialContext = new InitialContext(jndiParameters);
//
//         Queue queue = (Queue) initialContext.lookup("queue/ClientRequestQueue");
//
//         ConnectionFactory cf = (ConnectionFactory) initialContext.lookup("/ConnectionFactory");
//         connection = cf.createConnection();
//
//         Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//         MessageProducer producer = session.createProducer(queue);
//
//         TextMessage msg = session.createTextMessage();
//         msg.setText("the text");
//
//         for (int i = 0; i < 100000; i++) {
//            producer.send(msg);
//         }
//
//         connection.close();
//
//         /*
//          * for (int i = 0; i < 100; i++) { // Step 2. Perform a lookup on
//          * the Connection Factory ConnectionFactory cf =
//          * (ConnectionFactory)initialContext.lookup("/ConnectionFactory");
//          * 
//          * // Step 3. Create a JMS Connection connection =
//          * cf.createConnection(); connection.close();
//          * 
//          * // Step 4. Kill any of the servers. The Lookups will still be
//          * performed ok as long as you keep at least one // server alive.
//          * System.out.println("Connection " + i +
//          * " was created. If you kill any of the servers now, the lookup operation on Step 2 will still work fine"
//          * ); Thread.sleep(5000); }
//          */
//
//         System.out.println("Done");
//      } finally {
//         // Step 5. Be sure to close our JMS resources!
//         if (initialContext != null) {
//            initialContext.close();
//         }
//         if (connection != null) {
//            connection.close();
//         }
//      }
//
//   }
//
//   @Test
//   @Ignore
//   public void testReceiveJMS() throws NamingException, JMSException, InterruptedException {
//      Connection connection = null;
//      InitialContext initialContext = null;
//
//      try {
//         // Step 1. Create an initial context to perform the JNDI lookup.
//         // This JNDI is performing auto-discovery of the servers, by using
//         // the default UDP properties.
//         // You will find more information at the JBoss Application Server
//         // Documentation:
//         // http://www.jboss.org/file-access/default/members/jbossas/freezone/docs/Clustering_Guide/5/html/clustering-jndi.html
//
//         Hashtable<String, String> jndiParameters = new Hashtable<String, String>();
//         jndiParameters.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
//         jndiParameters.put("java.naming.factory.url.pkgs=", "org.jboss.naming:org.jnp.interfaces");
//
//         initialContext = new InitialContext(jndiParameters);
//
//         Queue queue = (Queue) initialContext.lookup("queue/ClientRequestQueue");
//
//         ConnectionFactory cf = (ConnectionFactory) initialContext.lookup("/ConnectionFactory");
//         connection = cf.createConnection();
//
//         Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//         MessageConsumer consumer = session.createConsumer(queue);
//
//         TextMessage msg = session.createTextMessage();
//         msg.setText("the text");
//
//         connection.start();
//
//         for (int i = 0; i < 100; i++) {
//            System.out.println(((TextMessage) consumer.receive()).getText());
//            Thread.sleep(100);
//         }
//
//         connection.close();
//
//         System.out.println("Done");
//      } finally {
//         // Step 5. Be sure to close our JMS resources!
//         if (initialContext != null) {
//            initialContext.close();
//         }
//         if (connection != null) {
//            connection.close();
//         }
//      }
//
//   }

}
