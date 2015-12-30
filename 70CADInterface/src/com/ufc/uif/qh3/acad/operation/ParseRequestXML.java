package com.ufc.uif.qh3.acad.operation;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.ufc.uif.qh3.acad.drawing.SaveModel;

/**
 * �������������XML����
 * 
 * @author Liugz
 * 
 */
public class ParseRequestXML {

	private static Document request = null;
	private static String filepeizhi = "uif.xml";

	public ParseRequestXML() {

	}

	public ParseRequestXML(InputStream in) {
		SAXReader reader = new SAXReader();
		try {
			request = reader.read(in);
		} catch (DocumentException e) {
			e.printStackTrace();
			System.out.println("δ�ܻ�ȡ������Ϣ��");
			request = null;
		}
	}

	/**
	 * ��ȡ�ӿڷ��͵�XML����
	 * 
	 * @return for GofACADInterface
	 * @author Liugz
	 * @create on 2008-10-26 Document
	 */
	public static Document getRequest() {
		return request;
	}

	public static Document getRequest(InputStream in) {
		SAXReader reader = new SAXReader();
		try {
			Document doc = reader.read(in);
			return doc;
		} catch (DocumentException e) {
			e.printStackTrace();
			System.out.println("δ�ܻ�ȡ���������");
		}
		return null;
	}

	/**
	 * ��ȡ�����й�����Ŀ����Ϣ�� <properties>�ڵ��е�����
	 * 
	 * @return for GofACADInterface
	 * @author zhangwh
	 * @create on 2015-12-10
	 */
	public static List<Map<String, String>> getPropsInfo(Element root) {
		// ������Ҫ���ص���Ϣ
		List<Map<String, String>> retnList = new ArrayList<Map<String, String>>();
		List list = new ArrayList();

		Element object = root.element("body").element("object");
		list = object.elements("properties");
		if (list.size() != 0) {

			for (Iterator projItor = list.iterator(); projItor.hasNext();) {
				Element projectElmt = (Element) projItor.next();
				Iterator propertyItor = projectElmt.elementIterator();
				Map<String, String> pptMap = new HashMap<String, String>();
				while (propertyItor.hasNext()) {
					Element ele = (Element) propertyItor.next();
					String name = ele.attributeValue("name");
					String value = ele.attributeValue("value");
					// �����������ֵ
					//
					System.out.println("name=" + name + ",value=" + value);
					// end
					pptMap.put(name, value);
				}
				retnList.add(pptMap);
			}

		}
		return retnList;
	}

	/**
	 * ��ȡ�����й���ͼֽ����Ϣ�� <doc>�ڵ��е�����
	 * 
	 * @return for GofACADInterface
	 * @author zhangwh
	 * @create on 2015-12-10
	 */
	public static List<Map<String, String>> getDocInfo(Element root) {
		// ������Ҫ���ص���Ϣ
		List<Map<String, String>> retnList = new ArrayList<Map<String, String>>();

		List docList = root.element("body").element("object").elements("doc");
		for (Iterator docItor = docList.iterator(); docItor.hasNext();) {
			Map<String, String> proMap = new HashMap<String, String>();
			Element docElmt = (Element) docItor.next();

			List doc_pro_list = docElmt.elements("property");
			String thepage = "", drawing_Size = "", docpath = "";
			Map<String, String> docMap = new HashMap<String, String>();
			for (int i = 0; i < doc_pro_list.size(); i++) {
				Element proElmt = (Element) doc_pro_list.get(i);
				System.out.println("************************************************************");
				for (int j = 0; j < proElmt.attributes().size(); j = j + 2) {
					String name = proElmt.attribute(j).getValue();
					String value = proElmt.attribute(j + 1).getValue();

					//
					System.out.println("nameA=" + name + ",valueA=" + value);
					// end
					proMap.put(name, value);
				}
				System.out.println("************************************************************");
			}
			retnList.add(proMap);
		}

		return retnList;
	}

	/**
	 * ��ȡ�����й���Ԫ��������Ϣ�� \<sub-objects\>�ڵ���\<object\>��\<properties\>�ڵ��е�����
	 * 
	 * @return for GofACADInterface
	 * @author zhangwh
	 * @create on 2015-12-10
	 */
	public static List<Map<String, String>> getSubObjsInfo(Element root) {
		// ������Ҫ���ص���Ϣ
		List<Map<String, String>> retnList = new ArrayList<Map<String, String>>();

		List subObjs = root.selectNodes("/transfer-data/body/object/sub-objects/object");
		
		for (Iterator subObjItor = subObjs.iterator(); subObjItor.hasNext();) {
			Element subObj = (Element) subObjItor.next();
			List subPptsList = subObj.elements("properties");
			for (Iterator docItor = subPptsList.iterator(); docItor.hasNext();) {
				Element subPptsElmt = (Element) docItor.next();
				Iterator pptItor = subPptsElmt.elementIterator();
				// ����ÿ��<doc>�ڵ��е�����
				Map<String, String> pptMap = new HashMap<String, String>();
				while (pptItor.hasNext()) {
					Element property = (Element) pptItor.next();
					String name = property.attributeValue("name");
					String value = property.attributeValue("value");
					pptMap.put(name, value);
				}
				retnList.add(pptMap);
			}
		}
		return retnList;
	}

	/**
	 * ��ȡ\<head\>�ڵ��� startpoint ���Ե�ֵ
	 * 
	 * @author Liugz
	 * @create on 2009-3-3 This project for CAD_Concrete
	 * @return
	 */
	public static String getStartpoint(Element root) {
		String value = "";
		Element head = root.element("head");
		List paramList = head.elements("param");
		for (Iterator itor = paramList.iterator(); itor.hasNext();) {
			Element param = (Element) itor.next();
			String name = param.attributeValue("name");
			if ("startpoint".equals(name)) {
				value = param.attributeValue("value");
				break;
			}
		}
		return value;
	}

	/**
	 * 
	 * TODO ���ݹؼ���ȡ�����еĶ�Ӧֵ
	 * 
	 * @return String
	 * @author lijj created on 2011-9-2����11:05:18
	 */
	public static String getRequestInfo(Element root, String keyName) {
		String value = "";
		Element head = root.element("head");
		List paramList = head.elements("param");
		for (Iterator itor = paramList.iterator(); itor.hasNext();) {
			Element param = (Element) itor.next();
			String name = param.attributeValue("name");
			if (keyName.equals(name)) {
				value = param.attributeValue("value");
				break;
			}
		}
		return value;
	}

	// ����һ������ֵȥ����һ������ֵ
	// eg��<property name="item_id" value="111we11111231" /> ���� item_id ��ȡvalue ֵ
	public static String getXMLValue(Element root, String nodes, String param, String attr) {
		// List listpro=
		// root.selectNodes("body/object/properties/property[@name='װ��������']");
		// "name='װ��������'"
		// System.out.println(nodes+"[@"+param+"]");
		List listpro = root.selectNodes(nodes + "[@" + param + "]");
		// System.out.println("properties size"+listpro.size());
		if (listpro.size() > 0) {
			Element elementp = (Element) listpro.get(0);
			return elementp.attributeValue(attr);
		} else {
			return "û���ҵ����";
		}
	}

	/*
	 * public static String getItemID(Element root) {// ��ȡcad��item ������ SAXReader
	 * reader = new SAXReader(); String UFCROOT = System.getenv("UFCROOT");
	 * String fileSeparator = System.getProperty("file.separator");
	 * 
	 * //
	 * System.out.println(UFCROOT+fileSeparator+"config"+fileSeparator+"test.xml"
	 * ); File input = new File(UFCROOT + fileSeparator + "config" +
	 * fileSeparator + filepeizhi); // File input = new
	 * File("C:\\UFC\\config\\test.xml"); Document doc = null; try { doc =
	 * reader.read(input); // doc = //
	 * reader.read(UFCROOT+fileSeparator+"config"+fileSeparator+"test.xml"); }
	 * catch (DocumentException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } // Document doc =
	 * reader.read("config/tc_object_properties.xml"); Element rootcofig =
	 * doc.getRootElement(); Element obj = rootcofig. element("save_operation").
	 * element("cad_item_id"); String caditemid = obj.getText();
	 * 
	 * String ItemId = ""; List<Map<String, String>> props = getPropsInfo(root);
	 * for (Iterator<Map<String, String>> itr = props.iterator(); itr
	 * .hasNext();) { Map<String, String> prop = itr.next(); Set<String> keys =
	 * prop.keySet(); for (Iterator<String> key = keys.iterator();
	 * key.hasNext();) { String keyName = key.next(); String keyVaue =
	 * prop.get(keyName); if (keyName.equalsIgnoreCase(caditemid)) { ItemId =
	 * keyVaue; break; } //
	 * System.out.println(keyName+"===================>"+keyVaue); } } return
	 * ItemId; }
	 */
	/*
	 * //��ѯ���ƻ�ȡ public static String getFindItemName() { List list=selectNodes(
	 * filepeizhi ,"/dialog/save_operation/find_item/name" ,false ,""); if
	 * (list.size() <0 ) return ""; Element e=(Element) list.get(0); return
	 * e.getText(); } //��ȡext_name:ʵ���ļ�����չ�ļ���
	 * 
	 * public static String getExt_name() {
	 * 
	 * List list=selectNodes( filepeizhi
	 * ,"/dialog/save_operation/dataset_map/dataset" ,false ,""); if
	 * (list.size() <0 ) return ""; Element e=(Element) list.get(0); String
	 * Ext_named=e.element("ext_name").getText();
	 * //System.out.println("Ext_named =" + Ext_named ); return Ext_named; }
	 * public static String getPdm_Type() {
	 * 
	 * List list=selectNodes( filepeizhi
	 * ,"/dialog/save_operation/dataset_map/dataset" ,false ,""); if
	 * (list.size() <0 ) return ""; Element e=(Element) list.get(0); String
	 * pdm_type=e.element("pdm_type").getText();
	 * 
	 * //System.out.println("pdm_type =" + pdm_type ); return pdm_type; }
	 * //ParseRequestXML public static String getNamed_ref() {
	 * 
	 * 
	 * List list=selectNodes( filepeizhi
	 * ,"/dialog/save_operation/dataset_map/dataset" ,false ,""); if
	 * (list.size() <0 ) return ""; Element e=(Element) list.get(0); String
	 * named_ref=e.element("named_ref").getText();
	 * 
	 * return named_ref; }
	 */

	public static List selectNodes(String filename, String nodes, boolean issort, String sort) {

		String UFCROOT = System.getenv("UFCROOT");
		String fileSeparator = System.getProperty("file.separator");
		// AutoCAD\cfg
		String input = UFCROOT + fileSeparator + "AutoCAD" + fileSeparator + "cfg" + fileSeparator + filename;
		SAXReader reader = new SAXReader();
		List list = null;
		try {
			Document document = reader.read(input);
			/*
			 * if (issort) { list= document.selectNodes(nodes ,sort);} else {
			 * list= document.selectNodes(nodes); }
			 */
			list = document.selectNodes(nodes);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	/*
	 * public static boolean isDataSet_add() { List list= selectNodes(
	 * filepeizhi ,"/dialog/save_operation/dataset_map/dataset_add" ,false ,"");
	 * if (list.size() <0 ) return false; Element e=(Element) list.get(0);
	 * String required=e.element("required").getText();
	 * System.out.println("isDataSet_add "+required); if
	 * ("true".equals(required)) { return true; }else{ return false; } }
	 * 
	 * 
	 * public static String getPdm_Type_add() { List list= selectNodes(
	 * filepeizhi ,"/dialog/save_operation/dataset_map/dataset_add" ,false ,"");
	 * if (list.size() <0 ) return ""; Element e=(Element) list.get(0); return
	 * e.element("pdm_type").getText();
	 * 
	 * 
	 * }
	 * 
	 * public static String getExt_name_add() { List list= selectNodes(
	 * filepeizhi ,"/dialog/save_operation/dataset_map/dataset_add" ,false ,"");
	 * if (list.size() <0 ) return ""; Element e=(Element) list.get(0); return
	 * e.element("ext_name").getText();
	 * 
	 * 
	 * }
	 * 
	 * public static String getconvertTool() { List list= selectNodes(
	 * filepeizhi ,"/dialog/save_operation/dataset_map/dataset_add" ,false ,"");
	 * if (list.size() <0 ) return ""; Element e=(Element) list.get(0); return
	 * e.element("convertTool").getText(); }
	 * 
	 * public static String getViewType() { List list= selectNodes( filepeizhi
	 * ,"/dialog/save_operation/bom_map/view_type" ,false ,""); if (list.size()
	 * <0 ) return ""; Element e=(Element) list.get(0); return e.getText(); }
	 * public static String getChildType() { List list= selectNodes( filepeizhi
	 * ,"/dialog/save_operation/bom_map/bom_line/child_type" ,false ,""); if
	 * (list.size() <0 ) return ""; Element e=(Element) list.get(0); return
	 * e.getText(); }
	 */

	/**
	 * ���ϲ�ѯӦ�� �������͵ĸ߼���ѯ filenameΪ��ѯ�������ļ���������UFCROOT�µ�config��
	 * 
	 * @return
	 */
	public static ArrayList<SaveModel> parseQuerycondition(String filename) {
		SAXReader reader = new SAXReader();
		String path = System.getenv("UFCROOT") + "\\config\\" + filename;
		ArrayList<SaveModel> al = new ArrayList<SaveModel>();

		try {
			FileInputStream file = new FileInputStream(path);

			Document document = reader.read(file);

			Element root = document.getRootElement();

			for (Iterator<?> iter = root.elementIterator(); iter.hasNext();) {
				Element element = (Element) iter.next();

				SaveModel sm = new SaveModel();

				sm.setDisplayName(element.attributeValue("displayName"));
				sm.setProprety(element.attributeValue("property"));
				sm.setType(element.attributeValue("type"));
				sm.setCadName(element.attributeValue("cadName"));

				if (element.attributeValue("isRequired").equals("true")) {
					sm.setRequired(true);
				} else {
					sm.setRequired(false);
				}

				if (element.attributeValue("type").equals("LOV") || element.attributeValue("type").equals("SelectButton")) {
					String lovlists = element.attributeValue("list");

					String[] lovs = lovlists.split(";");

					for (int i = 0; i < lovs.length; i++) {
						sm.setLov(lovs[i]);
					}
				}
				al.add(sm);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return al;
	}

	/**
	 * ���ϲ�ѯ�� ��ȡ�����ļ���TCObject�����Ӧ��������Ϣ
	 * 
	 * @author Liugz
	 * @create on 2009-6-15 This project for CAD_Concrete
	 * @return
	 */
	public static Map<String, Map<String, String>> getTCObjectProps() {
		try {
			Map<String, Map<String, String>> objList = null;
			SAXReader reader = new SAXReader();
			// InputStream input =
			// ParseRequestXML.class.getResourceAsStream("tc_object_properties.xml");
			File input = new File("config/tc_object_properties.xml");
			Document doc = reader.read(input);
			// Document doc = reader.read("config/tc_object_properties.xml");
			Element root = doc.getRootElement();
			List objs = root.elements("object");
			if (null != objs) {
				objList = new HashMap<String, Map<String, String>>();
				for (Object obj : objs) {
					Map<String, String> objProps = new HashMap<String, String>();
					Element objEle = (Element) obj;
					String objType = objEle.attributeValue("type");
					for (Iterator itor = objEle.elements("properties").iterator(); itor.hasNext();) {
						Element propEle = (Element) itor.next();
						String display = propEle.attributeValue("displayName");
						String tcname = propEle.attributeValue("tcName");
						objProps.put(display, tcname);
					}
					objList.put(objType, objProps);
				}
			}
			return objList;
		} catch (DocumentException e) {
			System.out.println("δ�ܻ�ȡ���������");
			return null;
		}
	}

	public static void main(String[] args) {
		SAXReader reader = new SAXReader();
		File configFile = new File("f:\\saveTest.xml");
		Document doc = null;
		try {
			doc = reader.read(configFile);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		Element root = doc.getRootElement();

		System.out.println(getStartpoint(root));
		System.out.println(getPropsInfo(root));
		System.out.println(getDocInfo(root));
		System.out.println(getSubObjsInfo(root));
	}
}
