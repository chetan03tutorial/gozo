package com.lbg.ib.api.sales.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by Rohit Soni on 12/03/2018.
 * PCA-6411,6523
 * Date util class
 */

public class DateUtil {

    final private static String DATE_AS_STRING_FORMAT = "yyyy-MM-dd HH:mm:ss.s";
    final private static String DATE_ONLY_AS_STRING_FORMAT = "dd MMM YYYY";

    public static String getCurrentUKDateAsString(boolean isAppendDummyTimeStamp){
        Calendar londonCalendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("Europe/London"));
        if(isAppendDummyTimeStamp){
            londonCalendar.set(Calendar.HOUR_OF_DAY,0);
            londonCalendar.set(Calendar.MINUTE,0);
            londonCalendar.set(Calendar.SECOND,0);
            londonCalendar.set(Calendar.AM,0);
        }
        String dateOnly = new SimpleDateFormat(DATE_AS_STRING_FORMAT).format(londonCalendar.getTime());
        return dateOnly;
    }

    public static boolean isPastDate(Date toBeChecked) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        final Date currentDate = df.parse(getCurrentUKDateAsString(true));
        return toBeChecked.before(currentDate);
    }

    public static String getCurrentUkDateOnlyAsString(){
        Calendar londonCalendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("Europe/London"));
        String dateWithoutTS = new SimpleDateFormat(DATE_ONLY_AS_STRING_FORMAT).format(londonCalendar.getTime());
        return dateWithoutTS;
    }
}
