package kz.edu.sdu.buben.j2ee.app.mom.ejb;

import javax.ejb.Stateless;

import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.LISimpleEJB;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.RISimpleEJB;

import org.apache.log4j.Logger;

@Stateless
public class SimpleEJB implements RISimpleEJB, LISimpleEJB {
   private final Logger log = Logger.getLogger(getClass());

   @Override
   public void sayHello(String message) {
      System.out.println(message);
   }

}
