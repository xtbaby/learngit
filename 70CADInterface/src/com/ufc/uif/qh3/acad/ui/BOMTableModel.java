package com.ufc.uif.qh3.acad.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

public class BOMTableModel extends AbstractTableModel {
  private static final long serialVersionUID = -3719958859261873955L;

  String[] tableHeader = new String[]{"序号", "ITEM_ID", "代号", "关重", "名称", "数量", "度量单位", "备注"};

  private List<Map<String, String>> props = new ArrayList<Map<String, String>>();

  public BOMTableModel(List<Map<String, String>> props){
    this.props = props;
  }

  public int getColumnCount() {
    return tableHeader.length;
  }

  public String getColumnName(int column){
    return tableHeader[column];
  }

  public int getRowCount() {
    return props.size();
  }


  public Object getValueAt(int rowIndex, int columnIndex) {
    String colValue = "";
    // 获取要显示的信息
    colValue = fillElementTable(rowIndex, columnIndex);
    return colValue;
  }

  /**
   * 填充元器件Table
   * @param rowIndex
   * @param columnIndex
   * @return for GofACADInterface
   * @author Liugz
   * @create on 2008-10-28
   * String
   */
  private String fillElementTable(int rowIndex, int columnIndex) {
    //"序号", "ITEM_ID", "代号", "关重", "名称", "数量", "度量单位", "备注"
    Map<String, String> property = props.get(rowIndex);
    String colValue = "";
    String colName;
    for (int i = 0; i < tableHeader.length; i++) {
      if ((columnIndex == i)/*&&(columnIndex!=6)*/) {
        colName = tableHeader[i];
        colValue = property.get(colName);
      }
      
    }
    return colValue;
  }

  public Class getColumnClass(int columnIndex) {
    return String.class;
  }

  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    if(getColumnName(columnIndex).equals("关重")|| getColumnName(columnIndex).equals("备注")|| getColumnName(columnIndex).equals("度量单位"))
      return true;
    else
      return false;
  }

  @Override
  public void setValueAt(Object value, int rowIndex, int columnIndex) {
    Map<String, String> property = props.get(rowIndex);
    if(getColumnName(columnIndex).equals("ItemID"))
      property.put("ITEM_ID", (String)value);
    else if(getColumnName(columnIndex).equals("代号"))
      property.put("代号", (String)value);
    else if(getColumnName(columnIndex).equals("关重"))
      property.put("关重", (String)value);
    else if(getColumnName(columnIndex).equals("名称"))
      property.put("名称", (String)value);
    else if(getColumnName(columnIndex).equals("数量"))
      property.put("数量", (String)value);
    else if(getColumnName(columnIndex).equals("备注"))
      property.put("备注", (String)value);
    else if(getColumnName(columnIndex).equals("度量单位"))
      property.put("度量单位", (String)value);
      
    fireTableCellUpdated(rowIndex, columnIndex);
  }

}
