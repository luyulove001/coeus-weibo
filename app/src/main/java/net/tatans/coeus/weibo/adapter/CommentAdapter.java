package net.tatans.coeus.weibo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.tatans.coeus.weibo.R;
import net.tatans.coeus.weibo.bean.CommentBean;
import net.tatans.coeus.weibo.util.TimeFormat;

import java.util.List;

/**
 * Created by LCM on 2016/7/27. 13:44
 */

public class CommentAdapter extends BaseAdapter {

    private Context mContext;
    private List<CommentBean> mList;

    public CommentAdapter(Context context, List<CommentBean> data) {
        this.mContext = context;
        this.mList = data;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.comments_item,null);
            holder.srceen_name = (TextView) convertView.findViewById(R.id.srceen_name);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.content = (TextView) convertView.findViewById(R.id.comment_content);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        String time = TimeFormat.dTime(mList.get(position).getCreated_at());
        holder.srceen_name.setText(mList.get(position).getScreen_name());
        holder.time.setText(time);
        holder.content.setText(mList.get(position).getText());
        return convertView;
    }

    class ViewHolder{
        public TextView srceen_name;
        public TextView time;
        public TextView content;
    }
}
