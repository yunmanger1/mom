package kz.edu.sdu.buben.j2ee.app.mom.dto.vaadin;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;

public class CallSessionReference implements Serializable, Item {

   private Long sessionId;
   private Map<Object, Property> propertyMap;

   public CallSessionReference(Long personId, Map<String, Object> propertyMap) {
      this.sessionId = personId;
      this.propertyMap = new HashMap<Object, Property>();
      for (Entry<String, Object> entry : propertyMap.entrySet()) {
         this.propertyMap.put(entry.getKey(), new ObjectProperty(entry.getValue()));
      }
   }

   public Long getSessionId() {
      return sessionId;
   }

   public Property getItemProperty(Object id) {
      return propertyMap.get(id);
   }

   public Collection<?> getItemPropertyIds() {
      return Collections.unmodifiableSet(propertyMap.keySet());
   }

   public boolean addItemProperty(Object id, Property property) {
      throw new UnsupportedOperationException("Item is read-only.");
   }

   public boolean removeItemProperty(Object id) {
      throw new UnsupportedOperationException("Item is read-only.");
   }
}