package com.ufc.uif.qh3.acad.ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class TableRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

		if ("1".equals(table.getValueAt(row, 8))) {
			setBackground(Color.red);
		} else if ("0".equals(table.getValueAt(row, 8))) {
			setBackground(Color.GREEN);
		} else {
			setBackground(Color.WHITE);
		}
		/*
		 * if(column==1){ setBackground(Color.ORANGE); }
		 */
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}

}
