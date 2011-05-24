package kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces;

import java.util.List;

import kz.edu.sdu.buben.j2ee.app.mom.db.CallSession;
import kz.edu.sdu.buben.j2ee.app.mom.dto.vaadin.CallSessionReference;
import kz.edu.sdu.buben.j2ee.app.mom.dto.vaadin.QueryMetaData;

public interface IVaadinEJB {

   public CallSession getCallSession(Long id);
   
   public List<CallSessionReference> getActiveCallSessionList(QueryMetaData queryMetaData, String... propertyNames);


}
