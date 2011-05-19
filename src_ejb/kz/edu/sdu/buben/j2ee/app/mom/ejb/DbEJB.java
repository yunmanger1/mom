package kz.edu.sdu.buben.j2ee.app.mom.ejb;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import kz.edu.sdu.buben.j2ee.app.mom.AppProps;
import kz.edu.sdu.buben.j2ee.app.mom.db.AccountEntity;
import kz.edu.sdu.buben.j2ee.app.mom.db.CallSession;
import kz.edu.sdu.buben.j2ee.app.mom.db.UnitsReserve;
import kz.edu.sdu.buben.j2ee.app.mom.dto.AccountChange;
import kz.edu.sdu.buben.j2ee.app.mom.dto.ChargeSessionDTO;
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

	@Resource(mappedName = AppProps.EVENT_QUEUE_NAME)
	Queue eventQueue;

	@Override
	public BigDecimal changeBalance(AccountEntity ac, BigDecimal delta) {
		ac.setBalance(ac.getBalance().add(delta));
		em.persist(ac);
		ms.sendObjectMessage(destination, new AccountChange(
				AppProps.BALANCE_ACCOUNT_CHANGE, ac.getAccount_id()),
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
				AppProps.CREATED_ACCOUNT_CHANGE, na.getAccount_id()),
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

	private BigDecimal calculateCostOf(AccountEntity account, int seconds) {
		// TODO: Pricing plan
		return BigDecimal.valueOf(seconds);
	}

	private CallSession getActiveCallSession(AccountEntity from,
			AccountEntity to) {
		CallSession cs = null;
		try {
			Query q = em
					.createQuery(
							"SELECT cs FROM CallSession cs WHERE cs.from = :from AND cs.to = :to AND cs.status = :status")
					.setParameter("from", from).setParameter("to", to)
					.setParameter("status", AppProps.ACTIVE_SESSION_STATUS);
			// TODO: permanent fetch of reserve
			cs = (CallSession) q.getSingleResult();
		} catch (NoResultException e) {

		}
		return cs;
	}

	private CallSession getOrCreateActiveCallSession(AccountEntity from,
			AccountEntity to) {
		CallSession cs = getActiveCallSession(from, to);
		if (cs == null) {
			cs = new CallSession();
			cs.setFrom(from);
			cs.setTo(to);
			cs.setStatus(AppProps.ACTIVE_SESSION_STATUS);
		}
		return cs;
	}

	private BigDecimal getReservedUnits(AccountEntity account) {
		BigDecimal result = getCallSessionReserved(account);
		// TODO: 3G
		return result;
	}

	private BigDecimal getCallSessionReserved(AccountEntity account) {
		Query q = em
				.createQuery(
						"SELECT SUM(ur.reservedUnits) FROM UnitsReserve ur WHERE ur.status = :status AND ur.account = :account")
				.setParameter("status", AppProps.ACTIVE_SESSION_STATUS)
				.setParameter("account", account);
		BigDecimal result = (BigDecimal) q.getSingleResult();
		if (result == null) {
			result = BigDecimal.ZERO;
		}
		return result;
	}

	@Override
	public boolean reserveCallSession(String fromNumber, String toNumber,
			int seconds) {
		AccountEntity from = getOrCreateAccountByNumber(fromNumber);
		AccountEntity to = getOrCreateAccountByNumber(toNumber);
		if (from != null && to != null) {
			return reserveCallSession(from, to, seconds);
		}
		return false;
	}

	@Override
	public boolean reserveCallSession(AccountEntity from, AccountEntity to,
			int seconds) {
		BigDecimal cost = calculateCostOf(from, seconds);
		return reserveCallSession(from, to, cost);
	}

	@Override
	public boolean reserveCallSession(AccountEntity from, AccountEntity to,
			BigDecimal units) {
		BigDecimal avail = from.getBalance().subtract(getReservedUnits(from));
		if (avail.compareTo(units) >= 0) {
			CallSession cs = getOrCreateActiveCallSession(from, to);
			UnitsReserve rs = cs.getReserve();
			if (rs == null) {
				rs = new UnitsReserve();
				rs.setAccount(from);
				rs.setStatus(cs.getStatus());
			}
			rs.setReservedUnits(rs.getReservedUnits().add(units));
			em.persist(rs);
			cs.setReserve(rs);
			em.persist(cs);
			return true;
		} else {
			overSession(from, to);
		}
		return false;
	}

	private void overSession(AccountEntity from, AccountEntity to) {
		CallSession cs = getActiveCallSession(from, to);
		if (cs != null) {
			overSession(cs);
		}
	}

	private void overSession(CallSession cs) {
		cs.setStatus(AppProps.OVER_SESSION_STATUS);
		UnitsReserve ur = cs.getReserve();
		if (ur != null) {
			ur.setStatus(cs.getStatus());
		}
		em.persist(cs);
		em.persist(ur);
		// send message for charge
		ChargeSessionDTO dto = new ChargeSessionDTO();
		dto.setSession_id(cs.getSessionId());
		dto.setSession_type(ChargeSessionDTO.CALL_SESSION);
		ms.sendObjectMessage(eventQueue, dto, new MessageModifier() {

			@Override
			public void modify(Message message) throws JMSException {
				message.setStringProperty(AppProps.MESSAGE_TYPE,
						AppProps.CHARGE_SESSION_MT);
			}
		});
	}
}
