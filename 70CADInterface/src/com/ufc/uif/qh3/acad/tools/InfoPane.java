package com.ufc.uif.qh3.acad.tools;

import java.awt.Color;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class InfoPane extends JTextArea {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InfoPane() {
		setOpaque(true);
	}

	public void showInfo(String paramString) {
		System.out.println("00000000000000000000000000--------");
		showInfo(paramString, null);
	}

	public void showInfo(String paramString, Color paramColor) {
		append(paramString + "\n");
		final JScrollPane localJScrollPane = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, this);
		if (localJScrollPane != null)
			System.out.println("00000000000000qqqqqqqqqqqqqqqq000000000000--------");
			try {
				System.out.println("000000000AAAAAAAAAA0000--------");
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						JScrollBar localJScrollBar = localJScrollPane.getVerticalScrollBar();
						localJScrollBar.setValue(localJScrollBar.getMaximum());
					}
				});
			} catch (Exception localException) {
				localException.printStackTrace();
			}
	}

	public void showInfo1(String paramString) {
		append(paramString + "\n");
		JScrollPane localJScrollPane = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, this);
		if (localJScrollPane == null)
			return;
		JScrollBar localJScrollBar = localJScrollPane.getVerticalScrollBar();
		localJScrollBar.setValue(localJScrollBar.getMaximum());
	}
}
