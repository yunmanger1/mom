package kz.edu.sdu.buben.j2ee.app.mom;

import kz.bips.comps.utils.ExtendedPreferences;
import kz.bips.comps.utils.ExtendedPreferencesProps;

/**
 * Created 18.08.2008 11:50:00
 * @author Victor Pyankov
 */
public class AppProps extends ExtendedPreferencesProps {
   private static ExtendedPreferences props = null;

   @Override
   public String getApplicationNodeName() {
      return "mom";
   }

   public static ExtendedPreferences getProps() {
      if (props == null) {
         props = new AppProps();
      }
      return props;
   }
}
