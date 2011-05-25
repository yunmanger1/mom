package kz.edu.sdu.buben.j2ee.app.mom.dto.vaadin;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;

public class CallSessionReference implements Serializable, Item {

   private final Integer sessionId;
   private final Map<Object, Property> propertyMap;

   public CallSessionReference(Integer sessionId, Map<String, Object> propertyMap) {
      this.sessionId = sessionId;
      this.propertyMap = new HashMap<Object, Property>();
      for (Entry<String, Object> entry : propertyMap.entrySet()) {
         if (entry.getValue() != null) {
            if (entry.getValue() instanceof BigDecimal) {
               this.propertyMap.put(entry.getKey(), new ObjectProperty(((BigDecimal) entry.getValue()).toString()));
            } else {
               this.propertyMap.put(entry.getKey(), new ObjectProperty(entry.getValue()));
            }
         } else {
            this.propertyMap.put(entry.getKey(), new ObjectProperty("not set"));
         }
      }
   }

   public Integer getSessionId() {
      return sessionId;
   }

   public Property getItemProperty(Object id) {
      return propertyMap.get(id);
   }

   public Collection< ? > getItemPropertyIds() {
      return Collections.unmodifiableSet(propertyMap.keySet());
   }

   public boolean addItemProperty(Object id, Property property) {
      throw new UnsupportedOperationException("Item is read-only.");
   }

   public boolean removeItemProperty(Object id) {
      throw new UnsupportedOperationException("Item is read-only.");
   }
}
