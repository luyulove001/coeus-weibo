package net.tatans.coeus.weibo.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sina.weibo.sdk.openapi.models.Comment;
import com.sina.weibo.sdk.openapi.models.CommentList;
import com.sina.weibo.sdk.openapi.models.Status;
import com.squareup.picasso.Picasso;

import net.tatans.coeus.weibo.R;
import net.tatans.coeus.weibo.util.Const;
import net.tatans.coeus.weibo.util.HomeSpan;
import net.tatans.coeus.weibo.util.TimeFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Created by LCM on 2016/8/11. 13:10
 * 查看一条微博的所有评论
 */

public class AllCommentAdapter extends BaseAdapter {

    private Context mContext;
    private CommentList mList;
    public ArrayList<String> pic_urls;
    public AllCommentAdapter(Context context, CommentList list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.commentList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.commentList.get(position);
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
            holder.home_page_me_relaytive = (RelativeLayout) convertView.findViewById(R.id.home_page_me_relaytive);
            holder.home_page_he_relaytive = (RelativeLayout) convertView.findViewById(R.id.home_page_he_relaytive);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Comment comment = mList.commentList.get(position);
        Matcher mt_me = Const.pattern.matcher(comment.text);
        holder.home_page_usercontent.setText(comment.text);
        String str = comment.text;
        while (mt_me.find()) {
            String mgroup = mt_me.group(0);
            str = str.replace(mgroup, "网页链接");
            SpannableString spannableString = new SpannableString(str);
            spannableString.setSpan(HomeSpan.getInstance(mgroup, mContext), str.indexOf("网页链接"), str.indexOf("网页链接") + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.home_page_usercontent.setText(spannableString);
            holder.home_page_usercontent.setMovementMethod(LinkMovementMethod.getInstance());
        }
        String time = TimeFormat.dTime(comment.created_at);
        holder.home_page_username.setText(comment.user.name);
        holder.home_page_usertime.setText(time);
        holder.home_page_me_relaytive.setVisibility(View.GONE);
        holder.home_page_he_relaytive.setVisibility(View.GONE);
//        if (comment.status.original_pic.equals("") || comment.status.original_pic == "") {
//            holder.home_page_pic.setVisibility(View.GONE);
//            holder.home_page_pic_text.setVisibility(View.GONE);
//        } else {
//            pic_urls = comment.status.pic_urls;
//            holder.home_page_pic_text.setVisibility(View.VISIBLE);
//            holder.home_page_pic.setVisibility(View.VISIBLE);
//            Picasso.with(mContext).load(comment.status.original_pic).resize(100, 100).placeholder(R.drawable.icon_image_model_short).resize(100, 100).error(R.drawable.icon_image_model_short).resize(100, 100).into(holder.home_page_pic);
//            holder.home_page_pic_text.setText(comment.status.pic_urls.size() + "张图片点击查看");
//        }
//        /**这里主要对转发的处理*/
//        if (comment.status.retweeted_status == null) {
//            holder.home_page_he_user.setVisibility(View.GONE);
//            holder.home_page_usercomments.setVisibility(View.GONE);
//            holder.home_page_he_pic.setVisibility(View.GONE);
//            holder.home_page_he_pic_text.setVisibility(View.GONE);
//        } else {
//            holder.home_page_usercomments.setVisibility(View.VISIBLE);
//            /**转发的微博被删除情况*/
//            if (comment.status.retweeted_status.user == null) {
//                holder.home_page_he_user.setVisibility(View.GONE);
//                holder.home_page_he_pic.setVisibility(View.GONE);
//                holder.home_page_he_pic_text.setVisibility(View.GONE);
//                holder.home_page_usercomments.setText("抱歉，此微博已被作者删除。");
//            } else {
//                holder.home_page_he_user.setVisibility(View.VISIBLE);
//                holder.home_page_he_user.setText("@" + comment.status.retweeted_status.user.name + ":");
//
//                holder.home_page_usercomments.setText(comment.status.retweeted_status.text);
//                Matcher mt_he = Const.pattern.matcher(comment.status.retweeted_status.text);
//                String strs = comment.status.retweeted_status.text;
//                while (mt_he.find()) {
//                    String mgroup = mt_he.group(0);
//                    strs = strs.replace(mgroup, "网页链接");
//                    SpannableString spannableString = new SpannableString(strs);
//                    spannableString.setSpan(HomeSpan.getInstance(mgroup, mContext), strs.indexOf("网页链接"), strs.indexOf("网页链接") + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    holder.home_page_usercomments.setText(spannableString);
//                    holder.home_page_usercomments.setMovementMethod(LinkMovementMethod.getInstance());
//                }
//                if (comment.status.retweeted_status.original_pic == null) {
//                    holder.home_page_he_pic.setVisibility(View.GONE);
//                    holder.home_page_he_pic_text.setVisibility(View.GONE);
//                } else {
//                    holder.home_page_he_pic.setVisibility(View.VISIBLE);
//                    holder.home_page_he_pic_text.setVisibility(View.VISIBLE);
//                    //该判断为转发后用户信息图片或者是投票或是音乐
//                    if (comment.status.retweeted_status.original_pic.equals("")) {
//                        holder.home_page_he_pic.setVisibility(View.GONE);
//                        holder.home_page_he_pic_text.setVisibility(View.GONE);
//                    } else {
//                        Picasso.with(mContext).load(comment.status.retweeted_status.original_pic).resize(100, 100).placeholder(R.drawable.icon_image_model_short).resize(100, 100).error(R.drawable.icon_image_model_short).resize(100, 100).into(holder.home_page_he_pic);
//                        holder.home_page_he_pic_text.setText(comment.status.retweeted_status.pic_urls.size() + "张图片点击查看");
//                    }
//                }
//            }
//        }
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
        private RelativeLayout home_page_he_relaytive;
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
}
