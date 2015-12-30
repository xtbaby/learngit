package com.ufc.uif.qh3.acad.drawing;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

//import com.ufc.uif.tccommunication.tc2007.objects.ClassifiClass;
import com.ufc.uif.qh3.acad.images.IconManager;
//import com.ufc.uif.tcuacommunication.objects.ClassifiClass;
import com.ufc.uif.tccommunicationimpl.object.ClassifiClass;

/**
 * @author Liugz
 *
 */
public class ClassifiTreeCellRender extends DefaultTreeCellRenderer {

  Color bgColor = new Color(241,250,255);
  Font boldFont = null;
  Font plainFont = null;
  @Override
  public Component getTreeCellRendererComponent(JTree tree, Object value,
      boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
    super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
    DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
    Object userObj = node.getUserObject();
    if(userObj instanceof String){
      setIcon(IconManager.getIcon("root.png"));
    }
    if(userObj instanceof ClassifiClass){
      ClassifiClass nodeObj = (ClassifiClass)node.getUserObject();
      String type = nodeObj.getType();
      if("Group".equalsIgnoreCase(type)){
        setIcon(IconManager.getIcon("group.png"));
      }
      else if("Class".equalsIgnoreCase(type)){
        setIcon(IconManager.getIcon("abstractclass.png"));
      }
      else if("StorageClass".equalsIgnoreCase(type)){
        setIcon(IconManager.getIcon("storageclass.png"));
      }
      else if("SubClass".equalsIgnoreCase(type)){
        setIcon(IconManager.getIcon("subclass.png"));
      }
      else{
        setIcon(IconManager.getIcon("classifying.png"));
      }
    }
    // 将当前选中的节点设为粗体字
    if(sel){
      if(null == boldFont)
        boldFont = getFont().deriveFont(Font.BOLD);
      setFont(boldFont);
    }
    else
      setFont(plainFont);
//    setBackground(Color.RED);
    setBackgroundNonSelectionColor(bgColor);
    return this;
  }


}
