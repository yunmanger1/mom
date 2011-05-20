package kz.edu.sdu.buben.j2ee.app.mom.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kz.edu.sdu.buben.j2ee.app.mom.db.AccountEntity;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.LIAccountEJB;

import org.apache.log4j.Logger;

public class ListAccountsServlet extends HttpServlet {

   /**
    * 
    */
   private static final long serialVersionUID = 9009182879261370763L;

   private final Logger log = Logger.getLogger(getClass());

   @EJB
   LIAccountEJB accounts;

   @Override
   public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      try {
         PrintWriter out = new PrintWriter(response.getOutputStream());
         out.println("<html><body><h1>Account List</h1>");
         out.println("<table>");
         for (AccountEntity ac : accounts.getAccountList()) {
            out.printf("<tr><td>%d</td><td>%s</td><td>%.3f</td></tr>", ac.getAccount_id(), ac.getPhone_number(), ac.getBalance());
         }
         out.println("</table>");
         out.println("</body></html>");
         out.close();
      } catch (IOException e) {
         log.trace(e);
      }

   }
}
