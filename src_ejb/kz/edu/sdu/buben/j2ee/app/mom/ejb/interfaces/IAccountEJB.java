package kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces;

import java.math.BigDecimal;
import java.util.List;

import kz.edu.sdu.buben.j2ee.app.mom.db.AccountEntity;

public interface IAccountEJB {
   public int getAccountIdByNumber(String phoneNumber);

   public AccountEntity getAccountById(int accountId);

   public List<AccountEntity> getAccountList();

   public boolean isValidPhoneNumber(String phoneNumber);

   public AccountEntity createAccount(String phoneNumber, BigDecimal balance);

   public AccountEntity createAccount(String phoneNumber);

   public AccountEntity getOrCreateAccountByNumber(String phoneNumber);

}
