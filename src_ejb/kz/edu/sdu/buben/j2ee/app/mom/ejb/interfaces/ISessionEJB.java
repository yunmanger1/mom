package kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces;

import java.util.List;

import kz.edu.sdu.buben.j2ee.app.mom.db.CallSession;

public interface ISessionEJB {

   public boolean reserveCallSession(String fromNumber, String toNumber, int seconds);

   public List<CallSession> getActiveCallSessionList();

   public void chargeSession(int sessionType, int sessionId);

   public void chargeCallSession(int sessionId);

   public void overCallSession(String fromNumber, String toNumber);

   public void overCallSessionOnDate(String fromNumber, String toNumber, java.util.Date endDate);

}
