/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AdvQueryPane2.java
 *
 * Created on 2009-9-12, 11:10:25
 */

package com.ufc.uif.qh3.acad.drawing;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.TableRowSorter;

import org.dom4j.Element;

import com.ufc.uif.base_adaptor.AdaptorWriter;
import com.ufc.uif.qh3.acad.ui.SaveDrawingDialog;
import com.ufc.uif.qh3.acad.ui.WSObjectTreeCellRender;
import com.ufc.uif.qh3.acad.ui.WaitDialog;
import com.ufc.uif.qh3.acad.ui.WorkspaceTableRender;
import com.ufc.uif.qh3.acad.util.UFCComboBox;
import com.ufc.uif.qh3.acad.util.UFCTextArea;
import com.ufc.uif.qh3.acad.util.UFCTextField;
import com.ufc.uif.qh3.acad.operation.ParseRequestXML;
import com.ufc.uif.tcuacommunication.objects.WSObject;

import com.ufc.uif.tcuacommunication.operation.ITCTCObjOperation;
import com.ufc.uif.tcuacommunication.operation.exception.TCOperationException;
import com.ufc.uif.util.service.ServiceUtil;

/**
 *
 * @author Administrator
 */
public class AdvMatQueryPane extends javax.swing.JPanel {

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  //private TCObjectOperation tcop = new TCObjectOperation();
  private ITCTCObjOperation tcop = (ITCTCObjOperation) ServiceUtil
      .getService(ITCTCObjOperation.class.getName(),
          SaveDrawingDialog.class.getClassLoader());
  private WSObject[] homeNodes;
  private WSObjectTreeCellRender render = new WSObjectTreeCellRender();
  private WorkspaceTableModel tableModel;
  private WorkspaceTableRender tableRender;
  private WSObject[] foundObjs;
  // 要获取的Form上的属性名称
  private static Map<String, Map<String, String>> itemProps = ParseRequestXML
      .getTCObjectProps();
  private Element requestBody;
  private AdaptorWriter out;
  private JFrame frame;
  private WaitDialog waitDlg;
  private String partType = "Cetc20Mat";
  private QueryConditionPanel qyPanel;

  //    private static Map<String, String> propNames = new HashMap<String, String>();
  //    static{
  //    //  propNames.put("材料名称", "object_name");
  //      propNames.put("简称", "az_MaterialShortName");
  //      propNames.put("材料编码", "az_MaterialNo");
  //      propNames.put("材料上标", "az_MaterialUp");
  //      propNames.put("材料下标", "az_MaterialDown");
  //    }
  public AdvMatQueryPane(JFrame frame) {
    initComponents();
    this.frame = frame;
    this.requestBody = requestBody;
    this.out = out;
    initComponents();
    initDialog();

  }

  private void initDialog() {
    //初始化搜索条件，默认为零部件查询
    initqueryPanel("Cetc20Mat.xml");
    homeNodes = null;
    try {
      homeNodes = tcop.getContentsOfFolder("Home");
    } catch (TCOperationException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(this, "对话框初始化时出现错误，错误原因如下：\n"
          + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
      return;
    }
    // 向Table添加信息
    tableModel = new WorkspaceTableModel(homeNodes);
    wsObjTable.setModel(tableModel);
    tableRender = new WorkspaceTableRender();
    wsObjTable.setDefaultRenderer(String.class, tableRender);
    wsObjTable.validate();
    wsObjTable.updateUI();
    TableRowSorter sorter = new TableRowSorter(wsObjTable.getModel());
    wsObjTable.setRowSorter(sorter);

  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jSplitPane1 = new javax.swing.JSplitPane();
    jSplitPane2 = new javax.swing.JSplitPane();
    jPanel2 = new javax.swing.JPanel();
    jPanel3 = new javax.swing.JPanel();
    okBtn = new javax.swing.JButton();
    cancelBtn = new javax.swing.JButton();
    jScrollPane2 = new javax.swing.JScrollPane();
    wsObjTable = new javax.swing.JTable();
    prosPanel = new javax.swing.JPanel();
    jPanel1 = new javax.swing.JPanel();
    queryPanel = new javax.swing.JPanel();
    clrBtn = new javax.swing.JButton();
    searchBtn = new javax.swing.JButton();
    comboxPanel = new javax.swing.JPanel();
    jLabel1 = new javax.swing.JLabel();
    jComboBox1 = new javax.swing.JTextField();

    setBackground(new java.awt.Color(226, 245, 252));

    jSplitPane1.setDividerLocation(230);
    jSplitPane1.setDividerSize(4);
    jSplitPane1.setOneTouchExpandable(true);
    jSplitPane1.setOpaque(false);

    jSplitPane2.setDividerLocation(240);
    jSplitPane2.setDividerSize(4);
    jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
    jSplitPane2.setOneTouchExpandable(true);

    jPanel2.setBackground(new java.awt.Color(226, 245, 252));
    jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("搜索结果"));

    jPanel3.setBackground(new java.awt.Color(226, 245, 252));

    okBtn.setText("确定");
    okBtn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        okBtnActionPerformed(evt);
      }
    });

    cancelBtn.setText("取消");
    cancelBtn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cancelBtnActionPerformed(evt);
      }
    });

    org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(
        jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(
        org.jdesktop.layout.GroupLayout.LEADING).add(
        org.jdesktop.layout.GroupLayout.TRAILING,
        jPanel3Layout.createSequentialGroup().addContainerGap(339,
            Short.MAX_VALUE).add(okBtn).add(32, 32, 32).add(
            cancelBtn).add(21, 21, 21)));
    jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(
        org.jdesktop.layout.GroupLayout.LEADING).add(
        jPanel3Layout.createSequentialGroup().add(
            jPanel3Layout.createParallelGroup(
                org.jdesktop.layout.GroupLayout.BASELINE).add(
                cancelBtn).add(okBtn)).addContainerGap(
            org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
            Short.MAX_VALUE)));

    wsObjTable.setModel(new javax.swing.table.DefaultTableModel(
    /*  new Object [][] {
          {null, null, null, null},
          {null, null, null, null},
          {null, null, null, null},
          {null, null, null, null}
      },
      new String [] {
          "Title 1", "Title 2", "Title 3", "Title 4"
      }*/
    ));
    setTableMouseListener();
    jScrollPane2.setViewportView(wsObjTable);

    org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(
        jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(
        org.jdesktop.layout.GroupLayout.LEADING).add(
        org.jdesktop.layout.GroupLayout.TRAILING,
        jPanel2Layout.createSequentialGroup().addContainerGap().add(
            jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
            org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
            Short.MAX_VALUE).addContainerGap()).add(jScrollPane2,
        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 526,
        Short.MAX_VALUE));
    jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(
        org.jdesktop.layout.GroupLayout.LEADING).add(
        org.jdesktop.layout.GroupLayout.TRAILING,
        jPanel2Layout.createSequentialGroup().add(jScrollPane2,
            org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 211,
            Short.MAX_VALUE).addPreferredGap(
            org.jdesktop.layout.LayoutStyle.UNRELATED).add(jPanel3,
            org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
            org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
            org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)));

    jSplitPane2.setRightComponent(jPanel2);

    prosPanel.setBackground(new java.awt.Color(226, 245, 252));
    prosPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("属性"));

    org.jdesktop.layout.GroupLayout prosPanelLayout = new org.jdesktop.layout.GroupLayout(
        prosPanel);
    prosPanel.setLayout(prosPanelLayout);
    prosPanelLayout.setHorizontalGroup(prosPanelLayout.createParallelGroup(
        org.jdesktop.layout.GroupLayout.LEADING).add(0, 526,
        Short.MAX_VALUE));
    prosPanelLayout.setVerticalGroup(prosPanelLayout.createParallelGroup(
        org.jdesktop.layout.GroupLayout.LEADING).add(0, 210,
        Short.MAX_VALUE));

    jSplitPane2.setLeftComponent(prosPanel);

    jSplitPane1.setRightComponent(jSplitPane2);

    jPanel1.setBackground(new java.awt.Color(226, 245, 252));
    jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(
        javax.swing.border.BevelBorder.RAISED));

    queryPanel.setBackground(new java.awt.Color(226, 245, 252));
    queryPanel.setBorder(javax.swing.BorderFactory
        .createTitledBorder("搜索条件："));

    org.jdesktop.layout.GroupLayout queryPanelLayout = new org.jdesktop.layout.GroupLayout(
        queryPanel);
    queryPanel.setLayout(queryPanelLayout);
    queryPanelLayout.setHorizontalGroup(queryPanelLayout
        .createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(0, 193, Short.MAX_VALUE));
    queryPanelLayout.setVerticalGroup(queryPanelLayout.createParallelGroup(
        org.jdesktop.layout.GroupLayout.LEADING).add(0, 392,
        Short.MAX_VALUE));

    //
    /*initqueryPanel("Cetc20Part.xml",queryPanel);
    System.out.println("======>默认为零部件查询");*/

    clrBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource(
        "clear.png"))); // NOI18N
    clrBtn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        clrBtnActionPerformed(evt);
      }
    });
    searchBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource(
        "query.png"))); // NOI18N
    searchBtn.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        searchBtnActionPerformed();
      }
    });

    comboxPanel.setBackground(new java.awt.Color(226, 245, 252));

    jLabel1.setForeground(new java.awt.Color(0, 70, 213));
    jLabel1.setText("查询类型：");
    jComboBox1.setText("材料");
    jComboBox1.setEnabled(false);
    jComboBox1.setEditable(false);
    jComboBox1.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent arg0) {
        //触发不同搜索条件面板的加载
        partType = "Cetc20Mat";
        addQueryPanel(partType);
      }

    });

    org.jdesktop.layout.GroupLayout comboxPanelLayout = new org.jdesktop.layout.GroupLayout(
        comboxPanel);
    comboxPanel.setLayout(comboxPanelLayout);
    comboxPanelLayout.setHorizontalGroup(comboxPanelLayout
        .createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(
            org.jdesktop.layout.GroupLayout.TRAILING,
            comboxPanelLayout.createSequentialGroup().add(10, 10,
                10).add(jLabel1,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE).addPreferredGap(
                org.jdesktop.layout.LayoutStyle.RELATED).add(
                jComboBox1,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                112,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(24, 24, 24)));
    comboxPanelLayout
        .setVerticalGroup(comboxPanelLayout
            .createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING)
            .add(
                comboxPanelLayout
                    .createSequentialGroup()
                    .addContainerGap()
                    .add(
                        comboxPanelLayout
                            .createParallelGroup(
                                org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel1)
                            .add(
                                jComboBox1,
                                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(13, Short.MAX_VALUE)));

    org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(
        jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout
        .setHorizontalGroup(jPanel1Layout
            .createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING)
            .add(
                jPanel1Layout
                    .createSequentialGroup()
                    .addContainerGap()
                    .add(
                        jPanel1Layout
                            .createParallelGroup(
                                org.jdesktop.layout.GroupLayout.LEADING)
                            .add(
                                org.jdesktop.layout.GroupLayout.TRAILING,
                                comboxPanel,
                                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE)
                            .add(
                                org.jdesktop.layout.GroupLayout.TRAILING,
                                jPanel1Layout
                                    .createSequentialGroup()
                                    .add(
                                        clrBtn,
                                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                                        50,
                                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(
                                        18,
                                        18,
                                        18)
                                    .add(
                                        searchBtn,
                                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                                        55,
                                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(
                                org.jdesktop.layout.GroupLayout.TRAILING,
                                queryPanel,
                                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE))
                    .addContainerGap()));
    jPanel1Layout
        .setVerticalGroup(jPanel1Layout
            .createParallelGroup(
                org.jdesktop.layout.GroupLayout.LEADING)
            .add(
                jPanel1Layout
                    .createSequentialGroup()
                    .addContainerGap()
                    .add(
                        comboxPanel,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(
                        org.jdesktop.layout.LayoutStyle.RELATED)
                    .add(
                        queryPanel,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        Short.MAX_VALUE)
                    .addPreferredGap(
                        org.jdesktop.layout.LayoutStyle.RELATED)
                    .add(
                        jPanel1Layout
                            .createParallelGroup(
                                org.jdesktop.layout.GroupLayout.LEADING)
                            .add(clrBtn).add(
                                searchBtn))
                    .addContainerGap()));

    jSplitPane1.setLeftComponent(jPanel1);

    org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(
        this);
    this.setLayout(layout);
    layout.setHorizontalGroup(layout.createParallelGroup(
        org.jdesktop.layout.GroupLayout.LEADING).add(
        org.jdesktop.layout.GroupLayout.TRAILING,
        layout.createSequentialGroup().addContainerGap().add(
            jSplitPane1,
            org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 767,
            Short.MAX_VALUE)));
    layout.setVerticalGroup(layout.createParallelGroup(
        org.jdesktop.layout.GroupLayout.LEADING).add(
        layout.createSequentialGroup().addContainerGap().add(
            jSplitPane1,
            org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 505,
            org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .addContainerGap(
                org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                Short.MAX_VALUE)));
  }// </editor-fold>//GEN-END:initComponents

  /**
   * 在搜索条件面板添加搜索条件
   * @param type，以type命名的xml文件
   */
  protected void addQueryPanel(String type) {
    initqueryPanel(type + ".xml");
  }

  /**
   * 添加控件到搜索面板
   * @param filename
   * @param attrib
   * @param itemName
   * @param prosPanel
   */
  private void initProsPanel(String filename, Map<String, String> attrib,
      String itemName) {
    prosPanel.removeAll();
    ArrayList<SaveModel> allComponents = (ArrayList<SaveModel>) ParseRequestXML
        .parseQuerycondition(filename);
    //根据配置文件生成控件
    for (int i = 0; i < allComponents.size(); i++) {
      JLabel label = new JLabel();
      JComponent jcom = null;

      SaveModel sc = allComponents.get(i);

      label.setText(sc.getDisplayName() + "：");

      if (sc.getType().equals("TEXT")) {
        jcom = new UFCTextField();

        ((UFCTextField) jcom).setDisplayName(sc.getDisplayName());
        ((UFCTextField) jcom).setProperty(sc.getProprety());
        ((UFCTextField) jcom).setRequired(sc.isRequired());
        ((UFCTextField) jcom).setCadName(sc.getCadName());

      } else if (sc.getType().equals("LOV")) {
        jcom = new UFCComboBox();

        ((UFCComboBox) jcom).setDisplayName(sc.getDisplayName());
        ((UFCComboBox) jcom).setProperty(sc.getProprety());
        ((UFCComboBox) jcom).setRequired(sc.isRequired());
        ((UFCComboBox) jcom).setCadName(sc.getCadName());

        for (int x = 0; x < sc.getLovlist().size(); x++) {
          ((UFCComboBox) jcom).addItem(sc.getLovlist().get(x));
        }
      } else if (sc.getType().equals("TEXTArea")) {

        jcom = new UFCTextArea();

        ((UFCTextArea) jcom).setDisplayName(sc.getDisplayName());
        ((UFCTextArea) jcom).setProperty(sc.getProprety());
        ((UFCTextArea) jcom).setRequired(sc.isRequired());
        ((UFCTextArea) jcom).setCadName(sc.getCadName());
        //jcom.isOpaque();
      }
      if (i % 2 == 0) {

        label.setBounds(new Rectangle(20, (i + 30) + (i * 15), 90, 22));
        jcom
            .setBounds(new Rectangle(110, (i + 30) + (i * 15), 120,
                22));
      } else {
        /*label.setBounds(new Rectangle(320, (i+30)+((i-1)*15),90, 22));
              jcom.setBounds(new Rectangle(410, (i+30)+((i-1)*15), 120, 22));*/
        label.setBounds(new Rectangle(260, (i + 30) + ((i - 1) * 15),
            90, 22));
        jcom.setBounds(new Rectangle(350, (i + 30) + ((i - 1) * 15),
            120, 22));

      }
      prosPanel.add(label);
      prosPanel.add(jcom);
    }
    prosPanel.updateUI();

    //根据选中的table行信息，初始化控件的值
    Component[] coms = prosPanel.getComponents();

    for (int i = 0; i < coms.length; i++) {
      if (coms[i] instanceof UFCTextField) {
        UFCTextField utf = (UFCTextField) coms[i];
        String s = attrib.get(utf.getDisplayName());
        if (s == null) {
          s = "";
        }
        utf.setText(s);
        utf.setEditable(false);
        utf.setOpaque(false);
        if (utf.getProperty().equals("object_name")) {
          utf.setText(itemName);
          utf.setEditable(false);
          utf.setOpaque(false);
        }
      } else if (coms[i] instanceof UFCComboBox) {
        UFCComboBox ucb = (UFCComboBox) coms[i];
        String s = attrib.get(ucb.getDisplayName());
        if (s == null) {
          s = "";
        }
        ucb.setSelectedItem(s);
        ucb.setEnabled(false);//.setEditable(false);
        ucb.setOpaque(false);
      }
    }
  }

  /**
   * 添加控件到搜索面板
   * @param filename
   * @param queryPanel
   */
  private void initqueryPanel(String filename) {

    queryPanel.removeAll();

    ArrayList<SaveModel> allComponents = ParseRequestXML
        .parseQuerycondition(filename);
    //根据配置文件生成控件
    for (int i = 0; i < allComponents.size(); i++) {
      JLabel label = new JLabel();
      JComponent jcom = null;

      SaveModel sc = allComponents.get(i);

      label.setText(sc.getDisplayName() + "：");

      if (sc.getType().equals("TEXT")) {
        jcom = new UFCTextField();

        ((UFCTextField) jcom).setDisplayName(sc.getDisplayName());
        ((UFCTextField) jcom).setProperty(sc.getProprety());
        ((UFCTextField) jcom).setRequired(sc.isRequired());
        ((UFCTextField) jcom).setCadName(sc.getCadName());

      } else if (sc.getType().equals("LOV")) {
        jcom = new UFCComboBox();

        ((UFCComboBox) jcom).setDisplayName(sc.getDisplayName());
        ((UFCComboBox) jcom).setProperty(sc.getProprety());
        ((UFCComboBox) jcom).setRequired(sc.isRequired());
        ((UFCComboBox) jcom).setCadName(sc.getCadName());

        for (int x = 0; x < sc.getLovlist().size(); x++) {
          ((UFCComboBox) jcom).addItem(sc.getLovlist().get(x));
        }
      } else if (sc.getType().equals("TEXTArea")) {

        jcom = new UFCTextArea();

        ((UFCTextArea) jcom).setDisplayName(sc.getDisplayName());
        ((UFCTextArea) jcom).setProperty(sc.getProprety());
        ((UFCTextArea) jcom).setRequired(sc.isRequired());
        ((UFCTextArea) jcom).setCadName(sc.getCadName());
        //jcom.isOpaque();
      }
      //if(i%2==0){

      label.setBounds(new Rectangle(10, (i + 20) + (i * 30), 90, 22));
      jcom.setBounds(new Rectangle(100, (i + 20) + (i * 30), 100, 22));
      /*}else{
        label.setBounds(new Rectangle(10, (i+40)+(i*15), 90, 22));
        jcom.setBounds(new Rectangle(100, (i+40)+(i*15), 100, 22));
      }*/
      queryPanel.add(label);
      queryPanel.add(jcom);
    }
    queryPanel.updateUI();
  }

  private void okBtnActionPerformed(java.awt.event.ActionEvent evt) {
    if (wsObjTable.getSelectedRowCount() == 0) {
      JOptionPane.showMessageDialog(this, "请从表格中选择要返回的数据！", "提示",
          JOptionPane.INFORMATION_MESSAGE);
      return;
    }
    // 将选中的数据放到Map中
    int row = wsObjTable.getSelectedRow();
    WorkspaceTableModel tableModel = (WorkspaceTableModel) wsObjTable
        .getModel();
    String itemId = tableModel.getObjectId(row);
    for (int i = 0; i < wsObjTable.getColumnCount(); i++) {
      int realIndex = wsObjTable.convertColumnIndexToModel(i);
      String value = (String) wsObjTable.getValueAt(row, realIndex);
      String colName = wsObjTable.getColumnName(realIndex);
      if (colName.equals("对象类型")) {
        if (!value.endsWith("Revision")) {
          JOptionPane.showMessageDialog(this, "请选择 Revision对象！",
              "提示", JOptionPane.INFORMATION_MESSAGE);
          return;
        }
      } else if (colName.equals("对象名称")) {
        //  String id=value.substring(0,value.indexOf("/"));
        Map<String, String> propNames = new HashMap<String, String>();
        try {
          WSObject[] itemObj = tcop.queryObjectsBySavedQuery(
              "零组件 ID", new String[] { itemId });
          WSObject itemRev = tcop.getItemRevision(itemObj[0], "Last");
          String itemName = itemRev.getName();
          propNames = itemProps.get(partType);
          WSObject wsObj = tcop.getItemRevisionInfo(itemRev,
              propNames);
          Map<String, String> attrib = wsObj.getAttributes();

          boolean okflag = true;
          if (attrib.get("材料种类").equals("金属材料")) {
            int ok = JOptionPane.showConfirmDialog(this,
                "您所选为金属材料，是否填写材料规格", "提示",
                JOptionPane.YES_NO_OPTION);
            if (ok != 0) {
              okflag = false;
            }
          }

          backfillCADDrawing(attrib, itemId, itemName, "零部件", okflag);
        } catch (TCOperationException e) {
          e.printStackTrace();
        }
      }
    }
    frame.dispose();
  }

  private void backfillCADDrawing(Map<String, String> infos, String id,
      String itemname, String type, boolean okflag) {
    // 修改请求中的信息

    out.setFuncID("QueryMat");
    out.setResult("true");
    out.setDesc("Backfill CAD Drawing.");
    Element object2 = out.getObjectElement();
    //object2.addAttribute("type", type);
    Element props = object2.addElement("properties");

    Element root = this.requestBody;

    Element object = root.element("body");//.element("object");

    String key = "";
    Set<String> keys = infos.keySet();
    Object[] ks = keys.toArray();
    Element pro = props.addElement("property");
    pro.addAttribute("name", "材料编码");
    pro.addAttribute("value", id);
    Element proname = props.addElement("property");
    if (infos.get("材料简称").equals("")) {

      proname.addAttribute("name", "材料名称");
      proname.addAttribute("value", itemname);
    } else {
      proname.addAttribute("name", "材料名称");
      proname.addAttribute("value", infos.get("材料简称"));
    }
    for (int i = 0; i < ks.length; i++) {
      Element pro1 = props.addElement("property");
      String name = ks[i].toString();
      if (name.equals("重量")) {
        name = "质量";
      }
      pro1.addAttribute("name", name);
      pro1.addAttribute("value", infos.get(ks[i].toString()));
    }
    String matdown = infos.get("材质标准");
    String matGG = infos.get("材料规格");
    String matg1 = infos.get("材质规格");
    String matstarded = infos.get("材料标准");
    String matUp = matg1 + " " + matGG + " " + matstarded;
    if (!okflag) {
      matUp = matg1 + " " + matstarded;
    }
    if (infos.get("材料种类").equals("非金属材料")) {
      matdown = infos.get("材料标准");
      matUp = matGG;
    } else if (infos.get("材料种类").equals("材质")) {
      matUp = matg1;
    }
    Element promatup = props.addElement("property");
    promatup.addAttribute("name", "材料上标");
    promatup.addAttribute("value", matUp);

    Element promatdown = props.addElement("property");
    promatdown.addAttribute("name", "材料下标");
    promatdown.addAttribute("value", matdown);

    try {
      // 将CAD信息打印出来
      out.sendResultToUI();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private void searchBtnActionPerformed() {
    Thread thread = new Thread(new Runnable() {
      public void run() {
        if (waitDlg != null)
          waitDlg.dispose();

        waitDlg = new WaitDialog(frame, false);
        waitDlg.setMessage("正在进行操作，请稍候……");
        frame.setCursor(Cursor.WAIT_CURSOR);
        searchWorkspace();
        frame.setCursor(Cursor.DEFAULT_CURSOR);
        waitDlg.dispose();
      }
    });
    thread.start();
  }

  /**
   * 清除所有搜索条件
   * @param evt
   */
  private void clrBtnActionPerformed(java.awt.event.ActionEvent evt) {
    //
    clrqyPanel(queryPanel);
  }

  /**
   * 清除queryPanel上所有控件的内容
   * @param queryPanel2
   */
  private void clrqyPanel(JPanel queryPanel) {
    Component[] coms = queryPanel.getComponents();
    for (int i = 0; i < coms.length; i++) {
      if (coms[i] instanceof UFCTextField) {
        UFCTextField uta = (UFCTextField) coms[i];
        if (!uta.getText().equals("") || uta.getText() != null) {
          uta.setText("");
        }
      } else if (coms[i] instanceof UFCComboBox) {
        UFCComboBox ucb = (UFCComboBox) coms[i];
        ucb.setSelectedItem("");
      }
    }

  }

  private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {
    // 关闭按钮
    int r = JOptionPane.showConfirmDialog(this, "您尚未完成操作，确认退出？", "提示",
        JOptionPane.YES_NO_CANCEL_OPTION);
    if (r == JOptionPane.OK_OPTION) {
      //  System.out.println("close Window");
      closeWindow();
    } else
      return;
  }

  /**
   * 根据查询类型查询对象
   */
  protected void searchWorkspace() {

    Map<String, String> criterias = collectqypanelPros(queryPanel, partType);

    for (Iterator<String> keyItor = criterias.keySet().iterator(); keyItor
        .hasNext();) {
      String key = keyItor.next();
      String value1 = criterias.get(key);
    }
    String qyType = "__Cetc20Mat Revision";
    foundObjs = null;
    try {
      foundObjs = tcop.queryObjectsBySavedQuery(qyType, criterias);
      if (foundObjs != null) {
        Map<String, String> propertyNames = new HashMap<String, String>();
        propertyNames.put("材料简称", "az_MaterialShortName");
        propertyNames.put("材质规格(旧)", "az_MaterialOldSpec");
        propertyNames.put("材料规格", "az_StuffSpecification");
        propertyNames.put("材质规格", "az_MaterialSpecification");
        propertyNames.put("材质标准", "az_MaterialStandardNo");
        propertyNames.put("材料标准", "az_StuffStandardNo");
        WSObject[] forms = new WSObject[foundObjs.length];
        Map<String, String> attris = null;
        for (int i = 0; i < foundObjs.length; i++) {
          WSObject obj = foundObjs[i];
          WSObject form = tcop
              .getItemRevisionInfo(obj, propertyNames);
          attris = form.getAttributes();
          forms[i] = form;
        }
        tableModel = new WorkspaceTableModel(foundObjs, forms, attris,
            "Item");
        wsObjTable.setModel(tableModel);
        wsObjTable.validate();
        TableRowSorter sorter = new TableRowSorter(wsObjTable
            .getModel());
        wsObjTable.setRowSorter(sorter);
        wsObjTable.updateUI();
      }

    } catch (TCOperationException e) {
      JOptionPane.showMessageDialog(this, "查询系统对象时发生错误，错误原因如下：\n"
          + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
      return;
    }
    // 刷新表格的内容
    /*tableModel = new WorkspaceTableModel(foundObjs);
    wsObjTable.setModel(tableModel);
    wsObjTable.validate();
    wsObjTable.updateUI();
     */
  }

  private void setTableMouseListener() {

    wsObjTable.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (MouseEvent.BUTTON1 == e.getButton()
            && 1 == e.getClickCount()) {
          int row = wsObjTable.getSelectedRow();

          for (int i = 0; i < wsObjTable.getColumnCount(); i++) {
            int realIndex = wsObjTable.convertColumnIndexToModel(i);
            String value = (String) wsObjTable.getValueAt(row,
                realIndex);
            String colName = wsObjTable.getColumnName(realIndex);
            if (colName.equals("对象类型")) {
              if (!value.endsWith("Revision")) {
                //  JOptionPane.showMessageDialog(this, "请选择 Revision对象！", "提示", JOptionPane.INFORMATION_MESSAGE);
                return;
              } else {

              }
            } else if (colName.equals("对象名称")) {
              String id = value.substring(0, value.indexOf("/"));
              Map<String, String> propNames = new HashMap<String, String>();
              try {
                //ModelObject[] itemObj = new ObjectOperation().findObjectsBySavedQuery("零组件 ID", new String[]{id});
                WSObject[] itemObj = tcop
                    .queryObjectsBySavedQuery("零组件 ID",
                        new String[] { id });
                WSObject itemRev = tcop.getItemRevision(
                    itemObj[0], "Last");
                String itemName = itemRev.getName();
                propNames = itemProps.get(partType);
                WSObject wsObj = tcop.getItemRevisionInfo(
                    itemRev, propNames);
                Map<String, String> attrib = wsObj
                    .getAttributes();
                for (Iterator<String> keyItor = attrib.keySet()
                    .iterator(); keyItor.hasNext();) {
                  String key = keyItor.next();
                  String value1 = attrib.get(key);
                }

                fill2prosPanel(attrib, itemName);
              } catch (TCOperationException e1) {
                e1.printStackTrace();
              }
            }
          }
        }
      }
    });
  }

  /**
   * 向属性面板添加属性
   * @param attrib
   * @param itemName
   */

  private void fill2prosPanel(Map<String, String> attrib, String itemName) {
    initProsPanel("Cetc20Mat.xml", attrib, itemName);

    System.out.println("=========>添加属性面板成功");

  }

  /**
   * 获得查询条件界面中的属性
   * @param qyPanel 查询条件界面的控件
   * @param parttype 所查询的零部件类型
   */
  private Map<String, String> collectqypanelPros(JPanel qyPanel,
      String parttype) {
    Map<String, String> pros = new HashMap<String, String>();
    Component[] coms = qyPanel.getComponents();
    for (int i = 0; i < coms.length; i++) {
      if (coms[i] instanceof UFCTextField) {
        UFCTextField uta = (UFCTextField) coms[i];
        if (uta.getProperty() != null && !uta.getProperty().equals("")
            && uta.isRequired()) {
          String value = uta.getText();
          if (!value.trim().equals("") && value != null) {
            pros.put(uta.getDisplayName(), "*" + value + "*");
          }
        }
      } else if (coms[i] instanceof UFCComboBox) {
        UFCComboBox ucb = (UFCComboBox) coms[i];
        if (ucb.getProperty() != null && !ucb.getProperty().equals("")
            && ucb.isRequired()) {

          if (ucb.getSelectedItem() != null
              && !ucb.getSelectedItem().equals("")) {
            String value = ucb.getSelectedItem().toString();
            pros.put(ucb.getDisplayName(), "*" + value + "*");
          }
        }
      }
    }
    return pros;

  }

  private void closeWindow() {
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

  public Element getRequestBody() {
    return requestBody;
  }

  /**
   * @param requestBody the requestBody to set
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
   * @param out the out to set
   */
  public void setOut(AdaptorWriter out) {
    this.out = out;
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton cancelBtn;
  private javax.swing.JButton clrBtn;
  private javax.swing.JPanel comboxPanel;
  private javax.swing.JTextField jComboBox1;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JPanel jPanel3;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JSplitPane jSplitPane1;
  private javax.swing.JSplitPane jSplitPane2;
  private javax.swing.JTable wsObjTable;
  private javax.swing.JButton okBtn;
  private javax.swing.JPanel prosPanel;
  private javax.swing.JPanel queryPanel;
  private javax.swing.JButton searchBtn;
  // End of variables declaration//GEN-END:variables

}
