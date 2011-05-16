package kz.edu.sdu.buben.j2ee.app.mom.db;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "accounts")
public class AccountEntity {
	protected int account_id;
	protected String phone_number;
	protected BigDecimal balance;

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getAccount_id() {
		return account_id;
	}

	public void setAccount_id(int accountId) {
		account_id = accountId;
	}

	@Column(length = 10, unique = true)
	public String getPhone_number() {
		return phone_number;
	}

	public void setPhone_number(String phoneNumber) {
		phone_number = phoneNumber;
	}

	@Column
	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

}
