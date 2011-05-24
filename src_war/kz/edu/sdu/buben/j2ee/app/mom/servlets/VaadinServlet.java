package kz.edu.sdu.buben.j2ee.app.mom.servlets;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.LISessionEJB;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.LIVaadinEJB;
import kz.edu.sdu.buben.j2ee.app.mom.vaadin.SimpleAddressBook;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.AbstractApplicationServlet;


//@WebServlet(urlPatterns = "/*")
public class VaadinServlet extends AbstractApplicationServlet {
	private static final long serialVersionUID = 8108966743526339732L;
	@EJB
   LIVaadinEJB vaadinEjb;
	
//	@EJB
//    UserManager userManager;

	@Override
   protected Application getNewApplication(HttpServletRequest request) throws ServletException {
       return new SimpleAddressBook(vaadinEjb);
   }

   @Override
   protected Class<? extends Application> getApplicationClass() throws ClassNotFoundException {
       return SimpleAddressBook.class;
   }
}
