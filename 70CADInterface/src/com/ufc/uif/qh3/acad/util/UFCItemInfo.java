package com.ufc.uif.qh3.acad.util;

import java.util.List;

import com.ufc.uif.tcuacommunication.objects.WSObject;

/**
 * 
 * TODO ���ڴ洢item�������Ϣ
 * @author lijj
 * created on 2011-1-27����04:04:40
 */

public class UFCItemInfo {
  private String ItemID;
  
  private String ItemName;
  
  private String drawingNO;
  
  private boolean isExist;
  
  private boolean canBeRefresh; //ע���ܸ��µ�item��Ȼ����
  
  private List<UFCDataSetInfo> dataSets;
  
  private WSObject itemObject;

  public WSObject getItemObject() {
    return itemObject;
  }

  public void setItemObject(WSObject itemObject) {
    this.itemObject = itemObject;
  }

  public String getItemID() {
    return ItemID;
  }

  public void setItemID(String itemID) {
    ItemID = itemID;
  }

  public String getItemName() {
    return ItemName;
  }

  public void setItemName(String itemName) {
    ItemName = itemName;
  }

  public String getDrawingNO() {
    return drawingNO;
  }

  public void setDrawingNO(String drawingNO) {
    this.drawingNO = drawingNO;
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

  public List<UFCDataSetInfo> getDataSets() {
    return dataSets;
  }

  public void setDataSets(List<UFCDataSetInfo> dataSets) {
    this.dataSets = dataSets;
  }
}
