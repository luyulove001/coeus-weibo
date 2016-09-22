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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sina.weibo.sdk.openapi.models.Comment;
import com.sina.weibo.sdk.openapi.models.CommentList;

import net.tatans.coeus.weibo.R;
import net.tatans.coeus.weibo.util.Const;
import net.tatans.coeus.weibo.util.HomeSpan;
import net.tatans.coeus.weibo.util.TimeFormat;

import java.util.ArrayList;
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
            holder.home_page_he_rela = (LinearLayout) convertView.findViewById(R.id.home_page_he_rela);
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
        holder.home_page_he_rela.setVisibility(View.GONE);
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
        private LinearLayout home_page_he_rela;
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
