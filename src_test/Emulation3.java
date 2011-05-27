import java.math.BigDecimal;

import kz.edu.sdu.buben.j2ee.app.mom.dto.BalanceRequestDTO;
import kz.edu.sdu.buben.j2ee.app.mom.dto.BalanceRequestType;

public class Emulation3 {

   public static void main(String[] args) throws Exception {
      long start = System.currentTimeMillis();
      final int N = 1000;

      BalanceRequestDTO dto = new BalanceRequestDTO();
      dto.setRequesttype(BalanceRequestType.ADD);
      dto.setUnits(BigDecimal.valueOf(1000));
      for (int i = 0; i < N; i++) {
         String acc = String.format("701%07d", i);
         dto.setPhonenumber(acc);
         EmulationUtils.addBalance(dto);
      }
      System.out.printf("Took %d ms\n", System.currentTimeMillis() - start);

   }
}
