package kz.edu.sdu.buben.j2ee.app.mom.ejb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import kz.bips.comps.utils.Log4JLoggerWrapper;
import kz.edu.sdu.buben.j2ee.app.mom.db.CallSession;
import kz.edu.sdu.buben.j2ee.app.mom.dto.vaadin.CallSessionReference;
import kz.edu.sdu.buben.j2ee.app.mom.dto.vaadin.QueryMetaData;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.LIVaadinEJB;

@Stateless
public class VaadinEJB implements LIVaadinEJB {
   private final Log4JLoggerWrapper log = new Log4JLoggerWrapper(getClass());

   @PersistenceContext(unitName = "mom")
   EntityManager em;

//   @EJB
//   LIMessagingService ms;
//
//   @EJB
//   LIAccountEJB accountEjb;
//
//   @EJB
//   LIBalanceEJB balanceEjb;

   @Override
   public List<CallSessionReference> getActiveCallSessionList(QueryMetaData queryMetaData, String... propertyNames) {
      StringBuffer pqlBuf = new StringBuffer();
      pqlBuf.append("SELECT p.sessionId");
      for (int i = 0; i < propertyNames.length; i++) {
         pqlBuf.append(",");
         pqlBuf.append("p.");
         pqlBuf.append(propertyNames[i]);
      }
      pqlBuf.append(" FROM CallSession p");

      if (queryMetaData.getPropertyName() != null) {
         pqlBuf.append(" WHERE p.");
         pqlBuf.append(queryMetaData.getPropertyName());
         if (queryMetaData.getSearchTerm() == null) {
            pqlBuf.append(" IS NULL");
         } else {
            pqlBuf.append(" = :searchTerm");
         }
      }

      if (queryMetaData != null && queryMetaData.getAscending().length > 0) {
         pqlBuf.append(" ORDER BY ");
         for (int i = 0; i < queryMetaData.getAscending().length; i++) {
            if (i > 0) {
               pqlBuf.append(",");
            }
            pqlBuf.append("p.");
            pqlBuf.append(queryMetaData.getOrderBy()[i]);
            if (!queryMetaData.getAscending()[i]) {
               pqlBuf.append(" DESC");
            }
         }
      }

      String pql = pqlBuf.toString();
      Query query = em.createQuery(pql);
      if (queryMetaData.getPropertyName() != null && queryMetaData.getSearchTerm() != null) {
         query.setParameter("searchTerm", queryMetaData.getSearchTerm());
      }

      List<Object[]> result = query.getResultList();
      log.debug("ActiveSessions@VaadinEJB: %d", result.size());
      List<CallSessionReference> referenceList = new ArrayList<CallSessionReference>(result.size());

      HashMap<String, Object> valueMap;
      for (Object[] row : result) {
         valueMap = new HashMap<String, Object>();
         for (int i = 1; i < row.length; i++) {
            valueMap.put(propertyNames[i - 1], row[i]);
         }
         referenceList.add(new CallSessionReference((Integer) row[0], valueMap));
      }
      return referenceList;
   }

   @Override
   public CallSession getCallSession(Long id) {
      return em.find(CallSession.class, id);
   }

   @Override
   public List<CallSessionReference> getLastCallSessionHistoryList(QueryMetaData queryMetaData, String... propertyNames) {
      StringBuffer pqlBuf = new StringBuffer();
      pqlBuf.append("SELECT p.sessionId");
      for (int i = 0; i < propertyNames.length; i++) {
         pqlBuf.append(",");
         pqlBuf.append("p.");
         pqlBuf.append(propertyNames[i]);
      }
      pqlBuf.append(" FROM CallSessionHistory p");

      if (queryMetaData.getPropertyName() != null) {
         pqlBuf.append(" WHERE p.");
         pqlBuf.append(queryMetaData.getPropertyName());
         if (queryMetaData.getSearchTerm() == null) {
            pqlBuf.append(" IS NULL");
         } else {
            pqlBuf.append(" = :searchTerm");
         }
      }

      if (queryMetaData != null && queryMetaData.getAscending().length > 0) {
         pqlBuf.append(" ORDER BY ");
         for (int i = 0; i < queryMetaData.getAscending().length; i++) {
            if (i > 0) {
               pqlBuf.append(",");
            }
            pqlBuf.append("p.");
            pqlBuf.append(queryMetaData.getOrderBy()[i]);
            if (!queryMetaData.getAscending()[i]) {
               pqlBuf.append(" DESC");
            }
         }
      }

      String pql = pqlBuf.toString();
      Query query = em.createQuery(pql);
      query.setMaxResults(30);
      if (queryMetaData.getPropertyName() != null && queryMetaData.getSearchTerm() != null) {
         query.setParameter("searchTerm", queryMetaData.getSearchTerm());
      }

      List<Object[]> result = query.getResultList();
      log.debug("refresh history: %d", result.size());
      List<CallSessionReference> referenceList = new ArrayList<CallSessionReference>(result.size());

      HashMap<String, Object> valueMap;
      for (Object[] row : result) {
         valueMap = new HashMap<String, Object>();
         for (int i = 1; i < row.length; i++) {
            valueMap.put(propertyNames[i - 1], row[i]);
         }
         referenceList.add(new CallSessionReference((Integer) row[0], valueMap));
      }
      return referenceList;
   }

}
