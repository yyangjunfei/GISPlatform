package cc.wanshan.gis.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static final String FMT_yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";
    public static final String FMT_yyyyMMddHHmm = "yyyy-MM-dd HH:mm";
    public static final String FMT_yyyyMMdd = "yyyy-MM-dd";
    public static final String FMT_yyyyMM = "yyyy-MM";
    public final static String FMT_yyyyMMdd_8 = "yyyyMMdd";
    public final static String FMT_yyyyMM_6 = "yyyyMM";

    /**
     * 将Data类型日期根据给定格式转化成字符串
     *
     * @param date      日期对象
     * @param formatStr 日期格式
     * @return
     * @throws ParseException
     */
    public static String date2String(Date date, String formatStr) throws ParseException {
        if (date == null) {
            return null;
        }
        if (formatStr == null || formatStr.trim().length() <= 0) {
            formatStr = DateUtils.FMT_yyyyMMdd;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        return sdf.format(date);
    }

    /**
     * 将String类型时间根据给定格式转化成Date
     *
     * @param dateTimeStr 字符串日期
     * @param formatStr   日期格式
     * @return
     * @throws ParseException
     */
    public static Date string2Date(String dateTimeStr, String formatStr) throws ParseException {

        if (dateTimeStr == null || dateTimeStr.trim().length() <= 0) {
            return null;
        }

        if (formatStr == null || formatStr.trim().length() <= 0) {
            formatStr = DateUtils.FMT_yyyyMMdd;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        return sdf.parse(dateTimeStr);
    }

    /**
     * 比较两个时间的先后顺序
     *
     * @param d1 日期对象
     * @param d2 日期对象
     * @return 如果时间d1在d2之前，返回1，如果时间d1在d2之后，返回-1，如果二者相等，返回0
     */
    public static int compareTwoDate(Date d1, Date d2) {
        if (d1 == null || d2 == null) {
            throw new IllegalArgumentException("参数d1或d2不能是null对象！");
        }

        long dI1 = d1.getTime();
        long dI2 = d2.getTime();

        if (dI1 > dI2) {
            return -1;
        } else if (dI1 < dI2) {
            return 1;
        } else {
            return 0;
        }
    }

}
