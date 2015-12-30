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

	// ��ʶע��ͼֽ�Ƿ�ɹ�
	public boolean succ;
	private Map params;
	private boolean processing;
	// ע��������Щȫ�ֱ�����Ϊ�˴�������ͼͼֽ����Ҫ

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

		// ���ô��ڵı�����ɫ
		this.setBackground(new Color(226, 245, 252));
	}

	private void initComponents() {
		// ��ʼ������
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

		progressL.setText("����");
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

		// �ı䰴ť״̬
		WizardButtonOperation.enableCancelBtn(false);
		WizardButtonOperation.enableNextBtn(false);
		WizardButtonOperation.enablePrevBtn(false);
		WizardButtonOperation.updateBtn();
		// ����������
		progressBar.setBackground(new java.awt.Color(241, 250, 255));
		progressBar.setForeground(Color.GREEN);
		startProgress();

		File file = null;

		// ��ȡdwg�ļ���ȫ·��
		String filePath = Dom4jUtil.getDom4jUtil().getProValue(objEle, CommonProperty.FILEOBJECT_LOCALPATH);
		// attrValues.put(CommonProperty.FILEOBJECT_LOCALPATH,
		// filePath);

		// ���ͼֽ���ڣ����˳�ע��ͼֽ����
		file = new File(filePath);
		if (!file.exists()) {
			stopProgress();
			JOptionPane.showMessageDialog(this, "�ļ�" + filePath + " �����ڣ�������ע��ͼֽ����");
			outputStatus("�ļ�" + filePath + " �����ڣ�������ע��ͼֽ����");
			return;
		}
		// ��ԭʼͼ·������������ע����������Ϊ��ͳһ������ͼ��һ��ͼͼֽ�Ĵ���
		orignalfilePath = filePath;

		// 1\��������ͼ�������ݼ�������������ϵ
		drawingName = util.getProValue(objEle, CommonProperty.DOC_NAME);
		try {
			/**
			 * �ĵ�������ͼ�ĵ����š���Ŀ���ƻ�����Ŀ���ơ����ƽ׶Ρ���Ʒ���� �鲿�����鲿�����š���Ŀ���ƻ�����Ŀ���ơ����ƽ׶Ρ���Ʒ����
			 * 
			 */
			// 1.1\��������ͼ��
			outputStatus("���ڴ�������ͼ������,���Ե�......");
			docObj = sdo.createItem(sdo.getHomeFolder(), "Ne7_EDrDoc", "", drawingName);
			outputStatus("����ͼ���������!");
			// 1.1\end
			// 1.2\�������ݼ�������������ϵ
			outputStatus("���ڴ���dwg���ݼ�����,���Ե�.....");
			dsObj = sdo.createDataset(docObj, drawingName, false);

			TCFileOperation fileOper = new TCFileOperation();
			String refName = TCUtil.getRefNameOfDataset(dsObj.getType())[0];
			fileOper.uploadFile(dsObj, refName, filePath);

			outputStatus("dwg���ݼ��������!");
			// 1.2\end
			// 2\����������߲�������
			outputStatus("���ڴ����㲿������,���Ե�.....");
			if ("1".equals(requestBody.element("body").element("object").element("assembly").getTextTrim())) {
				itemObj = sdo.createItem(sdo.getHomeFolder(), "Ne7_Assembly", "", drawingName);
			} else {
				itemObj = sdo.createItem(sdo.getHomeFolder(), "Ne7_Cmponent", "", drawingName);
			}
			outputStatus("�㲿���������!");
			// 2\end
			// 3\����ͼ�����㲿��������ϵ
			outputStatus("���ڴ�������ͼ�����㲿�����������ϵ,���Ե�.....");
			tcop.createRelationship(sdo.getLastestItemRevision(itemObj), sdo.getLastestItemRevision(docObj), "IMAN_specification");
			outputStatus("������ϵ�������!");
			// end
			succ = true;
		} catch (TCOperationException e) {
			succ = false;
		}
		// end

		if (succ) {
			stopProgress();
			outputStatus("ע��ͼֽ����!");
			JOptionPane.showMessageDialog(this, "ע��ͼֽ�����ɹ�");

			WizardButtonOperation.enableCancelBtn(true);
			WizardButtonOperation.enableNextBtn(true);
			WizardButtonOperation.updateBtn();

		} else {
			outputStatus("ע��ͼֽ����!");
			JOptionPane.showMessageDialog(this, "ע��ͼֽ����ʧ��");

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
			// ����Ǹ���ͼ��������Ҫ������Ʒ���
			if (Util.isFuzhuDrawing) {
				// �����ܱ༭��Ʒ�ṹ����ʾ�û�ִ����һ���������ڱ�����ɾ��ͼֽ
				int opt = JOptionPane.showConfirmDialog(dialog, "��һ�������Ὣ��ǰͼֽ�ӱ�����ɾ��,����Ҫ�ڱ����ϱ�������ǰ���ݣ�ȷ��ִ��?", "", JOptionPane.YES_NO_OPTION);
				if (JOptionPane.OK_OPTION == opt) {
					// DSCheckInDrawingsWizardPanel.switchToCheckinStep(dialog,
					// params, orignalfilePath);
					return WizardPanelNavResult.PROCEED;
				} else {
					return WizardPanelNavResult.REMAIN_ON_PAGE;
				}
			} else {
				// ������������������װ������ܴ�����Ʒ�ṹ��
				// 1 ������������򲿼�
				// 2 �����ǵ�һҳͼֽ
				// 3 �����������������ͨ�����ӿڻ�ֱ����Teamcenter�д�����
				// ����������֮һ������װ���������ͨ���ӿڴ����Ʒ�ṹ
				String pageNumber = (String) params.get(CommonProperty.FILEOBJECT_PAGENUMBER);
				String drawingCode = (String) params.get(CommonProperty.DOC_DRAWINGCODE);
				if (Util.isAssembly(drawingCode) && "1".equals(pageNumber)) {// &&(!"��Ʒ".equals(Institute70Util.getAssType(drawingCode)))
					Object assObj = null;
					if (assObj != null) {
						String createTool = "CAD2008";
						// ���װ����Ĵ�������Ϊ�գ���˵���Ǹ�װ�������tc�д����ģ����Ա༭���Ʒ�ṹ
						if (createTool == null || createTool.length() <= 0) {
							return WizardPanelNavResult.PROCEED;
						} // �����������Ϊ��CADInterface������˵����װ�����ͨ��cad�ӿڴ����ģ����Ա༭���Ʒ�ṹ
						else if ("CADInterface".equalsIgnoreCase(createTool)) {
							return WizardPanelNavResult.PROCEED;
						}
					}
				}
				// �����ܱ༭��Ʒ�ṹ����ʾ�û�ִ����һ���������ڱ�����ɾ��ͼֽ
				int opt = JOptionPane.showConfirmDialog(dialog, "��һ�������Ὣ��ǰͼֽ�ӱ�����ɾ��,����Ҫ�ڱ����ϱ�������ǰ���ݣ�ȷ��ִ��?", "", JOptionPane.YES_NO_OPTION);
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
