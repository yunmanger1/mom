import java.math.BigDecimal;

import kz.edu.sdu.buben.j2ee.app.mom.dto.ChangeBalanceDTO;
import kz.edu.sdu.buben.j2ee.app.mom.utils.JoxUtils;

import org.junit.Test;

public class TestStuff {

	@Test
	public void testJox() throws Exception {
		JoxUtils ju = new JoxUtils();
		ChangeBalanceDTO dto = new ChangeBalanceDTO();
		dto.setDelta(BigDecimal.valueOf(1500.256));
		dto.setPhoneNumber("7024476704");
		System.out.println(ju.toXml(dto));
	}
}
