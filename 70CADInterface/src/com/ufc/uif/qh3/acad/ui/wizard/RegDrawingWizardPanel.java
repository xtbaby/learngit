package com.ufc.uif.qh3.acad.ui.wizard;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import java.util.Map;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;

import org.dom4j.Element;
import org.netbeans.api.wizard.WizardDisplayer;
import org.netbeans.api.wizard.displayer.NavButtonManager;
import org.netbeans.api.wizard.displayer.WizardDisplayerImpl;
import org.netbeans.spi.wizard.Wizard;
import org.netbeans.spi.wizard.WizardPage;
import org.netbeans.spi.wizard.WizardPanelNavResult;

import com.ufc.uif.base_adaptor.AdaptorWriter;
import com.ufc.uif.qh3.acad.operation.SaveDrawingOperation;
import com.ufc.uif.qh3.acad.tools.CommonProperty;
import com.ufc.uif.qh3.acad.tools.Dom4jUtil;
import com.ufc.uif.qh3.acad.tools.Util;
import com.ufc.uif.qh3.acad.tools.WizardButtonOperation;
import com.ufc.uif.qh3.acad.ui.SaveDrawingDialog;
import com.ufc.uif.tccommunicationimpl.operation.TCFileOperation;
import com.ufc.uif.tccommunicationimpl.operation.connecttc.TCUtil;
import com.ufc.uif.tcuacommunication.objects.WSObject;
import com.ufc.uif.tcuacommunication.operation.ITCTCObjOperation;
import com.ufc.uif.tcuacommunication.operation.exception.TCOperationException;
import com.ufc.uif.util.service.ServiceUtil;

public class RegDrawingWizardPanel extends WizardPage {

	private Element requestBody;

	private JPanel infoPanel;
	private JScrollPane jScrollPane1;
	private JSeparator jSeparator1;
	public JProgressBar progressBar;
	private JLabel progressL;
	private JTextArea textArea;
	private SaveDrawingDialog dialog;
	private SaveDrawingOperation sdo;

	// 标识注册图纸是否成功
	public boolean succ;
	private Map params;
	private boolean processing;
	// 注：定义这些全局变量是为了处理连号图图纸的需要

	String orignalfilePath;

	private Element objEle;
	private Dom4jUtil util;
	private String drawingName;
	private ITCTCObjOperation tcop;
	private WSObject docObj, dsObj;
	public static WSObject itemObj;
	private JButton next;
	NavButtonManager mgr;

	private Thread t;

	private static final long serialVersionUID = 1L;

	public RegDrawingWizardPanel(SaveDrawingDialog dialog) {

		this.requestBody = dialog.requestBody;

		sdo = new SaveDrawingOperation();
		util = Dom4jUtil.getDom4jUtil();
		objEle = requestBody.element("body").element("object");

		tcop = (ITCTCObjOperation) ServiceUtil.getService(ITCTCObjOperation.class.getName(), SaveDrawingDialog.class.getClassLoader());

		initComponents();

		WizardDisplayerImpl displayer = (WizardDisplayerImpl) WizardDisplayer.defaultInstance;
		mgr = displayer.getButtonManager();
		next = mgr.getNext();
		mgr.setNextBtnEnabled(false);

		new Thread(new Runnable() {

			public void run() {
				try {
					Thread.sleep(50);
					registerDrawing(new HashMap());
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}

		}).start();

		// 设置窗口的背景颜色
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

	public void registerDrawing(Map settings) {

		// 改变按钮状态
		WizardButtonOperation.enableCancelBtn(false);
		WizardButtonOperation.enableNextBtn(false);
		WizardButtonOperation.enablePrevBtn(false);
		WizardButtonOperation.updateBtn();
		// 启动进度条
		progressBar.setBackground(new java.awt.Color(241, 250, 255));
		progressBar.setForeground(Color.GREEN);
		startProgress();

		File file = null;

		// 获取dwg文件的全路径
		String filePath = Dom4jUtil.getDom4jUtil().getProValue(objEle, CommonProperty.FILEOBJECT_LOCALPATH);
		// attrValues.put(CommonProperty.FILEOBJECT_LOCALPATH,
		// filePath);

		// 如果图纸存在，则退出注册图纸操作
		file = new File(filePath);
		if (!file.exists()) {
			stopProgress();
			JOptionPane.showMessageDialog(this, "文件" + filePath + " 不存在，不进行注册图纸操作");
			outputStatus("文件" + filePath + " 不存在，不进行注册图纸操作");
			return;
		}
		// 将原始图路径保存起来，注：这样做是为了统一对连号图和一般图图纸的处理
		orignalfilePath = filePath;

		// 1\创建工程图样和数据集及建立关联关系
		drawingName = util.getProValue(objEle, CommonProperty.DOC_NAME);
		try {
			/**
			 * 文档：根据图文档代号、项目名称或子项目名称、研制阶段、产品代号 组部件：组部件代号、项目名称或子项目名称、研制阶段、产品代号
			 * 
			 */
			// 1.1\创建工程图样
			outputStatus("正在创建工程图样对象,请稍等......");
			docObj = sdo.createItem(sdo.getHomeFolder(), "Ne7_EDrDoc", "", drawingName);
			outputStatus("工程图样创建完毕!");
			// 1.1\end
			// 1.2\创建数据集并建立关联关系
			outputStatus("正在创建dwg数据集对象,请稍等.....");
			dsObj = sdo.createDataset(docObj, drawingName, false);

			TCFileOperation fileOper = new TCFileOperation();
			String refName = TCUtil.getRefNameOfDataset(dsObj.getType())[0];
			fileOper.uploadFile(dsObj, refName, filePath);

			outputStatus("dwg数据集创建完毕!");
			// 1.2\end
			// 2\创建零件或者部件对象
			outputStatus("正在创建零部件对象,请稍等.....");
			if ("1".equals(requestBody.element("body").element("object").element("assembly").getTextTrim())) {
				itemObj = sdo.createItem(sdo.getHomeFolder(), "Ne7_Assembly", "", drawingName);
			} else {
				itemObj = sdo.createItem(sdo.getHomeFolder(), "Ne7_Cmponent", "", drawingName);
			}
			outputStatus("零部件创建完毕!");
			// 2\end
			// 3\工程图样和零部件建立关系
			outputStatus("正在创建工程图样和零部件对象关联关系,请稍等.....");
			tcop.createRelationship(sdo.getLastestItemRevision(itemObj), sdo.getLastestItemRevision(docObj), "IMAN_specification");
			outputStatus("关联关系创建完成!");
			// end
			succ = true;
		} catch (TCOperationException e) {
			succ = false;
		}
		// end

		if (succ) {
			stopProgress();
			outputStatus("注册图纸结束!");
			JOptionPane.showMessageDialog(this, "注册图纸操作成功");

			WizardButtonOperation.enableCancelBtn(true);
			WizardButtonOperation.enableNextBtn(true);
			WizardButtonOperation.updateBtn();

		} else {
			outputStatus("注册图纸结束!");
			JOptionPane.showMessageDialog(this, "注册图纸操作失败");

			WizardButtonOperation.enableCancelBtn(true);
			WizardButtonOperation.enablePrevBtn(true);
			WizardButtonOperation.updateBtn();
		}

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

	// @Override
	public WizardPanelNavResult allowNext11(String stepName, Map settings, Wizard wizard) {

		if (succ) {
			// 如果是辅助图样，则不需要创建产品结果
			if (Util.isFuzhuDrawing) {
				// 否则不能编辑产品结构，提示用户执行下一步操作将在本机上删除图纸
				int opt = JOptionPane.showConfirmDialog(dialog, "下一步操作会将当前图纸从本机上删除,如需要在本机上保留请提前备份，确定执行?", "", JOptionPane.YES_NO_OPTION);
				if (JOptionPane.OK_OPTION == opt) {
					// DSCheckInDrawingsWizardPanel.switchToCheckinStep(dialog,
					// params, orignalfilePath);
					return WizardPanelNavResult.PROCEED;
				} else {
					return WizardPanelNavResult.REMAIN_ON_PAGE;
				}
			} else {
				// 满足以下所有条件的装配件才能创建产品结构：
				// 1 必须是组件、或部件
				// 2 必须是第一页图纸
				// 3 该组件、部件必须是通过本接口或直接在Teamcenter中创建的
				// 不符合其中之一条件的装配件都不能通过接口处理产品结构
				String pageNumber = (String) params.get(CommonProperty.FILEOBJECT_PAGENUMBER);
				String drawingCode = (String) params.get(CommonProperty.DOC_DRAWINGCODE);
				if (Util.isAssembly(drawingCode) && "1".equals(pageNumber)) {// &&(!"产品".equals(Institute70Util.getAssType(drawingCode)))
					Object assObj = null;
					if (assObj != null) {
						String createTool = "CAD2008";
						// 如果装配件的创建工具为空，则说明是该装配件是在tc中创建的，可以编辑起产品结构
						if (createTool == null || createTool.length() <= 0) {
							return WizardPanelNavResult.PROCEED;
						} // 如果创建工具为“CADInterface”，则说明该装配件是通过cad接口创建的，可以编辑起产品结构
						else if ("CADInterface".equalsIgnoreCase(createTool)) {
							return WizardPanelNavResult.PROCEED;
						}
					}
				}
				// 否则不能编辑产品结构，提示用户执行下一步操作将在本机上删除图纸
				int opt = JOptionPane.showConfirmDialog(dialog, "下一步操作会将当前图纸从本机上删除,如需要在本机上保留请提前备份，确定执行?", "", JOptionPane.YES_NO_OPTION);
				if (JOptionPane.OK_OPTION == opt) {
					// DSCheckInDrawingsWizardPanel.switchToCheckinStep(drawingSaver,
					// params, orignalfilePath);
					return WizardPanelNavResult.PROCEED;
				} else {
					return WizardPanelNavResult.REMAIN_ON_PAGE;
				}
			}
		} else {
			return WizardPanelNavResult.REMAIN_ON_PAGE;
		}
	}

}
