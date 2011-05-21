import java.math.BigDecimal;

import kz.edu.sdu.buben.j2ee.app.mom.dto.BalanceRequestDTO;
import kz.edu.sdu.buben.j2ee.app.mom.dto.BalanceRequestType;
import kz.edu.sdu.buben.j2ee.app.mom.dto.CallSessionRequestDTO;
import kz.edu.sdu.buben.j2ee.app.mom.dto.CallSessionRequestType;
import kz.edu.sdu.buben.j2ee.app.mom.dto.ChangeBalanceDTO;
import kz.edu.sdu.buben.j2ee.app.mom.utils.XStreamUtils;

import org.junit.Test;

public class TestStuff {

   @Test
   public void testXStreame() throws Exception {
      XStreamUtils ju = new XStreamUtils();
      ju.a("changebalance", ChangeBalanceDTO.class);
      ChangeBalanceDTO dto = new ChangeBalanceDTO();
      dto.setDelta(BigDecimal.valueOf(1500.256));
      dto.setPhoneNumber("7024476704");
      System.out.println(ju.toXml(dto));
      ChangeBalanceDTO dto2 = ju.fromXml(ju.toXml(dto), ChangeBalanceDTO.class);
      System.out.println(dto2.getPhoneNumber());
      System.out.println(dto2.getDelta());
   }

   @Test
   public void testCSDTO() throws Exception {
      CallSessionRequestDTO dto = new CallSessionRequestDTO(CallSessionRequestType.RESERVE, "7024476704", "701368535", 30);
      XStreamUtils ju = new XStreamUtils();
      ju.a(CallSessionRequestDTO.ALIAS, CallSessionRequestDTO.class);
      System.out.println(ju.toXml(dto));
      dto = new CallSessionRequestDTO(CallSessionRequestType.OVER, "7024476704", "701368535");
      System.out.println(ju.toXml(dto));

   }

   @Test
   public void testBDTO() throws Exception {
      BalanceRequestDTO dto = new BalanceRequestDTO(BalanceRequestType.CHARGE, "7024476704", BigDecimal.valueOf(100));
      XStreamUtils ju = new XStreamUtils();
      ju.a(BalanceRequestDTO.ALIAS, BalanceRequestDTO.class);
      System.out.println(ju.toXml(dto));
      dto = new BalanceRequestDTO(BalanceRequestType.AVAILABLE, "7024476704");
      System.out.println(ju.toXml(dto));

   }
//   @Test
//   public void testJox() throws Exception {
//      JoxUtils ju = new JoxUtils();
//      ChangeBalanceDTO dto = new ChangeBalanceDTO();
//      dto.setDelta(BigDecimal.valueOf(1500.256));
//      dto.setPhoneNumber("7024476704");
//      System.out.println(ju.toXml(dto));
//   }
}
