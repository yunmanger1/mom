package kz.edu.sdu.buben.j2ee.app.mom.utils;

import kz.edu.sdu.buben.j2ee.app.mom.db.CallSession;

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

   public static int getNotChargedDurationInSeconds(CallSession cs) {
      return (int) ((cs.getDuration() / 1000) - cs.getChargedDuration());
   }

   public static void calculateCurDuration(CallSession cs) {
      if (cs.getStartDate() != null) {
         cs.setDuration((new java.util.Date()).getTime() - cs.getStartDate().getTime());
      } else {
         log.error(String.format("Start date is null: start = %s", cs.getStartDate()));
      }

   }
}
