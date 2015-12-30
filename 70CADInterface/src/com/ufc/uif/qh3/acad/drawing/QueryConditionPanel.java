package com.ufc.uif.qh3.acad.drawing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.dom4j.Element;

import com.ufc.uif.qh3.acad.operation.ParseRequestXML;
import com.ufc.uif.qh3.acad.util.UFCComboBox;
import com.ufc.uif.qh3.acad.util.UFCTextArea;
import com.ufc.uif.qh3.acad.util.UFCTextField;

//import com.ufc.uif.adaptor.acad.n2.operation.ParseRequestXML;
//import com.ufc.uif.model.Assembly;
//import com.ufc.uif.model.SaveModel;
//import com.ufc.uif.model.UFCComboBox;
//import com.ufc.uif.model.UFCTextArea;
//import com.ufc.uif.model.UFCTextField;
//import com.ufc.uif.tccommunication.operation.OperationManagerFactory;
//import com.ufc.uif.tccommunication.tc2008.objects.WSObject;
//import com.ufc.uif.tccommunication.tc2008.operation.TCObjectOperation;

public class QueryConditionPanel extends JPanel{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  public QueryConditionPanel(String filename) {
  
    initComponents();
    initDialog(filename);
   }
   
    private void initDialog(String filename){
      ArrayList<SaveModel> allComponents = ParseRequestXML.parseQuerycondition(filename);
      
      //根据配置文件生成控件
      for(int i = 0; i < allComponents.size(); i++)
      {
        JLabel label = new JLabel();
        JComponent jcom = null;
        
        SaveModel sc = allComponents.get(i);
        
        label.setText(sc.getDisplayName() + "：");
        
        if(sc.getType().equals("TEXT"))
        {
          jcom = new UFCTextField();
          
          ((UFCTextField)jcom).setDisplayName(sc.getDisplayName());
          ((UFCTextField)jcom).setProperty(sc.getProprety());
          ((UFCTextField)jcom).setRequired(sc.isRequired());
          ((UFCTextField)jcom).setCadName(sc.getCadName());
          
        }
        else if(sc.getType().equals("LOV"))
        {
          jcom = new UFCComboBox();
          
          ((UFCComboBox)jcom).setDisplayName(sc.getDisplayName());
          ((UFCComboBox)jcom).setProperty(sc.getProprety());
          ((UFCComboBox)jcom).setRequired(sc.isRequired());
          ((UFCComboBox)jcom).setCadName(sc.getCadName());
          
          for(int x = 0; x < sc.getLovlist().size(); x++)
          {
            ((UFCComboBox)jcom).addItem(sc.getLovlist().get(x));
          }
        }else
          if(sc.getType().equals("TEXTArea")){
            
            jcom=new UFCTextArea();
            
            ((UFCTextArea)jcom).setDisplayName(sc.getDisplayName());
            ((UFCTextArea)jcom).setProperty(sc.getProprety());
            ((UFCTextArea)jcom).setRequired(sc.isRequired());
            ((UFCTextArea)jcom).setCadName(sc.getCadName());
            
            
            //jcom.isOpaque();
          }
        
        label.setBounds(new Rectangle(10, (i+10)+(i*15), 90, 22));
        jcom.setBounds(new Rectangle(100, (i+10)+(i*15), 120, 22));
        add(label);
        add(jcom);
      }
 }      
  
  private void initComponents() {
    
    setBackground(new java.awt.Color(226, 245, 252));
    setBorder(javax.swing.BorderFactory.createTitledBorder("搜索条件："));
    org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(0, 193, Short.MAX_VALUE)//493
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
        .add(0, 392, Short.MAX_VALUE)
    );
  }

}