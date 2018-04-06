package xyz.inux.pluto.common.utils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    //24小时对应的秒数是86400秒
    public static final int SECONDS_ONE_DAY = 86400;

    public static final int SECONDS_ONE_HOUR = 3600;

    public static final int MINUTES_ONE_DAY = 1440;

    /**
     * 获取当前时间戳（秒）
     * @return int
     */
    public static int getUnixstamp() {
        BigDecimal bdSeconds = new BigDecimal(System.currentTimeMillis() * 0.001);
        return bdSeconds.intValue();
    }

    /**
     * 根据时间获得unixstamp
     * @return int
     */
    public static int getUnixstamp(int d, int h, int m, int s) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, h);
        calendar.set(Calendar.MINUTE, m);
        calendar.set(Calendar.SECOND, s);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, d);

        return getUnixstamp(calendar.getTime());
    }

    /**
     * 获取特定日期时间戳（秒）
     * @return int
     */
    public static int getUnixstamp(Date date) {
        if (date == null) {
            return 0;
        }

        BigDecimal bdSeconds = new BigDecimal(date.getTime() * 0.001);
        return bdSeconds.intValue();
    }

    /**
     * 将时间戳（秒）转换为日期格式
     * @param seconds
     * @return
     */
    public static Date getDate(int seconds) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(seconds *1000l);
        return c.getTime();
    }

    /**
     * 获取日期字符串，格式自定义
     * yyyy-MM-dd HH:mm:ss
     * yyyy-MM-dd HH:mm:ssSSS
     * @return String
     */
    public static String getDate(String fmt) {
        Date date  = new Date();
        DateFormat format = new SimpleDateFormat(fmt);
        return format.format(date);

    }

    /**
     * 时间戳（秒）转换为日期字符串
     * @return
     */
    public static String getDate(int seconds, String fmt) {
        DateFormat format = new SimpleDateFormat(fmt);
        return  format.format(getDate(seconds));
    }

    /**
     * 设置时间获取日期
     * @return int
     */
    public static String getDate(int d, int h, int m, int s, String fmt) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, h);
        calendar.set(Calendar.MINUTE, m);
        calendar.set(Calendar.SECOND, s);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DAY_OF_MONTH, d);

        return DateUtil.formatDate(calendar.getTime(), fmt);
    }

    /**
     * 字符串日期转换为Date
     * @return
     */
    public static Date parseDate(String dateStr, String fmt) {
        Date date = null;
        DateFormat format = new SimpleDateFormat(fmt);
        try {
            date = format.parse(dateStr);
        } catch (ParseException e) {
//            logger.info("日期转换出错:"+e.getMessage());
        }
        return date;
    }

    /**
     * Date转换为字符串日期
     * @return
     */
    public static String formatDate(Date date, String fmt) {
        DateFormat format = new SimpleDateFormat(fmt);
        return format.format(date);
    }



    /**
     * 获取当前月第一天，格式：yyyy-MM-dd
     * @return int
     */
    public static Date getCurrMonthFirstDay() {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天

        return calendar.getTime();
    }

    /**
     * 为给定的日期添加指定的天数
     * @param date
     * @param days
     * @return
     */
    public static Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }

    /**
     * 为给定的日期字符串添加指定的天数
     * @param dateStr yyyy-MM-dd格式的字符串
     * @param days
     * @return
     */
    public static Date addDays(String dateStr, String fmt, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtil.parseDate(dateStr, fmt));
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }

    /**
     * 获取当前日期是星期几
     *
     * @param dt
     * @return 当前日期是星期几
     */
    public static int getWeekDay(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);

        int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week < 0) {
            week = 0;
        }

        return week;
    }


}
