package com.example.fangxq.myapplication.utils;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * 通用工具类
 */
public class TimeUtil {
    /**
     * 默认的格式化时间时的模式字符串
     */
    private static String fmtPattern = "yyyy-MM-dd HH:mm:ss";
    /**
     * 格式化时间的工具
     */
    private static SimpleDateFormat sdf = new SimpleDateFormat(fmtPattern, Locale.CHINA);
    /**
     * 被格式化的时间对象
     */
    private static Date date = new Date();
    private static StringBuilder str = new StringBuilder();

    private static HashMap<String, DateFormat> mFormatsMap = new HashMap<>();

    public static final String FORMAT_SIMPLE4 = "yyyyMMdd";
    public static final String FORMAT_SIMPLE = "yyyy-MM-dd";



    /**
     * 获取格式化后的时间的字符串
     *
     * @param timeMills 以毫秒为单位的时间
     * @return 根据模式字符串格式化后的时间
     */
    public static String getFormatted(long timeMills) {
        return getFormatted("mm:ss", timeMills);
    }

    /**
     * 获取格式化后的时间的字符串
     *
     * @param format    格式化时使用的模式字符串，例如mm:ss
     * @param timeMills 以毫秒为单位的时间
     * @return 根据模式字符串格式化后的时间
     */
    public static String getFormatted(String format, long timeMills) {
        // 应用参数中指定的模式字符串
        if (format != null && !"".equals(format)) {
            sdf.applyPattern(format);
        }
        // 设置被格式化的时间
        date.setTime(timeMills);
        // 执行格式化，并返回
        return sdf.format(date);
    }

    /**
     * 获取格式化后的时间的字符串
     *
     * @param calendar calendar
     * @return 根据模式字符串格式化后的时间
     */
    public static String getFormatted(String format, Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        str.delete(0, str.length());
//		String y;
//		String mo;
//		String d;
//		String h;
//		String m;
//		String s;
        if (year < 10) {
//			y = "0" + year;
            str.append("0").append(year);
        } else {
//			y = String.valueOf(year);
            str.append(year);
        }
        if ("yyyy-MM-dd HH:mm:ss".equals(format))
            str.append("-");

        if (month < 10) {
//			mo = "0" + month;
            str.append("0").append(month);
        } else {
//			mo = String.valueOf(month);
            str.append(month);
        }
        if ("yyyy-MM-dd HH:mm:ss".equals(format))
        str.append("-");

        if (day < 10) {
//			d = "0" + day;
            str.append("0").append(day);
        } else {
//			d = String.valueOf(day);
            str.append(day);
        }
        if ("yyyy-MM-dd HH:mm:ss".equals(format))
        str.append(" ");

        if (hour < 10) {
//			h = "0" + hour;
            str.append("0").append(hour);
        } else {
//			h = String.valueOf(hour);
            str.append(hour);
        }
        if ("yyyy-MM-dd HH:mm:ss".equals(format))
        str.append(":");

        if (minute < 10) {
//			m = "0" + minute;
            str.append("0").append(minute);
        } else {
//			m = String.valueOf(minute);
            str.append(minute);
        }
        if ("yyyy-MM-dd HH:mm:ss".equals(format))
        str.append(":");

        if (second < 10) {
//			s = "0" + second;
            str.append("0").append(second);
        } else {
//			s = String.valueOf(second);
            str.append(second);
        }
        return str.toString();
    }

    public static long getNowTime() {
        return strToTime(getFormatted("yyyy-MM-dd HH:mm:ss", Calendar.getInstance()));
    }

    /**
     * 获取格式化后的时间的字符串
     *
     * @param format 格式化时使用的模式字符串，例如mm:ss
     * @param time   Date时间
     * @return 根据模式字符串格式化后的时间
     */
    public static String getFormatted(String format, Date time) {
        // 应用参数中指定的模式字符串
        if (format != null && !"".equals(format)) {
            sdf.applyPattern(format);
        }
        // 设置被格式化的时间
        // 执行格式化，并返回
        return sdf.format(time);
    }

    /**
     * 将短时间格式字符串转换为时间 yyyy-MM-dd
     *
     * @param strDate
     * @return
     */
    public static long strToTime(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate.getTime();
    }

    public static String formattedToFormatted(String oldFormatted, String newFormatted, String strDate) {
        SimpleDateFormat oldF = new SimpleDateFormat(oldFormatted);
        SimpleDateFormat newF = new SimpleDateFormat(newFormatted);
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = oldF.parse(strDate, pos);
        return newF.format(strtodate);
    }

    /**
     * 毫秒转化时分秒毫秒
     */
    public static String formatMilliSecond(long l) {
        int hour = 0, minute = 0;

        int second = (int) l / 1000;

        if (second > 60) {
            minute = second / 60;
            second = second % 60;
        }
        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        return (getTwoLength(hour) + ":" + getTwoLength(minute) + ":" + getTwoLength(second));
    }

    /**
     * 将数字转换成两位数的长度，前面不够补零
     *
     * @param data 传进来的数据
     * @return 将数据转换后返回
     */
    private static String getTwoLength(final int data) {
        if (data < 10) {
            return "0" + data;
        } else {
            return "" + data;
        }
    }

    /**
     * 状态栏时间显示
     * @return
     */
    public static String getTime() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        str.delete(0, str.length());
        if (hour < 10) {
            str.append("0").append(hour);
        } else {
            str.append(hour);
        }
        str.append(":");
        if (minute < 10) {
            str.append("0").append(minute);
        } else {
            str.append(minute);
        }
        return str.toString();
    }

    /**
     * 多媒体游戏时间转换
     * @param millisecond
     * @return
     */
    public static String getTimeDuration(long millisecond) {
        str.delete(0, str.length());
        long sec = (millisecond / 1000);

        if (sec / 3600 >= 1) {
            str.append((sec / 3600)).append(":");
        }

        sec = sec % 3600;
        if (sec / 60 >= 0 && sec / 3600 < 1) {
            if ((sec / 60) < 10) {
                str.append("0").append((sec / 60)).append(":");
            } else {
                str.append((sec / 60)).append(":");
            }
        }
        sec = sec % 60;
        if (sec / 60 < 1) {
            if (sec / 10 < 1) {
                str.append("0").append(sec);
            } else {
                str.append(sec);
            }
        }

        return str.toString();
    }

    public static Date string3Date(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        DateFormat df = getFormater(FORMAT_SIMPLE4);
        try {
            return df.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static String date2String(Date date) {
        if (date == null) {
            return null;
        }
        DateFormat tFormater = getFormater("yyyy-MM-dd");
        return tFormater.format(date);
    }

    public static Date string2Date(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        DateFormat df = getFormater(FORMAT_SIMPLE);
        try {
            return df.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Date();
    }

    @SuppressLint("SimpleDateFormat")
    @NonNull
    public static DateFormat getFormater(String aFormat) {
        if(!mFormatsMap.containsKey(aFormat)) {
            mFormatsMap.put(aFormat, new SimpleDateFormat(aFormat));
        }
        return mFormatsMap.get(aFormat);
    }
}
