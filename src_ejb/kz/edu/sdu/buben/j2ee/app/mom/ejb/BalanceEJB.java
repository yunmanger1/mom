package kz.edu.sdu.buben.j2ee.app.mom.ejb;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.Queue;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import kz.edu.sdu.buben.j2ee.app.mom.AppConsts;
import kz.edu.sdu.buben.j2ee.app.mom.db.AccountEntity;
import kz.edu.sdu.buben.j2ee.app.mom.dto.AccountChangeDTO;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.LIAccountEJB;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.LIBalanceEJB;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.LIMessagingService;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.RIBalanceEJB;

import org.apache.log4j.Logger;

@Stateless
public class BalanceEJB implements LIBalanceEJB, RIBalanceEJB {
   private final Logger log = Logger.getLogger(getClass());

   @PersistenceContext(unitName = "mom")
   EntityManager em;

   @EJB
   LIMessagingService ms;

   @EJB
   LIAccountEJB accountEjb;

   @Resource(mappedName = AppConsts.CHANGES_QUEUE_NAME)
   Queue changesQueue;

//   @Resource(mappedName = AppConsts.EVENT_QUEUE_NAME)
//   Queue eventQueue;

   @Override
   public BigDecimal changeBalance(AccountEntity ac, BigDecimal delta) {
      ac.setBalance(ac.getBalance().add(delta));
      em.persist(ac);
      AccountChangeDTO dto = new AccountChangeDTO();
      dto.setPk(ac.getAccount_id());
      dto.setType(AppConsts.BALANCE_ACCOUNT_CHANGE);
      log.debug(String.format("Charge Balance: account=%s, units=%.2f", ac.getPhone_number(), delta));
      if (!ms.sendObjectMessage(changesQueue, dto, AppConsts.CHANGE_ACCOUNT_MODIFIER)) {
         log.error(String.format("Error sending CHANGE_ACCOUNT: %d", ac.getAccount_id()));
      }
      return ac.getBalance();
   }

   @Override
   public BigDecimal changeBalance(int id, BigDecimal delta) {
      AccountEntity ac = em.find(AccountEntity.class, id);
      return changeBalance(ac, delta);
   }

   @Override
   public BigDecimal changeBalance(String phoneNumber, BigDecimal delta) {
      AccountEntity ac = accountEjb.getOrCreateAccountByNumber(phoneNumber);
      if (ac != null) {
         return changeBalance(ac, delta);
      } else {
         log.error(String.format("No account for: %s", phoneNumber));
      }
      return null;
   }

   public BigDecimal getReservedUnits(String phoneNumber) {
      AccountEntity ac = accountEjb.getOrCreateAccountByNumber(phoneNumber);
      if (ac != null) {
         return getReservedUnits(ac);
      }
      return null;
   }

   private BigDecimal getReservedUnits(AccountEntity account) {
      Query q = em.createQuery("SELECT SUM(ur.reservedUnits) FROM UnitsReserve ur WHERE ur.status = :active AND ur.account = :account").setParameter("active", AppConsts.ACTIVE_SESSION_STATUS).setParameter("account", account);
      BigDecimal result = (BigDecimal) q.getSingleResult();
      if (result == null) {
         result = BigDecimal.ZERO;
      }
      return result;
   }

   @Override
   public BigDecimal getReservedUnits(int accountId) {
      AccountEntity ac = accountEjb.getAccountById(accountId);
      if (ac != null) {
         return getReservedUnits(ac);
      }
      return null;
   }

   @Override
   public BigDecimal getAvailableUnits(String phoneNumber) {
      AccountEntity ac = accountEjb.getOrCreateAccountByNumber(phoneNumber);
      if (ac != null) {
         return getAvailableUnits(ac);
      }
      return null;
   }

   private BigDecimal getAvailableUnits(AccountEntity ac) {
      return ac.getBalance().subtract(getReservedUnits(ac));
   }

   @Override
   public BigDecimal getAvailableUnits(int accountId) {
      AccountEntity ac = accountEjb.getAccountById(accountId);
      if (ac != null) {
         return getAvailableUnits(ac);
      }
      return null;
   }

}
