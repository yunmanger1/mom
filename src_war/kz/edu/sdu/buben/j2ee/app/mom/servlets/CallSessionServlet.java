package kz.edu.sdu.buben.j2ee.app.mom.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kz.bips.comps.utils.Log4JLoggerWrapper;
import kz.bips.comps.utils.PVNStringBuilder;
import kz.edu.sdu.buben.j2ee.app.mom.dto.CallSessionRequestDTO;
import kz.edu.sdu.buben.j2ee.app.mom.dto.ResponseDTO;
import kz.edu.sdu.buben.j2ee.app.mom.dto.ResponseType;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.LISessionEJB;
import kz.edu.sdu.buben.j2ee.app.mom.utils.XStreamUtils;

public class CallSessionServlet extends HttpServlet {

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

   public CallSessionServlet() {
      super();
      ju.a(CallSessionRequestDTO.ALIAS, CallSessionRequestDTO.class).a(ResponseDTO.ALIAS, ResponseDTO.class);
   }

   @EJB
   LISessionEJB sessionEjb;

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
      ResponseDTO rdto = new ResponseDTO();
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
            CallSessionRequestDTO dto = ju.fromXml(sb.toString(), CallSessionRequestDTO.class);
            boolean result = true;
            switch (dto.getRequesttype()) {
               case RESERVE :
                  result = sessionEjb.reserveCallSession(dto.getFromnumber(), dto.getTonumber(), dto.getReserveseconds());
                  break;
               case OVER :
                  sessionEjb.overCallSession(dto.getFromnumber(), dto.getTonumber());
                  break;
               default :
                  break;
            }
            if (result) {
               rdto.setResult(ResponseType.OK);
            } else {
               rdto.setResult(ResponseType.FALSE);
            }
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
