package news.dvlp.testcamera.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by zillv on 2017/6/16.
 * #表示没有则为空，0表示如果没有则该位补0.
 */

public class NumFormat {
    //http://www.cnblogs.com/nayitian/p/3214178.html
    //http://www.jb51.net/article/46010.htm

    /**
     * 已废弃
     *
     * @param value
     * @param symbol
     * @return
     */
    @SuppressLint("NewApi")
    @Deprecated
    public static String formatDouble2(String value, String symbol) {
        if (TextUtils.isEmpty(value)) {
            return "";
        }
        if ("-1".equals(value) || "-1.0".equals(value) || "-1.00".equals(value) || "-1.000".equals(value) || "-1.0000".equals(value))
            return "--";
        Double d = 0d;
        DecimalFormat df = new DecimalFormat("0.00");
        try {
            d = Double.valueOf(value);
//            df = new DecimalFormat("0.00");
            // // 如果不需要四舍五入，可以使用RoundingMode.DOWN
            df.setRoundingMode(RoundingMode.DOWN);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return df.format(d) + symbol;
    }

    /**
     * 已废弃
     *
     * @param value
     * @param symbol
     * @return
     */
    @Deprecated
    public static String Decimal2(String value, String symbol) {
        if (TextUtils.isEmpty(value)) {
            return "";
        }
        Double d = Double.valueOf(value);
        DecimalFormat df = new DecimalFormat("0.00");
        // // 如果不需要四舍五入，可以使用RoundingMode.DOWN
        df.setRoundingMode(RoundingMode.DOWN);
        return df.format(d) + symbol;
    }

    /**
     * 已废弃
     *
     * @param value
     * @param symbol
     * @return
     */
    @Deprecated
    public static String formatDouble4(String value, String symbol) {
        if (TextUtils.isEmpty(value)) {
            return "";
        }
        if ("-1".equals(value) || "-1.0".equals(value) || "-1.00".equals(value) || "-1.000".equals(value) || "-1.0000".equals(value))
            return "--";
        Double d = Double.valueOf(value);
        DecimalFormat df = new DecimalFormat("0.0000");
        // // 如果不需要四舍五入，可以使用RoundingMode.DOWN
        df.setRoundingMode(RoundingMode.DOWN);
        //    String str=df.format(d);
        //避免以“0.9999”几开头  会显示".9999"
//        if(str.startsWith(".")){
//            str=0+str;
//        }
        return df.format(d) + symbol;
    }

    public static String format(String value, String symbol) {
        if (TextUtils.isEmpty(value)) {
            return "";
        }
        if ("-1".equals(value) || "-1.0".equals(value) || "-1.00".equals(value) || "-1.000".equals(value) || "-1.0000".equals(value))
            return "--";
        return value + symbol;
    }

    /**
     * 千位分隔符,并且小数点后保留2位
     *
     * @param num
     * @return String
     */
    public static String qianweifenge(double num) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        String ss = df.format(num);
//        String strNum="";
//        if(ss.endsWith(".00")){
//            strNum= ss.replace(".00","");
//        }else {
//            strNum=ss;
//        }
        return ss;
    }

    /**
     * 千位分隔符bu保留2位
     *
     * @param num
     * @return String
     */
    public static String qianweifengeNo(double num) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        String ss = df.format(num);
        String strNum = "";
        if (ss.endsWith(".00")) {
            strNum = ss.replace(".00", "");
        } else {
            strNum = ss;
        }
        return strNum;
    }

    public static String sub(Double d1, Double d2) {
        //http://blog.csdn.net/yinan9/article/details/17283081
        //Double  相减
        DecimalFormat df = new DecimalFormat("0.00");
        BigDecimal b1 = new BigDecimal(df.format(d1));
        BigDecimal b2 = new BigDecimal(df.format(d2));
        return df.format(b1.subtract(b2).doubleValue());

    }

    //判断是-999
    public static boolean isValue_999(String value) {
        if (TextUtils.isEmpty(value)) {
            return false;
        }
        if ("-999".equals(value) || "-999.0".equals(value) || "-999.00".equals(value) || "-999.000".equals(value) || "-999.0000".equals(value)) {
            return true;
        } else {
            return false;
        }
    }

    //判断是-1
    public static boolean isValue_1(String value) {
        if (TextUtils.isEmpty(value)) {
            return false;
        }
        if ("-1".equals(value) || "-1.0".equals(value) || "-1.00".equals(value) || "-1.000".equals(value) || "-1.0000".equals(value)) {
            return true;
        } else {
            return false;
        }
    }

    public static String formatNum1(String value, String symbol) {
        if (TextUtils.isEmpty(value)) {
            return "";
        }
        Double d = Double.valueOf(value);

        DecimalFormat df = new DecimalFormat("0.0");
        // // 如果不需要四舍五入，可以使用RoundingMode.DOWN
        df.setRoundingMode(RoundingMode.DOWN);
        return df.format(d / 10000) + symbol;
    }

    @SuppressLint("NewApi")
    public static String formatNum2(String value, String symbol) {
        if (TextUtils.isEmpty(value)) {
            return "";
        }
        Double d = Double.valueOf(value);
        DecimalFormat df = new DecimalFormat("0.00");
        // // 如果不需要四舍五入，可以使用RoundingMode.DOWN
        df.setRoundingMode(RoundingMode.DOWN);
        return df.format(d) + symbol;
    }


    public static String formatNum4(String value, String symbol) {
        if (TextUtils.isEmpty(value)) {
            return "";
        }
        Double d = Double.valueOf(value);
        DecimalFormat df = new DecimalFormat("0.0000");
        // // 如果不需要四舍五入，可以使用RoundingMode.DOWN
        df.setRoundingMode(RoundingMode.DOWN);
        //    String str=df.format(d);
        //避免以“0.9999”几开头  会显示".9999"
//        if(str.startsWith(".")){
//            str=0+str;
//        }
        return df.format(d) + symbol;
    }

//    public static String formatNumForWan(String value) {
//        if (TextUtils.isEmpty(value)) {
//            return "";
//        }
//        String formatNumStr = "";
//        String nuit = "";
//        String strNum = "";//最后处理的结果  去掉.0XX
//        try {
//            BigDecimal b0 = new BigDecimal("1");
//            BigDecimal b00 = new BigDecimal("0.1");
//            BigDecimal b000 = new BigDecimal("1000");
//            BigDecimal b1 = new BigDecimal("10000");
//            BigDecimal b2 = new BigDecimal("100000000");
//            BigDecimal b3 = new BigDecimal(value);
//            // 以万为单位处理
//            if (b3.compareTo(b1) == -1) {
//                if (b3.compareTo(b00) == -1) {
//                    formatNumStr = b3.multiply(new BigDecimal("100")).toString();
//                    nuit = "分";
//                } else if (b3.compareTo(b0) == -1) {
//                    formatNumStr = b3.multiply(new BigDecimal("10")).toString();
//                    nuit = "角";
//                } else if (b3.compareTo(b000) == -1) {
//                    formatNumStr = b3.toString();
//                    nuit = "元";
//                } else {
//                    formatNumStr = b3.toString();
//                    nuit = "";
//                }
//            } else if ((b3.compareTo(b1) == 0 && b3.compareTo(b1) == 1)
//                    || b3.compareTo(b2) == -1) {
//                String numStr = b3.divide(b1).toString();
//                if (numStr.contains(".")) {
//                    formatNumStr = numStr.substring(0, numStr.lastIndexOf('.'));
//                } else {
//                    formatNumStr = numStr;
//                }
//                nuit = "万";
//            } else if (b3.compareTo(b2) == 0 || b3.compareTo(b2) == 1) {
//                String numStr = b3.divide(b2).toString();
//                if (numStr.contains(".")) {
//                    formatNumStr = numStr.substring(0, numStr.lastIndexOf('.'));
//                } else {
//                    formatNumStr = numStr;
//                }
//                nuit = "亿";
//            }
//            if (StringUtils.isEmpty(formatNumStr)) {
//                return "";
//            }
//            if (formatNumStr.endsWith(".00")) {
//                strNum = formatNumStr.replace(".00", "");
//            } else if (formatNumStr.endsWith(".0")) {
//                strNum = formatNumStr.replace(".0", "");
//            } else if (formatNumStr.endsWith(".10")) {
//                strNum = formatNumStr.replace(".10", ".1");
//            } else if (formatNumStr.endsWith(".20")) {
//                strNum = formatNumStr.replace(".20", ".2");
//            } else if (formatNumStr.endsWith(".30")) {
//                strNum = formatNumStr.replace(".30", ".3");
//            } else if (formatNumStr.endsWith(".40")) {
//                strNum = formatNumStr.replace(".40", ".4");
//            } else if (formatNumStr.endsWith(".50")) {
//                strNum = formatNumStr.replace(".50", ".5");
//            } else if (formatNumStr.endsWith(".60")) {
//                strNum = formatNumStr.replace(".60", ".6");
//            } else if (formatNumStr.endsWith(".70")) {
//                strNum = formatNumStr.replace(".70", ".7");
//            } else if (formatNumStr.endsWith(".80")) {
//                strNum = formatNumStr.replace(".80", ".8");
//            } else if (formatNumStr.endsWith(".90")) {
//                strNum = formatNumStr.replace(".90", ".9");
//            } else {
//                strNum = formatNumStr;
//            }
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//        return strNum + nuit;
//    }

    //去除小数点后多余的零，在写一个计算器过程中，显示结果时需要处理结果中小数点后多余的零
    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }
}
