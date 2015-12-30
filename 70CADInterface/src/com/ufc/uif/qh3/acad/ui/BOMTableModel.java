package com.ufc.uif.qh3.acad.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

public class BOMTableModel extends AbstractTableModel {
  private static final long serialVersionUID = -3719958859261873955L;

  String[] tableHeader = new String[]{"���", "ITEM_ID", "����", "����", "����", "����", "������λ", "��ע"};

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
    // ��ȡҪ��ʾ����Ϣ
    colValue = fillElementTable(rowIndex, columnIndex);
    return colValue;
  }

  /**
   * ���Ԫ����Table
   * @param rowIndex
   * @param columnIndex
   * @return for GofACADInterface
   * @author Liugz
   * @create on 2008-10-28
   * String
   */
  private String fillElementTable(int rowIndex, int columnIndex) {
    //"���", "ITEM_ID", "����", "����", "����", "����", "������λ", "��ע"
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
    if(getColumnName(columnIndex).equals("����")|| getColumnName(columnIndex).equals("��ע")|| getColumnName(columnIndex).equals("������λ"))
      return true;
    else
      return false;
  }

  @Override
  public void setValueAt(Object value, int rowIndex, int columnIndex) {
    Map<String, String> property = props.get(rowIndex);
    if(getColumnName(columnIndex).equals("ItemID"))
      property.put("ITEM_ID", (String)value);
    else if(getColumnName(columnIndex).equals("����"))
      property.put("����", (String)value);
    else if(getColumnName(columnIndex).equals("����"))
      property.put("����", (String)value);
    else if(getColumnName(columnIndex).equals("����"))
      property.put("����", (String)value);
    else if(getColumnName(columnIndex).equals("����"))
      property.put("����", (String)value);
    else if(getColumnName(columnIndex).equals("��ע"))
      property.put("��ע", (String)value);
    else if(getColumnName(columnIndex).equals("������λ"))
      property.put("������λ", (String)value);
      
    fireTableCellUpdated(rowIndex, columnIndex);
  }

}
