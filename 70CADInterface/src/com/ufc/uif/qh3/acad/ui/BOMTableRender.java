package com.ufc.uif.qh3.acad.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


public class BOMTableRender extends DefaultTableCellRenderer {
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  private List<String> noItemIds;
  private List<String> nonDSIds;
  private List<String> updatedIds;
  private Map<String, List<String>> wrongItemIds;
  Color normalColor = new Color(241, 250, 255);
  Color nonItemColor = new Color(/*184, 170, 138*/189, 183, 107);
  Color nonDSColor = new Color(/*196, 219, 143*/144, 238, 144);
  Color wrongColor = new Color(/*236, 177, 155*/255, 190, 49);
  Color selectedColor = new Color(49,106,197);
  Color white = new Color(255, 255, 255);
  Color black = new Color(0, 0, 0);
  Color updatedColor = new Color(/*255, 248, 158*/204, 223, 229);
  Font boldFont = null;
  Font italicFont = null;
  Font plainFont = null;

  public BOMTableRender(List<String> ids) {
    this.noItemIds = ids;
  }

  public BOMTableRender(List<String> ids, List<String> datasetIds,
      Map<String, List<String>> wrongItemIds) {
    this.noItemIds = ids;
    this.nonDSIds = datasetIds;
    this.wrongItemIds = wrongItemIds;
  }

  public BOMTableRender(List<String> ids, List<String> datasetIds, List<String> updatedIds,
      Map<String, List<String>> wrongItemIds) {
    this.noItemIds = ids;
    this.nonDSIds = datasetIds;
    this.wrongItemIds = wrongItemIds;
    this.updatedIds = updatedIds;
  }

  public Component getTableCellRendererComponent(JTable table, Object value,
      boolean isSelected, boolean hasFocus, int row, int column) {
    Component cmpt = null;
    cmpt = super.getTableCellRendererComponent(table, value, isSelected,
        hasFocus, row, column);

    if (isSelected){
      setForeground(white);
      cmpt.setBackground(selectedColor);
    }
    else {
      setForeground(black);
      // 将单元格背景设为黄色
      String id = "";
      if ((String) table.getValueAt(row, 1)!=null && !((String) table.getValueAt(row, 1)).equals("")){
        id = (String) table.getValueAt(row, 1);
      }else{
        id = (String) table.getValueAt(row, 2);
      }
       
      // 更新属性后
      if(null != updatedIds && updatedIds.contains(id)){
        cmpt.setBackground(updatedColor);
      }
      else if (noItemIds.contains(id)) {
        /*if (Util.isPurchProd(id)) {// 如果是外购件，则加粗显示
          if (null == boldFont)
            boldFont = getFont().deriveFont(Font.BOLD);
          cmpt.setFont(boldFont);
        } else if (Util.isGeneralPart(id)) {
          if (null == italicFont)
            italicFont = getFont().deriveFont(Font.ITALIC);
          cmpt.setFont(italicFont);
        } else
          cmpt.setFont(plainFont);*/

        cmpt.setBackground(nonItemColor);
      } else if (wrongItemIds.containsKey(id)) {
        String colName = table.getColumnName(column);
        List<String> attribsName = wrongItemIds.get(id);
        if (attribsName.contains(colName)) {
          cmpt.setBackground(wrongColor);
        }
        /*
         * else if(column == 1){ cmpt.setBackground(wrongColor); }
         */
        else
          cmpt.setBackground(normalColor);
      } else if (nonDSIds.contains(id)) {
        cmpt.setBackground(nonDSColor);
      }
      else {
        cmpt.setBackground(normalColor);
      }
    }
    return cmpt;
  }

}
