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
  private String[] columnNames = new String[]{"对象名称", "对象类型", "所有者", "修改日期"};
  private String[] itemColNames = null;
  private WSObject[] wsObjs;
  private WSObject[] forms;

  public WorkspaceTableModel(WSObject[] objs){
    this.wsObjs = objs;
  }

  public WorkspaceTableModel(WSObject[] objs, Map<String, String> mapping, String hint){
    this.wsObjs = objs;
    List<String> header = new ArrayList<String>();
    header.add("对象 ID");
    header.add("对象名称");
    //header.add("对象类型");
    header.addAll(mapping.keySet());
    this.itemColNames = header.toArray(new String[0]);
    this.hint = hint;
  }
  
  public WorkspaceTableModel(WSObject[] foundObjs, WSObject[] forms,
      Map<String, String> mapping, String hint) {
    
    this.wsObjs = foundObjs;
    this.forms=forms;
    List<String> header = new ArrayList<String>();
    header.add("对象 ID");
    header.add("对象名称");
    header.add("对象类型");
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
   * 如果是"Item"节点，显示ItemRevisioin上的属性信息
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
      if(colName.contains("是否")&&value.equals("Y")){
        value="是";
      }else if(colName.contains("是否")&&value.equals("")){
        value="否";
      }
    }

  

    return value;
  }

  /**
   * 如果是"Folder"类型节点，则显示该文件夹下所有对象的信息
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
