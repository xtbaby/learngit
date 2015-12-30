package com.ufc.uif.qh3.acad.ui.wizard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.dom4j.Element;
import org.netbeans.api.wizard.WizardDisplayer;
import org.netbeans.api.wizard.displayer.NavButtonManager;
import org.netbeans.api.wizard.displayer.WizardDisplayerImpl;
import org.netbeans.spi.wizard.Wizard;
import org.netbeans.spi.wizard.WizardPage;
import org.netbeans.spi.wizard.WizardPanelNavResult;

import com.ufc.uif.base_adaptor.AdaptorWriter;
import com.ufc.uif.qh3.acad.images.IconManager;
import com.ufc.uif.qh3.acad.operation.ParseRequestXML;
import com.ufc.uif.qh3.acad.tools.Dom4jUtil;
import com.ufc.uif.qh3.acad.ui.SaveDrawingDialog;
import com.ufc.uif.qh3.acad.ui.TableRenderer;
import com.ufc.uif.tccommunicationimpl.operation.TCBOMOperation;
import com.ufc.uif.tccommunicationimpl.operation.TCObjOperation;
import com.ufc.uif.tccommunicationimpl.operation.TCStructureOperation;
import com.ufc.uif.tcuacommunication.objects.WSObject;
import com.ufc.uif.tcuacommunication.operation.ITCObjectOperation;
import com.ufc.uif.tcuacommunication.operation.ITCStructureOperation;
import com.ufc.uif.tcuacommunication.operation.ITCTCBOMOperation;
import com.ufc.uif.tcuacommunication.operation.exception.TCOperationException;

public class ImportBOMPanel extends WizardPage {

	private JTree tree;
	private JTable table;
	private JTextArea txtArea;
	private Vector<String> col;
	private JButton next;
	private JSplitPane tbPane, lrPane;
	private Vector<Vector<String>> rowData;
	private static final long serialVersionUID = 1L;
	private JLabel jLabel1, jLabel2, jLabel3, jLabel4;
	private JScrollPane scrollPane1, scrollPane2, scrollPane3;
	private JLabel projectLab, phaseLab, drawingNoLab, partNameLab;
	private JTextField projectTxt, phaseTxt, drawingNoTxt, partNameTxt;
	private JPanel topPanel, botPanel, leftPanel, rightPanel, operationPanel;

	private Dom4jUtil util;
	private Element objEle;
	private AdaptorWriter out;
	private Element requestBody;

	private DefaultTableModel model;
	private boolean mathSucc = false;
	private boolean gotoNext = false;
	private List<String> notExistItemNOList;

	private WizardPanelNavResult wizardPanelNavResult;

	private WSObject view;
	private TCObjOperation tcop;
	private ITCTCBOMOperation bom;
	private ITCObjectOperation op;
	private ITCStructureOperation so;

	/** װ����� */
	private WSObject itemObj;
	/** װ������ItemRevisioin���� */
	private WSObject rootItemRev;

	private List<Map<String, String>> elcObjsList;

	public ImportBOMPanel(SaveDrawingDialog dialog) {

		this.requestBody = dialog.requestBody;
		this.itemObj = RegDrawingWizardPanel.itemObj;

		util = Dom4jUtil.getDom4jUtil();
		objEle = requestBody.element("body").element("object");
		notExistItemNOList = new ArrayList<String>();

		initComponents();

		bom = new TCBOMOperation();
		tcop = new TCObjOperation();
		so = new TCStructureOperation();

		WizardDisplayerImpl displayer = (WizardDisplayerImpl) WizardDisplayer.defaultInstance;
		NavButtonManager mgr = displayer.getButtonManager();
		next = mgr.getNext();
	
		// ���ô��屳����ɫ
		setBackground(new Color(226, 245, 252));
	}

	public void initComponents() {

		this.setLayout(new BorderLayout());
		// ���·ָ�
		tbPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		tbPane.setDividerSize(5);// ���÷ָ����Ĵ�С
		tbPane.setDividerLocation(300);
		tbPane.setOneTouchExpandable(true);// �÷ָ�����ʾ����ͷ
		tbPane.setContinuousLayout(true);// ������ͷ���ػ�ͼ��
		// ���ҷָ�
		lrPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		lrPane.setDividerSize(5);// ���÷ָ����Ĵ�С
		lrPane.setDividerLocation(100);
		lrPane.setOneTouchExpandable(true);// �÷ָ�����ʾ����ͷ
		lrPane.setContinuousLayout(true);// ������ͷ���ػ�ͼ��

		// �����ϰ벿��
		// �����ϰ벿����߲���
		topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		//
		JPanel ttopPanel = new JPanel();
		ttopPanel.setBackground(new Color(226, 245, 252));
		ttopPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		projectLab = new JLabel("��Ŀ");
		projectTxt = new JTextField(10);
		projectTxt.setEditable(false);
		ttopPanel.add(projectLab);
		ttopPanel.add(projectTxt);

		phaseLab = new JLabel("�׶�");
		phaseTxt = new JTextField(10);
		phaseTxt.setEditable(false);
		ttopPanel.add(phaseLab);
		ttopPanel.add(phaseTxt);

		drawingNoLab = new JLabel("ͼ��");
		drawingNoTxt = new JTextField(10);
		drawingNoTxt.setText(util.getProValue(objEle, "ͼ��"));
		drawingNoTxt.setEditable(false);
		ttopPanel.add(drawingNoLab);
		ttopPanel.add(drawingNoTxt);

		partNameLab = new JLabel("����");
		partNameTxt = new JTextField(10);
		partNameTxt.setText(util.getProValue(objEle, "ͼ������"));
		partNameTxt.setEditable(false);
		ttopPanel.add(partNameLab);
		ttopPanel.add(partNameTxt);

		topPanel.add(ttopPanel, BorderLayout.NORTH);
		// end

		leftPanel = new JPanel();
		leftPanel.setLayout(new BorderLayout());
		tree = new JTree();
		scrollPane2 = new JScrollPane();
		scrollPane2.setViewportView(tree);
		leftPanel.add(scrollPane2);
		lrPane.setLeftComponent(leftPanel);
		// -----------------------------------------------end
		// �����ϰ벿���ұ߲���
		rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout());

		col = new Vector<String>();
		col.add("��ϸ���");
		col.add("�������");
		col.add("��������");
		col.add("�������");
		col.add("����");
		col.add("��������");
		col.add("����");
		col.add("��ע");
		col.add("״̬");

		table = new JTable(rowData, col) {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		elcObjsList = ParseRequestXML.getSubObjsInfo(requestBody);

		model = (DefaultTableModel) table.getModel();
		// ����table�еĵ�8��
		TableColumnModel colModel = table.getColumnModel();
		TableColumn column = colModel.getColumn(8);
		column.setMinWidth(0);
		column.setMaxWidth(0);

		for (int i = 0; i < elcObjsList.size(); i++) {
			model.addRow(new String[] { "" + (i + 1), elcObjsList.get(i).get("�������"), elcObjsList.get(i).get("��������"), elcObjsList.get(i).get("�������"), elcObjsList.get(i).get("����"),
					elcObjsList.get(i).get("����"), elcObjsList.get(i).get("����"), elcObjsList.get(i).get("��ע"), "" });
		}

		scrollPane3 = new JScrollPane();
		scrollPane3.setViewportView(table);
		rightPanel.add(scrollPane3, BorderLayout.CENTER);

		JPanel rbotPanel = new JPanel();
		rbotPanel.setOpaque(false);
		rbotPanel.setPreferredSize(new Dimension(0, 35));
		rbotPanel.setLayout(new GridLayout(1, 2));

		JPanel jPanel4 = new JPanel();
		jPanel4.setLayout(new GridLayout(1, 4));

		jLabel1 = new JLabel();
		// jLabel1.setHorizontalAlignment(2);
		jLabel1.setIcon(IconManager.getIcon("red.png"));
		jLabel1.setText("������");
		jPanel4.add(jLabel1);

		jLabel2 = new JLabel();
		// jLabel2.setHorizontalAlignment(2);
		jLabel2.setIcon(IconManager.getIcon("green.png"));
		jLabel2.setText("����");
		jPanel4.add(jLabel2);

		jLabel3 = new JLabel();
		// jLabel3.setHorizontalAlignment(2);
		jLabel3.setIcon(IconManager.getIcon("black.png"));
		jLabel3.setText("�ѹ���");
		jPanel4.add(jLabel3);

		jLabel4 = new JLabel();
		// jLabel4.setHorizontalAlignment(2);
		jLabel4.setIcon(IconManager.getIcon("gray.png"));
		jLabel4.setText("��ǰ��ȥ");
		jPanel4.add(jLabel4);

		operationPanel = new JPanel();
		operationPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		JButton mathBtn = new JButton("ƥ��");
		mathBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mathBtnAction();
			}

		});
		// operationPanel.add(btn1);
		// operationPanel.add(new JButton("׷��"));

		operationPanel.add(mathBtn);
		// operationPanel.add(new JButton("����"));
		operationPanel.add(new JButton("����"));

		rbotPanel.add(jPanel4);
		rbotPanel.add(operationPanel);

		rightPanel.add(rbotPanel, BorderLayout.SOUTH);

		lrPane.setRightComponent(rightPanel);
		// -----------------------------------------------end
		topPanel.add(lrPane);
		tbPane.setTopComponent(topPanel);
		// -----------------------------------------------end
		// �����°벿��
		botPanel = new JPanel();
		botPanel.setLayout(new BorderLayout());

		txtArea = new JTextArea();
		txtArea.setBorder(null);
		txtArea.setEditable(false);
		txtArea.setSelectedTextColor(Color.WHITE);
		txtArea.setSelectionColor(Color.BLACK);
		txtArea.setFont(new Font("����", 0, 15));

		scrollPane1 = new JScrollPane();
		scrollPane1.setViewportView(txtArea);
		botPanel.add(scrollPane1, BorderLayout.CENTER);
		tbPane.setBottomComponent(botPanel);
		// -----------------------------------------------end

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(tbPane);
		this.add(panel);

	}

	private void mathBtnAction() {

		mathSucc = true;
		outputStatus("����ƥ�������Ϣ,���Ե�......");
		queryItems();
		outputStatus("�����Ϣƥ�����!");

		if (notExistItemNOList.size() == 0) {
			next.setEnabled(true);
			JOptionPane.showMessageDialog(this, "�����Ϣƥ�����", "��ʾ", JOptionPane.WARNING_MESSAGE);
		} else {
			next.setEnabled(false);
			JOptionPane.showMessageDialog(this, "�����Ϣƥ�䲻��ȫ���봴����Ҫ����������ڽ��в�Ʒ�ṹ�ĵ���!", "��ʾ", JOptionPane.WARNING_MESSAGE);
		}
	}

	private void queryItems() {
		for (int i = 0; i < table.getRowCount(); i++) {
			try {
				String flag = (String) table.getValueAt(i, 0);
				String ID = (String) table.getValueAt(i, 1);

				Map<String, String> queryMap = new HashMap<String, String>();
				queryMap.put("����� ID", ID);

				WSObject[] wsObjs = tcop.queryObjectsBySavedQuery("Item ID", queryMap);

				if (null != wsObjs && wsObjs.length == 1) {

					String itemID = wsObjs[0].getId();
					String itemName = wsObjs[0].getName();
					table.setValueAt(itemID, i, 1);
					table.setValueAt(itemName, i, 3);
					table.setValueAt("0", i, 8);
					outputStatus("������Ϊ " + flag + " �������Ϣ!");
				} else {
					notExistItemNOList.add(flag);
					table.setValueAt("1", i, 8);
					outputStatus("û���ҵ����Ϊ " + flag + " �������Ϣ!");
				}
			} catch (TCOperationException e) {
				e.printStackTrace();
			}
		}
		table.revalidate();
		table.setDefaultRenderer(table.getColumnClass(8), new TableRenderer());
		table.updateUI();
	}

	@Override
	public WizardPanelNavResult allowNext(String stepName, Map settings, Wizard wizard) {
		return importBOM();
	}

	private void outputStatus(String str) {
		txtArea.append(str + "\n");
		txtArea.setCaretPosition(txtArea.getText().length());
	}

	private WizardPanelNavResult importBOM() {

		outputStatus("���ڴ�����Ʒ�ṹ,���Ե�......");
		if (createBom()) {
			gotoNext = true;
			outputStatus("��Ʒ�ṹ�����ɹ�!");
			wizardPanelNavResult = WizardPanelNavResult.PROCEED;
		} else {
			gotoNext = false;
			outputStatus("��Ʒ�ṹ����ʧ��!");
			wizardPanelNavResult = WizardPanelNavResult.REMAIN_ON_PAGE;
		}

		return wizardPanelNavResult;
	}

	private boolean createBom() {
		boolean result = false;
		// ����BOM
		List<WSObject> itemRevs = new ArrayList<WSObject>();
		List<Map<String, String>> itemRevAttribs = new ArrayList<Map<String, String>>();
		outputStatus("���ڻ�ȡBOM������Ϣ.......");

		checkBOMViewRevision();

		for (int i = 0; i < table.getRowCount(); i++) {
			// ��ȡ������Ϣ
			String id = (String) table.getValueAt(i, 1);
			Map<String, String> queryMap = new HashMap<String, String>();
			queryMap.put("ID", id);

			WSObject[] itemRev = null;
			try {
				itemRev = tcop.queryObjectsBySavedQuery("Item ID", queryMap);
				Map<String, String> attributes = new HashMap<String, String>();
				String no = (String) table.getValueAt(i, 7);

				if (no.matches("\\d+")) {
					attributes.put("bl_quantity", no);
				}
				itemRevs.add(itemRev[0]);
				itemRevAttribs.add(attributes);
			} catch (TCOperationException e) {
				e.printStackTrace();
			}

		}
		//
		outputStatus("��ȡBOM������Ϣ�ɹ�");
		try {
			// �ж�BOM�����ǩ��״̬

			if (null != view) {
				if (!op.isCheckOut(view)) {
					if (op.checkOutObjects(new WSObject[] { view }, ""))
						outputStatus("���BOM View");
					else {
						outputStatus("�������ʧ�ܣ�δ�ܼ�����������²���");
						return false;
					}
				}
				outputStatus("��ʼ����BOM View��Ϣ......");

				result = bom.createPSBOM(rootItemRev, (ArrayList<WSObject>) itemRevs, itemRevAttribs, "view");
				if (result) {
					outputStatus("BOM View ���³ɹ�");
					if (!op.checkin(new WSObject[] { view }))
						outputStatus("ǩ�����ʱ�������󣬶��󱣳ֿɱ༭״̬");
				}
			} else {
				outputStatus("׼���½�BOM View��Ϣ......");
				result = bom.createPSBOM(rootItemRev, (ArrayList<WSObject>) itemRevs, itemRevAttribs, "view");

				if (result)
					outputStatus("BOM View ���³ɹ�");
			}
		} catch (TCOperationException e) {
			e.printStackTrace();
		}
		// -----------------------------------------------end
		return result;
	}

	/**
	 * ���BOMView���µ�Revision�ļ��״̬
	 * 
	 * @author zhangwh
	 * @create on 2015-12-25
	 * @return
	 */
	private WSObject checkBOMViewRevision() {
		try {
			rootItemRev = tcop.getItemRevision(itemObj, "last");
		} catch (TCOperationException e) {
			e.printStackTrace();
			System.out.println("\t��ȡRevision��Ϣʧ��");
		}

		try {
			view = so.getBVRbyBVType(rootItemRev, "view");
			if (null != view) {
				outputStatus("���BOM View�ļ��״̬......");
				if (!op.isCheckOut(view)) {
					outputStatus("ָ����BOM View��δ���");
				} else {
					if (!op.isCheckOutByUser(view)) {
						outputStatus("��BOM View�ѱ������û����");
						outputStatus("�����û��������ڱ༭��BVR���������ܼ���������");
					} else
						outputStatus("���Ѽ����BOM View");
				}
			}
		} catch (TCOperationException te) {
			outputStatus("��ȡBOMView��Ϣʱ���������޷���������");
			outputStatus("δ�ܻ�ȡBOMView��Ϣ");
			return null;
		}
		return view;
	}
}
