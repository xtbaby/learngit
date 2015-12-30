package com.ufc.uif.qh3.acad.drawing;

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
import com.ufc.uif.qh3.acad.ui._UIFDialog;
import com.ufc.uif.tcuacommunication.operation.OperationManagerFactory;

public class QueryMatDialog extends _UIFDialog {
  private JTabbedPane tabPane;
  private Element requestBody;
  private AdaptorWriter out;
  private ImageIcon matIcon;
  private QueryMatPane queryMatPane;
  private AdvMatQueryPane advMatQueryPane;
  public QueryMatDialog(OperationManagerFactory factory) {
    super(factory);
    matIcon = IconManager.getIcon("home.png");
    tabPane = new JTabbedPane();
  }

  @Override
  public void initComponents() {
    // TODO Auto-generated method stub

  }

  @Override
  public void initConfig() {
    
  }

  @Override
  public void initListener() {
    // TODO Auto-generated method stub

  }
  public void doOperation(Element requestBody, AdaptorWriter out) throws BusiOperationException {
        this.out = out;
        this.requestBody = requestBody;
       
        if(null == queryMatPane)
          queryMatPane = new QueryMatPane(this);
        if(null == advMatQueryPane)
          advMatQueryPane = new AdvMatQueryPane( this);
        initDialog();

        //   设置 requestBody 和 out
       queryMatPane.setRequestBody(requestBody);
       queryMatPane.setOut(out);
       advMatQueryPane.setRequestBody(requestBody);
       advMatQueryPane.setOut(out);
       
  }
  private void initDialog() {
    tabPane.addTab("材料高级查询", matIcon, advMatQueryPane);
    tabPane.addTab("分类查询", matIcon, queryMatPane);
    add(tabPane);

    // 显示对话框
    setAlwaysOnTop(true);
        setPreferredSize(new Dimension(800, 600));
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setTitle("查询材料");
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

  public void closeWindow() {
    try {
      out.setFuncID("QueryMat");
      out.setResult("CloseWindow");
      out.setDesc("用户取消操作。");
      out.sendResultToUI();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    dispose();
  }


}
