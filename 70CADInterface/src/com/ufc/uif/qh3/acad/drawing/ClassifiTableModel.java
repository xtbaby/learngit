package com.ufc.uif.qh3.acad.drawing;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import com.ufc.uif.tccommunicationimpl.object.ClassifiAttribute;
import com.ufc.uif.tccommunicationimpl.object.ClassifiObject;

//import com.ufc.uif.tcuacommunication.objects.ClassifiAttribute;
//import com.ufc.uif.tcuacommunication.objects.ClassifiObject;

//import com.ufc.uif.tccommunication.tc2007.objects.ClassifiAttribute;
//import com.ufc.uif.tccommunication.tc2007.objects.ClassifiObject;

/**
 * @author Liugz
 *
 */
public class ClassifiTableModel extends AbstractTableModel {

  private String[] columnNames;
  private List<ClassifiObject> clfObjs;

  /**
   * @return the clfObjs
   */
  public List<ClassifiObject> getClfObjs() {
    return clfObjs;
  }

  /**
   * @param clfObjs the clfObjs to set
   */
  public void setClfObjs(List<ClassifiObject> clfObjs) {
    this.clfObjs = clfObjs;
  }

  public ClassifiTableModel(ClassifiAttribute[] attributes){
    this.columnNames = new String[attributes.length + 2];
    columnNames[0] = "对象 ID";
    columnNames[1] = "对象名称";
    for(int k = 0; k < attributes.length; k++){
      ClassifiAttribute attrib = attributes[k];
      columnNames[k + 2] = attrib.getName();
    }
  }

  public ClassifiTableModel(){

  }

  @Override
  public String getColumnName(int column) {

    return columnNames[column];
  }



  public int getColumnCount() {

    return columnNames.length;
  }

  public int getRowCount() {
    if(null == clfObjs)
      return 0;
    else
      return clfObjs.size();
  }

  public Object getValueAt(int rowIndex, int columnIndex) {
    if(null == clfObjs)
      return null;
    else {
      ClassifiObject clfObj = clfObjs.get(rowIndex);
      Map<String, String> attribs = clfObj.getAttributes();
      String key = columnNames[columnIndex];
      return attribs.get(key);
    }
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return String.class;
  }



}
