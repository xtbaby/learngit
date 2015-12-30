package com.ufc.uif.qh3.acad.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class Dom4jUtil {
	
	public static final String PROS = "properties";
	public static final String PRO = "property";
	public static final String NAME = "name";
	public static final String VALUE = "value";
	public static final String PARAM = "param";
	private static Dom4jUtil util;

	public static Dom4jUtil getDom4jUtil() {
		if (util == null)
			util = new Dom4jUtil();
		return util;
	}

	public void setProValue(Element paramElement, String paramString1, String paramString2) {
		Element localElement1 = paramElement.element("properties");
		if (localElement1 == null)
			localElement1 = paramElement.addElement("properties");
		Element localElement2 = localElement1.addElement("property");
		localElement2.addAttribute("name", paramString1);
		localElement2.addAttribute("value", paramString2);
	}

	public Map<String, String> getProValues(Element paramElement) {
		HashMap localHashMap = new HashMap();
		List localList = paramElement.element("properties").elements();
		for (int i = 0; i < localList.size(); i++) {
			Element localElement = (Element) localList.get(i);
			String str1 = localElement.attributeValue("name");
			String str2 = localElement.attributeValue("value");
			if (str2 != null)
				str2 = str2.trim();
			localHashMap.put(str1, str2);
		}
		return localHashMap;
	}

	public String getProValue(Element paramElement, String paramString) {
		Map localMap = getProValues(paramElement);
		if (localMap == null)
			return null;
		String str = (String) localMap.get(paramString);
		if (str == null)
			str = "";
		return str;
	}

	public Map<String, String> getParamValues(Element paramElement) {
		HashMap localHashMap = new HashMap();
		List localList = paramElement.elements("param");
		for (int i = 0; i < localList.size(); i++) {
			Element localElement = (Element) localList.get(i);
			String str1 = localElement.attributeValue("name").trim();
			String str2 = localElement.attributeValue("value").trim();
			localHashMap.put(str1, str2);
		}
		return localHashMap;
	}

	public String getParamValue(Element paramElement, String paramString) {
		Map localMap = getParamValues(paramElement);
		if (localMap == null)
			return null;
		return (String) localMap.get(paramString);
	}

	public void setParamValue(Element paramElement, String paramString1, String paramString2) {
		Element localElement = paramElement.addElement("param");
		localElement.addAttribute("name", paramString1);
		localElement.addAttribute("value", paramString2);
	}

	public static boolean doc2XmlFile(Document paramDocument, String paramString) throws IOException {
		boolean bool = true;
		OutputFormat localOutputFormat = OutputFormat.createPrettyPrint();
		localOutputFormat.setEncoding("GB2312");
		XMLWriter localXMLWriter = new XMLWriter(new FileWriter(new File(paramString)), localOutputFormat);
		localXMLWriter.write(paramDocument);
		localXMLWriter.close();
		return bool;
	}
}
