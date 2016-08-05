package net.tatans.coeus.weibo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by LCM on 2016/7/27. 14:26
 *
 */

public class TimeFormat {
    /**
     * 时间转化
     * @param time
     * @return
     */
    public static  String dTime(String time){
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.US);//MMM dd hh:mm:ss Z yyyy
        Date dates=null;
        try {
            String zz=sdf.parse(time).toString();
            dates= sdf.parse(time);
            sdf = new SimpleDateFormat("-MM-dd ",Locale.US);
        } catch (ParseException ex) {
        }

        String reg=" +";
        String [] arr=time.split(reg);
        String datetime=arr[5]+sdf.format(dates)+arr[3];
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        if (datetime == null || "".equals(datetime)) {
            return "";
        }
        Date date = null;
        try {
            date = format.parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar current = Calendar.getInstance();

        Calendar today = Calendar.getInstance(); // 今天

        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        // Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        Calendar yesterday = Calendar.getInstance(); // 昨天

        yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
        yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
        yesterday.set(Calendar.DAY_OF_MONTH,
                current.get(Calendar.DAY_OF_MONTH) - 1);
        yesterday.set(Calendar.HOUR_OF_DAY, 0);
        yesterday.set(Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);
        current.setTime(date);
        if (current.after(today)) {
            return datetime.split(" ")[1].substring(0,5);
        } else if (current.before(today) && current.after(yesterday)) {

            return "昨天 ";
        } else {
            return datetime.split(" ")[0];
        }
    }
}