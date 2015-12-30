package com.ufc.uif.qh3.acad.util;

import javax.swing.JTextArea;

/**
 * <p>Title: UFCTextArea</p>
 * <p>Description: </p>
 * @author Bizn
 * @version $Id: UFCTextArea.java,v 1.1 2009/03/18 06:40:54 wangjf Exp $
 * @Created On 2009-1-4
 */
public class UFCTextArea extends JTextArea {
  private static final long serialVersionUID = 1L;
  private String displayName;
  private String property;
  private String cadName;
  private boolean required;
  public boolean isRequired() {
    return required;
  }

  public void setRequired(boolean required) {
    this.required = required;
  }

  public UFCTextArea()
  {
    super();
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getProperty() {
    return property;
  }

  public void setProperty(String property) {
    this.property = property;
  }

  public String getCadName() {
    return cadName;
  }

  public void setCadName(String cadName) {
    this.cadName = cadName;
  }

}
