package kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces;

import java.math.BigDecimal;

public interface IDbEJB {

	public int createAccount(String phoneNumber, BigDecimal balance);

	public int createAccount(String phoneNumber);

	public void changeBalance(int id, BigDecimal delta);

	public void changeBalance(String phoneNumber, BigDecimal delta);

}
