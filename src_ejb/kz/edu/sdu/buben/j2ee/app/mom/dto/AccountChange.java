package kz.edu.sdu.buben.j2ee.app.mom.dto;

import java.io.Serializable;

public class AccountChange implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5470538498301446889L;
	AccountChangeType type;
	Integer pk;

	public AccountChange(AccountChangeType type, Integer pk) {
		this.type = type;
		this.pk = pk;
	}

	public AccountChangeType getType() {
		return type;
	}

	public void setType(AccountChangeType type) {
		this.type = type;
	}

	public Integer getPk() {
		return pk;
	}

	public void setPk(Integer pk) {
		this.pk = pk;
	}

}
