package kz.edu.sdu.buben.j2ee.app.mom.sar;

import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;

import kz.bips.comps.threads.ScheduleRecord;
import kz.bips.comps.utils.Log4JLoggerWrapper;
import kz.edu.sdu.buben.j2ee.app.mom.AppConsts;
import kz.edu.sdu.buben.j2ee.app.mom.db.CallSession;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.LISessionEJB;

public class CallSessionScheduleRecord extends ScheduleRecord {
   Log4JLoggerWrapper log = new Log4JLoggerWrapper(getClass());

   public CallSessionScheduleRecord() {
      super("callsession");
   }

//   @EJB
//   LISessionEJB sessionEjb;

   @Override
   protected void work() {
      try {
         Context context = new InitialContext();
         LISessionEJB sessionEjb = (LISessionEJB) context.lookup(AppConsts.LOCAL_NAME_SESSIONEJB);
         List<CallSession> list = sessionEjb.getActiveCallSessionList();
         int n = list.size();
         if (n > 0) {
            log.debug("SCHEDULER: %d active call session", n);
            for (int i = 0; i < n; i++) {
               CallSession cur = list.get(i);
               sessionEjb.reserveCallSession(cur.getFrom().getPhone_number(), cur.getTo().getPhone_number(), 30);
            }
         }
      } catch (Exception e) {
         log.logStackTrace(e, "");
      }
   }
}
