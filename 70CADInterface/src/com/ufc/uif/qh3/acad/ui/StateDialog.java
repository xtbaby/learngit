package com.ufc.uif.qh3.acad.ui;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;

import com.ufc.uif.qh3.acad.images.IconManager;

/**
 * <p>
 * Title: StateDialog
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author Zhongning.B
 * @version $Id: StateDialog.java,v 1.1 2010/11/11 03:43:08 wangjf Exp $
 * @Created On Aug 29, 2008
 */
public class StateDialog extends javax.swing.JDialog {

	private static final long serialVersionUID = 1L;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JProgressBar jProgressBar1;
	private String message;

	public StateDialog(String message) {
		super();
		this.message = message;
		initComponents();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		setVisible(true);

		jPanel1.updateUI();
	}

	public StateDialog(Frame frame, boolean modal, String message) {
		super(frame, modal);
		this.message = message;
		initComponents();
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		setLocation((screenSize.width - frameSize.width) / 4, (screenSize.height - frameSize.height) / 2);
		
		this.setLocationRelativeTo(null);;
		
		setVisible(true);

		jPanel1.updateUI();
	}

	public StateDialog(Dialog dialog, boolean modal, String message) {
		super(dialog, modal);
		this.message = message;
		initComponents();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
		setVisible(true);

		jPanel1.updateUI();
	}

	private void initComponents() {

		jPanel1 = new javax.swing.JPanel();
		jPanel2 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		jProgressBar1 = new javax.swing.JProgressBar();

		jPanel1.setBackground(new java.awt.Color(226, 245, 252));

		jPanel2.setBackground(new java.awt.Color(241, 250, 255));
		jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(144, 185, 215)));

		jLabel1.setForeground(new java.awt.Color(13, 55, 85));
		jLabel1.setText(message);

		org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
				jPanel2Layout
						.createSequentialGroup()
						.addContainerGap()
						.add(jPanel2Layout
								.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
								.add(jPanel2Layout.createSequentialGroup().add(jLabel2).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
										.add(jProgressBar1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jLabel3))
								.add(jLabel1)).addContainerGap()));
		jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
				jPanel2Layout
						.createSequentialGroup()
						.add(19, 19, 19)
						.add(jLabel1)
						.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
						.add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
								.add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel3).add(jLabel2))
								.add(jProgressBar1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 11, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
						.addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
				jPanel1Layout.createSequentialGroup().addContainerGap()
						.add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
				jPanel1Layout.createSequentialGroup().addContainerGap()
						.add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(20, Short.MAX_VALUE)));

		org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE));
		layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE));

		jLabel2.setIcon(IconManager.getIcon("user.png")); // NOI18N

		jLabel3.setIcon(IconManager.getIcon("server.png")); // NOI18N

		setAlwaysOnTop(true);
		setUndecorated(true);
		setResizable(false);
		startProgress();
		System.out.println("after start progress...");
		pack();
	}

	private void startProgress() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				int tempI = 0;

				while (true) {
					jProgressBar1.setValue(tempI);
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

	public void setMessage(String message) {
		jLabel1.setText(message);
	}
}
