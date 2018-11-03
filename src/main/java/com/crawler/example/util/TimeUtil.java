package com.crawler.example.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static java.time.ZoneId.SHORT_IDS;

/**
 * Created by daniel.luo on 2017/5/25.
 */
public class TimeUtil {
    public static String convertTime(String oriTime) {
        LocalDate fmtTime = null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH);
            fmtTime = LocalDate.parse(oriTime, formatter);
            System.out.printf("Successfully parsed String %s, date is %s%n", oriTime, fmtTime);
        } catch (DateTimeParseException ex) {
            System.out.printf("%s is not parsable!%n", oriTime);
            ex.printStackTrace();
        }
        if (fmtTime == null) return "";
        else return fmtTime.toString();
    }

    public static LocalDate convertDateTime(String oriTime) {
        LocalDate fmtTime = null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH);
            fmtTime = LocalDate.parse(oriTime, formatter);
            System.out.printf("Successfully parsed String %s, date is %s%n", oriTime, fmtTime);
        } catch (DateTimeParseException ex) {
            System.out.printf("%s is not parsable!%n", oriTime);
            ex.printStackTrace();
        }
        if (fmtTime == null) return null;
        else return fmtTime;
    }

    /**
     * 把一个字符串类型转换成下一天的date
     *
     * @param oriDate 如："2017-07-24"
     * @return 如"2017-07-25"
     */
    public static String nextDate(String oriDate) {
        LocalDate fmtTime = null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            fmtTime = LocalDate.parse(oriDate, formatter);
        } catch (DateTimeParseException ex) {
            System.out.printf("%s is not parsable!%n", oriDate);
            ex.printStackTrace();
        }
        return fmtTime.plusDays(1).toString();
    }

    /**
     * 把一个字符串类型转换成前一天的date
     *
     * @param oriDate 如："2017-07-24"
     * @return 如"2017-07-23"
     */
    public static String beforeDate(String oriDate) {
        LocalDate fmtTime = null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            fmtTime = LocalDate.parse(oriDate, formatter);
        } catch (DateTimeParseException ex) {
            System.out.printf("%s is not parsable!%n", oriDate);
            ex.printStackTrace();
        }
        return fmtTime.minusDays(1).toString();
    }

    /**
     * @param delta （因为美股收盘的时候是凌晨四点，我们当时已经是第二天）所以添加天的偏移量delta，-1代表前一天，0代表当天，1代表后一天
     * @return dateStr
     */
    public static String getDate(int delta) {
        LocalDate today = LocalDate.now();
        if (delta < 0) return today.minusDays(Math.abs(delta)).toString();
        else if (delta > 0) return today.plusDays(delta).toString();
        else return today.toString();
    }
    public static String beforeDate(int delta) {
        LocalDate today = LocalDate.now();
        if (delta < 0) return today.minusDays(Math.abs(delta)).toString();
        else if (delta > 0) return today.plusDays(delta).toString();
        else return today.toString();
    }

    /**
     * 例如20分钟前，返回当前时间的20分钟前
     * @param delta -20
     * @return
     */
    public static String beforeCurrentMillis(long delta) {
        long today = System.currentTimeMillis();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(delta<0){
            today=today+delta;
            return sdf.format(new Date(Long.valueOf(today)));
        }
        return sdf.format(new Date());
    }

    public static String getDateByTimeZone(String timeZone) {
        LocalDate date = LocalDate.now(ZoneId.of(SHORT_IDS.get(timeZone)));
        return date.toString();
    }

    /**
     * 当前日期返回 yyyy-MM-dd HH:mm:ss 格式
     *
     * @return 例如：2017-06-15 15:50:43
     */
    public static String getDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime time = LocalDateTime.now();
        return time.format(formatter);
    }

    /**
     * 当前日期返回 yyyy-MM-dd 格式
     *
            * @return 例如：2017-06-15
            */
    public static String getCurDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime time = LocalDateTime.now();
        return time.format(formatter);
    }

    /**
     * 获取当前的时间戳，精确到毫秒如：1497926312513
     *
     * @return
     */
    public static String getTimeStamp() {
        Timestamp ts = Timestamp.valueOf(LocalDateTime.now());
        return String.valueOf(ts.getTime());
    }

    /**
     *
     * @param timestamp 传的是秒
     * @return
     */
    public static String getTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(timestamp*1000);
    }

    public static String getHHmm() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime time = LocalDateTime.now();
        return time.format(formatter)+":00";
    }

    public static long getTimeStampAsLong() {
        return Timestamp.valueOf(LocalDateTime.now()).getTime();
    }

    public static void sleep(long miliseconds) {
        try {
            Thread.sleep(miliseconds);
        } catch (Exception e) {

        }
    }
    /**
     * 当前日期返回days_num 前的日期 list 日期 格式
     */
    public static List<String> getDateList(int days_num) {
        if (days_num<0) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime localDate = LocalDateTime.now();
        List<String> dates=new ArrayList<>();
        for(int i=0;i<=days_num;i++){
            dates.add(formatter.format(localDate.minus(i, ChronoUnit.DAYS)));
        }
        return dates;
    }

    /**
     * @param date_one 2018-05-21
     * @param date_two 2018-05-22
     * @return 是否是第二天
     */
    public static boolean isNextDate(String date_one,String date_two){
        if(date_two.compareTo(date_one)>0){
            return true;
        }
        return false;
    }

    public static void main(String[] args){
        System.out.println(getHHmm());
    }
}
