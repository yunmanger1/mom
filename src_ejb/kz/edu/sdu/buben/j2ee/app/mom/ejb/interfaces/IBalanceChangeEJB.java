package kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces;

import java.math.BigDecimal;

import javax.management.InvalidAttributeValueException;

import kz.edu.sdu.buben.j2ee.app.mom.dto.ChangeBalanceDTO;

public interface IBalanceChangeEJB {

	public void sayToChangeBalance(String phoneNumber, BigDecimal delta)
			throws InvalidAttributeValueException;

	public void saySthStupid();

	public boolean changeBalance(ChangeBalanceDTO dto);
}
