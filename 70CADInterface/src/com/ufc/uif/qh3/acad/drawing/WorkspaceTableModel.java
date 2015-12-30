package com.ufc.uif.qh3.acad.drawing;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import com.ufc.uif.tcuacommunication.objects.WSObject;

//import com.ufc.uif.tccommunication.tc2008.objects.WSObject;

/**
 *
 */

/**
 * @author Liugz
 *
 */
public class WorkspaceTableModel extends AbstractTableModel {

  private String hint = "";
  private String[] columnNames = new String[]{"��������", "��������", "������", "�޸�����"};
  private String[] itemColNames = null;
  private WSObject[] wsObjs;
  private WSObject[] forms;

  public WorkspaceTableModel(WSObject[] objs){
    this.wsObjs = objs;
  }

  public WorkspaceTableModel(WSObject[] objs, Map<String, String> mapping, String hint){
    this.wsObjs = objs;
    List<String> header = new ArrayList<String>();
    header.add("���� ID");
    header.add("��������");
    //header.add("��������");
    header.addAll(mapping.keySet());
    this.itemColNames = header.toArray(new String[0]);
    this.hint = hint;
  }
  
  public WorkspaceTableModel(WSObject[] foundObjs, WSObject[] forms,
      Map<String, String> mapping, String hint) {
    
    this.wsObjs = foundObjs;
    this.forms=forms;
    List<String> header = new ArrayList<String>();
    header.add("���� ID");
    header.add("��������");
    header.add("��������");
    header.addAll(mapping.keySet());
    this.itemColNames = header.toArray(new String[0]);
    this.hint = hint;
  }
  public String getObjectId(int rowIndex){
    if(null == wsObjs)
      return "";

    WSObject wsObj = wsObjs[rowIndex];
    return  wsObj.getId();
  
  }
  @Override
  public String getColumnName(int column) {
    if(!"Item".equals(hint))
      return columnNames[column];
    else
      return itemColNames[column];
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return String.class;
  }

  public int getColumnCount() {
    if(!"Item".equals(hint))
      return columnNames.length;
    else
      return itemColNames.length;
  }

  public int getRowCount() {
    if(null == wsObjs){
      return 0;
    }
    return wsObjs.length;
  }

  public Object getValueAt(int rowIndex, int columnIndex) {
    if(!"Item".equals(hint))
      return summaryOfFolder(rowIndex, columnIndex);
    else
      return summaryOfItem(rowIndex, columnIndex);
  }

  /**
   * �����"Item"�ڵ㣬��ʾItemRevisioin�ϵ�������Ϣ
   * @author Liugz
   * @create on 2009-3-11
   * This project for tc_communication.teamcenter2007
   * @param rowIndex
   * @param columnIndex
   * @return
   */
  private Object summaryOfItem(int rowIndex, int columnIndex) {
    if(null == wsObjs)
      return "";
    if(null==forms)
      return "";

    String value = "";
    WSObject wsObj = wsObjs[rowIndex];
    WSObject form = forms[rowIndex];
    Map<String, String> attribs = form.getAttributes();
    String colName = itemColNames[columnIndex];
  
    if(columnIndex == 0){
      value = wsObj.getId();
      //value = wsObj.getDisplayName();
    }
    else if(columnIndex == 1){
//      value = wsObj.getDisplayName();
//      if(null == value || "".equals(value))
      //modify
        value = wsObj.getDisplayName();//.getName();
    }else if(columnIndex == 2){
      value = wsObj.getType();
    }
    else {
      value =attribs.get(colName);
      if(colName.contains("�Ƿ�")&&value.equals("Y")){
        value="��";
      }else if(colName.contains("�Ƿ�")&&value.equals("")){
        value="��";
      }
    }

  

    return value;
  }

  /**
   * �����"Folder"���ͽڵ㣬����ʾ���ļ��������ж������Ϣ
   * @author Liugz
   * @create on 2009-3-11
   * This project for tc_communication.teamcenter2007
   * @param rowIndex
   * @param columnIndex
   * @return
   */
  private Object summaryOfFolder(int rowIndex, int columnIndex) {
    if(null == wsObjs)
      return "";

    String value = "";
    WSObject wsObj = wsObjs[rowIndex];
    switch (columnIndex) {
    case 0:
    {
      value = wsObj.getDisplayName();
      if(null == value || "".equals(value))
        value = wsObj.getName();
    }
      break;
    case 1:
      value = wsObj.getType();
      break;
    case 2:
      value = wsObj.getOwnUser();
      break;
    case 3:
      value = wsObj.getModDate();
      
      break;
    default:
      break;
    }
    return value;
  }

}
