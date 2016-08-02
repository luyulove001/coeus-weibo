package net.tatans.coeus.weibo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.tatans.coeus.weibo.R;
import net.tatans.coeus.weibo.bean.RemindBean;
import net.tatans.coeus.weibo.util.TimeFormat;

import java.util.List;

/**
 * Created by LCM on 2016/8/1. 12:46
 */

public class RemindAdapter extends BaseAdapter {
    private Context mContext;

    private List<RemindBean> mRemindBean;

    public RemindAdapter(Context context, List<RemindBean> list) {
        this.mContext = context;
        this.mRemindBean = list;
    }

    @Override
    public int getCount() {
        return mRemindBean.size();
    }

    @Override
    public Object getItem(int position) {
        return mRemindBean.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        RemindBean remindBean = mRemindBean.get(position);
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.comments_item, null);
            holder.srceen_name = (TextView) convertView.findViewById(R.id.srceen_name);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.content = (TextView) convertView.findViewById(R.id.comment_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.srceen_name.setText(remindBean.getScreen_name());
        holder.time.setText(TimeFormat.dTime(remindBean.getCreated_at()));
        holder.content.setText(remindBean.getText());
        return convertView;
    }

    class ViewHolder{
        public TextView srceen_name;
        public TextView time;
        public TextView content;
    }
}
