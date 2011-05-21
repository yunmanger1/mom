package kz.edu.sdu.buben.j2ee.app.mom.dto;

import java.math.BigDecimal;

public class BalanceResponseDTO extends ResponseDTO {
   public static final String ALIAS = "balanceresponse";
   BigDecimal value;

   public BigDecimal getValue() {
      return value;
   }

   public void setValue(BigDecimal value) {
      this.value = value;
   }

}
