package kz.edu.sdu.buben.j2ee.app.mom.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kz.bips.comps.utils.Log4JLoggerWrapper;
import kz.bips.comps.utils.PVNStringBuilder;
import kz.edu.sdu.buben.j2ee.app.mom.dto.BalanceRequestDTO;
import kz.edu.sdu.buben.j2ee.app.mom.dto.BalanceResponseDTO;
import kz.edu.sdu.buben.j2ee.app.mom.dto.ResponseType;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.LIBalanceEJB;
import kz.edu.sdu.buben.j2ee.app.mom.utils.XStreamUtils;

public class BalanceServlet extends HttpServlet {

   /**
    * 
    */
   private static final long serialVersionUID = -2710324555728113741L;

   private final Log4JLoggerWrapper log = new Log4JLoggerWrapper(getClass());

//   @EJB
//   LIMessagingService ms;
//
//   @Resource(mappedName = AppConsts.BALANCE_QUEUE_NAME)
//   Queue destination;

   XStreamUtils ju = new XStreamUtils();

   public BalanceServlet() {
      super();
      ju.a(BalanceRequestDTO.ALIAS, BalanceRequestDTO.class).a(BalanceResponseDTO.ALIAS, BalanceResponseDTO.class);
   }

   @EJB
   LIBalanceEJB balanceEjb;

   @Override
   public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      try {
         PrintWriter out = new PrintWriter(response.getOutputStream());
         out.print("<html><body>SEND XML VIA POST REQUEST</body></html>");
         out.close();
      } catch (IOException e) {
         log.logStackTrace(e, "OOps");
      }
   }

   @Override
   public void doPost(HttpServletRequest request, HttpServletResponse response) {
      long t = System.currentTimeMillis();
      response.setContentType("application/xml");
      BalanceResponseDTO rdto = new BalanceResponseDTO();
      try {
         PrintWriter out = new PrintWriter(response.getOutputStream());
         BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
         String line = null;
         PVNStringBuilder sb = new PVNStringBuilder();
         sb.f("");
         while ((line = in.readLine()) != null) {
            log.debug(line);
            sb.a(line);
         }
         try {
            BalanceRequestDTO dto = ju.fromXml(sb.toString(), BalanceRequestDTO.class);
            BigDecimal result = null;
            switch (dto.getRequesttype()) {
               case AVAILABLE :
                  result = balanceEjb.getAvailableUnits(dto.getPhonenumber());
                  break;
               case BLOCKED :
                  result = balanceEjb.getReservedUnits(dto.getPhonenumber());
                  break;
               case CHARGE :
                  result = balanceEjb.changeBalance(dto.getPhonenumber(), dto.getUnits());
                  break;
               default :
                  break;
            }
            rdto.setResult(ResponseType.OK);
            rdto.setValue(result);
         } catch (Exception e) {
            log.logStackTrace(e, "CallSession request fail");
            rdto.setResult(ResponseType.FAIL);
         }
         out.println(ju.toXml(rdto));
         out.close();
      } catch (Exception e) {
         log.logStackTrace(e, "asdasd");
      } finally {
         log.debug("request took: %d ms", System.currentTimeMillis() - t);
      }
   }
}
