package kz.edu.sdu.buben.j2ee.app.mom.dto;

public class ResponseDTO {
   public static final String ALIAS = "response";
   ResponseType result;

   public ResponseType getResult() {
      return result;
   }

   public void setResult(ResponseType result) {
      this.result = result;
   }

}
