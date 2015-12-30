package com.ufc.uif.qh3.acad.util;

import com.ufc.uif.tcuacommunication.objects.WSObject;

/**
 * 
 * TODO 用于存储dataset的相关信息
 * @author lijj
 * created on 2011-1-27下午04:05:12
 */
public class UFCDataSetInfo {
  private String belongItemId;
  
  private String datasetName;
  
  private String datasetType;
  
  private String datasetRefName;
  
  private String localPath;
  
  private boolean isExist;
  
  private boolean canBeRefresh;
  
  private WSObject dataSetObject;

  public WSObject getDataSetObject() {
    return dataSetObject;
  }

  public void setDataSetObject(WSObject dataSetObject) {
    this.dataSetObject = dataSetObject;
  }

  public String getBelongItemId() {
    return belongItemId;
  }

  public void setBelongItemId(String belongItemId) {
    this.belongItemId = belongItemId;
  }

  public String getDatasetName() {
    return datasetName;
  }

  public void setDatasetName(String datasetName) {
    this.datasetName = datasetName;
  }

  public String getDatasetType() {
    return datasetType;
  }

  public void setDatasetType(String datasetType) {
    this.datasetType = datasetType;
  }

  public String getDatasetRefName() {
    return datasetRefName;
  }

  public void setDatasetRefName(String datasetRefName) {
    this.datasetRefName = datasetRefName;
  }

  public String getLocalPath() {
    return localPath;
  }

  public void setLocalPath(String localPath) {
    this.localPath = localPath;
  }

  public boolean isExist() {
    return isExist;
  }

  public void setExist(boolean isExist) {
    this.isExist = isExist;
  }

  public boolean isCanBeRefresh() {
    return canBeRefresh;
  }

  public void setCanBeRefresh(boolean canBeRefresh) {
    this.canBeRefresh = canBeRefresh;
  }
  
}
