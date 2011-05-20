package kz.edu.sdu.buben.j2ee.app.mom.db;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import kz.edu.sdu.buben.j2ee.app.mom.AppConsts;

@Entity
@Table(name = "reservation")
public class UnitsReserve {
	protected int reservationId;
	protected AccountEntity account;
	protected BigDecimal reservedUnits = BigDecimal.ZERO;
	protected int status = AppConsts.BEFORE_SESSION_STATUS;

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getReservationId() {
		return reservationId;
	}

	public void setReservationId(int reservationId) {
		this.reservationId = reservationId;
	}

	@ManyToOne
	public AccountEntity getAccount() {
		return account;
	}

	public void setAccount(AccountEntity account) {
		this.account = account;
	}

	@Column
	public BigDecimal getReservedUnits() {
		return reservedUnits;
	}

	public void setReservedUnits(BigDecimal reservedUnits) {
		this.reservedUnits = reservedUnits;
	}

	@Column
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
