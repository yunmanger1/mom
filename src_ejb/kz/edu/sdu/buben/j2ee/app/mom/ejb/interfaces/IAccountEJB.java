package kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces;

import java.util.List;

import kz.edu.sdu.buben.j2ee.app.mom.db.AccountEntity;

public interface IAccountEJB {
	public int getAccountIdByNumber(String phoneNumber);

	public List<AccountEntity> getAccountList();
}
