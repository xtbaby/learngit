package com.ufc.uif.qh3.acad.operation;

import java.io.File;
import java.util.Map;
import com.ufc.uif.tcuacommunication.operation.ITCBasicFunction;
import com.ufc.uif.tcuacommunication.operation.OperationManagerFactory;
import com.ufc.uif.tcuacommunication.operation.exception.TCOperationException;
import com.ufc.uif.util.service.ServiceUtil;

/**
 * <p>
 * Title: UIFManager
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author Zhongning.B
 * @version $Id: CADManager.java,v 1.3 2010/12/01 05:56:04 wangjf Exp $
 * @Created On Aug 29, 2008
 */
public class InterfaceManager {

  public static boolean Login(String name, String pwd, String group, String role, String sessioncredit) throws TCOperationException {

    ITCBasicFunction loginManager = (ITCBasicFunction) ServiceUtil.getService(ITCBasicFunction.class.getName(), InterfaceManager.class.getClassLoader());
    return loginManager.login(name, pwd, group, role, sessioncredit);
  }

  public static boolean Logout() throws TCOperationException {
    ITCBasicFunction loginManager = (ITCBasicFunction) ServiceUtil.getService(ITCBasicFunction.class.getName(), InterfaceManager.class.getClassLoader());
    return loginManager.logout();
  }

  public static boolean creat(Map<String, String> attrs, File docPath, OperationManagerFactory factory) throws TCOperationException {
    /*
     * TCObject asmObj =
     * factory.getAssemblyOManager().createAssembly(attrs);
     * 
     * TCObject docObj = factory.getDocOManager().createDoc(attrs);
     * 
     * factory.getRelationOManager().createPartDocRelation(asmObj, docObj);
     */
    return true;
  }

}
