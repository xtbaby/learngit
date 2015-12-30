package com.ufc.uif.qh3.acad.ui.wizard;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.dom4j.Element;
import org.netbeans.api.wizard.WizardDisplayer;
import org.netbeans.api.wizard.displayer.NavButtonManager;
import org.netbeans.api.wizard.displayer.WizardDisplayerImpl;
import org.netbeans.spi.wizard.WizardPage;

import com.ufc.uif.base_adaptor.AdaptorWriter;
import com.ufc.uif.qh3.acad.tools.CommonProperty;
import com.ufc.uif.qh3.acad.tools.Dom4jUtil;
import com.ufc.uif.qh3.acad.ui.SaveDrawingDialog;

public class DrawingInfoPanel extends WizardPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private javax.swing.JTextField drawingCodeF;
	private javax.swing.JLabel drawingCodeL;
	private javax.swing.JTextField drawingNameF;
	private javax.swing.JLabel drawingNameL;
	private javax.swing.JTextField drawingScaleF;
	private javax.swing.JLabel drawingScaleL;
	private javax.swing.JLabel drawingSizeL;
	private javax.swing.JTextField drawingSizeF;
	private JComboBox<String> drawingSizeCmbox;
	private javax.swing.JLabel authorToolL;
	private javax.swing.JLabel phaseL;
	public static JComboBox<String> authorToolF;
	private javax.swing.JLabel toolVersionL;
	private javax.swing.JTextField toolVersionF;
	private javax.swing.JTextField pageNumberF;
	private javax.swing.JLabel pageNumberL;
	private javax.swing.JTextField phaseF;
	private javax.swing.JLabel totalNumberL;
	private javax.swing.JTextField totalNumberF;
	private javax.swing.JTextField projectNameF;
	private javax.swing.JLabel projectNameL;
	@SuppressWarnings("rawtypes")
	private javax.swing.JComboBox subProjectNameCmbox;
	private javax.swing.JLabel subProjectNameL;
	private javax.swing.JLabel productNoL;
	private javax.swing.JTextField productNoF;

	private Element requestBody;
	private AdaptorWriter out;
	private JButton next, cancel;

	public static Map<String, Object> DrawPROS;

	private String[] drawingSizeList = new String[] { "A4", "A3X5", "A2X3", "A3X6", "A3X7", "A0", "A2X4", "A3", "A1X3", "A4X3", "A0X2", "A1X4", "A2", "A4X4", "A4X5", "A4X6", "A0X3", "A3X3", "A0X4",
			"A4X7", "A4X8", "A4X9", "A1", "A3X4", "A2X5" };

	public DrawingInfoPanel(SaveDrawingDialog dialog) {
		this.out = dialog.out;
		this.requestBody = dialog.requestBody;

		initComponents();
		fillPanelContent();

		WizardDisplayerImpl displayer = (WizardDisplayerImpl) WizardDisplayer.defaultInstance;
		NavButtonManager bmr = displayer.getButtonManager();
		bmr.setNextBtnEnabled(true);
		next = bmr.getNext();

		cancel = bmr.getCancel();
		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				try {
					if("取消".equals(cancel.getText())){
						out.setFuncID("SaveDrawing");
						out.setResult("false");
						out.setDesc("用户取消导入。");
						out.sendResultToUI();
					}else{
						out.setFuncID("SaveDrawing");
						out.setResult("true");
						out.setDesc("导入成功。");
						out.sendResultToUI();
					}
					
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}

		});
		next.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				DrawPROS = collectDocInfo();
			}
		});
		// 设置背景颜色
		this.setBackground(new Color(226, 245, 252));
	}

	private void initComponents() {

		Element objEle = requestBody.element("body").element("object");
		Dom4jUtil util = Dom4jUtil.getDom4jUtil();

		File drawingFile = new File(util.getProValue(objEle, CommonProperty.FILEOBJECT_LOCALPATH));

		drawingNameL = new JLabel();
		drawingNameF = new JTextField(20);
		authorToolL = new JLabel();

		toolVersionF = new JTextField();
		drawingCodeL = new JLabel();
		drawingCodeF = new JTextField();
		drawingSizeL = new JLabel();
		drawingSizeF = new JTextField();
		drawingSizeCmbox = new JComboBox<String>();
		drawingScaleL = new JLabel();
		drawingScaleF = new JTextField();
		pageNumberL = new JLabel();
		pageNumberF = new JTextField();
		totalNumberL = new JLabel();
		totalNumberF = new JTextField();
		projectNameL = new JLabel();
		projectNameF = new JTextField();
		subProjectNameL = new JLabel();
		subProjectNameCmbox = new JComboBox<String>();
		authorToolF = new JComboBox<String>(new String[] { "CAD2008", "CAD2010", "CAD2012" });
		phaseL = new javax.swing.JLabel();
		phaseF = new javax.swing.JTextField();
		productNoL = new javax.swing.JLabel();
		productNoF = new javax.swing.JTextField();

		drawingNameL.setText("文件名称");
		drawingNameF.setEnabled(false);
		drawingNameF.setName("FILENAME");
		drawingNameF.setText(drawingFile.getName());
		drawingNameL.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

		authorToolL.setText("创建工具");
		authorToolL.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

		drawingCodeL.setText("图  号");
		drawingCodeF.setName(CommonProperty.DOC_DRAWINGCODE); // NOI18N
		drawingCodeF.setText(util.getProValue(objEle, CommonProperty.DOC_DRAWINGCODE));
		drawingCodeL.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

		drawingSizeL.setText("图  幅");
		drawingSizeL.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		for (String drawingSize : drawingSizeList) {
			drawingSizeCmbox.addItem(drawingSize);
		}
		// 默认选择第一个
		drawingSizeCmbox.setSelectedIndex(0);
		// 如果从客户端传过值，则选择传过来的值
		drawingSizeCmbox.setSelectedItem(util.getProValue(objEle, CommonProperty.DOC_DRAWINGSIZE));

		drawingScaleL.setText("比  例");
		drawingScaleF.setEnabled(false);
		drawingScaleF.setName(CommonProperty.DOC_DRAWINGSCALE);
		drawingScaleF.setText(util.getProValue(objEle, CommonProperty.DOC_DRAWINGSCALE));
		drawingScaleL.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

		pageNumberL.setText("第 几 页");
		pageNumberF.setEnabled(false);
		pageNumberF.setName(CommonProperty.FILEOBJECT_PAGENUMBER); // NOI18N
		pageNumberF.setText(util.getProValue(objEle, CommonProperty.FILEOBJECT_PAGENUMBER));
		pageNumberL.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

		totalNumberL.setText("共 几 页");
		totalNumberF.setEnabled(false);
		totalNumberF.setName(CommonProperty.DOC_TOTALNUMBER); // NOI18N
		totalNumberF.setText(util.getProValue(objEle, CommonProperty.DOC_TOTALNUMBER));
		totalNumberL.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

		productNoL.setText("产品代号");
		productNoF.setEnabled(false);
		productNoF.setName(CommonProperty.PROJECT_PRODUCTNO);
		productNoF.setText(util.getProValue(objEle, CommonProperty.PROJECT_PRODUCTNO));
		productNoL.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

		projectNameL.setText("项目名称");
		projectNameF.setEnabled(false);
		projectNameF.setName(CommonProperty.PROJECT_NAME);
		projectNameF.setText(util.getProValue(objEle, CommonProperty.PROJECT_NAME));
		projectNameL.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

		subProjectNameL.setText("子项目名称");
		subProjectNameL.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		subProjectNameCmbox.setName(CommonProperty.DOC_SUBPROJECTNAME);

		phaseL.setText("研制阶段");
		phaseF.setEnabled(false);
		phaseF.setName(CommonProperty.PROJECT_PHASE);
		phaseF.setText(util.getProValue(objEle, CommonProperty.PROJECT_PHASE));
		phaseL.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

	}

	private void fillPanelContent() {

		setLayout(new GridBagLayout());
		GridBagConstraints gbs = new GridBagConstraints();
		gbs.fill = GridBagConstraints.BOTH;
		gbs.anchor = GridBagConstraints.CENTER;
		gbs.insets = new Insets(5, 5, 5, 5);

		gbs.gridx = 0;
		gbs.gridy = 0;
		add(productNoL, gbs);
		gbs.gridx = 1;
		gbs.gridy = 0;
		add(productNoF, gbs);

		gbs.gridx = 0;
		gbs.gridy = 1;
		add(projectNameL, gbs);
		gbs.gridx = 1;
		gbs.gridy = 1;
		add(projectNameF, gbs);

		gbs.gridx = 0;
		gbs.gridy = 2;
		add(subProjectNameL, gbs);
		gbs.gridx = 1;
		gbs.gridy = 2;
		add(subProjectNameCmbox, gbs);

		gbs.gridx = 0;
		gbs.gridy = 3;
		add(phaseL, gbs);
		gbs.gridx = 1;
		gbs.gridy = 3;
		add(phaseF, gbs);

		gbs.gridx = 0;
		gbs.gridy = 4;
		add(drawingNameL, gbs);
		gbs.gridx = 1;
		gbs.gridy = 4;
		add(drawingNameF, gbs);

		gbs.gridx = 0;
		gbs.gridy = 5;
		add(drawingCodeL, gbs);
		gbs.gridx = 1;
		gbs.gridy = 5;
		add(drawingCodeF, gbs);

		gbs.gridx = 0;
		gbs.gridy = 6;
		add(drawingSizeL, gbs);
		gbs.gridx = 1;
		gbs.gridy = 6;
		// add(drawingSizeF, gbs);
		add(drawingSizeCmbox, gbs);

		gbs.gridx = 0;
		gbs.gridy = 7;
		add(drawingScaleL, gbs);
		gbs.gridx = 1;
		gbs.gridy = 7;
		add(drawingScaleF, gbs);

		gbs.gridx = 0;
		gbs.gridy = 8;
		add(pageNumberL, gbs);
		gbs.gridx = 1;
		gbs.gridy = 8;
		add(pageNumberF, gbs);

		gbs.gridx = 0;
		gbs.gridy = 9;
		add(totalNumberL, gbs);
		gbs.gridx = 1;
		gbs.gridy = 9;
		add(totalNumberF, gbs);

		gbs.gridx = 0;
		gbs.gridy = 10;
		add(authorToolL, gbs);
		gbs.gridx = 1;
		gbs.gridy = 10;
		add(authorToolF, gbs);
	}

	@Override
	protected String validateContents(Component component, Object event) {
		// 标识信息是否完全
		boolean result = true;
		String tip = "图纸信息(";
		if (drawingCodeF.getText() == null || drawingCodeF.getText().length() == 0) {
			tip += "图号、";
			result = false;
		}
		if (drawingSizeCmbox.getSelectedItem() == null || drawingSizeCmbox.getSelectedItem().toString().length() == 0) {
			tip += "图幅、";
			result = false;
		}
		if (drawingScaleF.getText() == null || drawingScaleF.getText().length() == 0) {
			tip += "比例、";
			result = false;
		}
		if (pageNumberF.getText() == null || pageNumberF.getText().length() == 0) {
			tip += "第几页、";
			result = false;
		}
		if (totalNumberF.getText() == null || totalNumberF.getText().length() == 0) {
			tip += "共几页、";
			result = false;
		}
		if (authorToolF.getSelectedItem() == null || authorToolF.getSelectedItem().toString().length() == 0) {
			tip += "创建工具、";
			result = false;
		}
		// 去掉最后的一个“、”
		tip = tip.substring(0, tip.length() - 1);
		tip += ")未填写，请返回CAD工具中填写后再进行保存图纸操作";
		if (!result) {
			return tip;
		} else {
			return null;
		}
	}

	/***************************************************************************
	 * 收集图样文档信息
	 * 
	 * @return
	 */
	public Map<String, Object> collectDocInfo() {

		Map<String, Object> pros = new HashMap<String, Object>();

		return pros;
	}
}
