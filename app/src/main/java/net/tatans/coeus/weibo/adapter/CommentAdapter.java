package net.tatans.coeus.weibo.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sina.weibo.sdk.openapi.models.Comment;
import com.sina.weibo.sdk.openapi.models.CommentList;

import net.tatans.coeus.weibo.R;
import net.tatans.coeus.weibo.activity.CommentDetailsActivity;
import net.tatans.coeus.weibo.activity.WeiboMenuDetailsActivity;
import net.tatans.coeus.weibo.util.Const;
import net.tatans.coeus.weibo.util.TimeFormat;

import java.util.List;

/**
 * Created by LCM on 2016/7/27. 13:44
 */

public class CommentAdapter extends BaseAdapter {

    private Context mContext;
    private CommentList mList;

    public CommentAdapter(Context context, CommentList data) {
        this.mContext = context;
        this.mList = data;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.comments_item, null);
            holder.srceen_name = (TextView) convertView.findViewById(R.id.srceen_name);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.content = (TextView) convertView.findViewById(R.id.comment_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String time = TimeFormat.dTime(mList.commentList.get(position).created_at);
        holder.srceen_name.setText(mList.commentList.get(position).user.screen_name);
        holder.time.setText(time);
        holder.content.setText(mList.commentList.get(position).text);
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
            Comment comment =mList.commentList.get(mPosition);
            Intent intent =  new Intent(mContext, WeiboMenuDetailsActivity.class);
            intent.putExtra("id",comment.id);
            intent.putExtra(Const.WEIBO_ID,comment.status.id);
            intent.putExtra(Const.COMMENTS_COUNT,comment.status.comments_count);
            intent.putExtra(Const.TYPE,Const.COMMENT);
            intent.putExtra(Const.UID, comment.status.user.id);
            intent.putExtra(Const.SCREEN_NAME, comment.status.user.screen_name);
            intent.putExtra(Const.FAVORITES, comment.status.favorited);
            intent.putExtra(Const.REPOSTS_COUNT, comment.status.reposts_count);
            mContext.startActivity(intent);
        }
    }
}
