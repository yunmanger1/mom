package kz.edu.sdu.buben.j2ee.app.mom.db;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import kz.edu.sdu.buben.j2ee.app.mom.AppProps;

@Entity
@Table(name = "callsession")
public class CallSession {
	protected int sessionId;
	protected AccountEntity from;
	protected AccountEntity to;
	protected java.util.Date startDate;
	protected java.util.Date endDate;
	protected int duration = 0;
	protected int chargedDuration = 0;
	protected BigDecimal chargedUnits = BigDecimal.ZERO;
	protected UnitsReserve reserve;
	protected int status = AppProps.BEFORE_SESSION_STATUS;

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getSessionId() {
		return sessionId;
	}

	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}

	@ManyToOne
	public AccountEntity getFrom() {
		return from;
	}

	public void setFrom(AccountEntity from) {
		this.from = from;
	}

	@ManyToOne
	public AccountEntity getTo() {
		return to;
	}

	public void setTo(AccountEntity to) {
		this.to = to;
	}

	@Column
	@Temporal(value = TemporalType.TIMESTAMP)
	public java.util.Date getStartDate() {
		return startDate;
	}

	public void setStartDate(java.sql.Date startDate) {
		this.startDate = startDate;
	}

	@Column(nullable = true)
	@Temporal(value = TemporalType.TIMESTAMP)
	public java.util.Date getEndDate() {
		return endDate;
	}

	public void setEndDate(java.sql.Date endDate) {
		this.endDate = endDate;
	}

	@Column
	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	@Column
	public int getChargedDuration() {
		return chargedDuration;
	}

	public void setChargedDuration(int chargedDuration) {
		this.chargedDuration = chargedDuration;
	}

	@Column
	public BigDecimal getChargedUnits() {
		return chargedUnits;
	}

	public void setChargedUnits(BigDecimal chargedUnits) {
		this.chargedUnits = chargedUnits;
	}

	@OneToOne
	public UnitsReserve getReserve() {
		return reserve;
	}

	public void setReserve(UnitsReserve reserve) {
		this.reserve = reserve;
	}

	@Column
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
