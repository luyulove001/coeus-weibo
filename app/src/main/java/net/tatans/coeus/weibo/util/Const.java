package net.tatans.coeus.weibo.util;


import java.util.regex.Pattern;

/**
 * Created by LCM on 2016/7/25. 10:45
 * 常量类
 */

public class Const {

    public static String TYPE = "type";
    public static String WRITE_WEIBO = "WriteWeibo";
    public static String CONTACT = "contact";
    public static String CONTACT_OR_FOllOW = "ContactOrFollow";
    public static String FOLLOW = "follow";
    //正则表达式
    public static Pattern pattern = Pattern
            .compile("(http://|ftp://|https://|www){1}([a-zA-Z0-9.]+/|[a-zA-Z0-9.]+)*");

    public static String PICURLS = "pic_urls";
}
