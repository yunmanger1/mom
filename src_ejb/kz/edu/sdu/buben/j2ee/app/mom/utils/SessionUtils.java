package kz.edu.sdu.buben.j2ee.app.mom.utils;

import kz.edu.sdu.buben.j2ee.app.mom.db.CallSession;
import kz.edu.sdu.buben.j2ee.app.mom.dto.CallSessionRequestDTO;
import kz.edu.sdu.buben.j2ee.app.mom.dto.ResponseDTO;
import kz.edu.sdu.buben.j2ee.app.mom.dto.ResponseType;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.ISessionEJB;

import org.apache.log4j.Logger;

public class SessionUtils {
   private static final Logger log = Logger.getLogger(SessionUtils.class);

   public static void calculateDuration(CallSession cs) {
      if (cs.getStartDate() != null && cs.getEndDate() != null) {
         cs.setDuration(cs.getEndDate().getTime() - cs.getStartDate().getTime());
      } else {
         log.error(String.format("One of dates is null: start = %s, end = %s", cs.getStartDate(), cs.getEndDate()));
      }
   }

   public static long getNotChargedDuration(CallSession cs) {
      return (cs.getDuration() - cs.getChargedDuration());
   }

   public static void calculateCurDuration(CallSession cs) {
      if (cs.getStartDate() != null) {
         cs.setDuration((new java.util.Date()).getTime() - cs.getStartDate().getTime());
      } else {
         log.error(String.format("Start date is null: start = %s", cs.getStartDate()));
      }

   }

   public static ResponseDTO requestSession(ISessionEJB sessionEjb, CallSessionRequestDTO dto) {
      ResponseDTO rdto = new ResponseDTO();
      boolean result = true;
      switch (dto.getRequesttype()) {
         case RESERVE :
            result = sessionEjb.reserveCallSession(dto.getFromnumber(), dto.getTonumber(), dto.getReserveseconds());
            break;
         case OVER :
            sessionEjb.overCallSession(dto.getFromnumber(), dto.getTonumber());
            break;
         default :
            break;
      }
      if (result) {
         rdto.setResult(ResponseType.OK);
      } else {
         rdto.setResult(ResponseType.FALSE);
      }
      return rdto;
   }
}
