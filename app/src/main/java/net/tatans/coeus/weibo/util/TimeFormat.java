package net.tatans.coeus.weibo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by LCM on 2016/7/27. 14:26
 */

public class TimeFormat {
    /**
     * 时间转化
     *
     * @param time
     * @return
     */
    public static String dTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.US);//MMM dd hh:mm:ss Z yyyy
        Date date;
        Calendar current = Calendar.getInstance();
        int year = current.get(Calendar.YEAR);
        int today = current.get(Calendar.DAY_OF_MONTH);
        int yesterday = current.get(Calendar.DAY_OF_MONTH) - 1;
        try {
            date = sdf.parse(time);
        } catch (ParseException ex) {
            try {
                sdf = new SimpleDateFormat("MM-dd HH:mm", Locale.US);
                date = sdf.parse(time);
            } catch (ParseException e) {
                return time;
            }
        }
        current.setTime(date);
        current.set(Calendar.YEAR, year);
        sdf = new SimpleDateFormat("MM-dd HH:mm", Locale.US);
        String str = sdf.format(current.getTime());
        if (current.get(Calendar.DAY_OF_MONTH) == today) {
            return str.split(" ")[1];
        } else if (current.get(Calendar.DAY_OF_MONTH) == yesterday) {
            return "昨天 " + str.split(" ")[1];
        } else if (current.get(Calendar.YEAR) < year) {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
            return sdf.format(current.getTime());
        } else {
            return str;
        }
    }
}
