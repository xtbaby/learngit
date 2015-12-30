/**
 *
 */
package com.ufc.uif.qh3.acad.operation;

import java.util.List;
import java.util.Map;

import com.ufc.uif.tccommunicationimpl.operation.TCObjectOperation;
import com.ufc.uif.tcuacommunication.operation.ITCObjectOperation;
import com.ufc.uif.tcuacommunication.operation.exception.TCOperationException;

/**
 * @author Liugz
 * 
 */
public class SetUserSessionInfo {

  private ITCObjectOperation op;

  public SetUserSessionInfo() {
    op = new TCObjectOperation();
  }

  public Map<String, String> getSessionInfo() {
    try {
      return op.getUserSessionInfo();
    } catch (TCOperationException e) {
      e.printStackTrace();
    }
    return null;
  }

  public Map<String, List<String>> getAvilableGroupRoleInfo() {
    try {
      return op.getUserAvilableGroupRole();
    } catch (TCOperationException e) {
      e.printStackTrace();
    }
    return null;
  }

  public String[] getAssignableprojects() {
    try {
      return op.getAssignableprojects();
    } catch (TCOperationException e) {
      e.printStackTrace();
    }
    return null;
  }

  public boolean setSessionInfo(Map<String, String> info) {
    try {
      op.setSessionInfo(info);
    } catch (TCOperationException e) {
      e.printStackTrace();
    }
    return true;
  }
}
