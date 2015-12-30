/**
 *
 */
package com.ufc.uif.qh3.acad.ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * @author Liugz
 * 
 */
public class WorkspaceTableRender extends DefaultTableCellRenderer {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  Color bgColor = new Color(241, 250, 255);
  Color bgColor1 = new Color(241, 200, 245);
  Color bgColor2 = new Color(191, 214, 248);
  Color selectedColor = new Color(49, 106, 197);
  Color white = new Color(255, 255, 255);
  Color black = new Color(0, 0, 0);

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    // TODO Auto-generated method stub
    Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

    setForeground(black);
    if (!isSelected) {
      int pos = 0;
      int colNum = table.getColumnCount();

      if (colNum != 0) {
        for (int i = 0; i < colNum; i++) {
          if ("器材状态".equals(table.getColumnName(i))) {
            pos = i;
            break;
          }
        }
      }
      String valueAT = (String) table.getModel().getValueAt(row, pos);
      if (null != valueAT && !"".equals(valueAT)) {

        if (valueAT.equals("优选")) {
          cell.setBackground(Color.GREEN);
        } else if (valueAT.equals("推荐")) {
          cell.setBackground(bgColor2);
        } else if (valueAT.equals("可用")) {
          cell.setBackground(bgColor);
        } else if (valueAT.equals("限用")) {
          cell.setBackground(bgColor1);
        } else if (valueAT.equals("试用")) {
          cell.setBackground(Color.lightGray);
        } else if (valueAT.equals("禁用")) {
          cell.setBackground(Color.RED);
        } else {
          if (row % 2 == 0)
            cell.setBackground(bgColor);
          else
            cell.setBackground(bgColor2);
        }

      } else {
        if (row % 2 == 0)
          cell.setBackground(bgColor);
        else
          cell.setBackground(bgColor2);
      }
    } else {
      cell.setForeground(white);
      cell.setBackground(selectedColor);
    }

    return cell;
  }
}
