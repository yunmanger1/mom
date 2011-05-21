package kz.edu.sdu.buben.j2ee.app.mom.dto;

public class CallSessionRequestDTO {
   public static final String ALIAS = "callrequest";
   CallSessionRequestType requesttype;
   String fromnumber;
   String tonumber;
   Integer reserveseconds;

   public CallSessionRequestDTO() {

   }

   public CallSessionRequestDTO(CallSessionRequestType type, String from, String to) {
      this.requesttype = type;
      this.fromnumber = from;
      this.tonumber = to;
   }

   public CallSessionRequestDTO(CallSessionRequestType type, String from, String to, Integer seconds) {
      this(type, from, to);
      this.reserveseconds = seconds;
   }

   public CallSessionRequestType getRequesttype() {
      return requesttype;
   }

   public void setRequesttype(CallSessionRequestType requesttype) {
      this.requesttype = requesttype;
   }

   public String getFromnumber() {
      return fromnumber;
   }

   public void setFromnumber(String fromnumber) {
      this.fromnumber = fromnumber;
   }

   public String getTonumber() {
      return tonumber;
   }

   public void setTonumber(String tonumber) {
      this.tonumber = tonumber;
   }

   public Integer getReserveseconds() {
      return reserveseconds;
   }

   public void setReserveseconds(Integer reserveseconds) {
      this.reserveseconds = reserveseconds;
   }

}
