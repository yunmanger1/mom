package kz.edu.sdu.buben.j2ee.app.mom.sar;

import kz.bips.comps.threads.ScheduleRecord;

public class TestScheduleRecord extends ScheduleRecord {

   public TestScheduleRecord() {
      super("test");
   }

   @Override
   protected void work() {
      System.out.println("TEST THREAD!!!!!!!!!!!!!!!");
   }
}
