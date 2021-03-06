/**
 *
 */
package com.ufc.uif.qh3.acad.ui;

import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JDialog;
import javax.swing.JFrame;

import com.ufc.uif.qh3.acad.images.IconManager;

/**
 * @author Liugz
 * 
 */
/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

/*
 * WaitDialog.java
 * 
 * Created on 2009-3-12, 9:38:14
 */
public class WaitDialog extends javax.swing.JDialog {

  /** Creates new form WaitDialog */
  public WaitDialog() {
    initComponents();
    pack();
  }

  public WaitDialog(JFrame frame, boolean modal) {
    super(frame, modal);
    initComponents();
    Dimension dlgSize = this.getPreferredSize();
    int dlgX = (frame.getWidth() - dlgSize.width) / 2;
    int dlgY = (frame.getHeight() - dlgSize.height) / 2;
    Point frameLoc = frame.getLocation();
    setLocation((frameLoc.x + dlgX), (frameLoc.y + dlgY));

    setVisible(true);
    pack();
  }

  public WaitDialog(JDialog frame, boolean modal) {
    super(frame, modal);
    initComponents();
    Dimension dlgSize = this.getPreferredSize();
    int dlgX = (frame.getWidth() - dlgSize.width) / 2;
    int dlgY = (frame.getHeight() - dlgSize.height) / 2;
    Point frameLoc = frame.getLocation();
    setLocation((frameLoc.x + dlgX), (frameLoc.y + dlgY));

    setVisible(true);
    pack();
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">
  private void initComponents() {

    jPanel1 = new javax.swing.JPanel();
    jPanel2 = new javax.swing.JPanel();
    jLabel1 = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();

    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    setAlwaysOnTop(true);
    setBackground(new java.awt.Color(232, 242, 254));
    setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
    setResizable(false);
    setUndecorated(true);

    jPanel1.setBackground(new java.awt.Color(232, 242, 254));
    jPanel1.setName("jPanel1"); // NOI18N

    jPanel2.setBackground(new java.awt.Color(241, 250, 255));
    jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 255), 2));
    jPanel2.setName("jPanel2"); // NOI18N

    jLabel1.setFont(new java.awt.Font("黑体", 1, 14));
    jLabel1.setText("正在载入内容，请稍候……");
    jLabel1.setName("jLabel1"); // NOI18N

    jLabel2.setIcon(IconManager.getIcon("loading.gif")); // NOI18N
    jLabel2.setName("jLabel2"); // NOI18N

    org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
        jPanel2Layout.createSequentialGroup().add(22, 22, 22).add(jLabel2).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jLabel1).addContainerGap(27, Short.MAX_VALUE)));
    jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
        jPanel2Layout.createSequentialGroup().addContainerGap().add(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.CENTER).add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24,
                org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel2)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

    org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
        jPanel1Layout.createSequentialGroup().addContainerGap().add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
            org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
    jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
        jPanel1Layout.createSequentialGroup().addContainerGap().add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
            org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

    org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE));
    layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE));

  }// </editor-fold>

  /**
   * @param args
   *            the command line arguments
   */
  public static void main(String args[]) {
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new WaitDialog().setVisible(true);
      }
    });
  }

  public void setMessage(String message) {
    jLabel1.setText(message);
    jLabel1.validate();
  }

  // Variables declaration - do not modify
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  // End of variables declaration

}
