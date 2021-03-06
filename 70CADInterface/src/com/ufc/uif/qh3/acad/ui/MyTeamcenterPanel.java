package com.ufc.uif.qh3.acad.ui;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.dom4j.Element;

import com.ufc.uif.base_adaptor.AdaptorWriter;
import com.ufc.uif.qh3.acad.images.IconManager;
import com.ufc.uif.qh3.acad.operation.ParseConfigXMl;
import com.ufc.uif.qh3.acad.operation.ParseRequestXML;
import com.ufc.uif.qh3.acad.util.UFCComboBox;
import com.ufc.uif.qh3.acad.util.UFCTextField;
import com.ufc.uif.tccommunicationimpl.operation.connecttc.TCUtil;
import com.ufc.uif.tcuacommunication.objects.WSObject;
import com.ufc.uif.tcuacommunication.operation.ITCTCObjOperation;
import com.ufc.uif.tcuacommunication.operation.exception.TCOperationException;
import com.ufc.uif.util.service.ServiceUtil;

/**
 * 
 * @author wjf
 */
public class MyTeamcenterPanel extends javax.swing.JPanel {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  /** Creates new form WorkspaceObjectFrame */
  private static List<String> typeList = new ArrayList<String>();
  WSObject[] wsobObjects = null;
  static {
    String[] types = new String[] { "Folder", "Newstuff Folder", "Mail Folder", "Qh3_DesPart", "Qh3_DesPartRevision", "Qh3_ElcPart", "Qh3_ElcPartRevision", "Qh3_Material", "Qh3_MaterialRevision",
        "Qh3_AssMaterial", "Qh3_AssMaterialRevision", "Qh3_SoftWare", "Qh3_SoftWareRevision", "Qh3_StdPart", "Qh3_StdPartRevision"};
    typeList = Arrays.asList(types);
  }

  private static Map<String, Map<String, String>> attrVauleInSendMes = new HashMap<String, Map<String, String>>();
  static {
    Map<String, String> desPartAttr = new HashMap<String, String>();
    desPartAttr.put("代号", "");
  }

  public MyTeamcenterPanel(JFrame frame) {
    System.out.println("207XTCAD----MyTeamcenterPanel-----------MyTeamcenterPanel");
    // super(frame, true);
    this.frame = frame;
    initComponents();
    initDialog();
  }

  public MyTeamcenterPanel() {
    System.out.println("207XTCAD----MyTeamcenterPanel-----------MyTeamcenterPanel");
    initComponents();
    initDialog();
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">
  private void initComponents() {
    System.out.println("207XTCAD----MyTeamcenterPanel-----------initComponents");
    jSplitPane1 = new javax.swing.JSplitPane();
    jPanel2 = new javax.swing.JPanel();
    jScrollPane1 = new javax.swing.JScrollPane();
    homeTree = new javax.swing.JTree();
    jPanel4 = new javax.swing.JPanel();
    searchText = new javax.swing.JTextField();
    searchBtn = new javax.swing.JButton();
    clearBtn = new javax.swing.JButton();
    jPanel3 = new javax.swing.JPanel();
    jPanel1 = new javax.swing.JPanel();
    jScrollPane2 = new javax.swing.JScrollPane();
    wsObjTable = new javax.swing.JTable();
    buttonPanel1 = new javax.swing.JPanel();
    cancelBtn = new javax.swing.JButton();
    okBtn = new javax.swing.JButton();

    searchText.setToolTipText("按ITEM ID进行查询");
    searchBtn.setToolTipText("根据输入的条件进行搜索");
    clearBtn.setToolTipText("清除搜索条件");
    okBtn.setToolTipText("返回选中的数据");
    cancelBtn.setToolTipText("关闭对话框，中止操作");

    jSplitPane1.setDividerLocation(210);
    jSplitPane1.setName("jSplitPane1"); // NOI18N
    jSplitPane1.setOneTouchExpandable(true);

    jPanel2.setBackground(new java.awt.Color(226, 245, 252));
    jPanel2.setName("jPanel2"); // NOI18N

    jScrollPane1.setBackground(new java.awt.Color(241, 250, 255));
    jScrollPane1.setName("jScrollPane1"); // NOI18N

    homeTree.setBackground(new java.awt.Color(241, 250, 255));
    homeTree.setName("homeTree"); // NOI18N
    jScrollPane1.setViewportView(homeTree);
    homeTree.addTreeSelectionListener(new TreeSelectionListener() {

      public void valueChanged(TreeSelectionEvent e) {
        treeSelectionAction();
      }

    });

    homeTree.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent event) {
        int click = event.getClickCount();
        if (click == 2) {
          // treeSelectionAction();
        }
      }
    });

    jPanel4.setBackground(new java.awt.Color(241, 250, 255));
    jPanel4.setName("jPanel4"); // NOI18N

    searchText.setName("searchText"); // NOI18N

    searchBtn.setIcon(IconManager.getIcon("search.png")); // NOI18N
    searchBtn.setName("searchBtn"); // NOI18N
    searchBtn.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        searchWorkspace();
      }

    });
    searchText.addKeyListener(new KeyAdapter() {

      @Override
      public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_ENTER) {
          searchBtnAction();
        }
      }
    });

    clearBtn.setIcon(IconManager.getIcon("clear.png")); // NOI18N
    clearBtn.setName("clearBtn"); // NOI18N
    clearBtn.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
        searchText.setText("");
      }

    });

    org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
    jPanel4.setLayout(jPanel4Layout);
    jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
        org.jdesktop.layout.GroupLayout.TRAILING,
        jPanel4Layout.createSequentialGroup().add(searchText, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(
            searchBtn, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 32, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(
            clearBtn, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 31, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)));
    jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
        org.jdesktop.layout.GroupLayout.TRAILING,
        jPanel4Layout.createSequentialGroup().add(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(org.jdesktop.layout.GroupLayout.LEADING, searchText,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.LEADING, clearBtn,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 31, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(org.jdesktop.layout.GroupLayout.LEADING, searchBtn,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)).addContainerGap()));

    org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
        jPanel2Layout.createSequentialGroup().addContainerGap().add(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE).add(jPanel4,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));
    jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
        org.jdesktop.layout.GroupLayout.TRAILING,
        jPanel2Layout.createSequentialGroup().addContainerGap().add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE).addPreferredGap(
            org.jdesktop.layout.LayoutStyle.UNRELATED).add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 31, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap()));

    jSplitPane1.setLeftComponent(jPanel2);

    jPanel3.setBackground(new java.awt.Color(226, 245, 252));
    jPanel3.setName("jPanel3"); // NOI18N

    jPanel1.setBackground(new java.awt.Color(226, 245, 252));
    jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("详细信息"));
    jPanel1.setName("jPanel1"); // NOI18N

    jScrollPane2.setName("jScrollPane2"); // NOI18N

    wsObjTable.setBackground(new java.awt.Color(241, 250, 255));
    wsObjTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {
    // {null, null, null, null},
        // {null, null, null, null},
        // {null, null, null, null},
        // {null, null, null, null}
        }, new String[] {
        // "Title 1", "Title 2", "Title 3", "Title 4"
        }));
    wsObjTable.setName("wsObjTable"); // NOI18N
    wsObjTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    wsObjTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
    jScrollPane2.setViewportView(wsObjTable);

    org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 447,
        Short.MAX_VALUE));
    jPanel1Layout
        .setVerticalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE));

    buttonPanel1.setBackground(new java.awt.Color(226, 245, 252));
    buttonPanel1.setName("buttonPanel1"); // NOI18N

    cancelBtn.setText("关闭");
    cancelBtn.setName("cancelBtn"); // NOI18N
    cancelBtn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cancelBtnActionPerformed(evt);
      }
    });

    okBtn.setText("确定");
    okBtn.setName("okBtn"); // NOI18N
    okBtn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        okBtnActionPerformed(evt);
      }
    });

    org.jdesktop.layout.GroupLayout buttonPanel1Layout = new org.jdesktop.layout.GroupLayout(buttonPanel1);
    buttonPanel1.setLayout(buttonPanel1Layout);
    buttonPanel1Layout.setHorizontalGroup(buttonPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING,
        buttonPanel1Layout.createSequentialGroup().addContainerGap(321, Short.MAX_VALUE).add(okBtn).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(cancelBtn).add(2, 2, 2)));
    buttonPanel1Layout.setVerticalGroup(buttonPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
        buttonPanel1Layout.createSequentialGroup().add(buttonPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(cancelBtn).add(okBtn)).addContainerGap(7,
            Short.MAX_VALUE)));

    org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(
        jPanel3Layout.createSequentialGroup().addContainerGap().add(buttonPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addContainerGap()));
    jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
        org.jdesktop.layout.GroupLayout.TRAILING,
        jPanel3Layout.createSequentialGroup().addContainerGap().add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(buttonPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addContainerGap()));

    jSplitPane1.setRightComponent(jPanel3);

    org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
    setLayout(layout);
    layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 679, Short.MAX_VALUE));
    layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 547, Short.MAX_VALUE));

  }// </editor-fold>

  /**
   * @param args
   *            the command line arguments
   */
  public static void main(String args[]) {
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new MyTeamcenterPanel(null).setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify
  private javax.swing.JPanel buttonPanel1;
  private javax.swing.JButton cancelBtn;
  private javax.swing.JButton clearBtn;
  private javax.swing.JTree homeTree;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JPanel jPanel3;
  private javax.swing.JPanel jPanel4;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JSplitPane jSplitPane1;
  private javax.swing.JButton okBtn;
  private javax.swing.JButton searchBtn;
  private javax.swing.JTextField searchText;
  private javax.swing.JTable wsObjTable;
  private WSObject Newstuff;

  // End of variables declaration

  /**
   * 初始化对话框
   */
  public void initDialog() {
    System.out.println("207XTCAD----MyTeamcenterPanel-----------initDialog");
    initTree();
    // 解析配置文件，获取TC对象的属性信息
    /*
     * itemProps = ParseRequestXML.getTCObjectProps(); String[] types =
     * itemProps.keySet().toArray(new String[0]);
     */
    // filterTypes = Arrays.asList(types);
    filterTypes.add("Qh3_DesPart");
    filterTypes.add("Qh3_ElcPart");
    filterTypes.add("Qh3_Material");
    filterTypes.add("Qh3_AssMaterial");
    filterTypes.add("Qh3_SoftWare");
    filterTypes.add("Qh3_StdPart");
  }

  /**
   * 初始化Home树
   * 
   * @author wjf
   * 
   *         This project for tc_communication.teamcenter2007
   */
  public void initTree() {
    System.out.println("207XTCAD----MyTeamcenterPanel-----------initTree");
    // 找到HOME文件夹
    homeNodes = null;
    try {
      homeNodes = tcop.getContentsOfFolder("Home");
    } catch (TCOperationException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(this, "对话框初始化时出现错误，错误原因如下：\n" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
      return;
    }
    root = new DefaultMutableTreeNode("Home");
    for (int j = 1; j < homeNodes.length; j++) {
      subNode = new DefaultMutableTreeNode(homeNodes[j]);
      root.add(subNode);
    }
    searchNode = new DefaultMutableTreeNode("搜索结果");
    root.add(searchNode);

    // 向Table添加信息
    tableModel = new WorkspaceTableModel(homeNodes);
    wsObjTable.setModel(tableModel);
    tableRender = new WorkspaceTableRender();
    wsObjTable.setDefaultRenderer(String.class, tableRender);
    wsObjTable.validate();
    wsObjTable.updateUI();

    DefaultTreeModel treeModel = new DefaultTreeModel(root);
    homeTree.setModel(treeModel);
    homeTree.setCellRenderer(render);
    homeTree.setExpandsSelectedPaths(true);
    homeTree.setScrollsOnExpand(true);
    homeTree.updateUI();
  }
  
  /**
   * 选择树节点时的操作
   * 
   * @author Liugz
   * @create on 2009-3-12 This project for CAD_Concrete
   * @param event
   */
  protected void treeSelectionAction() {
    System.out.println("207XTCAD----MyTeamcenterPanel-----------treeSelectionAction");
    // stateDlg = new StateDialog(frame, false, "正在载入内容，请稍候……");
    Thread thread = new Thread(new Runnable() {

      public void run() {
        if (waitDlg != null)
          waitDlg.dispose();

        waitDlg = new WaitDialog(frame, false);
        frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        searchTreeNode();
        frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        waitDlg.dispose();
      }

    });
    thread.start();
  }

  /**
   * 点击树节点时，查询该节点下的内容
   * 
   * @author
   * @create on 2009-3-10 This project for tc_communication.teamcenter2007
   */
  private void searchTreeNode() {
    System.out.println("207XTCAD----MyTeamcenterPanel-----------searchTreeNode");
    TreePath treePath = homeTree.getSelectionPath();
    if (null == treePath)
      return;

    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();

    // 如果选中的是Home节点
    if (selectedNode == root) {
      tableModel = new WorkspaceTableModel(homeNodes);
      wsObjTable.setModel(tableModel);
      wsObjTable.validate();
      wsObjTable.updateUI();
      return;
    }
    // 如果是搜索节点
    if (selectedNode == searchNode) {
      int child = searchNode.getChildCount();

      if (child == 0 || wsobObjects == null) {
        return;
      } else {
        tableModel = new WorkspaceTableModel(wsobObjects);
        wsObjTable.setModel(tableModel);
        wsObjTable.validate();
        wsObjTable.updateUI();
        return;
      }
    }

    Object objNode = selectedNode.getUserObject();
    if (objNode instanceof WSObject) {
      selectedNode.removeAllChildren();
      // homeTree.updateUI();
      WSObject selectedObj = (WSObject) objNode;
      String type = (selectedObj.getType());
      WSObject[] subObjs = null;
      List<WSObject> WSlist = new ArrayList<WSObject>();
      if (type.contains("Folder")) { // 如果是Folder
        try {
          WSObject[] subObjss = tcop.getContentsOfFolder(selectedObj.getUid());
          if (subObjss != null) {
            for (int i = 0; i < subObjss.length; i++) {
              if (typeList.contains(subObjss[i].getType())) {
                WSlist.add(subObjss[i]);
              }
            }
            int length = WSlist.size();
            subObjs = new WSObject[length];
            for (int j = 0; j < length; j++) {
              subObjs[j] = WSlist.get(j);
            }
          }
        } catch (TCOperationException e) {
          JOptionPane.showMessageDialog(this, "查询树节点时发生错误，错误信息如下：\n" + e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
          return;
        }
        if (null != subObjs && subObjs.length != 0) {
          for (WSObject wsObj : subObjs) {
            DefaultMutableTreeNode subNode = new DefaultMutableTreeNode(wsObj);
            selectedNode.add(subNode);
          }
        }
        tableModel = new WorkspaceTableModel(subObjs);
        wsObjTable.setModel(tableModel);
      } else if (filterTypes.contains(type)) {

        String id = selectedObj.getId();
        if (null != id && !"".equals(id)) {
          // Map<String, String> propNames = itemProps.get(type);
          try {
            subObjs = tcop.getAllItemRevision(selectedObj);
          } catch (TCOperationException e) {
            JOptionPane.showMessageDialog(this, "获取版本对象发生错误，错误信息如下：\n" + e.getMessage(), "提示", JOptionPane.ERROR_MESSAGE);
            return;
          }
          // subObjs =
          // tcop.getItemFormInfo(tcop.getItemByUid(selectedObj.getUid()),
          // propNames);

          if (null != subObjs && subObjs.length != 0) {
            for (WSObject wsObj : subObjs) {
              // 添加树节点
              DefaultMutableTreeNode subNode = new DefaultMutableTreeNode(wsObj);
              selectedNode.add(subNode);
            }
            // 改变表格内容
            /*
             * // 返回最新的Revision上的信息 WSObject lastRev =
             * subObjs[subObjs.length - 1]; WSObject lastObj=null;
             * if(type.equals("Cetc20Part")){ try {
             * lastObj=tcop.getItemRevisionFormInfo(lastRev,
             * propNames); } catch (TCOperationException e) {
             * JOptionPane.showMessageDialog(this,
             * "未能成功获取对象属性信息，错误原因如下：\n" + e.getMessage(), "提示",
             * JOptionPane.ERROR_MESSAGE); return ;
             * //e.printStackTrace(); } }else{ try {
             * lastObj=tcop.getItemRevisionInfo(lastRev, propNames);
             * } catch (TCOperationException e) { // TODO
             * Auto-generated catch block
             * JOptionPane.showMessageDialog(this,
             * "未能成功获取对象属性信息，错误原因如下：\n" + e.getMessage(), "提示",
             * JOptionPane.ERROR_MESSAGE); return ; //
             * e.printStackTrace(); } }
             */
            // tableModel = new WorkspaceTableModel(new
            // WSObject[]{lastObj}, propNames, "Item");
            tableModel = new WorkspaceTableModel(subObjs);
            wsObjTable.setModel(tableModel);
            wsObjTable.setRowSelectionInterval(0, 0);

          }
        }
      } else {

      }
      // 刷新Table的信息
      // tableModel = new WorkspaceTableModel(subObjs);
      // wsObjTable.setModel(tableModel);
      wsObjTable.validate();
    }
    // homeTree.updateUI();
    homeTree.expandPath(treePath);

  }
  
  /**
   * 根据文本框中输入的内容，按回车键进行查询
   * @author Liugz
   * @create on 2009-3-14 This project for CAD_Concrete
   */
  protected void searchBtnAction() {
    System.out.println("207XTCAD----MyTeamcenterPanel-----------searchBtnAction");
    Thread thread = new Thread(new Runnable() {
      public void run() {
        if (waitDlg != null)
          waitDlg.dispose();

        waitDlg = new WaitDialog(frame, false);
        waitDlg.setMessage("正在进行操作，请稍候……");
        frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        searchWorkspace();
        frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        waitDlg.dispose();
      }
    });
    thread.start();
  }
  
  /**
   * 根据文本框中输入的内容，进行查询
   * 
   * @author Liugz
   * @create on 2009-3-10 This project for tc_communication.teamcenter2007
   */
  private void searchWorkspace() {
    System.out.println("207XTCAD----MyTeamcenterPanel-----------searchWorkspace");
    List<WSObject> tableObjects = new ArrayList<WSObject>();
    String query = searchText.getText().trim();
    if ("".equals(query.trim())) {
      return;
    }
    foundObjs = null;
    searchNode.removeAllChildren();
    homeTree.updateUI();

    try {
      Map<String, String> criterias = new HashMap<String, String>();
      String itemId = query;
      foundObjs = tcop.queryObjectsBySavedQuery("Item ID", new String[] { itemId } 
      );
      String user = TCUtil.getUserId();
      String owner = "";
      if (null == foundObjs) {
        searchNode.add(new DefaultMutableTreeNode("没有找到符合条件的对象"));
      } else {
        // ======需过滤设计等其他类型，留零部件类型
        for (WSObject obj : foundObjs) {
          owner = obj.getOwnUser();
          String objType = obj.getType();
          /*
           * if(owner.equals(user)&&filterTypes.contains(objType)){
           * tableObjects.add(obj); searchNode.add(new
           * DefaultMutableTreeNode(obj)); }
           */
          if (filterTypes.contains(objType)) {
            tableObjects.add(obj);
            searchNode.add(new DefaultMutableTreeNode(obj));
          }
        }
      }
    } catch (TCOperationException e) {
      JOptionPane.showMessageDialog(this, "查询WorkspaceObject时发生错误，错误原因如下：\n" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
      return;
    }
    // // 展开搜索节点
    homeTree.setSelectionRow(homeTree.getRowCount() - 1);
    // homeTree.setSelectionPath(new TreePath(searchNode));
    // homeTree.expandPath(new TreePath(searchNode));
    homeTree.expandRow(homeTree.getRowCount() - 1);

    // 刷新表格的内容
    // tableModel = new WorkspaceTableModel(foundObjs);
    wsobObjects = new WSObject[tableObjects.size()];
    for (int k = 0; k < tableObjects.size(); k++) {
      wsobObjects[k] = tableObjects.get(k);
      // System.out.println("===table内有=="+wsobObjects[k].getName());
    }
    tableModel = new WorkspaceTableModel(wsobObjects);
    wsObjTable.setModel(tableModel);
    wsObjTable.validate();
    wsObjTable.updateUI();
  }
  
  /**
   * “确定”按钮的操作
   * 
   * @author Liugz
   * @create on 2009-3-11 This project for tc_communication.teamcenter2007
   * @param evt
   */

  // 需添加返回多种类型的信息
  protected void okBtnActionPerformed(ActionEvent evt) {
    System.out.println("207XTCAD----MyTeamcenterPanel-----------okBtnActionPerformed");
    if (wsObjTable.getSelectedRowCount() == 0) {
      JOptionPane.showMessageDialog(this, "请从表格中选择要返回的数据！", "提示", JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    // 将选中的数据放到Map中
    // Map<String, String> propNames =new HashMap<String, String>();

    WorkspaceTableModel tableModel = (WorkspaceTableModel) wsObjTable.getModel();
    int row = wsObjTable.getSelectedRow();
    WSObject tempObj = tableModel.getWSObj(row);
    String objType = tempObj.getType();

    if (!typeList.contains(objType) || objType.endsWith("Folder")) {
      frame.setAlwaysOnTop(false);
      JOptionPane.showConfirmDialog(null, "请选择正确的对象类型", "提示", JOptionPane.WARNING_MESSAGE);
      return;
    }
    /*
     * // System.out.println("======对象ID======"+itemId.toString()); for(int
     * i = 0; i < wsObjTable.getColumnCount(); i++){ int realIndex =
     * wsObjTable.convertColumnIndexToModel(i); String value =
     * (String)wsObjTable.getValueAt(row, realIndex); String colName =
     * wsObjTable.getColumnName(realIndex);
     * 
     * if(colName.equals("对象类型")){ if(!typeList.contains(value) ||
     * value.endsWith("Folder")){ frame.setAlwaysOnTop(false);
     * JOptionPane.showConfirmDialog(null, "请选择正确的对象类型", "提示",
     * JOptionPane.WARNING_MESSAGE); return; } } }
     */

    try {
      WSObject itemRev = null;
      if (objType.endsWith("Revision")) {
        itemRev = tempObj;
      } else {
        itemRev = tcop.getItemRevision(tempObj, "Last");
      }

      // 20130217
      WSObject tt[] = tcop.getView(itemRev);

      if (tt != null) {
        for (WSObject wso : tt) {
          System.out.println("Name=" + wso.getName() + ",Id=" + wso.getId());
        }
      }

      Map<String, String> attrMap = getDisNameValueMap(itemRev);
      getResponseXML(attrMap);

    } catch (TCOperationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    frame.dispose();
  }
  
  /**
   * 读取fillBackInfo配置文件，返回选中的版本属性
   * @param wsObj
   * @return
   */
  private Map<String, String> getDisNameValueMap(WSObject wsObj) {
    System.out.println("207XTCAD----MyTeamcenterPanel-----------getDisNameValueMap");
    Map<String, String> resultDisNameValueMap = new HashMap<String, String>();
    String UFCROOT = System.getenv("UFCROOT");
    String fileSeparator = System.getProperty("file.separator");
    String configFilePath = UFCROOT + fileSeparator + "AutoCAD" + fileSeparator + "cfg" + fileSeparator + "fillBackInfo.xml";

    ParseConfigXMl parseCfg = new ParseConfigXMl();
    Element rootNode = parseCfg.getRootNode(configFilePath);
    Map<String, List<Map<String, String>>> msgInfo = parseCfg.getSendMsgInfo(rootNode);

    String objectType = wsObj.getType();
    List<Map<String, String>> sendMsgMapList = msgInfo.get(objectType);

    for (int i = 0; i < sendMsgMapList.size(); i++) {
      Map<String, String> sendMsgMap = sendMsgMapList.get(i);
      String displayName = sendMsgMap.get("displayName");
      String property = sendMsgMap.get("property");
      String separator = sendMsgMap.get("separator");

      String value = "";

      String[] propertyArr = property.split(",");
      for (int j = 0; j < propertyArr.length; j++) {
        String tempProperty = propertyArr[j];
        if (tempProperty.trim().length() > 0) {
          String tempValue = tcop.getPropertyValueOfObject(wsObj, tempProperty);
          if (tempValue.trim().length() > 0) {
            if (value.trim().length() > 0) {
              value = value + separator + tempValue;
            } else {
              value = tempValue;
            }
          }
        }
      }

      resultDisNameValueMap.put(displayName, value);

    }
    /*
     * Iterator<?> it = sendMsgMap.entrySet().iterator();
     * while(it.hasNext()){ Map.Entry entry = (Map.Entry)it.next(); String
     * displayName = (String)entry.getKey(); String property =
     * (String)entry.getValue();
     * 
     * String value = "";
     * 
     * String[] propertyArr = property.split(","); for(int i = 0; i <
     * propertyArr.length; i++){ String tempProperty = propertyArr[i];
     * if(tempProperty.trim().length() > 0){ String tempValue =
     * tcop.getPropertyValueOfObject(wsObj, tempProperty);
     * if(tempValue.trim().length() > 0){ if(value.trim().length() > 0){
     * value = value + "_" + tempValue; }else{ value = tempValue; } } } }
     * resultDisNameValueMap.put(displayName, value); }
     */
    return resultDisNameValueMap;
  }
  
  /**
   * 
   * TODO 查询请求会送消息生成
   * 
   * @return void
   * @author lijj created on 2011-9-2下午02:23:04
   */
  private void getResponseXML(Map<String, String> infos) {
    System.out.println("207XTCAD----MyTeamcenterPanel-----------getResponseXML");
    // 修改请求中的信息
    out.setFuncID("doQuery");
    out.setResult("true");
    out.setDesc("Backfill CAD Drawing.");
    Element objectEle = out.getObjectElement();

    Element props = objectEle.addElement("properties");

    for (Iterator<String> keyItor = infos.keySet().iterator(); keyItor.hasNext();) {
      String key = keyItor.next();
      String value = infos.get(key);

      Element proInfo = props.addElement("property");
      proInfo.addAttribute("name", key);
      proInfo.addAttribute("value", value);
    }

    try {
      out.sendResultToUI();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  /**
   * 点击树节点时执行的操作
   */
  /*
   * private void treeNodeSelectedAction() { TreePath treePath =
   * homeTree.getSelectionPath(); if(null == treePath) return ;
   * 
   * DefaultMutableTreeNode selectedNode =
   * (DefaultMutableTreeNode)treePath.getLastPathComponent(); // 如果选中的是Home节点
   * if(selectedNode == root){ return ; } // 如果是搜索节点 if(selectedNode ==
   * searchNode){ return ; } //Map<String, String> propNames =
   * itemProps.get("Cetc20Part");
   * 
   * Object objNode = selectedNode.getUserObject(); if(objNode instanceof
   * WSObject){ WSObject selectedObj = (WSObject)objNode; String type =
   * (selectedObj.getType()); WSObject[] subObjs = null; WSObject[] subObjsRev
   * = null; // 对指定类型的Item进行查询，查询其下的Revision和Dataset
   * if(filterTypes.contains(type)){ try { selectedNode.removeAllChildren();
   * if(!type.contains("Revision")){// 如果是零部件，则查找出所有的Revision subObjs =
   * tcop.getAllItemRevision(selectedObj); } else
   * if(type.contains("Cetc20Part Revision")){
   * subObjs=tcop.getRelatedItemRevObj(selectedObj, "TC_Is_Represented_By");
   * }else if(type.contains("Cetc20Dwg Revision")){ subObjs =
   * tcop.getDatasetOfItemRevision(selectedObj, new String[]{"AcadDwg"}); } //
   * 如果是Revision类型，那么找出它下面所有的Dataset // subObjs =
   * tcop.getDatasetOfItemRevision(selectedObj, new String[]{"Dwg"}); //
   * subObjs[0]=tcop.getItemRevisionFormInfo(selectedObj,
   * propNames);//.getDatasetOfItemRevision(selectedObj, new
   * String[]{"Dwg"},"IMAN_specification");
   * 
   * } catch (TCOperationException e) { JOptionPane.showMessageDialog(this,
   * "查询树节点时发生错误，错误信息如下：\n" + e.getMessage(), "提示",
   * JOptionPane.ERROR_MESSAGE); return ; } if(null != subObjs &&
   * subObjs.length != 0){ for(WSObject wsObj : subObjs){
   * DefaultMutableTreeNode subNode = new DefaultMutableTreeNode(wsObj);
   * selectedNode.add(subNode); } } } else {
   * 
   * } } //homeTree.updateUI(); homeTree.expandPath(treePath); }
   */

  /**
   * 将对话框信息反填回CAD图纸
   * 
   * @author wjf
   * @param matrev
   * @create on 2009-2-25 This project for CAD_Concrete
   * @return
   */
  /*
   * private boolean backfillCADDrawing(Map<String, String> infos,String
   * id,String itemname,String type, WSObject matrev,String matName) {
   * 
   * // 修改请求中的信息 if(type.contains("Cetc20STPart")){ type="标准件"; }else
   * if(type.contains("Cetc20Stuff")){ type="辅料"; }else
   * if(type.contains("Cetc20Comp")){ type="元器件"; }else
   * if(type.contains("Cetc20Part")||type.contains("Cetc20GPart")){
   * type="零部件"; }else if(type.contains("Cetc20GPart")){ type="零部件"; } else
   * if(type.contains("Cetc20Mat")){ type="材料"; } out.setFuncID("QueryItem");
   * out.setResult("true"); out.setDesc("Backfill CAD Drawing."); Element
   * object2 = out.getObjectElement(); object2.addAttribute("type", type);
   * Element props = object2.addElement("properties"); Element root =
   * this.requestBody; Element object =
   * root.element("body");//.element("object"); String key=""; Set<String>
   * keys=infos.keySet(); Object[] ks=keys.toArray(); Element
   * pro=props.addElement("property"); pro.addAttribute("name","item_id");
   * pro.addAttribute("value",id ); Element
   * proname=props.addElement("property"); if(type.equals("元器件")){
   * proname.addAttribute("name","名称");
   * System.out.println("infos.get(+器材简称:"+infos.get("器材简称"));
   * if(infos.get("器材简称")!=null){
   * 
   * proname.addAttribute("value",infos.get("器材简称") ); }else{
   * proname.addAttribute("value","" ); } for(int i=0;i<ks.length;i++){
   * Element pro1=props.addElement("property"); String name=ks[i].toString();
   * String value=infos.get(ks[i].toString());
   * 
   * if(name.equals("零部件代号")){ name="图号"; }else if(name.equals("重量")){
   * name="质量"; } pro1.addAttribute("name",name); if(value.equals("一般件")){
   * value=""; }else if(value.equals("重要件")){ value="Z"; }else
   * if(value.equals("关键件")){ value="G"; } pro1.addAttribute("value",value); }
   * }else if(type.equals("辅料")){ proname.addAttribute("name","名称");
   * if(infos.get("器材简称")!=null){
   * 
   * proname.addAttribute("value",infos.get("器材简称") ); }else{
   * proname.addAttribute("value",infos.get("零部件名称")); } for(int
   * i=0;i<ks.length;i++){ Element pro1=props.addElement("property"); String
   * name=ks[i].toString(); String value=infos.get(ks[i].toString());
   * 
   * if(name.equals("零部件代号")){ name="图号"; }else if(name.equals("重量")){
   * name="质量"; } pro1.addAttribute("name",name); if(value.equals("一般件")){
   * value=""; }else if(value.equals("重要件")){ value="Z"; }else
   * if(value.equals("关键件")){ value="G"; } pro1.addAttribute("value",value); }
   * }else{ proname.addAttribute("name","名称");
   * proname.addAttribute("value",itemname );
   * 
   * for(int i=0;i<ks.length;i++){ Element pro1=props.addElement("property");
   * String name=ks[i].toString(); String value=infos.get(ks[i].toString());
   * 
   * if(name.equals("零部件代号")){ name="图号"; }else if(name.equals("重量")){
   * name="质量"; } pro1.addAttribute("name",name); if(value.equals("一般件")){
   * value=""; }else if(value.equals("重要件")){ value="Z"; }else
   * if(value.equals("关键件")){ value="G"; } pro1.addAttribute("value",value); }
   * //如果有材料返回材料信息 if(matrev!=null){
   * 
   * Map<String,String> matpropNames=new HashMap<String, String>();
   * matpropNames.put("材料编码", "az_MaterialNo"); matpropNames.put("材料上标",
   * "az_MaterialUp"); matpropNames.put("材料下标", "az_MaterialDown"); WSObject
   * matMaster; try { Element matproname=props.addElement("property");
   * matproname.addAttribute("name","材料名称");
   * matproname.addAttribute("value",matName); matMaster =
   * tcop.getItemRevisionInfo(matrev, matpropNames); Map<String,
   * String>attribMat= matMaster.getAttributes(); for(Iterator<String> keyItor
   * = attribMat.keySet().iterator(); keyItor.hasNext();) { String key1 =
   * keyItor.next(); String valuemat = attribMat.get(key1);
   * System.out.println("\t " + key1 + "  ==  " + valuemat); Element
   * pro1=props.addElement("property"); String name=key1; String
   * value=valuemat; pro1.addAttribute("name",name);
   * pro1.addAttribute("value",value); }
   * 
   * } catch (TCOperationException e) { // TODO Auto-generated catch block
   * e.printStackTrace(); }//.getItemFormInfo(itemObj[0], propNames); } } try
   * { // 将CAD信息打印出来 out.sendResultToUI(); return true;
   * 
   * } catch (IOException e) { // TODO Auto-generated catch block
   * e.printStackTrace(); return false; } }
   */
  /**
   * “取消”按钮的操作
   * 
   * @author Liugz
   * @create on 2009-3-11 This project for tc_communication.teamcenter2007
   * @param evt
   */
  protected void cancelBtnActionPerformed(ActionEvent evt) {
    // 关闭按钮
    int r = JOptionPane.showConfirmDialog(this, "您尚未完成操作，确认退出？", "提示", JOptionPane.YES_NO_CANCEL_OPTION);
    if (r == JOptionPane.OK_OPTION) {
      closeWindow();
    } else
      return;
  }

  /**
   * 关闭窗口
   * 
   * @author Liugz
   * @create on 2008-12-22 This project for CYM
   */
  public void closeWindow() {
    try {
      out.setFuncID("QueryItem");
      out.setResult("CloseWindow");
      out.setDesc("用户取消操作。");
      out.sendResultToUI();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    frame.dispose();
  }

  // private TCObjectOperation tcop = new TCObjectOperation();
  private ITCTCObjOperation tcop = (ITCTCObjOperation) ServiceUtil.getService(ITCTCObjOperation.class.getName(), SaveDrawingDialog.class.getClassLoader());

  private DefaultMutableTreeNode root;
  private DefaultMutableTreeNode subNode;
  private DefaultMutableTreeNode searchNode;
  private WSObjectTreeCellRender render = new WSObjectTreeCellRender();
  private WorkspaceTableModel tableModel;
  private WorkspaceTableRender tableRender;
  private WSObject[] foundObjs;
  private WSObject[] homeNodes;
  // private ObjectOperation op;
  private List<String> filterTypes = new ArrayList<String>();

  // 要获取的Form上的属性名称
  // private static Map<String, Map<String, String>> itemProps = new
  // HashMap<String, Map<String, String>>();

  private Element requestBody;
  private AdaptorWriter out;
  private JFrame frame;
  private StateDialog stateDlg;
  private WaitDialog waitDlg;

  public MyTeamcenterPanel(Element requestBody, AdaptorWriter out, JFrame frame) {
    this.frame = frame;
    this.requestBody = requestBody;
    this.out = out;
    initComponents();
    initDialog();
  }

  /**
   * @return the requestBody
   */
  public Element getRequestBody() {
    return requestBody;
  }

  /**
   * @param requestBody
   *            the requestBody to set
   */
  public void setRequestBody(Element requestBody) {
    this.requestBody = requestBody;
  }

  /**
   * @return the out
   */
  public AdaptorWriter getOut() {
    return out;
  }

  /**
   * @param out
   *            the out to set
   */
  public void setOut(AdaptorWriter out) {
    this.out = out;
  }

}
