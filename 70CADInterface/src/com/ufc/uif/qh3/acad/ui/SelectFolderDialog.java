package com.ufc.uif.qh3.acad.ui;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.ufc.uif.qh3.acad.images.IconManager;
import com.ufc.uif.qh3.acad.operation.SaveDrawingOperation;
import com.ufc.uif.tccommunicationimpl.operation.TCObjectOperation;
import com.ufc.uif.tcuacommunication.objects.WSObject;
import com.ufc.uif.tcuacommunication.operation.ITCObjectOperation;
import com.ufc.uif.tcuacommunication.operation.exception.TCOperationException;
/**
 *
 * @author Administrator
 */
public class SelectFolderDialog extends javax.swing.JDialog {

    /** Creates new form SelectFolderDialog */
  
  private SaveDrawingOperation op;
  
    public SelectFolderDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
//        picPath=System.getProperty("user.dir")+"\\img\\";
        initComponents();
        selectedfolder=null;
        op = new SaveDrawingOperation();
        addListener();

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jFolderTree = new javax.swing.JTree();
        jButtonOK = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("选择保存文件夹");
        setAlwaysOnTop(true);
        setBackground(new java.awt.Color(226, 245, 252));
        setForeground(new java.awt.Color(226, 245, 252));
        setName("selectfolderdialog"); // NOI18N
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(226, 245, 252));
        jPanel1.setForeground(new java.awt.Color(226, 245, 252));

        jFolderTree.setAutoscrolls(true);
        jFolderTree.setName("folder_tree"); // NOI18N
        DefaultTreeSelectionModel model=new DefaultTreeSelectionModel();
        model.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        jFolderTree.setSelectionModel(model);
        jScrollPane1.setViewportView(jFolderTree);

        jButtonOK.setText("确定");
        jButtonOK.setName("btnOK"); // NOI18N
        jButtonOK.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOKMouseClicked(evt);
            }
        });

        jButtonCancel.setText("取消");
        jButtonCancel.setName("btnCancel"); // NOI18N
        jButtonCancel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCancelMouseClicked(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
                        .addContainerGap())
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                        .add(jButtonOK, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 74, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jButtonCancel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 77, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(21, 21, 21))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 368, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jButtonCancel)
                    .add(jButtonOK))
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jFolderTree.setCellRenderer(new DefaultTreeCellRenderer()
        {
      private static final long serialVersionUID = 1L;

      public Component getTreeCellRendererComponent(JTree jtree, Object obj, boolean flag, boolean flag1, boolean flag2, int i, boolean flag3)
            {
        super.getTreeCellRendererComponent(jtree, obj, flag, flag1, flag2, i, flag3);

        DefaultMutableTreeNode node = (DefaultMutableTreeNode)obj;
        if(node.getUserObject() instanceof String)
        {
          setIcon(IconManager.getIcon("tree_query.png"));
        }
        
         if(node.getUserObject() instanceof WSObject)
         {
           
          
            String type= getNodeType(node);
          
           if (type.equalsIgnoreCase("Item"))
           {
             setIcon(IconManager.getIcon("tree_part.png"));
           }
          // else if (type.endsWith("Folder")) //感觉需要改
          // {
           else if (node.isRoot()) {
                setIcon(IconManager.getIcon("tree_home.png"));
              } else if(getNodeType(node) .equalsIgnoreCase( "Newstuff Folder")){
                setIcon(IconManager.getIcon("Newstuff_Folder.png"));
              }else if (getNodeType(node) .equalsIgnoreCase( "Mail Folder")){
                
                setIcon(IconManager.getIcon("Mail_Folder.png"));
              }else if (getNodeType(node) .equalsIgnoreCase( "TaskInbox")){
                setIcon(IconManager.getIcon("TaskInbox.png"));
              }else  if (getNodeType(node) .equalsIgnoreCase( "Folder")){
                setIcon(IconManager.getIcon("Folder.png"));
              }
          // }
         else if (type.equalsIgnoreCase("ItemRevision"))
        
          {
            setIcon(IconManager.getIcon("tree_partrev.png"));
          }
          else if(type.equalsIgnoreCase("Dataset"))
          {
            setIcon(IconManager.getIcon("tree_dwg.png"));
          }
        
        
            }
         return this;
        }});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnOKMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOKMouseClicked
        // TODO add your handling code here:
      DefaultMutableTreeNode node = (DefaultMutableTreeNode)jFolderTree.getLastSelectedPathComponent();
      //selectedfolder=(Folder) node.getUserObject();
      selectedfolder= (WSObject) node.getUserObject();
      System.out.println("  selectedfolder="+  selectedfolder);
        dispose();
    }//GEN-LAST:event_btnOKMouseClicked

    private void btnCancelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCancelMouseClicked
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnCancelMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonOK;
    private javax.swing.JTree jFolderTree;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private String picPath;
    private javax.swing.tree.DefaultMutableTreeNode rootnode;
  //fandy private DataManagementService dmService;
  //fandy private Folder selectedfolder;
    private WSObject selectedfolder;
    
    // End of variables declaration//GEN-END:variables

  private WSObject home;

  public void setHome(WSObject home) {
    this.home = home;
  }
  public boolean initTree(WSObject home) {
      //dmService = DataManagementService.getService(TcSession.getConnection());
    ITCObjectOperation op = new TCObjectOperation();
    WSObject[] newObjs = null;
    try {
      newObjs = op.refreshTCObjects(new WSObject[]{home});
    } catch (TCOperationException e) {
      e.printStackTrace();
    }
    if(null == newObjs){
      JOptionPane.showMessageDialog(this, "更新 Home 文件夹时发生错误，请重试！", "提示", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    WSObject newHome = (WSObject)newObjs[0];
    this.home=newHome;
      //dmService.getProperties(new ModelObject[]{newHome}, new String[]{"object_name"});
        rootnode = new javax.swing.tree.DefaultMutableTreeNode(newHome);
        jFolderTree.setModel(new javax.swing.tree.DefaultTreeModel(rootnode));
    WSObject[] children = null;
    try {
      children = op.listContentsofFolder(newHome, false);
    } catch (TCOperationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    for(int i=0;i<children.length;i++){
      WSObject obj=children[i];
      //  dmService.getProperties(new ModelObject[]{obj}, new String[]{"object_name"});
          javax.swing.tree.DefaultMutableTreeNode childnode = new javax.swing.tree.DefaultMutableTreeNode(obj);
      rootnode.add(childnode);
    }
    jFolderTree.expandPath(new TreePath(jFolderTree.getModel().getRoot()));
    this.pack();
    return true;
  }
  public void addListener()
  {
    jFolderTree.addMouseListener(new MouseAdapter() {
         public void mousePressed(MouseEvent e) {
             int selRow = jFolderTree.getRowForLocation(e.getX(), e.getY());
             TreePath selPath = jFolderTree.getPathForLocation(e.getX(), e.getY());
             if(selRow != -1) {
                 if(e.getClickCount() == 2) {
                     DefaultMutableTreeNode openNode = (DefaultMutableTreeNode) selPath.getLastPathComponent();

                     if( getNodeType(openNode).endsWith("Folder"))
                     {
                       WSObject folder= (WSObject)openNode.getUserObject();

                       openNode.removeAllChildren();

                       try
                       {
                         WSObject[] children=op.listContentsofFolder(folder, false);
                         if(children!=null){
                           for(int i=0;i<children.length;i++){
                             WSObject obj=children[i];
                            // dmService.getProperties(new ModelObject[]{obj}, new String[]{"object_name"});
                             javax.swing.tree.DefaultMutableTreeNode childnode = new javax.swing.tree.DefaultMutableTreeNode(obj);
                             openNode.add(childnode);
                           }
                         }
                       }
                       catch(Exception err)
                       {
                         err.printStackTrace();
                       }
                       jFolderTree.expandPath(selPath);
                     }
                 }
                 }
             }
    });
    jFolderTree.addTreeWillExpandListener(new javax.swing.event.TreeWillExpandListener()
    {
            public void treeWillCollapse(javax.swing.event.TreeExpansionEvent evt)throws javax.swing.tree.ExpandVetoException
            {
            }
            public void treeWillExpand(javax.swing.event.TreeExpansionEvent evt)throws javax.swing.tree.ExpandVetoException
            {
              DefaultMutableTreeNode openNode = (DefaultMutableTreeNode)evt.getPath().getLastPathComponent();

              if( "Folder".equalsIgnoreCase(getNodeType(openNode)))
              {
                WSObject folder= (WSObject)openNode.getUserObject();

                openNode.removeAllChildren();

                try
                {
                  WSObject[] children=op.listContentsofFolder(folder, false);
                  if (children!=null) {
                    for(int i=0;i<children.length;i++){
                      WSObject obj=children[i];
                      //dmService.getProperties(new ModelObject[]{obj}, new String[]{"object_name"});
                      javax.swing.tree.DefaultMutableTreeNode childnode = new javax.swing.tree.DefaultMutableTreeNode(obj);
                      openNode.add(childnode);
                    }
            }
                }
                catch(Exception e)
                {
                  e.printStackTrace();
                }
              }
            }
        });

  }

  public WSObject getSelectedfolder() {
    return selectedfolder;
  }
public String  getNodeType(DefaultMutableTreeNode node)
{
  WSObject wsObject=(WSObject)node.getUserObject();
    String type=  wsObject.getType();
    //System.out.println(wsObject +"="+type);
    return type;
}
}