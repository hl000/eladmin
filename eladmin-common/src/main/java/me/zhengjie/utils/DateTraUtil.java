package me.zhengjie.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTraUtil {
    static long base = 0L;
    public static long gap = 24*60*60*1000;
    static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");//要转换的日期格式，根据实际调整""里面内容
    //  static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");//要转换的日期格式，根据实际调整""里面内容

    static {
        try {
            base  = SDF.parse("1970-01-01 00-00-00").getTime();
        }catch (Exception e) {

        }
    }

    private static ThreadLocal<SimpleDateFormat> simpleDateFormatThreadLocal = new ThreadLocal<SimpleDateFormat>();
    private static ThreadLocal<SimpleDateFormat> simpleDateFormatThreadLocal2 = new ThreadLocal<SimpleDateFormat>();


    public static String formatDate(long sys) {
        try {
            SimpleDateFormat dayFormat = getSimpleDateFormat();

            return dayFormat.format(new Date(sys));
        } catch (Exception e) {
            return "1997-01-01 00-00-00";
        }
    }
    private static SimpleDateFormat getSimpleDateFormat() {

        SimpleDateFormat simpleDateFormat = simpleDateFormatThreadLocal.get();

        if (simpleDateFormat == null){

            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

            simpleDateFormatThreadLocal.set(simpleDateFormat);

        }
        return simpleDateFormat;

    }


    private static SimpleDateFormat getSimpleDateFormatHour() {

        SimpleDateFormat simpleDateFormat = simpleDateFormatThreadLocal2.get();

        if (simpleDateFormat == null){

            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

            simpleDateFormatThreadLocal2.set(simpleDateFormat);

        }
        return simpleDateFormat;

    }

    public static  long getDayId (long now) {
        return (now - base)/gap;
    }

    public static int getHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static String getDateStr(long sys) {
        return getSimpleDateFormatHour().format(new Date(sys));
    }

    public static String getDayStr(long now) {
        return formatDate(now);
    }


    public static int getHourByStr(String str) {
        try {
            return getHour(getSimpleDateFormat().parse(str));
        }catch (Exception e) {
            return -1;
        }
    }


}
