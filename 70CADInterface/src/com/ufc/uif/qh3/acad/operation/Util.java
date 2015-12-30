package com.ufc.uif.qh3.acad.operation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ufc.uif.tccommunicationimpl.operation.TCObjOperation;
import com.ufc.uif.tccommunicationimpl.operation.TCObjectOperation;
import com.ufc.uif.tccommunicationimpl.operation.connecttc.TCUtil;
import com.ufc.uif.tcuacommunication.objects.WSObject;
import com.ufc.uif.tcuacommunication.operation.ITCObjectOperation;
import com.ufc.uif.tcuacommunication.operation.ITCTCObjOperation;
import com.ufc.uif.tcuacommunication.operation.exception.TCOperationException;

/*import com.teamcenter.soa.client.model.ModelObject;
 import com.ufc.uif.tccommunication.operation.exception.TCOperationException;
 import com.ufc.uif.tccommunication.tc2007.objects.WSObject;
 import com.ufc.uif.tccommunication.tc2007.operation.ObjectOperation;
 import com.ufc.uif.tccommunication.tc2007.operation.TCObjectOperation;
 import com.ufc.uif.tccommunication.tc2007.operation.TCUtil;
 */

public class Util {

  public static boolean convertFinish = false;
  public static boolean convertStart = false;
  public static boolean convertError = false;
  public static String operator = "";
  public static String rev_id = "a";

  /**
   * ��NULLֵתΪ���ַ�����ȥ���ַ���ǰ��Ŀո�
   * 
   * @author Liugz
   * @create on 2009-2-13
   * @param value
   * @return
   */
  public static String convertNull(Object value) {
    return (null == value) ? "" : value.toString().trim();
  }

  /**
   * ��2ά�ַ�����תΪMap����
   * 
   * @author Liugz
   * @create on 2009-2-13
   * @param str
   * @return
   */
  public static Map<String, String> stringArray2Map(String[][] str) {
    Map<String, String> map = new HashMap<String, String>();
    if (null == str || str.length == 0) {
      return map;
    }
    for (int i = 0; i < str.length; i++) {
      map.put(str[i][1], str[i][0]);
    }
    return map;
  }

  /**
   * ��ӡʱ��
   * 
   * @author Liugz
   * @create on 2009-2-25 This project for CAD_HJ
   */
  public static void printTime() {
    System.out.println("---------------------------------------------------------");
    System.out.println("\tCurrent Time : " + new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss").format(new Date()));
    System.out.println("---------------------------------------------------------");
  }

  public static void printTime(String str) {
    System.out.println("---------------------------------------------------------");
    System.out.println("\t" + str + " : " + new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss").format(new Date()));
    System.out.println("---------------------------------------------------------");
  }

  /**
   * �Ƿ�Ϊ�ɹ��� �ɹ�������Ϊ��10λ����1��ͷ
   * 
   * @author Liugz
   * @create on 2009-2-25 This project for CAD_Concrete
   * @return
   * @throws TCOperationException
   */
  public static boolean isPurchProd(String itemid) {
    /*
     * if(id.length() == 10){ return id.charAt(0) == '1'; } else return
     * false;
     */
    try {
      int[] itemIdLens = zlItemIdLength("CommericalPart");
      if (null != itemIdLens && itemIdLens.length != 0) {
        for (int i = 0; i < itemIdLens.length; i++) {
          int len = itemIdLens[i];
          if (itemid.length() == len) {
            return true;
          }
        }
        return false;
      } else
        return true;
    } catch (TCOperationException e) {
      // throw new TCOperationException(e.getMessage());
      return true;
    }
  }

  /**
   * �Ƿ�Ϊ�ܼ����ͷ ����Ϊ��17λ��56λ��93��567λ��989
   * 
   * @author Liugz
   * @create on 2009-2-25 This project for CAD_Concrete
   * @return
   */
  public static boolean isGeneralPart(String itemid) {
    try {
      int[] itemIdLens = zlItemIdLength("Item");
      if (null != itemIdLens && itemIdLens.length != 0) {
        for (int i = 0; i < itemIdLens.length; i++) {
          int len = itemIdLens[i];
          if (itemid.length() == len) {
            String _56 = itemid.substring(4, 6);
            if (_56.equals("93")) {
              return true;
            }
            String _567 = itemid.substring(4, 7);
            if (_567.equals("989")) {
              return true;
            }
          }
        }
        return false;
      } else
        return true;
    } catch (TCOperationException e) {
      // throw new TCOperationException(e.getMessage());
      return true;
    }
  }

  /**
   * �Ƿ�Ϊ���Ƽ�����
   * 
   * @author Liugz
   * @create on 2009-6-30 This project for CAD_Concrete
   * @param itemid
   * @return
   * @throws TCOperationException
   */
  public static boolean isSelfPart(String itemid) {
    try {
      int[] itemIdLens = zlItemIdLength("Item");
      if (null != itemIdLens && itemIdLens.length != 0) {
        for (int i = 0; i < itemIdLens.length; i++) {
          int len = itemIdLens[i];
          if (itemid.length() == len) {
            return true;
          }
        }
        return false;
      } else
        return true;
    } catch (TCOperationException e) {
      // throw new TCOperationException(e.getMessage());
      return true;
    }
  }

  /**
   * �������������ַ���������С���㣩����ȡָ����λ��
   * 
   * @author Liugz
   * @create on 2009-8-6 This project for CAD_Concrete
   * @param originalNumber
   * @param reserveDigit
   *            -- Ҫ��ȡС������λ��
   * @return
   */
  public static String cutNumber(String originalNumber, int reserveDigit) {
    String newValue = "";
    if (originalNumber.indexOf(".") != -1 && originalNumber.substring(originalNumber.indexOf(".") + 1).length() > 3) {
      newValue = originalNumber.substring(0, originalNumber.indexOf(".") + 1 + reserveDigit);
    } else
      newValue = originalNumber;

    return newValue;
  }

  /**
   * @author Liugz
   * @create on 2008-12-17 This project for ZoomLion
   * @param zlItemAttribs
   */
  public static void setEmptyValue(Map<String, String> zlItemAttribs) {
    // �Ƴ�Map�еĿ�ֵ��
    for (Iterator<String> itor = zlItemAttribs.keySet().iterator(); itor.hasNext();) {
      String key = (String) itor.next();
      Object value = zlItemAttribs.get(key);
      if (null == value || "".equals(value.toString().trim())) {
        // itor.remove();
        // zlItemAttribs.remove(key);
        zlItemAttribs.put(key, " ");
      }
    }
  }

  /**
   * @author Liugz
   * @throws TCOperationException
   * @create on 2009-7-3 This project for CAD_Concrete
   * @throws TCOperationException
   */
  private static int[] zlItemIdLength(String itemType) throws TCOperationException {
    try {
      String[] lenInfos = TCUtil.getPreferenceValues("zl_Item_length", "site");
      int[] itemIdLen = null;
      if (null != lenInfos && lenInfos.length != 0) {
        for (int i = 0; i < lenInfos.length; i++) {
          String itemLen = lenInfos[i];
          String itemType2 = itemLen.substring(0, itemLen.indexOf(","));
          if (itemType.equalsIgnoreCase(itemType2)) {
            String[] lens = itemLen.substring(itemLen.indexOf(",") + 1).split(",");
            if (null != lens && lens.length != 0) {
              itemIdLen = new int[lens.length];
              for (int j = 0; j < lens.length; j++) {
                itemIdLen[j] = Integer.parseInt(lens[j]);
              }
            }
            break;
          }
        }
        return itemIdLen;
      } else
        throw new TCOperationException("��ȡ��ѡ�zl_Item_length��ʱ����������ѡ��������ô���");
    } catch (TCOperationException e) {
      throw new TCOperationException("��ȡ��ѡ�zl_Item_length��ʱ�������󣬻��߸���ѡ�����ô���");
    }
  }

  /**
   * ��DWGתΪPDF�ļ�
   * 
   * @author Liugz
   * @create on 2009-7-1 This project for CAD_Concrete
   * @return
   * @throws TCOperationException
   */
  public static String convertDWG2PDF(String dwgFile, String dwgFileName) throws TCOperationException {
    // System.out.println("�ɵ�PDF�ļ����ƣ�"+dwgFileName);
    // dwgFileName = dwgFile.substring(dwgFile.lastIndexOf("\\") + 1,
    // dwgFile.lastIndexOf("."));
    String tempDirStr = System.getenv("UFCROOT") + "\\temp\\PDF\\";
    // System.out.println("�µ�PDF�ļ����ƣ�"+dwgFileName);
    File tempDir = new File(tempDirStr);
    if (!tempDir.exists()) {
      tempDir.mkdir();
    }

    /*
     * String ext_name_add=ParseRequestXML.getExt_name_add(); if(
     * !ext_name_add.startsWith(".")){ ext_name_add="."+ext_name_add; }
     */

    try {
      String pdfFile = tempDirStr + dwgFileName + ".pdf";
      String convertPath = "";
      // System.out.println("��ʼת����");
      String[] path = TCUtil.getPreferenceValues("UFC_DWG2PDF_PATH", "site");
      if (null != path && path.length == 1) {
        convertPath = path[0];
      } else {
        throw new TCOperationException("ת������·�����ô�������TC��ѡ��");
      }
      File convertDir = new File(convertPath);
      // String convertTool=ParseRequestXML.getconvertTool();
      File exeFile = new File(convertDir, "dwg2pdf.exe");
      // File exeFile = new File(convertDir, "convertTool");
      if (!exeFile.exists()) {
        throw new TCOperationException("ת������·�����ô�������TC��ѡ��");
      }
      File batFile = new File(convertDir, "convert.bat");
      if (batFile.exists()) {
        batFile.delete();
      }
      FileWriter fileWriter = new FileWriter(batFile);
      StringBuffer sb = new StringBuffer();
      sb.append("dwg2pdf.exe " + dwgFile + "$" + pdfFile);
      // sb.append(convertTool+"  " + dwgFile + "$" + pdfFile );

      fileWriter.write(sb.toString());
      fileWriter.flush();
      fileWriter.close();
      Process proc = Runtime.getRuntime().exec("cmd /c" + "convert.bat", null, convertDir);
      byte[] inputBytes = new byte[1024 * 10];
      StringBuffer tempStr = new StringBuffer();
      InputStream input = proc.getInputStream();
      int len = input.read(inputBytes);
      while (len != -1) {
        len = input.read(inputBytes);
        tempStr.append(new String(inputBytes));
      }

      // ScanTasklist scanTask = new ScanTasklist();
      // Timer timer = new Timer("ScanTask", true);
      // Timer timer = new Timer();
      // timer.schedule(scanTask, 100, 1000);
      /*
       * boolean isExsisted = false; while(!isExsisted){ if(new
       * File(pdfFile).exists()){ isExsisted = true; } }
       */
      /*
       * boolean goon = false; while(!goon){ Thread.sleep(2000);
       * scanTask.run(); if(scanTask.isConvertError()) throw new
       * TCOperationException("��ȡWindows�����б��Ƿ��������޷���ȡ�����б�"); goon =
       * scanTask.isConvertStart() && scanTask.isConvertFinish(); }
       */
      proc.destroy();
      // if(convertError)
      // throw new TCOperationException("��ȡWindows�����б��Ƿ��������޷���ȡ�����б�");

      File pdf = new File(pdfFile);
      if (pdf.exists()) {
        return pdfFile;
      } else
        return null;

    } catch (IOException e) {
      throw new TCOperationException(e.getMessage());
    } catch (TCOperationException e) {
      throw new TCOperationException(e.getMessage());
    }
  }

  /**
   * ɾ����ʱĿ¼�е�ͼֽ�ļ�
   * 
   * @author Liugz
   * @create on 2009-7-20 This project for CAD_Concrete
   */
  public static void deleteTempFile(String dwgFilePath, WSObject itemObj) {
    ITCTCObjOperation tcop = new TCObjOperation();
    WSObject revObj;
    try {
      revObj = tcop.getItemRevision(itemObj, "Last");
    } catch (TCOperationException e) {
      // outputStatus("δ��ɾ����ʱĿ¼�����ֹ�ɾ��");
      return;
    }
    String dwgPath = dwgFilePath.substring(0, dwgFilePath.lastIndexOf("\\"));
    String user_id = TCUtil.getUserId();
    String item_id = revObj.getId();
    String rev_id = revObj.getRevision();
    String tempDir = System.getenv("UFCROOT") + "\\Temp\\" + user_id + "_" + item_id + "_" + rev_id;
    // �ж�ͼֽ�ǲ�������ʱĿ¼��
    if (dwgPath.equalsIgnoreCase(tempDir)) {
      deleteFile(new File(tempDir));
    }

  }

  private static void deleteFile(File file) {
    if (file.isDirectory()) {
      File[] subFiles = file.listFiles();
      for (int i = 0; i < subFiles.length; i++) {
        deleteFile(subFiles[i]);
      }
    } else {
      file.delete();
    }
  }

  public static void main(String[] args) throws ParseException {
    /*
     * String[] testValue = {"1111", "0022", "0.001", "22.1200", "11200",
     * "000.11200", "sdf0.11200"}; for (int i = 0; i < testValue.length;
     * i++) { String value = testValue[i]; if(value.matches("\\d+\\.\\d+")){
     * System.out.println("\t" + value + " == " ); float f =
     * Float.parseFloat(value); System.out.println("\t CONVERT FLOAT == " +
     * f); } }
     * 
     * double myNumber = -1234.56; double myNumber2 = 1111111234.56545455;
     * double myNumber3 = 121234.00596; double myNumber4 = 4.56000; double
     * myNumber5 = 0.5609; String num = "11123.0098"; String num2 =
     * "00011123.9462"; String num3 = "11123.98"; DecimalFormat format = new
     * DecimalFormat("#0.###"); System.out.println("\t" +
     * format.format(myNumber)); System.out.println("\t" +
     * format.format(myNumber2)); System.out.println("\t" +
     * format.format(myNumber3)); System.out.println("\t" +
     * format.format(myNumber4)); System.out.println("\t" +
     * format.format(myNumber5)); System.out.println("\t" +
     * format.format(format.parse(num).doubleValue()));
     * System.out.println("\t" +
     * format.format(format.parse(num2).doubleValue()));
     * System.out.println("\t" +
     * format.format(format.parse(num3).doubleValue()));
     */

    System.out.println(countChar("jsf_sf_bb", "_".charAt(0)));
  }

  /**
   * �������е����ݼ�������ɾ�������ݼ����������е�DWGͼֽ
   * 
   * @author xukun
   * @param dataset���ݼ�����
   * @throws TCOperationException
   * @date 2009.09.21
   */
  public static String deleteDWG(WSObject dataset) throws TCOperationException {
    String filename = "";
    ITCObjectOperation op = new TCObjectOperation();
    List<WSObject> objectList = new ArrayList<WSObject>();

    op.refreshTCObjects(new WSObject[] { dataset });
    WSObject[] files = op.getFilesOfDataset(dataset);
    if (files == null)
      return "";
    // ��ȡ�ļ���׺Ϊ .dwg�ĵ�һ���ļ�
    for (WSObject obj : files) {
      filename = op.getPropertyOfObject(obj, "original_file_name");
      if (filename.toLowerCase().endsWith(".dwg")) {
        objectList.add(obj);

      }

    }
    return filename;
    // try{
    // op.deleteRelationships(dataset, objectList.toArray(new
    // ModelObject[]{}), "dwg");
    // op.deleteObjects(objectList.toArray(new ModelObject[]{}));
    // }catch(TCOperationException e){
    // e.printStackTrace();
    // }
    // op.refreshTCObjects(new ModelObject[]{dataset});
    // System.out.println("Stop delete dwg");
  }

  /**
   * �ж�DWG���ݼ��µ��������õ��������������2������true;���򣬷���false;
   * 
   * @user xukun
   *@param dataset
   *@return
   * @throws TCOperationException
   *@throws TCOperationException
   *            date 2009-12-29 since jdk1.5.09
   * 
   */

  /*
   * public static boolean countDwg(ModelObject dataset)throws
   * TCOperationException{ String filename=""; int count=0; ObjectOperation
   * op=new ObjectOperation(); List<ModelObject> objectList=new
   * ArrayList<ModelObject>();
   * 
   * op.refreshTCObjects(new ModelObject[]{dataset}); ModelObject[] files =
   * op.getFilesOfDataset(dataset);
   * 
   * // ��ȡ�ļ���׺Ϊ .dwg�ĵ�һ���ļ� for(ModelObject obj : files){ filename =
   * op.getPropertyOfObject(obj, "original_file_name");
   * if(filename.endsWith(".dwg") || filename.endsWith(".DWG") ||
   * filename.endsWith(".Dwg")){ objectList.add(obj); count++;
   * 
   * }
   * 
   * } if(count>1) return true; else return false; }
   */

  /**
   * 
   * TODO ȡ���ݼ����һ���������õ�����
   * 
   * @return String
   * @author lijj created on 2011-9-17����06:55:10
   */
  public static String getDSFileRefName(WSObject dataset, String filterStr) throws TCOperationException {
    String filename = "";
    ITCTCObjOperation tcop = new TCObjOperation();

    tcop.refreshObjects(new WSObject[] { dataset });
    WSObject[] files = tcop.getFilesOfDataset(dataset);

    if (files == null)
      return "";
    for (WSObject obj : files) {
      filename = tcop.getPropertyValueOfObject(obj, "original_file_name");
      if (filename.toLowerCase().endsWith(filterStr)) {
        break;
      }
    }
    return filename;
  }

  public static boolean countDwg(WSObject dataset) throws TCOperationException {
    String filename = "";
    int count = 0;
    ITCObjectOperation op = new TCObjectOperation();
    List<WSObject> objectList = new ArrayList<WSObject>();
    // op.refreshTCObjects(new WSObject[]{dataset});
    WSObject[] files = op.getFilesOfDataset(dataset);

    if (files == null)
      return false;
    // ��ȡ�ļ���׺Ϊ .dwg�ĵ�һ���ļ�
    System.out.println("Util countDwg=" + files.length);
    if (files != null && files.length > 0) {
      for (WSObject obj : files) {
        System.out.println("util countDwg  obj=" + obj);
        filename = op.getPropertyOfObject(obj, "original_file_name");
        if (filename.endsWith(".dwg") || filename.endsWith(".DWG") || filename.endsWith(".Dwg")) {
          objectList.add(obj);
          count++;

        }

      }
    }
    if (count > 1)
      return true;
    else
      return false;
  }

  /**
   * ͳ��ĳ���ַ����У�����ĳ���ַ��Ĵ���
   * 
   * @user xukun
   *@param strԭʼ�ַ���
   *            ; cҪͳ�Ƶ��ַ�
   *@return ����c�ĸ��� date 2009-9-23 since jdk1.5.09
   * 
   */
  public static int countChar(String str, char c) {
    if ("".equals(str)) {
      return -1;

    }
    char[] tempstr = str.toCharArray();
    int count = 0;
    for (int i = 0; i < tempstr.length - 1; i++) {
      if (tempstr[i] == c)
        count++;
      else
        continue;
    }
    return count;
  }

  /**
   * ��ȡPLT_Pathѡ���·��ֵ
   */
  public static String getPltPath() {
    try {
      String[] path = TCUtil.getPreferenceValues("HNT_PLT_PATH", "site");
      if (null != path && path.length == 1) {
        return path[0];
      } else {
        return "";
      }
    } catch (TCOperationException e) {
      e.printStackTrace();
      return "";
    }
  }

  /**
   * ��ָ�����ļ�·���и��Ƶ���һ���ļ�·����
   * 
   * @user xukun
   *@param sourceFile
   *@param newFile
   *           date 2009-10-26 since jdk1.5.09
   * 
   */
  public static void copyFile(String sourceFile, String newFile) {
    try {
      int bytesum = 0;
      int byteread = 0;
      File oldfile = new File(sourceFile);
      if (oldfile.exists()) { // �ļ�����ʱ
        InputStream inStream = new FileInputStream(sourceFile); // ����ԭ�ļ�
        FileOutputStream fs = new FileOutputStream(newFile);
        byte[] buffer = new byte[1444];
        while ((byteread = inStream.read(buffer)) != -1) {
          bytesum += byteread; // �ֽ��� �ļ���С
          fs.write(buffer, 0, byteread);
        }
        inStream.close();
      }
    } catch (Exception e) {
      // System.out.println("���Ƶ����ļ���������");
      e.printStackTrace();

    }

  }

  public static void setRev_id(String rev_ids) {
    rev_id = rev_ids;
  }

  public static String getRev_id() {
    return rev_id;
  }

  public static void setOperator(String username) {
    operator = username;
  }

  public static String getOperator() {
    return operator;
  }
}
