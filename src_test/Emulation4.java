import java.util.Hashtable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

import kz.edu.sdu.buben.j2ee.app.mom.AppConsts;

public class Emulation4 {

   public static void main(String[] args) throws Exception {
      long start = System.currentTimeMillis();
      EmulationUtils.addBalanceJMS("7024476704", 1000);
      EmulationUtils.reserveCallJMS("7024476704", "7013362535", 123);
      System.out.printf("Took %d ms\n", System.currentTimeMillis() - start);

      Connection connection = null;
      InitialContext initialContext = null;

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

         initialContext = new InitialContext(jndiParameters);

         Queue queue = (Queue) initialContext.lookup(AppConsts.REPSESS_QUEUE_NAME);

         ConnectionFactory cf = (ConnectionFactory) initialContext.lookup("/ConnectionFactory");
         connection = cf.createConnection();

         Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
         MessageConsumer consumer = session.createConsumer(queue);

         connection.start();

         System.out.println(((TextMessage) consumer.receive()).getText());

         connection.close();

         System.out.println("Done");
      } finally {
         // Step 5. Be sure to close our JMS resources!
         if (initialContext != null) {
            initialContext.close();
         }
         if (connection != null) {
            connection.close();
         }
      }
   }
}
