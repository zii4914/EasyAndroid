package com.zii.easy.common.util.common;

import android.text.TextUtils;
import java.text.DecimalFormat;
import java.util.Random;

/**
 * 数字处理工具类
 * Create by zii at 2018/10/15.
 */
public class NumberUtils {

  public static final String CHAR_ALL = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
  public static final String CHAR_LETTER = "abcdefghijkllmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
  public static final String CHAR_NUMBER = "0123456789";

  /**
   * 格式化小数
   *
   * @param pattern 模板，如"#.00"是保留两位
   * @param number 数字
   * @return 结果
   */
  public static String decimalFormat(String pattern, Object number) {
    return new DecimalFormat(pattern).format(number);
  }

  /**
   * 格式化小数
   *
   * @param pattern 模板，如"#.00"是保留两位
   * @param number 数字
   * @return 结果
   */
  public static double decimalFormat(String pattern, double number) {
    return Double.valueOf(decimalFormat2String(pattern, number));
  }

  /**
   * 格式化小数
   *
   * @param pattern 模板，如"#.00"是保留两位
   * @param number 数字
   * @return 结果
   */
  public static String decimalFormat2String(String pattern, double number) {
    return new DecimalFormat(pattern).format(number);
  }

  /**
   * 格式化小数
   *
   * @param pattern 模板，如"#.00"是保留两位
   * @param number 数字
   * @return 结果
   */
  public static long decimalFormat(String pattern, long number) {
    return Long.valueOf(decimalFormat2String(pattern, number));
  }

  /**
   * 格式化小数
   *
   * @param pattern 模板，如"#.00"是保留两位
   * @param number 数字
   * @return 结果
   */
  public static String decimalFormat2String(String pattern, long number) {
    return new DecimalFormat(pattern).format(number);
  }

  /**
   * 保留多少位小数，不足补0，多则切去
   *
   * @param number 原数
   * @param digits 位数
   */
  public static String keepDigitsFraction(double number, int digits) {
    StringBuilder pattern = new StringBuilder("0.");
    for (int i = 0; i < digits; i++) {
      pattern.append("0");
    }
    return decimalFormat2String(pattern.toString(), number);
  }

  /**
   * 保留至少多少位整数，不足补0，多则切去
   *
   * @param number 原数
   * @param digits 位数
   */
  public static String keepDigitsInteger(double number, int digits) {
    StringBuilder pattern = new StringBuilder();
    for (int i = 0; i < digits; i++) {
      pattern.append("0");
    }
    return decimalFormat2String(pattern.toString(), number);
  }

  /**
   * 保留至少多少位整数和小数，不足补0，多则切去
   *
   * @param number 数
   * @param digitsInteger 整数位数
   * @param digitsFraction 小数位数
   */
  public static String keepDigitsIntegerFraction(double number, int digitsInteger, int digitsFraction) {
    StringBuilder pattern = new StringBuilder();
    //整数位数
    if (digitsInteger <= 0) {
      pattern.append("#");
    } else {
      for (int i = 0; i < digitsInteger; i++) {
        pattern.append("0");
      }
    }
    pattern.append(".");
    //小数部分
    if (digitsFraction <= 0) {
      pattern.append("#");
    } else {
      for (int i = 0; i < digitsInteger; i++) {
        pattern.append("0");
      }
    }
    return decimalFormat2String(pattern.toString(), number);
  }

  /**
   * 获取任意位小数点
   *
   * @param src 数据源
   * @param index 第几位，大于等于1
   * @return 结果，或者""
   */
  public static String getDecimal(double src, int index) {
    if (index < 1) {
      return "";
    }
    String[] split = String.valueOf(src).split("\\.");
    if (split.length == 2) {
      return split[1].length() >= index ? split[1].substring(index - 1, index) : "";
    }
    return "";
  }

  public static DecimalFormat getFormat(String pattern) {
    return new DecimalFormat(pattern);
  }

  /**
   * 获取任意位小数点
   *
   * @param src 数据源
   * @param index 第几位，大于等于1
   * @return 结果，或者-1
   */

  public static int getDecimal(String src, int index) {
    if (TextUtils.isEmpty(src)) {
      return -1;
    }
    try {
      String decimal = getDecimal(Double.parseDouble(src), index);
      return Integer.parseInt(decimal);
    } catch (NumberFormatException e) {
      return -1;
    }
  }

  public static int parseString2Int(String src) {
    try {
      return Integer.parseInt(src);
    } catch (NumberFormatException e) {
      e.printStackTrace();
      return 0;
    }
  }

  public static float parseString2Float(String src, float defaultValue) {
    try {
      return Float.parseFloat(src);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return defaultValue;
  }

  public static float parseString2Float(String src) {
    return parseString2Float(src, 0);
  }

  public static double parseString2Double(String src) {
    return parseString2Double(src, 0);
  }

  private static double parseString2Double(String src, double defaultValue) {
    try {
      return Double.parseDouble(src);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return defaultValue;
  }

  /**
   * 获得带正负符号的数字字符串
   *
   * @param isZeroPlus 为0时是否添加 + 符号
   */
  public static String getSignNumber(int num, boolean isZeroPlus) {
    if (num > 0) {
      return "+" + num;
    } else if (num < 0) {
      return "" + num;
    } else {
      return isZeroPlus ? "+" + num : "" + num;
    }
  }

  public static String getSignNumber(double number, boolean isZeroPlus) {
    if (number > 0) {
      return "+" + number;
    } else if (number < 0) {
      return "" + number;
    } else {
      return isZeroPlus ? "+" + number : "" + number;
    }
  }

  /**
   * 在数字和大小写字符串范围内,获得长度随机字符串
   *
   * @param length 长度
   * @return 随机字符串
   */
  public static String randomString(int length) {
    StringBuilder sb = new StringBuilder();
    Random random = new Random();
    for (int i = 0; i < length; i++) {
      sb.append(CHAR_ALL.charAt(random.nextInt(CHAR_ALL.length())));
    }
    return sb.toString();
  }

  /**
   * 在指定字符串范围内,获得长度随机字符串
   *
   * @param source 指定范围
   * @param length 长度
   * @return 随机字符串
   */
  public static String randomString(String source, int length) {
    StringBuilder sb = new StringBuilder();
    Random random = new Random();
    for (int i = 0; i < length; i++) {
      sb.append(source.charAt(random.nextInt(source.length())));
    }
    return sb.toString();
  }

}
