package com.ufc.uif.qh3.acad.ui.wizard;

import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTree;

import org.netbeans.spi.wizard.WizardPage;

public class SummaryPanel extends WizardPage {


	private static final long serialVersionUID = 1L;

	public SummaryPanel(JTree tree) {

		setLayout(new BorderLayout());
		add(new JScrollPane(tree), BorderLayout.CENTER);

	}
}
