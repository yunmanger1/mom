package kz.edu.sdu.buben.j2ee.app.mom.ejb;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.Queue;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import kz.edu.sdu.buben.j2ee.app.mom.AppConsts;
import kz.edu.sdu.buben.j2ee.app.mom.db.AccountEntity;
import kz.edu.sdu.buben.j2ee.app.mom.db.CallSession;
import kz.edu.sdu.buben.j2ee.app.mom.db.UnitsReserve;
import kz.edu.sdu.buben.j2ee.app.mom.dto.ChargeSessionDTO;
import kz.edu.sdu.buben.j2ee.app.mom.dto.DeleteSessionDTO;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.LIAccountEJB;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.LIBalanceEJB;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.LIMessagingService;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.LISessionEJB;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.RISessionEJB;
import kz.edu.sdu.buben.j2ee.app.mom.utils.SessionUtils;

import org.apache.log4j.Logger;

@Stateless(name = "SessionEJB")
public class SessionEJB implements RISessionEJB, LISessionEJB {
   private final Logger log = Logger.getLogger(getClass());

   @PersistenceContext(unitName = "mom")
   EntityManager em;

   @EJB
   LIMessagingService ms;

   @EJB
   LIAccountEJB accountEjb;

   @EJB
   LIBalanceEJB balanceEjb;

   @Resource(mappedName = AppConsts.CHANGES_QUEUE_NAME)
   Queue changesQueue;

   @Resource(mappedName = AppConsts.EVENT_QUEUE_NAME)
   Queue eventQueue;

   private BigDecimal calculateCostOf(AccountEntity account, long millisecs) {
      // TODO: Pricing plan
      return BigDecimal.valueOf(millisecs / 1000);
   }

   private CallSession getActiveCallSession(AccountEntity from, AccountEntity to) {
      CallSession cs = null;
      try {
         Query q = em.createQuery("SELECT cs FROM CallSession cs WHERE cs.from = :from AND cs.to = :to AND cs.status = :status").setParameter("from", from).setParameter("to", to).setParameter("status", AppConsts.ACTIVE_SESSION_STATUS);
         cs = (CallSession) q.getSingleResult();
      } catch (NoResultException e) {

      }
      return cs;
   }

   private CallSession getOrCreateActiveCallSession(AccountEntity from, AccountEntity to) {
      CallSession cs = getActiveCallSession(from, to);
      if (cs == null) {
         cs = createActiveCallSession(from, to);
      }
      return cs;
   }

   private CallSession createActiveCallSession(AccountEntity from, AccountEntity to) {
      CallSession cs = new CallSession();
      cs.setFrom(from);
      cs.setTo(to);
      cs.setStartDate(new Date());
      cs.setStatus(AppConsts.ACTIVE_SESSION_STATUS);
      return cs;
   }

   @Override
   public boolean reserveCallSession(String fromNumber, String toNumber, int seconds) {
      AccountEntity from = accountEjb.getOrCreateAccountByNumber(fromNumber);
      AccountEntity to = accountEjb.getOrCreateAccountByNumber(toNumber);
      if (from != null && to != null) {
         return reserveCallSession(from, to, seconds);
      }
      return false;
   }

   public boolean reserveCallSession(AccountEntity from, AccountEntity to, int seconds) {
      BigDecimal cost = calculateCostOf(from, seconds * 1000);
      return reserveCallSession(from, to, seconds, cost);
   }

   public boolean reserveCallSession(AccountEntity from, AccountEntity to, long millisecs, BigDecimal units) {
      millisecs *= 1000;
      CallSession cs = getActiveCallSession(from, to);
      if (cs != null) {
         SessionUtils.calculateCurDuration(cs);
         long durationNow = cs.getDuration();
         if (cs.getReservedDuration() > durationNow) {
            log.debug(String.format("Reserve units is early for: %d seconds", cs.getReservedDuration() - durationNow));
            return true;
         }

      } else {
         cs = createActiveCallSession(from, to);
      }
      BigDecimal avail = from.getBalance().subtract(balanceEjb.getReservedUnits(from.getAccount_id()));
      if (avail.compareTo(units) >= 0) {
         UnitsReserve rs = cs.getReserve();
         if (rs == null) {
            rs = new UnitsReserve();
            rs.setAccount(from);
            rs.setStatus(cs.getStatus());
         }
         rs.setReservedUnits(rs.getReservedUnits().add(units));
         cs.setReservedDuration(cs.getReservedDuration() + millisecs);
         em.persist(rs);
         cs.setReserve(rs);
         cs.setReserveEndDate(new Date(cs.getStartDate().getTime() + cs.getReservedDuration()));
         em.persist(cs);
         log.debug(String.format("Reserve units for CallSession: from=%s, to=%s, units=%.2f, seconds=%s", cs.getFrom().getPhone_number(), cs.getTo().getPhone_number(), units, millisecs));
         return true;
      } else {
         overCallSession(from, to);
      }
      return false;
   }

   @Override
   public void chargeCallSession(int sessionId) {
      CallSession cs = em.find(CallSession.class, sessionId);
      if (cs != null) {
         long millisecs = 0;
         if (cs.getStatus() == AppConsts.OVER_SESSION_STATUS) {
            millisecs = SessionUtils.getNotChargedDuration(cs);
            cs.setStatus(AppConsts.DELETED_SESSION_STATUS);
            // NOW RESERVE STATUS IS CHANGED, AND WILL NOT BE COUNTED IN COUNTING AVAILABLE BALANCE
            cs.getReserve().setStatus(AppConsts.DELETED_SESSION_STATUS);
         } else if (cs.getStatus() == AppConsts.ACTIVE_SESSION_STATUS) {
            SessionUtils.calculateCurDuration(cs);
            millisecs = SessionUtils.getNotChargedDuration(cs);
         } else {
            log.error(String.format("Invalid call session status: %d", cs.getStatus()));
            return;
         }
         BigDecimal chargeUnits = calculateCostOf(cs.getFrom(), millisecs);
         balanceEjb.changeBalance(cs.getFrom(), chargeUnits.negate());
         cs.setChargedDuration(cs.getChargedDuration() + millisecs);
         cs.setChargedUnits(cs.getChargedUnits().add(chargeUnits));
         cs.setChargeDate(new Date());
         em.persist(cs.getReserve());
         em.persist(cs);
         log.debug(String.format("Charge CallSession: cs=%d, from=%s, to=%s, %.2f", cs.getSessionId(), cs.getFrom().getPhone_number(), cs.getTo().getPhone_number(), chargeUnits));
         DeleteSessionDTO dto = new DeleteSessionDTO();
         dto.setPk(cs.getSessionId());
         dto.setType(AppConsts.SESSION_TYPE_CALL);
         if (!ms.sendObjectMessage(changesQueue, dto, AppConsts.DELETE_SESSION_MODIFIER)) {
            log.error(String.format("Error sending DELETE_SESSION: %d", cs.getSessionId()));
         }
      } else {
         log.error(String.format("No such session: %d", sessionId));
      }
   }

   @Override
   public void chargeSession(int sessionType, int sessionId) {
      switch (sessionType) {
         case AppConsts.SESSION_TYPE_CALL :
            chargeCallSession(sessionId);
            break;

         case AppConsts.SESSION_TYPE_G3 :
            log.warn("3G session are not implemented yet");
            //TODO: chargeG3Session(sessionId);
            break;
         default :
            break;
      }
   }

   @Override
   public void overCallSession(String fromNumber, String toNumber) {
      overCallSessionOnDate(fromNumber, toNumber, new Date());
   }

   @Override
   public void overCallSessionOnDate(String fromNumber, String toNumber, Date endDate) {
      AccountEntity from = accountEjb.getOrCreateAccountByNumber(fromNumber);
      AccountEntity to = accountEjb.getOrCreateAccountByNumber(toNumber);
      overCallSessionOnDate(from, to, endDate);
   }

   public void overCallSessionOnDate(AccountEntity from, AccountEntity to, Date endDate) {
      CallSession cs = getActiveCallSession(from, to);
      if (cs != null) {
         overCallSession(cs, endDate);
      }
   }

   public void overCallSession(AccountEntity from, AccountEntity to) {
      overCallSessionOnDate(from, to, new Date());
   }

   private void overCallSession(CallSession cs, java.util.Date endDate) {
      log.debug(String.format("over CallSession: from=%s, to=%s, date=%s", cs.getFrom().getPhone_number(), cs.getTo().getPhone_number(), endDate));
      cs.setStatus(AppConsts.OVER_SESSION_STATUS);
      // RESERVE STATUS STAYS ACTIVE, UNITS ARE NOT CHARGED YET
      cs.setEndDate(endDate);
      SessionUtils.calculateDuration(cs);
      em.persist(cs);
      // send message for charge
      ChargeSessionDTO dto = new ChargeSessionDTO();
      dto.setSession_id(cs.getSessionId());
      dto.setSession_type(AppConsts.SESSION_TYPE_CALL);
      if (!ms.sendObjectMessage(eventQueue, dto, AppConsts.CHARGE_SESSION_MODIFIER)) {
         log.error(String.format("Error sending CHARGE_SESSION: %d", cs.getSessionId()));
      }
   }

   @Override
   public List<CallSession> getActiveCallSessionList() {
      return em.createQuery("SELECT cs FROM CallSession cs WHERE cs.status = :active").setParameter("active", AppConsts.ACTIVE_SESSION_STATUS).getResultList();
   }

   @Override
   public List<CallSession> getActiveExpiredCallSessionList() {
      return em.createQuery("SELECT cs FROM CallSession cs WHERE cs.reserveEndDate <= :now AND cs.status = :active").setParameter("now", new Date()).setParameter("active", AppConsts.ACTIVE_SESSION_STATUS).getResultList();
   }

}
