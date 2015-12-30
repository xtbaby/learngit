package com.ufc.uif.qh3.acad.ui;

import java.io.File;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.dom4j.Element;
import org.netbeans.api.wizard.WizardDisplayer;
import org.netbeans.api.wizard.displayer.NavButtonManager;
import org.netbeans.api.wizard.displayer.WizardDisplayerImpl;
import org.netbeans.spi.wizard.Wizard;
import org.netbeans.spi.wizard.WizardController;
import org.netbeans.spi.wizard.WizardObserver;
import org.netbeans.spi.wizard.WizardPanelProvider;

import com.ufc.uif.qh3.acad.tools.CommonProperty;
import com.ufc.uif.qh3.acad.tools.Dom4jUtil;
import com.ufc.uif.qh3.acad.tools.WizardButtonOperation;
import com.ufc.uif.qh3.acad.ui.wizard.CheckInDrawingPanel;
import com.ufc.uif.qh3.acad.ui.wizard.DocumentInfoPanel;
import com.ufc.uif.qh3.acad.ui.wizard.DrawingInfoPanel;
import com.ufc.uif.qh3.acad.ui.wizard.ImportBOMPanel;
import com.ufc.uif.qh3.acad.ui.wizard.PartInfoPanel;
import com.ufc.uif.qh3.acad.ui.wizard.RegDrawingWizardPanel;
import com.ufc.uif.qh3.acad.ui.wizard.SummaryPanel;

public class SaveBOMWPageProvider extends WizardPanelProvider implements WizardObserver {

	/** �༭�㲿����Ϣ */
	public static final String PARTINFOPANEL = "PARTINFOPANEL";
	/** �༭ͼֽ��Ϣ */
	public static final String DRAWINGINFOPANEL = "DRAWINGINFOPANEL";
	/** ͼ����Ϣ */
	public static final String DOCUMENTINFOPANEL = "DOCUMENTINFOPANEL";
	/** ��ϢժҪ */
	public static final String SUMMARYPANEL = "SUMMARYPANEL";
	/** ѡ�񱣴�·�� */
	public static final String SELECTPANEL = "SELECTPANEL";
	/** ע��ͼֽ */
	public static final String REGDRAWING = "REGDRAWING";

	/** �������� */
	public static final String CREATEPANEL = "CREATEPANEL";
	/** ����ͼֽ */
	public static final String CHECKINDRAWINGPANEL = "CHECKINDRAWINGPANEL";
	/** �����Ʒ�ṹ */
	public static final String IMPORTBOMPANEL = "IMPORTBOMPANEL";
	/** ���� */
	public static final String FINISH = "FINISH";

	public SaveDrawingDialog frame;

	public Element requestBody;

	public JTree summaryTree;

	private Dom4jUtil dom4jUtil;
	private Element element;
	private RegDrawingWizardPanel regPanel;

	public SaveBOMWPageProvider(java.lang.String[] steps, java.lang.String[] descriptions, SaveDrawingDialog frame) {
		super("����ͼֽ", steps, descriptions);
		this.frame = frame;
		summaryTree = new JTree(new DefaultMutableTreeNode());
		requestBody = frame.requestBody;

		dom4jUtil = Dom4jUtil.getDom4jUtil();
		element = requestBody.element("body").element("object");

	}

	@Override
	protected JComponent createPanel(WizardController arg0, String id, Map settings) {

		if (DRAWINGINFOPANEL.equals(id)) {
			return new DrawingInfoPanel(frame);
		} else if (DOCUMENTINFOPANEL.equals(id)) {
			return new DocumentInfoPanel(frame);
		} else if (PARTINFOPANEL.equals(id)) {
			return new PartInfoPanel(frame);
		} else if (SUMMARYPANEL.equals(id)) {
			SummaryPanel p = new SummaryPanel(summaryTree);
			fullfilTreeContent();
			WizardDisplayerImpl displayer = (WizardDisplayerImpl) WizardDisplayer.defaultInstance;
			NavButtonManager bmr = displayer.getButtonManager();
			bmr.setPreBtnEnabled(false);
			return p;
		} else if (REGDRAWING.equals(id)) {
			return new RegDrawingWizardPanel(frame);
		} else if (CHECKINDRAWINGPANEL.equals(id)) {
			return new CheckInDrawingPanel(frame);
		} else if (IMPORTBOMPANEL.equals(id)) {
			return new ImportBOMPanel(frame);
		} else {
			return new JPanel();
		}

	}

	@Override
	public void navigabilityChanged(Wizard arg0) {

	}

	@Override
	public void selectionChanged(Wizard wizard) {

		if (SUMMARYPANEL.equals(wizard.getCurrentStep())) {
			WizardButtonOperation.renameNextBtnTxt("ע��ͼֽ");
			WizardButtonOperation.enableNextBtn(true);
		} else if (IMPORTBOMPANEL.equals(wizard.getCurrentStep())) {
			WizardButtonOperation.renameNextBtnTxt("������Ʒ�ṹ");
			WizardButtonOperation.enableNextBtn(false);
		} else if (REGDRAWING.equals(wizard.getCurrentStep())) {

			if (PartInfoPanel.isAssembly) {
				WizardButtonOperation.renameNextBtnTxt("��һ��");
				WizardButtonOperation.enableNextBtn(true);
			} else {
				WizardButtonOperation.renameNextBtnTxt("����ͼֽ");
				WizardButtonOperation.enableNextBtn(true);
			}

		} else {
			WizardButtonOperation.renameNextBtnTxt("��һ��");
			WizardButtonOperation.enableNextBtn(true);
		}

	}

	private void fullfilTreeContent() {
		DefaultMutableTreeNode top = (DefaultMutableTreeNode) summaryTree.getModel().getRoot();
		top.removeAllChildren();
		// ͼֽ��Ϣ
		DefaultMutableTreeNode drawingInfo = new DefaultMutableTreeNode("ͼֽ��Ϣ");
		DefaultMutableTreeNode productCodeNode = new DefaultMutableTreeNode("��Ʒ���ţ�" + dom4jUtil.getProValue(element, CommonProperty.PROJECT_PRODUCTNO));
		DefaultMutableTreeNode projectNameNode = new DefaultMutableTreeNode("��Ŀ���ƣ�" + dom4jUtil.getProValue(element, CommonProperty.PROJECT_NAME));
		DefaultMutableTreeNode zProjectNameNode = new DefaultMutableTreeNode("����Ŀ���ƣ�" + dom4jUtil.getProValue(element, CommonProperty.SUBPROJECT_NAME));
		DefaultMutableTreeNode belongToPahseNode = new DefaultMutableTreeNode("���ƽ׶Σ�" + dom4jUtil.getProValue(element, CommonProperty.PROJECT_PHASE));
		File file = new File(dom4jUtil.getProValue(element, CommonProperty.FILEOBJECT_LOCALPATH));
		DefaultMutableTreeNode fileNameNode = new DefaultMutableTreeNode("�ļ����ƣ�" + file.getName());
		DefaultMutableTreeNode drawingNoNode = new DefaultMutableTreeNode("ͼ �ţ�" + dom4jUtil.getProValue(element, CommonProperty.DOC_DRAWINGCODE));
		DefaultMutableTreeNode drawingSizeNode = new DefaultMutableTreeNode("ͼ ����" + dom4jUtil.getProValue(element, CommonProperty.DOC_DRAWINGSIZE));
		DefaultMutableTreeNode scaleNode = new DefaultMutableTreeNode("�� ����" + dom4jUtil.getProValue(element, CommonProperty.DOC_DRAWINGSCALE));
		DefaultMutableTreeNode pageNumberNode = new DefaultMutableTreeNode("�� �� ҳ��" + dom4jUtil.getProValue(element, CommonProperty.FILEOBJECT_PAGENUMBER));
		DefaultMutableTreeNode totalNumberNode = new DefaultMutableTreeNode("�� �� ҳ��" + dom4jUtil.getProValue(element, CommonProperty.DOC_TOTALNUMBER));
		DefaultMutableTreeNode createToolNode = new DefaultMutableTreeNode("�������ߣ�" + DrawingInfoPanel.authorToolF.getSelectedItem().toString());

		drawingInfo.add(productCodeNode);
		drawingInfo.add(projectNameNode);
		drawingInfo.add(zProjectNameNode);
		drawingInfo.add(belongToPahseNode);
		drawingInfo.add(fileNameNode);
		drawingInfo.add(drawingNoNode);
		drawingInfo.add(drawingSizeNode);
		drawingInfo.add(scaleNode);
		drawingInfo.add(pageNumberNode);
		drawingInfo.add(totalNumberNode);
		drawingInfo.add(createToolNode);
		// ----------------------------------------------end
		// ͼ����Ϣ
		DefaultMutableTreeNode docPathParams = new DefaultMutableTreeNode("ͼ����Ϣ");
		DefaultMutableTreeNode docCodeNode = new DefaultMutableTreeNode("ͼ�����룺" + dom4jUtil.getProValue(element, CommonProperty.DOC_CODE));
		DefaultMutableTreeNode docNumberNode = new DefaultMutableTreeNode("ͼ�����ţ�" + DocumentInfoPanel.docNumber);
		DefaultMutableTreeNode docNameNode = new DefaultMutableTreeNode("ͼ�����ƣ�" + dom4jUtil.getProValue(element, CommonProperty.DOC_NAME));
		DefaultMutableTreeNode docComponetNode = new DefaultMutableTreeNode("����ţ�" + DocumentInfoPanel.groupNumberF.getText());
		DefaultMutableTreeNode docSecretNode = new DefaultMutableTreeNode("�ĵ��ܼ���" + DocumentInfoPanel.secretLevelCmbox.getSelectedItem().toString());
		DefaultMutableTreeNode docFormatNode = new DefaultMutableTreeNode("�ļ���ʽ��" + "dwg");
		DefaultMutableTreeNode docTypeNode = new DefaultMutableTreeNode("ͼ�����ࣺ" + DocumentInfoPanel.drawingTypeCmbox.getSelectedItem().toString());
		DefaultMutableTreeNode docSignNode = new DefaultMutableTreeNode("ͼ����ǣ�" + DocumentInfoPanel.drawingSignCmbox.getSelectedItem().toString());

		docPathParams.add(docCodeNode);
		docPathParams.add(docNumberNode);
		docPathParams.add(docNameNode);
		docPathParams.add(docComponetNode);
		docPathParams.add(docSecretNode);
		docPathParams.add(docFormatNode);
		docPathParams.add(docTypeNode);
		docPathParams.add(docSignNode);
		// ----------------------------------------------end
		// �㲿����Ϣ
		DefaultMutableTreeNode partsPathParams = new DefaultMutableTreeNode("�㲿����Ϣ");
		DefaultMutableTreeNode partCodeNode = new DefaultMutableTreeNode("�㲿�����룺" + dom4jUtil.getProValue(element, CommonProperty.PARTS_COD));
		DefaultMutableTreeNode partNumberNode = new DefaultMutableTreeNode("�㲿�����ţ�" + DocumentInfoPanel.docNumber);
		DefaultMutableTreeNode partNameNode = new DefaultMutableTreeNode("�㲿�����ƣ�" + dom4jUtil.getProValue(element, CommonProperty.DOC_NAME));
		String typeName = "";
		String typeValue = "";
		if ("1".equals(element.element("assembly").getTextTrim())) {
			typeName = "��������";
			typeValue = PartInfoPanel.assTypeCmbox.getSelectedItem().toString();
		} else {
			typeName = "�������";
			typeValue = PartInfoPanel.cmpTypeCmbox.getSelectedItem().toString();
		}
		DefaultMutableTreeNode partTypeNode = new DefaultMutableTreeNode(typeName + "��" + typeValue);
		DefaultMutableTreeNode inOrOutNode = new DefaultMutableTreeNode("����/�⹺��" + PartInfoPanel.makeOrBuyCmbox.getSelectedItem().toString());

		partsPathParams.add(partCodeNode);
		partsPathParams.add(partNumberNode);
		partsPathParams.add(partNameNode);
		partsPathParams.add(partTypeNode);
		partsPathParams.add(inOrOutNode);

		// ----------------------------------------------end

		top.add(drawingInfo);
		top.add(docPathParams);
		top.add(partsPathParams);

		summaryTree.setRootVisible(true);
		summaryTree.expandPath(new TreePath(drawingInfo.getPath()));
		summaryTree.expandPath(new TreePath(docPathParams.getPath()));
		summaryTree.expandPath(new TreePath(partsPathParams.getPath()));
		// summaryTree.setScrollsOnExpand(true);
		summaryTree.scrollPathToVisible(new TreePath(drawingInfo.getPath()));
		summaryTree.scrollPathToVisible(new TreePath(docPathParams.getPath()));
		summaryTree.scrollPathToVisible(new TreePath(partsPathParams.getPath()));
		summaryTree.updateUI();

	}

	@Override
	public void stepsChanged(Wizard arg0) {

	}

}
