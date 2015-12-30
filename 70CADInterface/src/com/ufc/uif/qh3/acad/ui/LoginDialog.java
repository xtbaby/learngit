package com.ufc.uif.qh3.acad.ui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import javax.swing.JOptionPane;
import com.ufc.uif.qh3.acad.operation.InterfaceManager;
import com.ufc.uif.tcuacommunication.operation.OperationManagerFactory;
import com.ufc.uif.util.SaveUserUtil;

public class LoginDialog extends _UIFDialog {

	/**
   * 
   */
	private static final long serialVersionUID = 1L;
	private javax.swing.JPanel HomePanel;
	private javax.swing.JLabel LogoLabel;
	private javax.swing.JPasswordField PassWord;
	private javax.swing.JPanel UserInfoPanel;
	private javax.swing.JComboBox<String> UserName;
	private javax.swing.JLabel UserNameLabel;
	private javax.swing.JLabel UserPwdLabel;
	private javax.swing.JButton okButton;
	private javax.swing.JButton closeButton;
	private javax.swing.JCheckBox jCheckBox1;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private StateDialog dialog;
	private Object usrNameObj;
	private String usrPwd;
	private SaveUserUtil saveUser;

	public LoginDialog(OperationManagerFactory factory) {
		super(factory);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (null != dialog) {
					dialog.setVisible(false);
					dialog.dispose();
				}
				try {
					out.setFuncID("Login");
					out.setResult("false");
					out.setDesc("用户取消登录。");
					out.sendResultToUI();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				dispose();
			}
		});

		// 获取之前保存的用户信息
		saveUser = new SaveUserUtil();
		Map<String, String> userMap = saveUser.readSavedUsers();
		Set<String> nameSet = userMap.keySet();
		String[] names = nameSet.toArray(new String[0]);
		for (String name : names) {
			UserName.addItem(name);
		}
		UserName.setSelectedItem(saveUser.getLastUser());
		PassWord.requestFocus(true);
	}

	@Override
	public void initComponents() {
		HomePanel = new javax.swing.JPanel();
		LogoLabel = new javax.swing.JLabel();
		UserInfoPanel = new javax.swing.JPanel();
		UserNameLabel = new javax.swing.JLabel();
		UserPwdLabel = new javax.swing.JLabel();
		UserName = new javax.swing.JComboBox<String>();
		PassWord = new javax.swing.JPasswordField();
		jCheckBox1 = new javax.swing.JCheckBox();
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		okButton = new javax.swing.JButton();
		closeButton = new javax.swing.JButton();

		HomePanel.setBackground(new java.awt.Color(226, 245, 252));
		HomePanel.setForeground(new java.awt.Color(226, 245, 252));

		UserInfoPanel.setBackground(new java.awt.Color(241, 250, 255));
		UserInfoPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(144, 185, 215)));

		UserNameLabel.setForeground(new java.awt.Color(13, 55, 85));
		UserNameLabel.setText("用户账号：");

		UserPwdLabel.setForeground(new java.awt.Color(13, 55, 85));
		UserPwdLabel.setText("用户密码：");

		UserName.setEditable(true);

		jCheckBox1.setBackground(new java.awt.Color(241, 250, 255));
		jCheckBox1.setForeground(new java.awt.Color(13, 55, 85));
		jCheckBox1.setText("记录用户");
		jCheckBox1.setSelected(true);

		jLabel1.setForeground(new java.awt.Color(0, 0, 204));
		jLabel1.setText("删除记录");

		jLabel2.setForeground(new java.awt.Color(255, 0, 0));
		jLabel2.setText("*");

		jLabel3.setForeground(new java.awt.Color(255, 0, 0));
		jLabel3.setText("*");

		org.jdesktop.layout.GroupLayout UserInfoPanelLayout = new org.jdesktop.layout.GroupLayout(UserInfoPanel);
		UserInfoPanel.setLayout(UserInfoPanelLayout);
		UserInfoPanelLayout.setHorizontalGroup(UserInfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
				UserInfoPanelLayout
						.createSequentialGroup()
						.add(22, 22, 22)
						.add(UserInfoPanelLayout
								.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
								.add(UserInfoPanelLayout.createSequentialGroup().add(UserNameLabel).add(18, 18, 18)
										.add(UserName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 176, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap())
								.add(UserInfoPanelLayout.createSequentialGroup().add(
										UserInfoPanelLayout
												.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
												.add(UserInfoPanelLayout.createSequentialGroup().add(UserPwdLabel).add(18, 18, 18)
														.add(PassWord, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 176, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
												.add(UserInfoPanelLayout.createSequentialGroup().add(
														UserInfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
																UserInfoPanelLayout
																		.createSequentialGroup()
																		.add(UserInfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)

																		.add(UserInfoPanelLayout.createSequentialGroup().add(jCheckBox1)
																				.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 61, Short.MAX_VALUE).add(jLabel1))).add(26, 26, 26)))))))));
		UserInfoPanelLayout.setVerticalGroup(UserInfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
				UserInfoPanelLayout
						.createSequentialGroup()
						.add(19, 19, 19)
						.add(UserInfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(UserNameLabel)
								.add(UserName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
						.add(18, 18, 18)
						.add(UserInfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(UserPwdLabel)
								.add(PassWord, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(18, 18, 18)
						.add(UserInfoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jCheckBox1).add(jLabel1))
						.addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		org.jdesktop.layout.GroupLayout HomePanelLayout = new org.jdesktop.layout.GroupLayout(HomePanel);
		HomePanel.setLayout(HomePanelLayout);
		HomePanelLayout.setHorizontalGroup(HomePanelLayout
				.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(LogoLabel)
				.add(HomePanelLayout.createSequentialGroup().addContainerGap()
						.add(UserInfoPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap())
				.add(org.jdesktop.layout.GroupLayout.TRAILING,
						HomePanelLayout.createSequentialGroup().addContainerGap().add(okButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 121, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 63, Short.MAX_VALUE)
								.add(closeButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap()));
		HomePanelLayout.setVerticalGroup(HomePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
				HomePanelLayout
						.createSequentialGroup()
						.add(LogoLabel)
						.addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
						.add(UserInfoPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
						.add(18, 18, 18)
						.add(HomePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
								.add(closeButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
								.add(okButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
						.addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(HomePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE));
		layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(HomePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
				org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE));

		// LogoLabel.setIcon(new javax.swing.ImageIcon(getIcon("login.jpg")));

		okButton.setIcon(new javax.swing.ImageIcon(getIcon("button_1.jpg")));

		closeButton.setIcon(new javax.swing.ImageIcon(getIcon("button_2.jpg")));

		this.addWindowListener(new WindowListener() {

			public void windowOpened(WindowEvent e) {
			}

			public void windowClosing(WindowEvent e) {
				// closeWindow();
			}

			public void windowClosed(WindowEvent e) {
			}

			public void windowIconified(WindowEvent e) {
			}

			public void windowDeiconified(WindowEvent e) {
			}

			public void windowActivated(WindowEvent e) {
			}

			public void windowDeactivated(WindowEvent e) {
			}
		});

		this.setResizable(false);
		this.setAlwaysOnTop(true);
		this.setTitle("AutoCad 接口登录");
		this.requestFocus(true);
		setIconImage(getIcon("ico.png"));

		pack();
	}

	@Override
	public void initConfig() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initListener() {
		UserName.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(java.awt.event.KeyEvent evt) {
				if (evt.getKeyChar() == '\n') {
					PassWord.setFocusable(true);
				}
			}
		});

		PassWord.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(java.awt.event.KeyEvent evt) {
				if (evt.getKeyChar() == '\n') {
					apply();
				}
			}
		});

		jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				saveUser.deleteAllUsers();
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
			}
		});

		jLabel1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
			public void mouseMoved(java.awt.event.MouseEvent evt) {
				setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
			}
		});

		okButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseExited(java.awt.event.MouseEvent evt) {
				okButton.setIcon(new javax.swing.ImageIcon(getIcon("button_1.jpg")));
			}

			public void mousePressed(java.awt.event.MouseEvent evt) {
				okButton.setIcon(new javax.swing.ImageIcon(getIcon("button_1.jpg")));
			}
		});

		okButton.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
			public void mouseMoved(java.awt.event.MouseEvent evt) {
				okButton.setIcon(new javax.swing.ImageIcon(getIcon("button_1.jpg")));
			}
		});

		okButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				apply();
			}
		});

		closeButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseExited(java.awt.event.MouseEvent evt) {
				closeButton.setIcon(new javax.swing.ImageIcon(getIcon("button_2.jpg")));
			}

			public void mousePressed(java.awt.event.MouseEvent evt) {
				closeButton.setIcon(new javax.swing.ImageIcon(getIcon("button_2.jpg")));
			}
		});

		closeButton.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
			public void mouseMoved(java.awt.event.MouseEvent evt) {
				closeButton.setIcon(new javax.swing.ImageIcon(getIcon("button_2.jpg")));
			}
		});

		closeButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				System.out.println("colseWindow.............");
				closeWindow();
			}
		});
	}

	public void closeWindow() {
		if (null != dialog) {
			dialog.setVisible(false);
			dialog.dispose();
		}
		dispose();
		try {
			out.setFuncID("Login");
			out.setResult("false");
			out.setDesc("登录Teamcenter失败");
			out.sendResultToUI();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void apply() {
		char[] usrPwdchr = PassWord.getPassword();
		if (UserName.getSelectedItem() == null || UserName.getSelectedItem().toString().trim().equals("")) {
			setAlwaysOnTop(false);

			JOptionPane.showMessageDialog(null, "信息输入不完整！", "错误", JOptionPane.ERROR_MESSAGE);

			setAlwaysOnTop(true);

			return;
		}

		usrNameObj = UserName.getSelectedItem();
		usrPwd = new String(usrPwdchr);

		dialog = new StateDialog("正在通过提供的信息登录PDM......");
		this.setVisible(false);
		// dialog.setLocationRelativeTo(null);
		Thread loginThr = new Thread(new Runnable() {
			public void run() {
				try {
					boolean flag = false;

					flag = InterfaceManager.Login(usrNameObj.toString(), usrPwd, "", "", "");

					if (flag) {
						// 保存用户名和密码
						if (jCheckBox1.isSelected()) {
							saveUser.saveUser(usrNameObj.toString(), usrPwd);
						}
						// System.setProperty("IS_LOGIN_TC", "true");
						try {
							out.setFuncID("Login");
							out.setResult("true");
							out.setDesc("Login Successful.");
							out.sendResultToUI();
						} catch (IOException ex) {
							ex.printStackTrace();
						}
						// Util.setOperator(usrNameObj.toString());

						dialog.setVisible(false);
						dialog.dispose();
						dispose();
						JOptionPane.showMessageDialog(null, "登录成功", "信息", 1);
						setAlwaysOnTop(true);
					} else {
						setVisible(true);
						dialog.dispose();
						setAlwaysOnTop(false);
						JOptionPane.showMessageDialog(null, "登录失败", "错误", 0);
						setAlwaysOnTop(true);
					}
				} catch (Exception e) {
					dialog.dispose();
					setAlwaysOnTop(false);

					JOptionPane.showMessageDialog(null, e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
					setAlwaysOnTop(true);
				}
			}
		});

		loginThr.start();
	}
	
}
