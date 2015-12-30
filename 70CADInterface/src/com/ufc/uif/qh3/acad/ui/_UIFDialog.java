package com.ufc.uif.qh3.acad.ui;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.dom4j.Element;

import com.ufc.uif.base_adaptor.AdaptorWriter;
import com.ufc.uif.base_adaptor.busioperator.BusiOperationException;
import com.ufc.uif.base_adaptor.busioperator.IBusiOperator;
import com.ufc.uif.qh3.acad.images.IconManager;
import com.ufc.uif.tcuacommunication.operation.OperationManagerFactory;

/**
 * <p>
 * Title: _UIFDialog
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author Zhongning.B
 * @version $Id: _UIFDialog.java,v 1.2 2010/11/11 07:42:51 fandy Exp $
 * @Created On Aug 26, 2008
 */
public abstract class _UIFDialog extends JFrame implements IBusiOperator {

	/**
   *
   */
	private static final long serialVersionUID = -244179442381364805L;
	protected Element requestBody;
	protected Element responseBody;
	protected OperationManagerFactory factory;

	protected AdaptorWriter out;

	public _UIFDialog(OperationManagerFactory factory) {

		initConfig(); // 把配置文件读入内存lijj

		initComponents();
		initListener();
		try {
			// javax.swing.plaf.nimbus.NimbusLookAndFeel
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			// com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel
			// javax.swing.plaf.nimbus.NimbusLookAndFeel
			SwingUtilities.updateComponentTreeUI(this);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		this.factory = factory;
	}

	public Image getIcon(String s) {
		return IconManager.getIcon(s).getImage();
	}

	public void doOperation(Element requestBody, AdaptorWriter out) throws BusiOperationException {
		this.out = out;
		this.requestBody = requestBody;

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

		setVisible(true);
		System.out.println("after setvisible");

	}

	public abstract void initComponents();

	public abstract void initConfig();

	public abstract void initListener();

}
