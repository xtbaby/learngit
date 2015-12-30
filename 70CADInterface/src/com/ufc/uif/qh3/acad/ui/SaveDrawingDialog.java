package com.ufc.uif.qh3.acad.ui;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import org.dom4j.Element;
import org.netbeans.api.wizard.WizardDisplayer;
import org.netbeans.api.wizard.WizardResultReceiver;
import org.netbeans.spi.wizard.Wizard;
import com.ufc.uif.base_adaptor.AdaptorWriter;
import com.ufc.uif.base_adaptor.busioperator.BusiOperationException;
import com.ufc.uif.qh3.acad.operation.ParseConfigXMl;
import com.ufc.uif.qh3.acad.operation.ParseRequestXML;
import com.ufc.uif.qh3.acad.operation.SaveDrawingOperation;
import com.ufc.uif.qh3.acad.operation.Util;
import com.ufc.uif.qh3.acad.util.UFCComboBox;
import com.ufc.uif.qh3.acad.util.UFCDatePick;
import com.ufc.uif.qh3.acad.util.UFCTextField;
import com.ufc.uif.tcuacommunication.objects.WSObject;
import com.ufc.uif.tcuacommunication.operation.ITCFileOperation;
import com.ufc.uif.tcuacommunication.operation.ITCObjectOperation;
import com.ufc.uif.tcuacommunication.operation.ITCTCObjOperation;
import com.ufc.uif.tcuacommunication.operation.OperationManagerFactory;
import com.ufc.uif.tcuacommunication.operation.exception.TCOperationException;
import com.ufc.uif.util.service.ServiceUtil;

/**
 * 
 * @author admin
 */
public class SaveDrawingDialog extends _UIFDialog implements WizardResultReceiver {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// jar private ObjectOperation op;
	private ITCObjectOperation op;
	private ITCTCObjOperation tcop;
	// 获取请求中的属性
	private List<Map<String, String>> projList;
	private List<Map<String, String>> docList;
	private List<Map<String, String>> docproList;
	private List<Map<String, String>> subObjsList;
	private WSObject[] datasetObjs;

	private SaveDrawingOperation sdo;
	private boolean keepEditable;
	private Map<String, List<Map<String, String>>> datasetsStatus;

	private ParseConfigXMl parseConfig;

	private Map<String, String> itemRevisionAttribs;
	private StateDialog statedlg;
	private WSObject saveFolder;
	public  AdaptorWriter out;
	private boolean isAssembly = false;
	public Element requestBody;

	// 记录所有itemID及相关数据集名
	private Map<String, List<String>> allItemIDsWithDatasets;

	// 记录所有的itemid及对应的属性图号，主要用于一号多图时 图样代号属性需要特殊处理
	private Map<String, String> allItemIDSWithDrawingNO;

	private Map<String, Map<String, Boolean>> itemStatus;
	private String[] steps;
	private String[] descs;
	private Wizard wizard;
	private List<String> newItems;
	private List<String> refreshItems;
	private List<String> noRefItems;
	private String DrawingName;

	private Map<String, String> drawingSizeValueMap;

	// 可以更新属性的item
	private Map<String, WSObject> updateItems;
	private Map<String, WSObject> newItemObjs;

	private boolean isMutilDrawingNO = false;
	private static Map<String, String> GZlov;
	private static Map<String, String> ProjPhaselov;

	private static Map<String, String> classtype = new HashMap<String, String>();
	static {

		classtype.put("IRMF", "RevisionMaster");
		classtype.put("ITEM", "Item");
		classtype.put("IR", "Revision");
		classtype.put("IMF", "Master");
		classtype.put("DS", "DS");
	}

	// 项目 用于item对象的项目指派操作
	private WSObject projectObj;
	private String drawingType;
	private String drawingNum;
	private Cursor busyCursor = new Cursor(Cursor.WAIT_CURSOR);
	private Cursor defCursor = new Cursor(Cursor.DEFAULT_CURSOR);
	private boolean setValueFromPDM;
	private JButton button;

	public SaveDrawingDialog(OperationManagerFactory factory) {
		super(factory);
		setTitle("中国北方发动机研究所AutoCad接口");
		setAlwaysOnTop(true);
		setBackground(new java.awt.Color(241, 250, 255));
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				try {
					out.setFuncID("SaveDrawing");
					out.setResult("false");
					out.setDesc("用户取消导入。");
					out.sendResultToUI();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				dispose();
			}
		});

		labelWidth = 70;
		spacing = 10;
		comWidth = 150;
		startX = 20;
		this.setSize(650, 500);
		this.setLocationRelativeTo(null);

		sdo = new SaveDrawingOperation();
		op = (ITCObjectOperation) ServiceUtil.getService(ITCObjectOperation.class.getName(), SaveDrawingDialog.class.getClassLoader());
		tcop = (ITCTCObjOperation) ServiceUtil.getService(ITCTCObjOperation.class.getName(), SaveDrawingDialog.class.getClassLoader());
	}

	// 装配向导
	private void initStepPanels() {
		steps = new String[] { SaveBOMWPageProvider.DRAWINGINFOPANEL, SaveBOMWPageProvider.DOCUMENTINFOPANEL, SaveBOMWPageProvider.PARTINFOPANEL, SaveBOMWPageProvider.SUMMARYPANEL,
				SaveBOMWPageProvider.REGDRAWING, SaveBOMWPageProvider.IMPORTBOMPANEL, SaveBOMWPageProvider.CHECKINDRAWINGPANEL };
		descs = new String[] { "图纸信息", "图档信息", "零部件信息", "信息摘要", "注册图纸", "创建产品结构", "检入图纸" };
	}

	// 装配向导
	private void initStepPanels_noAss() {
		steps = new String[] { SaveBOMWPageProvider.DRAWINGINFOPANEL, SaveBOMWPageProvider.DOCUMENTINFOPANEL, SaveBOMWPageProvider.PARTINFOPANEL, SaveBOMWPageProvider.SUMMARYPANEL,
				SaveBOMWPageProvider.REGDRAWING, SaveBOMWPageProvider.CHECKINDRAWINGPANEL };
		descs = new String[] { "图纸信息", "图档信息", "零部件信息", "信息摘要", "注册图纸", "检入图纸" };
	}

	private void initDialog() {

		if (isAssembly) {
			initStepPanels();
		} else {
			initStepPanels_noAss();
		}

		SaveBOMWPageProvider provider = new SaveBOMWPageProvider(steps, descs, this);

		wizard = provider.createWizard();

		wizard.addWizardObserver(provider);

		WizardDisplayer.installInContainer(this.getContentPane(), null, wizard, null, null, this, 0);
	}

	/**
	 * 取消按钮
	 * 
	 * @param evt
	 */
	private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {
		closeWindow();
	}

	/**
	 * 保存图纸按钮
	 * 
	 * @param evt
	 */
	private void saveDrawingBtnActionPerformed(java.awt.event.ActionEvent evt) {

		importDrawing2();
	}

	private void jCheckBox2StateChanged(javax.swing.event.ChangeEvent evt) {

	}

	private javax.swing.JCheckBox jCheckBox1;
	private javax.swing.JCheckBox jCheckBox2;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JSplitPane jSplitPane1;
	private javax.swing.JTextArea jTA_status;
	private javax.swing.JTextField jTextField1;

	private JPanel propPanel = new JPanel();
	private Map<String, JComponent> mapComponent = new HashMap<String, JComponent>();
	private Map<String, JComponent> mapRequiredComponent = new HashMap<String, JComponent>();
	private int widthFrame, labelWidth, spacing, comWidth, startX;

	@Override
	public void initConfig() {
		System.out.println("207XTCAD----SaveDrawingDialog-----------initConfig");
		initAutoCADCfg();
	}

	/**
	 * 读取配置文件
	 */
	private void initAutoCADCfg() {
		System.out.println("70XTCAD----initAutoCADCfg-----------");
		String UFCROOT = System.getenv("UFCROOT");
		String fileSeparator = System.getProperty("file.separator");
		String configFilePath = UFCROOT + fileSeparator + "AutoCAD" + fileSeparator + "cfg" + fileSeparator + "uif.xml";
		parseConfig = new ParseConfigXMl(configFilePath);
		parseConfig.setInfos();
	}

	@Override
	public void initListener() {

	}

	public void doOperation(Element requestBody, AdaptorWriter out) throws BusiOperationException {
		System.out.println("70XTCAD----doOperation-----------");

		this.out = out;
		this.requestBody = requestBody;

		docproList = new ArrayList<Map<String, String>>();
		docList = new ArrayList<Map<String, String>>();
		projList = new ArrayList<Map<String, String>>();

		allItemIDsWithDatasets = new HashMap<String, List<String>>();
		allItemIDSWithDrawingNO = new HashMap<String, String>();
		itemStatus = new HashMap<String, Map<String, Boolean>>();
		newItems = new ArrayList<String>();
		refreshItems = new ArrayList<String>();
		noRefItems = new ArrayList<String>();
		newItemObjs = new HashMap<String, WSObject>();
		updateItems = new HashMap<String, WSObject>();

		GZlov = parseConfig.lovPropertyMap.get("qh3_GZPart");
		ProjPhaselov = parseConfig.lovPropertyMap.get("qh3_BelongtoPhase");

		// 获取标题栏信息
		projList = ParseRequestXML.getPropsInfo(requestBody);
		// 获取数据集信息（包括数据集上的属性和图纸要保存的路径）
		docproList = ParseRequestXML.getDocInfo(requestBody);
		// 获取bomline信息
		subObjsList = ParseRequestXML.getSubObjsInfo(requestBody);

		Element ass = requestBody.element("body").element("object").element("assembly");
		if ("1".equals(ass.getTextTrim())) {
			isAssembly = true;
		} else {
			isAssembly = false;
		}

		initDialog();
		// 显示保存界面

		setFrameVisibility();

	}

	public void closeWindow() {
		try {
			out.setFuncID("SaveDrawing");
			out.setResult("CloseWindow");
			out.setDesc("保存图纸操作失败，用户取消操作。");
			out.sendResultToUI();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		System.out.println(" closeWindow");
		dispose();
	}

	/**
	 * 动态构造属性界面
	 */
	private void GetPropPanel() {
		System.out.println("207XTCAD----SaveDrawingDialog-----------GetPropPanel");
		jSplitPane1.setRightComponent(jPanel1);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING,
				javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE));

		propPanel.setBackground(new java.awt.Color(241, 250, 255));

		javax.swing.GroupLayout propPanelLayout = new javax.swing.GroupLayout(propPanel);
		propPanel.setLayout(propPanelLayout);
		propPanelLayout.setVerticalGroup(propPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 348, Short.MAX_VALUE));

		javax.swing.JScrollPane jScrollPane2 = new javax.swing.JScrollPane();
		jScrollPane2.setViewportView(propPanel);
		jSplitPane1.setTopComponent(jScrollPane2);

		if (null == drawingType) {
			ConfigPropPanel("Qh3_DesPart");
		} else {
			ConfigPropPanel("Qh3_AssDrawing");
		}
	}

	/**
	 * 动态构造属性界面
	 * 
	 * @param ItemType
	 */
	public void ConfigPropPanel(String ItemType) {
		System.out.println("207XTCAD----SaveDrawingDialog-----------ConfigPropPanel");
		// 客户化属性面板
		propPanel.removeAll();
		List<Map<String, String>> saveInfoList = parseConfig.saveOpInfo.get(ItemType);

		// LOV汉化
		Map<String, String> lovValuePre = new HashMap<String, String>();
		List<String> drawingType = new ArrayList<String>();

		try {
			String[] lovMappPref = op.getPreferenceValues("Qh3_207_Lov_Mapping", "site");
			for (int p = 0; p < lovMappPref.length; p++) {
				String[] s = lovMappPref[p].split("\\|");
				if (s != null && s.length == 4) {

					lovValuePre.put(s[1], s[2].substring(s[2].indexOf("[") + 1, s[2].indexOf("]")));
					if ("Qh3_DrawingType_lov".equals(s[0])) {
						drawingType.add(s[2].substring(s[2].indexOf("[") + 1, s[2].indexOf("]")));
					}
				}
			}
		} catch (TCOperationException e) {
			e.printStackTrace();
		}
		// LOV汉化
		System.out.println("saveInfoList.size()=" + saveInfoList.size());
		for (int i = 0; i < saveInfoList.size(); i++) {
			Map<String, String> proInfoMap = saveInfoList.get(i);

			String displayName = proInfoMap.get("display_name");
			String cadName = proInfoMap.get("cad_name");
			String propertyName = proInfoMap.get("pdm_name");
			String required = proInfoMap.get("required");
			String defaultValue = proInfoMap.get("default_value");
			String direction = proInfoMap.get("direction");
			String componentType = proInfoMap.get("type");
			String sourceObject = proInfoMap.get("source");
			String canEdit = proInfoMap.get("editable");

			JComponent ufcComponent = null;

			JLabel myLabel = new JLabel();
			myLabel.setHorizontalAlignment(SwingConstants.RIGHT); // 对其方式设置
			if ("true".equalsIgnoreCase(required)) { // 记录必填项
				mapRequiredComponent.put(displayName, ufcComponent);
				myLabel.setText("<html>" + "<font color='red'>*</font>" + displayName + "</html>");
			} else {
				myLabel.setText("<html>" + displayName + "</html>");
			}

			// 判断控件类型
			if ("TEXT".equalsIgnoreCase(componentType)) {
				UFCTextField ufcTextFiled = new UFCTextField();

				ufcTextFiled.setDisplayName(displayName);
				ufcTextFiled.setCadName(cadName);
				ufcTextFiled.setProperty(propertyName);
				ufcTextFiled.setRequired(Boolean.valueOf(required));
				ufcTextFiled.setText(defaultValue);
				ufcTextFiled.setDirection(direction);
				ufcTextFiled.setObjType(sourceObject);
				ufcTextFiled.setCanEdit(Boolean.parseBoolean(canEdit));
				ufcTextFiled.setEditable(Boolean.parseBoolean(canEdit));

				ufcComponent = ufcTextFiled;
			} else if ("COMBO".equalsIgnoreCase(componentType)) {
				UFCComboBox ufcCombo = new UFCComboBox();

				String sourceObjectStr = "";
				if (null == sourceObject || sourceObject.trim().length() == 0) {
					System.out.println("displayName 是" + displayName + "的lov类型数据的source配置信息错误！");
				} else {
					String lovObject[] = sourceObject.split(":");

					if ("IR".equalsIgnoreCase(lovObject[0])) {
						sourceObjectStr = ItemType + classtype.get("IR");
					} else if ("ITEM".equalsIgnoreCase(lovObject[0])) {
						sourceObjectStr = ItemType;
					} else if ("IMF".equalsIgnoreCase(lovObject[0])) {
						sourceObjectStr = ItemType + classtype.get("IMF");
					} else if ("IRMF".equalsIgnoreCase(lovObject[0])) {
						sourceObjectStr = ItemType + classtype.get("IRMF");
					} else {
						System.out.println("displayName 是" + displayName + "的lov类型数据的source配置信息错误");
					}

					if (sourceObjectStr.length() > 0) {
						Map<String, String[]> lovValue = null;
						if (lovObject.length > 1) {
							lovValue = sdo.getValuesOfLOV(sourceObjectStr, new String[] { lovObject[1] });
						} else {
							lovValue = sdo.getValuesOfLOV(sourceObjectStr, new String[] { propertyName });
						}

						// duzl @207 20150924
						if ("qh3_DrawingType".equals(lovObject[1])) {
							lovValue = null;
							for (String dt : drawingType) {
								ufcCombo.addItem(dt);
							}
						}

						if (null != lovValue) {
							String tempArray[] = lovValue.get(propertyName);

							List<String> lovList = new ArrayList<String>();
							for (int t = 0; t < tempArray.length; t++) {
								String v = lovValuePre.get(tempArray[t]);
								if (v != null && v.length() > 0) {
									lovList.add(v);
								}
							}
							Object[] lovArray = lovList.toArray();
							if (lovArray != null && lovArray.length > 0) {
								for (int j = 0; j < lovArray.length; j++) {
									ufcCombo.addItem(lovArray[j]);
								}
							} else {
								for (int j = 0; j < tempArray.length; j++) {
									ufcCombo.addItem(tempArray[j]);
								}
							}
						}
					}
				}

				ufcCombo.setDisplayName(displayName);
				ufcCombo.setCadName(cadName);
				ufcCombo.setProperty(propertyName);
				ufcCombo.setRequired(Boolean.valueOf(required));
				ufcCombo.setSelectedItem(defaultValue);
				ufcCombo.setDirection(direction);
				ufcCombo.setObjType(sourceObject);
				ufcCombo.setCanEdit(Boolean.parseBoolean(canEdit));
				ufcCombo.setEditable(Boolean.parseBoolean(canEdit));
				ufcCombo.setEnabled(Boolean.parseBoolean(canEdit));

				ufcComponent = ufcCombo;
			} else if ("DATE".equalsIgnoreCase(componentType)) {
				UFCDatePick ufcDatePick = new UFCDatePick();
				ufcDatePick.setDisplayName(displayName);
				// ufcDatePick.setCanEdit(Boolean.valueOf(canEdit));
				ufcDatePick.setRequired(Boolean.valueOf(required));
				ufcDatePick.setProperty(propertyName);
				ufcDatePick.setObjType(sourceObject);
				ufcDatePick.setCadName(cadName);
				ufcDatePick.setDirection(direction);

				ufcComponent = ufcDatePick;
			} else if ("button".equalsIgnoreCase(componentType)) {// xuebing ADD
				button = new JButton();
				button.setText(displayName);
				ufcComponent = button;
			}

			if (ufcComponent == null)
				return;

			// 属性设置 位置确定
			if (i % 2 == 0) {
				myLabel.setBounds(new Rectangle(startX, (i + 20) + (i * 15), labelWidth, 22));
				ufcComponent.setBounds(new Rectangle(startX + labelWidth + spacing, (i + 20) + (i * 15), comWidth, 22));

			} else {
				myLabel.setBounds(new Rectangle((int) this.getSize().getWidth() / 2, (i + 20) + ((i - 1) * 15), labelWidth, 22));
				ufcComponent.setBounds(new Rectangle((int) this.getSize().getWidth() / 2 + labelWidth + spacing, (i + 20) + ((i - 1) * 15), comWidth, 22));
			}
			propPanel.add(myLabel);
			propPanel.add(ufcComponent);

			mapComponent.put(displayName, ufcComponent);
			// 控件记录
		}

		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// AssignItemID();
				button.setEnabled(false);
			}
		});
		// System.out.println("Width=" + this.getWidth());
		int componentCT = saveInfoList.size();

		int componentRowCT = 0;
		if (componentCT % 2 == 0) {
			componentRowCT = componentCT / 2;
		} else {
			componentRowCT = (componentCT + 10) / 2;
		}
		propPanel.setPreferredSize(new Dimension(widthFrame - 15, (22 * componentRowCT + (componentRowCT - 1) * 15) - 5));
	}// 客户化else

	/**
	 * 属性界面填值
	 */
	public void setValues() {
		System.out.println("70XTCAD----setValues-----------");
		Map<String, String> propertyMap = projList.get(0);
		Component[] component = propPanel.getComponents();

		for (int i = 0; i < component.length; i++) {
			if (component[i] instanceof UFCTextField) {
				UFCTextField tempTextField = (UFCTextField) component[i];
				String tempValue = null;
				if (tempTextField.getDisplayName().toString().equals("产品代号")) {
					tempValue = propertyMap.get("图号").split("-")[0];
				} else {
					// 获取消息中对应的值
					String[] cadNameArray = tempTextField.getCadName().split(",");
					if (cadNameArray.length == 1) {
						tempValue = propertyMap.get(cadNameArray[0]);
					}
				}
				if (tempValue != null) {
					tempTextField.setText(tempValue);
				} else {
					// 若没有从图纸上提取到该值则设置为可以编辑
					tempTextField.setEditable(true);
				}
			} else if (component[i] instanceof UFCComboBox) {
				UFCComboBox tempCombo = (UFCComboBox) component[i];
				String[] cadNameArray = tempCombo.getCadName().split(",");
				String tempValue = null;
				if (cadNameArray.length == 1) {
					tempValue = propertyMap.get(cadNameArray[0]);
				} else {
					if (tempCombo.getProperty().equals("qh3_BelongtoPhase")) {
						String biaoji1 = propertyMap.get(cadNameArray[0]);
						String biaoji2 = propertyMap.get(cadNameArray[1]);
						String biaoji3 = propertyMap.get(cadNameArray[2]);

						System.out.println("biaoji1=" + biaoji1 + ",biaoji2=" + biaoji2 + ",biaoji3=" + biaoji3);
						tempValue = ProjPhaselov.get(getBelongToPhase(biaoji1, biaoji2, biaoji3));
						System.out.println("tempValue=" + tempValue);
					}
				}
				if (null != tempValue) {
					tempCombo.setSelectedItem(tempValue);
				}
			} else if (component[i] instanceof UFCDatePick) {

			}
		}
	}

	/**
	 * 根据TC属性获取界面属性值
	 * 
	 * @param prop
	 * @return
	 */
	private String getPropertyValue(String prop) {
		System.out.println("207XTCAD----SaveDrawingDialog-----------getPropertyValue");
		String ret = "";
		Component[] component = propPanel.getComponents();

		for (int i = 0; i < component.length; i++) {
			if (component[i] instanceof UFCTextField) {
				UFCTextField utf = (UFCTextField) component[i];
				String pro = utf.getProperty();

				if (pro.equals(prop)) {
					ret = utf.getText();
					break;
				}
			}
			if (component[i] instanceof UFCComboBox) {
				UFCComboBox utf = (UFCComboBox) component[i];
				String pro = utf.getProperty();
				if (pro.equals(prop)) {
					ret = utf.getSelectedItem().toString();
					break;
				}
			}
		}
		return ret;
	}

	/**
	 * 
	 * TODO 根据displayName取对应的property和value值
	 * 
	 * @return String
	 * @author lijj created on 2011-1-7下午12:07:30
	 */
	private Map<String, String> getValueByDisplayName(String displayName) {
		System.out.println("207XTCAD----SaveDrawingDialog-----------getValueByDisplayName");
		Map<String, String> resultMap = new HashMap<String, String>();
		Component[] component = propPanel.getComponents();

		for (int i = 0; i < component.length; i++) {
			if (component[i] instanceof UFCTextField) {
				UFCTextField utf = (UFCTextField) component[i];
				String tempName = utf.getDisplayName();

				if (tempName.equals(displayName)) {
					String property = utf.getProperty();
					String value = utf.getText();
					resultMap.put("property", property);
					resultMap.put("value", value);
					break;
				}
			}
			if (component[i] instanceof UFCComboBox) {
				UFCComboBox utf = (UFCComboBox) component[i];
				String tempName = utf.getDisplayName();
				if (tempName.equals(displayName)) {
					String property = utf.getProperty();
					String value = utf.getSelectedItem().toString();
					resultMap.put("propery", property);
					resultMap.put("value", value);
					break;
				}
			}
		}
		return resultMap;
	}

	/**
	 * 
	 * TODO 图号的处理（连号图）
	 * 
	 * @return List<String>
	 * @author lijj created on 2010-12-22上午11:47:14
	 */
	private List<String> getDrawingNOs(String DrawingNO) {
		System.out.println("207XTCAD----SaveDrawingDialog-----------getDrawingNOs");
		List<String> resultList = new ArrayList<String>();
		String[] commaArray = DrawingNO.split(",");
		for (int i = 0; i < commaArray.length; i++) {
			String[] tempArray = commaArray[i].split("~");
			if (tempArray.length > 2) {
				System.out.println("连号图格式不正确");
				return null;
			}

			if (tempArray.length == 1) {
				resultList.add(tempArray[0]);
			} else if (tempArray.length == 2) {
				String startNO = tempArray[0];
				String endNO = tempArray[1];
				if (startNO.length() != endNO.length()) {
					System.out.println("连号图开始图号和结束图号格式不一致");
					return null;
				}

				int tempPos = 0;
				for (int j = 1; j <= startNO.length(); j++) {
					if (!startNO.substring(0, j).equals(endNO.substring(0, j))) {
						tempPos = j;
						break;
					}
				}
				if (tempPos > 0) {
					String preFixStr = startNO.substring(0, tempPos - 1);
					try {
						int start = Integer.parseInt(startNO.substring(tempPos - 1));
						int end = Integer.parseInt(endNO.substring(tempPos - 1));

						while (start <= end) {
							String fillStr = "";
							int zeroLength = String.valueOf(end).length() - String.valueOf(start).length();
							while (zeroLength > 0) {
								fillStr = fillStr + "0";
								zeroLength--;
							}
							resultList.add(preFixStr + fillStr + String.valueOf(start));
							start++;
						}
					} catch (Exception e) {
						System.out.println("连号图的连续号非数字！");
						return null;
					}
				} else {
					// startno与endNo一样
					resultList.add(startNO);
				}
			}
		}

		if (resultList.size() > 1) {
			isMutilDrawingNO = true;
		} else {
			isMutilDrawingNO = false;
		}

		return resultList;
	}

	/**
	 * 
	 * TODO 判断数据itemid下的数据集，添加itemid的相关状态
	 * 
	 * @return Integer 0 ：在非连号图的情况下 没有 检查通过。 1 ：在非连号图的情况下检查通过 -1：在连号图的情况下返回-1
	 * @author lijj created on 2011-1-5下午03:35:24
	 */
	private Integer checkItemDatasetState(String itemid, List<String> datasetNames) {
		System.out.println("207XTCAD----SaveDrawingDialog-----------checkItemDatasetState");
		Integer checkResult = -1;
		WSObject tempItemObj = null;
		if (null == drawingType) {
			// 普通图纸检查
			try {
				tempItemObj = sdo.findItem(itemid, "__Query_DesPart");
			} catch (TCOperationException e) {
				sendErrorMessage(e.getMessage());
				return 0;
			}
		} else {
			try {
				tempItemObj = sdo.findItem(itemid, "__Query_AssDrawing");
			} catch (TCOperationException e) {
				sendErrorMessage(e.getMessage());
				return 0;
			}
		}

		if (null == tempItemObj) {
			outputStatus("零部件【" + itemid + "】系统中不存在，可以新建");

			Map<String, Boolean> tempStatus = new HashMap<String, Boolean>();
			tempStatus.put("exist", false);
			tempStatus.put("refresh", false);
			tempStatus.put("haveDS", false);
			tempStatus.put("checkOut", false);
			itemStatus.put(itemid, tempStatus);

			List<Map<String, String>> datasetsInfoList = new ArrayList<Map<String, String>>();
			for (String dsname : datasetNames) {
				Map<String, String> tempDatasetMap = new HashMap<String, String>();
				tempDatasetMap.put("dsname", dsname);
				tempDatasetMap.put("isNew", "true");
				datasetsInfoList.add(tempDatasetMap);
			}
			datasetsStatus.put(itemid, datasetsInfoList);

			checkResult = 1;
		} else {
			outputStatus("零部件【" + itemid + "】系统中存在");

			String ucodeid = sdo.getPropertyValue(tempItemObj, "qh3_UcodeID");
			System.out.println("######     " + ucodeid + "     #######");
			JComponent idcom = mapComponent.get("零件编码");
			if (idcom instanceof UFCTextField) {
				UFCTextField tempTextField = (UFCTextField) idcom;
				tempTextField.setText(ucodeid);
				tempTextField.setEditable(false);
				tempTextField.setEnabled(false);
			}

			if (!sdo.checkPrivilege(tempItemObj, "WRITE")) {
				outputStatus("您没有修改该Item的权限，不能对该Item进行保存操作");
				Map<String, Boolean> tempStatus = new HashMap<String, Boolean>();
				tempStatus.put("exist", true);
				tempStatus.put("refresh", false);
				tempStatus.put("haveDS", false);
				tempStatus.put("checkOut", false);

				itemStatus.put(itemid, tempStatus);
				if (!isMutilDrawingNO) {
					sendErrorMessage("您没有修改该Item的权限，不能对该Item进行保存操作");
					checkResult = 0;
				}

			} else {
				// 检查Dataset的状态
				outputStatus("您有修改该Item的权限");

				if (!setValueFromPDM) {
					// 获取存放Item的文件夹信息
					WSObject originalFolder = sdo.getReferencedFolder(tempItemObj);
					if (null != originalFolder) {
						String folderName = sdo.getNameOfFolder(originalFolder);
						jTextField1.setText(folderName);
					}

					// 在处理一图多号时，将以第一个有更新权限的item属性值为准
					setProperValueFromPdm(tempItemObj);

					setValueFromPDM = true;
				}
				checkResult = checkDataset(itemid, tempItemObj, datasetNames);
			}
		}

		return checkResult;
	}

	/**
	 * 
	 * TODO 将映射方向为both和pdm2cad的控件赋值为相应pdm中的属性值(当控件值为空时)
	 * 
	 * @return void
	 * @author lijj created on 2010-12-22下午02:36:16
	 * @edit zhangwh 2014-11-7
	 */
	private void setProperValueFromPdm(WSObject item) {
		System.out.println("207XTCAD----SaveDrawingDialog-----------setProperValueFromPdm");
		Component[] component = propPanel.getComponents();
		for (int i = 0; i < component.length; i++) {
			if (component[i] instanceof UFCTextField) {
				UFCTextField utf = (UFCTextField) component[i];
				if ("pdm2cad".equalsIgnoreCase(utf.getDirection()) || "both".equalsIgnoreCase(utf.getDirection())) {
					String obj = utf.getObjType();
					String property = utf.getProperty();

					WSObject sourceObject = getSourceObject(item, obj.split(":")[0]);
					String value = tcop.getPropertyValueOfObject(sourceObject, property);
					utf.setText(value);
				}
			}

			if (component[i] instanceof UFCComboBox) {
				UFCComboBox utf = (UFCComboBox) component[i];
				if ("pdm2cad".equalsIgnoreCase(utf.getDirection()) || "both".equalsIgnoreCase(utf.getDirection())) {
					String obj = utf.getObjType();
					String property = utf.getProperty();

					// WSObject sourceObject = getSourceObject(item,
					// obj.split(":")[0]);
					// String value =
					// tcop.getPropertyValueOfObject(sourceObject, property);
					String value = tcop.getPropertyValueOfObject(sdo.getLastestItemRevision(item), property);
					utf.setSelectedItem(value);
				}
			}
		}
	}

	/**
	 * 
	 * TODO 判断数据itemid下的数据集，添加itemid的相关状态
	 * 
	 * @return Integer 0 ：在非连号图的情况下用户“不”可以更新保存。 1 ：在非连号图的情况下用户可以更新操作
	 *         -1：在连号图的情况下返回-1
	 * @author lijj created on 2011-1-5下午03:35:24
	 */
	private Integer checkDataset(String itemid, WSObject item, List<String> datasetNames) {
		System.out.println("207XTCAD----SaveDrawingDialog-----------checkDataset");
		Integer retflag = -1;
		List<Map<String, String>> datasetsInfoList = new ArrayList<Map<String, String>>();

		updateItems.put(itemid, item);

		for (String dsname : datasetNames) {
			try {
				Map<String, String> dsState = sdo.checkDataset(item, dsname);
				Map<String, String> tempDatasetMap = new HashMap<String, String>();

				if ("true".equalsIgnoreCase(dsState.get("existed"))) {
					outputStatus("数据集【" + dsname + "】存在");

					if ("0".equalsIgnoreCase(dsState.get("checkout"))) {

						Map<String, Boolean> tempStatus = new HashMap<String, Boolean>();
						tempStatus.put("exist", true);
						tempStatus.put("refresh", true);
						tempStatus.put("haveDS", true);
						tempStatus.put("checkOut", false);
						itemStatus.put(itemid, tempStatus);

						tempDatasetMap.put("dsname", dsname);
						tempDatasetMap.put("isNew", "false");
						datasetsInfoList.add(tempDatasetMap);

						if (!isMutilDrawingNO) {
							int result = JOptionPane.showConfirmDialog(this, "是否更新已存在的图纸" + "\n" + dsname, "提示",
							// JOptionPane.YES_NO_CANCEL_OPTION,
									JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
							if (result == 0) { // 是更新
								op.checkOutObjects(new WSObject[] { item }, "");
								retflag = 1;
							} else { // 否，取消
								sendMessageToCAD(false);
								retflag = 0;
								break;
							}
						}
					} else if ("1".equalsIgnoreCase(dsState.get("checkout"))) {
						Map<String, Boolean> tempStatus = new HashMap<String, Boolean>();
						// exist=true,refresh=true,haveDS=true,checkOut=true
						tempStatus.put("exist", true);
						tempStatus.put("refresh", true);
						tempStatus.put("haveDS", true);
						tempStatus.put("checkOut", true);
						itemStatus.put(itemid, tempStatus);

						if (!isMutilDrawingNO) {
							sendErrorMessage("指定的Dataset【" + dsname + "】已被被其他人签出，不能继续操作！");
							retflag = 0;
							break;
						}
					} else {
						// 图纸被本人签出
						Map<String, Boolean> tempStatus = new HashMap<String, Boolean>();
						// exist=true,refresh=true,haveDS=true,checkOut=true
						tempStatus.put("exist", true);
						tempStatus.put("refresh", true);
						tempStatus.put("haveDS", true);
						tempStatus.put("checkOut", false); // 被本人签出可以更新 视为没有被签出。
						itemStatus.put(itemid, tempStatus);

						tempDatasetMap.put("dsname", dsname);
						tempDatasetMap.put("isNew", "false");
						datasetsInfoList.add(tempDatasetMap);

						if (!isMutilDrawingNO) {
							int result = JOptionPane.showConfirmDialog(this, "是否更新已存在的图纸" + "\n" + dsname, "提示",
							// JOptionPane.YES_NO_CANCEL_OPTION,
									JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
							if (result == 0) {
								retflag = 1;
							}
						}
					}
				} else {
					outputStatus("数据集【" + dsname + "】不存在，新建数据集");

					Map<String, Boolean> tempStatus = new HashMap<String, Boolean>();
					// exist=true,refresh=true,haveDS=true,checkOut=true
					tempStatus.put("exist", true);
					tempStatus.put("refresh", true);
					tempStatus.put("haveDS", false);
					tempStatus.put("checkOut", false);
					itemStatus.put(itemid, tempStatus);

					tempDatasetMap.put("dsname", dsname);
					tempDatasetMap.put("isNew", "true");
					datasetsInfoList.add(tempDatasetMap);

					if (!isMutilDrawingNO) {
						retflag = 1;
					}
				}
			} catch (TCOperationException e) {
				sendMessageToUser(e.getMessage(), 0);
				if (!isMutilDrawingNO) {
					retflag = 0;
				}
			}
		}

		datasetsStatus.put(itemid, datasetsInfoList);
		return retflag;
	}

	/**
	 * 
	 * TODO 遍历itemStatus 将allItemIDsWithDatasets分类
	 * 
	 * @return Map<String,List<String>>
	 * @author lijj created on 2011-1-5下午04:05:45
	 */
	private void findItemToRefreh() {
		System.out.println("207XTCAD----SaveDrawingDialog-----------findItemToRefreh");
		if (null != itemStatus && itemStatus.size() > 0) {
			Iterator it = itemStatus.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				String key = (String) entry.getKey();
				Map<String, Boolean> value = itemStatus.get(key);
				if (!value.get("exist")) {
					newItems.add(key);
				} else {
					if (!value.get("refresh")) {
						noRefItems.add(key);
					} else {
						if (!value.get("haveDS")) {
							refreshItems.add(key);
						} else {
							if (value.get("checkOut")) {
								noRefItems.add(key);
							} else {
								refreshItems.add(key);
							}
						}
					}
				}
			}
		} else {
			System.out.println("itemStatus is null");
		}
	}

	/**
	 * 
	 * TODO 整理出在多号图时的提示信息
	 * 
	 * @return String
	 * @author lijj created on 2011-1-5下午04:32:24
	 */
	private String getInfoToShow() {
		System.out.println("207XTCAD----SaveDrawingDialog-----------getInfoToShow");
		StringBuffer resultStr = new StringBuffer("");
		if (newItems.size() > 0) {
			resultStr.append("可以创建的item: " + "\n");
			for (int i = 0; i < newItems.size(); i++) {
				String idStr = newItems.get(i);
				resultStr.append("【" + idStr + "】 ");
			}
		}

		if (refreshItems.size() > 0) {
			if (resultStr.length() != 0) {
				resultStr.append("\n");
			}

			resultStr.append("可以更新的item: " + "\n");
			for (int i = 0; i < refreshItems.size(); i++) {
				String idStr = refreshItems.get(i);
				resultStr.append("【" + idStr + "】 ");
			}
		}

		if (noRefItems.size() > 0) {
			if (resultStr.length() != 0) {
				resultStr.append("\n");
			}

			resultStr.append("不能更新的item: " + "\n");
			for (int i = 0; i < noRefItems.size(); i++) {
				String idStr = noRefItems.get(i);
				resultStr.append("【" + idStr + "】 ");
			}
		}

		return resultStr.toString();
	}

	/**
	 * 保存图纸
	 */
	private void importDrawing2() {
		System.out.println("207XTCAD----SaveDrawingDialog-----------importDrawing2");

		// 验证必填项
		outputClear();

		outputStatus("正在验证必填项信息...");
		if (!validateRequiredComps()) {
			return;
		}

		outputStatus("正在验证teamcenter中的属性信息...");
		if (!checkValue())
			return;

		outputStatus("信息验证通过。");
		// 图纸上传操作从doOperation移到按钮操作里
		outputStatus("正在向Teamcenter上传图纸，请稍候......");
		if (statedlg != null)
			statedlg.dispose();

		statedlg = new StateDialog(this, false, "正在向Teamcenter上传图纸，请稍候......");
		Thread importThread = new Thread(new Runnable() {
			public void run() {
				statedlg.setMessage("正在向Teamcenter提交图纸信息，请稍候......");
				setCursor(busyCursor);
				// 向TC保存ItemRevisioin信息
				boolean result = importDrawing();
				// 执行导入BOM操作
				// System.out.println("result=" + result);
				if (result && !isMutilDrawingNO) { // 非一图多号才对bom进行操作
					if (jCheckBox2.isSelected()) {
						statedlg.dispose();
						outputStatus("准备导入产品结构信息......");
						sendMessageToUser("成功导入图纸信息，准备导入BOM信息！", 1);

						WSObject tempObj = null;
						String itemID = null;
						if (newItemObjs.size() > 0) {
							Iterator itemIter = newItemObjs.entrySet().iterator();
							while (itemIter.hasNext()) {
								Map.Entry entry = (Map.Entry) itemIter.next();
								itemID = (String) entry.getKey();
								tempObj = newItemObjs.get(itemID);
								break;
							}
						} else {
							if (updateItems.size() > 0) {
								Iterator itemIter = updateItems.entrySet().iterator();
								while (itemIter.hasNext()) {
									Map.Entry entry = (Map.Entry) itemIter.next();
									itemID = (String) entry.getKey();
									tempObj = updateItems.get(itemID);
									break;
								}
							}
						}

						showBOMFrame(tempObj, itemID); // -------------lijj--------------
						setVisible(false);
					} else {
						try {
							statedlg.setMessage("正在向Teamcenter上传图纸，请稍候......");
							boolean haveFailed = false;
							for (int i = 0; i < datasetObjs.length; i++) {
								WSObject ds = datasetObjs[i];
								// 判断是否需要更改命名,保证一个数据集下只能有一个命名的引用
								List<Map<String, String>> tempDocList = getLocalPathMap(ds, ".dwg", docList.get(0).get("LocalPath"));
								// 判断是否需要更改命名,保证一个数据集下只能有一个命名的引用

								// if (sdo.uploadFiles(new WSObject[]{ds},
								// docList)) {
								if (sdo.uploadFiles(new WSObject[] { ds }, tempDocList)) {
									outputStatus("数据集" + ds.getDisplayName() + "图纸上传成功，完成图纸保存操作！");
								} else {
									outputStatus("数据集" + ds.getDisplayName() + "图纸上传失败！");
									haveFailed = true;
								}
							}

							if (!haveFailed) {
								sendMessageToUser("所有图纸上传成功，完成图纸保存操作", 1);
								outputStatus("完成操作，退出接口程序");
								sendMessageToCAD(true);
								setCursor(defCursor);
								statedlg.setVisible(false);
								statedlg.dispose();
								dispose();
								// closeWindow();
							} else {
								sendMessageToUser("存在图纸上传失败的数据集", 0);
								sendMessageToCAD(true);
								setCursor(defCursor);
								statedlg.setVisible(false);
								statedlg.dispose();
							}
						} catch (TCOperationException e) {
							e.printStackTrace();
							sendMessageToUser(e.getMessage(), 0);
							return;
						}
					}
				} else if (result) {
					try {
						statedlg.setMessage("正在向Teamcenter上传图纸，请稍候......");
						boolean haveFailed = false;
						for (int i = 0; i < datasetObjs.length; i++) {
							WSObject ds = datasetObjs[i];

							// 判断是否需要更改命名,保证一个数据集下只能有一个命名的引用
							List<Map<String, String>> tempDocList = getLocalPathMap(ds, ".dwg", docList.get(0).get("LocalPath"));
							// 判断是否需要更改命名,保证一个数据集下只能有一个命名的引用

							// if (sdo.uploadFiles(new WSObject[]{ds}, docList))
							// {
							if (sdo.uploadFiles(new WSObject[] { ds }, tempDocList)) {
								outputStatus("数据集" + ds.getDisplayName() + "图纸上传成功，完成图纸保存操作！");
							} else {
								outputStatus("数据集" + ds.getDisplayName() + "图纸上传失败！");
								haveFailed = true;
							}
						}

						if (!haveFailed) {
							sendMessageToUser("所有图纸上传成功，完成图纸保存操作", 1);
							outputStatus("完成操作，退出接口程序");
							sendMessageToCAD(true);
							setCursor(defCursor);
							statedlg.setVisible(false);
							statedlg.dispose();
							closeWindow();
						} else {
							sendMessageToUser("存在图纸上传失败的数据集", 0);
							sendMessageToCAD(true);
							setCursor(defCursor);
							statedlg.setVisible(false);
							statedlg.dispose();
						}
					} catch (TCOperationException e) {
						e.printStackTrace();
						sendMessageToUser(e.getMessage(), 0);
						return;
					}
				} else {
					statedlg.dispose();
					sendMessageToUser("向Teamcenter提交图纸信息时出现错误，请重新操作。", 0);
					setCursor(defCursor);
					Thread.currentThread().interrupt();
					return;
				}
			}
		});
		importThread.start();

	}

	/**
	 * 必填项验证
	 */
	public boolean validateRequiredComps() {
		String messageToShow = "";
		Component[] component = propPanel.getComponents();
		for (int i = 0; i < component.length; i++) {
			if (component[i] instanceof UFCTextField) {
				UFCTextField utf = (UFCTextField) component[i];
				if (utf.isRequired()) {
					String value = utf.getText();
					if (value == null || "".equals(value)) {
						messageToShow = messageToShow + "【" + utf.getDisplayName() + "】";
					}
				}

			}
			if (component[i] instanceof UFCComboBox) {
				UFCComboBox utf = (UFCComboBox) component[i];
				if (utf.isRequired()) {
					String value = utf.getSelectedItem().toString();
					if (value == null || "".equals(value)) {
						messageToShow = messageToShow + "【" + utf.getDisplayName() + "】";
					}
				}
			}
		}

		if (messageToShow.length() > 0) {
			sendMessageToUser("界面中" + messageToShow + "为必填属性，请检查!", 0);
			return false;
		}
		return true;
	}

	/**
	 * 遍历获取属性值
	 */
	private boolean checkValue() {
		System.out.println("207XTCAD----SaveDrawingDialog-----------checkValue");
		Component[] component = propPanel.getComponents();
		Map<String, String> propsMap = new HashMap<String, String>();
		itemRevisionAttribs = new HashMap<String, String>();
		String infoShowMessage = "";
		boolean returnflag = true;

		for (int i = 0; i < component.length; i++) {
			String displayName = null;
			String value = null;
			String source = null;
			String cadname = null;
			String direction = null;
			String property = null;
			boolean editAble = false;

			if (component[i] instanceof UFCTextField) {
				UFCTextField utf = (UFCTextField) component[i];
				displayName = utf.getDisplayName();
				value = utf.getText();
				source = utf.getObjType();
				cadname = utf.getCadName();
				direction = utf.getDirection();
				property = utf.getProperty();
				editAble = utf.getCanEdit();
			}
			if (component[i] instanceof UFCComboBox) {
				UFCComboBox utf = (UFCComboBox) component[i];
				displayName = utf.getDisplayName();
				value = utf.getSelectedItem().toString();
				source = utf.getObjType();
				cadname = utf.getCadName();
				direction = utf.getDirection();
				property = utf.getProperty();
				editAble = utf.getCanEdit();
			}

			if (null != property && property.length() > 0) {
				if (source.startsWith("IR")) {
					itemRevisionAttribs.put(property, value);
				}
			}

			if ("产品代号".equals(displayName)) {
				String info = "";
				String array[];
				int p = 0;

				try {
					projectObj = sdo.findProject("__Query_TCProject", value);
					if (null == projectObj) {
						p = 1;
						returnflag = false;
					} else {
						Map<String, String> map = op.getUserSessionInfo();
						String proName = map.get("project");
						System.out.println("====================");
						System.out.println(proName);
						System.out.println(projectObj.getName());
						if (!projectObj.getName().equals(proName)) {
							System.out.println("====================");
							p = 2;
							returnflag = false;
						}
					}
				} catch (TCOperationException e) {
					e.printStackTrace();
				}

				if (!returnflag) {
					// if (x != (array.length - 1)) {
					// continue;
					// } else {
					if (p == 1) {
						info = "根据产品代号或者项目ID【" + value + "】没有找到相关的项目！\n请更改产品代号！";
						outputStatus(info);
						JOptionPane.showMessageDialog(this, info, "错误", JOptionPane.ERROR_MESSAGE);
					} else {
						info = "根据产品代号或者项目ID【" + value + "】找到的项目和配置的项目信息不匹配,\n请重新配置项目或者更改产品代号！";
						outputStatus(info);
						JOptionPane.showMessageDialog(this, info, "错误", JOptionPane.ERROR_MESSAGE);
					}
					return returnflag;
					// }
				} else {
					return returnflag;
				}

			}

			if ("pdm2cad".equals(direction)) {
				// 在非连号图的情况下 非新建的item与界面进行属性比较
				if (!isMutilDrawingNO && newItems.size() == 0 && refreshItems.size() == 1) {
					// 界面值和tc值比较
					if (compare2PDM(value, source, property)) {
						propsMap.put(property, value); // -------lijj-----
						infoShowMessage = infoShowMessage + "【" + displayName + "】";
					}
				}
			}
		}

		if (infoShowMessage.length() > 0) {
			int result = JOptionPane.showConfirmDialog(this, "TC系统中属性值" + infoShowMessage + "已经被修改，将被更新到Teamcenter上," + "\n" + "是否继续操作", "提示", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (result == 1) {
				returnflag = false;
			} else {
				returnflag = true;
			}
		}
		return returnflag;
	}

	/**
	 * 界面值和TC中ITEM对象属性对比
	 * 
	 * @param uivalue
	 * @param type
	 * @param property
	 * @return
	 */
	private boolean compare2PDM(String uivalue, String type, String property) {
		if (uivalue == null)
			uivalue = "";

		WSObject tempItemObj = null;
		Iterator it = updateItems.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String key = (String) entry.getKey();
			tempItemObj = updateItems.get(key);
			break;
		}

		if (tempItemObj == null)
			return false;
		String value = "";
		/*
		 * if (type.startsWith("ITEM")) // if ""type { value =
		 * tcop.getPropertyValueOfObject(itemObj, property); }
		 */

		if (type.startsWith("IR")) //
		{
			WSObject itemRev;
			try {
				itemRev = tcop.getItemRevision(tempItemObj, "last");
				value = op.getPropertyOfObject(itemRev, property);
			} catch (TCOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		/*
		 * if (type.startsWith("IMF"))// { ;// 注意以后补上
		 * ;//itemMastAttribs.put(key, value); }
		 * 
		 * if (type.startsWith("IRMF")) {
		 * 
		 * try { WSObject itemRev = tcop.getItemRevision(itemObj, "last");
		 * String[] propNames = op.getPropertyNamesOfItemRevision(itemRev);
		 * 
		 * WSObject form = tcop.getRevMasterForm(itemRev);
		 * 
		 * if (null != form) { value = tcop.getPropertyValueOfObject(form,
		 * property); }
		 * 
		 * } catch (TCOperationException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * }
		 */

		if (!value.equals(uivalue))
			return true;
		else
			return false;
	}

	/**
	 * 保存ITEM对象
	 * 
	 * @return
	 */
	private boolean importDrawing() {
		System.out.println("207XTCAD----SaveDrawingDialog-----------importDrawing");
		outputStatus("正在进行图纸导入操作......");
		keepEditable = jCheckBox1.isSelected();
		List<WSObject> listdatasets = new ArrayList<WSObject>();

		try {
			if (newItems.size() > 0) {
				// StringBuffer infoShowStr = new StringBuffer("");

				for (int i = 0; i < newItems.size(); i++) {
					String idStr = newItems.get(i);

					outputStatus("正在Teamcenter中创建ItemID为【" + idStr + "】的item...");
					WSObject tempItemObj = null;
					if (null == drawingType) {
						tempItemObj = sdo.createItem(saveFolder, "Qh3_DesPart", idStr, DrawingName);
					} else {
						tempItemObj = sdo.createItem(saveFolder, "Qh3_AssDrawing", idStr, DrawingName);
					}

					newItemObjs.put(idStr, tempItemObj);

				}

				outputStatus("正在更新ItemRevisioin的属性信息......");
				Iterator itemIter = newItemObjs.entrySet().iterator();
				while (itemIter.hasNext()) {
					Map.Entry entry = (Map.Entry) itemIter.next();
					String key = (String) entry.getKey();
					WSObject tempObj = newItemObjs.get(key);

					// 指派项目
					if (null != projectObj) {
						System.out.println("1=" + projectObj.getId() + "," + projectObj.getName() + "," + tempObj.getId() + "," + tempObj.getName());
						boolean succ = tcop.assignObjectToProject(new WSObject[] { sdo.getLastestItemRevision(tempObj) }, projectObj);
						boolean succ1 = tcop.assignObjectToProject(new WSObject[] { tempObj }, projectObj);
						if (succ && succ1) {
							outputStatus("成功指派项目！");
						} else {
							outputStatus("指派项目失败！");
						}
					}

					System.out.println("$%$%$%$%$%$%$%$%$%$%$%$%$%$%$%$%$%$%$%$%$%$=" + sdo.getLastestItemRevision(tempObj));
					// xuebing ADD
					// 更新UcodeID属性
					setIDPropertyValue(sdo.getLastestItemRevision(tempObj), "零件编码");

					// ------------ 处理一图多号时 图样代号属性 -------------------
					String tempValue = allItemIDSWithDrawingNO.get(key);
					String tempProperty = getValueByDisplayName("图样代号").get("property");
					itemRevisionAttribs.put(tempProperty, tempValue); // put方法修改图样代号的属性值，该方法会覆盖同key值的value

					// 获取界面上的属性信息
					Component[] component = propPanel.getComponents();

					for (int i = 0; i < component.length; i++) {

						if (component[i] instanceof UFCTextField) {
							UFCTextField tempTextField = (UFCTextField) component[i];
						}
						if (component[i] instanceof UFCComboBox) {
							UFCComboBox tempCombo = (UFCComboBox) component[i];
							itemRevisionAttribs.put(tempCombo.getProperty(), (String) tempCombo.getSelectedItem());
						}
					}
					// ------------ 处理一图多号时 图样代号属性 -------------------

					if (sdo.updateObjectProps(sdo.getLastestItemRevision(tempObj), itemRevisionAttribs)) {
						outputStatus("更新【" + key + "】ItemRevisioin的属性信息成功");
					} else {
						outputStatus("更新【" + key + "】ItemRevisioin的属性信息失败");
					}

					List<Map<String, String>> datasetsList = datasetsStatus.get(key);
					if (null != datasetsList) {
						for (int i = 0; i < datasetsList.size(); i++) {
							Map isNewDataset = datasetsList.get(i);
							String datasetname = (String) isNewDataset.get("dsname");
							if ("true".equals((String) isNewDataset.get("isNew"))) {
								outputStatus("正在创建Dataset对象【" + datasetname + "】......");
								WSObject datasetObj = sdo.createDataset(tempObj, datasetname, keepEditable);
								outputStatus("成功创建Dataset对象【" + datasetname + "】完成！");
								listdatasets.add(datasetObj);
							} else {
								outputStatus("正在查找Dataset信息【" + datasetname + "】......");
								WSObject datasetObj = sdo.findDatasetOnItemRev(tempObj, datasetname, keepEditable);
								listdatasets.add(datasetObj);
							}
							outputStatus("成功更新Dataset信息【" + datasetname + "】");
						}
					}

					// 指派项目代码块状原始位置

					// 新建辅助图样，挂关系
					if (null != drawingType) {
						String productNO = getValueByDisplayName("产品代号").get("value");
						String belongToPhase = getValueByDisplayName("研制阶段").get("value");

						if (null == productNO || productNO.length() == 0) {
							sendMessageToUser("产品代码不能为空！", 0);
							setCursor(defCursor);
						}

						if (null == belongToPhase || belongToPhase.length() == 0) {
							sendMessageToUser("研制阶段不能为空！", 0);
							setCursor(defCursor);
						}
						System.out.println("productNO1=" + productNO);
						System.out.println("drawingNum1=" + drawingNum);
						System.out.println("belongToPhase1=" + belongToPhase);
						WSObject partItemRev = getPartItemRev(productNO, drawingNum, belongToPhase);
						System.out.println("*************************partItemRev********************" + partItemRev);

						if (null != partItemRev) {
							boolean flag = tcop.createRelationship(partItemRev, sdo.getLastestItemRevision(tempObj), "Qh3_AssmDoc");// 辅助

							if (flag) {
								outputStatus("创建零部件和图样文件的关系成功！");
							} else {
								JOptionPane.showMessageDialog(this, "注意：创建零部件和图样文件的关系失败");
								outputStatus("创建零部件和图样文件的关系失败！");
							}
						} else {
							JOptionPane.showMessageDialog(this, "注意：创建零部件和图样文件的关系失败");
							outputStatus("创建零部件和图样文件的关系失败！");
						}
					}
					// 新建辅助图样，挂关系

				}
			} else {
				// 只有第一页图纸才可以更新ItemRevisioin属性

				System.out.println("=update===newItemObjs=" + newItemObjs);
				if (updateItems.size() > 0 && null != saveFolder) {
					Iterator it = updateItems.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry entry = (Map.Entry) it.next();
						String key = (String) entry.getKey();
						WSObject tempObj = updateItems.get(key);

						WSObject originalFolder = sdo.getReferencedFolder(tempObj);
						if (originalFolder != saveFolder) {
							sdo.createRelation(tempObj, saveFolder, "contents");
						}
					}
				}

				String page = projList.get(0).get("第几页");

				outputStatus("正在更新ItemRevisioin的属性信息......");

				Iterator it = updateItems.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry entry = (Map.Entry) it.next();
					String key = (String) entry.getKey();
					WSObject tempObj = updateItems.get(key);

					// ------------ 处理一图多号时 图样代号属性 -------------------
					String tempValue = allItemIDSWithDrawingNO.get(key);
					String tempProperty = getValueByDisplayName("图样代号").get("property");
					itemRevisionAttribs.put(tempProperty, tempValue); // put方法修改图样代号的属性值，该方法会覆盖同key值的value

					for (Object key1 : itemRevisionAttribs.keySet()) {
						String v = (String) itemRevisionAttribs.get(key);
						System.out.println("key1=" + key1 + " v=" + v);
					}

					// 获取界面上的属性信息
					Component[] component = propPanel.getComponents();

					for (int i = 0; i < component.length; i++) {

						if (component[i] instanceof UFCTextField) {
							UFCTextField tempTextField = (UFCTextField) component[i];
						}
						if (component[i] instanceof UFCComboBox) {
							UFCComboBox tempCombo = (UFCComboBox) component[i];

							itemRevisionAttribs.put(tempCombo.getProperty(), (String) tempCombo.getSelectedItem());
						}
					}
					// ------------ 处理一图多号时 图样代号属性 -------------------

					if (page.equals("1")) {
						if (sdo.updateObjectProps(sdo.getLastestItemRevision(tempObj), itemRevisionAttribs)) {
							outputStatus("更新itemid为【" + key + "】ItemRevisioin的属性信息成功");
						} else {
							outputStatus("更新itemid为【" + key + "】ItemRevisioin的属性信息失败");
						}
					} else {
						outputStatus("不更新属性信息");
					}

					List<Map<String, String>> datasetsList = datasetsStatus.get(key);
					if (null != datasetsList) {
						for (int i = 0; i < datasetsList.size(); i++) {
							Map isNewDataset = datasetsList.get(i);
							String datasetname = (String) isNewDataset.get("dsname");
							if ("true".equals((String) isNewDataset.get("isNew"))) {
								outputStatus("正在创建Dataset对象【" + datasetname + "】......");
								WSObject datasetObj = sdo.createDataset(tempObj, datasetname, keepEditable);
								outputStatus("成功创建Dataset对象【" + datasetname + "】完成！");
								listdatasets.add(datasetObj);
							} else {
								outputStatus("正在查找Dataset信息【" + datasetname + "】......");
								WSObject datasetObj = sdo.findDatasetOnItemRev(tempObj, datasetname, keepEditable);
								listdatasets.add(datasetObj);
							}
							outputStatus("成功更新Dataset信息【" + datasetname + "】");
						}
					}

					// 指派项目
					if (null != projectObj) {
						System.out.println("2=" + projectObj.getId() + "," + projectObj.getName());
						boolean succ = tcop.assignObjectToProject(new WSObject[] { sdo.getLastestItemRevision(tempObj) }, projectObj);
						boolean succ1 = tcop.assignObjectToProject(new WSObject[] { tempObj }, projectObj);
						if (succ && succ1) {
							outputStatus("成功指派项目！");
						} else {
							outputStatus("指派项目失败！");
						}
					}

					// 新建辅助图样，挂关系
					if (null != drawingType) {
						String productNO = getValueByDisplayName("产品代号").get("value");
						String belongToPhase = getValueByDisplayName("研制阶段").get("value");

						if (null == productNO || productNO.length() == 0) {
							sendMessageToUser("产品代码不能为空！", 0);
							setCursor(defCursor);
						}

						if (null == belongToPhase || belongToPhase.length() == 0) {
							sendMessageToUser("研制阶段不能为空！", 0);
							setCursor(defCursor);
						}

						System.out.println("productNO2=" + productNO);
						System.out.println("drawingNum2=" + drawingNum);
						System.out.println("belongToPhase2=" + belongToPhase);
						WSObject partItemRev = getPartItemRev(productNO, drawingNum, belongToPhase);

						if (null != partItemRev) {
							// 1:从所有关系中找到该关系，若存在先删除
							WSObject drawingObjRev = sdo.getLastestItemRevision(tempObj);
							WSObject[] drawrelatedObjs = tcop.getRelatedItemRevObj(partItemRev, "Qh3_AssmDoc");
							if (drawrelatedObjs != null) {
								for (int i = 0; i < drawrelatedObjs.length; i++) {
									if (drawrelatedObjs[i].getUid().equals(drawingObjRev.getUid())) {
										tcop.deleteRelationships(partItemRev, new WSObject[] { drawingObjRev }, "Qh3_AssmDoc");
										break;
									}
								}
							}

							// 2:创建关系
							boolean flag = tcop.createRelationship(partItemRev, drawingObjRev, "Qh3_AssmDoc");// 辅助
							if (flag) {
								outputStatus("创建零部件和图样文件的关系成功！");
							} else {
								JOptionPane.showMessageDialog(this, "注意：创建零部件和图样文件的关系失败");
								outputStatus("创建零部件和图样文件的关系失败！");
							}
						} else {
							JOptionPane.showMessageDialog(this, "注意：零部件对象未找到！");
							outputStatus("零部件对象未找到，创建零部件和图样文件的关系失败！");
						}
					}
					// 新建辅助图样，挂关系

				}
			}

			datasetObjs = listdatasets.toArray(new WSObject[] {});

			if (null != drawingSizeValueMap) {
				for (int c = 0; c < datasetObjs.length; c++) {
					sdo.updateObjectProps(datasetObjs[c], drawingSizeValueMap); // ==============kkk
					// 图幅值存入描述属性里
				}
			}

		} catch (TCOperationException e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(this, e1.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;

	}

	private void setIDPropertyValue(WSObject partItemRev, String str) {
		JComponent idcom = mapComponent.get(str);
		if (idcom instanceof UFCTextField) {
			UFCTextField tempTextField = (UFCTextField) idcom;
			String id = tempTextField.getText();
			System.out.println("#########################################  " + id + "  #########################################");
			Map<String, String> idvalue = new HashMap<String, String>();
			idvalue.put("qh3_UcodeID", id);
			System.out.println(partItemRev);
			if (sdo.updateObjectProps(partItemRev, idvalue)) {
				outputStatus("更新【" + partItemRev.getId() + "】的零部件UcodeID属性信息成功");
			} else {
				outputStatus("更新【" + partItemRev.getId() + "】的零部件UcodeID属性信息失败");
			}
		}
	}

	/**
	 * 调用导入BOM
	 * 
	 * @param item
	 * @param itemID
	 */
	private void showBOMFrame(WSObject item, String itemID) {
		// cad只能一次保存一张图 故数据集也只有一个
		WSObject datasetObj = datasetObjs[0];
		new ImportBOMFrame(item, datasetObj, saveFolder, itemID, DrawingName, docList, subObjsList, requestBody, out);
	}

	public void sendMessageToCAD(boolean result) {
		out.setFuncID("SaveDrawing");
		out.setResult("CloseWindow");
		if (result)
			out.setDesc("上传图纸成功");
		else
			out.setDesc("上传图纸失败");
		try {
			out.sendResultToUI();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 提示错误信息，关闭窗口，并发送操作失败信息。
	 * 
	 * @author Liugz
	 * @create on 2008-12-22 This project for CYM
	 * @param message
	 */
	private void sendErrorMessage(String message) {
		if (null == message || "".equals(message))
			JOptionPane.showMessageDialog(this, "图纸信息导入Teamcenter过程中发生错误，请重新连接！", "提示", JOptionPane.ERROR_MESSAGE);
		else
			JOptionPane.showMessageDialog(this, message, "提示", JOptionPane.ERROR_MESSAGE);
		try {
			out.setFuncID("SaveDrawing");
			out.setResult("CloseWindow");
			// out.setResult("false");
			out.setDesc("Import Drawings Failure.");
			out.sendResultToUI();
		} catch (IOException e) {
			e.printStackTrace();
		}
		dispose();
	}

	private void sendMessageToUser(String message, int type) {
		int dlgType = 0;
		switch (type) {
		case 0:
			dlgType = JOptionPane.ERROR_MESSAGE;
			break;
		case 1:
			dlgType = JOptionPane.INFORMATION_MESSAGE;
			break;
		case 2:
			dlgType = JOptionPane.QUESTION_MESSAGE;
			break;
		default:
			dlgType = JOptionPane.INFORMATION_MESSAGE;
		}
		if (null == message || "".equals(message))
			JOptionPane.showMessageDialog(this, "图纸信息导入Teamcenter过程中发生错误，请重新连接！", "提示", dlgType);
		else
			JOptionPane.showMessageDialog(this, message, "提示", dlgType);
	}

	private void outputStatus(String string) {
		jTA_status.append(string + "\n");
		jTA_status.setCaretPosition(jTA_status.getText().length());

	}

	private void outputClear() {
		jTA_status.setText("");

	}

	private void backfillCADDrawing(Map<String, String> infos) {
		// 修改请求中的信息
		out.setFuncID("SaveDrawing");
		out.setResult("Refresh");
		out.setDesc("Backfill CAD Drawing.");
		Element object2 = out.getObjectElement();
		Element props = object2.addElement("properties");

		Element root = this.requestBody;
		List list = new ArrayList();
		Element object = root.element("body").element("object");
		list = object.elements("properties");
		if (list.size() != 0) {
			for (Iterator projItor = list.iterator(); projItor.hasNext();) {
				Element projectElmt = (Element) projItor.next();
				Iterator propertyItor = projectElmt.elementIterator();
				for (Object CADname : infos.keySet()) {
					// System.out.println(name+"---"+propsbackMap.get(name));
					Element prop = props.addElement("property"); // 添加property节点
					prop.addAttribute("name", CADname.toString());
					prop.addAttribute("value", infos.get(CADname));
				}
			}
		}

		try {
			// System.out.println("开始执行导入操作");
			// NcOperator();
			// System.out.println("执行完建立关系完成");

			out.sendResultToUI();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Map<String, String> ItemProEN2CN() {
		Map<String, String> propertyName = new HashMap<String, String>();
		Component[] component = propPanel.getComponents();
		for (int i = 0; i < component.length; i++) {
			if (component[i] instanceof UFCTextField) {
				UFCTextField utf = (UFCTextField) component[i];
				if (utf.getObjType().startsWith("ITEM") && utf.isRequired()) {
					propertyName.put(utf.getProperty(), utf.getDisplayName());
				}
			}
			if (component[i] instanceof UFCComboBox) {
				UFCComboBox utf = (UFCComboBox) component[i];
				if (utf.getObjType().startsWith("ITEM") && utf.isRequired()) {
					propertyName.put(utf.getProperty(), utf.getDisplayName());
				}
			}
		}
		System.out.println("propertyName" + propertyName);
		return propertyName;
	}

	private List<Map<String, String>> ItemProCN2Value() {
		Component[] component = propPanel.getComponents();
		List<Map<String, String>> tempPropsList = new ArrayList<Map<String, String>>();
		Map<String, String> props = new HashMap<String, String>();
		for (int i = 0; i < component.length; i++) {
			if (component[i] instanceof UFCTextField) {
				UFCTextField utf = (UFCTextField) component[i];
				if (utf.getObjType().startsWith("ITEM") && utf.isRequired()) {
					props.put(utf.getDisplayName(), utf.getText());
				}
			}
			if (component[i] instanceof UFCComboBox) {
				UFCComboBox utf = (UFCComboBox) component[i];
				if (utf.getObjType().startsWith("ITEM") && utf.isRequired()) {
					if (utf.getSelectedItem() != null) {
						props.put(utf.getDisplayName(), utf.getSelectedItem().toString());
					}
				}
			}
		}
		tempPropsList.add(props);
		return tempPropsList;
	}

	/**
	 * @author Liugz
	 * @create on 2009-7-1 This project for CAD_Concrete
	 * @return
	 */
	protected boolean convertDWGAndCreateDS(WSObject itemObj, String itemid, String dwgFilePath, String datasetName) {
		// return true;
		// 读取首选项，判断是否进行转换操作
		String[] values = sdo.getPreferenceValue("UFC_CONVERT_DWG2PDF", "site");
		if (null == values || values.length != 1 || !"YES".equalsIgnoreCase(values[0])) {
			return true;
		}

		if (null == statedlg) {
			statedlg = new StateDialog("正在转换DWG文件，请稍候......");
		} else
			statedlg.setMessage("正在转换DWG文件，请稍候......");
		try {
			outputStatus("正在执行DWG图纸转附加文件操作，请稍候......");
			// String dwgFilePath = docList.get(0).get("LocalPath");
			// String pdfFilePath = Util.convertDWG2PDF(dwgFilePath,
			// projList.get(
			// 0).get("产品编码"));
			String pdfFilePath = Util.convertDWG2PDF(dwgFilePath, itemid);
			if (null == pdfFilePath) {
				outputStatus("DWG图纸转换附加格式时发生错误");
				sendMessageToUser("DWG图纸转换附加格式时发生错误，原因可能是由于Teamcenter首选项配置错误，或者转换时发生异常", 0);
				return false;
			}
			// ITCTCObjOperation tcop = new TCObjOperation();
			// TCFileOperation tcf = new TCFileOperation();
			ITCTCObjOperation tcop = (ITCTCObjOperation) ServiceUtil.getService(ITCTCObjOperation.class.getName(), SaveDrawingOperation.class.getClassLoader());
			// context.registerService((ITCFileOperation.class).getName(), new
			// TCFileOperation(), null);
			ITCFileOperation tcf = (ITCFileOperation) ServiceUtil.getService(ITCFileOperation.class.getName(), SaveDrawingOperation.class.getClassLoader());
			WSObject itemRev = sdo.getLastestItemRevision(itemObj);
			WSObject itemWSObj = itemRev;// TCUtil.modelObject2WSObject(itemRev);
			// 先查询Dataset是否存在
			WSObject dsObj = null;

			// String pdm_Type_add=ParseRequestXML.getPdm_Type_add();

			WSObject[] dsObjs = tcop.getDatasetOfItemRevision(itemWSObj, new String[] { "PDF" });
			// WSObject[] dsObjs = tcop.getDatasetOfItemRevision(itemWSObj,new
			// String[] { pdm_Type_add });
			if (null != dsObjs && dsObjs.length != 0) {
				for (int i = 0; i < dsObjs.length; i++) {
					WSObject ds = dsObjs[i];
					String dsName = ds.getName();
					// if (dsName.equalsIgnoreCase(datasetName)&&
					// "PDF".equals(ds.getType())) {
					if (dsName.equalsIgnoreCase(datasetName) && "PDF".equalsIgnoreCase(ds.getType())) {
						dsObj = ds;
						break;
					}
				}
			}
			if (null == dsObj) {
				// 创建DATASET对象
				// dsObj = tcop.createDatasets(itemWSObj, "IMAN_specification",
				// "PDF", new String[] { datasetName })[0];
				dsObj = tcop.createDatasets(itemWSObj, "IMAN_specification", "PDF", new String[] { datasetName })[0];

				if (null == dsObj) {
					outputStatus("创建附加数据集时发生错误");
					sendMessageToUser("创建附加数据集时发生错误", 0);
					return false;
				}
				outputStatus("附加数据集创建成功");
			}

			// 准备上传PDF文件
			// String refName = TCUtil.getRefNameOfDataset("PDF")[0];

			// 测试暂且屏蔽
			// String refName = TCUtil.getRefNameOfDataset(pdm_Type_add)[0];
			String refName = "";
			System.out.println(pdfFilePath + datasetName);
			if (!tcf.uploadFile(dsObj, refName, pdfFilePath)) {
				outputStatus("上传 附加 文件时发生错误");
				sendMessageToUser("上传 附加 文件时发生错误", 0);
				// 上传成功后删除该PDF文件
				File pdfFile = new File(pdfFilePath);
				pdfFile.delete();
				return false;
			} else {
				outputStatus("附加 文件上传成功");
				// 上传成功后删除该PDF文件
				File pdfFile = new File(pdfFilePath);
				pdfFile.delete();
				return true;
			}

		} catch (TCOperationException e) {
			outputStatus(e.getMessage());
			sendMessageToUser(e.getMessage(), 0);
			return false;
		}

	}

	public void componentHidden(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentResized(ComponentEvent e) {
		// System.out.println( "componentResized!!!!!!!! ");
		Component[] component = propPanel.getComponents();
		// component[1].setBounds(100,100,100,100);
		for (int i = 0; i < component.length; i++) {
			;
			Rectangle rect1 = component[i].getBounds();
			int dx = (this.getSize().width - widthFrame) / 2;
			if (i % 4 == 1) {
				int x1 = component[i].getX();
				rect1.setSize((int) (comWidth + dx), (int) rect1.getHeight());
				rect1.x = startX + labelWidth + spacing;
			}

			if (i % 4 == 2) // 大小不变 ，位置变化
			{ // rect1.setSize((int )( 120+dx), (int )rect1.getHeight());
				rect1.x = this.getSize().width / 2;

			}

			if (i % 4 == 3) // 大小位置都 变化
			{ // rect1.setSize((int )( 120+dx), (int )rect1.getHeight());
				rect1.x = this.getSize().width / 2 + labelWidth + spacing;
				rect1.setSize((int) (comWidth + dx), (int) rect1.getHeight());
			}

			component[i].setBounds(rect1);
		}
		propPanel.updateUI();
	}

	public void componentShown(ComponentEvent e) {

	}

	/**
	 * 
	 * TODO 显示对话框
	 * 
	 * @return void
	 * @author lijj created on 2010-12-22下午02:38:01
	 */
	private void setFrameVisibility() {
		// 显示对话框
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		setSize(frameSize.width, frameSize.height);
		setVisible(true);
	}

	/**
	 * 
	 * TODO 由配置文件的source节点的值确定属性来源对象
	 * 
	 * @return WSObject
	 * @author lijj created on 2010-12-22下午02:39:22
	 */
	private WSObject getSourceObject(WSObject itemObj, String sourceStr) {
		if (sourceStr.equals("ITEM")) {
			return itemObj;
		}
		if (sourceStr.equals("IR")) {
			try {
				return tcop.getItemRevision(itemObj, "last");// 取最新的itemRevision
			} catch (TCOperationException e) {
				e.printStackTrace();
			}
		}
		if (sourceStr.equals("IMF")) {
			try {
				return op.getMasterForm(itemObj, null); // ------ 不确定
			} catch (TCOperationException e) {
				e.printStackTrace();
			}
		}
		if (sourceStr.equals("IRMF")) {
			WSObject itemRev;
			try {
				itemRev = tcop.getItemRevision(itemObj, "last");// 取最新的itemRevision
				return tcop.getRevMasterForm(itemRev);
			} catch (TCOperationException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 
	 * TODO 根据标记1，标记2，标记3判断所属阶段
	 * 
	 * @return String
	 * @author lijj created on 2010-12-30上午11:19:54
	 */
	private String getBelongToPhase(String biaoji1, String biaoji2, String biaoji3) {
		if (biaoji3.length() > 0) {
			// return biaoji3.substring(0, 2);
			return biaoji3;
		} else if (biaoji2.length() > 0) {
			// return biaoji2.substring(0, 2);
			return biaoji2;
		} else {
			// return biaoji1.substring(0, 2);
			return biaoji1;
		}
	}

	/**
	 * 
	 * TODO 设置itemid对应的各种状态：exist=true,refresh=true,haveDS=true,checkOut=true
	 * 是否存在 是否可更新 是否有数据集 是否被签出 主要对成员变量 itemStatus 进行操作
	 * 
	 * @return void
	 * @author lijj created on 2011-1-5下午02:05:23
	 */
	private void setItemIdStatus(String itemid, boolean exist, boolean refresh, boolean haveDS, boolean checkOut) {
		if (null != itemStatus && itemStatus.size() > 0) {
			Iterator it = itemStatus.entrySet().iterator();

			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				String key = (String) entry.getKey();
				Map<String, Boolean> value = itemStatus.get(key);

			}
		}
	}

	/**
	 * 
	 * TODO遍历itemStatus判断是否有可以更新的item
	 * 
	 * @return boolean
	 * @author lijj created on 2011-1-5下午03:07:36
	 */
	private boolean findTheFirstExist() {
		boolean findIt = false;
		if (null != itemStatus && itemStatus.size() > 0) {
			Iterator it = itemStatus.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				String key = (String) entry.getKey();
				Map<String, Boolean> value = itemStatus.get(key);
				if (value.get("refresh")) {
					findIt = true;
					break;
				}
			}
		}
		return findIt;
	}

	/**
	 * 
	 * TODO 根据图样代号判断是否是辅助图样及其类型
	 * 
	 * @return String 返回null表示不是辅助图样
	 * @author lijj created on 2011-11-21下午04:04:00
	 */
	private String getTypeByDrawNum(String drawingNumber) {
		if (null == drawingNumber) {
			drawingNum = null;
			return null;
		}

		if (drawingNumber.endsWith("BL")) {
			drawingNum = drawingNumber.substring(0, drawingNumber.length() - 2);
			return "布置图";
		} else if (drawingNumber.endsWith("WX")) {
			drawingNum = drawingNumber.substring(0, drawingNumber.length() - 2);
			return "外形图";
		} else if (drawingNumber.endsWith("BJ")) {
			drawingNum = drawingNumber.substring(0, drawingNumber.length() - 2);
			return "标志图";
		} else if (drawingNumber.endsWith("XT")) {
			drawingNum = drawingNumber.substring(0, drawingNumber.length() - 2);
			return "系统图";
		} else if (drawingNumber.endsWith("GX")) {
			drawingNum = drawingNumber.substring(0, drawingNumber.length() - 2);
			return "光学系统图";
		} else if (drawingNumber.endsWith("AZ")) {
			drawingNum = drawingNumber.substring(0, drawingNumber.length() - 2);
			return "安装图";
		} else if (drawingNumber.endsWith("BT")) {
			drawingNum = drawingNumber.substring(0, drawingNumber.length() - 2);
			return "包装图";
		} else if (drawingNumber.endsWith("LJL")) {
			drawingNum = drawingNumber.substring(0, drawingNumber.length() - 3);
			return "逻辑图";
		} else if (drawingNumber.endsWith("JL")) {
			drawingNum = drawingNumber.substring(0, drawingNumber.length() - 2);
			return "接线图";
		} else if (drawingNumber.endsWith("LL")) {
			drawingNum = drawingNumber.substring(0, drawingNumber.length() - 2);
			return "线缆连接图";
		} else if (drawingNumber.endsWith("BS")) {
			drawingNum = drawingNumber.substring(0, drawingNumber.length() - 2);
			return "靶场实验图";
		} else if (drawingNumber.endsWith("ZY")) {
			drawingNum = drawingNumber.substring(0, drawingNumber.length() - 2);
			return "装药图";
		} else if (drawingNumber.endsWith("FJ")) {
			drawingNum = drawingNumber.substring(0, drawingNumber.length() - 2);
			return "工具、附件图";
		} else if (drawingNumber.endsWith("DF")) {
			drawingNum = drawingNumber.substring(0, drawingNumber.length() - 2);
			return "电缆敷设图";
		} else if (drawingNumber.endsWith("WL")) {
			drawingNum = drawingNumber.substring(0, drawingNumber.length() - 2);
			return "网络图";
		} else if (drawingNumber.endsWith("GL")) {
			drawingNum = drawingNumber.substring(0, drawingNumber.length() - 2);
			return "功能图";
		} else if (drawingNumber.endsWith("FL")) {
			drawingNum = drawingNumber.substring(0, drawingNumber.length() - 2);
			return "方框图";
		} else if (drawingNumber.endsWith("DL")) {
			drawingNum = drawingNumber.substring(0, drawingNumber.length() - 2);
			return "电路图";
		} else if (drawingNumber.endsWith("MX")) {
			drawingNum = drawingNumber.substring(0, drawingNumber.length() - 2);
			return "明细表";
		} else if (drawingNumber.endsWith("BZ")) {
			drawingNum = drawingNumber.substring(0, drawingNumber.length() - 2);
			return "标准件汇总表";
		} else if (drawingNumber.endsWith("WG")) {
			drawingNum = drawingNumber.substring(0, drawingNumber.length() - 2);
			return "外购件汇总表";
		} else if (drawingNumber.endsWith("GZJ")) {
			drawingNum = drawingNumber.substring(0, drawingNumber.length() - 3);
			return "关重件汇总表";
		} else if (drawingNumber.endsWith("JY")) {
			drawingNum = drawingNumber.substring(0, drawingNumber.length() - 2);
			return "借用件汇总表";
		} else if (drawingNumber.endsWith("TM")) {
			drawingNum = drawingNumber.substring(0, drawingNumber.length() - 2);
			return "图幅目录";
		} else if (drawingNumber.endsWith("LM")) {
			drawingNum = drawingNumber.substring(0, drawingNumber.length() - 2);
			return "零部件目录";
		} else if (drawingNumber.endsWith("WM")) {
			drawingNum = drawingNumber.substring(0, drawingNumber.length() - 2);
			return "设计文件总目录";
		} else if (drawingNumber.endsWith("GF")) {
			drawingNum = drawingNumber.substring(0, drawingNumber.length() - 2);
			return "制造验收规范（晒蓝）";
		} else if (drawingNumber.endsWith("CJS")) {
			drawingNum = drawingNumber.substring(0, drawingNumber.length() - 3);
			return "尺寸链计算书（晒蓝）";
		} else if (drawingNumber.endsWith("DY")) {
			drawingNum = drawingNumber.substring(0, drawingNumber.length() - 2);
			return "电气检查要求";
		} else {
			drawingNum = drawingNumber;
			return null;
		}
	}

	/**
	 * 
	 * TODO 重新给图样类型赋值
	 * 
	 * @return void
	 * @author lijj created on 2011-11-21下午05:11:56
	 */
	private void resetCommentValue(String drType) {
		Component[] component = propPanel.getComponents();
		for (int i = 0; i < component.length; i++) {
			if (component[i] instanceof UFCComboBox) {
				UFCComboBox tempCombo = (UFCComboBox) component[i];

				if (tempCombo.getProperty().equals("qh3_DrawingType")) {
					tempCombo.setSelectedItem(drType);
					break;
				}
			}
		}
	}

	/**
	 * 
	 * TODO 根据产品代号，图样代号，阶段标记查找零部件对象
	 * 
	 * @return WSObject
	 * @author lijj created on 2011-11-21下午06:04:57
	 */
	private WSObject getPartItemRev(String productNO, String drawingNO, String belongToPhase) {

		String[] criterias = new String[] { productNO, drawingNO, belongToPhase };

		try {

			WSObject[] partObjs = sdo.useSavedQuery(criterias, "__Query_DesPartByProNODrawNO");

			// if (null == partObjs) {
			// partObjs = sdo.useSavedQuery(criterias,
			// "__Query_DesPartByProjectID");
			// }
			if (null == partObjs) {
				return null;
			}
			if (partObjs.length == 0) {
				return null;
			} else if (partObjs.length == 1) {
				return tcop.getItemRevision(partObjs[0], "last");
			} else {
				System.out.println("根据产品代号，图样代号和阶段联合查找的对象大于一个----" + partObjs.length);
				return null;
			}
		} catch (TCOperationException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 
	 * TODO 根据数据集第一个命名引用 返回localPath值 处理检出后文件改名再检入不能替换原命名引用的情况
	 * 
	 * @return List<Map<String,String>>
	 * @author lijj created on 2011-11-24上午10:05:30
	 */
	private List<Map<String, String>> getLocalPathMap(WSObject ds, String fileTrail, String localFilePath) {

		// 判断是否需要更改命名,保证一个数据集下只能有一个命名的引用
		List<Map<String, String>> resultDocList = new ArrayList<Map<String, String>>();
		try {
			String originalFileName = Util.getDSFileRefName(ds, fileTrail);
			File savedFile = new File(localFilePath);
			String saveFileName = savedFile.getName();
			String loadPath = "";

			String dsNameStr = tcop.getPropertyValueOfObject(ds, "object_name");
			System.out.println("originalFileName=" + originalFileName);
			File tcFile = null;
			if (originalFileName.length() == 0) {
				String oldName = saveFileName.substring(0, saveFileName.lastIndexOf("."));
				tcFile = new File(System.getenv("UFCROOT") + "\\Temp\\" + oldName + "-" + dsNameStr + ".dwg");
			} else {
				if (!saveFileName.equals(originalFileName)) {
					tcFile = new File(System.getenv("UFCROOT") + "\\Temp\\" + originalFileName);
				}
			}
			if (tcFile != null) {
				if (tcFile.exists()) {
					tcFile.delete();
				}
			} else {
				tcFile = new File(System.getenv("UFCROOT") + "\\Temp\\" + originalFileName);
			}

			Util.copyFile(savedFile.getAbsolutePath(), tcFile.getAbsolutePath());
			loadPath = tcFile.getAbsolutePath();
			Map<String, String> tempMap = new HashMap<String, String>();
			tempMap.put("LocalPath", loadPath);
			resultDocList.add(tempMap);
		} catch (TCOperationException e) {
			e.printStackTrace();
		}

		return resultDocList;
	}

	@Override
	public void cancelled(Map arg0) {
		dispose();
	}

	@Override
	public void finished(Object arg0) {
		dispose();
	}

	@Override
	public void initComponents() {

	}

}