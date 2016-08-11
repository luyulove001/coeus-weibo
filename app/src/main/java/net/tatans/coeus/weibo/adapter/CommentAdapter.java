package net.tatans.coeus.weibo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sina.weibo.sdk.openapi.models.Comment;

import net.tatans.coeus.weibo.R;
import net.tatans.coeus.weibo.activity.CommentDetailsActivity;
import net.tatans.coeus.weibo.util.TimeFormat;

import java.util.List;

/**
 * Created by LCM on 2016/7/27. 13:44
 */

public class CommentAdapter extends BaseAdapter {

    private Context mContext;
    private List<Comment> mList;

    public CommentAdapter(Context context, List<Comment> data) {
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
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.comments_item, null);
            holder.srceen_name = (TextView) convertView.findViewById(R.id.srceen_name);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.content = (TextView) convertView.findViewById(R.id.comment_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String time = TimeFormat.dTime(mList.get(position).created_at);
        holder.srceen_name.setText(mList.get(position).user.screen_name);
        holder.time.setText(time);
        holder.content.setText(mList.get(position).text);
        convertView.setOnClickListener(new OnClickListenerImp(position));
        return convertView;
    }

    class ViewHolder {
        public TextView srceen_name;
        public TextView time;
        public TextView content;
    }

    private class OnClickListenerImp implements View.OnClickListener {
        private int mPosition;

        public OnClickListenerImp(int position) {
            this.mPosition = position;
        }

        @Override
        public void onClick(View v) {
            Comment comment =mList.get(mPosition);
            Intent intent =  new Intent(mContext, CommentDetailsActivity.class);
            intent.putExtra("id",comment.id);
            intent.putExtra("weiboId",comment.status.id);
            mContext.startActivity(intent);
        }
    }
}
