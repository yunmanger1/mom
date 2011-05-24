package kz.edu.sdu.buben.j2ee.app.mom.ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import kz.edu.sdu.buben.j2ee.app.mom.AppConsts;
import kz.edu.sdu.buben.j2ee.app.mom.db.CallSession;
import kz.edu.sdu.buben.j2ee.app.mom.db.CallSessionHistory;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.LIChangesEJB;

import org.apache.log4j.Logger;

@Stateless
public class ChangesEJB implements LIChangesEJB {
   private final Logger log = Logger.getLogger(getClass());

   @PersistenceContext(unitName = "mom")
   EntityManager em;

   @Override
   public void copyAccountState(int accountId) {
      // TODO Auto-generated method stub

   }

   @Override
   public void copyAccountStateOnly(int accountId, String changeType) {
      // TODO Auto-generated method stub

   }

   @Override
   public void moveCallSession(int sessionId) {
      try {
         CallSession cs = (CallSession) em.createQuery("SELECT cs FROM CallSession cs WHERE cs.status = :deleted AND cs.sessionId = :id").setParameter("id", sessionId).setParameter("deleted", AppConsts.DELETED_SESSION_STATUS).getSingleResult();
         CallSessionHistory csh = new CallSessionHistory();
         csh.setFrom(cs.getFrom());
         csh.setTo(cs.getTo());
         csh.setStartDate(cs.getStartDate());
         csh.setEndDate(cs.getEndDate());
         csh.setDuration(cs.getDuration());
         csh.setChargedDuration(cs.getChargedDuration());
         csh.setChargedUnits(cs.getChargedUnits());
         csh.setChargeDate(cs.getChargeDate());
         csh.setMoveDate(new java.util.Date());
         csh.setReservedDuration(cs.getReservedDuration());
         csh.setReserveEndDate(cs.getReserveEndDate());
         em.persist(csh);
         log.debug(String.format("Move CallSession to CallSessionHistory: csId=%d, cshId=%d", cs.getSessionId(), csh.getSessionId()));
         em.remove(cs);
      } catch (NoResultException e) {
         log.error(String.format("No such DEELTE sesssion: %d", sessionId));
      }
   }

   @Override
   public void moveSession(int sessionType, int sessionId) {
      switch (sessionType) {
         case AppConsts.SESSION_TYPE_CALL :
            moveCallSession(sessionId);
            break;

         case AppConsts.SESSION_TYPE_G3 :
//            moveG3Session(sessionId);
            break;

         default :
            break;
      }
   }

}
