package com.ufc.uif.qh3.acad.operation;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/*
 * ��ȡ�����ļ�����Ϣ��������Ա������
 */
public class ParseConfigXMl {

	private String configFilePath = null;
	public Map<String, Map<String, List<Map<String, String>>>> infoToShow = null;
	public Map<String, Map<String, Map<String, String>>> attachInfos = null;
	public List<Map<String, String>> queryInfos = null;
	public Map<String, List<Map<String, String>>> saveOpInfo = null;
	// public Map<String, Map<String, String>> bomInfo = null;// ���浼��bomʱ������ӳ����Ϣ

	public Map<String, Map<String, String>> lovPropertyMap = null;

	public ParseConfigXMl(String configFilePath) {
		this.configFilePath = configFilePath;
		infoToShow = new HashMap<String, Map<String, List<Map<String, String>>>>();
		attachInfos = new HashMap<String, Map<String, Map<String, String>>>();
		queryInfos = new ArrayList<Map<String, String>>();

		// bomInfo = new HashMap<String, Map<String, String>>();

		// saveOpInfo = new HashMap<String, List<Map<String,String>>>();
		// lovPropertyMap = new HashMap<String, Map<String,String>>();

	}

	public ParseConfigXMl() {

	}

	/**
	 * 
	 * TODO ��ȡ�����ļ��ĸ��ڵ�
	 * 
	 * @return Element ���ظ��ڵ�
	 * @author lijj created on 2010-11-29����04:33:30
	 */
	public Element getRootNode(String fileName) {
		SAXReader reader = new SAXReader();
		File configFile = new File(fileName);
		Document doc = null;
		try {
			doc = reader.read(configFile);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		Element root = doc.getRootElement();
		return root;
	}

	/**
	 * 
	 * TODO ��ȡ�����ļ�uif.xml�й�����ʾ�������������Ϣ
	 * 
	 * @return Map<String,List<Map<String,String>>> ����ֵ˵��
	 *         �ṹ����item��Ӧ���itemType,ÿ��itemType��Ӧ�������б�,ÿ�����Ե������Ϣ���һ��map
	 * @author lijj created on 2010-11-29����04:23:36
	 */
	public Map<String, List<Map<String, String>>> getInfoToShow(Element rootNode, String objectName) {
		List<?> objectNodes = rootNode.element("open_operation").element("info_show").element(objectName).elements(objectName + "_type");
		Map<String, List<Map<String, String>>> objectTypeInfos = new HashMap<String, List<Map<String, String>>>();

		for (int i = 0; i < objectNodes.size(); i++) {
			Element tempObject = (Element) objectNodes.get(i);
			String name = tempObject.attributeValue("name");

			List<?> attributeNodes = tempObject.elements("attribute");
			List<Map<String, String>> attributeList = new ArrayList<Map<String, String>>();
			for (int j = 0; j < attributeNodes.size(); j++) {
				Element tempAttribute = (Element) attributeNodes.get(j);
				Map<String, String> propertyInfos = new HashMap<String, String>();
				List<?> propertyNodes = tempAttribute.elements();
				for (int k = 0; k < propertyNodes.size(); k++) {
					Element tempProperty = (Element) propertyNodes.get(k);
					String propertyName = tempProperty.getName();
					String propertyValue = tempProperty.getTextTrim();

					propertyInfos.put(propertyName, propertyValue);
				}
				attributeList.add(propertyInfos);
			}

			objectTypeInfos.put(name, attributeList);
		}

		return objectTypeInfos;
	}

	/**
	 * 
	 * TODO ��ȡ�����ļ��б�������������Ϣ
	 * 
	 * @return Map<String,List<Map<String,String>>>
	 * @author lijj created on 2010-12-21����02:22:17
	 */
	public Map<String, List<Map<String, String>>> getSaveOpInfo(Element rootNode) {
		List<?> objectNodes = rootNode.element("save_operation").elements("object");
		Map<String, List<Map<String, String>>> objectTypeInfos = new HashMap<String, List<Map<String, String>>>();
		for (int i = 0; i < objectNodes.size(); i++) {
			Element tempObject = (Element) objectNodes.get(i);
			String typeName = tempObject.attributeValue("pdm_type");

			List<?> attributeNodes = tempObject.elements("attribute");
			List<Map<String, String>> attributeList = new ArrayList<Map<String, String>>();
			for (int j = 0; j < attributeNodes.size(); j++) {
				Element tempAttribute = (Element) attributeNodes.get(j);
				Map<String, String> propertyInfos = new HashMap<String, String>();
				List<?> propertyNodes = tempAttribute.elements();
				for (int k = 0; k < propertyNodes.size(); k++) {
					Element tempProperty = (Element) propertyNodes.get(k);
					String propertyName = tempProperty.getName();
					String propertyValue = tempProperty.getTextTrim();
					propertyInfos.put(propertyName, propertyValue);
				}
				attributeList.add(propertyInfos);
			}

			objectTypeInfos.put(typeName, attributeList);
		}
		return objectTypeInfos;
	}

	/**
	 * 
	 * TODO ��ȡ�����ļ���ȡitem,revision,ds�ĸ�����Ϣ
	 * 
	 * @return Map<String,Map<String,String>>
	 * @author lijj created on 2010-11-29����05:29:15
	 */
	public Map<String, Map<String, String>> getAttatchInfos(Element rootNode, String objectName) {
		// Element rootNode = getRootNode(fileName);
		List<?> objectNodes = rootNode.element("open_operation").element("info_show").element(objectName).elements(objectName + "_type");
		Map<String, Map<String, String>> attachInfo = new HashMap<String, Map<String, String>>();
		for (int i = 0; i < objectNodes.size(); i++) {
			Map<String, String> attachMap = new HashMap<String, String>();
			Element tempObject = (Element) objectNodes.get(i);
			String name = tempObject.attributeValue("name");
			String relation = tempObject.attributeValue("relation");
			String extName = tempObject.attributeValue("ext_name");
			String localPath = tempObject.attributeValue("local_path");

			attachMap.put("relation", relation);
			attachMap.put("ext_name", extName);
			attachMap.put("local_path", localPath);
			attachInfo.put(name, attachMap);
		}
		return attachInfo;
	}

	/**
	 * 
	 * TODO ��ȡ�����ļ��й��ڸ߼���ѯ����Ϣ����Ҫ��ϵͳ�Ѵ��ڵĲ�ѯ����
	 * 
	 * @return List<Map<String,String>>
	 * @author lijj created on 2010-11-29����05:52:43
	 */
	public List<Map<String, String>> getQueryInfo(Element rootNode) {
		List<Map<String, String>> queryInfoList = new ArrayList<Map<String, String>>();
		List<?> queryNodes = rootNode.element("open_operation").element("data_query").elements("attribute");
		for (int i = 0; i < queryNodes.size(); i++) {
			Element queryNode = (Element) queryNodes.get(i);
			List<?> queryInfoNodes = queryNode.elements();
			Map<String, String> queryInfo = new HashMap<String, String>();
			for (int j = 0; j < queryInfoNodes.size(); j++) {
				Element tempInfoNode = (Element) queryInfoNodes.get(j);
				String infoName = tempInfoNode.getName();
				String infoValue = tempInfoNode.getTextTrim();
				queryInfo.put(infoName, infoValue);
			}
			queryInfoList.add(queryInfo);
		}
		/*
		for (int i0 = 0; i0 < queryInfoList.size(); i0++) {
			// ��ӡMap��ֵ��
			for (Object key : queryInfoList.get(i0).keySet()) {
				String v = (String) queryInfoList.get(i0).get(key);
				System.out.println("key=" + key + " v=" + v);
			}
		}*/
		return queryInfoList;
	}

	/**
	 * 
	 * TODO��ȡ�����ļ��й���lov���͵�������cad��ȡֵ�Ķ�Ӧ��Ϣ ��ʱֻ�ܴ����������ϵ����ԡ�
	 * 
	 * @return Map<String,Map<String,String>>
	 * @author lijj created on 2011-1-7����01:56:04
	 */
	public Map<String, Map<String, String>> getLovPropertyMap(Element rootNode) {
		Map<String, Map<String, String>> lovPropMap = new HashMap<String, Map<String, String>>();
		List<?> propNodes = rootNode.element("lov_property").elements("property");
		for (int i = 0; i < propNodes.size(); i++) {
			Element propNode = (Element) propNodes.get(i);
			String lovPropName = propNode.attributeValue("name");
			List<?> lovInfoNodes = propNode.elements();
			Map<String, String> lovInfo = new HashMap<String, String>();
			for (int j = 0; j < lovInfoNodes.size(); j++) {
				Element tempLovInfoNode = (Element) lovInfoNodes.get(j);
				String lovKey = tempLovInfoNode.attributeValue("key");
				String lovValue = tempLovInfoNode.attributeValue("value");
				lovInfo.put(lovKey, lovValue);
			}
			lovPropMap.put(lovPropName, lovInfo);
		}
		return lovPropMap;
	}

	/**
	 * 
	 * TODO ��ʼ��ʱ�������ļ��е�������Ϣһ�ζ�ȡ��
	 * 
	 * @return void
	 * @author lijj created on 2010-11-29����05:54:09
	 */
	public void setInfos() {
		Element rootNode = getRootNode(configFilePath);
		infoToShow.put("item", getInfoToShow(rootNode, "item"));
		infoToShow.put("revision", getInfoToShow(rootNode, "revision"));
		infoToShow.put("ds", getInfoToShow(rootNode, "ds"));
		attachInfos.put("item", getAttatchInfos(rootNode, "item"));
		attachInfos.put("revision", getAttatchInfos(rootNode, "revision"));
		attachInfos.put("ds", getAttatchInfos(rootNode, "ds"));

		queryInfos = getQueryInfo(rootNode);

		saveOpInfo = getSaveOpInfo(rootNode);
		lovPropertyMap = getLovPropertyMap(rootNode);
	}

	/**
	 * ��ȡ�����ļ���TCObject�����Ӧ��������Ϣ
	 * 
	 * @author Liugz
	 * @create on 2009-6-15 This project for CAD_Concrete
	 * @return
	 */
	public static Map<String, Map<String, String>> getTCObjectProps() {
		try {
			Map<String, Map<String, String>> objList = null;
			SAXReader reader = new SAXReader();
			Document doc = reader.read("AutoCAD/cfg/tc_object_properties.xml");
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

	/**
	 * 
	 * TODO �Բ�ѯ��ʾ��Ϊkey �Թ���Ĳ�ѯ��Ϊֵ
	 * 
	 * @return Map<String,String>
	 * @author lijj created on 2011-9-2����10:11:38
	 */
	public Map<String, String> getQueryName(Element rootNode) {
		List<?> objElementList = rootNode.elements("object");
		Map<String, String> allQueryInfoMap = new HashMap<String, String>();
		for (int i = 0; i < objElementList.size(); i++) {
			Element objElement = (Element) objElementList.get(i);
			String objDisplayType = objElement.attributeValue("displayType");

			Element queryElement = objElement.element("query_panel");
			String queryName = queryElement.attributeValue("queryName");

			allQueryInfoMap.put(objDisplayType, queryName);
		}
		return allQueryInfoMap;
	}

	/**
	 * 
	 * TODO �Ӳ�ѯ�����ļ����ȡ���в�ѯ����ѯ��������Ϣ------�Բ�ѯ��ʾ��Ϊkey�Կؼ���ϢlistΪֵ
	 * 
	 * @return Map<String,List<Map<String,String>>>
	 * @author lijj created on 2011-9-2����10:05:50
	 */
	public Map<String, List<Map<String, String>>> getQueryComponentInfo(Element rootNode) {
		List<?> objElementList = rootNode.elements("object");
		Map<String, List<Map<String, String>>> allQueryComInfoMap = new HashMap<String, List<Map<String, String>>>();
		for (int i = 0; i < objElementList.size(); i++) {
			Element objElement = (Element) objElementList.get(i);
			String objDisplayType = objElement.attributeValue("displayType");

			List<Map<String, String>> queryComponentInfoList = new ArrayList<Map<String, String>>();
			List<?> componentsList = objElement.element("query_panel").elements("component");
			for (int j = 0; j < componentsList.size(); j++) {
				Map<String, String> comPonentinfoMap = new HashMap<String, String>();
				Element comElement = (Element) componentsList.get(j);
				comPonentinfoMap.put("displayName", comElement.attributeValue("displayName"));
				comPonentinfoMap.put("property", comElement.attributeValue("property"));
				comPonentinfoMap.put("type", comElement.attributeValue("type"));
				comPonentinfoMap.put("isRequired", comElement.attributeValue("isRequired"));
				comPonentinfoMap.put("source", comElement.attributeValue("source"));

				queryComponentInfoList.add(comPonentinfoMap);
			}

			allQueryComInfoMap.put(objDisplayType, queryComponentInfoList);
		}
		return allQueryComInfoMap;
	}

	/**
	 * 
	 * TODO ���������ļ����йز�ѯ������������ʾ���ֵ������Ϣ
	 * 
	 * @return List<Map<String,String>>
	 * @author lijj created on 2011-8-25����11:32:02
	 */
	public Map<String, List<Map<String, String>>> getPropComponentInfo(Element rootNode) {
		List<?> objElementList = rootNode.elements("object");
		Map<String, List<Map<String, String>>> allQueryComInfoMap = new HashMap<String, List<Map<String, String>>>();
		for (int i = 0; i < objElementList.size(); i++) {
			Element objElement = (Element) objElementList.get(i);
			String objDisplayType = objElement.attributeValue("displayType");

			List<Map<String, String>> propComponentInfoList = new ArrayList<Map<String, String>>();
			List<?> componentsList = objElement.element("prop_panel").elements("component");
			for (int j = 0; j < componentsList.size(); j++) {
				Map<String, String> comPonentinfoMap = new HashMap<String, String>();
				Element comElement = (Element) componentsList.get(j);
				comPonentinfoMap.put("displayName", comElement.attributeValue("displayName"));
				comPonentinfoMap.put("property", comElement.attributeValue("property"));
				comPonentinfoMap.put("type", comElement.attributeValue("type"));
				comPonentinfoMap.put("source", comElement.attributeValue("source"));

				propComponentInfoList.add(comPonentinfoMap);
			}

			allQueryComInfoMap.put(objDisplayType, propComponentInfoList);
		}
		return allQueryComInfoMap;
	}

	/**
	 * 
	 * TODO ���������ļ���ȡ���ͷ�����Ϣ������Ϣ
	 * 
	 * @return List<Map<String,String>>
	 * @author lijj created on 2011-8-25����11:32:02
	 */
	public Map<String, List<Map<String, String>>> getSendMsgInfo(Element rootNode) {
		List<?> objElementList = rootNode.elements("object");
		Map<String, List<Map<String, String>>> allTypeMsgInfoMap = new HashMap<String, List<Map<String, String>>>();
		for (int i = 0; i < objElementList.size(); i++) {
			Element objElement = (Element) objElementList.get(i);
			String objDisplayType = objElement.attributeValue("type");

			List<Map<String, String>> paramInfoMapList = new ArrayList<Map<String, String>>();
			List<?> componentsList = objElement.elements("parameter");
			for (int j = 0; j < componentsList.size(); j++) {
				Map<String, String> paramInfoMap = new HashMap<String, String>();
				Element comElement = (Element) componentsList.get(j);
				paramInfoMap.put("displayName", comElement.attributeValue("displayName"));
				paramInfoMap.put("property", comElement.attributeValue("property"));
				paramInfoMap.put("separator", comElement.attributeValue("separator"));

				paramInfoMapList.add(paramInfoMap);
			}

			allTypeMsgInfoMap.put(objDisplayType, paramInfoMapList);
		}
		return allTypeMsgInfoMap;
	}

}
