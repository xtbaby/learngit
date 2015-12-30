package com.ufc.uif.qh3.acad.drawing;

import java.util.ArrayList;
import java.util.List;

public class SaveModel {

  private String displayName;
  public SaveModel() {
    this.lovlist=new ArrayList<String>();
  }
  public String getDisplayName() {
    return displayName;
  }
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }
  public String getCadName() {
    return cadName;
  }
  public void setCadName(String cadName) {
    this.cadName = cadName;
  }
  public String getProprety() {
    return proprety;
  }
  public void setProprety(String proprety) {
    this.proprety = proprety;
  }
  
  public boolean isRequired() {
    return required;
  }
  public void setRequired(boolean required) {
    this.required = required;
  }
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public ArrayList<String> getLovlist() {
    return lovlist;
  }
  public void setLov(String lov) {
    this.lovlist.add(lov);
  }

  
  private String cadName;
  private String proprety;
  private boolean required;
  private String type;
  private  ArrayList<String> lovlist;

}
