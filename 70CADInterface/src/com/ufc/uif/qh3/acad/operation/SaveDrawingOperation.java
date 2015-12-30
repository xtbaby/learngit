package com.ufc.uif.qh3.acad.operation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ufc.uif.tcuacommunication.objects.WSObject;
import com.ufc.uif.tcuacommunication.operation.ITCFileManagement;
import com.ufc.uif.tcuacommunication.operation.ITCObjectOperation;
import com.ufc.uif.tcuacommunication.operation.ITCTCObjOperation;

import com.ufc.uif.tcuacommunication.operation.exception.TCOperationException;
import com.ufc.uif.util.service.ServiceUtil;

public class SaveDrawingOperation {

	// private ObjectOperation op;
	private ITCObjectOperation op;

	private ITCTCObjOperation tcop;

	public SaveDrawingOperation() {

		this.op = (ITCObjectOperation) ServiceUtil.getService(ITCObjectOperation.class.getName(), SaveDrawingOperation.class.getClassLoader());
		this.tcop = (ITCTCObjOperation) ServiceUtil.getService(ITCTCObjOperation.class.getName(), SaveDrawingOperation.class.getClassLoader());
	}

	/**
	 * 查找请求中的Item是否已经存在
	 * 
	 * @author Liugz
	 * @create on 2009-2-24 This project for CAD_Concrete
	 * @param itemId
	 * @param itemType
	 * @return
	 * @throws TCOperationException
	 *             ModelObject
	 */
	public WSObject findItem(String itemId, String queryName) throws TCOperationException {
		// 先查询该Item是否已存在，通过系统中已存在查询条件 --"__Query_DesPart"
		// String itemfind = "__Query_DesPart";
		WSObject[] wsObjs = null;
		wsObjs = op.findObjectsBySavedQuery(queryName, new String[] { itemId });

		if (null == wsObjs || wsObjs.length == 0) {
			return null;
		}
		return wsObjs[0];
	}

	public WSObject findAllItem(String itemId) throws TCOperationException {
		// 先查询该Item是否已存在，通过系统中已存在查询条件 --"__Query_DesPart"
		String itemfind = "Item ID";
		WSObject[] wsObjs = null;
		wsObjs = op.findObjectsBySavedQuery(itemfind, new String[] { itemId });

		if (null == wsObjs || wsObjs.length == 0) {
			return null;
		}
		return wsObjs[0];
	}

	/**
	 * 
	 * TODO 根据产品代号查找项目对象
	 * 
	 * @return WSObject
	 * @author lijj created on 2011-8-31下午03:32:53
	 * 
	 */

	public WSObject findProject(String productID) throws TCOperationException {

		String itemfind = "__Query_TCProject";

		WSObject[] wsObjs = null;
		wsObjs = op.findObjectsBySavedQuery(itemfind, new String[] { productID });
		if (null == wsObjs || wsObjs.length == 0) {
			return null;
		}

		return wsObjs[0];
	}

	/**
	 * 
	 * TODO 根据参数itemfind指定的查询构建器查找项目对象
	 * 
	 * @param itemfind
	 *            (查询构建器名称)
	 * @param productID
	 * @return WSObject
	 * @author zhangwh created on 2012-11-05 上午10:50
	 * 
	 */
	public WSObject findProject(String itemfind, String productID) throws TCOperationException {

		WSObject[] wsObjs = null;
		wsObjs = op.findObjectsBySavedQuery(itemfind, new String[] { "*" + productID + "*" });

		if (null == wsObjs || wsObjs.length == 0) {
			return null;
		}

		return wsObjs[0];
		// return wsObjs;rongjw
	}

	/**
	 * 使用系统中保存的查询条件进行查询操作
	 * 
	 * @author Liugz
	 * @create on 2009-2-25 This project for CAD_Concrete
	 * @param itemId
	 * @param queryName
	 * @return
	 * @throws TCOperationException
	 */
	public WSObject[] useSavedQuery(String[] criterias, String queryName) throws TCOperationException {
		WSObject[] mObjs = op.findObjectsBySavedQuery(queryName, criterias);
		if (null == mObjs || mObjs.length == 0) {
			return null;
		}
		return mObjs;
	}

	/**
	 * 如果请求中指定的Item不存在，则直接创建。
	 * 
	 * @author Liugz
	 * @create on 2008-12-22 This project for CYM
	 * @param propsList
	 * @return
	 * @throws TCOperationException
	 */
	public WSObject createItem(String id, List<Map<String, String>> propsList, WSObject folder) throws TCOperationException {
		// 接口传来只传来一张图纸
		WSObject[] itemsCreated = new WSObject[1];
		Map<String, String> propertyName = new HashMap<String, String>();
		propertyName.put("itemId", "产品编码");
		propertyName.put("name", "名称");

		propsList.get(0).put("产品编码", id);

		// 创建Item
		itemsCreated = op.createNewItems(folder, "Item", propsList, propertyName);
		if (null == itemsCreated || itemsCreated.length == 0) {
			throw new TCOperationException("创建Item时发生错误！");
		}
		return itemsCreated[0];
	}

	public WSObject createItem(WSObject folder, String type, List<Map<String, String>> propsList, Map<String, String> propertyName) throws TCOperationException {
		// 接口传来只传来一张图纸
		WSObject[] itemsCreated = new WSObject[1];
		itemsCreated = op.createNewItems(folder, type, propsList, propertyName);
		if (null == itemsCreated || itemsCreated.length == 0) {
			throw new TCOperationException("创建Item时发生错误！");
		}
		return itemsCreated[0];
	}

	/**
	 * 
	 * TODO 单个item对象的创建
	 * 
	 * @return WSObject
	 * @author lijj created on 2011-1-6上午11:54:48
	 */
	public WSObject createItem(WSObject folder, String type, String itemID, String itemName) throws TCOperationException {
		WSObject[] itemsCreated = new WSObject[1];
		String[] itemIDs = new String[] { itemID };
		String[] itemNames = new String[] { itemName };

		itemsCreated = tcop.createItems(folder, type, itemIDs, itemNames);
		if (null == itemsCreated || itemsCreated.length == 0) {
			throw new TCOperationException("创建Item时发生错误！");
		}
		return itemsCreated[0];
	}

	/**
	 * 检查Item下的最新ItemRevisioin下Dataset的状态， 是否存在及其检出状态等
	 * 键值existed指示是否存在，checkout指示检出状态
	 * 
	 * @author Liugz
	 * @create on 2008-12-22 This project for CYM
	 * @param item
	 * @param datasetName
	 * @param 返回Item的签出状态
	 *            ，0--表示未被签出；1--表示已被别人签出；2--表示被自己签出
	 * @return
	 * @throws TCOperationException
	 */
	public Map<String, String> checkDataset(WSObject item, String datasetName) throws TCOperationException {
		Map<String, String> retnMap = new HashMap<String, String>();
		// 获取最新的ItemRevisioin
		WSObject item_rev = tcop.getItemRevision(item, null);
		System.out.println("item_rev=:" + item_rev.getType());

		// String[] dsTypes = new String[] { "Qh3_AutoCAD" }; // 数据集的类型
		String[] dsTypes = new String[] { "PM8ACADDWG" }; // 数据集的类型

		WSObject[] datasets = tcop.getDatasetOfItemRevision(item_rev, dsTypes, "IMAN_specification");
		// 检查传入的Dataset是否存在
		String isExistedDS = "false";
		String datasetCheckouted = ""; // 返回Item的签出状态，0--表示未被签出；1--表示已被别人签出；2--表示被自己签出

		if (datasets == null) {
			retnMap.put("existed", isExistedDS);
		} else {
			for (int i = 0; i < datasets.length; i++) {
				String name = (String) op.getPropertyOfObject(datasets[i], "object_name", String.class);
				if (datasetName.equals(name)) {
					isExistedDS = "true";
					if (!op.isCheckOut(datasets[i])) {
						datasetCheckouted = "0";
					} else {
						if (!op.isCheckOutByUser(datasets[i]))
							datasetCheckouted = "1";
						else
							datasetCheckouted = "2";
					}
				}
			}
			retnMap.put("existed", isExistedDS);
			retnMap.put("checkout", datasetCheckouted);
		}
		return retnMap;
	}

	/**
	 * 
	 * TODO 检查Item下的最新ItemRevisioin下Dataset的状态， 是否存在及其检出状态等
	 * 键值existed指示是否存在，checkout指示检出状态
	 * 
	 * @return Map<String,String>
	 * @param 返回Item的签出状态
	 *            ，0--表示未被签出；1--表示已被别人签出；2--表示被自己签出
	 * @author lijj created on 2011-1-28下午03:47:58
	 * @throws TCOperationException
	 */
	public Map<String, String> checkDataset(String dsType, WSObject item, String datasetName) throws TCOperationException {
		Map<String, String> retnMap = new HashMap<String, String>();
		// 获取最新的ItemRevisioin
		WSObject item_rev = tcop.getItemRevision(item, null);
		System.out.println("item_rev=:" + item_rev.getType());

		String[] dsTypes = new String[] { dsType }; // 数据集的类型

		WSObject[] datasets = tcop.getDatasetOfItemRevision(item_rev, dsTypes, "IMAN_specification");
		// 检查传入的Dataset是否存在
		String isExistedDS = "false";
		String datasetCheckouted = ""; // 返回Item的签出状态，0--表示未被签出；1--表示已被别人签出；2--表示被自己签出

		if (datasets == null) {
			retnMap.put("existed", isExistedDS);
		} else {
			for (int i = 0; i < datasets.length; i++) {
				String name = (String) op.getPropertyOfObject(datasets[i], "object_name", String.class);
				if (datasetName.equals(name)) {
					isExistedDS = "true";
					if (!op.isCheckOut(datasets[i])) {
						datasetCheckouted = "0";
					} else {
						if (!op.isCheckOutByUser(datasets[i]))
							datasetCheckouted = "1";
						else
							datasetCheckouted = "2";
					}
				}
			}
			retnMap.put("existed", isExistedDS);
			retnMap.put("checkout", datasetCheckouted);
		}
		return retnMap;
	}

	/**
	 * 获取Item上最新的ItemRevisioin上的属性信息
	 * 
	 * @author Liugz
	 * @create on 2008-12-22 This project for CYM
	 * @param item
	 * @return
	 * @throws TCOperationException
	 */
	public Map<String, String> getItemRevFormInfo(WSObject item) throws TCOperationException {
		// 获取最新的ItemRevisioin，并查询ItemRevisioin上的属性信息
		WSObject[] itemRevs = op.getItemRevisions(item, -1);
		WSObject lastestItemRev = itemRevs[itemRevs.length - 1];
		String[] propNames = op.getPropertyNamesOfItemRevision(lastestItemRev);
		WSObject form = op.getFormInfo(lastestItemRev);
		if (null == form) {
			return null;
		}
		Map<String, String> properties = new HashMap<String, String>();
		for (String name : propNames) {
			String value = op.getPropertyOfObject(form, name);
			properties.put(name, value);
		}
		return properties;
	}

	/**
	 * 创建或者更新Dataset
	 * 
	 * @author Liugz
	 * @create on 2008-12-22 This project for CYM
	 * @param item
	 * @param datasetName
	 * @param isNew
	 *            -- 创建还是更新
	 * @param keepEditable
	 *            -- 是否保持可编辑状态
	 * @param drawingList
	 * @return
	 * @throws TCOperationException
	 */
	public boolean createOrUpdateDataset(WSObject item, String datasetName, boolean isNew, boolean keepEditable, List<Map<String, String>> drawingList) throws TCOperationException {
		// 如果是接口新建的Item，则需要新建Dataset，Dataset上的关系，并且保存ItemRevisioin Master信息
		WSObject[] itemRevs = op.getItemRevisions(item, -1);
		WSObject itemRev = itemRevs[itemRevs.length - 1];
		WSObject[] datasetArray = new WSObject[1];
		if (isNew) {
			// 新建Dataset
			// fandy WSObject[] datasets = op.createDatasets(itemRev,
			// "IMAN_specification", "ACADDWG", new String[]{datasetName});
			WSObject[] datasets = op.createDatasets(itemRev, "IMAN_specification", "Qh3_DesPart", new String[] { datasetName });
			if (null == datasets || datasets.length == 0) {
				System.out.println("Dataset创建失败");
				return false;
			}
			if (keepEditable) {
				op.checkOutObjects(datasets, "");
			}
			datasetArray = datasets;
		} else {
			// 更新Dataset，删除Dataset下原有的文件
			// WSObject[] datasets = op.getDatasetOfItemRevision(item,new
			// String[]{}, -1, new String[]{"ACADDWG"}, "IMAN_specification");
			WSObject[] datasets = tcop.getDatasetOfItemRevision(item, new String[] { "Qh3_DesPart" }, "IMAN_specification");
			if (null != datasets && datasets.length != 0) {
				WSObject dataSet = null;
				String[] datasetNames = new String[datasets.length];
				for (int i = 0; i < datasets.length; i++) {
					datasetNames[i] = (String) op.getPropertyOfObject(datasets[i], "object_name", String.class);
					if (datasetName.toString().equals(datasetNames[i])) {
						dataSet = datasets[i];
						break;
					}
				}
				op.getFilesOfDataset(dataSet);
				datasetArray[0] = dataSet;
				// 使用Checkin();
				if (!keepEditable) {
					if (!op.checkin(new WSObject[] { dataSet })) {
						throw new TCOperationException("检入Dataset时发生错误，操作失败");
					}
				}

				// 需要处理 Util.deleteDWG(dataSet);
			}
		}
		try {
			// 上传文件
			// FileManagement fileMgmt = new FileManagement();
			// context.registerService((ITCFileManagement.class).getName(), new
			// TCFileManagement(), null);
			// context.registerService((ITCFileOperation.class).getName(), new
			// TCFileOperation(), null);
			// ITCFileManagement fileMgmt = new TCFileManagement();
			ITCFileManagement fileMgmt = (ITCFileManagement) ServiceUtil.getService(ITCFileManagement.class.getName(), SaveDrawingOperation.class.getClassLoader());
			/*
			 * if (!fileMgmt.uploadFiles(datasetArray, "Qh3_dwg", drawingList))
			 * { return false; }
			 */
			if (!fileMgmt.uploadFiles(datasetArray, "PM8dwg", drawingList)) {
				return false;
			}

		} catch (RuntimeException re) {
			re.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean updateItemRev(WSObject item, Map attributes) throws TCOperationException {
		if (null == item) {
			return false;
		}
		// 获取最新的ItemRevisioin

		System.out.println(" updateItemRev item=" + item);
		// WSObject[] itemRevs = op.getItemRevisions(item, -1);
		// WSObject itemRev = itemRevs[itemRevs.length - 1];
		WSObject itemRev = tcop.getItemRevision(item, "last");
		// WSObject form = null;
		// WSObject form1 =tcop.getRevMasterForm(itemRev);

		if (!op.updateObjectProperties(itemRev, attributes))
			return false;
		return true;

	}

	/**
	 * 获取Form属性上附件的LOV值
	 * 
	 * @author Liugz
	 * @create on 2008-12-22 This project for CYM
	 * @param type
	 * @param propNames
	 * @return
	 */
	public Map<String, String[]> getValuesOfLOV(String type, String[] propNames) {
		try {
			return op.getValueOfLOV(type, propNames);
		} catch (TCOperationException e) {
			e.printStackTrace();
			System.out.println("未能获取LOV的属性值");
			return null;
		}

	}

	public String getPropertyOfProject(String scope, String propName) {
		try {
			WSObject[] project = op.getProjectInfo(scope);
			if (null == project || project.length == 0) {
				System.out.println("未能获取项目信息，" + scope + " 范围中的项目信息为空！");
				return "";
			}
			if (null == project[0]) {
				System.out.println("未能获取项目信息，Session中的项目信息可能为空！");
				return "";
			}
			String value = op.getPropertyOfObject(project[0], propName);
			return value;
		} catch (TCOperationException e) {
			e.printStackTrace();
			System.out.println("未能获取属性为 " + propName + " 的属性值");
			return "";
		}
	}

	/**
	 * 获取登录PDM的用户名
	 * 
	 * @author Liugz
	 * @create on 2008-12-22 This project for CYM
	 * @return
	 */
	public String getUserName() {
		String name = "";
		// 注意修改
		/*
		 * try { name = op.getUser().get_user_name(); } catch
		 * (NotLoadedException e) { System.out.println("未能获取用户名信息"); }
		 */
		return name;
	}

	/**
	 * 获取Teamcenter中的Home文件夹
	 * 
	 * @author Liugz
	 * @create on 2008-12-22 This project for CYM
	 * @return
	 */
	public WSObject getHomeFolder() {
		WSObject home = null;

		try {
			home = tcop.getHomeFolder();
		} catch (TCOperationException e) {
			System.out.println("未能获取Home文件夹信息");
		}

		//

		return home;
	}

	/**
	 * 获取ItemRevisioin上的属性值
	 * 
	 * @author Liugz
	 * @create on 2008-12-22 This project for CYM
	 * @param obj
	 * @param name
	 * @return
	 */
	public String getPropertyValue(WSObject obj, String name) {
		String value = "";
		try {
			WSObject[] itemRevs = op.getItemRevisions(obj, -1);
			WSObject itemRev = itemRevs[itemRevs.length - 1];
			value = op.getPropertyOfObject(itemRev, name);
		} catch (TCOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * 获取ItemRevisioin上的属性值
	 * 
	 * @author Liugz
	 * @create on 2008-12-22 This project for CYM
	 * @param obj
	 * @param name
	 * @return
	 */
	public String getOwnUser(WSObject obj) {
		return op.getOwnsUser(obj);
	}

	/**
	 * 函数功能：列出给定Folder下的内容
	 * 
	 * @param parent
	 *            ：要列出其下内容的Folder
	 * @param listall
	 *            ：true：列出所有对象（包括item、folder等），false：仅列出Folder
	 * @author yangwb
	 * @return
	 */
	public WSObject[] listContentsofFolder(WSObject parent1, boolean listall) {
		WSObject[] ret = null;
		try {
			ret = op.listContentsofFolder(parent1, listall);
		} catch (TCOperationException e) {
			// TODO Auto-generated catch block
			System.out.println("listContentsofFolder 获取文件夹异常");
		}
		return ret;
	}

	/**
	 * 获取文件夹的名称
	 * 
	 * @author Liugz
	 * @create on 2008-12-22 This project for CYM
	 * @param folder
	 * @return
	 */
	public String getNameOfFolder(WSObject folder) {
		String name = "";
		try {
			name = op.getPropertyOfObject(folder, "object_name");
		} catch (TCOperationException e) {
			// TODO Auto-generated catch block
			System.out.println("getNameOfFolder  获取文件夹名称异常");
		}
		return name;
	}

	/**
	 * 检查用户权限
	 * 
	 * @author Liugz
	 * @create on 2008-12-22 This project for CYM
	 * @param obj
	 * @param privilegeName
	 * @return
	 */
	public boolean checkPrivilege(WSObject obj, String privilegeName) {
		// 客户化查找

		try {
			return op.checkPrivilege(obj, privilegeName);
		} catch (TCOperationException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			return false;
		}

	}

	public WSObject getReferencedFolder(WSObject item) {
		WSObject folder = null;
		try {
			folder = op.getReferencedFolder(item);
		} catch (TCOperationException e) {
			e.printStackTrace();
			return null;
		}
		return folder;
	}

	public boolean createRelation(WSObject item, WSObject container, String relationType) {
		try {
			tcop.createRelationship(container, item, relationType);
		} catch (TCOperationException e) {
			System.out.println("创建 " + relationType + "关系时，发生错误！");
			return false;
		}
		return true;
	}

	public WSObject getLastestItemRevision(WSObject item) {
		WSObject itemRev = null;
		if (null == item) {
			return null;
		}
		try {
			// 获取最新的ItemRevisioin
			// WSObject[] itemRevs = op.getItemRevisions(item, -1);
			// itemRev = itemRevs[itemRevs.length - 1];
			itemRev = tcop.getItemRevision(item, "last");
		} catch (TCOperationException te) {
			return itemRev;
		}
		System.out.println(null == itemRev ? "未能取到item的版本对象" : "成功取到item的rev对象");
		return itemRev;
	}

	public boolean updateObjectProps(WSObject object, Map<String, String> props) {
		if (null == object) {
			return false;
		}
		try {
			if (op.updateObjectProperties(object, props))
				return true;
		} catch (TCOperationException e) {
			return false;
		}
		return false;
	}

	/**
	 * 创建数据集
	 * 
	 * @author Liugz
	 * @create on 2009-2-26 This project for CAD_Concrete
	 * @param item
	 * @param datasetName
	 * @param isNew
	 * @param keepEditable
	 * @param drawingList
	 * @return
	 * @throws TCOperationException
	 */
	public WSObject createDataset(WSObject item, String datasetName, boolean keepEditable) throws TCOperationException {
		// 如果是接口新建的Item，则需要新建Dataset，Dataset上的关系，并且保存ItemRevisioin Master信息
		WSObject[] itemRevs = op.getItemRevisions(item, -1);
		WSObject itemRev = itemRevs[itemRevs.length - 1];
		WSObject dataset = null;

		// WSObject[] datasets = op.createDatasets(itemRev,
		// "IMAN_specification","Qh3_AutoCAD", new String[] { datasetName });

		WSObject[] datasets = op.createDatasets(itemRev, "IMAN_specification", "Ne7_AutoCAD", new String[] { datasetName });

		if (null == datasets || datasets.length == 0) {
			System.out.println("Dataset创建失败");
			throw new TCOperationException("创建Dataset时发生错误，请确认图纸信息正确。");
		}
		if (keepEditable) {
			op.checkOutObjects(datasets, "");
		}
		dataset = datasets[0];
		return dataset;
	}

	/**
	 * 
	 * TODO 创建数据集
	 * 
	 * @return WSObject
	 * @author lijj created on 2011-1-28下午03:51:47
	 */
	public WSObject createDataset(String dsType, WSObject item, String datasetName, boolean keepEditable) throws TCOperationException {
		// 如果是接口新建的Item，则需要新建Dataset，Dataset上的关系，并且保存ItemRevisioin Master信息
		WSObject[] itemRevs = op.getItemRevisions(item, -1);
		WSObject itemRev = itemRevs[itemRevs.length - 1];
		WSObject dataset = null;
		// 新建Dataset
		// fandy ModelObject[] datasets = op.createDatasets(itemRev,
		// "IMAN_specification", "ACADDWG", new String[]{datasetName});
		WSObject[] datasets = op.createDatasets(itemRev, "IMAN_specification", dsType, new String[] { datasetName });

		if (null == datasets || datasets.length == 0) {
			System.out.println("Dataset创建失败");
			throw new TCOperationException("创建Dataset时发生错误，请确认图纸信息正确。");
		}
		if (keepEditable) {
			op.checkOutObjects(datasets, "");
		}
		dataset = datasets[0];
		return dataset;
	}

	/**
	 * 如果数据集已经存在，则在最新的ItemRevisioin下找到指定的数据集
	 * 
	 * @author Liugz
	 * @create on 2009-2-26 This project for CAD_Concrete
	 * @param dataName
	 * @param keepEditable
	 * @return
	 * @throws TCOperationException
	 */
	public WSObject findDatasetOnItemRev(WSObject item, String datasetName, boolean keepEditable) throws TCOperationException {
		WSObject dataset = null;

		/*
		 * WSObject[] datasets = tcop.getDatasetOfItemRevision(
		 * getLastestItemRevision(item), new String[] { "Qh3_AutoCAD" },
		 * "IMAN_specification");
		 */

		WSObject[] datasets = tcop.getDatasetOfItemRevision(getLastestItemRevision(item), new String[] { "PM8ACADDWG" }, "IMAN_specification");

		if (null != datasets && datasets.length != 0) {
			WSObject dataSet = null;
			String[] datasetNames = new String[datasets.length];
			for (int i = 0; i < datasets.length; i++) {
				datasetNames[i] = (String) op.getPropertyOfObject(datasets[i], "object_name", String.class);
				if (datasetName.equals(datasetNames[i])) {
					dataSet = datasets[i];
					break;
				}
			}
			// op.getFilesOfDataset(dataSet);
			tcop.getFilesOfDataset(dataSet);
			dataset = dataSet;
			// 使用Checkin();
			if (!keepEditable) {
				if (!op.checkin(new WSObject[] { dataSet })) {
					op.checkin(new WSObject[] { dataSet });
					// throw new TCOperationException("检入Dataset时发生错误，操作失败");
				}
			}
		}
		return dataset;
	}

	/**
	 * 
	 * TODO 如果数据集已经存在，则在最新的ItemRevisioin下找到指定的数据集
	 * 
	 * @return WSObject
	 * @author lijj created on 2011-1-28下午03:51:05
	 */
	public WSObject findDatasetOnItemRev(String refName, WSObject item, String datasetName, boolean keepEditable) throws TCOperationException {
		WSObject dataset = null;
		// 更新Dataset，删除Dataset下原有的文件
		WSObject[] datasets = tcop.getDatasetOfItemRevision(getLastestItemRevision(item), new String[] { refName }, "IMAN_specification");
		if (null != datasets && datasets.length != 0) {
			WSObject dataSet = null;
			String[] datasetNames = new String[datasets.length];
			for (int i = 0; i < datasets.length; i++) {
				datasetNames[i] = (String) op.getPropertyOfObject(datasets[i], "object_name", String.class);
				if (datasetName.equals(datasetNames[i])) {
					dataSet = datasets[i];
					break;
				}
			}
			tcop.getFilesOfDataset(dataSet);
			dataset = dataSet;
			// 使用Checkin();
			if (!keepEditable) {
				if (!op.checkin(new WSObject[] { dataSet })) {
					op.checkin(new WSObject[] { dataSet });
				}
			}
		}
		return dataset;
	}

	/**
	 * 上传文件
	 * 
	 * @author Liugz
	 * @create on 2009-2-26 This project for CAD_Concrete
	 * @return
	 * @throws TCOperationException
	 */
	public boolean uploadFile(WSObject dataset, List<Map<String, String>> drawingList) throws TCOperationException {
		WSObject[] datasetArray = new WSObject[1];
		datasetArray[0] = dataset;
		try {
			// 上传文件
			// ITCFileManagement fileMgmt = new TCFileManagement();
			ITCFileManagement fileMgmt = (ITCFileManagement) ServiceUtil.getService(ITCFileManagement.class.getName(), SaveDrawingOperation.class.getClassLoader());
			// context.registerService((ITCFileOperation.class).getName(), new
			// TCFileOperation(), null);
			// 命名引用
			/*
			 * if (!fileMgmt.uploadFiles(datasetArray, "Qh3_dwg", drawingList))
			 * { throw new TCOperationException("上传文件时发生错误，可能由于文件位置不对或网络原因引起。");
			 * }
			 */
			if (!fileMgmt.uploadFiles(datasetArray, "PM8dwg", drawingList)) {
				throw new TCOperationException("上传文件时发生错误，可能由于文件位置不对或网络原因引起。");
			}
		} catch (RuntimeException re) {
			re.printStackTrace();
			throw new TCOperationException("上传文件时发生错误，可能由于文件位置不对或网络原因引起。");
		} catch (TCOperationException e) {
			e.printStackTrace();
			throw new TCOperationException("上传文件时发生错误，可能由于文件位置不对或网络原因引起。");
		}
		return true;
	}

	/**
	 * 上传文件
	 * 
	 * @author Liugz
	 * @create on 2009-2-26 This project for CAD_Concrete
	 * @return
	 * @throws TCOperationException
	 */
	public boolean uploadFiles(WSObject[] datasets, List<Map<String, String>> drawingList) throws TCOperationException {
		WSObject[] datasetArray = datasets;
		try {
			// 上传文件

			ITCFileManagement fileMgmt = (ITCFileManagement) ServiceUtil.getService(ITCFileManagement.class.getName(), SaveDrawingOperation.class.getClassLoader());

			/*
			 * if (!fileMgmt.uploadFiles(datasetArray, "Qh3_dwg", drawingList))
			 * { throw new TCOperationException("上传文件时发生错误，可能由于文件位置不对或网络原因引起。");
			 * }
			 */
			if (!fileMgmt.uploadFiles(datasetArray, "PM8dwg", drawingList)) {
				throw new TCOperationException("上传文件时发生错误，可能由于文件位置不对或网络原因引起。");
			}

		} catch (RuntimeException re) {
			re.printStackTrace();
			throw new TCOperationException("上传文件时发生错误，可能由于文件位置不对或网络原因引起。");
		} catch (TCOperationException e) {
			e.printStackTrace();
			throw new TCOperationException("上传文件时发生错误，可能由于文件位置不对或网络原因引起。");
		}
		return true;
	}

	/**
	 * 
	 * TODO 上传文件
	 * 
	 * @return boolean
	 * @author lijj created on 2011-1-28下午03:49:15
	 */
	public boolean uploadFiles(String refName, WSObject[] datasets, List<Map<String, String>> drawingList) throws TCOperationException {
		WSObject[] datasetArray = datasets;
		try {
			// 上传文件
			ITCFileManagement fileMgmt = (ITCFileManagement) ServiceUtil.getService(ITCFileManagement.class.getName(), SaveDrawingOperation.class.getClassLoader());
			if (!fileMgmt.uploadFiles(datasetArray, refName, drawingList)) {
				throw new TCOperationException("上传文件时发生错误，可能由于文件位置不对或网络原因引起。");
			}
		} catch (RuntimeException re) {
			re.printStackTrace();
			throw new TCOperationException("上传文件时发生错误，可能由于文件位置不对或网络原因引起。");
		} catch (TCOperationException e) {
			e.printStackTrace();
			throw new TCOperationException("上传文件时发生错误，可能由于文件位置不对或网络原因引起。");
		}
		return true;
	}

	/**
	 * 获取首选项的值
	 * 
	 * @author Liugz
	 * @create on 2009-7-2 This project for CAD_Concrete
	 * @param preferenceName
	 * @param scope
	 * @return
	 */
	public String[] getPreferenceValue(String preferenceName, String scope) {
		try {
			return op.getPreferenceValues(preferenceName, scope);

		} catch (TCOperationException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 刷新指定对象
	 * 
	 * @author xukun
	 * @create on 2009-8-5
	 * @param objectName
	 * @return boolean
	 */
	public boolean refreshWSObject(WSObject item) {
		WSObject[] modelojbects = new WSObject[1];
		modelojbects[0] = item;
		/*
		 * try { op.refreshTCObjects(modelojbects); return true; } catch
		 * (RuntimeException re) { re.printStackTrace(); return false; }
		 */
		try {
			op.refreshTCObjects(modelojbects);
		} catch (TCOperationException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param object
	 * @return
	 * @throws TCOperationException
	 */
	/*
	 * public WSObject getFormInfo(WSObject object,String type) throws
	 * TCOperationException { WSObject form= null; try { //if
	 * (object.getType().isInstanceOf("ItemRevision")) if
	 * (object.getType().equals(("ItemRevision"))) { ItemRevision itemRev =
	 * (ItemRevision) object; // form = //
	 * itemRev.get_item_master_tag();//.get_IMAN_master_form_rev(); WSObject[]
	 * model = itemRev.get_IMAN_specification(); if (model.length != 0) { for
	 * (int i = 0; i < model.length; i++) { String type_Name =
	 * model[i].getType().getName();
	 * System.out.println(model[i].getType().getName()); if
	 * (type_Name.equalsIgnoreCase("form")) { String
	 * ty_name=model[i].getProperty("object_type").getStringValue(); if
	 * (ty_name.equalsIgnoreCase(type)) form=model[i]; } } } } else throw new
	 * TCOperationException("传入的对象类型有误，操作失败。"); } catch (NotLoadedException e) {
	 * throw new TCOperationException("未能正确获取对象的属性，操作失败。"); } return form; }
	 */
	public WSObject getFormInfo(WSObject object) throws TCOperationException {
		WSObject form = op.getFormInfo(object);

		return form;
	}
}
