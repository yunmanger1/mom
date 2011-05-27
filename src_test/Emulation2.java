import java.math.BigDecimal;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import kz.edu.sdu.buben.j2ee.app.mom.AppConsts;
import kz.edu.sdu.buben.j2ee.app.mom.dto.ChangeBalanceDTO;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.IMessagingService;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.MessagesSender;

public class Emulation2 {

   public static void main(String[] args) throws Exception {
      long start = System.currentTimeMillis();
      final int N = 1000;
      EmulationUtils.ms.sendMultipleObjectMessages(EmulationUtils.balance, new MessagesSender() {

         @Override
         public void sendMessages(IMessagingService ms, Session session, MessageProducer producer) throws JMSException {
            ChangeBalanceDTO dto = new ChangeBalanceDTO();
            dto.setDelta(BigDecimal.valueOf(1000));
            for (int i = 0; i < N; i++) {
               String acc = String.format("701%07d", i);
               dto.setPhoneNumber(acc);
               Message message = ms.prepareMessage(session, dto);
               AppConsts.CHANGE_BALANCE_MODIFIER.modify(message);
               producer.send(message);
            }
         }
      });
      System.out.printf("Took %d ms\n", System.currentTimeMillis() - start);

   }
}
