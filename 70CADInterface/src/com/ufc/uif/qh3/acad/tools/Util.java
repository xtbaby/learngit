package com.ufc.uif.qh3.acad.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Util {

    private static final String CHINESEPOINT = "·";
    private static final String CHANGEDSIGNE = "_";
    public static final String STANDARDPREFIX = "GB,GJB,WJ,Q/LB,JB,SKF,YB";
    public static Map<String, String> secretLevels1 = new HashMap<String, String>();

    /** 辅助图样后缀*/
    public static List<String> fuzhuSuffiz = new ArrayList<String>();
    /** 辅助图样数据集信息*/
    public static Map<String , String> fuzhuDataset = new HashMap<String, String>();
    /** 是否是辅助图样*/
    public static boolean isFuzhuDrawing = false;
    /** 如果是辅助图样，记录辅助图样后缀*/
    public static String fuzhuDrawingSuffix = "";
    /** 辅助图样零部件代号*/
    public static String fuzhuDrawingCode = "";
    
    static {
        secretLevels1.put("秘密", "Screct1");
        secretLevels1.put("机密", "Screct2");
        secretLevels1.put("绝密", "Screct3");
    }
    public static Map<String, String> secretLevels2 = new HashMap<String, String>();


    static {
        secretLevels2.put("Screct1", "秘密");
        secretLevels2.put("Screct2", "机密");
        secretLevels2.put("Screct3", "绝密");
    }

    //这是70所特殊的情况，其历史遗留数据中，图号、零部件号中有可能有特殊符号中文点“·”，
    //注：在word程序的特殊符号中，其字符代码为00B7
    public static String replaceChinesePoint(String s) {
        if (s == null || s.length() <= 0) {
            return "";
        }
        if (s.indexOf(CHINESEPOINT) > -1) {
            return s.replace(CHINESEPOINT, CHANGEDSIGNE);
        } else {
            return s;
        }
    }

    //将中文点号，替换成英文点
    public static String replaceCPointToEPoint(String s) {
        if (s == null || s.length() <= 0) {
            return "";
        }
        if (s.indexOf(CHINESEPOINT) > -1) {
            return s.replace(CHINESEPOINT, ".");
        } else {
            return s;
        }
    }

    public static String replaceEPointToDLine(String s) {
        if (s == null || s.length() <= 0) {
            return "";
        }
        if (s.indexOf(".") > -1) {
            return s.replace(".", "_");
        } else {
            return s;
        }
    }
    
     public static String replaceCPointToDLine(String s) {
        if (s == null || s.length() <= 0) {
            return "";
        }
        if (s.indexOf(CHINESEPOINT) > -1) {
            return s.replace(CHINESEPOINT, "_");
        } else {
            return s;
        }
    }

    public static String getPartNumber(String drawingNo) {
        //将中文点替换掉
        String partNumber = replaceChinesePoint(drawingNo);
        /*注：经与客户讨论，发现没有这种情况，daizb，2009-07-02/10:01
        //处理连号图,注：在70处，连号图图号是以“-”分隔的，和客户商讨的
        //处理方式是直接将“-”以后的部分截去
        int index = partNumber.indexOf("-");
        if (index > -1) {
        partNumber = partNumber.substring(0, index);
        }*/
        return partNumber;
    }

    //注：图号中第二个中点后只有两个数字的是装配件,
    //该方法是在replaceChinesePoint方法后调用的，所有以“_”为判断标志
    public static boolean isAssembly(String drawingNo) {
        boolean result = false;
        //保证中文点被替换掉了
        drawingNo = replaceChinesePoint(drawingNo);
        //如果图号中没有“_”，则是标准件，则默认为是零件
        int index = drawingNo.indexOf(CHANGEDSIGNE);
        if (index > -1) {
            String[] tmp = drawingNo.split(CHANGEDSIGNE);
            if (tmp.length == 3) {
                String s = tmp[2];
                //如果最后部分是1位数字，则返回true
                if (s.length() == 1) {
                    result = true;
                    return result;
                }

                int i = 0;
                for (; i < s.length(); i++) {
                    //如果某个字符不是数字则退出
                    if (!isCharDigit(s.charAt(i))) {
                        break;
                    }
                }
                //判断i，只有i等于1，即只有两个数字的情况下，便认为是装配件
                if (i == 2) {
                    result = true;
                }
            }
        }
        return result;
    }

    public static void main(String args[]){
    	String s ="dd.00.00";
    	System.out.println(getAssType(s));
    	
    }
    
    public static String getDataType(String drawingCode, String description) {
        //如果没有零件代号，则认为是辅料
        if (drawingCode == null && drawingCode.length() <= 0) {
            return CommonProperty.AMATRI;
        }
        //如果表述中标明了是辅料，则认为零件类型是辅料
        if ("辅料".equals(description)) {
            return CommonProperty.AMATRI;
        }
        String dataType = CommonProperty.PART;
        if (isAssembly(drawingCode)) {
            dataType = CommonProperty.ASSEMBLY;
        }
        return dataType;
    }

    public static boolean isPartStandarded(String partNumber, String description) {
        //如果在备注中标明为“标准件”，则返回true
        if ("标准件".equals(description)) {
            return true;
        }

        //如果备注为“外购”，则返回false，注：这是根据客户的逻辑，如果备注中表明了“外购”则不能
        //认为是标准件
        if ("外购".equals(description)) {
            return false;
        }

        boolean result = false;
        //如果零件代号以标准件前缀之一开头，则返回true
        for (String prefix : STANDARDPREFIX.split(",")) {
            if (partNumber.startsWith(prefix)) {
                result = true;
                break;
            }
        }
        return result;
    }

    private static boolean isCharDigit(char c) {
        boolean result = false;
        if (c >= '0' && c <= '9') {
            result = true;
        }
        return result;
    }

    //注：两个中点之间的字符串为组号
    public static String getGroupNo(String drawingNo) {

        String groupNo = "";
        String[] tmp = drawingNo.split(CHANGEDSIGNE);
        if (tmp.length == 3) {
            groupNo = tmp[1];
        }
        return groupNo;
    }

    //根据图号判断部件类型
    //判断规则：图号中间和最后两段都为“00”，则为产品类型；
    //中间段不为“00”，最后段为“00”，则为组件类型；
    //如果最后一位是一位数字，则认为是部件
    //其余为部件类型
    public static String getAssType(String drawingCode) {
        String assType = "部件";
        drawingCode = replaceCPointToEPoint(drawingCode);
        drawingCode = replaceEPointToDLine(drawingCode);
        String[] tmp = drawingCode.split(CHANGEDSIGNE);
        if (tmp.length == 3) {
            String tmp1 = tmp[1];
            String tmp2 = tmp[2];
            if (tmp2.length() == 1) {
                assType = "组件";
            } else if ("00".equals(tmp2)) {
                if ("00".equals(tmp1)) {
                    assType = "产品";
                } else {
                    assType = "组件";
                }
            }
        }
        return assType;
    }

    public static String switchSecretLevelToInternalValue(String secretLevel) {
        if (secretLevel.length() == 0) {
            return "";
        }
        String internalValue = secretLevels1.get(secretLevel);
        if (internalValue == null) {
            internalValue = "";
        }
        return internalValue;
    }

    public static String switchSecretLevelToDisplayValue(String internalValue) {
        if (internalValue == null || internalValue.length() == 0) {
            return "";
        }
        String displayValue = secretLevels2.get(internalValue);
        if (displayValue == null) {
            displayValue = "";
        }
        return displayValue;
    }
    
    
}
