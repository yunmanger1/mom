package kz.edu.sdu.buben.j2ee.app.mom.dto;

public class ResponseDTO {
   public static final String ALIAS = "response";
   ResponseType result;
   Long requestid = Long.valueOf(0);

   public ResponseType getResult() {
      return result;
   }

   public void setResult(ResponseType result) {
      this.result = result;
   }

   public Long getRequestid() {
      return requestid;
   }

   public void setRequestid(Long requestid) {
      this.requestid = requestid;
   }

}
