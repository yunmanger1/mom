package kz.edu.sdu.buben.j2ee.app.mom.ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.LIAccountEJB;

import org.apache.log4j.Logger;

@Stateless
public class AccountEJB implements LIAccountEJB {
	private final Logger log = Logger.getLogger(getClass());

	@PersistenceContext(unitName = "mom")
	EntityManager em;

	@Override
	public int getAccountIdByNumber(String phoneNumber) {
		Query q = em
				.createQuery(
						"select a.account_id from AccountEntity a where a.phone_number = :1")
				.setParameter(1, phoneNumber);
		Integer result = (Integer) q.getSingleResult();
		return result.intValue();
	}

}
