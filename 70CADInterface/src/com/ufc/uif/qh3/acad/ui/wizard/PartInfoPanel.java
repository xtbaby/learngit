package com.ufc.uif.qh3.acad.ui.wizard;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
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
import com.ufc.uif.qh3.acad.tools.Util;
import com.ufc.uif.qh3.acad.ui.SaveDrawingDialog;

public class PartInfoPanel extends WizardPage {

	private Element requestBody;
	private JTextField assNumberF;
	private JLabel assNumberL;
	private JTextField assNameF;
	private JLabel assNameL;

	private JLabel assTypeL;
	private JLabel assCodeL;
	private JLabel makeOrBuyL;
	private JTextField materialF;
	private JTextField materialNoUPF;
	private JTextField materialNoDOWNF;
	private JTextField assCodeF;

	private JLabel cmpTypeL;

	public static JComboBox<String> makeOrBuyCmbox;
	public static JComboBox<String> assTypeCmbox;
	public static JComboBox<String> cmpTypeCmbox;
	private Element objEle;
	private Dom4jUtil util;
	private JButton next, cancel;
	public static boolean isAssembly = false;
	private static final long serialVersionUID = 1L;

	private Element ass;

	// end

	public PartInfoPanel(SaveDrawingDialog dialog) {
		
		this.requestBody = dialog.requestBody;

		objEle = requestBody.element("body").element("object");
		util = Dom4jUtil.getDom4jUtil();

		ass = requestBody.element("body").element("object").element("assembly");
		if ("1".equals(ass.getTextTrim())) {
			isAssembly = true;
			initComponents();
			fulfillAssembly();
		} else {
			isAssembly = false;
			initComponents();
			fulfillPart();
		}

		WizardDisplayerImpl displayer = (WizardDisplayerImpl) WizardDisplayer.defaultInstance;
		NavButtonManager bmr = displayer.getButtonManager();
		next = bmr.getNext();

		
		next.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// PartPROS = collectPartInfo();
			}

		});

		// 设置窗口的背景颜色
		this.setBackground(new Color(226, 245, 252));
	}

	public void initComponents() {

		assNumberL = new javax.swing.JLabel();
		assNameL = new javax.swing.JLabel();
		assNumberF = new javax.swing.JTextField(20);
		assNameF = new javax.swing.JTextField();
		assCodeL = new javax.swing.JLabel();
		assCodeF = new javax.swing.JTextField();
		assTypeL = new javax.swing.JLabel();
		assTypeCmbox = new JComboBox<String>();
		cmpTypeL = new javax.swing.JLabel();
		cmpTypeCmbox = new JComboBox<String>();
		makeOrBuyL = new javax.swing.JLabel();
		makeOrBuyCmbox = new JComboBox<String>();

		materialF = new javax.swing.JTextField();
		materialNoUPF = new javax.swing.JTextField();
		materialNoDOWNF = new javax.swing.JTextField();

		assNumberL.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		assNumberL.setText("零部件代号");
		assNumberF.setEnabled(false);
		assNumberF.setName(CommonProperty.ASSEMBLY_ASSNUMBER);
		assNumberF.setText(DocumentInfoPanel.docNumber);

		assNameL.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		assNameL.setText("零部件名称");
		if (Util.isFuzhuDrawing) {
			assNameF.setEnabled(false);
		}
		assNameF.setName(CommonProperty.ASSEMBLY_ASSNAME);
		assNameF.setText(util.getProValue(objEle, CommonProperty.DOC_NAME));

		assCodeL.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		assCodeL.setText("零部件编码");
		assCodeF.setName(CommonProperty.PARTS_COD); // NOI18N
		assCodeF.setEnabled(false);

		assCodeF.setText(util.getProValue(objEle, CommonProperty.PARTS_COD));

		assTypeL.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		assTypeL.setText("部件类型");
		assTypeCmbox.setModel(new DefaultComboBoxModel<String>(new String[] { "部件", "组件", "系统/机构", "产品" }));
		assTypeCmbox.setName(CommonProperty.ASSEMBLY_PARTTYPE);

		cmpTypeL.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		cmpTypeL.setText("零件类型");
		cmpTypeCmbox.setModel(new DefaultComboBoxModel<String>(new String[] { "机械", "橡胶件", "紧固件", "电子" }));
		cmpTypeCmbox.setName(CommonProperty.PART_TYPE);

		makeOrBuyL.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		makeOrBuyL.setText("自制/外购");
		makeOrBuyCmbox.setModel(new DefaultComboBoxModel<String>(new String[] { "自制", "外购" }));
		makeOrBuyCmbox.setName(CommonProperty.PART_MAKEORBUY);

		makeOrBuyCmbox.setSelectedItem(util.getProValue(objEle, CommonProperty.PART_MAKEORBUY));

	}

	@SuppressWarnings("unused")
	private boolean validateMaterial() {
		boolean result = false;
		boolean flag = false;

		String tmpString = "";

		if (materialF.getText() != null && materialF.getText().trim().length() > 0) {
			tmpString += materialF.getText().trim();
			flag = true;
		}

		tmpString += "@@";

		if (materialNoUPF.getText() != null && materialNoUPF.getText().trim().length() > 0) {
			tmpString += materialNoUPF.getText().trim();
			flag = true;
		}

		tmpString += "@@";

		if (materialNoDOWNF.getText() != null && materialNoDOWNF.getText().trim().length() > 0) {
			flag = true;
			tmpString += materialNoDOWNF.getText().trim();
		}
		if (flag) {

			Map<String, String> tmp = new HashMap<String, String>();
			tmp.put(CommonProperty.MATERIAL_NAMEANDCODE, tmpString);

			result = true;

		}

		return result;
	}

	private void fulfillPart() {

		setLayout(new GridBagLayout());
		GridBagConstraints gbs = new GridBagConstraints();
		gbs.fill = GridBagConstraints.BOTH;
		gbs.anchor = GridBagConstraints.CENTER;
		gbs.insets = new Insets(5, 5, 5, 5);

		gbs.gridx = 0;
		gbs.gridy = 0;
		add(assCodeL, gbs);
		gbs.gridx = 1;
		gbs.gridy = 0;
		add(assCodeF, gbs);

		gbs.gridx = 0;
		gbs.gridy = 1;
		add(assNumberL, gbs);
		gbs.gridx = 1;
		gbs.gridy = 1;
		add(assNumberF, gbs);

		gbs.gridx = 0;
		gbs.gridy = 2;
		add(assNameL, gbs);
		gbs.gridx = 1;
		gbs.gridy = 2;
		add(assNameF, gbs);

		gbs.gridx = 0;
		gbs.gridy = 3;
		add(cmpTypeL, gbs);
		gbs.gridx = 1;
		gbs.gridy = 3;
		add(cmpTypeCmbox, gbs);

		gbs.gridx = 0;
		gbs.gridy = 5;
		add(makeOrBuyL, gbs);
		gbs.gridx = 1;
		gbs.gridy = 5;
		add(makeOrBuyCmbox, gbs);

	}

	private void fulfillAssembly() {
		setLayout(new GridBagLayout());
		GridBagConstraints gbs = new GridBagConstraints();
		gbs.fill = GridBagConstraints.BOTH;
		gbs.anchor = GridBagConstraints.CENTER;
		gbs.insets = new Insets(5, 5, 5, 5);

		gbs.gridx = 0;
		gbs.gridy = 0;
		add(assCodeL, gbs);
		gbs.gridx = 1;
		gbs.gridy = 0;
		add(assCodeF, gbs);

		gbs.gridx = 0;
		gbs.gridy = 1;
		add(assNumberL, gbs);
		gbs.gridx = 1;
		gbs.gridy = 1;
		add(assNumberF, gbs);

		gbs.gridx = 0;
		gbs.gridy = 2;
		add(assNameL, gbs);
		gbs.gridx = 1;
		gbs.gridy = 2;
		add(assNameF, gbs);

		gbs.gridx = 0;
		gbs.gridy = 3;
		add(assTypeL, gbs);
		gbs.gridx = 1;
		gbs.gridy = 3;
		add(assTypeCmbox, gbs);

		gbs.gridx = 0;
		gbs.gridy = 5;
		add(makeOrBuyL, gbs);
		gbs.gridx = 1;
		gbs.gridy = 5;
		add(makeOrBuyCmbox, gbs);
	}

	@Override
	protected String validateContents(Component component, Object event) {
		return null;
	}

	public static String getDescription() {
		return "零部件信息";
	}

	// 收集当前属性信息
	public Map<String, Object> collectPartInfo() {
		Map<String, Object> pros = new HashMap<String, Object>();
		return pros;
	}
}
