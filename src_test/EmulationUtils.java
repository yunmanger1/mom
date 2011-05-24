import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;

import kz.edu.sdu.buben.j2ee.app.mom.dto.BalanceRequestDTO;
import kz.edu.sdu.buben.j2ee.app.mom.dto.BalanceRequestType;
import kz.edu.sdu.buben.j2ee.app.mom.dto.CallSessionRequestDTO;
import kz.edu.sdu.buben.j2ee.app.mom.dto.CallSessionRequestType;
import kz.edu.sdu.buben.j2ee.app.mom.utils.XStreamUtils;

public class EmulationUtils {
   private static final XStreamUtils ju = new XStreamUtils();
   private static final String CALL_URL = "http://localhost:8080/mom/call/";
   private static final String BALANCE_URL = "http://localhost:8080/mom/balance/";
   static {
      ju.a(CallSessionRequestDTO.ALIAS, CallSessionRequestDTO.class);
      ju.a(BalanceRequestDTO.ALIAS, BalanceRequestDTO.class);
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

   public static void addBalance(String from, int amount) throws Exception {
      BalanceRequestDTO dto = new BalanceRequestDTO(BalanceRequestType.ADD, from, BigDecimal.valueOf(amount));
      String xml = ju.toXml(dto).replaceAll("\n", "").replaceAll(">[ ]+", ">");
      String cmd = String.format("curl --data %s %s", xml, BALANCE_URL);
      exec(cmd);
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
}
