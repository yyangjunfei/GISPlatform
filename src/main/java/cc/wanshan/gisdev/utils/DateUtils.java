package cc.wanshan.gisdev.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
  public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
  public static final String MINUTE_PATTERN = "yyyy-MM-dd HH:mm";
  public static final String HOUR_PATTERN = "yyyy-MM-dd HH:mm:ss";
  public static final String DATE_PATTERN = "yyyy-MM-dd";
  public static final String MONTH_PATTERN = "yyyy-MM";

  /** 时间格式化成字符串 */
  public static String date2String(Date date, String pattern) throws ParseException {
    if (pattern == null || pattern.isEmpty()) {
      pattern = DateUtils.DATE_PATTERN;
    }
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    return sdf.format(date);
  }

  /** 字符串解析成时间对象 */
  public static Date string2Date(String dateTimeString, String pattern) throws ParseException {
    if (pattern == null || pattern.isEmpty()) {
      pattern = DateUtils.DATE_PATTERN;
    }
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    return sdf.parse(dateTimeString);
  }
}
