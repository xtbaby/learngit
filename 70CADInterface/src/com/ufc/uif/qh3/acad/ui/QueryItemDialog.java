package com.ufc.uif.qh3.acad.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;

import org.dom4j.Element;

import com.ufc.uif.base_adaptor.AdaptorWriter;
import com.ufc.uif.base_adaptor.busioperator.BusiOperationException;
import com.ufc.uif.qh3.acad.images.IconManager;
import com.ufc.uif.qh3.acad.operation.ParseConfigXMl;
import com.ufc.uif.qh3.acad.operation.ParseRequestXML;
import com.ufc.uif.qh3.acad.operation.SaveDrawingOperation;
import com.ufc.uif.qh3.acad.util.UFCComboBox;
import com.ufc.uif.qh3.acad.util.UFCTextField;
import com.ufc.uif.tcuacommunication.objects.WSObject;
import com.ufc.uif.tcuacommunication.operation.ITCObjectOperation;
import com.ufc.uif.tcuacommunication.operation.ITCTCObjOperation;
import com.ufc.uif.tcuacommunication.operation.OperationManagerFactory;
import com.ufc.uif.tcuacommunication.operation.exception.TCOperationException;
import com.ufc.uif.util.service.ServiceUtil;

public class QueryItemDialog extends _UIFDialog {

//  private int widthFrame, labelWidth, spacing, comWidth, startX;
  private ITCObjectOperation op;
  private ITCTCObjOperation  tcop;
  
  private SaveDrawingOperation sdo;
  
  private Map<String, List<Map<String, String>>> queryComInfo;
  private Map<String, List<Map<String, String>>> propComInfo;
  private Map<String, String> queryNameInfo;
  
  private WaitDialog waitDlg;
  private JFrame frame;
  
  public QueryItemDialog(OperationManagerFactory factory) {
    super(factory);
    
    frame = this;
    
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
    
    /*
    widthFrame = 600;
    labelWidth = 70;
    spacing = 10;
    comWidth = 150;
    startX = 20;
    this.setSize(widthFrame, 700);
    */
    this.setLocationRelativeTo(null);
    
    sdo = new SaveDrawingOperation();
    op =(ITCObjectOperation)ServiceUtil.getService(ITCObjectOperation.class.getName(), SaveDrawingDialog.class.getClassLoader());
    tcop = (ITCTCObjOperation)ServiceUtil.getService(ITCTCObjOperation.class.getName(), SaveDrawingDialog.class.getClassLoader());
  }

  /**
   * TODO
   * @author lijj
   * created on 2011-9-1下午04:57:03
   */
  private static final long serialVersionUID = 1L;

  
  public void doOperation(Element requestBody, AdaptorWriter out) throws BusiOperationException {
    super.doOperation(requestBody, out);
    String queryType = ParseRequestXML.getStartpoint(requestBody);
    
    jComboBox1.removeAllItems();
    
    if(queryType.equalsIgnoreCase("all")){
      Iterator<?> it = queryNameInfo.entrySet().iterator();
      while(it.hasNext()){
        Map.Entry entry = (Map.Entry)it.next();
        String key = (String)entry.getKey();
        jComboBox1.addItem(key);
      }
      
      jComboBox1.setEnabled(true);
      jComboBox1.setEditable(false);
      
      configQueryPanel((String)jComboBox1.getItemAt(0));
      configPropPanel((String)jComboBox1.getItemAt(0));
    }else{
      jComboBox1.addItem(queryType);
          jComboBox1.setEnabled(false);
          jComboBox1.setEditable(false);
      configQueryPanel(queryType);
      configPropPanel(queryType);
//      initSearchResult();
    }
    
    WorkspaceTableModel tableModel = new WorkspaceTableModel(null);
      wsObjTable.setModel(tableModel);
      wsObjTable.updateUI();
  }
  
  @Override
  public void initComponents() {
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
        jComboBox1 = new javax.swing.JComboBox();
        mainPanel = new javax.swing.JPanel();
        
//        setBackground(new java.awt.Color(226, 245, 252));

        jSplitPane1.setDividerLocation(260);
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
        /*okBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
//                okBtnActionPerformed(evt);
            }
        });*/

        cancelBtn.setText("取消");
        /*cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
//              cancelBtnActionPerformed(evt);
            }
        });*/

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(339, Short.MAX_VALUE)
                .add(okBtn)
                .add(32, 32, 32)
                .add(cancelBtn)
                .add(21, 21, 21))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cancelBtn)
                    .add(okBtn))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        /*wsObjTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));*/
        wsObjTable.getTableHeader().setBackground(new java.awt.Color(226, 245, 252));
        queryResultTableRender tableRender = new queryResultTableRender();
      wsObjTable.setDefaultRenderer(String.class, tableRender);
      
//        setTableMouseListener();
        
        jScrollPane2.setViewportView(wsObjTable);
        jScrollPane2.getViewport().setBackground(new java.awt.Color(255, 255, 255));

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 526, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        jSplitPane2.setRightComponent(jPanel2);

        prosPanel.setBackground(new java.awt.Color(226, 245, 252));
        prosPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("属性"));

        org.jdesktop.layout.GroupLayout prosPanelLayout = new org.jdesktop.layout.GroupLayout(prosPanel);
        prosPanel.setLayout(prosPanelLayout);
        prosPanelLayout.setHorizontalGroup(
            prosPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 526, Short.MAX_VALUE)
        );
        prosPanelLayout.setVerticalGroup(
            prosPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 210, Short.MAX_VALUE)
        );

        jSplitPane2.setLeftComponent(prosPanel);

        jSplitPane1.setRightComponent(jSplitPane2);

        jPanel1.setBackground(new java.awt.Color(226, 245, 252));
        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        queryPanel.setBackground(new java.awt.Color(226, 245, 252));
        queryPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("搜索条件："));

        org.jdesktop.layout.GroupLayout queryPanelLayout = new org.jdesktop.layout.GroupLayout(queryPanel);
        queryPanel.setLayout(queryPanelLayout);
        queryPanelLayout.setHorizontalGroup(
            queryPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 193, Short.MAX_VALUE)
        );
        queryPanelLayout.setVerticalGroup(
            queryPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 392, Short.MAX_VALUE)
        );
        
        clrBtn.setIcon(IconManager.getIcon("clear.png"));
        
        searchBtn.setIcon(IconManager.getIcon("query.png"));
       
        comboxPanel.setBackground(new java.awt.Color(226, 245, 252));

        jLabel1.setForeground(new java.awt.Color(0, 70, 213));
        jLabel1.setText("查询类型：");
        
        org.jdesktop.layout.GroupLayout comboxPanelLayout = new org.jdesktop.layout.GroupLayout(comboxPanel);
        comboxPanel.setLayout(comboxPanelLayout);
        comboxPanelLayout.setHorizontalGroup(
            comboxPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, comboxPanelLayout.createSequentialGroup()
                .add(10, 10, 10)
                .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 112, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(24, 24, 24))
        );
        comboxPanelLayout.setVerticalGroup(
            comboxPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(comboxPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(comboxPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, comboxPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                        .add(clrBtn, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 50, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(searchBtn, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 55, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, queryPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(comboxPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(queryPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(clrBtn)
                    .add(searchBtn))
                .addContainerGap())
        );

        jSplitPane1.setLeftComponent(jPanel1);

        
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(mainPanel);
        mainPanel.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 767, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jSplitPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 505, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        
        setContentPane(mainPanel);
        setTitle("查询");
        pack();
  }
  
  private javax.swing.JButton cancelBtn;
    private javax.swing.JButton clrBtn;
    private javax.swing.JPanel comboxPanel;
    private javax.swing.JComboBox jComboBox1;
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
    private javax.swing.JPanel mainPanel;

    /**
     * 
     * TODO
     * @return void
     * @author lijj
     * created on 2011-9-2下午03:10:00
     */
    private void setTableMouseListener() {
        wsObjTable.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          if (MouseEvent.BUTTON1 == e.getButton() && 1 == e.getClickCount()) {
            int row = wsObjTable.getSelectedRow();
            WSObject wsObj = ((WorkspaceTableModel)wsObjTable.getModel()).getWSObj(row);
            
            Map<String, String> attrib = getDisNameValueMap(wsObj);
            String itemId = ((WorkspaceTableModel)wsObjTable.getModel()).getObjectId(row);
            String itemName = wsObj.getName();
            
            fill2prosPanel(attrib,itemId, itemName);
            
          }
        }
      });
    }
    
    private Map<String, String> getDisNameValueMap(WSObject wsObj){
      Map<String, String> resultDisNameValueMap = new HashMap<String, String>();
      Component[] coms = prosPanel.getComponents();
      for(int i = 0; i < coms.length; i++){
        String displayName = "";
        String propName = "";
        String value = "";
        if(coms[i] instanceof UFCTextField){
          UFCTextField ufcTextField= (UFCTextField)coms[i];
          displayName = ufcTextField.getDisplayName();
          propName = ufcTextField.getProperty();
          value = tcop.getPropertyValueOfObject(wsObj, propName);
        }
        
        if(coms[i] instanceof UFCComboBox){
          UFCComboBox ufcCombo=(UFCComboBox)coms[i];
          displayName = ufcCombo.getDisplayName();
          propName = ufcCombo.getProperty();
          value = tcop.getPropertyValueOfObject(wsObj, propName);
        }
        
        resultDisNameValueMap.put(displayName, value);
      }
      return resultDisNameValueMap;
    }
    
    /**
     * 向属性面板添加属性
     * @param attrib
   * @param itemName 
     */
    private void fill2prosPanel(Map<String, String> attrib, String itemID, String itemName) {
      //根据选中的table行信息，初始化控件的值
      Component[] coms = prosPanel.getComponents();
      
      for(int i = 0; i < coms.length; i++){
        if(coms[i] instanceof UFCTextField){
          UFCTextField utf = (UFCTextField)coms[i];
          String s = attrib.get(utf.getDisplayName());
          if(s == null){
            s = "";
          }
          utf.setText(s);
          
          if(utf.getProperty().equals("object_name")){
            utf.setText(itemName);
          }
          
          if(utf.getProperty().equals("item_id")){
            utf.setText(itemID);
          }
          
          utf.setEditable(false);
//          utf.setOpaque(false);
        }
        
        
        if(coms[i] instanceof UFCComboBox){
        UFCComboBox ucb = (UFCComboBox)coms[i];
          String s=attrib.get(ucb.getDisplayName());
          if(s==null){
            s="";
          }
          ucb.setSelectedItem(s);
          ucb.setEnabled(false);//.setEditable(false);
//          ucb.setOpaque(false);
        }
      }  
        
      System.out.println("=========>添加属性面板成功");
    }
    
    /**
   * 
   * TODO
   * @return void
   * @author lijj
   * created on 2011-8-25下午02:17:28
   */
  private void configQueryPanel(String displayType){
    List<Map<String, String>> queryComponentInfoList = queryComInfo.get(displayType);
  
    queryPanel.removeAll();
      //根据配置文件生成控件
      for(int i = 0; i < queryComponentInfoList.size(); i++){
        JLabel label = new JLabel();
        JComponent tempComponent = null;
        Map<String, String> componentInfo = queryComponentInfoList.get(i);
        
        String displayName = componentInfo.get("displayName");
        String property = componentInfo.get("property");
        String source = componentInfo.get("source");
        String required = componentInfo.get("isRequired");
        String type = componentInfo.get("type");
        
        label.setText(displayName + "：");
        label.setHorizontalAlignment(SwingConstants.RIGHT); // 对其方式设置
        
        if(type.equals("TEXT")){
          UFCTextField ufcFiled = new UFCTextField();
          
          ufcFiled.setDisplayName(displayName);
          ufcFiled.setProperty(property);
          ufcFiled.setObjType(source);
          if(required.equalsIgnoreCase("true")){
            ufcFiled.setRequired(true);
          }else{
            ufcFiled.setRequired(false);
          }
          
          tempComponent = ufcFiled;
          
        }else if(type.equals("LOV")){
          UFCComboBox ufcCombo = new UFCComboBox();
          
          ufcCombo.setDisplayName(displayName);
          ufcCombo.setProperty(property);
          ufcCombo.setObjType(source);
          if(required.equalsIgnoreCase("true")){
            ufcCombo.setRequired(true);
          }else{
            ufcCombo.setRequired(false);
          }
          
        Map<String, String[]> lovValue = null;
        lovValue = sdo.getValuesOfLOV(source, new String[] {property});
        
        if (null != lovValue) {
          String tempArray[] = lovValue.get(property);
          ufcCombo.addItem("");
          for (int j = 0; j < tempArray.length; j++) {
            ufcCombo.addItem(tempArray[j]);
          }
        }
          
          tempComponent = ufcCombo;
        }else{
          System.out.println("该type类型未处理");
        }
        
        //对查询面板进行控件初始化
      label.setBounds(new Rectangle(10, (i+20)+(i*30), 90, 22));
      tempComponent.setBounds(new Rectangle(100, (i+20)+(i*30), 100, 22));
        queryPanel.add(label);
        queryPanel.add(tempComponent);
        //对查询面板进行控件初始化
        
      }
      queryPanel.updateUI();
  }
  
  /**
   * 
   * TODO
   * @return void
   * @author lijj
   * created on 2011-8-25下午02:17:28
   */
  private void configPropPanel(String displayType){
    List<Map<String, String>> propComponentInfoList = propComInfo.get(displayType);
    
      prosPanel.removeAll();
      //根据配置文件生成控件
      for(int i = 0; i < propComponentInfoList.size(); i++){
        JLabel label = new JLabel();
        JComponent tempComponent = null;
        Map<String, String> componentInfo = propComponentInfoList.get(i);
        
        String displayName = componentInfo.get("displayName");
        String property = componentInfo.get("property");
        String source = componentInfo.get("source");
        String type = componentInfo.get("type");
        
        label.setText(displayName + "：");
        label.setHorizontalAlignment(SwingConstants.RIGHT); // 对其方式设置
        
        if(type.equals("TEXT")){
          UFCTextField ufcFiled = new UFCTextField();
          
          ufcFiled.setDisplayName(displayName);
          ufcFiled.setProperty(property);
          ufcFiled.setObjType(source);

          ufcFiled.setEnabled(false);
          tempComponent = ufcFiled;
          
        }else if(type.equals("LOV")){
          UFCComboBox ufcCombo = new UFCComboBox();
          
          ufcCombo.setDisplayName(displayName);
          ufcCombo.setProperty(property);
          ufcCombo.setObjType(source);
          

        Map<String, String[]> lovValue = null;
        lovValue = sdo.getValuesOfLOV(source, new String[] {property});
        
        if (null != lovValue) {
          String tempArray[] = lovValue.get(property);
          ufcCombo.addItem("");
          for (int j = 0; j < tempArray.length; j++) {
            ufcCombo.addItem(tempArray[j]);
          }
        }
          
        ufcCombo.setEnabled(false);
          tempComponent = ufcCombo;
        }else{
          System.out.println("该type类型未处理");
        }
        
        //对属性显示面板进行控件初始化
        if(i%2 == 0){
          label.setBounds(new Rectangle(20, (i+30)+(i*15), 90, 22));
          tempComponent.setBounds(new Rectangle(110, (i+30)+(i*15), 120, 22));
        }else{
          label.setBounds(new Rectangle(260, (i+30)+((i-1)*15),90, 22));
          tempComponent.setBounds(new Rectangle(350, (i+30)+((i-1)*15), 120, 22));
          
        } 
        prosPanel.add(label);
        prosPanel.add(tempComponent);
        //对属性显示面板进行控件初始化
      }
      prosPanel.updateUI();
  }
  
  /**
   * 
   * TODO
   * @return void
   * @author lijj
   * created on 2011-8-25下午02:17:20
   */
    private void initProsPanel(Map<String, String> attrib){
      //根据选中的table行信息，初始化控件的值
      Component[] components =prosPanel. getComponents();     
      for(int i = 0; i < components.length; i++){
        if(components[i] instanceof UFCTextField){
          UFCTextField utf = (UFCTextField)components[i];
          String value = attrib.get(utf.getProperty());
          if(value == null){
            value = "";
          }
          utf.setText(value);
          utf.setEditable(false);
          utf.setOpaque(false);
        }else if(components[i] instanceof UFCComboBox){
            UFCComboBox ucb = (UFCComboBox)components[i];
              String value = attrib.get(ucb.getProperty());
              if(value == null){
                value ="";
              }
              ucb.setSelectedItem(value);
              ucb.setEnabled(false);
              ucb.setOpaque(false);
        }else{
          //nothing
        }
      }  
    }
    
  @Override
  public void initConfig() {
    String UFCROOT = System.getenv("UFCROOT");
    String fileSeparator = System.getProperty("file.separator");
    String  configFilePath = UFCROOT + fileSeparator + "AutoCAD" + fileSeparator + "cfg" + fileSeparator + "queryModel.xml";
    
    ParseConfigXMl parseCfg = new ParseConfigXMl();
    Element rootNode = parseCfg.getRootNode(configFilePath);
    queryComInfo = parseCfg.getQueryComponentInfo(rootNode);
    propComInfo = parseCfg.getPropComponentInfo(rootNode);
    queryNameInfo = parseCfg.getQueryName(rootNode);
  }

  @Override
  public void initListener() {
    jComboBox1.addItemListener(new ItemListener(){
      public void itemStateChanged(ItemEvent e) {
        if(e.getStateChange() == ItemEvent.SELECTED){
          String displayType = (String) (jComboBox1.getSelectedItem());
          configQueryPanel(displayType);
          configPropPanel(displayType);
        }
      }
    });
    
    clrBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
              clearQueryPanel(queryPanel);
            }
        });
    
     searchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
              searchBtnActionPerformed();
            }
     });
     
       okBtn.addActionListener(new java.awt.event.ActionListener() {
              public void actionPerformed(java.awt.event.ActionEvent evt) {
                  okBtnActionPerformed(evt);
              }
        });
       
       cancelBtn.addActionListener(new java.awt.event.ActionListener() {
              public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
              }
        });
       
       setTableMouseListener();
  }  
  
  /**
   * 
   * TODO 确定按钮触发的事件
   * @return void
   * @author lijj
   * created on 2011-9-2下午01:59:40
   */
  private void okBtnActionPerformed(java.awt.event.ActionEvent evt) {                                      
    if(wsObjTable.getSelectedRowCount() == 0){
      JOptionPane.showMessageDialog(this, "请从表格中选择需要的数据！", "提示", JOptionPane.INFORMATION_MESSAGE);
      return ;
    }
    // 将选中的数据放到Map中
      
    int row = wsObjTable.getSelectedRow();
    WorkspaceTableModel tableModel = (WorkspaceTableModel) wsObjTable.getModel();
    
    WSObject tempObj = tableModel.getWSObj(row);
    String itemId = tableModel.getObjectId(row);
    String itemName = "";
//    String itemType = "";
    
    for(int i = 0; i < wsObjTable.getColumnCount(); i++){
      int realIndex = wsObjTable.convertColumnIndexToModel(i);
      String colName = wsObjTable.getColumnName(realIndex);
      String value = (String)wsObjTable.getValueAt(row, realIndex);
      
      
      if(colName.equals("对象名称")){
        itemName = value;
      }
      
      /*if(colName.equals("对象类型")){
        itemType = value;
      }*/
    }
    
    Map<String, String> attrMap = getDisNameValueMap(tempObj);
    getResponseXML(attrMap, itemId, itemName);
    
    frame.dispose();
    }
  
  /**
   * 
   * TODO 查询请求会送消息生成
   * @return void
   * @author lijj
   * created on 2011-9-2下午02:23:04
   */
  private void getResponseXML(Map<String, String> infos, String id, String itemName) {
        // 修改请求中的信息
    
    out.setFuncID("doQuery");
    out.setResult("true");
    out.setDesc("Backfill CAD Drawing.");
    Element objectEle = out.getObjectElement();
//    objectEle.addAttribute("type", type);
    
    Element props = objectEle.addElement("properties");
    
    Element proID = props.addElement("property");
    proID.addAttribute("name","item_id");
    proID.addAttribute("value",id );
    
    Element proName = props.addElement("property");
    proName.addAttribute("name","名称");
    proName.addAttribute("value", itemName);
    
    for(Iterator<String> keyItor = infos.keySet().iterator(); keyItor.hasNext();){
      String key = keyItor.next();
      String value = infos.get(key);
      
      Element proInfo = props.addElement("property");
      proInfo.addAttribute("name", key);
      proInfo.addAttribute("value", value);
    }
    
    /*
    Element proname=props.addElement("property");
    if(infos.get("材料简称").equals("")){
      proname.addAttribute("name","材料名称");
      proname.addAttribute("value",itemName);
    }else{
      proname.addAttribute("name","材料名称");
      proname.addAttribute("value",infos.get("材料简称"));
    }
    */
    /*
    for(int i=0;i<ks.length;i++){
      Element pro1=props.addElement("property");
      String name=ks[i].toString();
      if(name.equals("重量")){
        name="质量";
      }
      pro1.addAttribute("name",name);
      pro1.addAttribute("value",infos.get(ks[i].toString()) );
    }
    String matdown = infos.get("材质标准");
    String matGG=infos.get("材料规格");
    String matg1=infos.get("材质规格");
    String matstarded=infos.get("材料标准");
    String matUp=matg1+" "+matGG+" "+matstarded;
    if(!okflag){
      matUp=matg1+" "+matstarded;
    }
    if(infos.get("材料种类").equals("非金属材料")){
      matdown=infos.get("材料标准");
      matUp=matGG;
    }else if(infos.get("材料种类").equals("材质")){
      matUp=matg1;
    }
    Element promatup=props.addElement("property");
    promatup.addAttribute("name","材料上标");
    promatup.addAttribute("value",matUp);
      
    Element promatdown=props.addElement("property");
    promatdown.addAttribute("name","材料下标");
    promatdown.addAttribute("value",matdown);
    */
    try {
      out.sendResultToUI();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * 
   * TODO 取消按钮触发的事件
   * @return void
   * @author lijj
   * created on 2011-9-2下午02:00:25
   */
  private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {                                          
      // 关闭按钮
    int r = JOptionPane.showConfirmDialog(this, "确定退出？", "提示", JOptionPane.YES_NO_CANCEL_OPTION);
    if(r == JOptionPane.OK_OPTION){
      closeWindow();
    }
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
  
  /**
   * 
   * TODO
   * @return void
   * @author lijj
   * created on 2011-9-2上午11:37:29
   */
  private void searchBtnActionPerformed() {                                          
      Thread thread = new Thread(new Runnable(){
      public void run() {
        if(waitDlg != null)
          waitDlg.dispose();

        waitDlg = new WaitDialog(frame, false);
        waitDlg.setMessage("正在进行操作，请稍候……");
        frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        searchWSObj();
        frame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        waitDlg.dispose();
      }
    });
    thread.start();
  }

  /**
   * 
   * TODO 查询按钮的查询操作
   * @return void
   * @author lijj
   * created on 2011-9-2上午11:44:34
   */
  private void searchWSObj(){
    Map<String,String> criterias = getDisplayNameValueMap(queryPanel);
    /*
    for(Iterator<String> keyItor = criterias.keySet().iterator(); keyItor.hasNext();){
      String key = keyItor.next();
      String value1 = criterias.get(key);
    }
    */
    System.out.println("criterias=" + criterias);
    String queryType = (String)jComboBox1.getSelectedItem();
    String queryName = queryNameInfo.get(queryType) ;
      WSObject[] foundObjs = null;
      try {
        foundObjs = tcop.queryObjectsBySavedQuery(queryName, criterias);
        if(foundObjs != null){
          //不知道为什么会查到空对象，故需要处理查询结果将所有为null的对象过滤掉
          List<WSObject> queryWSObjs = new ArrayList<WSObject>();
          for(int i = 0; i < foundObjs.length; i++){
            if(null != foundObjs[i]){
              queryWSObjs.add(foundObjs[i]);
            }
          }
          
          WSObject[] resultWSObjArr = new WSObject[queryWSObjs.size()];
          for(int i = 0; i < queryWSObjs.size(); i++){
            resultWSObjArr[i] = queryWSObjs.get(i);
          }
          //不知道为什么会查到空对象，故需要处理查询结果将所有为null的对象过滤掉  
          WorkspaceTableModel tableModel = new WorkspaceTableModel(resultWSObjArr);
          
          wsObjTable.setModel(tableModel);
          wsObjTable.validate();
          TableRowSorter sorter = new TableRowSorter(tableModel);  
          wsObjTable.setRowSorter(sorter);
//          wsObjTable.updateUI();
        }else{
          JOptionPane.showMessageDialog(this, "没有查找到相关对象！");
          wsObjTable.setModel(null);
        }
        
    } catch (TCOperationException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(this, "查询系统对象时发生错误，错误原因如下：\n" + e.getMessage(),
              "错误", JOptionPane.ERROR_MESSAGE);
      return ;
    }
  }
  
    /**
     * 获得查询条件界面中的属性
     * @param qyPanel 查询条件界面的控件
     * @param parttype 所查询的零部件类型
     */
      private Map<String, String> getDisplayNameValueMap(JPanel qyPanel) {
        Map<String, String> displayNameValueMap = new HashMap<String, String>();
          Component[] coms=qyPanel.getComponents();
          //注意现在构建的查询中基本查询条件均为属性的真实名称，故生成的map的key值也为property值，名称除外
        for(int i = 0; i < coms.length; i++){
          if(coms[i] instanceof UFCTextField){
            UFCTextField ufcTextField= (UFCTextField)coms[i];
            if(ufcTextField.getDisplayName().equals("名称")){
              //查询条件是中文只有“名称”
              String value = ufcTextField.getText();
              if(null != value && value.trim().length() > 0){
                displayNameValueMap.put("名称",value);
              }
            }else{
              if(null != ufcTextField.getProperty() && ufcTextField.getProperty().length() > 0 ){
                String value = ufcTextField.getText();
                if(null != value && value.trim().length() > 0){
                  displayNameValueMap.put(ufcTextField.getProperty(),value);
                }
              }
            }
          }
          
          if(coms[i] instanceof UFCComboBox){
            UFCComboBox ufcCombo=(UFCComboBox)coms[i];
            if(null != ufcCombo.getProperty() && ufcCombo.getProperty().length() > 0){
              String value = ufcCombo.getSelectedItem().toString();
              if(null != value && value.length() > 0){
                displayNameValueMap.put(ufcCombo.getProperty(),value);
              }
            }
          }
        }
        return displayNameValueMap;
    }
  
  private void initSearchResult(){
      WorkspaceTableModel tableModel = new WorkspaceTableModel(null);
      wsObjTable.setModel(tableModel);
      queryResultTableRender tableRender = new queryResultTableRender();
      wsObjTable.setDefaultRenderer(String.class, tableRender);
      wsObjTable.validate();
      wsObjTable.updateUI();
      TableRowSorter sorter = new TableRowSorter(wsObjTable.getModel());  
      wsObjTable.setRowSorter(sorter);
    }
  
  
  /**
   * 清除queryPanel上所有控件的内容
   * @param queryPanel2
   */
    private void clearQueryPanel(JPanel queryPanel) {
      Component[] coms = queryPanel.getComponents();
      for(int i=0;i<coms.length;i++){
        if(coms[i]instanceof UFCTextField){
          UFCTextField uta=(UFCTextField)coms[i];
          if(!uta.getText().equals("")||uta.getText()!=null){
            uta.setText("");
          }
        }else if(coms[i]instanceof UFCComboBox){
          UFCComboBox ucb=(UFCComboBox)coms[i];
          ucb.setSelectedItem("");
        }
      }
  }
    
    class queryResultTableRender extends DefaultTableCellRenderer {
      Color bgColor = new Color(241,250,255);
      Color bgColor2 = new Color(191,214,248);
      Color selectedColor = new Color(49,106,197);
      Color white = new Color(255, 255, 255);
      Color black = new Color(0, 0, 0);
      boolean flag = false;
      
      @Override
      public Component getTableCellRendererComponent(JTable table, Object value,
          boolean isSelected, boolean hasFocus, int row, int column) {
        // TODO Auto-generated method stub
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus,row, column);

        setForeground(black);
        if(!isSelected){
          if(row % 2 == 0){
            setBackground(bgColor);
          }else{
            setBackground(bgColor2);
          }
        }else{
          setForeground(white);
          setBackground(selectedColor);
          flag=true;
        }
        return this;
      }
    }

}
