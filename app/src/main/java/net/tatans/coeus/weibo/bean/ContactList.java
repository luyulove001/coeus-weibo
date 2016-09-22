/*
 * Copyright (C) 2010-2013 The SINA WEIBO Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.tatans.coeus.weibo.bean;

import android.text.TextUtils;

import com.sina.weibo.sdk.openapi.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 微博列表结构。
 *
 * @author SINA
 * @see <a href="http://t.cn/zjM1a2W">常见返回对象数据结构</a>
 * @since 2013-11-22
 */
public class ContactList {

    /**
     * 微博列表
     */
    public ArrayList<User> contactList;
    public User user;
    public boolean hasvisible;
    public String previous_cursor;
    public String next_cursor;
    public int total_number;
    public Object[] advertises;

    public static ContactList parse(String jsonString) {
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }
        ContactList contact = new ContactList();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            contact.hasvisible = jsonObject.optBoolean("hasvisible", false);
            contact.previous_cursor = jsonObject.optString("previous_cursor", "0");
            contact.next_cursor = jsonObject.optString("next_cursor", "0");
            contact.total_number = jsonObject.optInt("total_number", 0);

            JSONArray jsonArray = jsonObject.optJSONArray("users");
            if (jsonArray != null && jsonArray.length() > 0) {
                int length = jsonArray.length();
                contact.contactList = new ArrayList<User>(length);
                for (int ix = 0; ix < length; ix++) {
                    contact.contactList.add(User.parse(jsonArray.getJSONObject(ix)));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return contact;
    }
}
