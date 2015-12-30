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
 * 读取配置文件的信息保存至成员变量中
 */
public class ParseConfigXMl {

	private String configFilePath = null;
	public Map<String, Map<String, List<Map<String, String>>>> infoToShow = null;
	public Map<String, Map<String, Map<String, String>>> attachInfos = null;
	public List<Map<String, String>> queryInfos = null;
	public Map<String, List<Map<String, String>>> saveOpInfo = null;
	// public Map<String, Map<String, String>> bomInfo = null;// 保存导入bom时的属性映射信息

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
	 * TODO 获取配置文件的根节点
	 * 
	 * @return Element 返回根节点
	 * @author lijj created on 2010-11-29下午04:33:30
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
	 * TODO 读取配置文件uif.xml中关于显示树的相关限制信息
	 * 
	 * @return Map<String,List<Map<String,String>>> 返回值说明
	 *         结构例子item对应多个itemType,每个itemType对应其属性列表,每个属性的相关信息组成一个map
	 * @author lijj created on 2010-11-29下午04:23:36
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
	 * TODO 读取配置文件中保存操作的相关信息
	 * 
	 * @return Map<String,List<Map<String,String>>>
	 * @author lijj created on 2010-12-21下午02:22:17
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
	 * TODO 读取配置文件获取item,revision,ds的附加信息
	 * 
	 * @return Map<String,Map<String,String>>
	 * @author lijj created on 2010-11-29下午05:29:15
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
	 * TODO 读取配置文件中关于高级查询的信息，主要是系统已存在的查询名。
	 * 
	 * @return List<Map<String,String>>
	 * @author lijj created on 2010-11-29下午05:52:43
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
			// 打印Map的值得
			for (Object key : queryInfoList.get(i0).keySet()) {
				String v = (String) queryInfoList.get(i0).get(key);
				System.out.println("key=" + key + " v=" + v);
			}
		}*/
		return queryInfoList;
	}

	/**
	 * 
	 * TODO提取配置文件中关于lov类型的属性与cad中取值的对应信息 暂时只能处理单个对象上的属性。
	 * 
	 * @return Map<String,Map<String,String>>
	 * @author lijj created on 2011-1-7下午01:56:04
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
	 * TODO 初始化时将配置文件中的所有信息一次读取。
	 * 
	 * @return void
	 * @author lijj created on 2010-11-29下午05:54:09
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
	 * 获取配置文件中TCObject对象对应的属性信息
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
			System.out.println("未能获取请求参数。");
			return null;
		}
	}

	/**
	 * 
	 * TODO 以查询显示名为key 以构造的查询名为值
	 * 
	 * @return Map<String,String>
	 * @author lijj created on 2011-9-2上午10:11:38
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
	 * TODO 从查询配置文件里读取所有查询面板查询条件的信息------以查询显示名为key以控件信息list为值
	 * 
	 * @return Map<String,List<Map<String,String>>>
	 * @author lijj created on 2011-9-2上午10:05:50
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
	 * TODO 解析配置文件中有关查询界面上属性显示部分的相关信息
	 * 
	 * @return List<Map<String,String>>
	 * @author lijj created on 2011-8-25上午11:32:02
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
	 * TODO 解析配置文件获取回送反填消息所需信息
	 * 
	 * @return List<Map<String,String>>
	 * @author lijj created on 2011-8-25上午11:32:02
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
