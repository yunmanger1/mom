package kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces;

public interface IChangesEJB {

   public void moveCallSession(int sessionId);

   public void moveSession(int sessionType, int sessionId);

   public void copyAccountState(int accountId);

   public void copyAccountStateOnly(int accountId, String changeType);
}
