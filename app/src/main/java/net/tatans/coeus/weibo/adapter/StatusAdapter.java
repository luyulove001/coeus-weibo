package net.tatans.coeus.weibo.adapter;


import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.squareup.picasso.Picasso;

import net.tatans.coeus.weibo.R;
import net.tatans.coeus.weibo.activity.ImagesActivity;
import net.tatans.coeus.weibo.activity.WeiboMenuDetailsActivity;
import net.tatans.coeus.weibo.util.Const;
import net.tatans.coeus.weibo.util.HomeSpan;
import net.tatans.coeus.weibo.util.TimeFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.regex.Matcher;

/**
 * Created by Administrator on 2016/7/19.
 */
public class StatusAdapter extends BaseAdapter {

    private Context mContext;
    private StatusList statusList;
    public ArrayList<String> pic_urls;
    //用于判断来自哪个界面
    private String isComefrom;

    public StatusAdapter(Context context, StatusList list, String comefrom) {
        this.mContext = context;
        this.statusList = list;
        this.isComefrom = comefrom;
    }

    @Override
    public int getCount() {
        return statusList.statusList.size();
    }

    @Override
    public Object getItem(int position) {
        return statusList.statusList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.home_page_fragment_item, null);
            holder.home_page_username = (TextView) convertView.findViewById(R.id.home_page_username);
            holder.home_page_usertime = (TextView) convertView.findViewById(R.id.home_page_usertime);
            holder.home_page_usercontent = (TextView) convertView.findViewById(R.id.home_page_usercontent);
            holder.home_page_pic = (ImageView) convertView.findViewById(R.id.home_page_pic);
            holder.home_page_pic_text = (TextView) convertView.findViewById(R.id.home_page_pic_text);
            holder.home_page_me_relaytive = (LinearLayout) convertView.findViewById(R.id.home_page_me_relaytive);
            holder.home_page_usercomments = (TextView) convertView.findViewById(R.id.home_page_usercomments);
            holder.home_page_he_user = (TextView) convertView.findViewById(R.id.home_page_he_user);
            holder.home_page_he_pic = (ImageView) convertView.findViewById(R.id.home_page_he_pic);
            holder.home_page_he_pic_text = (TextView) convertView.findViewById(R.id.home_page_he_pic_text);
            holder.home_page_me_relaytive = (LinearLayout) convertView.findViewById(R.id.home_page_me_relaytive);
            holder.home_page_he_relaytive = (LinearLayout) convertView.findViewById(R.id.home_page_he_relaytive);
            holder.home_page_head = (LinearLayout) convertView.findViewById(R.id.home_page_head);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Status status = statusList.statusList.get(position);
        Matcher mt_me = Const.pattern.matcher(status.text);
        holder.home_page_usercontent.setText(status.text);
        String str = status.text;
        while (mt_me.find()) {
            String mgroup = mt_me.group(0);
            str = str.replace(mgroup, "网页链接");
            SpannableString spannableString = new SpannableString(str);
            spannableString.setSpan(HomeSpan.getInstance(mgroup, mContext), str.indexOf("网页链接"), str.indexOf("网页链接") + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.home_page_usercontent.setText(spannableString);
            holder.home_page_usercontent.setMovementMethod(LinkMovementMethod.getInstance());
        }
        String time = TimeFormat.dTime(status.created_at);
        holder.home_page_username.setText(status.user.screen_name);
        holder.home_page_usertime.setText(time);

        if (status.original_pic == null || status.original_pic.equals("") || status.original_pic == "") {
            holder.home_page_pic.setVisibility(View.GONE);
            holder.home_page_pic_text.setVisibility(View.GONE);
        } else {
            pic_urls = status.pic_urls;
            holder.home_page_pic_text.setVisibility(View.VISIBLE);
            holder.home_page_pic.setVisibility(View.VISIBLE);
            Picasso.with(mContext).load(status.original_pic).resize(100, 100).placeholder(R.drawable.icon_image_model_short).resize(100, 100).error(R.drawable.icon_image_model_short).resize(100, 100).into(holder.home_page_pic);
            holder.home_page_pic_text.setText(status.pic_urls.size() + "张图片点击查看");
        }
        /**这里主要对转发的处理*/
        if (status.retweeted_status == null) {
            holder.home_page_he_user.setVisibility(View.GONE);
            holder.home_page_usercomments.setVisibility(View.GONE);
            holder.home_page_he_pic.setVisibility(View.GONE);
            holder.home_page_he_pic_text.setVisibility(View.GONE);
        } else {
            holder.home_page_usercomments.setVisibility(View.VISIBLE);
            /**转发的微博被删除情况*/
            if (status.retweeted_status.user == null) {
                holder.home_page_he_user.setVisibility(View.GONE);
                holder.home_page_he_pic.setVisibility(View.GONE);
                holder.home_page_he_pic_text.setVisibility(View.GONE);
                holder.home_page_usercomments.setText("抱歉，此微博已被作者删除。");
            } else {
                holder.home_page_he_user.setVisibility(View.VISIBLE);
                holder.home_page_he_user.setText("@" + status.retweeted_status.user.screen_name + ":");
                holder.home_page_usercomments.setText(status.retweeted_status.text);
                Matcher mt_he = Const.pattern.matcher(status.retweeted_status.text);
                String strs = status.retweeted_status.text;
                while (mt_he.find()) {
                    String mgroup = mt_he.group(0);
                    strs = strs.replace(mgroup, "网页链接");
                    SpannableString spannableString = new SpannableString(strs);
                    spannableString.setSpan(HomeSpan.getInstance(mgroup, mContext), strs.indexOf("网页链接"), strs.indexOf("网页链接") + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    holder.home_page_usercomments.setText(spannableString);
                    holder.home_page_usercomments.setMovementMethod(LinkMovementMethod.getInstance());
                }
                if (status.retweeted_status.original_pic == null) {
                    holder.home_page_he_pic.setVisibility(View.GONE);
                    holder.home_page_he_pic_text.setVisibility(View.GONE);
                } else {
                    holder.home_page_he_pic.setVisibility(View.VISIBLE);
                    holder.home_page_he_pic_text.setVisibility(View.VISIBLE);
                    //该判断为转发后用户信息图片或者是投票或是音乐
                    if (status.retweeted_status.original_pic.equals("")) {
                        holder.home_page_he_pic.setVisibility(View.GONE);
                        holder.home_page_he_pic_text.setVisibility(View.GONE);
                    } else {
                        Picasso.with(mContext).load(status.retweeted_status.original_pic).resize(100, 100).placeholder(R.drawable.icon_image_model_short).resize(100, 100).error(R.drawable.icon_image_model_short).resize(100, 100).into(holder.home_page_he_pic);
                        if (!(status.retweeted_status.pic_urls == null))
                            holder.home_page_he_pic_text.setText(status.retweeted_status.pic_urls.size() + "张图片点击查看");
                    }
                }
            }
        }
        holder.home_page_me_relaytive.setOnClickListener(new OnClickListenerIml(position));
        holder.home_page_he_relaytive.setOnClickListener(new OnClickListenerIml(position));
        holder.home_page_head.setOnClickListener(new OnClickListenerIml(position));
        return convertView;
    }

    class ViewHolder {

        private LinearLayout home_page_head;
        /**
         * 用户名
         */
        private TextView home_page_username;
        /**
         * 时间
         */
        private TextView home_page_usertime;
        /**
         * 内容
         */
        private TextView home_page_usercontent;
        /**
         * 图片
         */
        private ImageView home_page_pic;
        private TextView home_page_pic_text;
        private LinearLayout home_page_me_relaytive;
        private LinearLayout home_page_he_relaytive;
        /**
         * 转发用户名
         */
        private TextView home_page_he_user;
        /**
         * 转发用户内容
         */
        private TextView home_page_usercomments;
        /**
         * 转发的图片
         */
        private ImageView home_page_he_pic;
        private TextView home_page_he_pic_text;
    }


    private class OnClickListenerIml implements View.OnClickListener {
        private int mPosition;

        public OnClickListenerIml(int position) {
            this.mPosition = position;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            Status status = statusList.statusList.get(mPosition);
            switch (v.getId()) {
                case R.id.home_page_he_relaytive://转发微博
                    ImagesStart("forward", status);
                    break;
                case R.id.home_page_me_relaytive://不是转发微博
                    ImagesStart("original", status);
                    break;
                case R.id.home_page_head://点击微博进入菜单详情
                    intent.setClass(mContext, WeiboMenuDetailsActivity.class);
                    intent.putExtra("userInfo", (Serializable) status.user);
                    if (isComefrom.equals(Const.REMIND)) {
                        intent.putExtra(Const.TYPE, Const.REMIND);
                    } else if (isComefrom.equals(Const.MY_HOME_PAGE)) {
                        intent.putExtra(Const.TYPE, Const.MY_HOME_PAGE);
                    } else if (isComefrom.equals(Const.SEARCH)) {
                        intent.putExtra(Const.TYPE, Const.SEARCH);
                    } else if (isComefrom.equals(Const.HOME)) {
                        intent.putExtra(Const.TYPE, Const.HOME);
                    }
                    if (status != null) {
                        intent.putExtra(Const.REPOSTS_COUNT, status.reposts_count);
                        intent.putExtra(Const.COMMENTS_COUNT, status.comments_count);
                        intent.putExtra(Const.UID, status.user.id);
                        intent.putExtra(Const.SCREEN_NAME, status.user.screen_name);
                        intent.putExtra(Const.WEIBO_ID, status.id);
                        intent.putExtra(Const.FAVORITES, status.favorited);
                    }
                    mContext.startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 点击图片看
     */
    private void ImagesStart(String oglAndFwd, Status status) {
        Intent intent = new Intent();
        intent.setClass(mContext, ImagesActivity.class);
        if (oglAndFwd.equals("original")) {
            intent.putStringArrayListExtra(Const.PICURLS, status.pic_urls);
        } else if (oglAndFwd.equals("forward")) {
            intent.putStringArrayListExtra(Const.PICURLS, status.retweeted_status.pic_urls);
        }
        mContext.startActivity(intent);
    }

}
