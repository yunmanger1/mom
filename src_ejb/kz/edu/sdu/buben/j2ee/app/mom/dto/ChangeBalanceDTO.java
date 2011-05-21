package kz.edu.sdu.buben.j2ee.app.mom.dto;

import java.math.BigDecimal;

public class ChangeBalanceDTO {
   public static final String ALIAS = "changebalance";

   public String phoneNumber;
   public BigDecimal delta;

   public String getPhoneNumber() {
      return phoneNumber;
   }

   public void setPhoneNumber(String phoneNumber) {
      this.phoneNumber = phoneNumber;
   }

   public BigDecimal getDelta() {
      return delta;
   }

   public void setDelta(BigDecimal delta) {
      this.delta = delta;
   }

}
