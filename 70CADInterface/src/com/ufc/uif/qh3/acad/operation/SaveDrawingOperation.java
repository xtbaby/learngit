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
	 * ���������е�Item�Ƿ��Ѿ�����
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
		// �Ȳ�ѯ��Item�Ƿ��Ѵ��ڣ�ͨ��ϵͳ���Ѵ��ڲ�ѯ���� --"__Query_DesPart"
		// String itemfind = "__Query_DesPart";
		WSObject[] wsObjs = null;
		wsObjs = op.findObjectsBySavedQuery(queryName, new String[] { itemId });

		if (null == wsObjs || wsObjs.length == 0) {
			return null;
		}
		return wsObjs[0];
	}

	public WSObject findAllItem(String itemId) throws TCOperationException {
		// �Ȳ�ѯ��Item�Ƿ��Ѵ��ڣ�ͨ��ϵͳ���Ѵ��ڲ�ѯ���� --"__Query_DesPart"
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
	 * TODO ���ݲ�Ʒ���Ų�����Ŀ����
	 * 
	 * @return WSObject
	 * @author lijj created on 2011-8-31����03:32:53
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
	 * TODO ���ݲ���itemfindָ���Ĳ�ѯ������������Ŀ����
	 * 
	 * @param itemfind
	 *            (��ѯ����������)
	 * @param productID
	 * @return WSObject
	 * @author zhangwh created on 2012-11-05 ����10:50
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
	 * ʹ��ϵͳ�б���Ĳ�ѯ�������в�ѯ����
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
	 * ���������ָ����Item�����ڣ���ֱ�Ӵ�����
	 * 
	 * @author Liugz
	 * @create on 2008-12-22 This project for CYM
	 * @param propsList
	 * @return
	 * @throws TCOperationException
	 */
	public WSObject createItem(String id, List<Map<String, String>> propsList, WSObject folder) throws TCOperationException {
		// �ӿڴ���ֻ����һ��ͼֽ
		WSObject[] itemsCreated = new WSObject[1];
		Map<String, String> propertyName = new HashMap<String, String>();
		propertyName.put("itemId", "��Ʒ����");
		propertyName.put("name", "����");

		propsList.get(0).put("��Ʒ����", id);

		// ����Item
		itemsCreated = op.createNewItems(folder, "Item", propsList, propertyName);
		if (null == itemsCreated || itemsCreated.length == 0) {
			throw new TCOperationException("����Itemʱ��������");
		}
		return itemsCreated[0];
	}

	public WSObject createItem(WSObject folder, String type, List<Map<String, String>> propsList, Map<String, String> propertyName) throws TCOperationException {
		// �ӿڴ���ֻ����һ��ͼֽ
		WSObject[] itemsCreated = new WSObject[1];
		itemsCreated = op.createNewItems(folder, type, propsList, propertyName);
		if (null == itemsCreated || itemsCreated.length == 0) {
			throw new TCOperationException("����Itemʱ��������");
		}
		return itemsCreated[0];
	}

	/**
	 * 
	 * TODO ����item����Ĵ���
	 * 
	 * @return WSObject
	 * @author lijj created on 2011-1-6����11:54:48
	 */
	public WSObject createItem(WSObject folder, String type, String itemID, String itemName) throws TCOperationException {
		WSObject[] itemsCreated = new WSObject[1];
		String[] itemIDs = new String[] { itemID };
		String[] itemNames = new String[] { itemName };

		itemsCreated = tcop.createItems(folder, type, itemIDs, itemNames);
		if (null == itemsCreated || itemsCreated.length == 0) {
			throw new TCOperationException("����Itemʱ��������");
		}
		return itemsCreated[0];
	}

	/**
	 * ���Item�µ�����ItemRevisioin��Dataset��״̬�� �Ƿ���ڼ�����״̬��
	 * ��ֵexistedָʾ�Ƿ���ڣ�checkoutָʾ���״̬
	 * 
	 * @author Liugz
	 * @create on 2008-12-22 This project for CYM
	 * @param item
	 * @param datasetName
	 * @param ����Item��ǩ��״̬
	 *            ��0--��ʾδ��ǩ����1--��ʾ�ѱ�����ǩ����2--��ʾ���Լ�ǩ��
	 * @return
	 * @throws TCOperationException
	 */
	public Map<String, String> checkDataset(WSObject item, String datasetName) throws TCOperationException {
		Map<String, String> retnMap = new HashMap<String, String>();
		// ��ȡ���µ�ItemRevisioin
		WSObject item_rev = tcop.getItemRevision(item, null);
		System.out.println("item_rev=:" + item_rev.getType());

		// String[] dsTypes = new String[] { "Qh3_AutoCAD" }; // ���ݼ�������
		String[] dsTypes = new String[] { "PM8ACADDWG" }; // ���ݼ�������

		WSObject[] datasets = tcop.getDatasetOfItemRevision(item_rev, dsTypes, "IMAN_specification");
		// ��鴫���Dataset�Ƿ����
		String isExistedDS = "false";
		String datasetCheckouted = ""; // ����Item��ǩ��״̬��0--��ʾδ��ǩ����1--��ʾ�ѱ�����ǩ����2--��ʾ���Լ�ǩ��

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
	 * TODO ���Item�µ�����ItemRevisioin��Dataset��״̬�� �Ƿ���ڼ�����״̬��
	 * ��ֵexistedָʾ�Ƿ���ڣ�checkoutָʾ���״̬
	 * 
	 * @return Map<String,String>
	 * @param ����Item��ǩ��״̬
	 *            ��0--��ʾδ��ǩ����1--��ʾ�ѱ�����ǩ����2--��ʾ���Լ�ǩ��
	 * @author lijj created on 2011-1-28����03:47:58
	 * @throws TCOperationException
	 */
	public Map<String, String> checkDataset(String dsType, WSObject item, String datasetName) throws TCOperationException {
		Map<String, String> retnMap = new HashMap<String, String>();
		// ��ȡ���µ�ItemRevisioin
		WSObject item_rev = tcop.getItemRevision(item, null);
		System.out.println("item_rev=:" + item_rev.getType());

		String[] dsTypes = new String[] { dsType }; // ���ݼ�������

		WSObject[] datasets = tcop.getDatasetOfItemRevision(item_rev, dsTypes, "IMAN_specification");
		// ��鴫���Dataset�Ƿ����
		String isExistedDS = "false";
		String datasetCheckouted = ""; // ����Item��ǩ��״̬��0--��ʾδ��ǩ����1--��ʾ�ѱ�����ǩ����2--��ʾ���Լ�ǩ��

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
	 * ��ȡItem�����µ�ItemRevisioin�ϵ�������Ϣ
	 * 
	 * @author Liugz
	 * @create on 2008-12-22 This project for CYM
	 * @param item
	 * @return
	 * @throws TCOperationException
	 */
	public Map<String, String> getItemRevFormInfo(WSObject item) throws TCOperationException {
		// ��ȡ���µ�ItemRevisioin������ѯItemRevisioin�ϵ�������Ϣ
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
	 * �������߸���Dataset
	 * 
	 * @author Liugz
	 * @create on 2008-12-22 This project for CYM
	 * @param item
	 * @param datasetName
	 * @param isNew
	 *            -- �������Ǹ���
	 * @param keepEditable
	 *            -- �Ƿ񱣳ֿɱ༭״̬
	 * @param drawingList
	 * @return
	 * @throws TCOperationException
	 */
	public boolean createOrUpdateDataset(WSObject item, String datasetName, boolean isNew, boolean keepEditable, List<Map<String, String>> drawingList) throws TCOperationException {
		// ����ǽӿ��½���Item������Ҫ�½�Dataset��Dataset�ϵĹ�ϵ�����ұ���ItemRevisioin Master��Ϣ
		WSObject[] itemRevs = op.getItemRevisions(item, -1);
		WSObject itemRev = itemRevs[itemRevs.length - 1];
		WSObject[] datasetArray = new WSObject[1];
		if (isNew) {
			// �½�Dataset
			// fandy WSObject[] datasets = op.createDatasets(itemRev,
			// "IMAN_specification", "ACADDWG", new String[]{datasetName});
			WSObject[] datasets = op.createDatasets(itemRev, "IMAN_specification", "Qh3_DesPart", new String[] { datasetName });
			if (null == datasets || datasets.length == 0) {
				System.out.println("Dataset����ʧ��");
				return false;
			}
			if (keepEditable) {
				op.checkOutObjects(datasets, "");
			}
			datasetArray = datasets;
		} else {
			// ����Dataset��ɾ��Dataset��ԭ�е��ļ�
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
				// ʹ��Checkin();
				if (!keepEditable) {
					if (!op.checkin(new WSObject[] { dataSet })) {
						throw new TCOperationException("����Datasetʱ�������󣬲���ʧ��");
					}
				}

				// ��Ҫ���� Util.deleteDWG(dataSet);
			}
		}
		try {
			// �ϴ��ļ�
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
		// ��ȡ���µ�ItemRevisioin

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
	 * ��ȡForm�����ϸ�����LOVֵ
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
			System.out.println("δ�ܻ�ȡLOV������ֵ");
			return null;
		}

	}

	public String getPropertyOfProject(String scope, String propName) {
		try {
			WSObject[] project = op.getProjectInfo(scope);
			if (null == project || project.length == 0) {
				System.out.println("δ�ܻ�ȡ��Ŀ��Ϣ��" + scope + " ��Χ�е���Ŀ��ϢΪ�գ�");
				return "";
			}
			if (null == project[0]) {
				System.out.println("δ�ܻ�ȡ��Ŀ��Ϣ��Session�е���Ŀ��Ϣ����Ϊ�գ�");
				return "";
			}
			String value = op.getPropertyOfObject(project[0], propName);
			return value;
		} catch (TCOperationException e) {
			e.printStackTrace();
			System.out.println("δ�ܻ�ȡ����Ϊ " + propName + " ������ֵ");
			return "";
		}
	}

	/**
	 * ��ȡ��¼PDM���û���
	 * 
	 * @author Liugz
	 * @create on 2008-12-22 This project for CYM
	 * @return
	 */
	public String getUserName() {
		String name = "";
		// ע���޸�
		/*
		 * try { name = op.getUser().get_user_name(); } catch
		 * (NotLoadedException e) { System.out.println("δ�ܻ�ȡ�û�����Ϣ"); }
		 */
		return name;
	}

	/**
	 * ��ȡTeamcenter�е�Home�ļ���
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
			System.out.println("δ�ܻ�ȡHome�ļ�����Ϣ");
		}

		//

		return home;
	}

	/**
	 * ��ȡItemRevisioin�ϵ�����ֵ
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
	 * ��ȡItemRevisioin�ϵ�����ֵ
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
	 * �������ܣ��г�����Folder�µ�����
	 * 
	 * @param parent
	 *            ��Ҫ�г��������ݵ�Folder
	 * @param listall
	 *            ��true���г����ж��󣨰���item��folder�ȣ���false�����г�Folder
	 * @author yangwb
	 * @return
	 */
	public WSObject[] listContentsofFolder(WSObject parent1, boolean listall) {
		WSObject[] ret = null;
		try {
			ret = op.listContentsofFolder(parent1, listall);
		} catch (TCOperationException e) {
			// TODO Auto-generated catch block
			System.out.println("listContentsofFolder ��ȡ�ļ����쳣");
		}
		return ret;
	}

	/**
	 * ��ȡ�ļ��е�����
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
			System.out.println("getNameOfFolder  ��ȡ�ļ��������쳣");
		}
		return name;
	}

	/**
	 * ����û�Ȩ��
	 * 
	 * @author Liugz
	 * @create on 2008-12-22 This project for CYM
	 * @param obj
	 * @param privilegeName
	 * @return
	 */
	public boolean checkPrivilege(WSObject obj, String privilegeName) {
		// �ͻ�������

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
			System.out.println("���� " + relationType + "��ϵʱ����������");
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
			// ��ȡ���µ�ItemRevisioin
			// WSObject[] itemRevs = op.getItemRevisions(item, -1);
			// itemRev = itemRevs[itemRevs.length - 1];
			itemRev = tcop.getItemRevision(item, "last");
		} catch (TCOperationException te) {
			return itemRev;
		}
		System.out.println(null == itemRev ? "δ��ȡ��item�İ汾����" : "�ɹ�ȡ��item��rev����");
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
	 * �������ݼ�
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
		// ����ǽӿ��½���Item������Ҫ�½�Dataset��Dataset�ϵĹ�ϵ�����ұ���ItemRevisioin Master��Ϣ
		WSObject[] itemRevs = op.getItemRevisions(item, -1);
		WSObject itemRev = itemRevs[itemRevs.length - 1];
		WSObject dataset = null;

		// WSObject[] datasets = op.createDatasets(itemRev,
		// "IMAN_specification","Qh3_AutoCAD", new String[] { datasetName });

		WSObject[] datasets = op.createDatasets(itemRev, "IMAN_specification", "Ne7_AutoCAD", new String[] { datasetName });

		if (null == datasets || datasets.length == 0) {
			System.out.println("Dataset����ʧ��");
			throw new TCOperationException("����Datasetʱ����������ȷ��ͼֽ��Ϣ��ȷ��");
		}
		if (keepEditable) {
			op.checkOutObjects(datasets, "");
		}
		dataset = datasets[0];
		return dataset;
	}

	/**
	 * 
	 * TODO �������ݼ�
	 * 
	 * @return WSObject
	 * @author lijj created on 2011-1-28����03:51:47
	 */
	public WSObject createDataset(String dsType, WSObject item, String datasetName, boolean keepEditable) throws TCOperationException {
		// ����ǽӿ��½���Item������Ҫ�½�Dataset��Dataset�ϵĹ�ϵ�����ұ���ItemRevisioin Master��Ϣ
		WSObject[] itemRevs = op.getItemRevisions(item, -1);
		WSObject itemRev = itemRevs[itemRevs.length - 1];
		WSObject dataset = null;
		// �½�Dataset
		// fandy ModelObject[] datasets = op.createDatasets(itemRev,
		// "IMAN_specification", "ACADDWG", new String[]{datasetName});
		WSObject[] datasets = op.createDatasets(itemRev, "IMAN_specification", dsType, new String[] { datasetName });

		if (null == datasets || datasets.length == 0) {
			System.out.println("Dataset����ʧ��");
			throw new TCOperationException("����Datasetʱ����������ȷ��ͼֽ��Ϣ��ȷ��");
		}
		if (keepEditable) {
			op.checkOutObjects(datasets, "");
		}
		dataset = datasets[0];
		return dataset;
	}

	/**
	 * ������ݼ��Ѿ����ڣ��������µ�ItemRevisioin���ҵ�ָ�������ݼ�
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
			// ʹ��Checkin();
			if (!keepEditable) {
				if (!op.checkin(new WSObject[] { dataSet })) {
					op.checkin(new WSObject[] { dataSet });
					// throw new TCOperationException("����Datasetʱ�������󣬲���ʧ��");
				}
			}
		}
		return dataset;
	}

	/**
	 * 
	 * TODO ������ݼ��Ѿ����ڣ��������µ�ItemRevisioin���ҵ�ָ�������ݼ�
	 * 
	 * @return WSObject
	 * @author lijj created on 2011-1-28����03:51:05
	 */
	public WSObject findDatasetOnItemRev(String refName, WSObject item, String datasetName, boolean keepEditable) throws TCOperationException {
		WSObject dataset = null;
		// ����Dataset��ɾ��Dataset��ԭ�е��ļ�
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
			// ʹ��Checkin();
			if (!keepEditable) {
				if (!op.checkin(new WSObject[] { dataSet })) {
					op.checkin(new WSObject[] { dataSet });
				}
			}
		}
		return dataset;
	}

	/**
	 * �ϴ��ļ�
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
			// �ϴ��ļ�
			// ITCFileManagement fileMgmt = new TCFileManagement();
			ITCFileManagement fileMgmt = (ITCFileManagement) ServiceUtil.getService(ITCFileManagement.class.getName(), SaveDrawingOperation.class.getClassLoader());
			// context.registerService((ITCFileOperation.class).getName(), new
			// TCFileOperation(), null);
			// ��������
			/*
			 * if (!fileMgmt.uploadFiles(datasetArray, "Qh3_dwg", drawingList))
			 * { throw new TCOperationException("�ϴ��ļ�ʱ�������󣬿��������ļ�λ�ò��Ի�����ԭ������");
			 * }
			 */
			if (!fileMgmt.uploadFiles(datasetArray, "PM8dwg", drawingList)) {
				throw new TCOperationException("�ϴ��ļ�ʱ�������󣬿��������ļ�λ�ò��Ի�����ԭ������");
			}
		} catch (RuntimeException re) {
			re.printStackTrace();
			throw new TCOperationException("�ϴ��ļ�ʱ�������󣬿��������ļ�λ�ò��Ի�����ԭ������");
		} catch (TCOperationException e) {
			e.printStackTrace();
			throw new TCOperationException("�ϴ��ļ�ʱ�������󣬿��������ļ�λ�ò��Ի�����ԭ������");
		}
		return true;
	}

	/**
	 * �ϴ��ļ�
	 * 
	 * @author Liugz
	 * @create on 2009-2-26 This project for CAD_Concrete
	 * @return
	 * @throws TCOperationException
	 */
	public boolean uploadFiles(WSObject[] datasets, List<Map<String, String>> drawingList) throws TCOperationException {
		WSObject[] datasetArray = datasets;
		try {
			// �ϴ��ļ�

			ITCFileManagement fileMgmt = (ITCFileManagement) ServiceUtil.getService(ITCFileManagement.class.getName(), SaveDrawingOperation.class.getClassLoader());

			/*
			 * if (!fileMgmt.uploadFiles(datasetArray, "Qh3_dwg", drawingList))
			 * { throw new TCOperationException("�ϴ��ļ�ʱ�������󣬿��������ļ�λ�ò��Ի�����ԭ������");
			 * }
			 */
			if (!fileMgmt.uploadFiles(datasetArray, "PM8dwg", drawingList)) {
				throw new TCOperationException("�ϴ��ļ�ʱ�������󣬿��������ļ�λ�ò��Ի�����ԭ������");
			}

		} catch (RuntimeException re) {
			re.printStackTrace();
			throw new TCOperationException("�ϴ��ļ�ʱ�������󣬿��������ļ�λ�ò��Ի�����ԭ������");
		} catch (TCOperationException e) {
			e.printStackTrace();
			throw new TCOperationException("�ϴ��ļ�ʱ�������󣬿��������ļ�λ�ò��Ի�����ԭ������");
		}
		return true;
	}

	/**
	 * 
	 * TODO �ϴ��ļ�
	 * 
	 * @return boolean
	 * @author lijj created on 2011-1-28����03:49:15
	 */
	public boolean uploadFiles(String refName, WSObject[] datasets, List<Map<String, String>> drawingList) throws TCOperationException {
		WSObject[] datasetArray = datasets;
		try {
			// �ϴ��ļ�
			ITCFileManagement fileMgmt = (ITCFileManagement) ServiceUtil.getService(ITCFileManagement.class.getName(), SaveDrawingOperation.class.getClassLoader());
			if (!fileMgmt.uploadFiles(datasetArray, refName, drawingList)) {
				throw new TCOperationException("�ϴ��ļ�ʱ�������󣬿��������ļ�λ�ò��Ի�����ԭ������");
			}
		} catch (RuntimeException re) {
			re.printStackTrace();
			throw new TCOperationException("�ϴ��ļ�ʱ�������󣬿��������ļ�λ�ò��Ի�����ԭ������");
		} catch (TCOperationException e) {
			e.printStackTrace();
			throw new TCOperationException("�ϴ��ļ�ʱ�������󣬿��������ļ�λ�ò��Ի�����ԭ������");
		}
		return true;
	}

	/**
	 * ��ȡ��ѡ���ֵ
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
	 * ˢ��ָ������
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
	 * TCOperationException("����Ķ����������󣬲���ʧ�ܡ�"); } catch (NotLoadedException e) {
	 * throw new TCOperationException("δ����ȷ��ȡ��������ԣ�����ʧ�ܡ�"); } return form; }
	 */
	public WSObject getFormInfo(WSObject object) throws TCOperationException {
		WSObject form = op.getFormInfo(object);

		return form;
	}
}
