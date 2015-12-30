package com.ufc.uif.qh3.acad.ui.wizard;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;

import org.netbeans.api.wizard.WizardDisplayer;
import org.netbeans.api.wizard.displayer.NavButtonManager;
import org.netbeans.api.wizard.displayer.WizardDisplayerImpl;
import org.netbeans.spi.wizard.WizardPage;
import com.ufc.uif.qh3.acad.ui.SaveDrawingDialog;

public class CheckInDrawingPanel extends WizardPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton next;
	private JPanel infoPanel;
	private JScrollPane jScrollPane1;
	private JSeparator jSeparator1;
	public JProgressBar progressBar;
	private JLabel progressL;
	private JTextArea textArea;
	private Thread t;

	public CheckInDrawingPanel(SaveDrawingDialog Dialog) {

		initComponents();

		WizardDisplayerImpl displayer = (WizardDisplayerImpl) WizardDisplayer.defaultInstance;
		NavButtonManager mgr = displayer.getButtonManager();
		next = mgr.getNext();

		mgr.setNextBtnEnabled(false);

		new Thread(new Runnable() {

			public void run() {
				try {
					Thread.sleep(50);
					CheckInDrawing();
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}

		}).start();

		this.setBackground(new Color(226, 245, 252));
	}

	private void initComponents() {
		// 初始化变量
		progressBar = new javax.swing.JProgressBar();
		jSeparator1 = new javax.swing.JSeparator();
		infoPanel = new javax.swing.JPanel();
		jScrollPane1 = new javax.swing.JScrollPane();
		textArea = new JTextArea();
		progressL = new javax.swing.JLabel();
		infoPanel.setBackground(new Color(226, 245, 252));
		// end

		textArea.setColumns(20);
		textArea.setEditable(false);
		textArea.setRows(5);
		jScrollPane1.setViewportView(textArea);

		org.jdesktop.layout.GroupLayout infoPanelLayout = new org.jdesktop.layout.GroupLayout(infoPanel);
		infoPanel.setLayout(infoPanelLayout);
		infoPanelLayout.setHorizontalGroup(infoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane1,
				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 437, Short.MAX_VALUE));
		infoPanelLayout.setVerticalGroup(infoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 277,
				Short.MAX_VALUE));

		progressL.setText("进度");
		org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(layout.createSequentialGroup().addContainerGap().add(infoPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addContainerGap())
				.add(jSeparator1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 457, Short.MAX_VALUE)
				.add(layout.createSequentialGroup().addContainerGap().add(progressL).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
						.add(progressBar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE).addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
				layout.createSequentialGroup()
						.add(17, 17, 17)
						.add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(progressL)
								.add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
						.add(18, 18, 18).add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(18, 18, 18)
						.add(infoPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

	}

	public void outputStatus(String str) {
		textArea.append(str + "\n");

		textArea.setCaretPosition(textArea.getText().length());
	}

	private void stopProgress() {
		// progressBar.setIndeterminate(false);
		t.stop();
		progressBar.setValue(100);
		// processing = false;
	}

	private void startProgress() {
		// processing = true;
		// progressBar.setIndeterminate(true);

		t = new Thread(new Runnable() {
			public void run() {
				int tempI = 0;

				while (true) {
					progressBar.setValue(tempI);
					tempI++;
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					if (tempI >= 100) {
						tempI = 0;
					}
				}
			}
		});
		t.start();
	}

	public void CheckInDrawing() {
		progressBar.setBackground(new java.awt.Color(241, 250, 255));
		progressBar.setForeground(Color.GREEN);
		startProgress();
		outputStatus("正在检入图纸对象,请稍等.....");
		for (int i = 0; i < 999999999; i++) {

		}
		stopProgress();
		outputStatus("图纸对象检入完毕!");
		JOptionPane.showMessageDialog(this, "检入图纸操作成功");

		WizardDisplayerImpl displayer = (WizardDisplayerImpl) WizardDisplayer.defaultInstance;
		NavButtonManager mgr = displayer.getButtonManager();
		JButton cancel = mgr.getCancel();
		cancel.setText("完成");

		JButton next = mgr.getNext();
		next.setEnabled(false);
	}

}
