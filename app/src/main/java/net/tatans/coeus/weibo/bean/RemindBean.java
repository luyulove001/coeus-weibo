package net.tatans.coeus.weibo.bean;

import com.sina.weibo.sdk.openapi.models.Geo;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.User;

/**
 * Created by LCM on 2016/8/1. 12:53
 * @我的
 */

public class RemindBean {

    /** 用户UID（int64） */
    private  String  userId;

    /** 微博创建时间 */
    private String created_at;
    /** 微博ID */
    private String weiboId;
    /** 微博MID */
    private String mid;
    /** 字符串型的微博ID */
    private String idstr;
    /** 微博信息内容 */
    private String text;
    /** 微博来源 */
    private String source;
    /** 是否已收藏，true：是，false：否  */
    private boolean favorited;
    /** 是否被截断，true：是，false：否 */
    private boolean truncated;
    /**（暂未支持）回复ID */
    private String in_reply_to_status_id;
    /**（暂未支持）回复人UID */
    private String in_reply_to_user_id;
    /**（暂未支持）回复人昵称 */
    private String in_reply_to_screen_name;
    /** 缩略图片地址（小图），没有时不返回此字段 */
    private String thumbnail_pic;
    /** 中等尺寸图片地址（中图），没有时不返回此字段 */
    private String bmiddle_pic;
    /** 原始图片地址（原图），没有时不返回此字段 */
    private String original_pic;
    /** 转发数 */
    private int reposts_count;
    /** 评论数 */
    private int comments_count;
    /** 表态数 */
    private int attitudes_count;
    /** 用户昵称 */
    private String screen_name;
    /** 友好显示名称 */
    public String name;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getWeiboId() {
        return weiboId;
    }

    public void setWeiboId(String weiboId) {
        this.weiboId = weiboId;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getIdstr() {
        return idstr;
    }

    public void setIdstr(String idstr) {
        this.idstr = idstr;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public boolean isTruncated() {
        return truncated;
    }

    public void setTruncated(boolean truncated) {
        this.truncated = truncated;
    }

    public String getIn_reply_to_status_id() {
        return in_reply_to_status_id;
    }

    public void setIn_reply_to_status_id(String in_reply_to_status_id) {
        this.in_reply_to_status_id = in_reply_to_status_id;
    }

    public String getIn_reply_to_user_id() {
        return in_reply_to_user_id;
    }

    public void setIn_reply_to_user_id(String in_reply_to_user_id) {
        this.in_reply_to_user_id = in_reply_to_user_id;
    }

    public String getIn_reply_to_screen_name() {
        return in_reply_to_screen_name;
    }

    public void setIn_reply_to_screen_name(String in_reply_to_screen_name) {
        this.in_reply_to_screen_name = in_reply_to_screen_name;
    }

    public String getThumbnail_pic() {
        return thumbnail_pic;
    }

    public void setThumbnail_pic(String thumbnail_pic) {
        this.thumbnail_pic = thumbnail_pic;
    }

    public String getBmiddle_pic() {
        return bmiddle_pic;
    }

    public void setBmiddle_pic(String bmiddle_pic) {
        this.bmiddle_pic = bmiddle_pic;
    }

    public String getOriginal_pic() {
        return original_pic;
    }

    public void setOriginal_pic(String original_pic) {
        this.original_pic = original_pic;
    }

    public int getReposts_count() {
        return reposts_count;
    }

    public void setReposts_count(int reposts_count) {
        this.reposts_count = reposts_count;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public int getAttitudes_count() {
        return attitudes_count;
    }

    public void setAttitudes_count(int attitudes_count) {
        this.attitudes_count = attitudes_count;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
