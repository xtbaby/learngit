package com.ufc.uif.qh3.acad.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Util {

    private static final String CHINESEPOINT = "��";
    private static final String CHANGEDSIGNE = "_";
    public static final String STANDARDPREFIX = "GB,GJB,WJ,Q/LB,JB,SKF,YB";
    public static Map<String, String> secretLevels1 = new HashMap<String, String>();

    /** ����ͼ����׺*/
    public static List<String> fuzhuSuffiz = new ArrayList<String>();
    /** ����ͼ�����ݼ���Ϣ*/
    public static Map<String , String> fuzhuDataset = new HashMap<String, String>();
    /** �Ƿ��Ǹ���ͼ��*/
    public static boolean isFuzhuDrawing = false;
    /** ����Ǹ���ͼ������¼����ͼ����׺*/
    public static String fuzhuDrawingSuffix = "";
    /** ����ͼ���㲿������*/
    public static String fuzhuDrawingCode = "";
    
    static {
        secretLevels1.put("����", "Screct1");
        secretLevels1.put("����", "Screct2");
        secretLevels1.put("����", "Screct3");
    }
    public static Map<String, String> secretLevels2 = new HashMap<String, String>();


    static {
        secretLevels2.put("Screct1", "����");
        secretLevels2.put("Screct2", "����");
        secretLevels2.put("Screct3", "����");
    }

    //����70����������������ʷ���������У�ͼ�š��㲿�������п���������������ĵ㡰������
    //ע����word�������������У����ַ�����Ϊ00B7
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

    //�����ĵ�ţ��滻��Ӣ�ĵ�
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
        //�����ĵ��滻��
        String partNumber = replaceChinesePoint(drawingNo);
        /*ע������ͻ����ۣ�����û�����������daizb��2009-07-02/10:01
        //��������ͼ,ע����70��������ͼͼ�����ԡ�-���ָ��ģ��Ϳͻ����ֵ�
        //����ʽ��ֱ�ӽ���-���Ժ�Ĳ��ֽ�ȥ
        int index = partNumber.indexOf("-");
        if (index > -1) {
        partNumber = partNumber.substring(0, index);
        }*/
        return partNumber;
    }

    //ע��ͼ���еڶ����е��ֻ���������ֵ���װ���,
    //�÷�������replaceChinesePoint��������õģ������ԡ�_��Ϊ�жϱ�־
    public static boolean isAssembly(String drawingNo) {
        boolean result = false;
        //��֤���ĵ㱻�滻����
        drawingNo = replaceChinesePoint(drawingNo);
        //���ͼ����û�С�_�������Ǳ�׼������Ĭ��Ϊ�����
        int index = drawingNo.indexOf(CHANGEDSIGNE);
        if (index > -1) {
            String[] tmp = drawingNo.split(CHANGEDSIGNE);
            if (tmp.length == 3) {
                String s = tmp[2];
                //�����󲿷���1λ���֣��򷵻�true
                if (s.length() == 1) {
                    result = true;
                    return result;
                }

                int i = 0;
                for (; i < s.length(); i++) {
                    //���ĳ���ַ������������˳�
                    if (!isCharDigit(s.charAt(i))) {
                        break;
                    }
                }
                //�ж�i��ֻ��i����1����ֻ���������ֵ�����£�����Ϊ��װ���
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
        //���û��������ţ�����Ϊ�Ǹ���
        if (drawingCode == null && drawingCode.length() <= 0) {
            return CommonProperty.AMATRI;
        }
        //��������б������Ǹ��ϣ�����Ϊ��������Ǹ���
        if ("����".equals(description)) {
            return CommonProperty.AMATRI;
        }
        String dataType = CommonProperty.PART;
        if (isAssembly(drawingCode)) {
            dataType = CommonProperty.ASSEMBLY;
        }
        return dataType;
    }

    public static boolean isPartStandarded(String partNumber, String description) {
        //����ڱ�ע�б���Ϊ����׼�������򷵻�true
        if ("��׼��".equals(description)) {
            return true;
        }

        //�����עΪ���⹺�����򷵻�false��ע�����Ǹ��ݿͻ����߼��������ע�б����ˡ��⹺������
        //��Ϊ�Ǳ�׼��
        if ("�⹺".equals(description)) {
            return false;
        }

        boolean result = false;
        //�����������Ա�׼��ǰ׺֮һ��ͷ���򷵻�true
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

    //ע�������е�֮����ַ���Ϊ���
    public static String getGroupNo(String drawingNo) {

        String groupNo = "";
        String[] tmp = drawingNo.split(CHANGEDSIGNE);
        if (tmp.length == 3) {
            groupNo = tmp[1];
        }
        return groupNo;
    }

    //����ͼ���жϲ�������
    //�жϹ���ͼ���м��������ζ�Ϊ��00������Ϊ��Ʒ���ͣ�
    //�м�β�Ϊ��00��������Ϊ��00������Ϊ������ͣ�
    //������һλ��һλ���֣�����Ϊ�ǲ���
    //����Ϊ��������
    public static String getAssType(String drawingCode) {
        String assType = "����";
        drawingCode = replaceCPointToEPoint(drawingCode);
        drawingCode = replaceEPointToDLine(drawingCode);
        String[] tmp = drawingCode.split(CHANGEDSIGNE);
        if (tmp.length == 3) {
            String tmp1 = tmp[1];
            String tmp2 = tmp[2];
            if (tmp2.length() == 1) {
                assType = "���";
            } else if ("00".equals(tmp2)) {
                if ("00".equals(tmp1)) {
                    assType = "��Ʒ";
                } else {
                    assType = "���";
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
