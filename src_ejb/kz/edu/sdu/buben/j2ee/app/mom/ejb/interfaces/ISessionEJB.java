package kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces;

import kz.edu.sdu.buben.j2ee.app.mom.db.AccountEntity;

public interface ISessionEJB {

   public boolean reserveCallSession(String fromNumber, String toNumber, int seconds);

   public boolean reserveCallSession(AccountEntity from, AccountEntity to, int seconds);

   public void chargeSession(int sessionType, int sessionId);

   public void chargeCallSession(int sessionId);

   public void overCallSession(String fromNumber, String toNumber);

   public void overCallSession(AccountEntity from, AccountEntity to);

   public void overCallSessionOnDate(String fromNumber, String toNumber, java.util.Date endDate);

   public void overCallSessionOnDate(AccountEntity from, AccountEntity to, java.util.Date endDate);

}
