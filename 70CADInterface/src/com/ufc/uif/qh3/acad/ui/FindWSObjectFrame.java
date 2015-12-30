package com.ufc.uif.qh3.acad.ui;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import org.dom4j.Element;

import com.ufc.uif.base_adaptor.AdaptorWriter;
import com.ufc.uif.base_adaptor.busioperator.BusiOperationException;
import com.ufc.uif.qh3.acad.images.IconManager;
import com.ufc.uif.qh3.acad.operation.ParseRequestXML;
import com.ufc.uif.tcuacommunication.operation.OperationManagerFactory;

/**
 *
 */

/**
 * @author Liugz
 *
 */
public class FindWSObjectFrame extends _UIFDialog {

  private JTabbedPane tabPane;
  private MyTeamcenterPanel myHomePane;
  private AdvQueryPane advquerypane;
  private ClassificationPanel classificationPane;
  private String startpointValue;
  ImageIcon homeIcon = null;
  ImageIcon queryIcon = null;
  ImageIcon classifiIcon = null;
  private Element requestBody;
  private AdaptorWriter out;

  public FindWSObjectFrame(OperationManagerFactory factory) {
    super(factory);
    homeIcon = IconManager.getIcon("home.png");
    queryIcon=IconManager.getIcon("query.png");
    classifiIcon = IconManager.getIcon("classifying.png");
    tabPane = new JTabbedPane();
  }

  private void initDialog(){
//    tabPane = new JTabbedPane();
//    myHomePane = new WorkspaceObjectFrame(requestBody, out, this);
//    classificationPane = new ClassificationFrame(requestBody, out, this);
    // 为Tab页添加监听器
//    tabPane.addChangeListener(new ChangeListener(){
//
//      public void stateChanged(ChangeEvent e) {
//        // 当前选中的是哪个Tab页
//        int selected = tabPane.getSelectedIndex();
//        if(selected == 0){
//          tabPane.setIconAt(0, homeIcon);
//        }
//        if(selected == 1){
//          tabPane.setIconAt(1, classifiIcon);
//        }
//      }
//
//    });
    //判断是标题栏还是明细栏发送的请求
    tabPane.addTab("分类对象", classifiIcon, classificationPane);
    tabPane.addTab("我的TeamCenter", homeIcon, myHomePane);
    tabPane.addTab("高级查询", queryIcon, advquerypane);
    /*
    if(startpointValue.contains("edittitle")){
    }else
    {
      tabPane.addTab("分类对象", classifiIcon, classificationPane);
    }
    */
    add(tabPane);

    // 显示对话框
    setAlwaysOnTop(true);
        setPreferredSize(new Dimension(800, 600));
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setTitle("查询WorkspaceObject对象");
    Toolkit tool = Toolkit.getDefaultToolkit();
      Dimension scrSize = tool.getScreenSize();
      Dimension dlgSize = this.getPreferredSize();
      int width = (scrSize.width - dlgSize.width) / 2 ;
      int height = (scrSize.height - dlgSize.height) / 2;

      addWindowListener(new WindowAdapter(){
      public void windowClosing(WindowEvent e) {
        closeWindow();
      }
      });
        setLocation(width/2, height/2);
        setVisible(true);
        pack();
  }

  @Override
  public void initConfig() {
    // TODO Auto-generated method stub

  }

  @Override
  public void initListener() {
    // TODO Auto-generated method stub

  }

  @Override
  public void doOperation(Element requestBody, AdaptorWriter out) throws BusiOperationException {
        this.out = out;
        this.requestBody = requestBody;
        
        startpointValue = ParseRequestXML.getStartpoint(requestBody);
        
        if(null == myHomePane){
          myHomePane = new MyTeamcenterPanel(this);
        }
        if(null == advquerypane){
          advquerypane = new AdvQueryPane(this);
        }
        
        advquerypane.iniDialog(startpointValue);
        
        if(null == classificationPane){
          classificationPane = new ClassificationPanel(this,startpointValue);
        }else{
          classificationPane.setQueryType(startpointValue);
          classificationPane.refreshTree();
        }
        
        //   设置 requestBody 和 out
        classificationPane.setRequestBody(requestBody);
        classificationPane.setOut(out);
        myHomePane.setRequestBody(requestBody);
        myHomePane.setOut(out);
        advquerypane.setRequestBody(requestBody);
        advquerypane.setOut(out);
        
        initDialog();
  }

  @Override
  public void initComponents() {
    // TODO Auto-generated method stub

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
    dispose();
  }

}
