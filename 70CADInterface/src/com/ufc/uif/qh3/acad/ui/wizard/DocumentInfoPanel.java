package com.ufc.uif.qh3.acad.ui.wizard;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;

import javax.swing.DefaultComboBoxModel;
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
import com.ufc.uif.qh3.acad.tools.Util;
import com.ufc.uif.qh3.acad.ui.SaveDrawingDialog;

public class DocumentInfoPanel extends WizardPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JButton next, cancel;

	private JTextField docCodeF;
	private JLabel docCodeL;
	private JComboBox<String> docFormatCmbox;
	private JLabel docFormatL;

	private JTextField docNameF;
	private JLabel docNameL;
	private JTextField docNumberF;
	private JLabel docNumberL;

	private JLabel drawingTypeL;

	private JLabel secretLevelL;
	private JLabel groupNumberL;

	private JLabel drawingSignL;
	private Element requestBody;

	public static JTextField groupNumberF;
	public static JComboBox<String> secretLevelCmbox;
	public static JComboBox<String> drawingTypeCmbox;
	public static JComboBox<String> drawingSignCmbox;

	// 当前图纸的图档代号
	public static String docNumber = "";

	private Element objEle;
	private Dom4jUtil util;

	public DocumentInfoPanel(SaveDrawingDialog dialog) {

		this.requestBody = dialog.requestBody;
	
		// 根据项目代号、研制阶段和图档代号查找工程图档
		Element objEle = requestBody.element("body").element("object");
		Dom4jUtil util = Dom4jUtil.getDom4jUtil();

		docNumber = util.getProValue(objEle, "图号");
		docNumber = Util.replaceCPointToEPoint(docNumber);
		docNumber = Util.replaceEPointToDLine(docNumber);

		initComponents();
		fillPanelContent();

		this.setBackground(new Color(226, 245, 252));
		WizardDisplayerImpl displayer = (WizardDisplayerImpl) WizardDisplayer.defaultInstance;
		NavButtonManager mgr = displayer.getButtonManager();
		next = mgr.getNext();

		next.setEnabled(false);
	}

	private void initComponents() {

		objEle = requestBody.element("body").element("object");
		util = Dom4jUtil.getDom4jUtil();

		docNumberL = new JLabel();
		docCodeF = new JTextField(20);

		docNameL = new JLabel();
		docNumberF = new JTextField();
		docCodeL = new JLabel();
		docNameF = new JTextField();
		groupNumberL = new JLabel();
		groupNumberF = new JTextField();

		secretLevelL = new JLabel();
		secretLevelCmbox = new JComboBox<String>();
		docFormatL = new JLabel();
		docFormatCmbox = new JComboBox<String>();
		drawingTypeL = new JLabel();
		drawingTypeCmbox = new JComboBox<String>();
		drawingSignL = new JLabel();
		drawingSignCmbox = new JComboBox<String>();

		docNumberL.setText("图样代号");
		docNumberF.setEnabled(false);
		docNumberF.setName(CommonProperty.DOC_NUMBER);
		docNumberF.setText(docNumber);
		docNumberL.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

		docNameL.setText("图样名称");
		docNameF.setEnabled(false);
		docNameF.setName(CommonProperty.DOC_NAME);
		docNameF.setText(util.getProValue(objEle, CommonProperty.DOC_NAME));
		docNameL.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

		docCodeL.setText("图样编码");
		docCodeF.setName(CommonProperty.DOC_CODE);
		docCodeF.setEnabled(false);
		// 设置值-pengst
		docCodeF.setText("");

		groupNumberL.setText("组件号");
		groupNumberL.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		groupNumberF.setName(CommonProperty.DOC_GROUPNUMBER);
		groupNumberF.setText(util.getProValue(objEle, CommonProperty.DOC_GROUPNUMBER));

		drawingSignL.setText("图样标记");
		drawingSignL.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		drawingSignCmbox.setEditable(false);
		drawingSignCmbox.setName(CommonProperty.DOC_DRAWINGSIGN); // NOI18N
		drawingSignCmbox.setModel(new DefaultComboBoxModel<String>(new String[] { "", "S", "S1", "S2", "S3", "S4", "S5", "SA", "SAB" }));
		String drawingSign = util.getProValue(objEle, CommonProperty.DOC_DRAWINGSIGN);
		if (drawingSign != null && drawingSign.length() > 0) {
			drawingSignCmbox.setSelectedItem(drawingSign);
		}

		secretLevelL.setText("文档密级");
		secretLevelL.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		secretLevelCmbox.setModel(new DefaultComboBoxModel<String>(new String[] { "", "秘密", "机密", "保留" }));
		secretLevelCmbox.setName(CommonProperty.DOC_SECRETLEVEL); // NOI18N

		docFormatL.setText("文档格式");
		docFormatL.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		docFormatCmbox.setModel(new DefaultComboBoxModel<String>(new String[] { "dwg" }));
		docFormatCmbox.setEnabled(false);
		docFormatCmbox.setName(CommonProperty.DOC_FORMAT); // NOI18N

		drawingTypeL.setText("图样分类");
		drawingTypeL.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		drawingTypeCmbox.setModel(new DefaultComboBoxModel<String>(new String[] { "Ⅰ", "Ⅱ", "Ⅲ", "Ⅳ" }));
		drawingTypeCmbox.setName(CommonProperty.DOC_DRAWINGTYPE); // NOI18N
	}

	private void fillPanelContent() {
		setLayout(new GridBagLayout());
		GridBagConstraints gbs = new GridBagConstraints();
		gbs.fill = GridBagConstraints.BOTH;
		gbs.anchor = GridBagConstraints.CENTER;
		gbs.insets = new Insets(5, 5, 5, 5);

		gbs.gridx = 0;
		gbs.gridy = 0;
		add(docCodeL, gbs);
		gbs.gridx = 1;
		gbs.gridy = 0;
		add(docCodeF, gbs);

		gbs.gridx = 0;
		gbs.gridy = 1;
		add(docNumberL, gbs);
		gbs.gridx = 1;
		gbs.gridy = 1;
		add(docNumberF, gbs);

		gbs.gridx = 0;
		gbs.gridy = 2;
		add(docNameL, gbs);
		gbs.gridx = 1;
		gbs.gridy = 2;
		add(docNameF, gbs);

		gbs.gridx = 0;
		gbs.gridy = 3;
		add(groupNumberL, gbs);
		gbs.gridx = 1;
		gbs.gridy = 3;
		add(groupNumberF, gbs);

		gbs.gridx = 0;
		gbs.gridy = 5;
		add(secretLevelL, gbs);
		gbs.gridx = 1;
		gbs.gridy = 5;
		add(secretLevelCmbox, gbs);

		gbs.gridx = 0;
		gbs.gridy = 6;
		add(docFormatL, gbs);
		gbs.gridx = 1;
		gbs.gridy = 6;
		add(docFormatCmbox, gbs);

		gbs.gridx = 0;
		gbs.gridy = 7;
		add(drawingTypeL, gbs);
		gbs.gridx = 1;
		gbs.gridy = 7;
		add(drawingTypeCmbox, gbs);

		gbs.gridx = 0;
		gbs.gridy = 8;
		add(drawingSignL, gbs);
		gbs.gridx = 1;
		gbs.gridy = 8;
		add(drawingSignCmbox, gbs);
	}

	@Override
	protected String validateContents(Component component, Object event) {
		return null;
	}
}
