package kz.edu.sdu.buben.j2ee.app.mom.utils;

import java.io.StringReader;
import java.io.StringWriter;

import kz.bips.comps.utils.Log4JLoggerWrapper;
import kz.bips.comps.utils.ObjectUtil;

import com.wutka.jox.JOXBeanReader;
import com.wutka.jox.JOXBeanWriter;
import com.wutka.jox.JOXConfig;

public class JoxUtils {
	public static final String ENCODING = "UTF-8";
	public static final String ROOT_TAG = "root";

	public JoxUtils() {
		// JOXConfig conf = JOXConfig.getDefaultConfig();
		// conf.setWriteClassNames(false);
		// conf.setDateFormat(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss")); //
		// ������ ����������� � XML
		// conf.setAtomsAsAttributes(true); // ��������� ������ XML.
		// ������������ � production?
	}

	@SuppressWarnings("unchecked")
	public <T> T fromXml(String str, Class<T> clazz) throws Exception {
		JOXBeanReader joxIn = new JOXBeanReader(new StringReader(str));
		joxIn.setConfig(createJOXConfig());
		T obj = (T) joxIn.readObject(clazz);
		joxIn.close();
		return obj;
	}

	public String toXml(Object obj) throws Exception {
		return toXml(obj, null);
	}

	public String toXml(Object obj, String[] cdataElements) throws Exception {
		try {
			StringWriter os = new StringWriter();
			JOXBeanWriter joxOut = new JOXBeanWriter(os, ENCODING);
			joxOut.setConfig(createJOXConfig());
			joxOut.writeObject(ROOT_TAG, obj, cdataElements);
			joxOut.close();
			return os.toString();
		} catch (ArrayIndexOutOfBoundsException e) {
			Log4JLoggerWrapper log = new Log4JLoggerWrapper(getClass());
			log.debug("Investigate joxed object:");
			ObjectUtil.printObjectProperties(log, obj);
			throw new Exception(
					"ArrayIndexOutOfBoundsException in JoxUtils.toXml", e);
		}
	}

	/**
	 * �� ������������: Date formats are not synchronized. It is recommended to
	 * create separate format instances for each thread. If multiple threads
	 * access a format concurrently, it must be synchronized externally.
	 * 
	 * getDefaultConfig ���������� ����������� ��������� JOXConfig � �������
	 * ���� ���������� ���� DateFormat. ������ ��� �������� �������� ���
	 * ������������� JoxUtils �� ���������� �������.
	 * 
	 * � ���������� �������� ���� �����.
	 * 
	 * http://iris.kase.kz:8080/jira/browse/IRIM-115
	 */
	private JOXConfig createJOXConfig() {
		JOXConfig conf = new JOXConfig();
		conf.setWriteClassNames(false);
		// conf.setDateFormat(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss")); //
		// ������ ����������� � XML
		// conf.setAtomsAsAttributes(true); // ��������� ������ XML.
		// ������������ � production?
		return conf;
	}
}
