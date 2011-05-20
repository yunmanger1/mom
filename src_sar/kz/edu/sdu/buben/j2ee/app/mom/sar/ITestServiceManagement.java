package kz.edu.sdu.buben.j2ee.app.mom.sar;

public interface ITestServiceManagement {
   void create() throws Exception;

   void start() throws Exception;

   void stop();

   void destroy();
}
