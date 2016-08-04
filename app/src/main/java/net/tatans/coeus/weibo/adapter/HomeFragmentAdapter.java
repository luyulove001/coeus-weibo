package net.tatans.coeus.weibo.adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.squareup.picasso.Picasso;

import net.tatans.coeus.weibo.R;
import net.tatans.coeus.weibo.util.TimeFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/7/19.
 */
public class HomeFragmentAdapter extends BaseAdapter {

    private Context mContext;
    private List<StatusList> mlist;
    public ArrayList<String> pic_urls;
    private String me_urlhttp = null;
    private String he_urlhttp = null;
    private Oauth2AccessToken mAccessToken;

    //    private Pattern pattern= Pattern
//            .compile("(http://|ftp://|https://|www){1}([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?");
//正则表达式匹配规则
    private Pattern pattern = Pattern
            .compile("(http://|ftp://|https://|www){1}([a-zA-Z0-9.]+/|[a-zA-Z0-9.]+)*");

    public HomeFragmentAdapter(Context context, List<StatusList> list, Oauth2AccessToken AccessToken) {
        this.mContext = context;
        this.mlist = list;
        mAccessToken = AccessToken;
    }

    @Override
    public int getCount() {
        return mlist.get(0).statusList.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(0).statusList.get(position);
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
            holder.home_page_me_relaytive = (RelativeLayout) convertView.findViewById(R.id.home_page_me_relaytive);
            holder.home_page_usercomments = (TextView) convertView.findViewById(R.id.home_page_usercomments);
            holder.home_page_he_user = (TextView) convertView.findViewById(R.id.home_page_he_user);
            holder.home_page_he_pic = (ImageView) convertView.findViewById(R.id.home_page_he_pic);
            holder.home_page_he_pic_text = (TextView) convertView.findViewById(R.id.home_page_he_pic_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Status status = mlist.get(0).statusList.get(position);
        Matcher mt_me = pattern.matcher(status.text);
        holder.home_page_usercontent.setText(status.text);
        String str = status.text;
        while (mt_me.find()) {
            String mgroup = mt_me.group(0);
            str = str.replace(mgroup, "网页链接");
            SpannableString spannableString = new SpannableString(str);
            spannableString.setSpan(new HomeSpan(mgroup), str.indexOf("网页链接"), str.indexOf("网页链接") + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.home_page_usercontent.setText(spannableString);
            holder.home_page_usercontent.setMovementMethod(LinkMovementMethod.getInstance());
        }
        String time = TimeFormat.dTime(status.created_at);
        holder.home_page_username.setText(status.user.name);
        holder.home_page_usertime.setText(time);

        if (status.original_pic.equals("") || status.original_pic == "") {
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
                holder.home_page_he_user.setText("@" + status.retweeted_status.user.name + ":");

                holder.home_page_usercomments.setText(status.retweeted_status.text);
                Matcher mt_he = pattern.matcher(status.retweeted_status.text);
                String strs = status.retweeted_status.text;
                while (mt_he.find()) {
                    String mgroup = mt_he.group(0);
                    strs = strs.replace(mgroup, "网页链接");
                    SpannableString spannableString = new SpannableString(strs);
                    spannableString.setSpan(new HomeSpan(mgroup), strs.indexOf("网页链接"), strs.indexOf("网页链接") + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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
                        holder.home_page_he_pic_text.setText(status.retweeted_status.pic_urls.size() + "张图片点击查看");
                    }
                }
            }

        }
        return convertView;
    }

    class ViewHolder {
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
        private RelativeLayout home_page_me_relaytive;
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

    class HomeSpan extends ClickableSpan {
        String mtext;

        public HomeSpan(String text) {
            this.mtext = text;

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(Color.parseColor("#ff0000"));
            ds.setUnderlineText(true);
        }

        @Override
        public void onClick(View widget) {
            String vvv = mtext;
            System.out.println(vvv);
            final Uri uri = Uri.parse(mtext);
            final Intent it = new Intent(Intent.ACTION_VIEW, uri);
            mContext.startActivity(it);
        }
    }
}
