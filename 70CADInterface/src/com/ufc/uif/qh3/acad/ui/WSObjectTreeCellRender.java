package com.ufc.uif.qh3.acad.ui;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.ufc.uif.qh3.acad.images.IconManager;
import com.ufc.uif.tcuacommunication.objects.WSObject;

/**
 * @author Liugz
 *
 */
public class WSObjectTreeCellRender extends DefaultTreeCellRenderer {

  Color bgColor = new Color(241,250,255);
  Font boldFont = null;
  Font plainFont = null;

  @Override
  public Component getTreeCellRendererComponent(JTree tree, Object value,
      boolean sel, boolean expanded, boolean leaf, int row,
      boolean hasFocus) {
    super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
        row, hasFocus);

    DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
    Object userObj = node.getUserObject();
    if(userObj instanceof String){
      if(userObj.toString().equalsIgnoreCase("Home")){
        setIcon(IconManager.getIcon("home.png"));
      }else if(userObj.toString().contains("搜索结果")){
        setIcon(IconManager.getIcon("query.png"));
      }else if(userObj.toString().contains("高级查询")){
        setIcon(IconManager.getIcon("tree_query.png"));
      }else if(userObj.toString().contains("打开")){
        setIcon(IconManager.getIcon("tree_tree.png"));
      }else{
        setIcon(IconManager.getIcon("red.png"));
      }
    }else if(userObj instanceof WSObject){
      WSObject nodeObj = (WSObject)node.getUserObject();
      String type = nodeObj.getType();
      if("Folder".equalsIgnoreCase(type)){
        setIcon(IconManager.getIcon("tree_folder.png"));
      }else if("Item".equalsIgnoreCase(type)){
        setIcon(IconManager.getIcon("item.png"));
      }else if("ItemRevision".equalsIgnoreCase(type)){
        setIcon(IconManager.getIcon("itemrev.png"));
      }else if("Document".equalsIgnoreCase(type)){
        setIcon(IconManager.getIcon("doc.png"));
      }else if("Newstuff Folder".equalsIgnoreCase(type)){
        setIcon(IconManager.getIcon("newfolder.png"));
      }else if("Mail Folder".equalsIgnoreCase(type)){
        setIcon(IconManager.getIcon("mailbox.png"));
      }else if("User_Inbox".equalsIgnoreCase(type)){
        setIcon(IconManager.getIcon("TaskInbox.png"));
      }else if("Part".equalsIgnoreCase(type)){
        setIcon(IconManager.getIcon("Part.png"));
      }else if("Part Revision".equalsIgnoreCase(type)){
        setIcon(IconManager.getIcon("Part_Revision.png"));
      }else if("Qh3_DesPart".equalsIgnoreCase(type)){
        setIcon(IconManager.getIcon("partCcmcPart.png"));
      }else if("Qh3_DesPartRevision".equalsIgnoreCase(type)){
        setIcon(IconManager.getIcon("partCcmcPartRevision.png"));
      }else if("Qh3_AutoCAD".equalsIgnoreCase(type)){
        setIcon(IconManager.getIcon("dataset.png"));
      }else if("PM8ACADDWG".equalsIgnoreCase(type)){
        setIcon(IconManager.getIcon("dataSetAutoCAD.png"));
      }else if("Qh3_PCB".equalsIgnoreCase(type)){
        setIcon(IconManager.getIcon("cae_16.png"));
      }else if("Qh3_SCH".equalsIgnoreCase(type)){
        setIcon(IconManager.getIcon("caj_16.png"));
      }else if("Qh3_DSN".equalsIgnoreCase(type)){
        setIcon(IconManager.getIcon("24.png"));
      }else if("Qh3_BRD".equalsIgnoreCase(type)){
        setIcon(IconManager.getIcon("30.png"));
      }else if("Qh3_AssDrawing".equalsIgnoreCase(type)){
        setIcon(IconManager.getIcon("16.png"));
      }else if("Qh3_AssDrawingRevision".equalsIgnoreCase(type)){
        setIcon(IconManager.getIcon("17.png"));
      }else if("PlanningDocument Revision".equalsIgnoreCase(type)){
        setIcon(IconManager.getIcon("PlanningDocRev16.png"));
      }else if("ConDesignerDoc".equalsIgnoreCase(type)){
        setIcon(IconManager.getIcon("ConDoc16.png"));
      }else if("ConDesignerDoc Revision".equalsIgnoreCase(type)){
        setIcon(IconManager.getIcon("ConDocRev16.png"));
      }else if("GenDesignerDoc".equalsIgnoreCase(type)){
        setIcon(IconManager.getIcon("GenDoc16.png"));
      }else if("GenDesignerDoc Revision".equalsIgnoreCase(type)){
        setIcon(IconManager.getIcon("GenDocRev16.png"));
      }else if("RDDocument".equalsIgnoreCase(type)){
        setIcon(IconManager.getIcon("RDDocument16.png"));
      }else if("RDDocument Revision".equalsIgnoreCase(type)){
        setIcon(IconManager.getIcon("RDDocumentRev16.png"));
      }else{
        setIcon(IconManager.getIcon("other.png"));
      }
    }else if(userObj instanceof JButton){
      JButton btn = (JButton)userObj;
      /*add(btn);
      setText(btn.getText());*/
      return btn;
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
