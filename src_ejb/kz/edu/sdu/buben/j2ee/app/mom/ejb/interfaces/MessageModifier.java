package kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

public interface MessageModifier {

	public void modify(ObjectMessage message) throws JMSException;
}
