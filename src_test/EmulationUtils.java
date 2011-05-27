import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Hashtable;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.naming.InitialContext;

import kz.edu.sdu.buben.j2ee.app.mom.AppConsts;
import kz.edu.sdu.buben.j2ee.app.mom.dto.BalanceRequestDTO;
import kz.edu.sdu.buben.j2ee.app.mom.dto.BalanceRequestType;
import kz.edu.sdu.buben.j2ee.app.mom.dto.CallSessionRequestDTO;
import kz.edu.sdu.buben.j2ee.app.mom.dto.CallSessionRequestType;
import kz.edu.sdu.buben.j2ee.app.mom.dto.ChangeBalanceDTO;
import kz.edu.sdu.buben.j2ee.app.mom.utils.MessagingUtils;
import kz.edu.sdu.buben.j2ee.app.mom.utils.XStreamUtils;

public class EmulationUtils {
   private static final XStreamUtils ju = new XStreamUtils();
   private static final String CALL_URL = "http://localhost:8080/mom/call/";
   private static final String BALANCE_URL = "http://localhost:8080/mom/balance/";
   public static ConnectionFactory cf;
   public static Destination balance;
   public static Destination sessionReq;
   public static MessagingUtils ms;
   static {
      ju.a(CallSessionRequestDTO.ALIAS, CallSessionRequestDTO.class);
      ju.a(BalanceRequestDTO.ALIAS, BalanceRequestDTO.class);
      Hashtable<String, String> p = new Hashtable<String, String>();
      p.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
      p.put("java.naming.factory.url.pkgs=", "org.jboss.naming:org.jnp.interfaces");

      try {
         InitialContext ic = new InitialContext(p);
         cf = (ConnectionFactory) ic.lookup("/ConnectionFactory");
         balance = (Destination) ic.lookup(AppConsts.BALANCE_QUEUE_NAME);
         sessionReq = (Destination) ic.lookup(AppConsts.REQSESS_QUEUE_NAME);
         ms = new MessagingUtils(cf);
      } catch (Exception e) {
         e.printStackTrace();
      }
   };

   public static int exec(String cmd) throws IOException, InterruptedException {
      System.out.println(cmd);
      Runtime rt = Runtime.getRuntime();
      Process p = rt.exec(cmd.split(" "));
      p.waitFor();
      BufferedReader out = new BufferedReader(new InputStreamReader(p.getInputStream()));
      String line = null;
      while ((line = out.readLine()) != null) {
         System.out.println(line);
      }
      System.out.println(p.exitValue());
      return p.exitValue();
   }

   public static void addBalance(BalanceRequestDTO dto) throws Exception {
      String xml = ju.toXml(dto).replaceAll("\n", "").replaceAll(">[ ]+", ">");
      String cmd = String.format("curl --data %s %s", xml, BALANCE_URL);
      exec(cmd);
   }

   public static void addBalance(String from, int amount) throws Exception {
      BalanceRequestDTO dto = new BalanceRequestDTO(BalanceRequestType.ADD, from, BigDecimal.valueOf(amount));
      addBalance(dto);
   }

   public static void reserveCall(String from, String to) throws Exception {
      CallSessionRequestDTO dto = new CallSessionRequestDTO(CallSessionRequestType.RESERVE, from, to, 30);
      String xml = ju.toXml(dto).replaceAll("\n", "").replaceAll(">[ ]+", ">");
      String cmd = String.format("curl --data %s %s", xml, CALL_URL);
      exec(cmd);
   }

   public static void overSession(String from, String to) throws Exception {
      CallSessionRequestDTO dto = new CallSessionRequestDTO(CallSessionRequestType.OVER, from, to);
      String xml = ju.toXml(dto).replaceAll("\n", "").replaceAll(">[ ]+", ">");
      String cmd = String.format("curl --data %s %s", xml, CALL_URL);
      exec(cmd);
   }

   public static void addBalanceJMS(String from, int amount) throws Exception {
//      BalanceRequestDTO dto = new BalanceRequestDTO(BalanceRequestType.ADD, from, BigDecimal.valueOf(amount));
      ChangeBalanceDTO dto = new ChangeBalanceDTO();
      dto.setPhoneNumber(from);
      dto.setDelta(BigDecimal.valueOf(amount));
      ms.sendObjectMessage(balance, dto, AppConsts.CHANGE_BALANCE_MODIFIER);
   }

   public static void reserveCallJMS(String from, String to, long requestId) throws Exception {
      CallSessionRequestDTO dto = new CallSessionRequestDTO(CallSessionRequestType.RESERVE, from, to, 30);
      dto.setRequestid(requestId);
      ms.sendObjectMessage(sessionReq, dto, AppConsts.REQUEST_SESSION_MODIFIER);
   }
}
