package net.tatans.coeus.weibo.bean;

import com.sina.weibo.sdk.openapi.models.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Search suggestions response
 * Created by cly on 2016/7/27.
 */

public class UserSearchBean {
    private String screen_name;
    private int followers_count;
    private long uid;

    public static UserSearchBean parse(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            return UserSearchBean.parse(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static UserSearchBean parse(JSONObject jsonObject) {
        if (null == jsonObject) {
            return null;
        }
        UserSearchBean bean = new UserSearchBean();
        bean.screen_name = jsonObject.optString("screen_name", "");
        bean.followers_count = jsonObject.optInt("followers_count", 0);
        bean.uid = jsonObject.optLong("uid", 0);
        return bean;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public int getFollowers_count() {
        return followers_count;
    }

    public void setFollowers_count(int followers_count) {
        this.followers_count = followers_count;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "UserSearchBean{" +
                "screen_name='" + screen_name + '\'' +
                ", followers_count=" + followers_count +
                ", uid=" + uid +
                '}';
    }
}
