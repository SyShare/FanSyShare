/*
 * Copyright (c) 2016  athou（cai353974361@163.com）.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pince.ut;

import android.content.Context;
import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

public final class TimeUtil {

    public static final String DATE_FORMAT_Y_M_D_H_M_S = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_Y_M_D = "yyyy-MM-dd";
    public static final String DATE_FORMAT_Y_M_D_SLASH = "yyyy/MM/dd";
    public static final String DATE_FORMAT_Y_M = "yyyy-MM";
    public static final String DATE_FORMAT_M_D = "MM.dd";
    public static final String DATE_FORMAT_D = "dd";
    public static final String DATE_FORMAT_Y_M_D_H_M = "yyyy-MM-dd HH:mm";
    public static final String DATE_FORMAT_Y_M_D_H_M_ = "yyyy/MM/dd HH:mm";
    public static final String DATE_FORMAT_H_M_S = "HH:mm:ss";
    public static final String DATE_FORMAT_H_M = "HH:mm";
    public static final String DATE_FORMAT_Y_M_D_a_h_M = "yyyy-MM-dd aa hh:mm";
    public static final String DATE_FORMAT_a_h_M = "aa hh:mm";

    public static final String DATE_FORMAT_YMDHMS = "yyyy年MM月dd日 HH时mm分ss秒";
    public static final String DATE_FORMAT_YMDHM = "yyyy年MM月dd日 HH时mm分";
    public static final String DATE_FORMAT_YMD = "yyyy年MM月dd日";
    public static final String DATE_FORMAT_YM = "yyyy年MM月";


    public static final int SECONDS_PER_MINUTE = 60;
    public static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * 60;
    public static final int SECONDS_PER_DAY = 24 * SECONDS_PER_HOUR;
    public static final long MILLISECOND_EVERYDAY = SECONDS_PER_DAY * 1000;

    public static final long MILLIS_PER_SECOND = 1000L;
    public static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
    public static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;
    public static final long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;

    /**
     * 判断是否为24小时格式
     *
     * @param context
     * @return
     */
    public static boolean is24Hour(Context context) {
        return DateFormat.is24HourFormat(context);
    }

    /**
     * 判断时间是否过期
     *
     * @param startTime
     * @param expriseTime
     * @return true: 日期可用，未过期 ; false: 日期不可用, 已过期
     */
    public static boolean timeIsValid(long startTime, long expriseTime) {

        if (getCurrentTimeMillis() < startTime + expriseTime) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 默认为 "yyyy-MM-dd HH:mm:ss"
     *
     * @return
     */
    public static String getDateString() {
        return getDateStringWithFormate(new Date(), DATE_FORMAT_Y_M_D_H_M_S);
    }

    /**
     * 默认为 "yyyy.MM.dd"
     *
     * @return
     */
    public static String getDateFormatString() {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 1);
        Date date = calendar.getTime();
        return getDateStringWithFormate(date, "yyyy.MM.dd");
    }

    /**
     * 默认为 "yyyy-MM-dd HH:mm:ss"
     *
     * @return
     */
    public static String getDateString(long millTime, String format) {
        return getDateStringWithFormate(new Date(millTime), format);
    }

    /**
     * 默认为 "yyyy-MM-dd HH:mm:ss"
     *
     * @param date
     * @return
     */
    public static String getDateString(Date date) {
        return getDateStringWithFormate(date, DATE_FORMAT_Y_M_D_H_M_S);
    }

    /**
     * 默认为 "yyyy-MM-dd HH:mm:ss"
     *
     * @return
     */
    public static String getDateStringWithFormate() {
        return getDateStringWithFormate(new Date(), DATE_FORMAT_Y_M_D_H_M_S);
    }

    /**
     */
    public static String getDateStringWithFormate(String formate) {
        return getDateStringWithFormate(new Date(), formate);
    }

    /**
     * 默认为 "yyyy-MM-dd HH:mm:ss"
     *
     * @param date
     * @param formate 可为空
     * @return
     */
    public static String getDateStringWithFormate(Date date, String formate) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat dateFormat = null;
        if (StrUtil.isEmpty(formate)) {
            dateFormat = new SimpleDateFormat(DATE_FORMAT_Y_M_D_H_M_S);
        } else {
            dateFormat = new SimpleDateFormat(formate);
        }
        return dateFormat.format(date);
    }

    /**
     * 默认为 "yyyy-MM-dd HH:mm:ss"
     *
     * @param date
     * @param timezone
     * @param formate  可为空
     * @return
     */
    public static String getDateStringWithFormate(Date date, int timezone, String formate) {
        TimeZone timeZone = TimeZone.getTimeZone("GMT" + (timezone < 0 ? timezone : "+" + timezone));
        return getDateStringWithFormate(date, timeZone, formate);
    }

    /**
     * 默认为 "yyyy-MM-dd HH:mm:ss"
     *
     * @param date
     * @param timezone
     * @param formate  可为空
     * @return
     */
    public static String getDateStringWithFormate(Date date, TimeZone timezone, String formate) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat dateFormat = null;
        if (StrUtil.isEmpty(formate)) {
            dateFormat = new SimpleDateFormat(DATE_FORMAT_Y_M_D_H_M_S);
        } else {
            dateFormat = new SimpleDateFormat(formate);
        }
        dateFormat.setTimeZone(timezone);
        return dateFormat.format(date);
    }

    /**
     * 获取指定时区的时间
     */
    public static String getTimeZoneFormatedDateString(float timeZoneOffset, String formate) {
        if (timeZoneOffset > 13 || timeZoneOffset < -12) {
            timeZoneOffset = 0;
        }
        int newTime = (int) (timeZoneOffset * 60 * 60 * 1000);
        TimeZone timeZone;
        String[] ids = TimeZone.getAvailableIDs(newTime);
        if (ids.length == 0) {
            timeZone = TimeZone.getDefault();
        } else {
            timeZone = new SimpleTimeZone(newTime, ids[0]);
        }
        SimpleDateFormat sdf = null;
        if (StrUtil.isEmpty(formate)) {
            sdf = new SimpleDateFormat(DATE_FORMAT_Y_M_D_H_M);
        } else {
            sdf = new SimpleDateFormat(formate);
        }
        sdf.setTimeZone(timeZone);
        return sdf.format(new Date());
    }

    /**
     * 根据已有日期String转换日期格式
     *
     * @param otherDateStr
     * @param fromFormate
     * @param toFormate
     * @return
     */
    public static String getDateStringWithDateString(String otherDateStr, String fromFormate, String toFormate) {
        if (StrUtil.isEmpty(otherDateStr) || StrUtil.isEmpty(fromFormate) || StrUtil.isEmpty(toFormate)) {
            return null;
        }
        Date date = getDateFromString(otherDateStr, fromFormate);
        return getDateStringWithFormate(date, toFormate);
    }

    /**
     * Get the date form the given dateString with the given Formate. If the
     * formate is null, will use the default formate
     * "yyyy-MM-dd HH:mm:ss"
     *
     * @param dateString
     * @param formate
     * @return
     */
    public static Date getDateFromString(String dateString, String formate) {
        SimpleDateFormat dateFormat = null;
        if (StrUtil.isEmpty(formate)) {
            dateFormat = new SimpleDateFormat(DATE_FORMAT_Y_M_D_H_M_S);
        } else {
            dateFormat = new SimpleDateFormat(formate);
        }

        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取2个日期的天数差 yyyy-MM-dd hh:mm:ss
     * "yyyy-MM-dd HH:mm:ss"
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static int getDaySpace(String startTime, String endTime) {
        return getDaySpace(startTime, endTime, DATE_FORMAT_Y_M_D_H_M_S);
    }

    /**
     * 获取2个日期的天数差
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static int getDaySpace(String startTime, String endTime, String format) {
        return getDaySpace(getDateFromString(startTime, format), getDateFromString(endTime, format));
    }

    /**
     * 获取2个日期的天数差
     *
     * @param startDate
     * @param endDate
     */
    public static int getDaySpace(Date startDate, Date endDate) {
        return getDaySpace(startDate.getTime(), endDate.getTime());
    }

    /**
     * 获取2个日期的天数差
     *
     * @param startMills
     * @param endMills
     * @return
     */
    public static int getDaySpace(long startMills, long endMills) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTimeInMillis(startMills / 1000 * 1000); //将ms部分取整 1480581171324 -> 1480581171000
        startCal.set(Calendar.MILLISECOND, 0);
        startCal.set(Calendar.SECOND, 0);
        startCal.set(Calendar.MINUTE, 0);
        startCal.set(Calendar.HOUR_OF_DAY, 0);
        Calendar endCal = Calendar.getInstance();
        endCal.setTimeInMillis(endMills / 1000 * 1000);
        endCal.set(Calendar.MILLISECOND, 0);
        endCal.set(Calendar.SECOND, 0);
        endCal.set(Calendar.MINUTE, 0);
        endCal.set(Calendar.HOUR_OF_DAY, 0);

        int days = 0;
        if (startCal.after(endCal)) {
            while (startCal.after(endCal)) {
                days++;
                endCal.add(Calendar.DATE, 1);
            }
        } else if (startCal.before(endCal)) {
            while (startCal.before(endCal)) {
                days--;
                endCal.add(Calendar.DATE, -1);
            }
        }
        startCal.clear();
        endCal.clear();

        startCal = null;
        endCal = null;
        return days;
    }

    /**
     * 获取2个日期的月份差
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int getMonthSpace(Date startDate, Date endDate) {
        GregorianCalendar cal1 = new GregorianCalendar();
        GregorianCalendar cal2 = new GregorianCalendar();
        cal1.setTime(startDate);
        cal2.setTime(endDate);
        int year1 = cal1.get(Calendar.YEAR);
        int month1 = cal1.get(Calendar.MONTH);

        int year2 = cal2.get(Calendar.YEAR);
        int month2 = cal2.get(Calendar.MONTH);

        int monthCount1 = year1 * 12 + month1;
        int monthCount2 = year2 * 12 + month2;
        return monthCount2 - monthCount1;
    }

    /**
     * 拼接时间段
     */
    public static String getHourAndMinuteAndSecondTime(int hours, int minute, int second) {
        StringBuffer sb = new StringBuffer();
        if (hours < 10) {
            sb.append("0" + hours + "");
        } else {
            sb.append(hours + "");
        }
        sb.append(":");
        if (minute < 10) {
            sb.append("0" + minute + "");
        } else {
            sb.append(minute + "");
        }
        sb.append(":");
        if (second < 10) {
            sb.append("0" + second);
        } else {
            sb.append(second + "");
        }
        return sb.toString();
    }

    /**
     * 拼接时间段
     */
    public static String getHourAndMinuteAndSecondTime(int second) {
        int hours = second / 60 / 60;
        int minute = second / 60 % 60;
        int sec = second % 60;

        StringBuffer sb = new StringBuffer();
        if (hours < 10) {
            sb.append("0" + hours + "");
        } else {
            sb.append(hours + "");
        }
        sb.append(":");
        if (minute < 10) {
            sb.append("0" + minute + "");
        } else {
            sb.append(minute + "");
        }
        sb.append(":");
        if (sec < 10) {
            sb.append("0" + sec);
        } else {
            sb.append(sec + "");
        }
        return sb.toString();
    }

    /**
     * 拼接时间段
     */
    public static String getHourAndMinuteTime(int hours, int minute) {
        StringBuffer sb = new StringBuffer();
        if (hours < 10) {
            sb.append("0" + hours + "");
        } else {
            sb.append(hours + "");
        }
        sb.append(":");
        if (minute < 10) {
            sb.append("0" + minute + "");
        } else {
            sb.append(minute + "");
        }
        return sb.toString();
    }

    /**
     * 拼接时间段
     */
    public static String getMinuteAndSecendTime(int second) {
        int minute = second / 60;
        int sec = second % 60;
        StringBuffer sb = new StringBuffer();
        if (minute < 10) {
            sb.append("0" + minute + "");
        } else {
            sb.append(minute + "");
        }
        sb.append(":");
        if (sec < 10) {
            sb.append("0" + sec + "");
        } else {
            sb.append(sec + "");
        }
        return sb.toString();
    }

    /**
     * 根据指定格式获取当前时间
     *
     * @param symbols
     * @return mm-dd-yyyy
     */
    public static String getCurrentDate(String symbols) {
        Calendar cale = Calendar.getInstance(TimeZone.getDefault());
        int year = cale.get(Calendar.YEAR);
        int month = cale.get(Calendar.MONTH);
        int day = cale.get(Calendar.DAY_OF_MONTH);
        return month + symbols + day + symbols + year;
    }

    /**
     * 获取系统时间毫秒数
     */
    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 获取系统时间秒数
     */
    public static long getCurrentTimeSecond() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 获取x年x月x日x时x分x秒
     *
     * @param time
     * @return
     */
    public static String getDateString(long time) {
        String str = "";
        Date date = new Date(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        str = String.valueOf(calendar.get(Calendar.YEAR)) + "年" + String.valueOf(calendar.get(Calendar.MONTH)) + "月"
                + String.valueOf(calendar.get(Calendar.DATE)) + "日"
                + String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) + "时)"
                + String.valueOf(calendar.get(Calendar.MINUTE)) + "分" + String.valueOf(calendar.get(Calendar.SECOND))
                + "秒";
        return str;
    }

    /**
     * 根据天数获取毫秒数
     *
     * @return
     */
    public static long getDateTimeMillis(int dateNum) {
        return MILLISECOND_EVERYDAY * dateNum;
    }

    /**
     * 根据毫秒数获取天数
     *
     * @return
     */
    public static int getDaysFromMillis(long millis) {
        return (int) (millis / MILLISECOND_EVERYDAY);
    }

    /**
     * 根据毫秒数获取小时数
     *
     * @return
     */
    public static int getHoursFromMillis(long millis) {
        return (int) (millis / MILLIS_PER_HOUR);
    }

    /**
     * 根据毫秒数获取分钟数
     *
     * @return
     */
    public static int getMinutsFromMillis(long millis) {
        return (int) (millis / MILLIS_PER_MINUTE);
    }

    /**
     * 根据当前年份和月份判断这个月的天数
     *
     * @param year
     * @param month
     * @return
     */
    public static int getDayNum(int year, int month) {
        int day;
        if (year % 4 == 0 && year % 100 != 0) { // 闰年
            if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
                day = 31;
            } else if (month == 2) {
                day = 29;
            } else {
                day = 30;
            }
        } else { // 平年
            if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
                day = 31;
            } else if (month == 2) {
                day = 28;
            } else {
                day = 30;
            }
        }
        return day;
    }

    /**
     * 通过日期天数判断2个日期的先后
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static boolean beforeByDay(Date startDate, Date endDate) {
        int year_start = startDate.getYear();
        int month_start = startDate.getMonth();
        int day_start = startDate.getDate();

        int year_end = endDate.getYear();
        int month_end = endDate.getMonth();
        int day_end = endDate.getDate();
        if (year_start < year_end) {
            return true;
        } else if (year_start == year_end) {
            if (month_start < month_end) {
                return true;
            } else if (month_start == month_end) {
                if (day_start < day_end) {
                    return true;
                } else if (day_start == day_end) {
                    return false;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 通过日期天数判断2个日期的先后
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static boolean afterByDay(Date startDate, Date endDate) {
        int year_start = startDate.getYear();
        int month_start = startDate.getMonth();
        int day_start = startDate.getDate();

        int year_end = endDate.getYear();
        int month_end = endDate.getMonth();
        int day_end = endDate.getDate();
        if (year_start < year_end) {
            return false;
        } else if (year_start == year_end) {
            if (month_start < month_end) {
                return false;
            } else if (month_start == month_end) {
                if (day_start < day_end) {
                    return false;
                } else if (day_start == day_end) {
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    /**
     * 判断2个日期是否同一天
     *
     * @param day1
     * @param day2
     * @return
     */
    public static boolean isSameDay(Date day1, Date day2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String ds1 = sdf.format(day1);
        String ds2 = sdf.format(day2);
        if (ds1.equals(ds2)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否相差超过1个小时
     *
     * @param timeBefore
     * @return
     */
    public static boolean isMoreThanHour(long timeBefore) {
        long from = new Date(timeBefore).getTime();
        long now = System.currentTimeMillis();
        int hours = (int) ((now - from) / (1000 * 60 * 60));
        return hours >= 1;
    }

    //计算年龄部分有误，未考虑闰年
//	/**
//	 * 计算年龄
//	 *
//	 * @param secondTime
//	 * @return
//	 */
//	public static int getYearsFromSecondTime(long secondTime) {
//		long m1 = secondTime * MILLIS_PER_SECOND;
//		long m2 = getCurrentTimeMillis();
//		return (int) ((m2 - m1) / (MILLISECOND_EVERYDAY * 365));
//	}
//
//	/**
//	 * 计算年龄
//	 *
//	 * @param milTime
//	 * @return
//	 */
//	public static int getYearsFromMillTime(long milTime) {
//		return (int) ((getCurrentTimeMillis() - milTime) / (MILLISECOND_EVERYDAY * 365));
//	}
//
//	/**
//	 * 计算年龄
//	 *
//	 * @param milTime
//	 * @return
//	 */
//	public static int getYearsFromMillTime(String milTime) {
//		int years = 0;
//		if (!StrUtil.isEmpty(milTime)) {
//			long m1 = Long.valueOf(milTime);
//			long m2 = getCurrentTimeMillis();
//			years = (int) ((m2 - m1) / (MILLISECOND_EVERYDAY * 365));
//		}
//		return years;
//	}
//
//	/**
//	 * 计算年龄
//	 *
//	 * @param secondTime
//	 * @return
//	 */
//	public static int getYearsFromSecondTime(String secondTime) {
//		int years = 0;
//		if (!StrUtil.isEmpty(secondTime)) {
//			long m1 = Long.valueOf(secondTime) * MILLIS_PER_SECOND;
//			long m2 = getCurrentTimeMillis();
//			years = (int) ((m2 - m1) / (MILLISECOND_EVERYDAY * 365));
//		}
//		return years;
//	}

    /**
     * 根据用户生日计算年龄
     */
    public static int getAgeByBirthday(Date birthday, Date today) {
        if (birthday.after(today)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(birthday);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                // monthNow==monthBirth
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                // monthNow>monthBirth
                age--;
            }
        }
        return age;
    }

    /**
     * 根据用户生日计算年龄
     */
    public static int getAgeByBirthday(Date birthday) {
        Calendar cal = Calendar.getInstance();

        if (cal.before(birthday)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(birthday);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                // monthNow==monthBirth
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                // monthNow>monthBirth
                age--;
            }
        }
        return age;
    }

    /**
     * 描述：标准化日期时间类型的数据，不足两位的补0.
     *
     * @param dateTime 预格式的时间字符串，如:2012-3-2 12:2:20
     * @return String 格式化好的时间字符串，如:2012-03-20 12:02:20
     */
    public static String dateTimeFormat(String dateTime) {
        StringBuilder sb = new StringBuilder();
        try {
            if (StrUtil.isEmpty(dateTime)) {
                return null;
            }
            String[] dateAndTime = dateTime.split(" ");
            if (dateAndTime.length > 0) {
                for (String str : dateAndTime) {
                    if (str.indexOf("-") != -1) {
                        String[] date = str.split("-");
                        for (int i = 0; i < date.length; i++) {
                            String str1 = date[i];
                            sb.append(StrUtil.strFormatTwoPlace(str1));
                            if (i < date.length - 1) {
                                sb.append("-");
                            }
                        }
                    } else if (str.indexOf(":") != -1) {
                        sb.append(" ");
                        String[] date = str.split(":");
                        for (int i = 0; i < date.length; i++) {
                            String str1 = date[i];
                            sb.append(StrUtil.strFormatTwoPlace(str1));
                            if (i < date.length - 1) {
                                sb.append(":");
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return sb.toString();
    }

    /**
     * 获取某年第一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Date getFirstDayOfYear(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    /**
     * 获取某年最后一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Date getLastDayOfYear(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();

        return currYearLast;
    }

    /**
     * 获取某年某月第一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Date getFirstDayOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getMinimum(Calendar.DATE));
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    /**
     * 获取某年某月最后一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Date getLastDayOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DATE));
        Date currYearLast = calendar.getTime();

        return currYearLast;
    }

    /**
     * Returns the number of milliseconds that have passed on the specified day.
     *
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static long millisecondsOfDay(int hour, int minute, int second) {
        long ret = 0;

        ret += hour * MILLIS_PER_HOUR;
        ret += minute * MILLIS_PER_MINUTE;
        ret += second * MILLIS_PER_SECOND;
        return ret;
    }

    /**
     * Get the date string with the given time. Accurate to seconds. Default
     * formate "yyyy-MM-dd HH:mm:ss"
     *
     * @param timeStr
     * @return
     */
    public static String getDateStringFromPhpTime(String timeStr) {
        return getDateStringFromPhpTime(timeStr, DATE_FORMAT_Y_M_D_H_M_S);
    }

    /**
     * Get the date string with the given time and formate. Accurate to seconds.
     *
     * @param timeStr
     * @param formate
     * @return
     */
    public static String getDateStringFromPhpTime(String timeStr, String formate) {
        if (StrUtil.isEmpty(timeStr)) {
            return null;
        }
        return getDateStringWithFormate(getDateFromPhpTime(Long.valueOf(timeStr)), formate);
    }

    /**
     * Get the date string with the given time and formate. Accurate to seconds.
     *
     * @param timeStr
     * @param formate
     * @param timeDiff
     * @return
     */
    public static String getDateStringFromPhpTime(String timeStr, String formate, long timeDiff) {
        if (StrUtil.isEmpty(timeStr)) {
            return null;
        }
        return getDateStringWithFormate(getDateFromPhpTime(Long.valueOf(timeStr), timeDiff), formate);
    }

    /**
     * Get the date string with the given time. Accurate to seconds. Default
     * formate  "yyyy-MM-dd HH:mm:ss"
     *
     * @param time
     * @return
     */
    public static String getDateStringFromPhpTime(long time) {
        return getDateStringWithFormate(getDateFromPhpTime(time), DATE_FORMAT_Y_M_D_H_M_S);
    }

    /**
     * Get the date string with the given time and formate. Accurate to seconds.
     *
     * @param time
     * @param formate
     * @return
     */
    public static String getDateStringFromPhpTime(long time, String formate) {
        return getDateStringWithFormate(getDateFromPhpTime(time), formate);
    }

    /**
     * Get the date string with the given time and formate. Accurate to seconds.
     *
     * @param time
     * @param formate
     * @return
     */
    public static String getDateStringFromPhpTime(long time, String formate, long timeDiff) {
        return getDateStringWithFormate(getDateFromPhpTime(time, timeDiff), formate);
    }

    /**
     * Get the date with the given time. Accurate to seconds.
     *
     * @param time
     * @return
     */
    public static Date getDateFromPhpTime(long time) {
        return getDateFromPhpTime(time, 0);
    }

    /**
     * Get the date with the given time. Accurate to seconds.
     *
     * @param time
     * @param timeDiff
     * @return
     */
    public static Date getDateFromPhpTime(long time, long timeDiff) {

        if (time == 0) {
            return null;
        }
        Date result = null;
        try {
            result = new Date(time * MILLIS_PER_SECOND + timeDiff);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Get the date with the given time. Accurate to seconds.
     *
     * @param timeStr
     * @return
     */
    public static Date getDateFromPhpTime(String timeStr) {
        return getDateFromPhpTime(timeStr, 0);
    }

    /**
     * Get the date with the given time. Accurate to seconds.
     *
     * @param timeStr
     * @param timeDiff
     * @return
     */
    public static Date getDateFromPhpTime(String timeStr, long timeDiff) {

        if (StrUtil.isEmpty(timeStr)) {
            return null;
        }
        Date result = null;
        try {
            result = new Date(Long.valueOf(timeStr) * MILLIS_PER_SECOND + timeDiff);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<Date> convertDateListBySecondTime(List<Long> list) {
        List<Date> dates = new ArrayList<Date>();
        if (list != null) {
            for (Long time : list) {
                dates.add(new Date(time * 1000));
            }
        }
        return dates;
    }

    public static List<Long> convertSecondTimeListByDate(List<Date> list) {
        List<Long> dates = new ArrayList<Long>();
        if (list != null) {
            for (Date date : list) {
                dates.add(date.getTime() / 1000);
            }
        }
        return dates;
    }

    /**
     * Given a date string list with the given formate.
     *
     * @param list
     * @return
     */
    public static List<Date> convertDateListByStringTime(List<String> list, String formate) {
        List<Date> dates = new ArrayList<Date>();
        if (list != null) {
            for (String time : list) {
                Date date = getDateFromString(time, formate);
                if (date != null) {
                    dates.add(date);
                }
            }
        }
        return dates;
    }

    /**
     * Convert the date list to String list with the given formate.
     *
     * @param list
     * @param formate
     * @return
     */
    public static List<String> convertStringTimeListByDate(List<Date> list, String formate) {
        List<String> dates = new ArrayList<String>();
        if (list != null) {
            for (Date date : list) {
                dates.add(getDateStringWithFormate(date, formate));
            }
        }
        return dates;
    }

    /**
     * 秒转换为HH:MM:SS
     *
     * @param second 秒
     * @return
     */
    public static String secondFormatHHMMSS(long second) {
        int hours = (int) (second / SECONDS_PER_HOUR);
        int minute = (int) (second % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE;
        second = second - (hours * SECONDS_PER_HOUR) - (minute * SECONDS_PER_MINUTE);
        return getHourAndMinuteAndSecondTime(hours, minute, (int) second);
    }

    /**
     * 判断两个日期是否在同一周
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameWeekDates(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
        if (0 == subYear) {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;
        } else if (1 == subYear && 11 == cal2.get(Calendar.MONTH)) {
            // 如果12月的最后一周横跨来年第一周的话则最后一周即算做来年的第一周
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;
        } else if (-1 == subYear && 11 == cal1.get(Calendar.MONTH)) {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;
        }
        return false;
    }

    public static boolean isSameWeek(long startMills, long endMills) {
        return getDaySpace(startMills, endMills) <= 7;
    }
}
