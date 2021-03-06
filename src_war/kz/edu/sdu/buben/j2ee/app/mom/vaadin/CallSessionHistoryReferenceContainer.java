package kz.edu.sdu.buben.j2ee.app.mom.vaadin;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kz.edu.sdu.buben.j2ee.app.mom.db.CallSession;
import kz.edu.sdu.buben.j2ee.app.mom.dto.vaadin.CallSessionReference;
import kz.edu.sdu.buben.j2ee.app.mom.dto.vaadin.QueryMetaData;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.LIVaadinEJB;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;

public class CallSessionHistoryReferenceContainer implements Container, Container.ItemSetChangeNotifier {

   public static final Object[] NATURAL_COL_ORDER = new String[]{"sessionId", "from", "to", "startDate", "endDate", "duration", "chargedDuration", "reservedDuration", "reserveEndDate", "chargedUnits", "status", "chargeDate"};
   protected static final Collection<Object> NATURAL_COL_ORDER_COLL = Collections.unmodifiableList(Arrays.asList(NATURAL_COL_ORDER));
   protected final LIVaadinEJB vaadinEjb;
   protected List<CallSessionReference> csReferences;
   protected Map<Object, CallSessionReference> idIndex;
   public static QueryMetaData defaultQueryMetaData = new QueryMetaData(new String[]{"moveDate"}, new boolean[]{false});
   protected QueryMetaData queryMetaData = defaultQueryMetaData;

   // Some fields omitted

   public CallSessionHistoryReferenceContainer(LIVaadinEJB vaadinEJB) {
      this.vaadinEjb = vaadinEJB;
   }

   public void refresh() {
      refresh(queryMetaData);
   }

   public void refresh(QueryMetaData queryMetaData) {
      this.queryMetaData = queryMetaData;
      csReferences = vaadinEjb.getLastCallSessionHistoryList(queryMetaData, (String[]) NATURAL_COL_ORDER);
      idIndex = new HashMap<Object, CallSessionReference>(csReferences.size());
      for (CallSessionReference pf : csReferences) {
         idIndex.put(pf.getSessionId(), pf);
      }
      notifyListeners();
   }

   public QueryMetaData getQueryMetaData() {
      return queryMetaData;
   }

   public void close() {
      if (csReferences != null) {
         csReferences.clear();
         csReferences = null;
      }
   }

   public boolean isOpen() {
      return csReferences != null;
   }

   public int size() {
      return csReferences == null ? 0 : csReferences.size();
   }

   public Item getItem(Object itemId) {
      return idIndex.get(itemId);
   }

   public Collection< ? > getContainerPropertyIds() {
      return NATURAL_COL_ORDER_COLL;
   }

   public Collection< ? > getItemIds() {
      return Collections.unmodifiableSet(idIndex.keySet());
   }

   public List<CallSessionReference> getItems() {
      return Collections.unmodifiableList(csReferences);
   }

   public Property getContainerProperty(Object itemId, Object propertyId) {
      Item item = idIndex.get(itemId);
      if (item != null) {
         return item.getItemProperty(propertyId);
      }
      return null;
   }

   public Class< ? > getType(Object propertyId) {
      try {
         PropertyDescriptor pd = new PropertyDescriptor((String) propertyId, CallSession.class);
         return pd.getPropertyType();
      } catch (Exception e) {
         return null;
      }
   }

   public boolean containsId(Object itemId) {
      return idIndex.containsKey(itemId);
   }

   // Unsupported methods omitted
   // addListener(..) and removeListener(..) omitted

   protected void notifyListeners() {
//      ArrayList<ItemSetChangeListener> cl = (ArrayList<ItemSetChangeListener>) listeners.clone();
//      ItemSetChangeEvent event = new ItemSetChangeEvent() {
//         public Container getContainer() {
//            return CallSessionReferenceContainer.this;
//         }
//      };
//
//      for (ItemSetChangeListener listener : cl) {
//         listener.containerItemSetChange(event);
//      }
   }

   @Override
   public boolean addContainerProperty(Object propertyId, Class< ? > type, Object defaultValue) throws UnsupportedOperationException {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public Object addItem() throws UnsupportedOperationException {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Item addItem(Object itemId) throws UnsupportedOperationException {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public boolean removeAllItems() throws UnsupportedOperationException {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public boolean removeContainerProperty(Object propertyId) throws UnsupportedOperationException {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public boolean removeItem(Object itemId) throws UnsupportedOperationException {
      // TODO Auto-generated method stub
      return false;
   }

   @Override
   public void addListener(ItemSetChangeListener listener) {
      // TODO Auto-generated method stub

   }

   @Override
   public void removeListener(ItemSetChangeListener listener) {
      // TODO Auto-generated method stub

   }
}
