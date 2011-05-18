package kz.edu.sdu.buben.j2ee.app.mom.ejb;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.management.InvalidAttributeValueException;

import kz.edu.sdu.buben.j2ee.app.mom.AppProps;
import kz.edu.sdu.buben.j2ee.app.mom.dto.ChangeBalanceDTO;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.LIBalanceChangeEJB;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.LIDbEJB;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.LIMessagingService;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.MessageModifier;
import kz.edu.sdu.buben.j2ee.app.mom.utils.AccountUtils;

import org.apache.log4j.Logger;

@Stateless
public class BalanceChangeEJB implements LIBalanceChangeEJB {
	private final Logger log = Logger.getLogger(getClass());

	// @PersistenceContext(unitName = "mom")
	// EntityManager em;

	@EJB
	LIDbEJB db;

	@EJB
	LIMessagingService ms;

	@Resource(mappedName = AppProps.BALANCE_QUEUE_NAME)
	private Queue destination;

	public boolean sendChangeBalance(ChangeBalanceDTO o) {
		return ms.sendObjectMessage(destination, o, new MessageModifier() {
			@Override
			public void modify(Message message) throws JMSException {
				message.setStringProperty(AppProps.MESSAGE_TYPE,
						AppProps.CHANGE_BALANCE_MT);
				message.setJMSDeliveryMode(DeliveryMode.PERSISTENT);
			}
		});
	}

	@Override
	public boolean changeBalance(ChangeBalanceDTO dto) {
		if (AccountUtils.validatePhoneNumber(dto.getPhoneNumber())
				&& AccountUtils.validateDelta(dto.getDelta())) {
			db.changeBalance(dto.getPhoneNumber(), dto.getDelta());
			return true;
		}
		return false;
	}

	@Override
	public void sayToChangeBalance(String phoneNumber, BigDecimal delta)
			throws InvalidAttributeValueException {
		if (AccountUtils.validatePhoneNumber(phoneNumber)
				&& AccountUtils.validateDelta(delta)) {
			ChangeBalanceDTO o = new ChangeBalanceDTO();
			o.setPhoneNumber(phoneNumber);
			o.setDelta(delta);
			sendChangeBalance(o);
		} else {
			throw new InvalidAttributeValueException("Phone number is invalid");
		}
	}

	@Override
	public void saySthStupid() {
		ms.sendObjectMessage(destination, "stupid");
	}

}
