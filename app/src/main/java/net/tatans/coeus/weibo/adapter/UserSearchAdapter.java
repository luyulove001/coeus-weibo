package net.tatans.coeus.weibo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.tatans.coeus.weibo.R;
import net.tatans.coeus.weibo.activity.MyHomePageActivity;
import net.tatans.coeus.weibo.bean.SearchResultUser;

import java.util.List;

/**
 * 搜索出用户列表的适配器
 * Created by cly on 2016/7/27.
 */

public class UserSearchAdapter extends BaseAdapter {
    private Context ctx;
    private List<SearchResultUser> users;

    private class ViewHolder {
        private TextView tvScreenName;
        private TextView tvFollowersCount;
        private TextView tvVerified;
    }

    public UserSearchAdapter(Context mContext, List<SearchResultUser> users) {
        this.ctx = mContext;
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(ctx).inflate(R.layout.user_search_item, null);
            viewHolder.tvScreenName = (TextView) convertView.findViewById(R.id.screen_name);
            viewHolder.tvFollowersCount = (TextView) convertView.findViewById(R.id.followers_count);
            viewHolder.tvVerified = (TextView) convertView.findViewById(R.id.verified);
            convertView.setTag(viewHolder);
            convertView.setOnClickListener(new MyClickListener(position));
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvScreenName.setText(users.get(position).getScreen_name());
        viewHolder.tvFollowersCount.setText(ctx.getString(R.string.followers) + users.get(position).getFansNum());
        viewHolder.tvVerified.setText(users.get(position).getVerified_reason());
        return convertView;
    }

    private class MyClickListener implements View.OnClickListener {
        private int mPosition;

        public MyClickListener(int position) {
            this.mPosition = position;
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(ctx, MyHomePageActivity.class);
            i.putExtra("uid", Long.valueOf(users.get(mPosition).getId()));
            i.putExtra("description", users.get(mPosition).getDescription());
            i.putExtra("verified_reason", users.get(mPosition).getDesc1());
            i.putExtra("followers_count", users.get(mPosition).getFansNum());
            ctx.startActivity(i);
        }
    }
}
