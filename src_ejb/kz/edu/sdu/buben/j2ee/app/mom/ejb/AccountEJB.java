package kz.edu.sdu.buben.j2ee.app.mom.ejb;

import java.math.BigDecimal;
import java.util.List;

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
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.LIMessagingService;
import kz.edu.sdu.buben.j2ee.app.mom.utils.AccountUtils;

import org.apache.log4j.Logger;

@Stateless
public class AccountEJB implements LIAccountEJB {
   private final Logger log = Logger.getLogger(getClass());

   @PersistenceContext(unitName = "mom")
   EntityManager em;

   @EJB
   LIMessagingService ms;

   @Resource(mappedName = AppConsts.CHANGES_QUEUE_NAME)
   Queue changesQueue;

   @Override
   public int getAccountIdByNumber(String phoneNumber) {
      Query q = em.createQuery("select a.account_id from AccountEntity a where a.phone_number = :phone").setParameter("phone", phoneNumber);
      Integer result = (Integer) q.getSingleResult();
      return result.intValue();
   }

   @Override
   public List<AccountEntity> getAccountList() {
      Query q = em.createQuery("select a from AccountEntity a");
      return q.getResultList();
   }

   @Override
   public boolean isValidPhoneNumber(String phoneNumber) {
      if (AccountUtils.validatePhoneNumber(phoneNumber)) {
         Integer result = (Integer) em.createQuery("select a.account_id from AccountEntity a where a.phone_number = :phone").setParameter("phone", phoneNumber).getSingleResult();
         if (result != null) {
            return true;
         }
      }
      return false;
   }

   @Override
   public AccountEntity createAccount(String phoneNumber, BigDecimal balance) {
      if (!AccountUtils.validatePhoneNumber(phoneNumber)) {
         log.error(String.format("Invalid phone number: %s", phoneNumber));
         return null;
      }
      AccountEntity na = new AccountEntity();
      na.setPhone_number(phoneNumber);
      na.setBalance(balance);
      em.persist(na);
      em.refresh(na);
      log.debug(String.format("Create new Account with balance: %s, balance=%.2f", phoneNumber, balance));
      AccountChangeDTO dto = new AccountChangeDTO();
      dto.setPk(na.getAccount_id());
      dto.setType(AppConsts.CREATED_ACCOUNT_CHANGE);
      if (!ms.sendObjectMessage(changesQueue, dto, AppConsts.CHANGE_ACCOUNT_MODIFIER)) {
         log.error(String.format("Error sending CHANGE_ACCOUNT: %d", na.getAccount_id()));
      }
      return na;
   }

   @Override
   public AccountEntity createAccount(String phoneNumber) {
      return createAccount(phoneNumber, BigDecimal.valueOf(0));
   }

   @Override
   public AccountEntity getOrCreateAccountByNumber(String phoneNumber) {
      Query q = em.createQuery("select a from AccountEntity a where a.phone_number = :phone").setParameter("phone", phoneNumber);
      AccountEntity result = null;
      try {
         result = (AccountEntity) q.getSingleResult();
      } catch (Exception e) {
         result = createAccount(phoneNumber);
      }
      return result;
   }
}
