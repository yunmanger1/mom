package kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces;

import javax.jms.JMSException;
import javax.jms.Message;

public interface MessageModifier {

	public void modify(Message message) throws JMSException;
}
