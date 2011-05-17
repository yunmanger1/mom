package kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces;

import java.io.Serializable;

import javax.jms.Destination;

public interface IMessagingService {
	public boolean sendObjectMessage(Destination destination,
			Serializable object);

	public boolean sendObjectMessage(Destination destination,
			Serializable object, MessageModifier modifier);
}
