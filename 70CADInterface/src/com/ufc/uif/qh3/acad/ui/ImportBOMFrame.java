/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ImportBOMFrame.java
 *
 * Created on 2008-9-23, 10:21:27
 */

package com.ufc.uif.qh3.acad.ui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.dom4j.Document;
import org.dom4j.Element;

import com.ufc.uif.base_adaptor.AdaptorWriter;
import com.ufc.uif.qh3.acad.operation.ParseRequestXML;
import com.ufc.uif.qh3.acad.operation.SaveDrawingOperation;
import com.ufc.uif.qh3.acad.operation.Util;
import com.ufc.uif.qh3.acad.util.UFCComboBox;
import com.ufc.uif.tccommunicationimpl.operation.TCBOMOperation;
import com.ufc.uif.tccommunicationimpl.operation.TCObjOperation;
import com.ufc.uif.tccommunicationimpl.operation.TCObjectOperation;
import com.ufc.uif.tccommunicationimpl.operation.TCStructureOperation;
import com.ufc.uif.tcuacommunication.objects.WSObject;
import com.ufc.uif.tcuacommunication.operation.ITCObjectOperation;
import com.ufc.uif.tcuacommunication.operation.ITCStructureOperation;
import com.ufc.uif.tcuacommunication.operation.ITCTCBOMOperation;
import com.ufc.uif.tcuacommunication.operation.ITCTCObjOperation;
import com.ufc.uif.tcuacommunication.operation.exception.TCOperationException;

/**
 * 
 * @author Administrator
 */
public class ImportBOMFrame extends JFrame {// _UIFDialog
  private static final long serialVersionUID = -244179442381364805L;

  // private final static int MAT_COL = 6;
  // private final static int WEIGHT_COL = 7;
  // private final static int TOTAL_WEIGHT_COL = 8;
  private final static int ITEMID_COL = 1;
  private final static int DRAWINGNO_COL = 2;
  private final static int NAME_COL = 4;
  private final static int QUANTITY_COL = 5;
  private final static int BL_UOM_COL = 6;

  private Element requestBody;
  private AdaptorWriter out;
  /** BOM中新建的Item对象保存到的文件夹 */
  private WSObject folder;
  /** 装配对象的BOM View 对象 */
  private WSObject view;
  /** 装配对象 */
  private WSObject itemObj;
  private String itemid;
  private String itemname;
  /** 装配对象的ItemRevisioin对象 */
  private WSObject rootItemRev;
  /** 装配对象下的数据集对象 */
  private WSObject datasetObj;
  private SaveDrawingOperation sdo;
  private ITCObjectOperation op;
  private ITCTCObjOperation tcop;
  private ITCStructureOperation so;
  private ITCTCBOMOperation bom;
  /** 记录TC中不存在的对象 */
  private Map<String, WSObject> nonExistItems;
  /** 记录不存在Dataset的对象 */
  private Map<String, WSObject> nonDSItems;
  /** 记录TC中已存在的对象 */
  private Map<String, WSObject> existItems;
  /** 记录TC中不存在对象的请求信息 */
  private List<Map<String, String>> creatingItemInfo;
  /** 记录不存在Dataset对象的请求信息 */
  private List<Map<String, String>> creatingDatasetInfo;
  private List<Map<String, String>> docList;
  private List<Map<String, String>> subObjs;
  /** 排序后的明细请求信息 */
  private List<Map<String, String>> sortedSubObjs;
  /** 记录TC中不存在对象的ItemId */
  private List<String> nonItemIds;
  /** 记录不存在Dataset的对象的ItemId */
  private List<String> nonDatasetIds;
  /** 记录图纸数据与TC中不一致的对象的ItemId，及不一致的属性名称 */
  private Map<String, List<String>> propsWrongPart;
  /** 记录图纸数据与TC中不一致的对象的ItemId */
  private List<String> propWrongItemId;
  /** 记录已经更新过的对象的ItemId */
  private List<String> updatedIds;
  /** 要获取的REV上的属性名称 */
  // private static Map<String, Map<String, String>> itemProps = new
  // HashMap<String, Map<String, String>>();
  /** 保存ItemRevisioin上的属性 */
  private Map<String, Map<String, String>> itemRevInfo = new HashMap<String, Map<String, String>>();
  /** BOM创建是否成功标志 */
  private boolean success;
  /** JTable下TextArea中显示的信息 */
  private String message;
  /** BOM中是否有采购件不存在 */
  private boolean hasPurch;
  /** BOM中是否有通用件不存在 */
  // boolean hasGeneral;
  /** BOM中是否有自制件编码不合规范的信息存在 */
  private boolean hasSelfPart;
  /** BOM中是否有属性不一致的问题 */
  private boolean hasAttribsWrong;
  /* 是否存在序号不一致但是编码号一致 */
  private boolean hasSameProNo;
  /* 判断是否是废弃状态 */
  // boolean hasUseless;
  // private Map<String, String> propsMap = new HashMap<String, String>();

  /** 进度条对话框 */
  private StateDialog stateDlg;
  /** 转换数字格式 */
  DecimalFormat numFormat = new DecimalFormat("#0.###");
  /** 记录更新后的总重信息 **/
  // private String totalWeight = "";
  // 判断输入的数量是否正确
  private boolean qualityStatus = false;
  /*
   * private String[] values_part_type = new String[] { "产品", "一级部件", "零组件" };
   * private String[] values_Yes_No = new String[] { "是", "否" }; private
   * String[] values_crux = new String[] { "A", "B", "非" };
   */
  // private String[] values_GZ = new String[]{"一般件","关键件","重要件"};
  private String[] values_GZ = new String[] { "", "G", "Z" };
  private static Map<String, String> GZlov = new HashMap<String, String>();
  static {
    GZlov.put("", "一般件");
    GZlov.put("G", "关键件");
    GZlov.put("Z", "重要件");
  }
  private StringBuilder sBuilder = null;

  public ImportBOMFrame(Element requestBody, AdaptorWriter out) {
    System.out.println("207XTCAD----ImportBOMFrame-----------ImportBOMFrame");
    this.requestBody = requestBody;
    this.out = out;
    sdo = new SaveDrawingOperation();
    op = new TCObjectOperation();
    tcop = new TCObjOperation();
    so = new TCStructureOperation();
    bom = new TCBOMOperation();
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    initDialog();
  }

  public ImportBOMFrame(WSObject item, WSObject datasetObj, WSObject folder, String itemid, String itemName, List<Map<String, String>> docList, List<Map<String, String>> subObjs,
      Element requestBody, AdaptorWriter out) {
    System.out.println("207XTCAD----ImportBOMFrame-----------ImportBOMFrame");
    this.requestBody = requestBody;
    this.out = out;
    sdo = new SaveDrawingOperation();
    op = new TCObjectOperation();
    tcop = new TCObjOperation();
    so = new TCStructureOperation();
    bom = new TCBOMOperation();
    this.itemObj = item;
    this.datasetObj = datasetObj;
    this.folder = folder;
    this.docList = docList;
    this.subObjs = subObjs;
    this.itemid = itemid;
    this.itemname = itemName;
    // setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setTitle("导入BOM");
    setAlwaysOnTop(true);
    /*
     * addWindowListener(new java.awt.event.WindowAdapter() { public void
     * windowClosing(java.awt.event.WindowEvent evt) { closeWindow(); } });
     */
    initDialog();
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed"
  // desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    System.out.println("207XTCAD----ImportBOMFrame-----------initComponents");
    jPanel2 = new javax.swing.JPanel();
    jPanel1 = new javax.swing.JPanel();
    drawingNoL = new javax.swing.JLabel();
    drawingNoF = new javax.swing.JTextField();
    drawingNameL = new javax.swing.JLabel();
    drawingNameF = new javax.swing.JTextField();
    jButtonCancel = new javax.swing.JButton();
    jButtonOK = new javax.swing.JButton();
    jScrollPane4 = new javax.swing.JScrollPane();
    jTAOutput = new javax.swing.JTextArea();
    jScrollPane1 = new javax.swing.JScrollPane();
    jTableBOM = new javax.swing.JTable();
    drawingNoL2 = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    jLabel3 = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();
    jLabel5 = new javax.swing.JLabel();
    jLabel6 = new javax.swing.JLabel();
    jPanel3 = new javax.swing.JPanel();
    jLabel1 = new javax.swing.JLabel();
    jScrollPane2 = new javax.swing.JScrollPane();
    txa_Output = new javax.swing.JTextArea();
    btn_Update = new javax.swing.JButton();
    jButton1 = new javax.swing.JButton();

    // setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    jPanel2.setBackground(new java.awt.Color(226, 245, 252));
    jPanel2.setPreferredSize(new java.awt.Dimension(900, 600));

    jPanel1.setBackground(new java.awt.Color(241, 250, 255));
    jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(144, 158, 215), 1, true));

    drawingNoL.setText("图号");

    drawingNameL.setText("名称");

    org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
        jPanel1Layout.createSequentialGroup().add(67, 67, 67).add(drawingNoL).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(drawingNoF, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE).add(116, 116, 116).add(drawingNameL)
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(drawingNameF, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE).add(19, 19, 19)));
    jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
        org.jdesktop.layout.GroupLayout.TRAILING,
        jPanel1Layout
            .createSequentialGroup()
            .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(drawingNoL)
                .add(drawingNoF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(drawingNameL)
                .add(drawingNameF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
            .addContainerGap()));

    jButtonCancel.setText("取消");
    jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonCancelActionPerformed(evt);
      }
    });

    jButtonOK.setText("导入BOM");
    jButtonOK.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonOKActionPerformed(evt);
      }
    });

    jTAOutput.setColumns(20);
    jTAOutput.setRows(2);
    jScrollPane4.setViewportView(jTAOutput);
    jTableBOM.addMouseListener(new MouseListener() {

      @Override
      public void mouseClicked(MouseEvent e) {
        
        if (e.getClickCount() == 2) {
          JFrame f = new JFrame("资源库调用");
          f.setSize(new Dimension(300, 200));
          f.setLocationRelativeTo(ImportBOMFrame.this);
          f.setAlwaysOnTop(true);
          f.setVisible(true);
          System.out.println("-------------hello World!!---------------");
        } else {
          
        }
      }

      @Override
      public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub

      }

      @Override
      public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

      }

      @Override
      public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

      }

      @Override
      public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

      }

    });
    jTableBOM.setBackground(new java.awt.Color(241, 250, 255));
    // jTableBOM.setModel(new javax.swing.table.DefaultTableModel(
    // new Object [][] {
    // {null, null, null, null, null, null, null},
    // {null, null, null, null, null, null, null},
    // {null, null, null, null, null, null, null},
    // {null, null, null, null, null, null, null},
    // {null, null, null, null, null, null, null}
    // },
    // new String [] {
    // "序号", "图号", "名称", "数量", "材料", "标准号", "备注"
    // }
    // ) {
    // Class[] types = new Class [] {
    // java.lang.String.class, java.lang.String.class,
    // java.lang.String.class, java.lang.String.class,
    // java.lang.String.class, java.lang.String.class,
    // java.lang.String.class
    // };
    // boolean[] canEdit = new boolean [] {
    // false, false, false, false, false, false, false
    // };
    //
    // public Class getColumnClass(int columnIndex) {
    // return types [columnIndex];
    // }
    //
    // public boolean isCellEditable(int rowIndex, int columnIndex) {
    // return canEdit [columnIndex];
    // }
    // });
    jScrollPane1.setViewportView(jTableBOM);
    // 设置Table单行选取模式并添加行选择监听事件
    jTableBOM.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    jTableBOM.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent event) {
        jTableBOMvalueChanged(event);
      }
    });

    drawingNoL2.setText("表示Teamcenter中尚未创建该对象");

    jLabel2.setText("表示该对象没有对应的Dataset对象");

    jLabel3.setBackground(new java.awt.Color(255, 190, 49));
    jLabel3.setForeground(new java.awt.Color(255, 190, 49));
    jLabel3.setOpaque(true);

    jLabel4.setText("表示该对象的属性与TC中的不一致");

    jLabel5.setBackground(new java.awt.Color(144, 238, 144));
    jLabel5.setForeground(new java.awt.Color(144, 238, 144));
    jLabel5.setOpaque(true);

    jLabel6.setBackground(new java.awt.Color(189, 183, 107));
    jLabel6.setForeground(new java.awt.Color(189, 183, 107));
    jLabel6.setOpaque(true);

    jPanel3.setBackground(new java.awt.Color(241, 250, 255));

    jLabel1.setFont(new java.awt.Font("宋体", 1, 14));
    jLabel1.setText("Teamcenter数据库数据");

    txa_Output.setColumns(20);
    txa_Output.setRows(5);
    jScrollPane2.setViewportView(txa_Output);

    btn_Update.setText("更新物料信息");
    btn_Update.setVisible(false);
    btn_Update.setName("btn_Update"); // NOI18N
    btn_Update.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btn_UpdateActionPerformed(evt);
      }
    });

    jButton1.setText("反写到图纸");
    jButton1.setVisible(false);
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        // jButton1ActionPerformed(evt);
        applyBOMInfo();
      }
    });

    org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout
        .setHorizontalGroup(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
            jPanel3Layout
                .createSequentialGroup()
                .add(jPanel3Layout
                    .createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING,
                        jPanel3Layout.createSequentialGroup().addContainerGap().add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE))
                    .add(jPanel3Layout.createSequentialGroup().add(29, 29, 29).add(btn_Update, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE).add(18, 18, 18)
                        .add(jButton1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(11, 11, 11))
                    .add(jPanel3Layout.createSequentialGroup().addContainerGap().add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)))
                .addContainerGap()));
    jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
        jPanel3Layout.createSequentialGroup().add(18, 18, 18).add(jLabel1).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
            .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
            .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER, false).add(btn_Update).add(jButton1)).addContainerGap()));

    jPanel3Layout.linkSize(new java.awt.Component[] { btn_Update, jButton1 }, org.jdesktop.layout.GroupLayout.VERTICAL);

    org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
        org.jdesktop.layout.GroupLayout.TRAILING,
        jPanel2Layout
            .createSequentialGroup()
            .addContainerGap()
            .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 622, Short.MAX_VALUE)
                .add(jScrollPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 622, Short.MAX_VALUE))
            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
            .add(jPanel2Layout
                .createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(org.jdesktop.layout.GroupLayout.LEADING,
                    jPanel2Layout.createSequentialGroup().add(jLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jLabel2).add(33, 33, 33))
                .add(jPanel2Layout.createSequentialGroup().add(jLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(drawingNoL2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE))
                .add(org.jdesktop.layout.GroupLayout.LEADING,
                    jPanel2Layout
                        .createSequentialGroup()
                        .add(jPanel2Layout
                            .createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(org.jdesktop.layout.GroupLayout.LEADING,
                                jPanel2Layout.createSequentialGroup().add(33, 33, 33).add(jButtonOK, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                                    .add(22, 22, 22).add(jButtonCancel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE))
                            .add(org.jdesktop.layout.GroupLayout.LEADING,
                                jPanel2Layout.createSequentialGroup()
                                    .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jLabel4))).add(39, 39, 39))).addContainerGap()));
    jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
        jPanel2Layout
            .createSequentialGroup()
            .addContainerGap()
            .add(jPanel2Layout
                .createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                .add(jPanel2Layout.createSequentialGroup()
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 357, Short.MAX_VALUE))
                .add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .add(jPanel2Layout
                .createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(jPanel2Layout
                    .createSequentialGroup()
                    .add(18, 18, 18)
                    .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER, false)
                        .add(jLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 15, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel2))
                    .add(14, 14, 14)
                    .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER)
                        .add(jLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 15, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        // .add(drawingNoL2,
                        // org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        // org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        // Short.MAX_VALUE))
                        .add(drawingNoL2))
                    .add(18, 18, 18)
                    .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER)
                        .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 15, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel4))
                    .add(18, 18, 18)
                    .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jButtonOK, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(jButtonCancel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .add(jPanel2Layout.createSequentialGroup().addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                    .add(jScrollPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE))).addContainerGap()));

    getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void jButtonOKActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonOKActionPerformed
    importBOMInfo2();
  }// GEN-LAST:event_jButtonOKActionPerformed

  /**
   * 选中表格中一行数据时，对比该行数据是否与TC中数据一致
   * 
   * @author Liugz
   * @create on 2009-8-8 This project for CAD_Concrete
   * @param event
   */
  private void jTableBOMvalueChanged(ListSelectionEvent event) {
    System.out.println("207XTCAD----ImportBOMFrame-----------jTableBOMvalueChanged");
    txa_Output.setText("");
    txa_Output.setText("正在查找对象，请稍候......");
    int rows = jTableBOM.getSelectedRow();
    String itemid = Util.convertNull(jTableBOM.getValueAt(rows, ITEMID_COL));
    // 如果该行对象在TC中不存在
    if (nonItemIds.contains(itemid)) {
      txa_Output.setText("该对象在Teamcenter中尚未创建！");
      btn_Update.setEnabled(false);
    } else if (updatedIds.contains(itemid)) {
      txa_Output.setText("该对象已被更新，请选择其他需要更新\n的对象！");
      btn_Update.setEnabled(false);
    } else if (propWrongItemId.contains(itemid)) {
      Map<String, String> formInfo = itemRevInfo.get(itemid);
      StringBuffer tcInfos = new StringBuffer();
      // 如果该行数据与TC中的数据不一致
      tcInfos.append("产品编码：").append(itemid).append("\n").append("代号：").append(formInfo.get("代号")).append("\n");
      txa_Output.setText(tcInfos.toString());
      btn_Update.setEnabled(true);
    } else {
      txa_Output.setText("该对象的产品编码和名称与Teamcenter中的数据一致。");
      btn_Update.setEnabled(false);
    }
  }

  /**
   * 点击“更新图纸”时，触发的事件，将表格中的数据更新
   * 
   * @author Liugz
   * @create on 2009-8-8 This project for CAD_Concrete
   * @param evt
   */
  private void btn_UpdateActionPerformed(java.awt.event.ActionEvent evt) {
    System.out.println("207XTCAD----ImportBOMFrame-----------btn_UpdateActionPerformed");
    int row = jTableBOM.getSelectedRow();

    String itemid = Util.convertNull(jTableBOM.getValueAt(row, ITEMID_COL));
    // if( Util.isSelfPart(itemid)&& itemid.length()!=17 ){
    // JOptionPane.showMessageDialog(this,
    // "改行的编码必须是17位", "提示",
    // JOptionPane.ERROR_MESSAGE);
    // jButtonOK.setEnabled(false);
    // jButton1.setEnabled(false);
    // return;
    // }
    if (!updatedIds.contains(itemid)) {
      String seq = Util.convertNull(jTableBOM.getValueAt(row, 0));
      List<String> attribsName = propsWrongPart.get(itemid);
      for (int col = 0; col < jTableBOM.getColumnCount(); col++) {
        String colName = jTableBOM.getColumnName(col);
        if (attribsName.contains(colName)) {
          Map<String, String> tcRevInfo = itemRevInfo.get(itemid);
          String tcValue = tcRevInfo.get(colName);
          jTableBOM.setValueAt(tcValue, row, col);
        }
      }

      // 更新后记录下更新对象的ItemId
      updatedIds.add(itemid);
      jTableBOM.updateUI();
      // 更新属性后，更新显示渲染器
      jTableBOM.setDefaultRenderer(String.class, new BOMTableRender(nonItemIds, nonDatasetIds, updatedIds, propsWrongPart));
      outputStatus("第 " + seq + " 行明细信息更新成功");
    }
    if (propWrongItemId.size() == updatedIds.size()) {
      if (hasPurch || hasSameProNo) {
        jButtonOK.setEnabled(false);
      } else {
        jButtonOK.setEnabled(true);
      }
    } else
      jButtonOK.setEnabled(false);

  }// GEN

  private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButtonCancelActionPerformed
    /*
     * try { // 取消签出 if(op.isCheckOut(datasetObj)){
     * op.cancelCheckout(datasetObj); }
     * 
     * closeWindow(); // 删除关系
     * op.deleteRelationships(sdo.getLastestItemRevision(itemObj), new
     * ModelObject[]{datasetObj}, "IMAN_specification");
     * 
     * // 删除对象 op.deleteObjects(new ModelObject[]{datasetObj}); } catch
     * (TCOperationException e) { e.printStackTrace(); }
     */

    /*
     * stateDlg = new StateDialog(this, false,
     * "正在向Teamcenter上传图纸，请稍候......"); Thread thread = new Thread(new
     * Runnable(){ public void run(){ try { // 上传图纸 setButtonStatus(false);
     * jTableBOM.setEnabled(false);
     * outputStatus("正在向Teamcenter上传图纸，请稍候......");
     * if(sdo.uploadFile(datasetObj, docList)){ stateDlg.dispose();
     * outputStatus("图纸上传成功"); outputStatus("完成操作，退出接口程序");
     * sendMessageToUser("图纸上传成功！", 1); closeWindow(); } } catch
     * (TCOperationException e) { stateDlg.dispose(); e.printStackTrace();
     * sendMessageToUser(e.getMessage(), 0); setButtonStatus(true);
     * jTableBOM.setEnabled(false); return ; } } }); thread.start();
     */

    closeWindow();

  }// GEN-LAST:event_jButtonCancelActionPerformed

  // Variables declaration - do not modify
  private javax.swing.JButton btn_Update;
  private javax.swing.JTextField drawingNameF;
  private javax.swing.JLabel drawingNameL;
  private javax.swing.JTextField drawingNoF;
  private javax.swing.JLabel drawingNoL;
  private javax.swing.JLabel drawingNoL2;
  private javax.swing.JButton jButton1;
  private javax.swing.JButton jButtonCancel;
  private javax.swing.JButton jButtonOK;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JPanel jPanel3;
  private javax.swing.JPanel jPanel4;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JScrollPane jScrollPane4;
  private javax.swing.JTextArea jTAOutput;
  private javax.swing.JTable jTableBOM;

  private javax.swing.JTextArea txa_Output;

  // End of variables declaration

  /**
   * 初始化窗口，将信息显示在界面上
   * 
   * @author Liugz
   * @create on 2008-12-22 This project for CYM
   * @param requestBody
   */
  private void initDialog() {
    System.out.println("207XTCAD----ImportBOMFrame-----------initDialog");
    message = "";
    success = false;
    view = null;
    rootItemRev = null;

    addWindowListener(new java.awt.event.WindowAdapter() {
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

    // 初始化对话框
    initComponents();

    // subObjs = ParseRequestXML.getSubObjsInfo(requestBody);

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
    pack();
    setVisible(true);

    drawingNoF.setEditable(false);
    drawingNameF.setEditable(false);
    drawingNoF.setText(itemid);
    drawingNameF.setText(itemname);

    // 对BOM信息按序号从小到大排序
    Map<String, Map<String, String>> tableMap = new HashMap<String, Map<String, String>>();
    float[] numbers = new float[subObjs.size()];
    float startNum = 0f;
    try {
      for (int i = 0; i < subObjs.size(); i++) {
        Map<String, String> obj = subObjs.get(i);
        String num = Util.convertNull(obj.get("序号"));

        if (null == num || num.trim().length() == 0) {
          startNum = (float) (startNum + 0.01);
          numbers[i] = startNum;
          tableMap.put(String.valueOf(startNum), obj);
        } else {
          numbers[i] = Float.valueOf(num);
          // 为使排序的float数组中的值 能够和map中的key值一一对应 需要做如下2次格式转换。
          tableMap.put(String.valueOf(Float.valueOf(num)), obj);
        }
      }
    } catch (NumberFormatException e) {
      sendMessageToUser("图纸明细中序号存在非数字字符！", 0);
      return;
    }

    Arrays.sort(numbers);
    sortedSubObjs = new ArrayList<Map<String, String>>();
    sBuilder = new StringBuilder();
    for (int k = 0; k < numbers.length; k++) {

      Map<String, String> obj1 = tableMap.get(String.valueOf(numbers[k]));
      String tmpStr = "";
      String unit = "";
      boolean isNum = false;

      // rongjw start 数量小数 单位
      if (obj1.get("数量") != null && !obj1.get("数量").equals("")) {

        // 整数
        if (obj1.get("数量").matches("\\d+")) {
          sortedSubObjs.add(tableMap.get(String.valueOf(numbers[k])));
        } else {
          if (obj1.get("数量").split(".").length > 2) {
            sBuilder.append(obj1.get("序号") + ",");
          }

          if (obj1.get("数量").endsWith("kg")) {
            tmpStr = obj1.get("数量").substring(0, obj1.get("数量").length() - 2);
            unit = "kg";
          } else if (obj1.get("数量").endsWith("ml")) {
            tmpStr = obj1.get("数量").substring(0, obj1.get("数量").length() - 2);
            unit = "ml";
          } else if (obj1.get("数量").endsWith("mm")) {
            tmpStr = obj1.get("数量").substring(0, obj1.get("数量").length() - 2);
            unit = "mm";
          } else if (obj1.get("数量").endsWith("g")) {
            tmpStr = obj1.get("数量").substring(0, obj1.get("数量").length() - 1);
            unit = "g";
          } else if (obj1.get("数量").endsWith("l")) {
            tmpStr = obj1.get("数量").substring(0, obj1.get("数量").length() - 1);
            unit = "l";
          } else if (obj1.get("数量").endsWith("m")) {
            tmpStr = obj1.get("数量").substring(0, obj1.get("数量").length() - 1);
            unit = "m";
          } else {
            isNum = true;
            sBuilder.append(obj1.get("序号") + ",");
          }

          if (isNum == false) {
            if (!tmpStr.matches("\\d+.\\d+")) {
              sBuilder.append(obj1.get("序号") + ",");
            } else {
              tableMap.get(String.valueOf(numbers[k])).put("数量", tmpStr);
              tableMap.get(String.valueOf(numbers[k])).put("度量单位", unit);
            }
          }

          sortedSubObjs.add(tableMap.get(String.valueOf(numbers[k])));
        }

      } else {
        sortedSubObjs.add(tableMap.get(String.valueOf(numbers[k])));
      }
      // rongjw end 数量小数 单位

      // sortedSubObjs.add(tableMap.get(String.valueOf(numbers[k])));
      //
      // //rongjw start 数量小数
      //
      // try {
      // Map<String, String> obj =
      // tableMap.get(String.valueOf(numbers[k]));
      // if (obj.get("数量") != null && obj.get("数量").matches("\\d+.\\d+"))
      // {
      //
      // sBuilder.append(obj.get("序号") + ",");
      // }
      //
      // } catch (NumberFormatException e) {
      // sendMessageToUser("图纸明细中序号存在非数字字符！", 0);
      // return;
      // }
      // //rongjw end 数量小数

    }
    // BOMTableModel elementModel = new BOMTableModel(subObjs);
    BOMTableModel elementModel = new BOMTableModel(sortedSubObjs);
    jTableBOM.setModel(elementModel);

    // rongjw start 数量是小数
    final UFCComboBox bl_uomComboBox = new UFCComboBox();

    String tempArray[] = sdo.getPreferenceValue("Qh3_bl_uom", "site");
    for (int j = 0; j < tempArray.length; j++) {
      bl_uomComboBox.addItem(tempArray[j]);
    }
    bl_uomComboBox.setEditable(true);

    // bl_uomComboBox.addItemListener(new ItemListener() {
    // public void itemStateChanged(ItemEvent e) {
    // if (e.getStateChange() == ItemEvent.SELECTED) {
    // String selectedValue = (String) bl_uomComboBox.getSelectedItem();
    // if ("个".equals(selectedValue) || selectedValue==null ||
    // "".equals(selectedValue)) {
    // sendMessageToUser("度量单位的值为“个”时，数量值只支持整数", 3);
    // }
    // }
    // }
    // });

    DefaultCellEditor comboEditor_bl_uom = new DefaultCellEditor(bl_uomComboBox);
    jTableBOM.getColumnModel().getColumn(BL_UOM_COL).setCellEditor(comboEditor_bl_uom);
    // rongjw end 数量是小数

    // JComboBox combo_unit = new JComboBox();
    // combo_unit.setModel(new DefaultComboBoxModel(values_GZ));
    // DefaultCellEditor comboEditor_part_type = new
    // DefaultCellEditor(combo_unit);
    // jTableBOM.getColumnModel().getColumn(6).setCellEditor(comboEditor_part_type);

    // 初始化对话框
    stateDlg = new StateDialog(this, false, "正在查询Item和数据集信息，请稍候......");
    Thread initThread = new Thread(new Runnable() {
      public void run() {
        outputStatus("正在查询各零件信息，请稍候......");
        setButtonStatus(true);
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        queryItems();
        checkBOMViewRevision();
        showInfoToUser();
        // setButtonStatus(true);

        if (hasSameProNo)
          jButtonOK.setEnabled(false);

        setInitState();
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        stateDlg.dispose();

      }
    });
    initThread.start();

  }

  /**
   * 首先去查询各零件是否存在
   * 
   * @author Liugz
   * @create on 2008-12-23 This project for CYM
   */
  public void queryItems() {
    System.out.println("207XTCAD----ImportBOMFrame-----------queryItems");
    nonItemIds = new ArrayList<String>();
    nonDatasetIds = new ArrayList<String>();
    propsWrongPart = new HashMap<String, List<String>>();
    propWrongItemId = new ArrayList<String>();
    updatedIds = new ArrayList<String>();
    hasPurch = false;
    hasSelfPart = false;
    hasAttribsWrong = false;
    // 在系统中进行查找，对没有找到的对象并分为两类，标准件、非标准件
    creatingItemInfo = new ArrayList<Map<String, String>>();
    creatingDatasetInfo = new ArrayList<Map<String, String>>();
    nonExistItems = new HashMap<String, WSObject>();
    nonDSItems = new HashMap<String, WSObject>();
    existItems = new HashMap<String, WSObject>();

    // 获取图号
    try {
      for (int i = 0; i < jTableBOM.getRowCount(); i++) {

        String itemid = "";
        if (jTableBOM.getValueAt(i, ITEMID_COL) != null) {
          itemid = Util.convertNull(jTableBOM.getValueAt(i, ITEMID_COL).toString());
        } else {
          itemid = "";
        }

        String drawingno = "";
        if (jTableBOM.getValueAt(i, DRAWINGNO_COL) != null) {
          drawingno = Util.convertNull(jTableBOM.getValueAt(i, DRAWINGNO_COL).toString());
        } else {
          drawingno = "";
        }

        String name = "";
        if (jTableBOM.getValueAt(i, NAME_COL) != null) {
          name = Util.convertNull(jTableBOM.getValueAt(i, NAME_COL).toString());
        } else {
          name = "";
        }

        String quantity = "";
        if (jTableBOM.getValueAt(i, QUANTITY_COL) != null) {
          quantity = Util.convertNull(jTableBOM.getValueAt(i, QUANTITY_COL).toString());
        } else {
          quantity = "";
        }

        if (name.equals("") || quantity.equals("")) {
          sendMessageToUser("第" + (i + 1) + "行明细栏信息填写不完整，名称、数量不能为空，请检查后导入", 0);
          jButtonOK.setEnabled(false);
          jTableBOM.setRowSelectionInterval(i, i);
          return;
        }

        if ((drawingno.equals("") || !drawingno.toUpperCase().startsWith("AZ")) && itemid.equals("")) {
          sendMessageToUser("第" + (i + 1) + "行明细栏信息填写不完整，外购件ITEM_ID不能为空，请检查后导入", 0);
          jButtonOK.setEnabled(false);
          jTableBOM.setRowSelectionInterval(i, i);
          return;
        }

        // if(!quantity.matches("\\d+")) {
        // sendMessageToUser("数量信息输入错误，输入值应为整型", 0);
        // jButtonOK.setEnabled(false);
        // jTableBOM.setRowSelectionInterval(i, i);
        // return;
        // }

        // 判断编码号相同，但是序号不同的情况
        // for (int j = i + 1; j < jTableBOM.getRowCount(); j++) {
        // String newitemid = "";
        // if (jTableBOM.getValueAt(i, ITEMID_COL)!=null){
        // newitemid = Util.convertNull(jTableBOM.getValueAt(j,
        // ITEMID_COL).toString());
        // }else{
        // newitemid = "";
        // }
        //
        // String newitemname = "";
        // if (jTableBOM.getValueAt(i, NAME_COL)!=null){
        // newitemname = Util.convertNull(jTableBOM.getValueAt(j,
        // NAME_COL).toString());
        // }else{
        // newitemname = "";
        // }
        // //String newitemid = Util.convertNull(jTableBOM.getValueAt(j,
        // ITEMID_COL).toString());
        // //String newitemname =
        // Util.convertNull(jTableBOM.getValueAt(j,
        // NAME_COL).toString());
        // String newnumber = Util.convertNull(jTableBOM.getValueAt(j,
        // 0).toString());
        //
        // if (newitemid.equals(Util.convertNull(jTableBOM.getValueAt(i,
        // ITEMID_COL).toString())) //&&
        // newitemname.equals(Util.convertNull(jTableBOM.getValueAt(i,
        // NAME_COL).toString()))
        // && !newnumber.equals(Util.convertNull(jTableBOM.getValueAt(i,
        // 0).toString()))) {
        // JOptionPane.showMessageDialog(this, "第" + (i + 1) + "与第" + (j
        // + 1) + "行编码号相同但序号不同", "提示",
        // JOptionPane.ERROR_MESSAGE);
        // hasSameProNo = true;
        // jButtonOK.setEnabled(false);
        // break;
        // }
        // }

        // 查询所有类型的item
        WSObject obj = null;
        if (itemid != null && !itemid.equals("")) {
          obj = sdo.findAllItem(itemid);
        } else {
          obj = sdo.findAllItem(drawingno);
        }

        if (null == obj) {

          if (drawingno.equals("") || !drawingno.toUpperCase().startsWith("AZ")) {
            sendMessageToUser("未能在Teamcenter中找到对应的第" + (i + 1) + "行外购件信息，无法创建产品结构，请先创建所需的外购件 再进行导入！", 0);

            jButtonOK.setEnabled(false);
            jTableBOM.setRowSelectionInterval(i, i);
            return;
          }

          if (itemid != null && !itemid.equals("")) {
            nonItemIds.add(itemid);
            creatingItemInfo.add(sortedSubObjs.get(i));
            outputStatus("没有找到 产品编码 为 " + itemid + " 的零组件信息");
          } else {
            nonItemIds.add(drawingno);
            creatingItemInfo.add(sortedSubObjs.get(i));
            outputStatus("没有找到 产品编码 为 " + drawingno + " 的零组件信息");
          }

        } else {
          WSObject rev = tcop.getItemRevision(obj, "last");
          if (itemid != null && !itemid.equals("")) {
            existItems.put(itemid, rev);
            outputStatus("获得 产品编码 为 " + itemid + " 的零件信息");
          } else {
            existItems.put(drawingno, rev);
            outputStatus("获得 产品编码 为 " + drawingno + " 的零件信息");
          }

          outputStatus("正在获取 ItemRevisioin 对象的相关属性......");
        }
      }
    } catch (TCOperationException e1) {
      System.out.println("获取信息出错的itemid" + itemid);
      sendMessageToUser(e1.getMessage(), 0);
      return;
    } catch (NumberFormatException e1) {
      sendMessageToUser("数值转换时发生错误，请检查图纸明细中重量填写是否正确！", 0);
      return;
    }
    // 渲染表格
    // jTableBOM.setDefaultRenderer(String.class, new
    // BOMTableRender(nonItemIds));
    creatingDatasetInfo.addAll(creatingItemInfo);
    // 将属性不一致的对象的ItemId记录下来
    String[] wrongIds = propsWrongPart.keySet().toArray(new String[0]);
    propWrongItemId = Arrays.asList(wrongIds);
    jTableBOM.setDefaultRenderer(String.class, new BOMTableRender(nonItemIds, nonDatasetIds, propsWrongPart));
    jTableBOM.updateUI();
  }

  /**
   * 检查BOMView最新的Revision的检出状态
   * 
   * @author Liugz
   * @create on 2008-12-25 This project for CYM
   * @return
   */
  private WSObject checkBOMViewRevision() {
    System.out.println("207XTCAD----ImportBOMFrame-----------checkBOMViewRevision");
    // 获取BOMView
    /*
     * WSObject[] rootItemRevs = null; try { rootItemRevs =
     * op.getItemRevisions(itemObj, -1); } catch (TCOperationException e1) {
     * e1.printStackTrace(); System.out.println("\t获取Revision信息失败"); }
     */
    try {
      rootItemRev = tcop.getItemRevision(itemObj, "last");
    } catch (TCOperationException e) {
      e.printStackTrace();
      System.out.println("\t获取Revision信息失败");
    }

    try {
      view = so.getBVRbyBVType(rootItemRev, "view");
      if (null != view) {
        outputStatus("检查BOM View的检出状态......");
        if (!op.isCheckOut(view)) {
          outputStatus("指定的BOM View尚未检出");
        } else {
          if (!op.isCheckOutByUser(view)) {
            outputStatus("该BOM View已被其他用户检出");
            sendMessageToUser("其他用户可能正在编辑该BVR对象，您不能继续操作。", 1);
            jButtonOK.setEnabled(false);
          } else
            outputStatus("您已检出该BOM View");
        }
      }
    } catch (TCOperationException te) {
      sendMessageToUser("获取BOMView信息时发生错误，无法继续操作", 1);
      System.out.println("未能获取BOMView信息");
      jButtonOK.setEnabled(false);
      return null;
    }
    return view;
  }

  /**
   * @author Liugz
   * @create on 2009-6-26 This project for CAD_Concrete
   */
  protected void showInfoToUser() {
    System.out.println("207XTCAD----ImportBOMFrame-----------showInfoToUser");
    outputStatus("***************************************************");
    if (null != nonItemIds && nonItemIds.size() != 0) {
      int pCount = 0;
      int gCount = 0;
      int sCount = 0;
      StringBuffer nonPurchIds = new StringBuffer();
      StringBuffer nonGenIds = new StringBuffer();
      StringBuffer nonSelfIds = new StringBuffer();
      for (int i = 0; i < nonItemIds.size(); i++) {
        if (Util.isPurchProd(nonItemIds.get(i))) {
          pCount++;
          nonPurchIds.append(nonItemIds.get(i)).append(" , ");
          if ((pCount % 4) == 0) {
            nonPurchIds.append("\n");
          }
        } else if (!Util.isSelfPart(nonItemIds.get(i))) {
          sCount++;
          nonSelfIds.append(nonItemIds.get(i)).append(" , ");
          if ((sCount % 4) == 0) {
            nonSelfIds.append("\n");
          }
        }
      }
      if (hasPurch && !"".equals(nonPurchIds.toString())) {
        outputStatus("Teamcenter中不存在的采购件或标准件的产品编码：");
        outputStatus(nonPurchIds.toString());
        outputStatus("----------------------------------------------------");

      }
      // if(hasGeneral && !"".equals(nonGenIds.toString())){
      // outputStatus("Teamcenter中不存在的通用件 ItemId：");
      // outputStatus(nonGenIds.toString());
      // outputStatus("----------------------------------------------------");
      // }
      if (!"".equals(nonSelfIds.toString())) {
        outputStatus("以下零组件的 产品编码 不符合编码规范，不能进行创建操作：");
        outputStatus(nonSelfIds.toString());
        outputStatus("----------------------------------------------------");
      }
    }

    if (null != nonDatasetIds && nonDatasetIds.size() != 0) {
      StringBuffer tmpIds = new StringBuffer();
      for (int i = 1; i < nonDatasetIds.size() + 1; i++) {
        if (Util.isPurchProd(nonDatasetIds.get(i - 1))) {
          continue;
        }
        tmpIds.append(nonDatasetIds.get(i - 1)).append(" , ");
        if ((i % 4) == 0) {
          tmpIds.append("\n");
        }
      }
      if (!"".equals(tmpIds.toString())) {
        outputStatus("以下 零组件 对象没有图纸信息，未上传图纸信息：");
        outputStatus(tmpIds.toString());
        outputStatus("----------------------------------------------------");
      }
    }

    if (null != propsWrongPart && propsWrongPart.size() != 0) {
      outputStatus("以下对象的属性信息与TC中的属性不一致：");
      String[] ids = propsWrongPart.keySet().toArray(new String[0]);
      for (String id : ids) {
        List<String> propNames = propsWrongPart.get(id);
        outputStatus(id + " 对象上的 " + propNames.toString() + " 属性与TC中的属性不一致");
      }

      sendMessageToUser("明细栏中存在属性和TC中不一致的对象！", 1);

      // jButtonOK.setEnabled(false);
    }

    // rongjw start 度量单位
    if (sBuilder.length() > 0) {
      jButtonOK.setEnabled(false);
      sendMessageToUser("明细表中序号为：\n【" + sBuilder.toString().substring(0, sBuilder.length() - 1) + "】\n的行数量值格式错误", 1);
      outputStatus("明细表中序号为：\n【" + sBuilder.toString().substring(0, sBuilder.length() - 1) + "】\n的行数量值格式错误");

    }
    // rongjw end 度量单位
    outputStatus("***************************************************");
  }

  /**
   * @author Liugz
   * @create on 2009-8-11 This project for CAD_Concrete
   */
  protected void setInitState() {
    System.out.println("207XTCAD----ImportBOMFrame-----------setInitState");
    // if (null == propWrongItemId || propWrongItemId.size() == 0) {
    // btn_Update.setEnabled(false);
    // }

    // if (hasPurch && hasGeneral) {
    // jButtonOK.setEnabled(false);
    // sendMessageToUser(
    // "未能在Teamcenter中找到对应的 采购件 和 通用件 信息，无法创建产品结构，请先创建所需的 采购件 和 通用件 再进行导入！",
    // 1);
    // // jTableBOM.setEnabled(false);
    // } else
    if (hasPurch) {
      jButtonOK.setEnabled(false);
      sendMessageToUser("未能在Teamcenter中找到对应的 采购件或标准件 信息，无法创建产品结构，请先创建所需的 采购件或标准件 再进行导入！", 1);
      // jTableBOM.setEnabled(false);
    }
    // else if (hasGeneral) {
    // jButtonOK.setEnabled(false);
    // sendMessageToUser(
    // "未能在Teamcenter中找到对应的 通用件 信息，无法创建产品结构，请先创建所需的 通用件 再进行导入！", 1);
    // // jTableBOM.setEnabled(false);
    // }
    else if (hasSelfPart) {
      jButtonOK.setEnabled(false);
      sendMessageToUser("零組件的 产品编码 不符合编码规范，不能进行创建，无法创建产品结构", 1);
      // jTableBOM.setEnabled(false);
    } else if (hasAttribsWrong) {
      jButtonOK.setEnabled(false);
      sendMessageToUser("当前明细表中的属性信息与Teamcenter中的属性信息不一致，请修改正确后再进行保存操作", 1);
      // jTableBOM.setEnabled(false);
    }
  }

  /**
   * 点击导入BOM按钮时执行的操作
   * 
   * @author Liugz
   * @create on 2009-8-11 This project for CAD_Concrete
   */
  public void importBOMInfo2() {
    System.out.println("207XTCAD----ImportBOMFrame-----------importBOMInfo2");
    for (int i = 0; i < jTableBOM.getRowCount(); i++) {
      // 判断数量是否为空并且是否为数字
      // String sl = jTableBOM.getValueAt(i, QUANTITY_COL).toString();
      // System.out.println("si="+sl);
      // Pattern pattern = Pattern.compile("[0-9]*");
      // if ("".equals(sl) || !pattern.matcher(sl).matches()) {
      // JOptionPane.showMessageDialog(this, "数量不能为空并且必须是数字",
      // "提示",JOptionPane.ERROR_MESSAGE);
      // jButtonOK.setEnabled(false);
      // return;
      // }

      // if (jTableBOM.getValueAt(i, ITEMID_COL).toString().equals("")
      // || jTableBOM.getValueAt(i, NAME_COL).toString().equals("")
      // || jTableBOM.getValueAt(i, QUANTITY_COL).toString().equals("")) {
      // qualityStatus = true;
      // sendMessageToUser("明细栏信息填写不完整，产品编码、图样名称、数量不能为空，请检查后导入", 0);
      // jButtonOK.setEnabled(false);
      // return;
      // }
    }

    stateDlg = new StateDialog(this, false, "正在导入BOM信息，请稍候......");
    /*
     * Thread importThread = new Thread(new Runnable() { public void run() {
     * outputStatus("正在导入BOM信息，请稍候......"); setButtonStatus(false);
     * setCursor(new Cursor(Cursor.WAIT_CURSOR));
     * jTableBOM.setEnabled(false);
     * 
     * // LiugzTodo 验证输入信息 // 回填明细栏
     * stateDlg.setMessage("正在向CAD回填BOM信息，请稍候......"); backfillCADDrawing();
     * } }); importThread.start();
     */

    outputStatus("正在导入BOM信息，请稍候......");
    setButtonStatus(false);
    setCursor(new Cursor(Cursor.WAIT_CURSOR));
    jTableBOM.setEnabled(false);
    doOperation("succeed");
  }

  /**
   * 执行回填后的操作
   * 
   * @author Liugz
   * @create on 2009-3-4 This project for CAD_Concrete
   * @param status
   */

  // 改为由按钮事件调用 直接赋值为succeed
  public void doOperation(String status) {
    // 解析请求，查看状态
    System.out.println("207XTCAD----ImportBOMFrame-----------doOperation");
    if ("succeed".equals(status)) {
      if (null == stateDlg)
        stateDlg = new StateDialog(this, false, "正在导入BOM信息，请稍候......");
      Thread importThread = new Thread(new Runnable() {
        public void run() {
          // 导入BOM
          stateDlg.setMessage("正在导入BOM信息，请稍候......");
          outputStatus("正在导入BOM信息......");

          importBOMInfo();

          if (isSuccess()) {// 导入BOM操作成功
            outputStatus("BOM信息导入成功");
            try {
              // 上传图纸
              outputStatus("正在向Teamcenter上传图纸，请稍候......");
              stateDlg.setMessage("正在向Teamcenter上传图纸，请稍候......");

              // 判断是否需要更改命名,保证一个数据集下只能有一个命名的引用
              List<Map<String, String>> tempDocList = getLocalPathMap(datasetObj, ".dwg", docList.get(0).get("LocalPath"));
              // 判断是否需要更改命名,保证一个数据集下只能有一个命名的引用

              if (sdo.uploadFile(datasetObj, tempDocList)) {
                // if (sdo.uploadFile(datasetObj, docList)) {
                outputStatus("图纸上传成功，完成图纸保存操作");
                stateDlg.dispose();
                sendMessageToUser("图纸上传成功，完成图纸保存操作", 1);
                sendMessageToCAD("CloseWindow");
              }
            } catch (TCOperationException e) {
              stateDlg.dispose();
              e.printStackTrace();
              sendMessageToUser(e.getMessage(), 0);
              setButtonStatus(true);
              setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
              jTableBOM.setEnabled(false);
              return;
            }
            // stateDlg.dispose();
            // sendMessageToCAD("CloseWindow");
          } else {
            stateDlg.dispose();
            sendMessageToUser("导入BOM时发生错误，请重新操作。", 0);
            setButtonStatus(true);
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            jTableBOM.setEnabled(false);
            Thread.currentThread().interrupt();
            return;
          }
        }
      });
      importThread.start();
    } else if ("failed".equals(status)) {
      stateDlg.dispose();
      sendMessageToUser("向CAD回填信息时发生错误，请重新操作。", 0);
      setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
      jTableBOM.setEnabled(true);
      setButtonStatus(true);
      return;
    }
  }

  /**
   * @author Liugz
   * @throws NotLoadedException
   * @create on 2008-12-24 This project for CYM
   */
  private void importBOMInfo() {
    System.out.println("207XTCAD----ImportBOMFrame-----------importBOMInfo");
    List<Map<String, String>> itemRevAttribs2 = new ArrayList<Map<String, String>>();// 度量单位不为“个”时，先更新BOMLine上的度量单位，再更新数量值

    if (null != creatingItemInfo && creatingItemInfo.size() != 0) {
      WSObject[] ci = createItems();
      if (null == ci || ci.length == 0) {
        sendMessageToUser("创建Item时出现错误，请重新操作。", ERROR);
        return;
      }
    }

    // 为创建的Item对象创建Dataset，并上传图纸
    // if (!createDatasetsAndUploadFiles()) {
    // return;
    // }
    // 获取Revision

    for (Iterator itor = nonExistItems.keySet().iterator(); itor.hasNext();) {
      String itemid = (String) itor.next();
      WSObject rev = nonExistItems.get(itemid);
      existItems.put(itemid, rev);
    }

    // 创建BOM
    List<WSObject> itemRevs = new ArrayList<WSObject>();
    List<WSObject> itemRevs2 = new ArrayList<WSObject>();
    List<Map<String, String>> itemRevAttribs = new ArrayList<Map<String, String>>();
    outputStatus("正在获取BOM属性信息.......");
    for (int i = 0; i < subObjs.size(); i++) {
      // 获取属性信息
      Map<String, String> partMap = subObjs.get(i);
      String id = "";
      if (partMap.get("ITEM_ID") != null && !partMap.get("ITEM_ID").toString().equals("")) {
        id = partMap.get("ITEM_ID");
      } else {
        id = partMap.get("代号");
      }

      WSObject itemRev = existItems.get(id);
      Map<String, String> attributes = new HashMap<String, String>();
      Map<String, String> attributes2 = new HashMap<String, String>();
      String no = partMap.get("数量");

      // rongjw start 度量单位
      if (partMap.get("度量单位") == null || "".equals(partMap.get("度量单位")) || "个".equals(partMap.get("度量单位"))) {
        if (no.matches("\\d+")) {
          attributes.put("bl_quantity", no);
        }
      } else {// 度量单位不为“个”时，先更新BOMLine上的度量单位，再更新数量值
        if (no.matches("\\d+.\\d+")) {
          // gaott 保留3位
          if (no.length() > no.indexOf(".") + 3) {
            no = no.substring(0, no.indexOf(".") + 3);
          }
          itemRevs2.add(itemRev);
          attributes2.put("bl_quantity", no);
        }
      }
      // rongjw end 度量单位

      String unit = partMap.get("度量单位");

      /*
       * if (no.matches("\\d+")){ attributes.put("bl_quantity", no); }
       */
      String remark = partMap.get("备注");
      attributes.put("Qh3_Beizhu", remark);

      attributes.put("Qh3_Source", "CAD");
      attributes.put("bl_uom", unit);
      // attributes.put("PSE_config_demo",
      // Util.convertNull(partMap.get("备注")));
      // attributes.put("件号备注", partMap.get("序号"));
      // attributes.put("bl_sequence_no", partMap.get("序号"));
      itemRevs.add(itemRev);
      itemRevAttribs.add(attributes);
      if (attributes2.size() > 0) {
        itemRevAttribs2.add(attributes2);
      } else {
        itemRevAttribs2.add(attributes);
      }
    }
    outputStatus("获取BOM属性信息成功");
    try {
      // 判断BOM对象的签出状态
      boolean result = false;
      if (null != view) {
        if (!op.isCheckOut(view)) {
          if (op.checkOutObjects(new WSObject[] { view }, ""))
            outputStatus("检出BOM View");
          else {
            outputStatus("检出操作失败，未能检出对象，请重新操作");
            sendMessageToUser("检出操作失败，未能检出对象，请重新操作", 0);
            return;
          }
        }
        outputStatus("开始更新BOM View信息......");
        // Calendar c = (Calendar)
        // op.getPropertyOfObject(view,"last_mod_date", Calendar.class);
        // rongjw
        Calendar c = null;
        if (null == c)
          c = Calendar.getInstance();

        // result = bom.createPSBOM(rootItemRev, (ArrayList<WSObject>)
        // itemRevs, itemRevAttribs, "view");
        result = bom.createPSBOM(rootItemRev, (ArrayList<WSObject>) itemRevs, itemRevAttribs, "view", "Qh3_Source", "CAD");

        if (result) {
          outputStatus("BOM View 更新成功");
          if (!op.checkin(new WSObject[] { view }))
            outputStatus("签入对象时发生错误，对象保持可编辑状态");
        }
      } else {
        outputStatus("准备新建BOM View信息......");
        Calendar c = Calendar.getInstance();
        System.out.println(c.getTime());
        // result=bom.createPSBOM(TCUtil.modelObject2WSObject(rootItemRev),
        // itemRevs, itemRevAttribs, "view");
        result = bom.createPSBOM(rootItemRev, (ArrayList<WSObject>) itemRevs, itemRevAttribs, "view");

        if (result)
          outputStatus("BOM View 更新成功");
      }
      // rongjw start 数量小数
      if (itemRevAttribs2.size() > 0) {

        if (view == null) {
          view = so.getBVRbyBVType(rootItemRev, "view");
        }
        if (!op.isCheckOut(view)) {
          if (op.checkOutObjects(new WSObject[] { view }, ""))
            outputStatus("检出BOM View");
          else {
            outputStatus("检出操作失败，未能检出对象，请重新操作");
            sendMessageToUser("检出操作失败，未能检出对象，请重新操作", 0);
            return;
          }
        }
        outputStatus("开始更新BOM View信息......");
        Calendar c = null;
        if (null == c)
          c = Calendar.getInstance();
        result = bom.createPSBOM(rootItemRev, (ArrayList<WSObject>) itemRevs, itemRevAttribs2, "view", "Qh3_Source", "CAD");
        if (result) {
          outputStatus("BOM View 更新成功");
        }
      }
      // rongjw end 数量小数
      setSuccess(result);
    } catch (TCOperationException e) {
      e.printStackTrace();
      setSuccess(false);
    }
  }

  /**
   * 创建零件
   * 
   * @author Liugz
   * @create on 2008-12-23 This project for CYM
   */
  public WSObject[] createItems() {
    System.out.println("207XTCAD----ImportBOMFrame-----------createItems");
    WSObject[] items = null;
    Map<String, String> propertyName = new HashMap<String, String>();
    propertyName.put("itemId", "代号");
    // propertyName.put("itemId", "ITEM_ID");
    propertyName.put("name", "名称");

    outputStatus("正在Teamcenter中创建零件，请稍候......");
    String[] revIds = new String[creatingItemInfo.size()];
    for (int i = 0; i < revIds.length; i++) {
      revIds[i] = "A";
    }
    try {
      items = op.createNewItems(folder, "Qh3_DesPart", creatingItemInfo, propertyName, revIds);
      outputStatus("零件创建成功，成功创建了 " + items.length + " 个 零组件");
    } catch (TCOperationException e) {
      e.printStackTrace();
    }
    // 更新对象的属性信息
    int index = 0;
    Map<String, String> attributes = new HashMap<String, String>();
    /*
     * String userName = sdo.getUserName(); SimpleDateFormat sdf = new
     * SimpleDateFormat("dd-M-yyyy  HH:mm");
     */
    outputStatus("更新零件的属性信息，请稍候.......");
    for (WSObject item : items) {
      Map<String, String> partMap = creatingItemInfo.get(index);
      // attributes.put("Hj_Material", partMap.get("图号"));
      // attributes.put("zl_ItemForm_Part_Number", partMap.get("产品编码"));
      attributes.put("object_name", partMap.get("名称"));
      attributes.put("qh3_DrawingNumber", Util.convertNull(partMap.get("代号")));
      attributes.put("qh3_GZPart", Util.convertNull(GZlov.get(partMap.get("关重"))));

      // 添加创建者信息
      // attributes.put("hw_designer", userName);
      // attributes.put("hw_desig_date", sdf.format(new Date()));
      Util.setEmptyValue(attributes);

      try {
        sdo.updateItemRev(item, attributes);
        String id = op.getPropertyOfObject(item, "item_id");
        WSObject rev = tcop.getItemRevision(item, "last");
        nonExistItems.put(id, rev);
      } catch (TCOperationException e) {
        e.printStackTrace();
      }
      outputStatus("零组件 " + partMap.get("代号") + " 属性更新成功");
      // outputStatus("零组件 " + partMap.get("ITEM_ID") + " 属性更新成功");
      index++;
    }
    nonDSItems.putAll(nonExistItems);
    outputStatus("零件属性信息更新成功");
    return items;
  }

  /**
   * 
   * TODO 根据数据集第一个命名引用 返回localPath值 处理检出后文件改名再检入不能替换原命名引用的情况
   * 
   * @return List<Map<String,String>>
   * @author lijj created on 2011-11-24上午10:05:30
   */
  private List<Map<String, String>> getLocalPathMap(WSObject ds, String fileTrail, String newSavePath) {
    System.out.println("207XTCAD----ImportBOMFrame-----------getLocalPathMap");
    // 判断是否需要更改命名,保证一个数据集下只能有一个命名的引用
    List<Map<String, String>> resultDocList = new ArrayList<Map<String, String>>();
    try {
      String originalFileName = Util.getDSFileRefName(ds, fileTrail);

      File savedFile = new File(newSavePath);
      String saveFileName = savedFile.getName();
      String loadPath = "";
      /*
       * System.out.println("originalFileName = " + originalFileName );
       * System.out.println("saveFileName = " + saveFileName);
       */
      /*
       * if(originalFileName.length() > 0 &&
       * !saveFileName.equals(originalFileName)){ File tcFile = new
       * File(System.getenv("UFCROOT") + "\\Temp\\" + originalFileName);
       * if(tcFile.exists()){ tcFile.delete(); }
       * 
       * Util.copyFile(savedFile.getAbsolutePath(),tcFile.getAbsolutePath()
       * ); loadPath = tcFile.getAbsolutePath(); }else{ loadPath =
       * savedFile.getAbsolutePath(); }
       */
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

  /**
   * 关闭窗口，并发送操作信息。
   * 
   * @author Liugz
   * @create on 2008-12-22 This project for CYM
   * @param message
   */
  private void sendMessageToCAD(String success) {
    if (null != stateDlg)
      stateDlg.dispose();
    try {
      out.setFuncID("SaveDrawing");
      out.setResult(success);
      out.setDesc("Import BOM");
      // out.setDesc("CloseWindow");
      out.sendResultToUI();
    } catch (IOException e) {
      e.printStackTrace();
    }
    dispose();
  }

  /**
   * 提示信息
   * 
   * @author Liugz
   * @create on 2008-12-22 This project for CYM
   * @param message
   * @param type
   */
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

  /**
   * 关闭窗口
   * 
   * @author Liugz
   * @create on 2008-12-22 This project for CYM
   */
  public void closeWindow() {
    setSuccess(false);
    if (null != stateDlg)
      stateDlg.dispose();
    try {
      out.setFuncID("SaveDrawing");
      out.setResult("CloseWindow");
      out.setDesc("导入BOM操作失败，用户取消操作。");
      out.sendResultToUI();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    dispose();
  }

  /**
   * 当表格中的数量、单重发生变化的时候，程序自动计算总重
   * 
   * @author Liugz
   * @create on 2009-8-10 This project for CAD_Concrete
   * @param row
   * @param col
   * @param wt
   */
  /*
   * protected void calculateTotalWT(int row, int col) {
   * 
   * String dz = Util.convertNull(jTableBOM.getValueAt(row, WEIGHT_COL)); if
   * ("".equals(dz) || !dz.matches("\\d+\\.?\\d*")) {
   * sendMessageToUser("单重信息输入错误，输入值应为数字类型", 0); return; }
   * 
   * String sl = Util.convertNull(jTableBOM.getValueAt(row, QUANTITY_COL)); if
   * ("".equals(dz) || !sl.matches("\\d+")) {
   * sendMessageToUser("数量信息输入错误，输入值应为数字类型", 0);
   * 
   * return; } //计算总重 double wt = Double.parseDouble(dz); double qt =
   * Double.parseDouble(sl); double total = wt * qt;
   * jTableBOM.setValueAt(numFormat.format(total), row, TOTAL_WEIGHT_COL);
   * jTableBOM.updateUI();
   * 
   * }
   */

  /**
   * @author Liugz
   * @create on 2009-6-26 This project for CAD_Concrete
   * @param string
   */
  private void setPropsInfo(String id, String propName) {
    System.out.println("207XTCAD----ImportBOMFrame-----------setPropsInfo");
    hasAttribsWrong = true;
    if (propsWrongPart.containsKey(id)) {
      List<String> values = propsWrongPart.get(id);
      values.add(propName);
    } else {
      List<String> values = new ArrayList<String>();
      values.add(propName);
      propsWrongPart.put(id, values);
    }
  }

  /**
   * 为Item对象创建Dataset，并将图纸文件上传到TC中
   * 
   * @author Liugz
   * @create on 2009-6-6 This project for CAD_Concrete
   * @return
   */
  /*
   * private boolean createDatasetsAndUploadFiles() { List<WSObject> datasets
   * = new ArrayList<WSObject>( creatingDatasetInfo.size()); List<Map<String,
   * String>> filePath = new ArrayList<Map<String, String>>(
   * creatingDatasetInfo.size()); List<String> nonFileDataset = new
   * ArrayList<String>(); if (creatingDatasetInfo.size() != 0) {
   * outputStatus("正在为零组件对象创建Dataset对象，请稍候......"); for (int i = 0; i <
   * creatingDatasetInfo.size(); i++) { Map<String, String> itemMap =
   * creatingDatasetInfo.get(i); String localpath = itemMap.get("LocalPath");
   * String itemid = itemMap.get("产品编码"); if (null == localpath ||
   * "".equals(localpath)) { nonFileDataset.add(itemid); continue; } String
   * dsName = itemid + "_1"; WSObject itemRev = nonDSItems.get(itemid); try {
   * WSObject[] ds = op.createDatasets(itemRev, "IMAN_specification",
   * "ACADDWG", new String[] { dsName }); if (null != ds && ds.length == 1) {
   * outputStatus("为产品编码 为 " + itemid + " 的对象创建了Dataset对象");
   * datasets.add(ds[0]); Map<String, String> path = new HashMap<String,
   * String>(); path.put("LocalPath", localpath); filePath.add(path); } }
   * catch (TCOperationException e) { e.printStackTrace();
   * outputStatus("为 产品编码 为 " + itemid + " 的对象创建Dataset对象失败"); } } }
   * 
   * outputStatus("准备上传明细图纸文件，请稍候......"); try {
   * sdo.uploadFiles(datasets.toArray(new WSObject[0]), filePath); } catch
   * (TCOperationException e) { outputStatus("上传文件时发生失败");
   * sendMessageToUser("上传文件时发生失败，请重试", ERROR); e.printStackTrace(); return
   * false; } if (nonFileDataset.size() > 0) { StringBuffer tmpIds = new
   * StringBuffer(); for (int i = 1; i < nonFileDataset.size() + 1; i++) { if
   * (Util.isPurchProd(nonFileDataset.get(i - 1))) { continue; }
   * tmpIds.append(nonFileDataset.get(i - 1)).append(" , "); if ((i % 5) == 0)
   * { tmpIds.append("\n"); } } if (!"".equals(tmpIds.toString())) {
   * outputStatus("*******************************************************");
   * outputStatus("以下零组件对象没有图纸信息，未上传图纸信息："); outputStatus(tmpIds.toString());
   * outputStatus("*******************************************************");
   * // sendMessageToUser("ItemId 为：\n " + tmpIds.toString() // +
   * " \n的 Item 对象没有图纸，未上传图纸信息", ERROR); } }
   * 
   * return true; }
   */

  /**
   * 获取最新的Revision信息
   * 
   * @author Liugz
   * @create on 2008-12-24 This project for CYM
   * @param items
   * @return
   */
  public List<WSObject> getRevisions(List<WSObject> items) {
    System.out.println("207XTCAD----ImportBOMFrame-----------getRevisions");
    List<WSObject> itemRevList = new ArrayList<WSObject>();
    WSObject rev = null;
    for (WSObject obj : items) {
      try {
        /*
         * WSObject[] revs = op.getItemRevisions(obj, -1); rev =
         * (WSObject) revs[revs.length - 1];
         */
        rev = tcop.getItemRevision(obj, "last");
      } catch (TCOperationException e) {
        e.printStackTrace();
        rev = null;
      }
      itemRevList.add(rev);
    }
    return itemRevList;
  }

  public static void main(String[] args) {
    InputStream in = null;
    try {
      in = new FileInputStream(new File("E:\\workbench\\CADInterface_Requests\\465_New_Request.xml"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    Document doc = ParseRequestXML.getRequest(in);

    // ImportBOMFrame bomFrame = new ImportBOMFrame(null, null,
    // doc.getRootElement(), null);
    ImportBOMFrame bomFrame = new ImportBOMFrame(null, null);

  }

  /**
   * 在Textarea中显示接口的状态
   * 
   * @author Liugz
   * @create on 2008-11-25 This project for ZoomLion
   * @param string
   */
  private void outputStatus(String string) {
    message += string + "\n";
    jTAOutput.setText(" " + message);
    jTAOutput.setCaretPosition(jTAOutput.getText().length());
    // jTA_status.updateUI();
  }

  /**
   * 设置界面上按钮的状态
   * 
   * @author Liugz
   * @create on 2008-12-4 This project for ZoomLion
   * @param panel
   */
  private void setButtonStatus(boolean enable) {
    jButtonCancel.setEnabled(enable);
    jButtonOK.setEnabled(enable);
    // jButton1.setEnabled(enable);
    // btn_Update.setEnabled(enable);
  }

  public JTable getBomTable() {
    System.out.println("207XTCAD----ImportBOMFrame-----------getBomTable");
    return jTableBOM;
  }

  /**
   * 点击应用按钮时出发的事件
   * 
   * @author Liugz
   * @create on 2009-8-11 This project for CAD_Concrete
   */
  private void applyUpdatedDrawing() {
    System.out.println("207XTCAD----ImportBOMFrame-----------applyUpdatedDrawing");
    // 获取已更新的信息
    Map<String, Map<String, String>> updateInfos = new HashMap<String, Map<String, String>>();
    for (int i = 0; i < jTableBOM.getRowCount(); i++) {
      String id = Util.convertNull(jTableBOM.getValueAt(i, ITEMID_COL));
      if (updatedIds.contains(id)) {
        Map<String, String> row = new HashMap<String, String>();
        String seq = "";
        for (int c = 0; c < jTableBOM.getColumnCount(); c++) {
          String key = jTableBOM.getColumnName(c);
          Object o = jTableBOM.getValueAt(i, c);
          // if ("代号".equals(key)) {
          // key = "图号";
          // } else
          if ("序号".equals(key)) {
            seq = o.toString();
          }
          row.put(key, Util.convertNull(o));
        }
        updateInfos.put(seq, row);
      }
    }
    // 将信息写回CAD
    updateRequest("Refresh", updateInfos);
  }

  /**
   * 将对话框信息反填回CAD图纸
   * 
   * @author Liugz
   * @create on 2009-2-25 This project for CAD_Concrete
   * @return
   */
  private void backfillCADDrawing() {
    System.out.println("207XTCAD----ImportBOMFrame-----------backfillCADDrawing");
    // 收集表格中的信息
    Map<String, Map<String, String>> table = new HashMap<String, Map<String, String>>();
    int rows = jTableBOM.getRowCount();
    int cols = jTableBOM.getColumnCount();
    for (int r = 0; r < rows; r++) {
      Map<String, String> row = new HashMap<String, String>();
      String seq = "";
      for (int c = 0; c < cols; c++) {
        String key = jTableBOM.getColumnName(c);
        Object o = jTableBOM.getValueAt(r, c);
        if ("序号".equals(key)) {
          seq = o.toString();
        }
        row.put(key, Util.convertNull(o));
      }
      // table.add(row);

      table.put(seq, row);
    }
    // 将信息写回CAD
    updateRequest("Refresh", table);
  }

  /**
   * 将对话框上的信息更新会CAD
   * 
   * @author Liugz
   * @create on 2009-2-25 This project for CAD_Concrete
   * @param infos
   */
  private void updateRequest(String func, Map<String, Map<String, String>> infos/*
                                         * List
                                         * <
                                         * Map
                                         * <
                                         * String
                                         * ,
                                         * String
                                         * >>
                                         * infos
                                         */) {
    System.out.println("207XTCAD----ImportBOMFrame-----------updateRequest");
    // 修改请求中的信息
    out.setFuncID("SaveDrawing");
    out.setResult(func);
    out.setDesc("Backfill CAD Drawing.");
    Element object2 = out.getObjectElement();
    Element props2 = object2.addElement("properties");
    Element subObjEle = object2.addElement("sub-objects");

    Element root = this.requestBody;
    // Element object = root.element("body").element("object");
    // Element srcProps = object.element("properties");
    // Element doc = object.element("doc");

    // 添加原先的节点
    // srcProps.setParent(null);
    // doc.setParent(null);
    // object2.add(srcProps);
    // object2.add(doc);

    // 标题栏的反写 消失生成
    /*
     * List list = new ArrayList(); list = object.elements("properties"); if
     * (list.size() != 0) { for (Iterator projItor = list.iterator();
     * projItor.hasNext();) { Element projectElmt = (Element)
     * projItor.next(); Iterator propertyItor =
     * projectElmt.elementIterator(); while (propertyItor.hasNext()) { //
     * 更新属性，获取之前的请求所传来的元素，只更新这些元素 Element ele = (Element)
     * propertyItor.next(); String name = ele.attributeValue("name"); String
     * oldValue = ele.attributeValue("value"); Element prop2 =
     * props2.addElement("property"); // 添加property节点
     * prop2.addAttribute("name", name); String newValue = null;
     * 
     * //-------------lijj----------------------
     * 
     * if ("重量".equals(name)) { // 计算图纸标题栏的总重 float total = 0f; for (int i =
     * 0; i < jTableBOM.getRowCount(); i++) { String weight =
     * Util.convertNull(jTableBOM.getValueAt(i, TOTAL_WEIGHT_COL)); if
     * ("".equals(weight.trim())) continue; else { try { float wt =
     * Float.parseFloat(weight); total += wt; } catch (NumberFormatException
     * e1) { e1.printStackTrace(); sendMessageToUser("输入的重量不是数字型", 1);
     * return; } } } //-------------lijj----------------------
     * 
     * newValue = numFormat.format(total); totalWeight = newValue; } if
     * (null != newValue) prop2.addAttribute("value", newValue); else
     * prop2.addAttribute("value", oldValue);
     * 
     * }// 遍历完<properties>下的<property>节点 } }
     */

    Element subProj = root.element("body").element("object").element("sub-objects");
    List subObjs = subProj.elements("object");
    for (Iterator subObjItor = subObjs.iterator(); subObjItor.hasNext();) {
      Element subObj = (Element) subObjItor.next();
      // 为<sub-objs>节点添加<object>节点
      Element subObjObj = subObjEle.addElement("object");
      subObjObj.addAttribute("type", "assembly");
      List subPptsList = subObj.elements("properties");
      for (Iterator docItor = subPptsList.iterator(); docItor.hasNext();) {
        // 为<object>节点添加<properties>
        Element subObjProps = subObjObj.addElement("properties");
        Element subPptsElmt = (Element) docItor.next();
        // Iterator pptItor = subPptsElmt.elementIterator();
        List pptList = subPptsElmt.elements();
        // 保存每个<doc>节点中的内容
        // 获取表格中的信息
        for (int k = 0; k < pptList.size(); k++) {
          // 在<properties>节点下添加<property>节点
          Element property = (Element) pptList.get(k);
          String name = property.attributeValue("name");
          if ("序号".equals(name)) {
            String oldValue = property.attributeValue("value");
            Map<String, String> row = infos.get(oldValue);
            for (int p = 0; p < pptList.size(); p++) {
              Element subObjProp = subObjProps.addElement("property");
              Element reProperty = (Element) pptList.get(p);
              String propName = reProperty.attributeValue("name");
              String propOldValue = reProperty.attributeValue("value");

              subObjProp.addAttribute("name", propName);
              if (null == row) {
                subObjProp.addAttribute("value", propOldValue);
              } else if (propName.equals("零件类型")) {
                subObjProp.addAttribute("value", propOldValue);
              } else if (propName.equals("LocalPath")) {
                subObjProp.addAttribute("value", propOldValue);
              } else {
                String newValue = row.get(propName);
                subObjProp.addAttribute("value", newValue);
              }
            }
            break;
          }
          // 更新属性
        }
      }
    }
    try {
      out.sendResultToUI();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 上传文件
   * 
   * @author Liugz
   * @create on 2009-3-4 This project for CAD_Concrete
   * @return
   */
  public boolean uploadFileOfDataset() {
    System.out.println("207XTCAD----ImportBOMFrame-----------uploadFileOfDataset");
    boolean result = false;
    try {
      Util.deleteDWG(datasetObj);
      // 上传文件
      outputStatus("正在向Teamcenter上传图纸......");

      if (sdo.uploadFile(datasetObj, docList)) {
        outputStatus("上传图纸成功");
        JOptionPane.showMessageDialog(this, "上传图纸成功", "提示", JOptionPane.INFORMATION_MESSAGE);
        result = true;
      } else {
        outputStatus("上传图纸失败");
        JOptionPane.showMessageDialog(this, "上传图纸失败，可能由于文件位置不对或网络原因。", "错误", JOptionPane.ERROR_MESSAGE);
        result = false;
      }
    } catch (TCOperationException e) {
      e.printStackTrace();
      outputStatus("上传图纸失败");
      JOptionPane.showMessageDialog(this, "上传图纸失败，可能由于文件位置不对或网络原因。", "错误", JOptionPane.ERROR_MESSAGE);
      result = false;
    }
    return result;
  }

  /**
   * @return the success
   */
  public boolean isSuccess() {
    return success;
  }

  /**
   * @param success
   *            the success to set
   */
  public void setSuccess(boolean success) {
    this.success = success;
  }

  /**
   * 点击应用按钮时执行的操作
   * 
   * @author Liugz
   * @create on 2009-8-11 This project for CAD_Concrete
   */
  public void applyBOMInfo() {
    System.out.println("207XTCAD----ImportBOMFrame-----------applyBOMInfo");
    if (null == updatedIds || updatedIds.size() == 0) {
      sendMessageToUser("没有需要更新的信息！", 1);
      return;
    }

    stateDlg = new StateDialog(this, false, "正在保存更新信息，请稍候......");
    Thread importThread = new Thread(new Runnable() {
      public void run() {
        outputStatus("正在保存更新信息，请稍候......");
        setButtonStatus(false);
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        jTableBOM.setEnabled(false);

        // LiugzTodo 验证输入信息
        // 回填明细栏
        stateDlg.setMessage("正在向CAD回填BOM信息，请稍候......");
        applyUpdatedDrawing();
      }
    });
    importThread.start();
  }

}
