package kz.edu.sdu.buben.j2ee.app.mom.ejb;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import kz.edu.sdu.buben.j2ee.app.mom.AppProps;
import kz.edu.sdu.buben.j2ee.app.mom.db.AccountEntity;
import kz.edu.sdu.buben.j2ee.app.mom.dto.AccountChange;
import kz.edu.sdu.buben.j2ee.app.mom.dto.AccountChangeType;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.LIDbEJB;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.LIMessagingService;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.MessageModifier;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.RIDbEJB;
import kz.edu.sdu.buben.j2ee.app.mom.utils.AccountUtils;

import org.apache.log4j.Logger;

@Stateless
public class DbEJB implements RIDbEJB, LIDbEJB {
	private final Logger log = Logger.getLogger(getClass());

	@PersistenceContext(unitName = "mom")
	EntityManager em;

	@EJB
	LIMessagingService ms;

	@Resource(mappedName = AppProps.CHANGES_QUEUE_NAME)
	Queue destination;

	@Override
	public BigDecimal changeBalance(AccountEntity ac, BigDecimal delta) {
		ac.setBalance(ac.getBalance().add(delta));
		em.persist(ac);
		ms.sendObjectMessage(destination, new AccountChange(
				AccountChangeType.BALANCE, ac.getAccount_id()),
				new MessageModifier() {

					@Override
					public void modify(Message message) throws JMSException {
						message.setStringProperty(AppProps.MESSAGE_TYPE,
								AppProps.CHANGE_ACCOUNT_MT);
					}
				});
		return ac.getBalance();
	}

	@Override
	public BigDecimal changeBalance(int id, BigDecimal delta) {
		AccountEntity ac = em.find(AccountEntity.class, id);
		return changeBalance(ac, delta);
	}

	@Override
	public BigDecimal changeBalance(String phoneNumber, BigDecimal delta) {
		log.debug(String.format("Modify balance of: %s by: %s", phoneNumber,
				delta.toString()));
		AccountEntity ac = getOrCreateAccountByNumber(phoneNumber);
		if (ac != null) {
			return changeBalance(ac, delta);
		} else {
			log.error(String.format("No account for: %s", phoneNumber));
		}
		return null;
	}

	@Override
	public AccountEntity createAccount(String phoneNumber, BigDecimal balance) {
		if (!AccountUtils.validatePhoneNumber(phoneNumber)) {
			log.error(String.format("Invalid phone number: %s", phoneNumber));
			return null;
		}
		AccountEntity na = new AccountEntity();
		na.setPhone_number(phoneNumber);
		na.setBalance(balance);
		em.persist(na);
		em.refresh(na);
		ms.sendObjectMessage(destination, new AccountChange(
				AccountChangeType.CREATED, na.getAccount_id()),
				new MessageModifier() {

					@Override
					public void modify(Message message) throws JMSException {
						message.setStringProperty(AppProps.MESSAGE_TYPE,
								AppProps.CHANGE_ACCOUNT_MT);
					}
				});
		return na;
	}

	@Override
	public AccountEntity createAccount(String phoneNumber) {
		return createAccount(phoneNumber, BigDecimal.valueOf(0));
	}

	@Override
	public AccountEntity getOrCreateAccountByNumber(String phoneNumber) {
		Query q = em.createQuery(
				"select a from AccountEntity a where a.phone_number = :phone")
				.setParameter("phone", phoneNumber);
		AccountEntity result = null;
		try {
			result = (AccountEntity) q.getSingleResult();
		} catch (Exception e) {
			result = createAccount(phoneNumber);
		}
		return result;
	}

}
