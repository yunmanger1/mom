package kz.edu.sdu.buben.j2ee.app.mom.db;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import kz.edu.sdu.buben.j2ee.app.mom.AppConsts;

@Entity
@Table(name = "callsessionhistory")
public class CallSessionHistory {
   protected int sessionId;
   protected AccountEntity from;
   protected AccountEntity to;
   protected java.util.Date startDate;
   protected java.util.Date endDate;
   protected long duration = 0;
   protected long chargedDuration = 0;
   protected long reservedDuration = 0;
   protected java.util.Date reserveEndDate;
   protected BigDecimal chargedUnits = BigDecimal.ZERO;
   protected int status = AppConsts.HISTORY_SESSION_STATUS;
   protected java.util.Date chargeDate;
   protected java.util.Date moveDate;

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

   @Column
   public int getStatus() {
      return status;
   }

   public void setStatus(int status) {
      this.status = status;
   }

   @Column
   @Temporal(value = TemporalType.TIMESTAMP)
   public java.util.Date getChargeDate() {
      return chargeDate;
   }

   public void setChargeDate(java.util.Date chargeDate) {
      this.chargeDate = chargeDate;
   }

   @Column
   @Temporal(value = TemporalType.TIMESTAMP)
   public java.util.Date getMoveDate() {
      return moveDate;
   }

   public void setMoveDate(java.util.Date moveDate) {
      this.moveDate = moveDate;
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
