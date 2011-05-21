package kz.edu.sdu.buben.j2ee.app.mom.sar;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.List;

import kz.bips.comps.threads.MultiScheduleWorkingThread;
import kz.bips.comps.threads.ScheduleRecord;
import kz.bips.comps.utils.Log4JLoggerWrapper;
import kz.edu.sdu.buben.j2ee.app.mom.AppProps;

import org.jboss.ejb3.annotation.Depends;
import org.jboss.ejb3.annotation.Management;
import org.jboss.ejb3.annotation.Service;

/**
 * Created 09.09.2008 11:50:00
 * @author Victor Pyankov
 */
@Service
@Management(ITestServiceManagement.class)
@Depends({"jboss.j2ee:ear=mom.ear,jar=mom.jar,name=SessionEJB,service=EJB3"})
//@Depends({"jboss.jca:name=IrisImpDealsDS,service=DataSourceBinding", "jboss.jca:name=IrisImpCSOraDS,service=DataSourceBinding"})
public class ITestService implements ITestServiceManagement {
   protected Log4JLoggerWrapper log = new Log4JLoggerWrapper(getClass());

   private MultiScheduleWorkingThread mainThread = null;

   // Lifecycle methods
   public void create() throws Exception {
      log.debug("TestService: Creating...");
   }

   //@TransactionAttribute(TransactionAttributeType.NEVER)
   public void start() throws Exception {
      log.info("TestService: Starting...");

      List<ScheduleRecord> schedules = new ArrayList<ScheduleRecord>();
      schedules.add(new CallSessionScheduleRecord());
      mainThread = new MultiScheduleWorkingThread("TestThread", AppProps.getProps(), schedules);
      mainThread.start();
   }

   public void stop() {
      log.info("TestService: Stopping...");
      if (mainThread != null && mainThread.isAlive()) {
         log.info("Thread reporting that it is alive");

         // TODO For test only BEG
         log.info("Thread state: %s", mainThread.getState());
         log.info("Thread interrupted: %b", mainThread.isInterrupted());
         UncaughtExceptionHandler h = mainThread.getUncaughtExceptionHandler();
         if (h == null) {
            log.info("Handler is null");
         } else {
            log.info("Handler: %s", h.toString());
         }

         // TODO For test only END

         mainThread.safeInterruptWithJoin();
         mainThread = null;
         log.info("TestThread stopped");
      }
      log.info("TestService: Stopped...");
   }

   public void destroy() {
      log.debug("TestService: Destroying...");
   }
}
