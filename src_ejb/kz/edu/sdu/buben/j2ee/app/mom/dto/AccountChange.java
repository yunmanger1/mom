package kz.edu.sdu.buben.j2ee.app.mom.dto;

import java.io.Serializable;

public class AccountChange implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5470538498301446889L;
	String type;
	Integer pk;

	public AccountChange(String type, Integer pk) {
		this.type = type;
		this.pk = pk;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getPk() {
		return pk;
	}

	public void setPk(Integer pk) {
		this.pk = pk;
	}

}
