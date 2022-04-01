package com.algo4chris.algo4chriscommon.utils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public final class FormatUtil {
    private static final NumberFormat numberFormat;

    static {
        numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        numberFormat.setMinimumFractionDigits(2);
    }

    private FormatUtil() {
    }

    public static Date parseDateTime(String dateTime) throws ParseException {
        if (dateTime == null || dateTime.trim().length() == 0) {
            return null;
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.parse(dateTime);
    }

    public static String formatDateTime(Date date) {
        if (date == null) {
            return "";
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    public static String formatDateTime() {
        return formatDateTime(new Date());
    }

    public static String formatDateTimeCompact(Date date) {
        if (date == null) {
            return "";
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(date);
    }

    public static String formatDateTimeCompact() {
        return formatDateTimeCompact(new Date());
    }

    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    public static String formatMoney(BigDecimal value) {
        if (value == null) {
            value = BigDecimal.ZERO;
        }
        return formatMoney(value.doubleValue());
    }

    public static String formatMoney(Double value) {
        if (value == null || value.equals(Double.NaN)) {
            value = 0.0;
        }
        return numberFormat.format(value / 100);
    }
    
    /**
     * Date类型转换为10位时间戳
     * 
     * @param time
     * @return
     */
    public static Integer dateToTimestamp(Date time){
    	if(time == null) {
    		return new Integer(0);
    	}
        Timestamp ts = new Timestamp(time.getTime());
        return (int) ((ts.getTime())/1000);
    }
    
    /**
     * 10位时间戳转Date
     * 
     * @param time
     * @return
     */
    public static Date timestampToDate(Integer time){
    	if(time == null) {
    		return null;
    	}
        long temp = (long)time*1000;
        Timestamp ts = new Timestamp(temp);  
        Date date = new Date();  
        try {  
            date = ts;  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return date;
    }

    /**
     * Date类型转换为13位时间戳
     *
     * @param time
     * @return
     */
    public static Integer dateToTimestamp13Digits(Date time){
        if(time == null) {
            return new Integer(0);
        }
        Timestamp ts = new Timestamp(time.getTime());
        return (int) (ts.getTime());
    }

    /**
     * 13位时间戳转Date
     *
     * @param time
     * @return
     */
    public static Date timestampToDate13Digits(Integer time){
        if(time == null) {
            return null;
        }
        long temp = (long)time;
        Timestamp ts = new Timestamp(temp);
        Date date = new Date();
        try {
            date = ts;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }
    
    /**
     * UTC时间戳
     * 
     * @return
     * @throws Exception
     */
	public static String getUTCTimeStr() throws Exception {
		Calendar cal = Calendar.getInstance();
		TimeZone tz = TimeZone.getTimeZone("GMT");
		cal.setTimeZone(tz);
		// 返回的UTC时间戳
		return String.valueOf(cal.getTimeInMillis());
	}
}
