package com.ufc.uif.qh3.acad.ui;

import static javax.swing.BorderFactory.createTitledBorder;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.dom4j.Element;

import com.sunking.swing.JDatePicker;

import com.ufc.uif.base_adaptor.AdaptorWriter;
import com.ufc.uif.base_adaptor.busioperator.BusiOperationException;
import com.ufc.uif.qh3.acad.images.IconManager;
import com.ufc.uif.qh3.acad.operation.ParseConfigXMl;
import com.ufc.uif.tccommunicationimpl.operation.TCFileManagement;
import com.ufc.uif.tccommunicationimpl.operation.TCObjOperation;
import com.ufc.uif.tccommunicationimpl.operation.TCObjectOperation;
import com.ufc.uif.tccommunicationimpl.operation.connecttc.TCUtil;
import com.ufc.uif.tcuacommunication.connecttc.SessionManager;
import com.ufc.uif.tcuacommunication.objects.WSObject;
import com.ufc.uif.tcuacommunication.operation.ITCObjectOperation;
import com.ufc.uif.tcuacommunication.operation.ITCTCObjOperation;
import com.ufc.uif.tcuacommunication.operation.OperationManagerFactory;
import com.ufc.uif.tcuacommunication.operation.exception.TCOperationException;
import com.ufc.uif.util.Dom4jUtil;

public class OpenDrawingDialog extends _UIFDialog {

	private static final long serialVersionUID = 1L;

	private AdaptorWriter out;
	private boolean isHomeTree;
	private TreePath clickTreePath;
	private List<String> filePathList = new ArrayList<String>();
	private String filePath;
	private File downFile;
	private WaitDialog waitDlg;
	private String newfileName = "";
	private String datasetname = "";
	private ITCObjectOperation tcObjectOperation;
	private DefaultTreeModel homeTreeDefmodel;
	private DefaultTreeModel advSearchDefmodel;
	private ITCTCObjOperation tcObjOper;
	private ParseConfigXMl parseConfig;
	private WSObject[] advSearchObjects;
	private WSObjectTreeCellRender render = new WSObjectTreeCellRender();

	private javax.swing.JPanel mainPanel;
	private javax.swing.JPanel homeTreePanel;
	private javax.swing.JPanel advSearchPanel;
	private javax.swing.JPanel contentPanel;
	private javax.swing.JPanel advSearchInputPanel;
	private javax.swing.JPanel buttonPanel;
	private javax.swing.JPanel preViewPanel;
	private javax.swing.JSplitPane jSplitPane1;
	private javax.swing.JSplitPane jSplitPane2;
	private javax.swing.JTabbedPane jTabbedPane1;
	private javax.swing.JScrollPane propertyScroll;
	private javax.swing.JScrollPane homeTreeScroll;
	private javax.swing.JScrollPane searchResultScroll;
	private javax.swing.JScrollPane searchCompScroll;
	private javax.swing.JTree homeTree;
	private javax.swing.JTree searchResultTree;
	private javax.swing.JButton searchBtn;
	private javax.swing.JButton clearBtn;
	private javax.swing.JButton openBtn;
	private javax.swing.JButton editBtn;
	private javax.swing.JButton advSearchBtn;
	private javax.swing.JButton advSearchResetBtn;
	private javax.swing.JButton cancelBtn;
	private javax.swing.JTextField searchTextField;
	private javax.swing.JPanel advSearchComponentContainer;
	private javax.swing.JComboBox<String> searchComboBox;
	private javax.swing.JTable propertyTable;

	private javax.swing.tree.DefaultMutableTreeNode homeTreeRootNode;
	private javax.swing.tree.DefaultMutableTreeNode advSearchTreeRootNode;
	private javax.swing.tree.DefaultMutableTreeNode homeNode;
	private javax.swing.tree.DefaultMutableTreeNode mailBoxNode;
	private javax.swing.tree.DefaultMutableTreeNode workListNode;
	private javax.swing.tree.DefaultMutableTreeNode newStuffNode;
	private javax.swing.tree.DefaultMutableTreeNode searchNode;
	private javax.swing.tree.DefaultMutableTreeNode advSearchNode;

	private Cursor busyCursor = new Cursor(Cursor.WAIT_CURSOR);
	private Cursor defCursor = new Cursor(Cursor.DEFAULT_CURSOR);

	public OpenDrawingDialog(OperationManagerFactory factory) {
		super(factory);
		// initComponents();

		tcObjectOperation = new TCObjectOperation();
		tcObjOper = new TCObjOperation();

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				try {
					out.setFuncID("OpenDrawing");
					out.setResult("false");
					out.setDesc("�û�ȡ�����롣");
					out.sendResultToUI();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				dispose();
			}

		});
	}

	@Override
	public void initComponents() {

		mainPanel = new javax.swing.JPanel();
		buttonPanel = new javax.swing.JPanel();
		openBtn = new javax.swing.JButton();
		editBtn = new javax.swing.JButton();
		cancelBtn = new javax.swing.JButton();
		contentPanel = new javax.swing.JPanel();
		jSplitPane1 = new javax.swing.JSplitPane();
		jSplitPane2 = new javax.swing.JSplitPane();
		propertyScroll = new javax.swing.JScrollPane();
		propertyTable = new javax.swing.JTable();
		jTabbedPane1 = new javax.swing.JTabbedPane();
		homeTreePanel = new javax.swing.JPanel();
		searchTextField = new javax.swing.JTextField();
		searchBtn = new javax.swing.JButton();
		clearBtn = new javax.swing.JButton();
		homeTreeScroll = new javax.swing.JScrollPane();
		homeTree = new javax.swing.JTree();
		advSearchPanel = new javax.swing.JPanel();
		advSearchInputPanel = new javax.swing.JPanel();
		searchComboBox = new javax.swing.JComboBox<String>();

		advSearchComponentContainer = new javax.swing.JPanel();
		searchCompScroll = new javax.swing.JScrollPane(); // ʵ�ֿؼ�̫��ʱ�Թ���������ʾ�Ĺ�����Ҫ��panel����scrollpanel�ڣ�panel����ӵĿؼ�
		searchCompScroll.setViewportView(advSearchComponentContainer);

		advSearchBtn = new javax.swing.JButton();
		advSearchResetBtn = new javax.swing.JButton();
		searchResultScroll = new javax.swing.JScrollPane();
		searchResultTree = new javax.swing.JTree();
		preViewPanel = new javax.swing.JPanel();

		advSearchComponentContainer.setBackground(new java.awt.Color(226, 245, 252));

		setTitle("��ͼֽ");
		setAlwaysOnTop(true);
		setBackground(new java.awt.Color(226, 245, 252));
		setForeground(new java.awt.Color(226, 245, 252));
		setName("openDrawingDialog");
		setResizable(true);

		mainPanel.setName("mainPanel");
		mainPanel.setBackground(new java.awt.Color(226, 245, 252));
		mainPanel.setForeground(new java.awt.Color(226, 245, 252));

		buttonPanel.setName("buttonPanel");
		buttonPanel.setBackground(new java.awt.Color(226, 245, 252));

		openBtn.setText("�鿴");
		openBtn.setName("okBtn");

		editBtn.setText("�༭");
		editBtn.setName("editBtn");

		cancelBtn.setText("ȡ��");
		cancelBtn.setName("cancelBtn");

		javax.swing.GroupLayout buttonPanelLayout = new javax.swing.GroupLayout(buttonPanel);
		buttonPanel.setLayout(buttonPanelLayout);
		buttonPanelLayout.setHorizontalGroup(buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
				buttonPanelLayout.createSequentialGroup().addContainerGap(281, Short.MAX_VALUE)
				// .addComponent(refresh)
				// .addGap(18, 18, 18)
						.addComponent(openBtn).addGap(18, 18, 18).addComponent(editBtn).addGap(18, 18, 18).addComponent(cancelBtn).addGap(41, 41, 41)));
		buttonPanelLayout.setVerticalGroup(buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				buttonPanelLayout.createSequentialGroup().addContainerGap().addGroup(buttonPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
				// .addComponent(refresh)
						.addComponent(openBtn).addComponent(editBtn).addComponent(cancelBtn)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		contentPanel.setName("contentPanel");

		jSplitPane1.setBorder(null);
		jSplitPane1.setBackground(new java.awt.Color(226, 245, 252));
		jSplitPane1.setDividerLocation(400);
		// jSplitPane1.setDividerSize(9);
		jSplitPane1.setName("jSplitPane1");
		jSplitPane1.setOneTouchExpandable(true);

		jSplitPane2.setBorder(null);
		jSplitPane2.setBackground(new java.awt.Color(226, 245, 252));
		jSplitPane2.setDividerLocation(241);
		jSplitPane2.setDividerSize(9);
		jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
		jSplitPane2.setName("jSplitPane2");
		jSplitPane2.setOneTouchExpandable(true);

		propertyScroll.setName("propertyScroll");
		propertyScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		propertyTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { "", "" } }, new String[] { "������", "����ֵ" }));
		propertyTable.setName("propertyTable");
		propertyTable.setRowHeight(20);
		propertyTable.setFillsViewportHeight(true);
		propertyTable.setEnabled(false);
		propertyScroll.setViewportView(propertyTable);

		preViewPanel.setName("preViewPanel");
		jSplitPane2.setTopComponent(preViewPanel);
		jSplitPane2.setBottomComponent(propertyScroll);
		// jSplitPane1.setRightComponent(jSplitPane2);
		jSplitPane1.setRightComponent(propertyScroll);

		jTabbedPane1.setName("jTabbedPane1");
		jTabbedPane1.setBackground(new java.awt.Color(204, 255, 255));
		jTabbedPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("ѡ��Ҫ�򿪵Ķ���"));

		homeTreePanel.setName("homeTreePanel");
		homeTreePanel.setBackground(new java.awt.Color(204, 255, 255));

		homeTreeScroll.setName("homeTreeScroll");

		homeTree.setName("HomeFolder");
		homeTreeScroll.setViewportView(homeTree);

		searchBtn.setIcon(IconManager.getIcon("search.png"));
		searchBtn.setToolTipText("���������������������");
		searchBtn.setName("searchBtn");

		clearBtn.setIcon(IconManager.getIcon("clear.png"));
		clearBtn.setToolTipText("�����������");
		clearBtn.setName("clearBtn");

		searchTextField.setText("����ItemId��ѯItem����......");
		searchTextField.setName("searchTextField");
		searchTextField.setFont(new java.awt.Font("����", 0, 14));

		javax.swing.GroupLayout homeTreePanelLayout = new javax.swing.GroupLayout(homeTreePanel);
		homeTreePanel.setLayout(homeTreePanelLayout);
		homeTreePanelLayout.setHorizontalGroup(homeTreePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				homeTreePanelLayout
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								homeTreePanelLayout
										.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(homeTreeScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
										.addGroup(
												homeTreePanelLayout.createSequentialGroup()
														.addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18)
														.addComponent(searchBtn).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(clearBtn))).addContainerGap()));
		homeTreePanelLayout.setVerticalGroup(homeTreePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				homeTreePanelLayout
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								homeTreePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(searchBtn)
										.addComponent(clearBtn)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(homeTreeScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 421, Short.MAX_VALUE).addContainerGap()));
		jTabbedPane1.addTab("�ҵ�Teamcenter", homeTreePanel);

		advSearchPanel.setName("advSearchPanel");
		advSearchPanel.setBackground(new java.awt.Color(204, 255, 255));

		searchCompScroll.setName("searchCompScroll");
		searchCompScroll.setViewportView(advSearchComponentContainer);
		searchCompScroll.setBackground(new java.awt.Color(226, 245, 252));
		// searchCompScroll.setBackground(new java.awt.Color(255,0,255));
		// searchCompScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		// searchCompScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		searchCompScroll.setBorder(createTitledBorder("��������"));

		/*
		 * searchNameLab.setText("ѡ�����е�����");
		 * searchNameLab.setName("searchNameLab");
		 */
		searchComboBox.setModel(new DefaultComboBoxModel<String>(getQueryInfo()));
		searchComboBox.setBackground(new java.awt.Color(226, 245, 252));
		searchComboBox.setName("searchComboBox");

		advSearchInputPanel.setName("advSearchInputPanel");
		advSearchInputPanel.setBackground(new java.awt.Color(204, 255, 255));

		advSearchBtn.setIcon(IconManager.getIcon("search.png"));
		advSearchBtn.setName("advSearchBtn");
		advSearchBtn.setText("����");

		advSearchResetBtn.setIcon(IconManager.getIcon("clear.png"));
		advSearchResetBtn.setName("advSearchResetBtn");
		advSearchResetBtn.setText("����");

		javax.swing.GroupLayout advSearchInputPanelLayout = new javax.swing.GroupLayout(advSearchInputPanel);
		advSearchInputPanel.setLayout(advSearchInputPanelLayout);
		advSearchInputPanelLayout.setHorizontalGroup(advSearchInputPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(advSearchInputPanelLayout.createSequentialGroup().addContainerGap().addComponent(searchComboBox, 0, 168, Short.MAX_VALUE).addContainerGap())
				.addComponent(searchCompScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 255, Short.MAX_VALUE)
				.addGroup(
						advSearchInputPanelLayout.createSequentialGroup().addContainerGap().addComponent(advSearchBtn).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(advSearchResetBtn).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		advSearchInputPanelLayout.setVerticalGroup(advSearchInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				advSearchInputPanelLayout.createSequentialGroup().addContainerGap()
						.addComponent(searchComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(searchCompScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(advSearchInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(advSearchBtn).addComponent(advSearchResetBtn))
						.addContainerGap(11, Short.MAX_VALUE)));

		searchResultScroll.setName("searchResultScroll");
		searchResultScroll.setViewportView(searchResultTree);

		javax.swing.GroupLayout advSearchPanelLayout = new javax.swing.GroupLayout(advSearchPanel);
		advSearchPanel.setLayout(advSearchPanelLayout);
		advSearchPanelLayout.setHorizontalGroup(advSearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				advSearchPanelLayout.createSequentialGroup()
						.addComponent(advSearchInputPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(searchResultScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)));
		advSearchPanelLayout.setVerticalGroup(advSearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(searchResultScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE)
				.addComponent(advSearchInputPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

		jTabbedPane1.addTab("�߼�����", advSearchPanel);

		jSplitPane1.setLeftComponent(jTabbedPane1);

		javax.swing.GroupLayout contentPanelLayout = new javax.swing.GroupLayout(contentPanel);
		contentPanel.setLayout(contentPanelLayout);
		contentPanelLayout.setHorizontalGroup(contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 610,
				Short.MAX_VALUE));
		contentPanelLayout.setVerticalGroup(contentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 507,
				Short.MAX_VALUE));

		javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
		mainPanel.setLayout(mainPanelLayout);
		mainPanelLayout.setHorizontalGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(buttonPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(contentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		mainPanelLayout.setVerticalGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				mainPanelLayout.createSequentialGroup().addComponent(contentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));

		org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(mainPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(mainPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

		pack();
	}

	@Override
	public void initConfig() {
		initAutoCADCfg();
	}

	@Override
	public void initListener() {
		// home���Ľڵ�ѡ����Ӧ�¼�
		/*
		 * homeTree.addTreeSelectionListener(new
		 * javax.swing.event.TreeSelectionListener() { public void
		 * valueChanged(javax.swing.event.TreeSelectionEvent event) {
		 * treeSelectionAction(); } });
		 */

		homeTree.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				int selRow = homeTree.getRowForLocation(e.getX(), e.getY());
				clickTreePath = homeTree.getPathForLocation(e.getX(), e.getY());
				isHomeTree = true;
				if (selRow != -1) {
					if (e.getClickCount() == 1) {
						treeNodeClickAction();
					} else if (e.getClickCount() == 2) {
						treeNodeDoubleClickAction();
					}
				}
			}
		});

		editBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				OKBtnAction(true);
			}
		});

		cancelBtn.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				btnCancelMouseClicked(evt);
			}
		});

		// �����������Ļس���Ӧ�¼�
		searchTextField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(java.awt.event.KeyEvent evt) {
				int key = evt.getKeyCode();
				if (KeyEvent.VK_ENTER == key) {
					simpleQueryAction();
				}
			}
		});

		openBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				OKBtnAction(false);
			}
		});

		// ��ռ������������
		clearBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				clearBtnActionPerformed(evt);
			}
		});

		// ��������ť����Ӧ�¼�
		searchBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				searchBtnActionPerformed(evt);
			}
		});

		// ------------------------�򿪺Ͳ�ѯ�ָ���---------------------------

		/*
		 * searchResultTree.addTreeSelectionListener(new
		 * javax.swing.event.TreeSelectionListener() { public void
		 * valueChanged(javax.swing.event.TreeSelectionEvent event) {
		 * treeSelectionAction(); } });
		 */

		searchResultTree.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				int selRow = searchResultTree.getRowForLocation(e.getX(), e.getY());
				clickTreePath = searchResultTree.getPathForLocation(e.getX(), e.getY());
				isHomeTree = false;
				if (selRow != -1) {
					if (e.getClickCount() == 1) {
						treeNodeClickAction();
					} else if (e.getClickCount() == 2) {
						treeNodeDoubleClickAction();
					}
				}
			}
		});

		advSearchBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				advSearchBtnActionPerformed(evt);
			}
		});

		// ���ø߼���������������
		advSearchResetBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				searchReset(advSearchComponentContainer);
			}
		});

		searchComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					String item = (String) event.getItem();
					// �������ı���¼�
					// getQueryComponentInfo(item,searchCompScroll);
					getQueryComponentInfo(item, advSearchComponentContainer);
				}
			}
		});
	}

	private void initAutoCADCfg() {
		String UFCROOT = System.getenv("UFCROOT");
		String fileSeparator = System.getProperty("file.separator");
		String configFilePath = UFCROOT + fileSeparator + "AutoCAD" + fileSeparator + "cfg" + fileSeparator + "uif.xml";
		parseConfig = new ParseConfigXMl(configFilePath);
		parseConfig.setInfos();
	}

	/**
	 * 
	 * TODO ��ʼ��Home��
	 * 
	 * @author lijj created on 2010-11-18����02:18:51
	 */
	private boolean initTree() {
		WSObject Home = null;
		WSObject newStuff = null;
		WSObject mailBox = null;
		List<WSObject> otherFolderList = new ArrayList<WSObject>();

		try {
			Home = tcObjOper.getHomeFolder();
			if (null == Home) {
				System.out.println("Home folder is null!");
				return false;
			}

			WSObject[] homeContents = tcObjOper.getContentsOfFolder(Home);
			for (int i = 0; i < homeContents.length; i++) {
				if (homeContents[i].getType().equals("Newstuff Folder")) {
					newStuff = homeContents[i];
				}

				if (homeContents[i].getType().equals("Mail Folder")) {
					mailBox = homeContents[i];
				}

				if (homeContents[i].getType().equals("Folder")) {
					otherFolderList.add(homeContents[i]);
				}
			}
		} catch (TCOperationException e1) {
			e1.printStackTrace();
		}

		homeNode = new javax.swing.tree.DefaultMutableTreeNode(Home);
		mailBoxNode = new javax.swing.tree.DefaultMutableTreeNode(mailBox);
		newStuffNode = new javax.swing.tree.DefaultMutableTreeNode(newStuff);

		homeTreeRootNode = new javax.swing.tree.DefaultMutableTreeNode("�򿪲鿴Teamcenter�е�ͼֽ");
		homeTreeDefmodel = new javax.swing.tree.DefaultTreeModel(homeTreeRootNode);

		homeTree.setModel(homeTreeDefmodel);
		// ���ڵ�Ϊ��ѡ
		homeTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

		homeTree.setCellRenderer(render);
		homeTree.setRootVisible(true);
		homeTree.setExpandsSelectedPaths(true);
		homeTree.setScrollsOnExpand(true);
		homeTree.setAutoscrolls(true);

		homeNode.add(mailBoxNode);
		homeNode.add(newStuffNode);
		// homeNode.add(workListNode);
		for (WSObject folder : otherFolderList) {
			DefaultMutableTreeNode folderNode = new javax.swing.tree.DefaultMutableTreeNode(folder);
			homeNode.add(folderNode);
		}

		homeTreeRootNode.add(homeNode);
		// ��ӡ������ڵ㡱
		searchNode = new javax.swing.tree.DefaultMutableTreeNode("�������");
		homeTreeRootNode.add(searchNode);

		// �߼���ѯ������ʼ��
		advSearchTreeRootNode = new javax.swing.tree.DefaultMutableTreeNode("�߼���ѯ");
		advSearchDefmodel = new javax.swing.tree.DefaultTreeModel(advSearchTreeRootNode);
		searchResultTree.setModel(advSearchDefmodel);
		searchResultTree.setCellRenderer(render);
		searchResultTree.setRootVisible(true);
		searchResultTree.setExpandsSelectedPaths(true);
		searchResultTree.setScrollsOnExpand(true);

		advSearchNode = new javax.swing.tree.DefaultMutableTreeNode("�������");
		advSearchTreeRootNode.add(advSearchNode);

	
		homeTree.expandPath(new TreePath(homeTreeRootNode));
		homeTree.expandPath(new TreePath(homeNode));
		homeTree.expandRow(1);
		this.pack();

		return true;
	}

	private void setTableColor() {
		TableColumnModel tcm = propertyTable.getColumnModel();
		for (int i = 0, n = tcm.getColumnCount(); i < n; i++) {
			TableColumn tc = tcm.getColumn(i);
			tc.setCellRenderer(new WorkspaceTableRender());
		}
	}

	private void treeNodeClickAction() {
		if (null != waitDlg)
			waitDlg.dispose();
		waitDlg = new WaitDialog(this, false);
		Thread thread = new Thread(new Runnable() {
			public void run() {
				setCursor(busyCursor);
				treeNodeClick();
				setCursor(defCursor);
				waitDlg.dispose();
			}
		});
		thread.start();
	}

	private void treeNodeClick() {
		/*
		 * TreePath selPath = homeTree.getSelectionPath(); if (null == selPath)
		 * return;
		 */
		DefaultMutableTreeNode openNode = (DefaultMutableTreeNode) clickTreePath.getLastPathComponent();
		showNodeObjProperty(openNode); // ��table���������
	}

	private void treeNodeDoubleClickAction() {
		if (null != waitDlg)
			waitDlg.dispose();
		waitDlg = new WaitDialog(this, false);
		Thread thread = new Thread(new Runnable() {
			public void run() {
				setCursor(busyCursor);
				treeNodeDoubleClick();
				setCursor(defCursor);
				waitDlg.dispose();
			}
		});
		thread.start();
	}

	/**
	 * 
	 * TODO home���Ľڵ�ѡ����Ӧ�¼�
	 * 
	 * @author lijj created on 2010-11-18����02:57:14
	 */
	private void treeNodeDoubleClick() {

		DefaultMutableTreeNode openNode = (DefaultMutableTreeNode) clickTreePath.getLastPathComponent();
		//showNodeObjProperty(openNode); //��table���������

		WSObject nodeObject = null;
		if (openNode.getUserObject() instanceof WSObject) {
			nodeObject = (WSObject) openNode.getUserObject();
		}

		if (nodeObject == null) {
			return;
		}

		String nodeType = nodeObject.getType();
		// ���ڵ���folder���ͻ���newstuff������mailbox������worklist
		if (nodeType.equals("Folder") || nodeType.equals("Mail Folder") || nodeType.equals("Newstuff Folder") || nodeType.equals("User_Inbox")) {
			openNode.removeAllChildren();
			try {
				WSObject[] children = tcObjectOperation.listContentsofFolder(nodeObject, true);
				if (children != null) {
					for (int i = 0; i < children.length; i++) {
						WSObject obj = children[i];
						String objType = obj.getType();
						// ���folderʱֻҪ��������folder �� �ȶ���3���� item,revision,ds��������ʾ
						if (objType.equals("Folder") || checkNodeObject(objType)) {
							javax.swing.tree.DefaultMutableTreeNode childnode = new javax.swing.tree.DefaultMutableTreeNode(obj);
							openNode.add(childnode);
						}
					}
				}
			} catch (Exception err) {
				err.printStackTrace();
			}
		} else if (isItem(nodeType)) {
			openNode.removeAllChildren();
			try {
				// ����
				tcObjOper.refreshObjects(new WSObject[] { nodeObject });
				WSObject[] children = tcObjOper.getAllItemRevision(nodeObject);// ��ӻ�ȡitem���������е�revision
				if (children != null) {
					for (int i = 0; i < children.length; i++) {
						WSObject obj = children[i];
						javax.swing.tree.DefaultMutableTreeNode childnode = new javax.swing.tree.DefaultMutableTreeNode(obj);
						openNode.add(childnode);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (isRevision(nodeType)) {
			try {
				openNode.removeAllChildren();
				tcObjOper.refreshObjects(new WSObject[] { nodeObject });
				List<String> dsTypeList = getObjectTypes("ds");
				List<WSObject> allChilds = new ArrayList<WSObject>();
				// ���������ļ���Ϣ����ȡÿһ��dstype�Ķ���relation��ds

				for (int i = 0; i < dsTypeList.size(); i++) {
					String[] dsTypes = new String[1];
					String dsType = dsTypeList.get(i);
					dsTypes[0] = dsType;
				
					String[] relations = getRelation(dsType, "ds").split(",");
					
					for (int j = 0; j < relations.length; j++) {
						WSObject[] children = tcObjOper.getDatasetOfItemRevision(nodeObject, dsTypes, relations[j]);

						if (null != children && children.length != 0) {
							for (int k = 0; k < children.length; k++) {
								WSObject child = children[k];
								allChilds.add(child);
							}
						}
					}
				}

				for (int i = 0; i < allChilds.size(); i++) {
					WSObject child = allChilds.get(i);
					tcObjOper.refreshObjects(new WSObject[] { child });
					DefaultMutableTreeNode childnode = new DefaultMutableTreeNode(child);
					openNode.add(childnode);
				}

			} catch (TCOperationException e2) {
				e2.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (openNode.getUserObject() instanceof String) {

		}

		if (isHomeTree) {
			homeTree.expandPath(clickTreePath);
		} else {
			searchResultTree.expandPath(clickTreePath);
		}
		homeTree.updateUI();
	}

	protected void checkoutDatasetFile(DefaultMutableTreeNode treeNode) {
		WSObject dataSetObject = (WSObject) treeNode.getUserObject();
		DefaultMutableTreeNode itemRevNode = (DefaultMutableTreeNode) treeNode.getParent();
		WSObject itemRev = (WSObject) itemRevNode.getUserObject();

		WSObject[] files = null;
		try {
			files = tcObjOper.getFilesOfDataset(dataSetObject);
		} catch (TCOperationException e) {
			e.printStackTrace();
		}

		try {
			String item_id = tcObjectOperation.getPropertyOfObject(itemRev, "item_id");
			String user_id = SessionManager.getUserID();
			String rev_id = tcObjectOperation.getPropertyOfObject(itemRev, "item_revision_id");
			String tempPath = user_id + "_" + item_id + "_" + rev_id;
			newfileName = item_id;

			// Util.setRev_id(rev_id);
			datasetname = tcObjectOperation.getPropertyOfObject(itemRev, "object_name");

			// �ж��û��Ƿ���Ȩ��ǩ�� --------------------
			if (!tcObjectOperation.checkPrivilege(dataSetObject, "WRITE")) {
				JOptionPane.showMessageDialog(this, "���Ըö���û��д��Ȩ�ޣ����ܽ��б༭������", "��ʾ", JOptionPane.ERROR_MESSAGE);
				return;
			}

			filePathList.clear(); // �����������ļ�·����list���

			if (!tcObjectOperation.isCheckOut(dataSetObject)) {// ִ�м������
				// �����أ��ټ��
				if (!downloadFilesOfDataset(dataSetObject.getType(), files, tempPath)) {
					return;
				}
				if (!tcObjectOperation.checkOutObjects(new WSObject[] { dataSetObject }, "")) {
					// ɾ��֮ǰ���ļ�
					downFile.delete();
					sendMessageForUser("�������ʱ�������������²���");
					return;
				}
			} else {
				if (!tcObjectOperation.isCheckOutByUser(dataSetObject)) {
					int ok = JOptionPane.showConfirmDialog(this, "���޷�ǩ���ö��󣬸ö����ѱ������û�ǩ�����Ƿ������������ȷ�������ظ�ͼֽ�ļ��������������ȡ�����������²���", "��ʾ", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (ok == JOptionPane.CANCEL_OPTION)
						return;
					if (!downloadFilesOfDataset(dataSetObject.getType(), files, tempPath)) {
						return;
					}
				} else {
					// ������UFCROOT�µ�Temp�ļ�����
					if (!downloadFilesOfDataset(dataSetObject.getType(), files, tempPath)) {
						return;
					}
				}
			}
		} catch (TCOperationException e) {
			e.printStackTrace();
			sendMessageForUser("�򿪲���ʧ�ܣ������²���");
			return;
		}
	}

	private void sendMessageForUser(String message) {
		if (null == message || "".equals(message))
			JOptionPane.showMessageDialog(this, "��ͼֽ����ʧ�ܣ���������������ԭ���Teamcenter����ԭ������ϵ����Ա��", "��ʾ", JOptionPane.ERROR_MESSAGE);
		else
			JOptionPane.showMessageDialog(this, message, "��ʾ", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * 
	 * TODO
	 * 
	 * @return boolean ����˵�� file-DS�������õ��ļ�����savePath-�������ļ���ȡ�ĸ����͵�ds�Ĵ��λ�ã�
	 *         originalName-DS�������õ�ԭʼ�ļ�����tempPath-��user_id + "_" + item_id + "_"
	 *         + rev_idƴ�ɵ��ļ�����
	 * @author lijj created on 2010-11-30����04:30:32
	 */

	private boolean downloadFileOfDataset(WSObject file, String savePath, String originalName, String tempPath) {
		TCFileManagement fm = new TCFileManagement();
		String tempFilePath = "";
		if (null == file) {
			sendMessageForUser("��ȡ�ļ�ʱ��������������ѡ��");
			return false;
		}
		File[] filesOfDS = fm.downloadFile(new WSObject[] { file });

		String dsFileSavePath = System.getenv("UFCROOT") + "\\Temp\\";
		if (savePath.length() > 0) {
			dsFileSavePath = savePath;
		}
		String dir = "";
		String dirPath = "";
		if (null == tempPath || "".equals(tempPath.trim())) {
			dir = dsFileSavePath + originalName;
			dirPath = dsFileSavePath;
		} else {
			dir = dsFileSavePath + tempPath + "\\" + originalName;
			dirPath = dsFileSavePath + tempPath;
		}
		File fileDir = new File(dirPath);
		if (!fileDir.exists()) {
			fileDir.mkdir();
		}
		try {

			byte[] b = new byte[500 * 1024];
			downFile = new File(dir);
			if (!downFile.exists()) {
				downFile.createNewFile();
			}

			/*
			 * File oldNameFile = new File(downFile.getParent() + "\\" +
			 * newfileName //ֵΪitemId + "_" +
			 * datasetname.substring(datasetname.length() -
			 * 1,datasetname.length()) //ֵΪ���ݼ����һλ���汾�� + "_" +
			 * originalName//ԭʼ���� + ".dwg");
			 * 
			 * if (oldNameFile.exists()) { oldNameFile.delete(); }
			 */

			FileInputStream in = new FileInputStream(filesOfDS[0]);
			FileOutputStream out = new FileOutputStream(downFile);
			BufferedInputStream buffInput = new BufferedInputStream(in);
			BufferedOutputStream buffOutput = new BufferedOutputStream(out);
			int cnt = buffInput.read(b);
			while (cnt != -1) {
				buffOutput.write(b, 0, cnt);
				cnt = buffInput.read(b);
			}
			buffOutput.flush();
			out.close();
			buffOutput.close();
			in.close();
			buffInput.close();

			System.out.println("path:" + downFile.getAbsolutePath());

			tempFilePath = downFile.getAbsolutePath();
			filePathList.add(tempFilePath);// ��ĳ��ds�е��ļ�����·������list

		} catch (IOException e) {
			sendMessageForUser("�����ļ�ʱ�������󣬴���ԭ�����£�\n" + e.getMessage());
			e.printStackTrace();
			return false;
		}

		return true;
	}

	private boolean downloadFilesOfDataset(String dsType, WSObject[] files, String tempPath) {
		String originalName = "";
		WSObject file = null;

		Map<String, Map<String, String>> dsInfo = parseConfig.attachInfos.get("ds");
		Map<String, String> tempMap = dsInfo.get(dsType);
		System.out.println(tempMap);
		String[] extNames = tempMap.get("ext_name").split(",");
		String savePath = tempMap.get("local_path");
		if (null == savePath) {
			savePath = "";
		} else {
			File tempFile = new File(savePath);
			if (!tempFile.isDirectory()) {
				savePath = "";
				// ---------------------------------
			}
		}

		Map<String, Object> fileInfo = new HashMap<String, Object>();
		System.out.println(files.length);
		for (int i = 0; i < files.length; i++) {
			String name = null;
			try {
				name = tcObjectOperation.getPropertyOfObject(files[i], "original_file_name");
			} catch (TCOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (int j = 0; j < extNames.length; j++) {
				if (null != name && name.toLowerCase().endsWith(extNames[j].toLowerCase())) {
					fileInfo.put(name, files[i]);
				}
			}
		}

		Iterator<?> fileIter = fileInfo.entrySet().iterator();
		while (fileIter.hasNext()) {
			Map.Entry entry = (Map.Entry) fileIter.next();
			originalName = (String) entry.getKey();
			file = (WSObject) entry.getValue();

			if (!downloadFileOfDataset(file, savePath, originalName, tempPath)) {
				return false;
			}
		}

		return true;
	}

	private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_btnCancelMouseClicked
		closeWindow();
	}

	public void closeWindow() {
		try {
			out.setFuncID("OpenDrawing");
			out.setResult("false");
			out.setDesc("�û�ȡ��������");
			out.sendResultToUI();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		dispose();
	}

	protected void simpleQueryAction() {
		if (null != waitDlg)
			waitDlg.dispose();
		waitDlg = new WaitDialog(this, false);
		Thread thread = new Thread(new Runnable() {
			public void run() {
				waitDlg.setMessage("���ڽ��в��������Ժ򡭡�");
				setCursor(busyCursor);
				simpleQuery();
				setCursor(defCursor);
				waitDlg.dispose();
			}
		});
		thread.start();
	}

	/**
	 * 
	 * TODO �򵥲�ѯ
	 * 
	 * @author lijj created on 2010-11-18����02:46:59
	 */
	private void simpleQuery() {
		// ����ЩItem����һ��TreeNode�ϣ��ٽ���TreeNode���ء�����������ڵ���
		// ���ԭ�еĽڵ�
		searchNode.removeAllChildren();
		homeTree.updateUI();
		String queryStr = searchTextField.getText().trim();
		if (null == queryStr || "".equals(queryStr.trim())) {
			sendMessageForUser("��ѯ��������Ϊ�գ�");
			return;
		}
		try {
			WSObject[] foundObjs = tcObjectOperation.findObjectsBySavedQuery("__Query_EdrDoc", new String[] { queryStr });
			if (null == foundObjs) {
				searchNode.add(new DefaultMutableTreeNode("û���ҵ����������Ķ���"));
			} else {
				for (WSObject obj : foundObjs) {
					searchNode.add(new DefaultMutableTreeNode(obj));
				}
			}
		} catch (TCOperationException e) {
			sendMessageForUser(e.getMessage());
			return;
		}
		// �۵�Home�ڵ�
		homeTree.collapseRow(1);
		// չ�������ڵ�
		homeTree.expandRow(2);
		homeTree.updateUI();

	}

	private boolean checkNodeValidity(DefaultMutableTreeNode treeNode) {

		WSObject dataSetObject = (WSObject) treeNode.getUserObject();
		String dsName = dataSetObject.getName();
		if (!isDs(dataSetObject.getType())) {
			sendMessageForUser("ֻ�ܶ�Dataset���͵Ķ�����в�����" + dsName + "�������ݼ���");
			return false;
		}

		DefaultMutableTreeNode itemRevNode = (DefaultMutableTreeNode) treeNode.getParent();
		WSObject itemRev = (WSObject) itemRevNode.getUserObject();
		if (!isRevision(itemRev.getType())) {
			sendMessageForUser(dsName + "����Ч���ݼ���ֻ�ܴ�ItemRevision�¹��ص�Dataset��");
			return false;
		}

		WSObject[] files = null;
		try {
			files = tcObjOper.getFilesOfDataset(dataSetObject);
			if (null == files || files.length == 0) {
				sendMessageForUser("�����ļ�ʧ�ܣ���Dataset��" + dsName + "���¿���û�й���ͼֽ�ļ�");
				return false;
			}
			if (!tcObjectOperation.checkPrivilege(dataSetObject, "READ")) {
				JOptionPane.showMessageDialog(this, "���Ըö���" + dsName + "��û�ж�Ȩ�ޣ����ܽ������ز�����", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
		} catch (TCOperationException e) {
			e.printStackTrace();
		}
		return true;
	}

	private void openBtnDownActionPerformed(DefaultMutableTreeNode treeNode) {
		// �ж��Ƿ�Ϊ���ݼ�

		WSObject dataSetObject = (WSObject) treeNode.getUserObject();
		WSObject[] files = null;
		try {
			files = tcObjOper.getFilesOfDataset(dataSetObject);
		} catch (TCOperationException e) {
			e.printStackTrace();
		}
		DefaultMutableTreeNode itemRevNode = (DefaultMutableTreeNode) treeNode.getParent();
		WSObject itemRev = (WSObject) itemRevNode.getUserObject();

		String tempPath = null;
		try {
			String item_id = tcObjectOperation.getPropertyOfObject(itemRev, "item_id");
			String user_id = SessionManager.getUserID();
			String rev_id = tcObjectOperation.getPropertyOfObject(itemRev, "item_revision_id");
			tempPath = user_id + "_" + item_id + "_" + rev_id;

			// ���ô�ͼֽ�İ汾id
			// Util.setRev_id(rev_id);
			datasetname = tcObjectOperation.getPropertyOfObject(dataSetObject, "object_name");
			newfileName = item_id;
			datasetname = tcObjectOperation.getPropertyOfObject(dataSetObject, "object_name");

		} catch (TCOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		filePathList.clear(); // �����������ļ�·����list���
		// �����ļ�
		if (!downloadFilesOfDataset(dataSetObject.getType(), files, tempPath)) {
			return;
		}

	}

	protected void clearBtnActionPerformed(ActionEvent evt) {
		searchTextField.setText("");
	}

	protected void searchBtnActionPerformed(ActionEvent evt) {
		simpleQueryAction();
	}

	private void advSearchBtnActionPerformed(ActionEvent evt) {
		advQueryAction(evt);
	}

	private void advQueryAction(ActionEvent evt) {
		if (null != waitDlg)
			waitDlg.dispose();
		waitDlg = new WaitDialog(this, false);
		waitDlg.setMessage("���ڽ��в�ѯ�����Ժ򡭡�");
		Thread thread = new Thread(new Runnable() {
			public void run() {
				advSearchBtn.setEnabled(false);
				clearBtn.setEnabled(false);
				setCursor(busyCursor);
				if (advQuery()) {
					// dispose();
				}
				waitDlg.dispose();
				setCursor(defCursor);
				advSearchBtn.setEnabled(true);
				clearBtn.setEnabled(true);
			}
		});
		thread.start();
	}

	private boolean advQuery() {
		String queryName = searchComboBox.getSelectedItem().toString();
		// �ռ�ҳ���ϵ���Ϣ�����attributId=attributeValue�ļ�ֵ��
		Map<String, String> infos = new HashMap<String, String>();
		getSearchInfo(advSearchComponentContainer, infos);
		try {
			advSearchObjects = tcObjOper.queryObjectsBySavedQuery(queryName, infos);
			advSearchResultShow();
			return true;
		} catch (TCOperationException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "��ʾ", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	private void advSearchResultShow() {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				if (waitDlg != null)
					waitDlg.dispose();

				waitDlg = new WaitDialog();
				waitDlg.setMessage("���ڻ�ȡ������Ϣ�����Ժ�...");
				setCursor(busyCursor);
				advSearchNode.removeAllChildren();
				if (null == advSearchObjects || advSearchObjects.length == 0) {
					advSearchNode.setUserObject("������� �ҵ� 0 ������");
					advSearchNode.add(new DefaultMutableTreeNode("û���ҵ����������Ķ���"));
				} else {
					advSearchNode.setUserObject("������� �ҵ� " + advSearchObjects.length + " ������");
					for (WSObject obj : advSearchObjects) {
						advSearchNode.add(new DefaultMutableTreeNode(obj));
					}
				}
				setCursor(defCursor);
				waitDlg.dispose();

				// չ�������ڵ�
				TreePath advSearchNodePath = new TreePath(new Object[] { advSearchTreeRootNode, advSearchNode });
				searchResultTree.expandPath(advSearchNodePath);
				searchResultTree.scrollPathToVisible(advSearchNodePath);
				searchResultTree.setSelectionPath(advSearchNodePath);
				searchResultTree.updateUI();
			}
		});
		thread.start();
	}

	/**
	 * ��ȡָ��Panel��������е�ֵ������ȡ���Ĳ�ѯ��Ϣ����map�У�keyΪ�ؼ�����valueΪ�ؼ�ֵ
	 * 
	 * @author Liugz
	 * @create on 2009-3-6 This project for tc_communication.teamcenter2007
	 */
	private void getSearchInfo(JComponent comp, Map<String, String> infos) {
		Component[] comps = comp.getComponents();

		for (int i = 0; i < comps.length; i++) {
			Component compon = comps[i];
			if (compon instanceof JTextComponent) {
				JTextComponent txtComp = (JTextComponent) compon;
				infos.put(compon.getName(), txtComp.getText().trim());
			} else if (compon instanceof JDatePicker) {
				SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				try {
					Date date = ((JDatePicker) compon).getSelectedDate();
					infos.put(compon.getName(), dateFmt.format(date));
				} catch (ParseException e2) {

				}
			} else if (compon instanceof JComboBox) {
				Object seleObj = ((JComboBox) compon).getSelectedItem();
				infos.put(compon.getName(), seleObj.toString());
			}
		}
	}

	/**
	 * 
	 * TODO �������ļ���ȡϵͳ���Ѵ��ڵ��������������������������ؿؼ����������
	 * 
	 * @return void
	 * @author lijj created on 2010-12-9����11:43:40
	 */

	private void getQueryComponentInfo(String queryName, JComponent attribPanel) {
		if (null == queryName || queryName.trim().length() == 0) {
			attribPanel.removeAll();
			return;
		}
		attribPanel.removeAll();
		attribPanel.setLayout(null); // setBounds�����Ͳ��ֹ�������ͻ�����Ĭ����BorderLayout���֡�

		Map<String, Component> queryComps = new HashMap<String, Component>();
		List<String> queryEntryNames = null;
		try {
			queryEntryNames = TCUtil.getQueryEntryNames(queryName);
			queryComps = TCUtil.getSavedQueryComponents(queryName);
			// ��ȡ��ѯ�������û���Ŀ��Ϣ
		} catch (TCOperationException e1) {
			attribPanel.setLayout(new BorderLayout());
			attribPanel.add(new JLabel(e1.getMessage(), SwingConstants.CENTER), BorderLayout.CENTER);
			return;
		}

		for (int i = 0; i < queryEntryNames.size(); i++) {
			String entryName = queryEntryNames.get(i);
			Component comp = queryComps.get(entryName);

			JLabel tempLabel = new JLabel(entryName + ":");
			tempLabel.setBounds(new Rectangle(5, (27 * i), 98, 22)); // ��û�в��ֹ�����ʱ����Ч
			attribPanel.add(tempLabel);

			comp.setBounds(new Rectangle(110, (27 * i), 125, 22));
			attribPanel.add(comp);

			if (attribPanel.HEIGHT < (2 + (27 * i) + 22)) {
				attribPanel.setSize(attribPanel.WIDTH, attribPanel.HEIGHT + 22 * 2);
			}
		}
		attribPanel.updateUI();
		searchCompScroll.updateUI();
	}

	/**
	 * ��ӿؼ����������
	 * 
	 * @param filename
	 * @param queryPanel
	 */
	/*
	 * private void initqueryPanel(String filename, ){
	 * 
	 * queryPanel.removeAll();
	 * 
	 * ArrayList<SaveModel> allComponents =
	 * ParseRequestXML.parseQuerycondition(filename); //���������ļ����ɿؼ�
	 * if(partType.equals
	 * ("Cetc20Comp")||partType.equals("Cetc20GPart")||partType
	 * .equals("Cetc20Stuff")){ for(int i = 0; i < allComponents.size(); i++) {
	 * JLabel label = new JLabel(); JComponent jcom = null; SaveModel sc =
	 * allComponents.get(i); label.setText(sc.getDisplayName() + "��");
	 * if(sc.getType().equals("TEXT")) { jcom = new UFCTextField();
	 * ((UFCTextField)jcom).setDisplayName(sc.getDisplayName());
	 * ((UFCTextField)jcom).setProperty(sc.getProprety());
	 * ((UFCTextField)jcom).setRequired(sc.isRequired());
	 * ((UFCTextField)jcom).setCadName(sc.getCadName()); } else
	 * if(sc.getType().equals("LOV")) { jcom = new UFCComboBox();
	 * 
	 * ((UFCComboBox)jcom).setDisplayName(sc.getDisplayName());
	 * ((UFCComboBox)jcom).setProperty(sc.getProprety());
	 * ((UFCComboBox)jcom).setRequired(sc.isRequired());
	 * ((UFCComboBox)jcom).setCadName(sc.getCadName());
	 * 
	 * for(int x = 0; x < sc.getLovlist().size(); x++) {
	 * ((UFCComboBox)jcom).addItem(sc.getLovlist().get(x)); } }else
	 * if(sc.getType().equals("TEXTArea")){
	 * 
	 * jcom=new UFCTextArea();
	 * 
	 * ((UFCTextArea)jcom).setDisplayName(sc.getDisplayName());
	 * ((UFCTextArea)jcom).setProperty(sc.getProprety());
	 * ((UFCTextArea)jcom).setRequired(sc.isRequired());
	 * ((UFCTextArea)jcom).setCadName(sc.getCadName()); //jcom.isOpaque(); }
	 * 
	 * label.setBounds(new Rectangle(10, (i+16)+(i*23), 90, 22));
	 * jcom.setBounds(new Rectangle(100, (i+16)+(i*23), 100, 22));
	 * queryPanel.add(label); queryPanel.add(jcom); } }else {
	 * 
	 * for(int i = 0; i < allComponents.size(); i++) { JLabel label = new
	 * JLabel(); JComponent jcom = null;
	 * 
	 * SaveModel sc = allComponents.get(i);
	 * 
	 * label.setText(sc.getDisplayName() + "��");
	 * 
	 * if(sc.getType().equals("TEXT")) { jcom = new UFCTextField();
	 * 
	 * ((UFCTextField)jcom).setDisplayName(sc.getDisplayName());
	 * ((UFCTextField)jcom).setProperty(sc.getProprety());
	 * ((UFCTextField)jcom).setRequired(sc.isRequired());
	 * ((UFCTextField)jcom).setCadName(sc.getCadName());
	 * 
	 * } else if(sc.getType().equals("LOV")) { jcom = new UFCComboBox();
	 * 
	 * ((UFCComboBox)jcom).setDisplayName(sc.getDisplayName());
	 * ((UFCComboBox)jcom).setProperty(sc.getProprety());
	 * ((UFCComboBox)jcom).setRequired(sc.isRequired());
	 * ((UFCComboBox)jcom).setCadName(sc.getCadName());
	 * 
	 * for(int x = 0; x < sc.getLovlist().size(); x++) {
	 * ((UFCComboBox)jcom).addItem(sc.getLovlist().get(x)); } }else
	 * if(sc.getType().equals("TEXTArea")){
	 * 
	 * jcom=new UFCTextArea();
	 * 
	 * ((UFCTextArea)jcom).setDisplayName(sc.getDisplayName());
	 * ((UFCTextArea)jcom).setProperty(sc.getProprety());
	 * ((UFCTextArea)jcom).setRequired(sc.isRequired());
	 * ((UFCTextArea)jcom).setCadName(sc.getCadName()); //jcom.isOpaque(); }
	 * //if(i%2==0){
	 * 
	 * label.setBounds(new Rectangle(10, (i+20)+(i*30), 90, 22));
	 * jcom.setBounds(new Rectangle(100, (i+20)+(i*30), 100, 22)); }else{
	 * label.setBounds(new Rectangle(10, (i+40)+(i*15), 90, 22));
	 * jcom.setBounds(new Rectangle(100, (i+40)+(i*15), 100, 22)); }
	 * queryPanel.add(label); queryPanel.add(jcom); } } queryPanel.updateUI(); }
	 */

	/**
	 * ����ѯ�����е�ֵ����Ϊ��
	 * 
	 * @author Liugz
	 * @create on 2009-8-14 This project for CAD_Concrete
	 */
	private void searchReset(JComponent panel) {
		Component[] comps = panel.getComponents();
		if (null == comps || comps.length == 0) {
			return;
		}
		for (Component comp : comps) {
			if (comp instanceof JTextComponent) {
				((JTextComponent) comp).setText("");
			}
			if (comp instanceof JComboBox) {
				((JComboBox) comp).setSelectedItem(" ");
			}
		}

	}

	public void doOperation(Element requestBody, AdaptorWriter out) throws BusiOperationException {
		this.requestBody = requestBody;
		this.out = out;

		// --------------------------------

		if (null == homeTreeRootNode) {
			initTree();
		}

		setTableColor();
		// ----------------------------------

		Thread initThread = new Thread(new Runnable() {
			public void run() {

				// ��ʾ�Ի���
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
		});
		initThread.start();
	}

	private void showNodeObjProperty(DefaultMutableTreeNode openNode) {
		String[][] propertyValues = getObjectProperties(openNode);
		String[] titleValues = new String[] { "������", "����ֵ" };
		propertyTable.setModel(new javax.swing.table.DefaultTableModel(propertyValues, titleValues));
		setTableColor();
	}

	private String[][] getObjectProperties(DefaultMutableTreeNode openNode) {
		WSObject WSOb = null;
		if (openNode.getUserObject() instanceof WSObject) {
			WSOb = (WSObject) openNode.getUserObject();
		}

		if (WSOb == null) {
			return new String[][] {};
		}
		String objectType = WSOb.getType();

		// �����folder������,newstuff,�����б�����ؿյĶ�ά����
		if (objectType.equals("Folder")) {
			return new String[][] { { "", "" }, { "", "" } };
		}
		if (objectType.equals("Mail Folder")) {
			return new String[][] { { "", "" }, { "", "" } };
		}
		if (objectType.equals("Newstuff Folder")) {
			return new String[][] { { "", "" }, { "", "" } };
		}
		if (objectType.equals("User_Inbox")) {
			return new String[][] { { "", "" }, { "", "" } };
		}
		// -------------------------------------------

		String[][] propertyInfo = null;
		List<Map<String, String>> propertyInfoList = null;
		int rowCount = 0;
		int initRowCount = 0;

		if (isItem(objectType)) {
			propertyInfoList = parseConfig.infoToShow.get("item").get(objectType);
			initRowCount = 3;
			rowCount = propertyInfoList.size() + initRowCount;
			propertyInfo = new String[rowCount][2];
			try {
				propertyInfo[0][0] = "�����ID";
				propertyInfo[0][1] = tcObjectOperation.getPropertyOfObject(WSOb, "item_id");
				propertyInfo[1][0] = "����";
				propertyInfo[1][1] = tcObjectOperation.getPropertyOfObject(WSOb, "object_name");
				propertyInfo[2][0] = "������";
				propertyInfo[2][1] = tcObjectOperation.getPropertyOfObject(WSOb, "owning_user");
			} catch (TCOperationException e) {
				e.printStackTrace();
			}

		}

		if (isRevision(objectType)) {
			propertyInfoList = parseConfig.infoToShow.get("revision").get(objectType);
			initRowCount = 5;
			rowCount = propertyInfoList.size() + initRowCount;
			propertyInfo = new String[rowCount][2];

			try {
				propertyInfo[0][0] = "�����ID";
				propertyInfo[0][1] = tcObjectOperation.getPropertyOfObject(WSOb, "item_id");
				propertyInfo[1][0] = "����";
				propertyInfo[1][1] = tcObjectOperation.getPropertyOfObject(WSOb, "object_name");
				propertyInfo[2][0] = "������";
				propertyInfo[2][1] = tcObjectOperation.getPropertyOfObject(WSOb, "owning_user");
				propertyInfo[3][0] = "�汾";
				propertyInfo[3][1] = tcObjectOperation.getPropertyOfObject(WSOb, "item_revision_id");
				propertyInfo[4][0] = "������ʱ��";
				propertyInfo[4][1] = tcObjectOperation.getPropertyOfObject(WSOb, "last_mod_date");

			} catch (TCOperationException e) {
				e.printStackTrace();
			}

		}

		if (isDs(objectType)) {
			propertyInfoList = parseConfig.infoToShow.get("ds").get(objectType);
			initRowCount = 5;
			rowCount = propertyInfoList.size() + initRowCount;
			propertyInfo = new String[rowCount][2];

			try {
				/*
				 * propertyInfo[0][0] = "current_name"; propertyInfo[0][1] =
				 * tcObjectOperation.getPropertyOfObject(WSOb, "current_name");
				 */
				propertyInfo[0][0] = "����";
				propertyInfo[0][1] = tcObjectOperation.getPropertyOfObject(WSOb, "object_name");
				propertyInfo[1][0] = "������";
				propertyInfo[1][1] = tcObjectOperation.getPropertyOfObject(WSOb, "owning_user");
				propertyInfo[2][0] = "������ʱ��";
				propertyInfo[2][1] = tcObjectOperation.getPropertyOfObject(WSOb, "last_mod_date");
				propertyInfo[3][0] = "�Ƿ�ǩ��";
				propertyInfo[3][1] = tcObjectOperation.getPropertyOfObject(WSOb, "checked_out");
				propertyInfo[4][0] = "ǩ����";
				propertyInfo[4][1] = tcObjectOperation.getPropertyOfObject(WSOb, "checked_out_user");

			} catch (TCOperationException e) {
				e.printStackTrace();
			}
		}

		for (int i = 0; i < propertyInfoList.size(); i++) {
			Map<String, String> tempMap = propertyInfoList.get(i);
			String pdmName = "";
			WSObject sourceObj = null;
			for (Iterator<String> keyItor = tempMap.keySet().iterator(); keyItor.hasNext();) {
				String key = keyItor.next();
				if (key.equals("display_name")) {
					propertyInfo[i + initRowCount][0] = tempMap.get(key);
				}

				if (key.equals("pdm_name")) {
					pdmName = tempMap.get(key);
				}

				if (key.equals("source")) {
					sourceObj = getWsObjectBySource(WSOb, tempMap.get(key));
				}
			}
			propertyInfo[i + initRowCount][1] = tcObjOper.getPropertyValueOfObject(sourceObj, pdmName);
		}
		return propertyInfo;
	}

	/**
	 * 
	 * TODO ȡ��
	 * 
	 * @return List<String> ������objectKey����ȷ���������б�����item ȷ�� part��Ccmcpart
	 * @author lijj created on 2010-11-30����11:30:32
	 */
	private List<String> getObjectTypes(String objectKey) {
		List<String> resultList = new ArrayList<String>();
		Map<String, List<Map<String, String>>> tempMap = parseConfig.infoToShow.get(objectKey);
		for (Iterator<String> it = tempMap.keySet().iterator(); it.hasNext();) {
			String key = it.next();
			resultList.add(key);
		}
		return resultList;
	}

	/**
	 * 
	 * TODO �ɴ����ϸ����item��Ccmcpartȷ��Ccmcpart�롰�������Ĺ�ϵ
	 * 
	 * @return String
	 * @author lijj created on 2010-11-30����11:33:06
	 */
	private String getRelation(String objectType, String objectKey) {
		Map<String, Map<String, String>> tempMap = parseConfig.attachInfos.get(objectKey);
		String resultStr = tempMap.get(objectType).get("relation");
		return resultStr;
	}

	private boolean checkNodeObject(String objType) {
		if (isItem(objType) || isRevision(objType) || isDs(objType)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * TODO ��ϸ������������б�
	 * 
	 * @return boolean
	 * @author lijj created on 2010-11-30����01:16:41
	 */
	private boolean isItem(String objType) {
		List<String> itemTypeList = getObjectTypes("item");
		if (itemTypeList.contains(objType)) {
			return true;
		}
		return false;
	}

	private boolean isRevision(String objType) {
		List<String> revisionTypeList = getObjectTypes("revision");
		if (revisionTypeList.contains(objType)) {
			return true;
		}
		return false;
	}

	private boolean isDs(String objType) {
		List<String> dsTypeList = getObjectTypes("ds");
		if (dsTypeList.contains(objType)) {
			return true;
		}
		return false;
	}

	private String[] getQueryInfo() {
		String[] resultStrArray = new String[parseConfig.queryInfos.size() + 1];
		for (int i = 0; i < parseConfig.queryInfos.size(); i++) {
			resultStrArray[i + 1] = parseConfig.queryInfos.get(i).get("name");
		}
		resultStrArray[0] = "";
		return resultStrArray;
	}

	private WSObject getWsObjectBySource(WSObject nodeObject, String Source) {
		String nodeObjectType = nodeObject.getType();
		if (isItem(nodeObjectType)) {
			if (Source.equals("ITEM")) {
				return nodeObject;
			}
			if (Source.equals("IR")) {
				try {
					return tcObjOper.getItemRevision(nodeObject, "last");// ȡ���µ�itemRevision
				} catch (TCOperationException e) {
					e.printStackTrace();
				}
			}
			if (Source.equals("IMF")) {
				try {
					return tcObjectOperation.getMasterForm(nodeObject, null); // ------
																				// ��ȷ��
				} catch (TCOperationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (Source.equals("IRMF")) {
				WSObject itemRev;
				try {
					itemRev = tcObjOper.getItemRevision(nodeObject, "last");// ȡ���µ�itemRevision
					return tcObjOper.getRevMasterForm(itemRev);
				} catch (TCOperationException e) {
					e.printStackTrace();
				}
			}
		}

		if (isRevision(nodeObjectType)) {
			if (Source.equals("IR")) {
				return nodeObject;
			}
			if (Source.equals("IRMF")) {
				try {
					return tcObjOper.getRevMasterForm(nodeObject);
				} catch (TCOperationException e) {
					e.printStackTrace();
				}
			}
		}

		/*
		 * if (isDs(nodeObjectType)){ return nodeObject; }
		 */
		return nodeObject;
	}

	private void sendMessageToTool() {
		try {
			Dom4jUtil util = Dom4jUtil.getDom4jUtil();
			out.setFuncID("OpenDrawing");
			out.setResult("true");
			out.setDesc("�ɹ���ͼֽ");
			Element objEle = out.getObjectElement();
			Element docEle = objEle.addElement("docs").addElement("doc");
			util.setProValue(docEle, "LocalPath", filePath);
			out.sendResultToUI();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		dispose();
	}

	private boolean checkChosedTreeNode() {
		boolean checked = true;
		TreePath[] treePaths = null;
		if (isHomeTree) {
			treePaths = homeTree.getSelectionPaths();
		} else {
			treePaths = searchResultTree.getSelectionPaths();
		}
		for (int i = 0; i < treePaths.length; i++) {
			DefaultMutableTreeNode openNode = (DefaultMutableTreeNode) treePaths[i].getLastPathComponent();
			if (!checkNodeValidity(openNode)) {
				checked = false;
				break;
			}
		}
		return checked;
	}

	private void OKBtnAction(boolean isCheckOut) {
		boolean checked = true;
		TreePath[] treePaths = null;
		if (isHomeTree) {
			treePaths = homeTree.getSelectionPaths();
		} else {
			treePaths = searchResultTree.getSelectionPaths();
		}
		for (int i = 0; i < treePaths.length; i++) {
			DefaultMutableTreeNode tempTreeNode = (DefaultMutableTreeNode) treePaths[i].getLastPathComponent();
			if (!checkNodeValidity(tempTreeNode)) {
				checked = false;
				break;
			}
		}
		// ֻ��ѡ���ȫ�����ݼ���ͨ����� �ſ�ʼ�������ش�
		if (checked) {
			setCursor(busyCursor);

			Dom4jUtil util = Dom4jUtil.getDom4jUtil();
			out.setFuncID("OpenDrawing");
			out.setResult("true");
			out.setDesc("�ɹ���ͼֽ");
			for (int i = 0; i < treePaths.length; i++) {
				Element objEle = out.getObjectElement();
				// util.setProValue(objEle, "testName", "testValue");

				DefaultMutableTreeNode tempTreeNode = (DefaultMutableTreeNode) treePaths[i].getLastPathComponent();

				if (isCheckOut) {
					objEle.addAttribute("refresh", "true");
					checkoutDatasetFile(tempTreeNode);
				} else {
					objEle.addAttribute("refresh", "false");
					openBtnDownActionPerformed(tempTreeNode);
				}

				for (int j = 0; j < filePathList.size(); j++) {
					Element docEle = objEle.addElement("docs").addElement("doc");
					util.setProValue(docEle, "LocalPath", filePathList.get(j));
				}
			}
			try {
				out.sendResultToUI();
				dispose();
				setCursor(defCursor);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	// ----------------------------------------------------------------------------------------------------------------------

}
