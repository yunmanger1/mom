package kz.edu.sdu.buben.j2ee.app.mom.utils;

import java.math.BigDecimal;

public class AccountUtils {
	public static boolean validatePhoneNumber(String phoneNumber) {
		return phoneNumber.length() == 10
				&& (phoneNumber.startsWith("702")
						|| phoneNumber.startsWith("701")
						|| phoneNumber.startsWith("705")
						|| phoneNumber.startsWith("700")
						|| phoneNumber.startsWith("777") || phoneNumber
						.startsWith("707"));
	}

	public static boolean validateDelta(BigDecimal delta) {
		return delta != null;
	}
}
