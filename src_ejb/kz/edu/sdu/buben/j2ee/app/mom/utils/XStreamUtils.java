package kz.edu.sdu.buben.j2ee.app.mom.utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XppDomDriver;

public class XStreamUtils {
   XStream xstream;

   public XStreamUtils() {
      xstream = new XStream(new XppDomDriver());
   }

   public XStreamUtils a(String alias, Class clazz) {
      xstream.alias(alias, clazz);
      return this;
   }

   public <T> T fromXml(String text, Class<T> class1) {
      return (T) xstream.fromXML(text);
   }

   public String toXml(Object object) {
      return xstream.toXML(object);
   }

}
