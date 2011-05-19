package kz.edu.sdu.buben.j2ee.app.mom.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import kz.edu.sdu.buben.j2ee.app.mom.db.AccountEntity;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.LIAccountEJB;
import kz.edu.sdu.buben.j2ee.app.mom.utils.AccountUtils;

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
						"select a.account_id from AccountEntity a where a.phone_number = :phone")
				.setParameter("phone", phoneNumber);
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
			Integer result = (Integer) em
					.createQuery(
							"select a.account_id from AccountEntity a where a.phone_number = :phone")
					.setParameter("phone", phoneNumber).getSingleResult();
			if (result != null) {
				return true;
			}
		}
		return false;
	}
}
