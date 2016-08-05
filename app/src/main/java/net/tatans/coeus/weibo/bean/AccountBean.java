package net.tatans.coeus.weibo.bean;

import com.sina.weibo.sdk.openapi.models.User;

import java.io.Serializable;

/**
 * Created by cly on 16/8/3.
 */
public class AccountBean implements Serializable {

    private static final long serialVersionUID = -8974889891999245818L;

    private String uid;

    private String account;

    private String password;

    private String cookie;// 网页授权的cookie

    private User user;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }
}
