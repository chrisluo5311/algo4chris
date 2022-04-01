package com.algo4chris.algo4chriscommon.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 時間工具類
 *
 * @author Chris
 * @Date 2021/01/29
 * */
@Slf4j
@Component
public class TimeUtil extends org.apache.commons.lang3.time.DateUtils{

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"
    };

    /**
     * 月份與日期
     * @return Map<String,String> Map<月,日>
     *
     * */
    public static Map<String, String> genTodayDate() {
        Map<String, String> dateMap = new HashMap<>();
        // YYYY-MM-DD
        LocalDate now = LocalDate.now();
        dateMap.put(String.valueOf(now.getMonthValue()), String.valueOf(now.getDayOfMonth()));
        return dateMap;
    }

    /**
     * UTC時間戳(秒)
     * @return String
     */
    public static String getUTCTimeStrInSeconds(){
        return String.valueOf(Instant.now().getEpochSecond());
    }

    /**
     * 今日日期(YYYY-MM-DD)
     * @return String
     * */
    public static String formTodayDate() {
        //YYYY-MM-DD
        LocalDate now = LocalDate.now();
        return now.toString();
    }

    /**
     * 客製化日期格式
     * 並可設定往前幾天
     * 不設定可放null值
     *
     * @param format 日期格式 ex. YYYY-MM-dd
     * @param minusDays 減去幾天 long型別 (可空)
     * @return String
     * */
    public static String formCustomDate(String format, Long minusDays){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        if(Objects.nonNull(minusDays)){
            log.info("minusDays:{}",formatter.format(LocalDateTime.now().minusDays(minusDays)));
            return formatter.format(LocalDateTime.now().minusDays(minusDays));
        }
        return formatter.format(LocalDateTime.now());
    }

    /**
     * 提取數字並返回日期
     * 若非日期返回null
     *
     * @param title 下載標題
     * */
    public static String verifyDate(String title){
        Integer index = 0;
        Integer sum   = 0;
        while (Character.isDigit(title.charAt(index))) {
            sum = sum*10 + Character.getNumericValue(title.charAt(index));
            index++;
        }
        return sum==0?null:String.valueOf(sum);
    }

    /**
     * Date 根据 format 转换成文字
     *
     * @param date
     * @param format
     * */
    public static String customDateToString(Date date, String format){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        }
        catch (ParseException e) {
            return null;
        }
    }

}
