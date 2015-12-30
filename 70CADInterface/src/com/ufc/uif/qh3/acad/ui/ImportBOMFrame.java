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
  /** BOM���½���Item���󱣴浽���ļ��� */
  private WSObject folder;
  /** װ������BOM View ���� */
  private WSObject view;
  /** װ����� */
  private WSObject itemObj;
  private String itemid;
  private String itemname;
  /** װ������ItemRevisioin���� */
  private WSObject rootItemRev;
  /** װ������µ����ݼ����� */
  private WSObject datasetObj;
  private SaveDrawingOperation sdo;
  private ITCObjectOperation op;
  private ITCTCObjOperation tcop;
  private ITCStructureOperation so;
  private ITCTCBOMOperation bom;
  /** ��¼TC�в����ڵĶ��� */
  private Map<String, WSObject> nonExistItems;
  /** ��¼������Dataset�Ķ��� */
  private Map<String, WSObject> nonDSItems;
  /** ��¼TC���Ѵ��ڵĶ��� */
  private Map<String, WSObject> existItems;
  /** ��¼TC�в����ڶ����������Ϣ */
  private List<Map<String, String>> creatingItemInfo;
  /** ��¼������Dataset�����������Ϣ */
  private List<Map<String, String>> creatingDatasetInfo;
  private List<Map<String, String>> docList;
  private List<Map<String, String>> subObjs;
  /** ��������ϸ������Ϣ */
  private List<Map<String, String>> sortedSubObjs;
  /** ��¼TC�в����ڶ����ItemId */
  private List<String> nonItemIds;
  /** ��¼������Dataset�Ķ����ItemId */
  private List<String> nonDatasetIds;
  /** ��¼ͼֽ������TC�в�һ�µĶ����ItemId������һ�µ��������� */
  private Map<String, List<String>> propsWrongPart;
  /** ��¼ͼֽ������TC�в�һ�µĶ����ItemId */
  private List<String> propWrongItemId;
  /** ��¼�Ѿ����¹��Ķ����ItemId */
  private List<String> updatedIds;
  /** Ҫ��ȡ��REV�ϵ��������� */
  // private static Map<String, Map<String, String>> itemProps = new
  // HashMap<String, Map<String, String>>();
  /** ����ItemRevisioin�ϵ����� */
  private Map<String, Map<String, String>> itemRevInfo = new HashMap<String, Map<String, String>>();
  /** BOM�����Ƿ�ɹ���־ */
  private boolean success;
  /** JTable��TextArea����ʾ����Ϣ */
  private String message;
  /** BOM���Ƿ��вɹ��������� */
  private boolean hasPurch;
  /** BOM���Ƿ���ͨ�ü������� */
  // boolean hasGeneral;
  /** BOM���Ƿ������Ƽ����벻�Ϲ淶����Ϣ���� */
  private boolean hasSelfPart;
  /** BOM���Ƿ������Բ�һ�µ����� */
  private boolean hasAttribsWrong;
  /* �Ƿ������Ų�һ�µ��Ǳ����һ�� */
  private boolean hasSameProNo;
  /* �ж��Ƿ��Ƿ���״̬ */
  // boolean hasUseless;
  // private Map<String, String> propsMap = new HashMap<String, String>();

  /** �������Ի��� */
  private StateDialog stateDlg;
  /** ת�����ָ�ʽ */
  DecimalFormat numFormat = new DecimalFormat("#0.###");
  /** ��¼���º��������Ϣ **/
  // private String totalWeight = "";
  // �ж�����������Ƿ���ȷ
  private boolean qualityStatus = false;
  /*
   * private String[] values_part_type = new String[] { "��Ʒ", "һ������", "�����" };
   * private String[] values_Yes_No = new String[] { "��", "��" }; private
   * String[] values_crux = new String[] { "A", "B", "��" };
   */
  // private String[] values_GZ = new String[]{"һ���","�ؼ���","��Ҫ��"};
  private String[] values_GZ = new String[] { "", "G", "Z" };
  private static Map<String, String> GZlov = new HashMap<String, String>();
  static {
    GZlov.put("", "һ���");
    GZlov.put("G", "�ؼ���");
    GZlov.put("Z", "��Ҫ��");
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
    setTitle("����BOM");
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

    drawingNoL.setText("ͼ��");

    drawingNameL.setText("����");

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

    jButtonCancel.setText("ȡ��");
    jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButtonCancelActionPerformed(evt);
      }
    });

    jButtonOK.setText("����BOM");
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
          JFrame f = new JFrame("��Դ�����");
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
    // "���", "ͼ��", "����", "����", "����", "��׼��", "��ע"
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
    // ����Table����ѡȡģʽ�������ѡ������¼�
    jTableBOM.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    jTableBOM.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent event) {
        jTableBOMvalueChanged(event);
      }
    });

    drawingNoL2.setText("��ʾTeamcenter����δ�����ö���");

    jLabel2.setText("��ʾ�ö���û�ж�Ӧ��Dataset����");

    jLabel3.setBackground(new java.awt.Color(255, 190, 49));
    jLabel3.setForeground(new java.awt.Color(255, 190, 49));
    jLabel3.setOpaque(true);

    jLabel4.setText("��ʾ�ö����������TC�еĲ�һ��");

    jLabel5.setBackground(new java.awt.Color(144, 238, 144));
    jLabel5.setForeground(new java.awt.Color(144, 238, 144));
    jLabel5.setOpaque(true);

    jLabel6.setBackground(new java.awt.Color(189, 183, 107));
    jLabel6.setForeground(new java.awt.Color(189, 183, 107));
    jLabel6.setOpaque(true);

    jPanel3.setBackground(new java.awt.Color(241, 250, 255));

    jLabel1.setFont(new java.awt.Font("����", 1, 14));
    jLabel1.setText("Teamcenter���ݿ�����");

    txa_Output.setColumns(20);
    txa_Output.setRows(5);
    jScrollPane2.setViewportView(txa_Output);

    btn_Update.setText("����������Ϣ");
    btn_Update.setVisible(false);
    btn_Update.setName("btn_Update"); // NOI18N
    btn_Update.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        btn_UpdateActionPerformed(evt);
      }
    });

    jButton1.setText("��д��ͼֽ");
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
   * ѡ�б����һ������ʱ���Աȸ��������Ƿ���TC������һ��
   * 
   * @author Liugz
   * @create on 2009-8-8 This project for CAD_Concrete
   * @param event
   */
  private void jTableBOMvalueChanged(ListSelectionEvent event) {
    System.out.println("207XTCAD----ImportBOMFrame-----------jTableBOMvalueChanged");
    txa_Output.setText("");
    txa_Output.setText("���ڲ��Ҷ������Ժ�......");
    int rows = jTableBOM.getSelectedRow();
    String itemid = Util.convertNull(jTableBOM.getValueAt(rows, ITEMID_COL));
    // ������ж�����TC�в�����
    if (nonItemIds.contains(itemid)) {
      txa_Output.setText("�ö�����Teamcenter����δ������");
      btn_Update.setEnabled(false);
    } else if (updatedIds.contains(itemid)) {
      txa_Output.setText("�ö����ѱ����£���ѡ��������Ҫ����\n�Ķ���");
      btn_Update.setEnabled(false);
    } else if (propWrongItemId.contains(itemid)) {
      Map<String, String> formInfo = itemRevInfo.get(itemid);
      StringBuffer tcInfos = new StringBuffer();
      // �������������TC�е����ݲ�һ��
      tcInfos.append("��Ʒ���룺").append(itemid).append("\n").append("���ţ�").append(formInfo.get("����")).append("\n");
      txa_Output.setText(tcInfos.toString());
      btn_Update.setEnabled(true);
    } else {
      txa_Output.setText("�ö���Ĳ�Ʒ�����������Teamcenter�е�����һ�¡�");
      btn_Update.setEnabled(false);
    }
  }

  /**
   * ���������ͼֽ��ʱ���������¼���������е����ݸ���
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
    // "���еı��������17λ", "��ʾ",
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

      // ���º��¼�¸��¶����ItemId
      updatedIds.add(itemid);
      jTableBOM.updateUI();
      // �������Ժ󣬸�����ʾ��Ⱦ��
      jTableBOM.setDefaultRenderer(String.class, new BOMTableRender(nonItemIds, nonDatasetIds, updatedIds, propsWrongPart));
      outputStatus("�� " + seq + " ����ϸ��Ϣ���³ɹ�");
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
     * try { // ȡ��ǩ�� if(op.isCheckOut(datasetObj)){
     * op.cancelCheckout(datasetObj); }
     * 
     * closeWindow(); // ɾ����ϵ
     * op.deleteRelationships(sdo.getLastestItemRevision(itemObj), new
     * ModelObject[]{datasetObj}, "IMAN_specification");
     * 
     * // ɾ������ op.deleteObjects(new ModelObject[]{datasetObj}); } catch
     * (TCOperationException e) { e.printStackTrace(); }
     */

    /*
     * stateDlg = new StateDialog(this, false,
     * "������Teamcenter�ϴ�ͼֽ�����Ժ�......"); Thread thread = new Thread(new
     * Runnable(){ public void run(){ try { // �ϴ�ͼֽ setButtonStatus(false);
     * jTableBOM.setEnabled(false);
     * outputStatus("������Teamcenter�ϴ�ͼֽ�����Ժ�......");
     * if(sdo.uploadFile(datasetObj, docList)){ stateDlg.dispose();
     * outputStatus("ͼֽ�ϴ��ɹ�"); outputStatus("��ɲ������˳��ӿڳ���");
     * sendMessageToUser("ͼֽ�ϴ��ɹ���", 1); closeWindow(); } } catch
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
   * ��ʼ�����ڣ�����Ϣ��ʾ�ڽ�����
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
          out.setDesc("�û�ȡ�����롣");
          out.sendResultToUI();
        } catch (IOException ex) {
          ex.printStackTrace();
        }
        dispose();
      }
    });

    // ��ʼ���Ի���
    initComponents();

    // subObjs = ParseRequestXML.getSubObjsInfo(requestBody);

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
    pack();
    setVisible(true);

    drawingNoF.setEditable(false);
    drawingNameF.setEditable(false);
    drawingNoF.setText(itemid);
    drawingNameF.setText(itemname);

    // ��BOM��Ϣ����Ŵ�С��������
    Map<String, Map<String, String>> tableMap = new HashMap<String, Map<String, String>>();
    float[] numbers = new float[subObjs.size()];
    float startNum = 0f;
    try {
      for (int i = 0; i < subObjs.size(); i++) {
        Map<String, String> obj = subObjs.get(i);
        String num = Util.convertNull(obj.get("���"));

        if (null == num || num.trim().length() == 0) {
          startNum = (float) (startNum + 0.01);
          numbers[i] = startNum;
          tableMap.put(String.valueOf(startNum), obj);
        } else {
          numbers[i] = Float.valueOf(num);
          // Ϊʹ�����float�����е�ֵ �ܹ���map�е�keyֵһһ��Ӧ ��Ҫ������2�θ�ʽת����
          tableMap.put(String.valueOf(Float.valueOf(num)), obj);
        }
      }
    } catch (NumberFormatException e) {
      sendMessageToUser("ͼֽ��ϸ����Ŵ��ڷ������ַ���", 0);
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

      // rongjw start ����С�� ��λ
      if (obj1.get("����") != null && !obj1.get("����").equals("")) {

        // ����
        if (obj1.get("����").matches("\\d+")) {
          sortedSubObjs.add(tableMap.get(String.valueOf(numbers[k])));
        } else {
          if (obj1.get("����").split(".").length > 2) {
            sBuilder.append(obj1.get("���") + ",");
          }

          if (obj1.get("����").endsWith("kg")) {
            tmpStr = obj1.get("����").substring(0, obj1.get("����").length() - 2);
            unit = "kg";
          } else if (obj1.get("����").endsWith("ml")) {
            tmpStr = obj1.get("����").substring(0, obj1.get("����").length() - 2);
            unit = "ml";
          } else if (obj1.get("����").endsWith("mm")) {
            tmpStr = obj1.get("����").substring(0, obj1.get("����").length() - 2);
            unit = "mm";
          } else if (obj1.get("����").endsWith("g")) {
            tmpStr = obj1.get("����").substring(0, obj1.get("����").length() - 1);
            unit = "g";
          } else if (obj1.get("����").endsWith("l")) {
            tmpStr = obj1.get("����").substring(0, obj1.get("����").length() - 1);
            unit = "l";
          } else if (obj1.get("����").endsWith("m")) {
            tmpStr = obj1.get("����").substring(0, obj1.get("����").length() - 1);
            unit = "m";
          } else {
            isNum = true;
            sBuilder.append(obj1.get("���") + ",");
          }

          if (isNum == false) {
            if (!tmpStr.matches("\\d+.\\d+")) {
              sBuilder.append(obj1.get("���") + ",");
            } else {
              tableMap.get(String.valueOf(numbers[k])).put("����", tmpStr);
              tableMap.get(String.valueOf(numbers[k])).put("������λ", unit);
            }
          }

          sortedSubObjs.add(tableMap.get(String.valueOf(numbers[k])));
        }

      } else {
        sortedSubObjs.add(tableMap.get(String.valueOf(numbers[k])));
      }
      // rongjw end ����С�� ��λ

      // sortedSubObjs.add(tableMap.get(String.valueOf(numbers[k])));
      //
      // //rongjw start ����С��
      //
      // try {
      // Map<String, String> obj =
      // tableMap.get(String.valueOf(numbers[k]));
      // if (obj.get("����") != null && obj.get("����").matches("\\d+.\\d+"))
      // {
      //
      // sBuilder.append(obj.get("���") + ",");
      // }
      //
      // } catch (NumberFormatException e) {
      // sendMessageToUser("ͼֽ��ϸ����Ŵ��ڷ������ַ���", 0);
      // return;
      // }
      // //rongjw end ����С��

    }
    // BOMTableModel elementModel = new BOMTableModel(subObjs);
    BOMTableModel elementModel = new BOMTableModel(sortedSubObjs);
    jTableBOM.setModel(elementModel);

    // rongjw start ������С��
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
    // if ("��".equals(selectedValue) || selectedValue==null ||
    // "".equals(selectedValue)) {
    // sendMessageToUser("������λ��ֵΪ������ʱ������ֵֻ֧������", 3);
    // }
    // }
    // }
    // });

    DefaultCellEditor comboEditor_bl_uom = new DefaultCellEditor(bl_uomComboBox);
    jTableBOM.getColumnModel().getColumn(BL_UOM_COL).setCellEditor(comboEditor_bl_uom);
    // rongjw end ������С��

    // JComboBox combo_unit = new JComboBox();
    // combo_unit.setModel(new DefaultComboBoxModel(values_GZ));
    // DefaultCellEditor comboEditor_part_type = new
    // DefaultCellEditor(combo_unit);
    // jTableBOM.getColumnModel().getColumn(6).setCellEditor(comboEditor_part_type);

    // ��ʼ���Ի���
    stateDlg = new StateDialog(this, false, "���ڲ�ѯItem�����ݼ���Ϣ�����Ժ�......");
    Thread initThread = new Thread(new Runnable() {
      public void run() {
        outputStatus("���ڲ�ѯ�������Ϣ�����Ժ�......");
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
   * ����ȥ��ѯ������Ƿ����
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
    // ��ϵͳ�н��в��ң���û���ҵ��Ķ��󲢷�Ϊ���࣬��׼�����Ǳ�׼��
    creatingItemInfo = new ArrayList<Map<String, String>>();
    creatingDatasetInfo = new ArrayList<Map<String, String>>();
    nonExistItems = new HashMap<String, WSObject>();
    nonDSItems = new HashMap<String, WSObject>();
    existItems = new HashMap<String, WSObject>();

    // ��ȡͼ��
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
          sendMessageToUser("��" + (i + 1) + "����ϸ����Ϣ��д�����������ơ���������Ϊ�գ��������", 0);
          jButtonOK.setEnabled(false);
          jTableBOM.setRowSelectionInterval(i, i);
          return;
        }

        if ((drawingno.equals("") || !drawingno.toUpperCase().startsWith("AZ")) && itemid.equals("")) {
          sendMessageToUser("��" + (i + 1) + "����ϸ����Ϣ��д���������⹺��ITEM_ID����Ϊ�գ��������", 0);
          jButtonOK.setEnabled(false);
          jTableBOM.setRowSelectionInterval(i, i);
          return;
        }

        // if(!quantity.matches("\\d+")) {
        // sendMessageToUser("������Ϣ�����������ֵӦΪ����", 0);
        // jButtonOK.setEnabled(false);
        // jTableBOM.setRowSelectionInterval(i, i);
        // return;
        // }

        // �жϱ������ͬ��������Ų�ͬ�����
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
        // JOptionPane.showMessageDialog(this, "��" + (i + 1) + "���" + (j
        // + 1) + "�б������ͬ����Ų�ͬ", "��ʾ",
        // JOptionPane.ERROR_MESSAGE);
        // hasSameProNo = true;
        // jButtonOK.setEnabled(false);
        // break;
        // }
        // }

        // ��ѯ�������͵�item
        WSObject obj = null;
        if (itemid != null && !itemid.equals("")) {
          obj = sdo.findAllItem(itemid);
        } else {
          obj = sdo.findAllItem(drawingno);
        }

        if (null == obj) {

          if (drawingno.equals("") || !drawingno.toUpperCase().startsWith("AZ")) {
            sendMessageToUser("δ����Teamcenter���ҵ���Ӧ�ĵ�" + (i + 1) + "���⹺����Ϣ���޷�������Ʒ�ṹ�����ȴ���������⹺�� �ٽ��е��룡", 0);

            jButtonOK.setEnabled(false);
            jTableBOM.setRowSelectionInterval(i, i);
            return;
          }

          if (itemid != null && !itemid.equals("")) {
            nonItemIds.add(itemid);
            creatingItemInfo.add(sortedSubObjs.get(i));
            outputStatus("û���ҵ� ��Ʒ���� Ϊ " + itemid + " ���������Ϣ");
          } else {
            nonItemIds.add(drawingno);
            creatingItemInfo.add(sortedSubObjs.get(i));
            outputStatus("û���ҵ� ��Ʒ���� Ϊ " + drawingno + " ���������Ϣ");
          }

        } else {
          WSObject rev = tcop.getItemRevision(obj, "last");
          if (itemid != null && !itemid.equals("")) {
            existItems.put(itemid, rev);
            outputStatus("��� ��Ʒ���� Ϊ " + itemid + " �������Ϣ");
          } else {
            existItems.put(drawingno, rev);
            outputStatus("��� ��Ʒ���� Ϊ " + drawingno + " �������Ϣ");
          }

          outputStatus("���ڻ�ȡ ItemRevisioin ������������......");
        }
      }
    } catch (TCOperationException e1) {
      System.out.println("��ȡ��Ϣ�����itemid" + itemid);
      sendMessageToUser(e1.getMessage(), 0);
      return;
    } catch (NumberFormatException e1) {
      sendMessageToUser("��ֵת��ʱ������������ͼֽ��ϸ��������д�Ƿ���ȷ��", 0);
      return;
    }
    // ��Ⱦ���
    // jTableBOM.setDefaultRenderer(String.class, new
    // BOMTableRender(nonItemIds));
    creatingDatasetInfo.addAll(creatingItemInfo);
    // �����Բ�һ�µĶ����ItemId��¼����
    String[] wrongIds = propsWrongPart.keySet().toArray(new String[0]);
    propWrongItemId = Arrays.asList(wrongIds);
    jTableBOM.setDefaultRenderer(String.class, new BOMTableRender(nonItemIds, nonDatasetIds, propsWrongPart));
    jTableBOM.updateUI();
  }

  /**
   * ���BOMView���µ�Revision�ļ��״̬
   * 
   * @author Liugz
   * @create on 2008-12-25 This project for CYM
   * @return
   */
  private WSObject checkBOMViewRevision() {
    System.out.println("207XTCAD----ImportBOMFrame-----------checkBOMViewRevision");
    // ��ȡBOMView
    /*
     * WSObject[] rootItemRevs = null; try { rootItemRevs =
     * op.getItemRevisions(itemObj, -1); } catch (TCOperationException e1) {
     * e1.printStackTrace(); System.out.println("\t��ȡRevision��Ϣʧ��"); }
     */
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
            sendMessageToUser("�����û��������ڱ༭��BVR���������ܼ���������", 1);
            jButtonOK.setEnabled(false);
          } else
            outputStatus("���Ѽ����BOM View");
        }
      }
    } catch (TCOperationException te) {
      sendMessageToUser("��ȡBOMView��Ϣʱ���������޷���������", 1);
      System.out.println("δ�ܻ�ȡBOMView��Ϣ");
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
        outputStatus("Teamcenter�в����ڵĲɹ������׼���Ĳ�Ʒ���룺");
        outputStatus(nonPurchIds.toString());
        outputStatus("----------------------------------------------------");

      }
      // if(hasGeneral && !"".equals(nonGenIds.toString())){
      // outputStatus("Teamcenter�в����ڵ�ͨ�ü� ItemId��");
      // outputStatus(nonGenIds.toString());
      // outputStatus("----------------------------------------------------");
      // }
      if (!"".equals(nonSelfIds.toString())) {
        outputStatus("����������� ��Ʒ���� �����ϱ���淶�����ܽ��д���������");
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
        outputStatus("���� ����� ����û��ͼֽ��Ϣ��δ�ϴ�ͼֽ��Ϣ��");
        outputStatus(tmpIds.toString());
        outputStatus("----------------------------------------------------");
      }
    }

    if (null != propsWrongPart && propsWrongPart.size() != 0) {
      outputStatus("���¶����������Ϣ��TC�е����Բ�һ�£�");
      String[] ids = propsWrongPart.keySet().toArray(new String[0]);
      for (String id : ids) {
        List<String> propNames = propsWrongPart.get(id);
        outputStatus(id + " �����ϵ� " + propNames.toString() + " ������TC�е����Բ�һ��");
      }

      sendMessageToUser("��ϸ���д������Ժ�TC�в�һ�µĶ���", 1);

      // jButtonOK.setEnabled(false);
    }

    // rongjw start ������λ
    if (sBuilder.length() > 0) {
      jButtonOK.setEnabled(false);
      sendMessageToUser("��ϸ�������Ϊ��\n��" + sBuilder.toString().substring(0, sBuilder.length() - 1) + "��\n��������ֵ��ʽ����", 1);
      outputStatus("��ϸ�������Ϊ��\n��" + sBuilder.toString().substring(0, sBuilder.length() - 1) + "��\n��������ֵ��ʽ����");

    }
    // rongjw end ������λ
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
    // "δ����Teamcenter���ҵ���Ӧ�� �ɹ��� �� ͨ�ü� ��Ϣ���޷�������Ʒ�ṹ�����ȴ�������� �ɹ��� �� ͨ�ü� �ٽ��е��룡",
    // 1);
    // // jTableBOM.setEnabled(false);
    // } else
    if (hasPurch) {
      jButtonOK.setEnabled(false);
      sendMessageToUser("δ����Teamcenter���ҵ���Ӧ�� �ɹ������׼�� ��Ϣ���޷�������Ʒ�ṹ�����ȴ�������� �ɹ������׼�� �ٽ��е��룡", 1);
      // jTableBOM.setEnabled(false);
    }
    // else if (hasGeneral) {
    // jButtonOK.setEnabled(false);
    // sendMessageToUser(
    // "δ����Teamcenter���ҵ���Ӧ�� ͨ�ü� ��Ϣ���޷�������Ʒ�ṹ�����ȴ�������� ͨ�ü� �ٽ��е��룡", 1);
    // // jTableBOM.setEnabled(false);
    // }
    else if (hasSelfPart) {
      jButtonOK.setEnabled(false);
      sendMessageToUser("��M���� ��Ʒ���� �����ϱ���淶�����ܽ��д������޷�������Ʒ�ṹ", 1);
      // jTableBOM.setEnabled(false);
    } else if (hasAttribsWrong) {
      jButtonOK.setEnabled(false);
      sendMessageToUser("��ǰ��ϸ���е�������Ϣ��Teamcenter�е�������Ϣ��һ�£����޸���ȷ���ٽ��б������", 1);
      // jTableBOM.setEnabled(false);
    }
  }

  /**
   * �������BOM��ťʱִ�еĲ���
   * 
   * @author Liugz
   * @create on 2009-8-11 This project for CAD_Concrete
   */
  public void importBOMInfo2() {
    System.out.println("207XTCAD----ImportBOMFrame-----------importBOMInfo2");
    for (int i = 0; i < jTableBOM.getRowCount(); i++) {
      // �ж������Ƿ�Ϊ�ղ����Ƿ�Ϊ����
      // String sl = jTableBOM.getValueAt(i, QUANTITY_COL).toString();
      // System.out.println("si="+sl);
      // Pattern pattern = Pattern.compile("[0-9]*");
      // if ("".equals(sl) || !pattern.matcher(sl).matches()) {
      // JOptionPane.showMessageDialog(this, "��������Ϊ�ղ��ұ���������",
      // "��ʾ",JOptionPane.ERROR_MESSAGE);
      // jButtonOK.setEnabled(false);
      // return;
      // }

      // if (jTableBOM.getValueAt(i, ITEMID_COL).toString().equals("")
      // || jTableBOM.getValueAt(i, NAME_COL).toString().equals("")
      // || jTableBOM.getValueAt(i, QUANTITY_COL).toString().equals("")) {
      // qualityStatus = true;
      // sendMessageToUser("��ϸ����Ϣ��д����������Ʒ���롢ͼ�����ơ���������Ϊ�գ��������", 0);
      // jButtonOK.setEnabled(false);
      // return;
      // }
    }

    stateDlg = new StateDialog(this, false, "���ڵ���BOM��Ϣ�����Ժ�......");
    /*
     * Thread importThread = new Thread(new Runnable() { public void run() {
     * outputStatus("���ڵ���BOM��Ϣ�����Ժ�......"); setButtonStatus(false);
     * setCursor(new Cursor(Cursor.WAIT_CURSOR));
     * jTableBOM.setEnabled(false);
     * 
     * // LiugzTodo ��֤������Ϣ // ������ϸ��
     * stateDlg.setMessage("������CAD����BOM��Ϣ�����Ժ�......"); backfillCADDrawing();
     * } }); importThread.start();
     */

    outputStatus("���ڵ���BOM��Ϣ�����Ժ�......");
    setButtonStatus(false);
    setCursor(new Cursor(Cursor.WAIT_CURSOR));
    jTableBOM.setEnabled(false);
    doOperation("succeed");
  }

  /**
   * ִ�л����Ĳ���
   * 
   * @author Liugz
   * @create on 2009-3-4 This project for CAD_Concrete
   * @param status
   */

  // ��Ϊ�ɰ�ť�¼����� ֱ�Ӹ�ֵΪsucceed
  public void doOperation(String status) {
    // �������󣬲鿴״̬
    System.out.println("207XTCAD----ImportBOMFrame-----------doOperation");
    if ("succeed".equals(status)) {
      if (null == stateDlg)
        stateDlg = new StateDialog(this, false, "���ڵ���BOM��Ϣ�����Ժ�......");
      Thread importThread = new Thread(new Runnable() {
        public void run() {
          // ����BOM
          stateDlg.setMessage("���ڵ���BOM��Ϣ�����Ժ�......");
          outputStatus("���ڵ���BOM��Ϣ......");

          importBOMInfo();

          if (isSuccess()) {// ����BOM�����ɹ�
            outputStatus("BOM��Ϣ����ɹ�");
            try {
              // �ϴ�ͼֽ
              outputStatus("������Teamcenter�ϴ�ͼֽ�����Ժ�......");
              stateDlg.setMessage("������Teamcenter�ϴ�ͼֽ�����Ժ�......");

              // �ж��Ƿ���Ҫ��������,��֤һ�����ݼ���ֻ����һ������������
              List<Map<String, String>> tempDocList = getLocalPathMap(datasetObj, ".dwg", docList.get(0).get("LocalPath"));
              // �ж��Ƿ���Ҫ��������,��֤һ�����ݼ���ֻ����һ������������

              if (sdo.uploadFile(datasetObj, tempDocList)) {
                // if (sdo.uploadFile(datasetObj, docList)) {
                outputStatus("ͼֽ�ϴ��ɹ������ͼֽ�������");
                stateDlg.dispose();
                sendMessageToUser("ͼֽ�ϴ��ɹ������ͼֽ�������", 1);
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
            sendMessageToUser("����BOMʱ�������������²�����", 0);
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
      sendMessageToUser("��CAD������Ϣʱ�������������²�����", 0);
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
    List<Map<String, String>> itemRevAttribs2 = new ArrayList<Map<String, String>>();// ������λ��Ϊ������ʱ���ȸ���BOMLine�ϵĶ�����λ���ٸ�������ֵ

    if (null != creatingItemInfo && creatingItemInfo.size() != 0) {
      WSObject[] ci = createItems();
      if (null == ci || ci.length == 0) {
        sendMessageToUser("����Itemʱ���ִ��������²�����", ERROR);
        return;
      }
    }

    // Ϊ������Item���󴴽�Dataset�����ϴ�ͼֽ
    // if (!createDatasetsAndUploadFiles()) {
    // return;
    // }
    // ��ȡRevision

    for (Iterator itor = nonExistItems.keySet().iterator(); itor.hasNext();) {
      String itemid = (String) itor.next();
      WSObject rev = nonExistItems.get(itemid);
      existItems.put(itemid, rev);
    }

    // ����BOM
    List<WSObject> itemRevs = new ArrayList<WSObject>();
    List<WSObject> itemRevs2 = new ArrayList<WSObject>();
    List<Map<String, String>> itemRevAttribs = new ArrayList<Map<String, String>>();
    outputStatus("���ڻ�ȡBOM������Ϣ.......");
    for (int i = 0; i < subObjs.size(); i++) {
      // ��ȡ������Ϣ
      Map<String, String> partMap = subObjs.get(i);
      String id = "";
      if (partMap.get("ITEM_ID") != null && !partMap.get("ITEM_ID").toString().equals("")) {
        id = partMap.get("ITEM_ID");
      } else {
        id = partMap.get("����");
      }

      WSObject itemRev = existItems.get(id);
      Map<String, String> attributes = new HashMap<String, String>();
      Map<String, String> attributes2 = new HashMap<String, String>();
      String no = partMap.get("����");

      // rongjw start ������λ
      if (partMap.get("������λ") == null || "".equals(partMap.get("������λ")) || "��".equals(partMap.get("������λ"))) {
        if (no.matches("\\d+")) {
          attributes.put("bl_quantity", no);
        }
      } else {// ������λ��Ϊ������ʱ���ȸ���BOMLine�ϵĶ�����λ���ٸ�������ֵ
        if (no.matches("\\d+.\\d+")) {
          // gaott ����3λ
          if (no.length() > no.indexOf(".") + 3) {
            no = no.substring(0, no.indexOf(".") + 3);
          }
          itemRevs2.add(itemRev);
          attributes2.put("bl_quantity", no);
        }
      }
      // rongjw end ������λ

      String unit = partMap.get("������λ");

      /*
       * if (no.matches("\\d+")){ attributes.put("bl_quantity", no); }
       */
      String remark = partMap.get("��ע");
      attributes.put("Qh3_Beizhu", remark);

      attributes.put("Qh3_Source", "CAD");
      attributes.put("bl_uom", unit);
      // attributes.put("PSE_config_demo",
      // Util.convertNull(partMap.get("��ע")));
      // attributes.put("���ű�ע", partMap.get("���"));
      // attributes.put("bl_sequence_no", partMap.get("���"));
      itemRevs.add(itemRev);
      itemRevAttribs.add(attributes);
      if (attributes2.size() > 0) {
        itemRevAttribs2.add(attributes2);
      } else {
        itemRevAttribs2.add(attributes);
      }
    }
    outputStatus("��ȡBOM������Ϣ�ɹ�");
    try {
      // �ж�BOM�����ǩ��״̬
      boolean result = false;
      if (null != view) {
        if (!op.isCheckOut(view)) {
          if (op.checkOutObjects(new WSObject[] { view }, ""))
            outputStatus("���BOM View");
          else {
            outputStatus("�������ʧ�ܣ�δ�ܼ�����������²���");
            sendMessageToUser("�������ʧ�ܣ�δ�ܼ�����������²���", 0);
            return;
          }
        }
        outputStatus("��ʼ����BOM View��Ϣ......");
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
          outputStatus("BOM View ���³ɹ�");
          if (!op.checkin(new WSObject[] { view }))
            outputStatus("ǩ�����ʱ�������󣬶��󱣳ֿɱ༭״̬");
        }
      } else {
        outputStatus("׼���½�BOM View��Ϣ......");
        Calendar c = Calendar.getInstance();
        System.out.println(c.getTime());
        // result=bom.createPSBOM(TCUtil.modelObject2WSObject(rootItemRev),
        // itemRevs, itemRevAttribs, "view");
        result = bom.createPSBOM(rootItemRev, (ArrayList<WSObject>) itemRevs, itemRevAttribs, "view");

        if (result)
          outputStatus("BOM View ���³ɹ�");
      }
      // rongjw start ����С��
      if (itemRevAttribs2.size() > 0) {

        if (view == null) {
          view = so.getBVRbyBVType(rootItemRev, "view");
        }
        if (!op.isCheckOut(view)) {
          if (op.checkOutObjects(new WSObject[] { view }, ""))
            outputStatus("���BOM View");
          else {
            outputStatus("�������ʧ�ܣ�δ�ܼ�����������²���");
            sendMessageToUser("�������ʧ�ܣ�δ�ܼ�����������²���", 0);
            return;
          }
        }
        outputStatus("��ʼ����BOM View��Ϣ......");
        Calendar c = null;
        if (null == c)
          c = Calendar.getInstance();
        result = bom.createPSBOM(rootItemRev, (ArrayList<WSObject>) itemRevs, itemRevAttribs2, "view", "Qh3_Source", "CAD");
        if (result) {
          outputStatus("BOM View ���³ɹ�");
        }
      }
      // rongjw end ����С��
      setSuccess(result);
    } catch (TCOperationException e) {
      e.printStackTrace();
      setSuccess(false);
    }
  }

  /**
   * �������
   * 
   * @author Liugz
   * @create on 2008-12-23 This project for CYM
   */
  public WSObject[] createItems() {
    System.out.println("207XTCAD----ImportBOMFrame-----------createItems");
    WSObject[] items = null;
    Map<String, String> propertyName = new HashMap<String, String>();
    propertyName.put("itemId", "����");
    // propertyName.put("itemId", "ITEM_ID");
    propertyName.put("name", "����");

    outputStatus("����Teamcenter�д�����������Ժ�......");
    String[] revIds = new String[creatingItemInfo.size()];
    for (int i = 0; i < revIds.length; i++) {
      revIds[i] = "A";
    }
    try {
      items = op.createNewItems(folder, "Qh3_DesPart", creatingItemInfo, propertyName, revIds);
      outputStatus("��������ɹ����ɹ������� " + items.length + " �� �����");
    } catch (TCOperationException e) {
      e.printStackTrace();
    }
    // ���¶����������Ϣ
    int index = 0;
    Map<String, String> attributes = new HashMap<String, String>();
    /*
     * String userName = sdo.getUserName(); SimpleDateFormat sdf = new
     * SimpleDateFormat("dd-M-yyyy  HH:mm");
     */
    outputStatus("���������������Ϣ�����Ժ�.......");
    for (WSObject item : items) {
      Map<String, String> partMap = creatingItemInfo.get(index);
      // attributes.put("Hj_Material", partMap.get("ͼ��"));
      // attributes.put("zl_ItemForm_Part_Number", partMap.get("��Ʒ����"));
      attributes.put("object_name", partMap.get("����"));
      attributes.put("qh3_DrawingNumber", Util.convertNull(partMap.get("����")));
      attributes.put("qh3_GZPart", Util.convertNull(GZlov.get(partMap.get("����"))));

      // ��Ӵ�������Ϣ
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
      outputStatus("����� " + partMap.get("����") + " ���Ը��³ɹ�");
      // outputStatus("����� " + partMap.get("ITEM_ID") + " ���Ը��³ɹ�");
      index++;
    }
    nonDSItems.putAll(nonExistItems);
    outputStatus("���������Ϣ���³ɹ�");
    return items;
  }

  /**
   * 
   * TODO �������ݼ���һ���������� ����localPathֵ ���������ļ������ټ��벻���滻ԭ�������õ����
   * 
   * @return List<Map<String,String>>
   * @author lijj created on 2011-11-24����10:05:30
   */
  private List<Map<String, String>> getLocalPathMap(WSObject ds, String fileTrail, String newSavePath) {
    System.out.println("207XTCAD----ImportBOMFrame-----------getLocalPathMap");
    // �ж��Ƿ���Ҫ��������,��֤һ�����ݼ���ֻ����һ������������
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
   * �رմ��ڣ������Ͳ�����Ϣ��
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
   * ��ʾ��Ϣ
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
      JOptionPane.showMessageDialog(this, "ͼֽ��Ϣ����Teamcenter�����з����������������ӣ�", "��ʾ", dlgType);
    else
      JOptionPane.showMessageDialog(this, message, "��ʾ", dlgType);
  }

  /**
   * �رմ���
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
      out.setDesc("����BOM����ʧ�ܣ��û�ȡ��������");
      out.sendResultToUI();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    dispose();
  }

  /**
   * ������е����������ط����仯��ʱ�򣬳����Զ���������
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
   * sendMessageToUser("������Ϣ�����������ֵӦΪ��������", 0); return; }
   * 
   * String sl = Util.convertNull(jTableBOM.getValueAt(row, QUANTITY_COL)); if
   * ("".equals(dz) || !sl.matches("\\d+")) {
   * sendMessageToUser("������Ϣ�����������ֵӦΪ��������", 0);
   * 
   * return; } //�������� double wt = Double.parseDouble(dz); double qt =
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
   * ΪItem���󴴽�Dataset������ͼֽ�ļ��ϴ���TC��
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
   * outputStatus("����Ϊ��������󴴽�Dataset�������Ժ�......"); for (int i = 0; i <
   * creatingDatasetInfo.size(); i++) { Map<String, String> itemMap =
   * creatingDatasetInfo.get(i); String localpath = itemMap.get("LocalPath");
   * String itemid = itemMap.get("��Ʒ����"); if (null == localpath ||
   * "".equals(localpath)) { nonFileDataset.add(itemid); continue; } String
   * dsName = itemid + "_1"; WSObject itemRev = nonDSItems.get(itemid); try {
   * WSObject[] ds = op.createDatasets(itemRev, "IMAN_specification",
   * "ACADDWG", new String[] { dsName }); if (null != ds && ds.length == 1) {
   * outputStatus("Ϊ��Ʒ���� Ϊ " + itemid + " �Ķ��󴴽���Dataset����");
   * datasets.add(ds[0]); Map<String, String> path = new HashMap<String,
   * String>(); path.put("LocalPath", localpath); filePath.add(path); } }
   * catch (TCOperationException e) { e.printStackTrace();
   * outputStatus("Ϊ ��Ʒ���� Ϊ " + itemid + " �Ķ��󴴽�Dataset����ʧ��"); } } }
   * 
   * outputStatus("׼���ϴ���ϸͼֽ�ļ������Ժ�......"); try {
   * sdo.uploadFiles(datasets.toArray(new WSObject[0]), filePath); } catch
   * (TCOperationException e) { outputStatus("�ϴ��ļ�ʱ����ʧ��");
   * sendMessageToUser("�ϴ��ļ�ʱ����ʧ�ܣ�������", ERROR); e.printStackTrace(); return
   * false; } if (nonFileDataset.size() > 0) { StringBuffer tmpIds = new
   * StringBuffer(); for (int i = 1; i < nonFileDataset.size() + 1; i++) { if
   * (Util.isPurchProd(nonFileDataset.get(i - 1))) { continue; }
   * tmpIds.append(nonFileDataset.get(i - 1)).append(" , "); if ((i % 5) == 0)
   * { tmpIds.append("\n"); } } if (!"".equals(tmpIds.toString())) {
   * outputStatus("*******************************************************");
   * outputStatus("�������������û��ͼֽ��Ϣ��δ�ϴ�ͼֽ��Ϣ��"); outputStatus(tmpIds.toString());
   * outputStatus("*******************************************************");
   * // sendMessageToUser("ItemId Ϊ��\n " + tmpIds.toString() // +
   * " \n�� Item ����û��ͼֽ��δ�ϴ�ͼֽ��Ϣ", ERROR); } }
   * 
   * return true; }
   */

  /**
   * ��ȡ���µ�Revision��Ϣ
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
   * ��Textarea����ʾ�ӿڵ�״̬
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
   * ���ý����ϰ�ť��״̬
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
   * ���Ӧ�ð�ťʱ�������¼�
   * 
   * @author Liugz
   * @create on 2009-8-11 This project for CAD_Concrete
   */
  private void applyUpdatedDrawing() {
    System.out.println("207XTCAD----ImportBOMFrame-----------applyUpdatedDrawing");
    // ��ȡ�Ѹ��µ���Ϣ
    Map<String, Map<String, String>> updateInfos = new HashMap<String, Map<String, String>>();
    for (int i = 0; i < jTableBOM.getRowCount(); i++) {
      String id = Util.convertNull(jTableBOM.getValueAt(i, ITEMID_COL));
      if (updatedIds.contains(id)) {
        Map<String, String> row = new HashMap<String, String>();
        String seq = "";
        for (int c = 0; c < jTableBOM.getColumnCount(); c++) {
          String key = jTableBOM.getColumnName(c);
          Object o = jTableBOM.getValueAt(i, c);
          // if ("����".equals(key)) {
          // key = "ͼ��";
          // } else
          if ("���".equals(key)) {
            seq = o.toString();
          }
          row.put(key, Util.convertNull(o));
        }
        updateInfos.put(seq, row);
      }
    }
    // ����Ϣд��CAD
    updateRequest("Refresh", updateInfos);
  }

  /**
   * ���Ի�����Ϣ�����CADͼֽ
   * 
   * @author Liugz
   * @create on 2009-2-25 This project for CAD_Concrete
   * @return
   */
  private void backfillCADDrawing() {
    System.out.println("207XTCAD----ImportBOMFrame-----------backfillCADDrawing");
    // �ռ�����е���Ϣ
    Map<String, Map<String, String>> table = new HashMap<String, Map<String, String>>();
    int rows = jTableBOM.getRowCount();
    int cols = jTableBOM.getColumnCount();
    for (int r = 0; r < rows; r++) {
      Map<String, String> row = new HashMap<String, String>();
      String seq = "";
      for (int c = 0; c < cols; c++) {
        String key = jTableBOM.getColumnName(c);
        Object o = jTableBOM.getValueAt(r, c);
        if ("���".equals(key)) {
          seq = o.toString();
        }
        row.put(key, Util.convertNull(o));
      }
      // table.add(row);

      table.put(seq, row);
    }
    // ����Ϣд��CAD
    updateRequest("Refresh", table);
  }

  /**
   * ���Ի����ϵ���Ϣ���»�CAD
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
    // �޸������е���Ϣ
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

    // ���ԭ�ȵĽڵ�
    // srcProps.setParent(null);
    // doc.setParent(null);
    // object2.add(srcProps);
    // object2.add(doc);

    // �������ķ�д ��ʧ����
    /*
     * List list = new ArrayList(); list = object.elements("properties"); if
     * (list.size() != 0) { for (Iterator projItor = list.iterator();
     * projItor.hasNext();) { Element projectElmt = (Element)
     * projItor.next(); Iterator propertyItor =
     * projectElmt.elementIterator(); while (propertyItor.hasNext()) { //
     * �������ԣ���ȡ֮ǰ��������������Ԫ�أ�ֻ������ЩԪ�� Element ele = (Element)
     * propertyItor.next(); String name = ele.attributeValue("name"); String
     * oldValue = ele.attributeValue("value"); Element prop2 =
     * props2.addElement("property"); // ���property�ڵ�
     * prop2.addAttribute("name", name); String newValue = null;
     * 
     * //-------------lijj----------------------
     * 
     * if ("����".equals(name)) { // ����ͼֽ������������ float total = 0f; for (int i =
     * 0; i < jTableBOM.getRowCount(); i++) { String weight =
     * Util.convertNull(jTableBOM.getValueAt(i, TOTAL_WEIGHT_COL)); if
     * ("".equals(weight.trim())) continue; else { try { float wt =
     * Float.parseFloat(weight); total += wt; } catch (NumberFormatException
     * e1) { e1.printStackTrace(); sendMessageToUser("�������������������", 1);
     * return; } } } //-------------lijj----------------------
     * 
     * newValue = numFormat.format(total); totalWeight = newValue; } if
     * (null != newValue) prop2.addAttribute("value", newValue); else
     * prop2.addAttribute("value", oldValue);
     * 
     * }// ������<properties>�µ�<property>�ڵ� } }
     */

    Element subProj = root.element("body").element("object").element("sub-objects");
    List subObjs = subProj.elements("object");
    for (Iterator subObjItor = subObjs.iterator(); subObjItor.hasNext();) {
      Element subObj = (Element) subObjItor.next();
      // Ϊ<sub-objs>�ڵ����<object>�ڵ�
      Element subObjObj = subObjEle.addElement("object");
      subObjObj.addAttribute("type", "assembly");
      List subPptsList = subObj.elements("properties");
      for (Iterator docItor = subPptsList.iterator(); docItor.hasNext();) {
        // Ϊ<object>�ڵ����<properties>
        Element subObjProps = subObjObj.addElement("properties");
        Element subPptsElmt = (Element) docItor.next();
        // Iterator pptItor = subPptsElmt.elementIterator();
        List pptList = subPptsElmt.elements();
        // ����ÿ��<doc>�ڵ��е�����
        // ��ȡ����е���Ϣ
        for (int k = 0; k < pptList.size(); k++) {
          // ��<properties>�ڵ������<property>�ڵ�
          Element property = (Element) pptList.get(k);
          String name = property.attributeValue("name");
          if ("���".equals(name)) {
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
              } else if (propName.equals("�������")) {
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
          // ��������
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
   * �ϴ��ļ�
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
      // �ϴ��ļ�
      outputStatus("������Teamcenter�ϴ�ͼֽ......");

      if (sdo.uploadFile(datasetObj, docList)) {
        outputStatus("�ϴ�ͼֽ�ɹ�");
        JOptionPane.showMessageDialog(this, "�ϴ�ͼֽ�ɹ�", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
        result = true;
      } else {
        outputStatus("�ϴ�ͼֽʧ��");
        JOptionPane.showMessageDialog(this, "�ϴ�ͼֽʧ�ܣ����������ļ�λ�ò��Ի�����ԭ��", "����", JOptionPane.ERROR_MESSAGE);
        result = false;
      }
    } catch (TCOperationException e) {
      e.printStackTrace();
      outputStatus("�ϴ�ͼֽʧ��");
      JOptionPane.showMessageDialog(this, "�ϴ�ͼֽʧ�ܣ����������ļ�λ�ò��Ի�����ԭ��", "����", JOptionPane.ERROR_MESSAGE);
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
   * ���Ӧ�ð�ťʱִ�еĲ���
   * 
   * @author Liugz
   * @create on 2009-8-11 This project for CAD_Concrete
   */
  public void applyBOMInfo() {
    System.out.println("207XTCAD----ImportBOMFrame-----------applyBOMInfo");
    if (null == updatedIds || updatedIds.size() == 0) {
      sendMessageToUser("û����Ҫ���µ���Ϣ��", 1);
      return;
    }

    stateDlg = new StateDialog(this, false, "���ڱ��������Ϣ�����Ժ�......");
    Thread importThread = new Thread(new Runnable() {
      public void run() {
        outputStatus("���ڱ��������Ϣ�����Ժ�......");
        setButtonStatus(false);
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        jTableBOM.setEnabled(false);

        // LiugzTodo ��֤������Ϣ
        // ������ϸ��
        stateDlg.setMessage("������CAD����BOM��Ϣ�����Ժ�......");
        applyUpdatedDrawing();
      }
    });
    importThread.start();
  }

}
