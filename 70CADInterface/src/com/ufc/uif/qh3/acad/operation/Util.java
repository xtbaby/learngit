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
   * 将NULL值转为空字符串，去除字符串前后的空格
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
   * 将2维字符数组转为Map类型
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
   * 打印时间
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
   * 是否为采购件 采购件编码为：10位且以1开头
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
   * 是否为管件或接头 编码为：17位且56位是93或567位是989
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
   * 是否为自制件编码
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
   * 将传来的数字字符串（包含小数点），截取指定的位数
   * 
   * @author Liugz
   * @create on 2009-8-6 This project for CAD_Concrete
   * @param originalNumber
   * @param reserveDigit
   *            -- 要截取小数点后的位数
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
    // 移除Map中的空值项
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
        throw new TCOperationException("获取首选项“zl_Item_length”时发生错误，首选项可能配置错误");
    } catch (TCOperationException e) {
      throw new TCOperationException("获取首选项“zl_Item_length”时发生错误，或者该首选项配置错误");
    }
  }

  /**
   * 将DWG转为PDF文件
   * 
   * @author Liugz
   * @create on 2009-7-1 This project for CAD_Concrete
   * @return
   * @throws TCOperationException
   */
  public static String convertDWG2PDF(String dwgFile, String dwgFileName) throws TCOperationException {
    // System.out.println("旧的PDF文件名称："+dwgFileName);
    // dwgFileName = dwgFile.substring(dwgFile.lastIndexOf("\\") + 1,
    // dwgFile.lastIndexOf("."));
    String tempDirStr = System.getenv("UFCROOT") + "\\temp\\PDF\\";
    // System.out.println("新的PDF文件名称："+dwgFileName);
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
      // System.out.println("开始转换：");
      String[] path = TCUtil.getPreferenceValues("UFC_DWG2PDF_PATH", "site");
      if (null != path && path.length == 1) {
        convertPath = path[0];
      } else {
        throw new TCOperationException("转换程序路径配置错误，请检查TC首选项");
      }
      File convertDir = new File(convertPath);
      // String convertTool=ParseRequestXML.getconvertTool();
      File exeFile = new File(convertDir, "dwg2pdf.exe");
      // File exeFile = new File(convertDir, "convertTool");
      if (!exeFile.exists()) {
        throw new TCOperationException("转换程序路径配置错误，请检查TC首选项");
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
       * TCOperationException("获取Windows任务列表是发生错误，无法获取任务列表"); goon =
       * scanTask.isConvertStart() && scanTask.isConvertFinish(); }
       */
      proc.destroy();
      // if(convertError)
      // throw new TCOperationException("获取Windows任务列表是发生错误，无法获取任务列表");

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
   * 删除临时目录中的图纸文件
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
      // outputStatus("未能删除临时目录，请手工删除");
      return;
    }
    String dwgPath = dwgFilePath.substring(0, dwgFilePath.lastIndexOf("\\"));
    String user_id = TCUtil.getUserId();
    String item_id = revObj.getId();
    String rev_id = revObj.getRevision();
    String tempDir = System.getenv("UFCROOT") + "\\Temp\\" + user_id + "_" + item_id + "_" + rev_id;
    // 判断图纸是不是在临时目录里
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
   * 根据已有的数据集对象，来删除该数据集对象下所有的DWG图纸
   * 
   * @author xukun
   * @param dataset数据集对象
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
    // 获取文件后缀为 .dwg的第一个文件
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
   * 判断DWG数据集下的命名引用的数量，如果等于2，返回true;否则，返回false;
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
   * // 获取文件后缀为 .dwg的第一个文件 for(ModelObject obj : files){ filename =
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
   * TODO 取数据集里第一个命名引用的名字
   * 
   * @return String
   * @author lijj created on 2011-9-17下午06:55:10
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
    // 获取文件后缀为 .dwg的第一个文件
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
   * 统计某个字符串中，出现某个字符的次数
   * 
   * @user xukun
   *@param str原始字符串
   *            ; c要统计的字符
   *@return 出现c的个数 date 2009-9-23 since jdk1.5.09
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
   * 获取PLT_Path选项的路径值
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
   * 从指定的文件路径中复制到另一个文件路径下
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
      if (oldfile.exists()) { // 文件存在时
        InputStream inStream = new FileInputStream(sourceFile); // 读入原文件
        FileOutputStream fs = new FileOutputStream(newFile);
        byte[] buffer = new byte[1444];
        while ((byteread = inStream.read(buffer)) != -1) {
          bytesum += byteread; // 字节数 文件大小
          fs.write(buffer, 0, byteread);
        }
        inStream.close();
      }
    } catch (Exception e) {
      // System.out.println("复制单个文件操作出错");
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
