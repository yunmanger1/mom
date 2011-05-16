package kz.edu.sdu.buben.j2ee.app.mom.ejb;

import java.math.BigDecimal;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import kz.edu.sdu.buben.j2ee.app.mom.db.AccountEntity;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.LIAccountEJB;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.LIDbEJB;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.RIDbEJB;

import org.apache.log4j.Logger;

@Stateless
public class DbEJB implements RIDbEJB, LIDbEJB {
	private final Logger log = Logger.getLogger(getClass());

	@PersistenceContext(unitName = "mom")
	EntityManager em;

	@EJB
	LIAccountEJB accounts;

	@Override
	public void changeBalance(int id, BigDecimal delta) {
		AccountEntity ac = em.find(AccountEntity.class, id);
		ac.setBalance(ac.getBalance().add(delta));
		em.persist(ac);
	}

	@Override
	public void changeBalance(String phoneNumber, BigDecimal delta) {
		int id = accounts.getAccountIdByNumber(phoneNumber);
		changeBalance(id, delta);
	}

	@Override
	public int createAccount(String phoneNumber, BigDecimal balance) {
		if (phoneNumber.length() != 10) {
			log.error("Invalid phone number");
			return -1;
		}
		AccountEntity na = new AccountEntity();
		na.setPhone_number(phoneNumber);
		na.setBalance(balance);
		em.persist(na);
		em.refresh(na);
		return na.getAccount_id();
	}

	@Override
	public int createAccount(String phoneNumber) {
		return createAccount(phoneNumber, BigDecimal.valueOf(0));
	}

}
