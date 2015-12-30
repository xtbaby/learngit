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

	/** 编辑零部件信息 */
	public static final String PARTINFOPANEL = "PARTINFOPANEL";
	/** 编辑图纸信息 */
	public static final String DRAWINGINFOPANEL = "DRAWINGINFOPANEL";
	/** 图档信息 */
	public static final String DOCUMENTINFOPANEL = "DOCUMENTINFOPANEL";
	/** 信息摘要 */
	public static final String SUMMARYPANEL = "SUMMARYPANEL";
	/** 选择保存路径 */
	public static final String SELECTPANEL = "SELECTPANEL";
	/** 注册图纸 */
	public static final String REGDRAWING = "REGDRAWING";

	/** 创建对象 */
	public static final String CREATEPANEL = "CREATEPANEL";
	/** 检入图纸 */
	public static final String CHECKINDRAWINGPANEL = "CHECKINDRAWINGPANEL";
	/** 导入产品结构 */
	public static final String IMPORTBOMPANEL = "IMPORTBOMPANEL";
	/** 结束 */
	public static final String FINISH = "FINISH";

	public SaveDrawingDialog frame;

	public Element requestBody;

	public JTree summaryTree;

	private Dom4jUtil dom4jUtil;
	private Element element;
	private RegDrawingWizardPanel regPanel;

	public SaveBOMWPageProvider(java.lang.String[] steps, java.lang.String[] descriptions, SaveDrawingDialog frame) {
		super("保存图纸", steps, descriptions);
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
			WizardButtonOperation.renameNextBtnTxt("注册图纸");
			WizardButtonOperation.enableNextBtn(true);
		} else if (IMPORTBOMPANEL.equals(wizard.getCurrentStep())) {
			WizardButtonOperation.renameNextBtnTxt("创建产品结构");
			WizardButtonOperation.enableNextBtn(false);
		} else if (REGDRAWING.equals(wizard.getCurrentStep())) {

			if (PartInfoPanel.isAssembly) {
				WizardButtonOperation.renameNextBtnTxt("下一步");
				WizardButtonOperation.enableNextBtn(true);
			} else {
				WizardButtonOperation.renameNextBtnTxt("检入图纸");
				WizardButtonOperation.enableNextBtn(true);
			}

		} else {
			WizardButtonOperation.renameNextBtnTxt("下一步");
			WizardButtonOperation.enableNextBtn(true);
		}

	}

	private void fullfilTreeContent() {
		DefaultMutableTreeNode top = (DefaultMutableTreeNode) summaryTree.getModel().getRoot();
		top.removeAllChildren();
		// 图纸信息
		DefaultMutableTreeNode drawingInfo = new DefaultMutableTreeNode("图纸信息");
		DefaultMutableTreeNode productCodeNode = new DefaultMutableTreeNode("产品代号：" + dom4jUtil.getProValue(element, CommonProperty.PROJECT_PRODUCTNO));
		DefaultMutableTreeNode projectNameNode = new DefaultMutableTreeNode("项目名称：" + dom4jUtil.getProValue(element, CommonProperty.PROJECT_NAME));
		DefaultMutableTreeNode zProjectNameNode = new DefaultMutableTreeNode("子项目名称：" + dom4jUtil.getProValue(element, CommonProperty.SUBPROJECT_NAME));
		DefaultMutableTreeNode belongToPahseNode = new DefaultMutableTreeNode("研制阶段：" + dom4jUtil.getProValue(element, CommonProperty.PROJECT_PHASE));
		File file = new File(dom4jUtil.getProValue(element, CommonProperty.FILEOBJECT_LOCALPATH));
		DefaultMutableTreeNode fileNameNode = new DefaultMutableTreeNode("文件名称：" + file.getName());
		DefaultMutableTreeNode drawingNoNode = new DefaultMutableTreeNode("图 号：" + dom4jUtil.getProValue(element, CommonProperty.DOC_DRAWINGCODE));
		DefaultMutableTreeNode drawingSizeNode = new DefaultMutableTreeNode("图 幅：" + dom4jUtil.getProValue(element, CommonProperty.DOC_DRAWINGSIZE));
		DefaultMutableTreeNode scaleNode = new DefaultMutableTreeNode("比 例：" + dom4jUtil.getProValue(element, CommonProperty.DOC_DRAWINGSCALE));
		DefaultMutableTreeNode pageNumberNode = new DefaultMutableTreeNode("第 几 页：" + dom4jUtil.getProValue(element, CommonProperty.FILEOBJECT_PAGENUMBER));
		DefaultMutableTreeNode totalNumberNode = new DefaultMutableTreeNode("共 几 页：" + dom4jUtil.getProValue(element, CommonProperty.DOC_TOTALNUMBER));
		DefaultMutableTreeNode createToolNode = new DefaultMutableTreeNode("创建工具：" + DrawingInfoPanel.authorToolF.getSelectedItem().toString());

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
		// 图档信息
		DefaultMutableTreeNode docPathParams = new DefaultMutableTreeNode("图档信息");
		DefaultMutableTreeNode docCodeNode = new DefaultMutableTreeNode("图样编码：" + dom4jUtil.getProValue(element, CommonProperty.DOC_CODE));
		DefaultMutableTreeNode docNumberNode = new DefaultMutableTreeNode("图样代号：" + DocumentInfoPanel.docNumber);
		DefaultMutableTreeNode docNameNode = new DefaultMutableTreeNode("图样名称：" + dom4jUtil.getProValue(element, CommonProperty.DOC_NAME));
		DefaultMutableTreeNode docComponetNode = new DefaultMutableTreeNode("组件号：" + DocumentInfoPanel.groupNumberF.getText());
		DefaultMutableTreeNode docSecretNode = new DefaultMutableTreeNode("文档密级：" + DocumentInfoPanel.secretLevelCmbox.getSelectedItem().toString());
		DefaultMutableTreeNode docFormatNode = new DefaultMutableTreeNode("文件格式：" + "dwg");
		DefaultMutableTreeNode docTypeNode = new DefaultMutableTreeNode("图样分类：" + DocumentInfoPanel.drawingTypeCmbox.getSelectedItem().toString());
		DefaultMutableTreeNode docSignNode = new DefaultMutableTreeNode("图样标记：" + DocumentInfoPanel.drawingSignCmbox.getSelectedItem().toString());

		docPathParams.add(docCodeNode);
		docPathParams.add(docNumberNode);
		docPathParams.add(docNameNode);
		docPathParams.add(docComponetNode);
		docPathParams.add(docSecretNode);
		docPathParams.add(docFormatNode);
		docPathParams.add(docTypeNode);
		docPathParams.add(docSignNode);
		// ----------------------------------------------end
		// 零部件信息
		DefaultMutableTreeNode partsPathParams = new DefaultMutableTreeNode("零部件信息");
		DefaultMutableTreeNode partCodeNode = new DefaultMutableTreeNode("零部件编码：" + dom4jUtil.getProValue(element, CommonProperty.PARTS_COD));
		DefaultMutableTreeNode partNumberNode = new DefaultMutableTreeNode("零部件代号：" + DocumentInfoPanel.docNumber);
		DefaultMutableTreeNode partNameNode = new DefaultMutableTreeNode("零部件名称：" + dom4jUtil.getProValue(element, CommonProperty.DOC_NAME));
		String typeName = "";
		String typeValue = "";
		if ("1".equals(element.element("assembly").getTextTrim())) {
			typeName = "部件类型";
			typeValue = PartInfoPanel.assTypeCmbox.getSelectedItem().toString();
		} else {
			typeName = "零件类型";
			typeValue = PartInfoPanel.cmpTypeCmbox.getSelectedItem().toString();
		}
		DefaultMutableTreeNode partTypeNode = new DefaultMutableTreeNode(typeName + "：" + typeValue);
		DefaultMutableTreeNode inOrOutNode = new DefaultMutableTreeNode("自制/外购：" + PartInfoPanel.makeOrBuyCmbox.getSelectedItem().toString());

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
