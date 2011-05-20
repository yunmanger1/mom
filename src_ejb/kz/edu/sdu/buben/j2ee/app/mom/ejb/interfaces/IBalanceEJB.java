package kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces;

import java.math.BigDecimal;

import kz.edu.sdu.buben.j2ee.app.mom.db.AccountEntity;

public interface IBalanceEJB {

   public BigDecimal changeBalance(int id, BigDecimal delta);

   public BigDecimal changeBalance(AccountEntity account, BigDecimal delta);

   public BigDecimal changeBalance(String phoneNumber, BigDecimal delta);

}
