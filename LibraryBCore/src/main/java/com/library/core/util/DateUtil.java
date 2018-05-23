package com.library.core.util;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    private DateUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /*
     * 根据不同的dateType 返回不同的日期格式
     *
     * @param dateType
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static DateFormat getDateFormat(int dateType) {
        switch (dateType) {
            case 0:
                return new SimpleDateFormat("yyyyMMdd");
            case 1:
                return new SimpleDateFormat("yyyyMMdd HH:mm:ss");
            case 2:
                return new SimpleDateFormat("yyyy-MM-dd");
            case 3:
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            case 4:
                return new SimpleDateFormat("yyyy/MM/dd");
            case 5:
                return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        }
        return SimpleDateFormat.getDateTimeInstance();
    }

    /*
     * 获取当前的日期 20171118
     *
     * @return
     */
    private static String getCurrentDayDate(DateFormat dateFormat) {
        return dateFormat.format(new Date());
    }

    /*
     * 获取前一天的日期 20171117  2017-11-17  2017/11/17
     *
     * @param currentDayDate
     * @param dateFormat
     */
    public static String getPreDayDate(String currentDayDate, DateFormat dateFormat) {
        if (TextUtils.isEmpty(currentDayDate)) {
            currentDayDate = getCurrentDayDate(dateFormat);
        }
        Calendar calendar = Calendar.getInstance();
        if (currentDayDate.contains("/") || currentDayDate.contains("-")) {
            calendar.set(Integer.parseInt(currentDayDate.substring(0, 4)),
                    Integer.parseInt(currentDayDate.substring(5, 7)),
                    Integer.parseInt(currentDayDate.substring(8, 10)));
        } else {
            calendar.set(Integer.parseInt(currentDayDate.substring(0, 4)),
                    Integer.parseInt(currentDayDate.substring(4, 6)),
                    Integer.parseInt(currentDayDate.substring(6, 8)));
        }
        return dateFormat.format(calendar.getTime());
    }
}
