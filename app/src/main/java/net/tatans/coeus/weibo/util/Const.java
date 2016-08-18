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

    public static String REPLY = "reply";
    public static String WRITE_COMMENT = "WriteComment";
    public static String REMIND= "REMIND";
    public static String MY_HOME_PAGE= "myHomePage";
    public static String SEARCH= "SEARCH";
    public static String HOME= "HOME";
    public static String WEIBO_COMMENT = "WEIBO_COMMENT";
    public static String WEIBO_FORWARD = "WEIBO_FORWARD";
    public static String WEIBO_FAVORITE = "WEIBO_FAVORITE";
    public static String UID = "uid";
    public static String SCREEN_NAME = "screen_name";
    public static String WEIBO_ID = "weiboId";
    public static String REPOSTS_COUNT = "reposts_count";
    public static String COMMENTS_COUNT = "comments_count";
    public static String FAVORITES = "favorites";
    public static String COMMENT_OR_REMIND = "comment_or_remind";
    public static String COMMENT = "comment";


}
