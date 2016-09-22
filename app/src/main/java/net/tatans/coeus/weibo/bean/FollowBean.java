package net.tatans.coeus.weibo.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by LCM on 2016/8/16. 9:46
 * 用于判断前用户与其他博主的关系 是否关注
 */

public class FollowBean {

    public String id;

    public String screen_name;

    public boolean followed_by;

    public boolean following;


    public static FollowBean parse(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            return FollowBean.parse(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static FollowBean parse(JSONObject jsonObject) {
        if (null == jsonObject) {
            return null;
        }

        FollowBean followBean = new FollowBean();
        String source = jsonObject.optString("source", "");
        try {
            JSONObject jsonSource = new JSONObject(source);
            followBean.id = jsonSource.optString("id", "");
            followBean.screen_name = jsonSource.optString("screen_name", "");
            followBean.followed_by = jsonSource.optBoolean("followed_by", false);
            followBean.following = jsonSource.optBoolean("following", false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return followBean;
    }
}
