package kz.edu.sdu.buben.j2ee.app.mom.vaadin;

import kz.bips.comps.utils.Log4JLoggerWrapper;
import kz.edu.sdu.buben.j2ee.app.mom.ejb.interfaces.LIVaadinEJB;

import com.github.wolfie.refresher.Refresher;
import com.github.wolfie.refresher.Refresher.RefreshListener;
import com.vaadin.Application;
import com.vaadin.ui.SplitPanel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class SimpleAddressBook extends Application {
   Log4JLoggerWrapper log = new Log4JLoggerWrapper(getClass());
   private static String[] visibleCols = new String[]{"sessionId", "from", "to", "startDate", "reserveEndDate", "reservedDuration"};
   private static String[] visibleHistCols = new String[]{"sessionId", "from", "to", "startDate", "endDate"};

   private final Table sessionList = new Table();
   private final Table historyList = new Table();

   LIVaadinEJB vaadinEjb;
   private CallSessionReferenceContainer dataSource;
   private CallSessionHistoryReferenceContainer historyDataSource;

   public SimpleAddressBook() {

   }

   public SimpleAddressBook(LIVaadinEJB vaadinEjb) {
      this.vaadinEjb = vaadinEjb;
   }

   @Override
   public void init() {
      log.debug("Init vaadin application");
      dataSource = new CallSessionReferenceContainer(vaadinEjb);
      dataSource.refresh();
      historyDataSource = new CallSessionHistoryReferenceContainer(vaadinEjb);
      historyDataSource.refresh();
      initLayout();
      initSessionList();
   }

   private void initLayout() {
      SplitPanel splitPanel = new SplitPanel(SplitPanel.ORIENTATION_HORIZONTAL);
      setMainWindow(new Window("Call Sessions", splitPanel));
      VerticalLayout left = new VerticalLayout();
      left.setSizeFull();
      left.addComponent(sessionList);
      sessionList.setSizeFull();
      left.setExpandRatio(sessionList, 1);

      VerticalLayout right = new VerticalLayout();
      right.setSizeFull();
      right.addComponent(historyList);
      historyList.setSizeFull();
      right.setExpandRatio(historyList, 1);

      splitPanel.addComponent(left);
      splitPanel.addComponent(right);

      Refresher refresher = new Refresher();
      refresher.setRefreshInterval(1000);
      refresher.addListener(new CallSessionListener());
      left.addComponent(refresher);

   }

   private void initSessionList() {
      sessionList.setContainerDataSource(dataSource);
      sessionList.setVisibleColumns(visibleCols);
      sessionList.setSelectable(true);
      sessionList.setImmediate(true);

      historyList.setContainerDataSource(historyDataSource);
      historyList.setVisibleColumns(visibleHistCols);
      historyList.setSelectable(true);
      historyList.setImmediate(true);
   }

   public class CallSessionListener implements RefreshListener {
      private static final long serialVersionUID = -8765221895426102605L;

      @Override
      public void refresh(Refresher source) {
         dataSource.refresh();
         sessionList.setContainerDataSource(dataSource);
         sessionList.setVisibleColumns(visibleCols);

         historyDataSource.refresh();
         historyList.setContainerDataSource(historyDataSource);
         historyList.setVisibleColumns(visibleHistCols);
      }
   }

}
