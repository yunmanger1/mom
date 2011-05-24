package kz.edu.sdu.buben.j2ee.app.mom.dto.vaadin;

import java.io.Serializable;

public class QueryMetaData implements Serializable {

   private boolean[] ascending;
   private String[] orderBy;
   private String searchTerm;
   private String propertyName;

   public QueryMetaData(String propertyName, String searchTerm, String[] orderBy, boolean[] ascending) {
      this.propertyName = propertyName;
      this.searchTerm = searchTerm;
      this.ascending = ascending;
      this.orderBy = orderBy;
   }

   public QueryMetaData(String[] orderBy, boolean[] ascending) {
      this(null, null, orderBy, ascending);
   }

   public boolean[] getAscending() {
      return ascending;
   }

   public String[] getOrderBy() {
      return orderBy;
   }

   public String getSearchTerm() {
      return searchTerm;
   }

   public String getPropertyName() {
      return propertyName;
   }
}