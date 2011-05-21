package kz.edu.sdu.buben.j2ee.app.mom.dto;

import java.math.BigDecimal;

public class BalanceRequestDTO {
   public static final String ALIAS = "balancerequest";

   BalanceRequestType requesttype;
   public String phonenumber;
   public BigDecimal units;

   public BalanceRequestDTO() {

   }

   public BalanceRequestDTO(BalanceRequestType type, String number) {
      this.requesttype = type;
      this.phonenumber = number;
   }

   public BalanceRequestDTO(BalanceRequestType type, String number, BigDecimal units) {
      this(type, number);
      this.units = units;
   }

   public BalanceRequestType getRequesttype() {
      return requesttype;
   }

   public void setRequesttype(BalanceRequestType requesttype) {
      this.requesttype = requesttype;
   }

   public String getPhonenumber() {
      return phonenumber;
   }

   public void setPhonenumber(String phonenumber) {
      this.phonenumber = phonenumber;
   }

   public BigDecimal getUnits() {
      return units;
   }

   public void setUnits(BigDecimal units) {
      this.units = units;
   }

}
