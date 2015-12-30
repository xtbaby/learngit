package com.ufc.uif.qh3.acad.tools;

import javax.swing.JButton;

import org.netbeans.api.wizard.WizardDisplayer;
import org.netbeans.api.wizard.displayer.NavButtonManager;
import org.netbeans.api.wizard.displayer.WizardDisplayerImpl;

public class WizardButtonOperation {
	public static void renameNextBtnTxt(String paramString) {
		WizardDisplayerImpl wizardDisplayerImpl = (WizardDisplayerImpl) WizardDisplayer.defaultInstance;
		NavButtonManager navButtonManager = wizardDisplayerImpl.getButtonManager();
		JButton btn = navButtonManager.getNext();
		btn.setText(paramString);
	}

	public static void enableNextBtn(boolean paramBoolean) {
		WizardDisplayerImpl wizardDisplayerImpl = (WizardDisplayerImpl) WizardDisplayer.defaultInstance;
		NavButtonManager navButtonManager = wizardDisplayerImpl.getButtonManager();
		navButtonManager.setNextBtnEnabled(paramBoolean);
	}

	public static void updateBtn() {
		WizardDisplayerImpl wizardDisplayerImpl = (WizardDisplayerImpl) WizardDisplayer.defaultInstance;
		NavButtonManager navButtonManager = wizardDisplayerImpl.getButtonManager();
		navButtonManager.updateButtons();
	}

	public static void enablePrevBtn(boolean paramBoolean) {
		WizardDisplayerImpl wizardDisplayerImpl = (WizardDisplayerImpl) WizardDisplayer.defaultInstance;
		NavButtonManager navButtonManager = wizardDisplayerImpl.getButtonManager();
		navButtonManager.setPreBtnEnabled(paramBoolean);
	}

	public static void enableFinishBtn(boolean paramBoolean) {
		WizardDisplayerImpl wizardDisplayerImpl = (WizardDisplayerImpl) WizardDisplayer.defaultInstance;
		NavButtonManager navButtonManager = wizardDisplayerImpl.getButtonManager();
		JButton btn = navButtonManager.getFinish();
		btn.setEnabled(paramBoolean);
	}

	public static void enableCancelBtn(boolean paramBoolean) {
		WizardDisplayerImpl wizardDisplayerImpl = (WizardDisplayerImpl) WizardDisplayer.defaultInstance;
		NavButtonManager navButtonManager = wizardDisplayerImpl.getButtonManager();
		JButton btn = navButtonManager.getCancel();
		btn.setEnabled(paramBoolean);
	}

	public static void renameFinishBtnTxt(String paramString) {
		WizardDisplayerImpl wizardDisplayerImpl = (WizardDisplayerImpl) WizardDisplayer.defaultInstance;
		NavButtonManager navButtonManager = wizardDisplayerImpl.getButtonManager();
		JButton btn = navButtonManager.getFinish();
		btn.setText(paramString);
	}

	public static void renameCancelBtnTxt(String paramString) {
		WizardDisplayerImpl wizardDisplayerImpl = (WizardDisplayerImpl) WizardDisplayer.defaultInstance;
		NavButtonManager navButtonManager = wizardDisplayerImpl.getButtonManager();
		JButton btn = navButtonManager.getCancel();
		btn.setText(paramString);
	}
}
