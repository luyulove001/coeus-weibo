package net.tatans.coeus.weibo.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.squareup.picasso.Picasso;

import net.tatans.coeus.speech.dto.UnderstandResultDto;
import net.tatans.coeus.weibo.R;
import net.tatans.coeus.weibo.util.NewHomeUtil;
import net.tatans.coeus.weibo.util.TimeFormat;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Administrator on 2016/7/19.
 */
public class HomeFragmentAdapter extends BaseAdapter {

    private Context mContext;
    private List<StatusList> mlist;
    public ArrayList<String> pic_urls;

    public HomeFragmentAdapter(Context context, List<StatusList> list) {
        this.mContext = context;
        this.mlist = list;
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
        Status status = mlist.get(0).statusList.get(position);
        String  time = TimeFormat.dTime(status.created_at);
        holder.home_page_username.setText(status.user.name);
        holder.home_page_usertime.setText(time);
        holder.home_page_usercontent.setText(status.text);
        if (status.original_pic.equals("") || status.original_pic == "") {
            holder.home_page_pic.setVisibility(View.GONE);
            holder.home_page_pic_text.setVisibility(View.GONE);
        } else {
            pic_urls = status.pic_urls;
            int nums = pic_urls.size();
            System.out.println(nums);
            holder.home_page_pic_text.setVisibility(View.VISIBLE);
            holder.home_page_pic.setVisibility(View.VISIBLE);
            Picasso.with(mContext).load(status.original_pic).resize(200, 200).placeholder(R.drawable.icon_image_model).resize(200, 200).error(R.drawable.icon_image_model).resize(200, 200).into(holder.home_page_pic);
            holder.home_page_pic_text.setText(status.pic_urls.size() + "张图片点击查看");
        }
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
                String vvvvv = status.retweeted_status.user.name;
                String zzzzz = status.retweeted_status.text;
                System.out.println(vvvvv + zzzzz);
                holder.home_page_he_user.setVisibility(View.VISIBLE);
                holder.home_page_he_user.setText("@" + status.retweeted_status.user.name + ":");

                holder.home_page_usercomments.setText(status.retweeted_status.text);

                if (status.retweeted_status.original_pic == null) {
                    holder.home_page_he_pic.setVisibility(View.GONE);
                    holder.home_page_he_pic_text.setVisibility(View.GONE);
                } else {
                    holder.home_page_he_pic.setVisibility(View.VISIBLE);
                    holder.home_page_he_pic_text.setVisibility(View.VISIBLE);
                    if (status.retweeted_status.original_pic.equals("")) {
                        Picasso.with(mContext).load(R.drawable.icon_image_model).resize(200, 200).into(holder.home_page_he_pic);
                    } else {
                        String vvvvvv = status.retweeted_status.original_pic;
                        System.out.println(vvvvvv);
                        Picasso.with(mContext).load(status.retweeted_status.original_pic).resize(200, 200).placeholder(R.drawable.icon_image_model).resize(200, 200).error(R.drawable.icon_image_model).resize(200, 200).into(holder.home_page_he_pic);
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
}
