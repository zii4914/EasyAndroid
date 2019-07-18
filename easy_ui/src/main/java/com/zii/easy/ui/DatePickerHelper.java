package com.zii.easy.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by zii on 2019/7/18.
 */
public class DatePickerHelper {

  public static Calendar getCalendar(String date) {
    final String pattern = "yyyy-MM-dd HH:mm:dd";
    return getCalendar(date, pattern, true);
  }

  public static Calendar getCalendar(String date, String pattern) {
    return getCalendar(date, pattern, true);
  }

  public static Calendar getCalendar(String date, String pattern, boolean lenient) {
    SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
    try {
      sdf.setLenient(lenient);//校验日期的合法性
      Date parse = sdf.parse(date);
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(parse);
      return calendar;
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static int getMonth(Calendar calendar) {
    return calendar.get(Calendar.MONTH) + 1;
  }

}
