package kz.edu.sdu.buben.j2ee.app.mom.db;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import kz.edu.sdu.buben.j2ee.app.mom.AppConsts;

@Entity
@Table(name = "callsession")
public class CallSession {
   protected int sessionId;
   protected AccountEntity from;
   protected AccountEntity to;
   protected java.util.Date startDate;
   protected java.util.Date endDate;
   protected long duration = 0;
   protected long chargedDuration = 0;
   protected BigDecimal chargedUnits = BigDecimal.ZERO;
   protected UnitsReserve reserve;
   protected long reservedDuration = 0;
   protected java.util.Date reserveEndDate;
   protected int status = AppConsts.BEFORE_SESSION_STATUS;
   protected java.util.Date chargeDate;

   @Id
   @Column
   @GeneratedValue(strategy = GenerationType.AUTO)
   public int getSessionId() {
      return sessionId;
   }

   public void setSessionId(int sessionId) {
      this.sessionId = sessionId;
   }

   @ManyToOne(fetch = FetchType.EAGER)
   public AccountEntity getFrom() {
      return from;
   }

   public void setFrom(AccountEntity from) {
      this.from = from;
   }

   @ManyToOne(fetch = FetchType.EAGER)
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

   public void setStartDate(java.util.Date startDate) {
      this.startDate = startDate;
   }

   @Column(nullable = true)
   @Temporal(value = TemporalType.TIMESTAMP)
   public java.util.Date getEndDate() {
      return endDate;
   }

   public void setEndDate(java.util.Date endDate) {
      this.endDate = endDate;
   }

   @Column
   public long getDuration() {
      return duration;
   }

   public void setDuration(long duration) {
      this.duration = duration;
   }

   @Column
   public long getChargedDuration() {
      return chargedDuration;
   }

   public void setChargedDuration(long chargedDuration) {
      this.chargedDuration = chargedDuration;
   }

   @Column
   public BigDecimal getChargedUnits() {
      return chargedUnits;
   }

   public void setChargedUnits(BigDecimal chargedUnits) {
      this.chargedUnits = chargedUnits;
   }

   @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE})
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

   @Column(nullable = true)
   @Temporal(value = TemporalType.TIMESTAMP)
   public java.util.Date getChargeDate() {
      return chargeDate;
   }

   public void setChargeDate(java.util.Date chargeDate) {
      this.chargeDate = chargeDate;
   }

   @Column
   public long getReservedDuration() {
      return reservedDuration;
   }

   public void setReservedDuration(long reservedDuration) {
      this.reservedDuration = reservedDuration;
   }

   @Column
   @Temporal(value = TemporalType.TIMESTAMP)
   public java.util.Date getReserveEndDate() {
      return reserveEndDate;
   }

   public void setReserveEndDate(java.util.Date reserveEndDate) {
      this.reserveEndDate = reserveEndDate;
   }

}
