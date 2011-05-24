package kz.edu.sdu.buben.j2ee.app.mom.vaadin;

import java.text.SimpleDateFormat;
import java.util.List;

import kz.edu.sdu.buben.j2ee.app.mom.db.AccountEntity;
import kz.edu.sdu.buben.j2ee.app.mom.db.CallSession;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.LISessionEJB;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.LIVaadinEJB;

import com.vaadin.Application;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.SplitPanel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

public class SimpleAddressBook extends Application {

//   private static String[] fields = {"Session ID", "From", "To", "Start", "End", "Duration", "Charged duration", "Reserved Duration", "Charged Units", "Reserved Units", "Status", "Charge Date"};
//   private static String[] visibleCols = new String[]{"Session ID","From", "To", "Start","End","Reserved Duration"};
   private static String[] fields = {"sessionId", "from", "to", 
      "startDate", "endDate", "duration", "chargedDuration", "reservedDuration", "chargedUnits", "reserve", "status", "chargeDate"};
   private static String[] visibleCols = new String[]{"sessionId", "from", "to", 
      "startDate", "endDate", "reservedDuration", "status"};

   private Table contactList = new Table();
   private Form contactEditor = new Form();
   private HorizontalLayout bottomLeftCorner = new HorizontalLayout();
   private Button contactRemovalButton;
//   private IndexedContainer addressBookData;
   
   LIVaadinEJB vaadinEjb;
   private CallSessionReferenceContainer dataSource;
   
   public SimpleAddressBook(){
      
   }
   
   public SimpleAddressBook(LIVaadinEJB vaadinEjb){
      this.vaadinEjb = vaadinEjb;
   }

   @Override
   public void init() {
      initLayout();
//      initContactAddRemoveButtons();
      dataSource = new CallSessionReferenceContainer(vaadinEjb);
      dataSource.refresh();
//      addressBookData = createDummyData(data);
      initAddressList();
//      initFilteringControls();
   }
   
   private void initLayout() {
      SplitPanel splitPanel = new SplitPanel(SplitPanel.ORIENTATION_HORIZONTAL);
      setMainWindow(new Window("Call Sessions", splitPanel));
      VerticalLayout left = new VerticalLayout();
      left.setSizeFull();
      left.addComponent(contactList);
      contactList.setSizeFull();
      left.setExpandRatio(contactList, 1);
      splitPanel.addComponent(left);
      splitPanel.addComponent(contactEditor);
      contactEditor.setSizeFull();
      contactEditor.getLayout().setMargin(true);
      contactEditor.setImmediate(true);
      bottomLeftCorner.setWidth("100%");
      left.addComponent(bottomLeftCorner);
   }

   private void initContactAddRemoveButtons() {
      // New item button
      bottomLeftCorner.addComponent(new Button("+", new Button.ClickListener() {
         public void buttonClick(ClickEvent event) {
            Object id = contactList.addItem();
            contactList.setValue(id);
         }
      }));

      // Remove item button
      contactRemovalButton = new Button("-", new Button.ClickListener() {
         public void buttonClick(ClickEvent event) {
            contactList.removeItem(contactList.getValue());
            contactList.select(null);
         }
      });
      contactRemovalButton.setVisible(false);
      bottomLeftCorner.addComponent(contactRemovalButton);
   }

   private String[] initAddressList() {
      contactList.setContainerDataSource(dataSource);
      contactList.setVisibleColumns(visibleCols);
      contactList.setSelectable(true);
      contactList.setImmediate(true);
      
//      contactList.addListener(new Property.ValueChangeListener() {
//         public void valueChange(ValueChangeEvent event) {
//            Object id = contactList.getValue();
//            contactEditor.setItemDataSource(id == null ? null : contactList.getItem(id));
//            contactRemovalButton.setVisible(id != null);
//         }
//      });
      return visibleCols;
   }

//   private void initFilteringControls() {
//      for (final String pn : visibleCols) {
//         final TextField sf = new TextField();
//         bottomLeftCorner.addComponent(sf);
//         sf.setWidth("100%");
//         sf.setInputPrompt(pn);
//         sf.setImmediate(true);
//         bottomLeftCorner.setExpandRatio(sf, 1);
//         sf.addListener(new Property.ValueChangeListener() {
//            public void valueChange(ValueChangeEvent event) {
//               dataSource.removeContainerFilters(pn);
//               if (sf.toString().length() > 0 && !pn.equals(sf.toString())) {
//                  addressBookData.addContainerFilter(pn, sf.toString(), true, false);
//               }
//               getMainWindow().showNotification("" + addressBookData.size() + " matches found");
//            }
//         });
//      }
//   }

   private static IndexedContainer createDummyData(List<CallSession> list) {

      IndexedContainer ic = new IndexedContainer();
      

      for (String p : fields) {
         ic.addContainerProperty(p, String.class, "");
      }
      int n = list.size();
      for (int i = 0; i < n; i++) {
         Object id = ic.addItem();
         CallSession cs = list.get(i);
         ic.getContainerProperty(id, "Session ID").setValue(String.valueOf(cs.getSessionId()));
         ic.getContainerProperty(id, "From").setValue(cs.getFrom().getPhone_number());
         ic.getContainerProperty(id, "To").setValue(cs.getTo().getPhone_number());
//         "Start","End","Reserved Duration"
         SimpleDateFormat fmt = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
         ic.getContainerProperty(id, "Start").setValue(fmt.format(cs.getStartDate()));
         if (cs.getEndDate() != null){
            ic.getContainerProperty(id, "End").setValue(fmt.format(cs.getEndDate()));
         }
         ic.getContainerProperty(id, "Reserved Duration").setValue(cs.getReservedDuration());
      }

      return ic;
   }

}
