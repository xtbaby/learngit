package com.ufc.uif.qh3.acad.util;

import com.sunking.swing.JDatePicker;

public class UFCDatePick extends JDatePicker
{
  private String displayName;
  private String property;
  private String cadName;
  private boolean required;
  private String objType;
  private String direction;
  private boolean canEdit;
  
  public boolean getCanEdit() {
    return canEdit;
  }
  
  public void setCanEdit(boolean canEdit) {
    this.canEdit = canEdit;
  }
  
  public String getDisplayName()
  {
    return displayName;
  }
  public void setDisplayName(String displayName)
  {
    this.displayName = displayName;
  }
  public String getProperty()
  {
    return property;
  }
  public void setProperty(String property)
  {
    this.property = property;
  }
  public String getCadName()
  {
    return cadName;
  }
  public void setCadName(String cadName)
  {
    this.cadName = cadName;
  }
  public boolean isRequired()
  {
    return required;
  }
  public void setRequired(boolean required)
  {
    this.required = required;
  }
  public String getObjType()
  {
    return objType;
  }
  public void setObjType(String objType)
  {
    this.objType = objType;
  }
  public String getDirection() {
    return direction;
  }
  public void setDirection(String direction) {
    this.direction = direction;
  }
}
