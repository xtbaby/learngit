package com.ufc.uif.qh3.acad;

import java.io.IOException;
import java.net.Socket;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.ufc.uif.adaptor.server.service.IComponentProcess;
import com.ufc.uif.adaptor.server.service.InitializationException;
import com.ufc.uif.base_adaptor.AdaptorWriter;
import com.ufc.uif.base_adaptor.BaseAdaptor;
import com.ufc.uif.base_adaptor.busioperator.BusiOperationException;
import com.ufc.uif.qh3.acad.ui.FindWSObjectFrame;
import com.ufc.uif.qh3.acad.ui.LoginDialog;
import com.ufc.uif.qh3.acad.ui.LogoutDialog;
import com.ufc.uif.qh3.acad.ui.OpenDrawingDialog; //import com.ufc.uif.qh3.acad.ui.QueryParts;
import com.ufc.uif.qh3.acad.ui.SaveDrawingDialog;
import com.ufc.uif.qh3.acad.ui.SetUserOptDialog;
import com.ufc.uif.tcuacommunication.connecttc.SessionManager;
import com.ufc.uif.tcuacommunication.operation.ITCBasicFunction; //import com.ufc.uif.tcuacommunication.operation.ITCClassificationOperation;
import com.ufc.uif.tcuacommunication.operation.ITCFileManagement;
import com.ufc.uif.tcuacommunication.operation.ITCObjectOperation;
import com.ufc.uif.tcuacommunication.operation.ITCStructureOperation;
import com.ufc.uif.tcuacommunication.operation.ITCTCBOMOperation;
import com.ufc.uif.tcuacommunication.operation.ITCTCObjOperation;
import com.ufc.uif.tcuacommunication.operation.OperationManagerFactory;
import com.ufc.uif.util.service.ServiceUtil;

public class Qh3_CADAdaptor extends BaseAdaptor implements IComponentProcess {

  Thread t = null;

  public Qh3_CADAdaptor(Socket client) throws InitializationException {
    super(client);
  }

  @Override
  protected void setOperators() throws InitializationException {
    try {
      System.out.println("setOperators..........");
      OperationManagerFactory factory = getOManagerFactory();
      SessionManager.setFactory(factory);
      operatorpairs.put("Login", new LoginDialog(factory));
      operatorpairs.put("Logout", new LogoutDialog(factory));
      operatorpairs.put("SaveDrawing", new SaveDrawingDialog(factory));
      // operatorpairs.put("doQuery", new QueryItemDialog(factory));
      operatorpairs.put("doQuery", new FindWSObjectFrame(factory));
      // operatorpairs.put("QueryPart", new QueryParts());
      operatorpairs.put("OpenDrawing", new OpenDrawingDialog(factory));
      operatorpairs.put("SetUserOpt", new SetUserOptDialog(factory));

    } catch (DocumentException ex) {
      InitializationException iex = new InitializationException("∂¡»°≈‰÷√Œƒº˛ ß∞‹£¨" + ex.getMessage());
      iex.setStackTrace(ex.getStackTrace());
      throw iex;
    } catch (IOException ex) {
      InitializationException iex = new InitializationException("Ω‚Œˆ≈‰÷√Œƒº˛ ß∞‹£¨" + ex.getMessage());
      iex.setStackTrace(ex.getStackTrace());
      throw iex;
    }
  }

  @Override
  protected void login(final Element requestBody, final AdaptorWriter out) throws BusiOperationException {
    operatorpairs.get("Login").doOperation(requestBody, out);

    /*
     * t= new Thread(new Runnable(){
     * 
     * @SuppressWarnings("deprecation")
     * 
     * @Override public void run() { if(SessionManager.isLogin()){ try {
     * operatorpairs.get("SaveDrawing").doOperation(requestBody, out);
     * t.stop(); } catch (BusiOperationException e) { // TODO Auto-generated
     * catch block e.printStackTrace(); } } } }); t.start();
     */

  }

  /*
   * public InputStream getConfigFile() { return
   * this.getClass().getResourceAsStream("tc_adaptor_config.xml"); }
   */

  private OperationManagerFactory getOManagerFactory() throws DocumentException, IOException {
    OperationManagerFactory factory = new OperationManagerFactory();
    // factory.setTcbasicFunction((ITCBasicFunction)
    // ServiceUtil.getService(ITCBasicFunction.class.getName(),
    // this.getClass().getClassLoader()));
    factory.setBaseicFunction((ITCBasicFunction) ServiceUtil.getService(ITCBasicFunction.class.getName(), this.getClass().getClassLoader()));
    // factory.setClassficOp((ITCClassificationOperation)
    // ServiceUtil.getService(ITCClassificationOperation.class.getName(),
    // this.getClass().getClassLoader()));
    factory.setFileManage((ITCFileManagement) ServiceUtil.getService(ITCFileManagement.class.getName(), this.getClass().getClassLoader()));
    factory.setObjOp((ITCObjectOperation) ServiceUtil.getService(ITCObjectOperation.class.getName(), this.getClass().getClassLoader()));
    factory.setStructureOp((ITCStructureOperation) ServiceUtil.getService(ITCStructureOperation.class.getName(), this.getClass().getClassLoader()));
    factory.setBomOp((ITCTCBOMOperation) ServiceUtil.getService(ITCTCBOMOperation.class.getName(), this.getClass().getClassLoader()));
    factory.setTcop((ITCTCObjOperation) ServiceUtil.getService(ITCTCObjOperation.class.getName(), this.getClass().getClassLoader()));
    return factory;
  }

  @Override
  protected void login() throws BusiOperationException {
    operatorpairs.get("Login").doOperation(null, null);
  }

  public String getComponentName() {
    // TODO Auto-generated method stub
    return "ACAD";
  }

  public Thread getProcessThread(Socket arg0) throws InitializationException {
    // TODO Auto-generated method stub
    return new Thread(this);
  }

  public Thread getProcessThread(Socket arg0, Document arg1) throws InitializationException {
    // TODO Auto-generated method stub
    return null;
  }

}
