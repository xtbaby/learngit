package com.ufc.uif.qh3.acad.ui;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.dom4j.Element;

import com.ufc.uif.base_adaptor.AdaptorWriter;
import com.ufc.uif.base_adaptor.busioperator.BusiOperationException;
import com.ufc.uif.base_adaptor.busioperator.IBusiOperator;
import com.ufc.uif.qh3.acad.operation.InterfaceManager;
import com.ufc.uif.tcuacommunication.objects.WSObject;
import com.ufc.uif.tcuacommunication.operation.OperationManagerFactory;
import com.ufc.uif.tcuacommunication.operation.exception.TCOperationException;

/**
 * <p>Title: _UIFDialog</p>
 * <p>Description: </p>
 * @author  Zhongning.B
 * @version $Id: Logout.java,v 1.2 2010/11/22 06:11:50 fandy Exp $
 * @Created On Aug 26, 2008
 */
public class LogoutDialog extends _UIFDialog {


    public LogoutDialog(OperationManagerFactory factory) {
      super(factory);
    }


    public void initConfig() {
    }
 
    public void doOperation(Element requestBody, AdaptorWriter out) throws BusiOperationException {
      boolean successed = true;
      int result = JOptionPane.showConfirmDialog(this,"确定要退出？", "提示",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE);
    if (result == 0) { // 是更新
      try {
        InterfaceManager.Logout();
      } catch (TCOperationException e) {
        successed = false;
        e.printStackTrace();
      }
      try {
              out.setFuncID("Logout");
              out.setResult("true");
              out.setDesc("用户退出Teamcenter");
              out.sendResultToUI();
          } catch (IOException ex) {
            successed = false;
              ex.printStackTrace();
          }
          
          if (successed){
            JOptionPane.showMessageDialog(this, "成功退出！");
          }
    } else { 
      dispose();
    }
    }


  @Override
  public void initComponents() {
    // TODO Auto-generated method stub
    
  }


  @Override
  public void initListener() {
    // TODO Auto-generated method stub
    
  }
  
}
