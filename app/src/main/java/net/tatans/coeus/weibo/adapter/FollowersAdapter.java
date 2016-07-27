package net.tatans.coeus.weibo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.tatans.coeus.weibo.R;

import java.util.List;

/**
 * Created by LCM on 2016/7/27. 10:18
 */

public class FollowersAdapter extends BaseAdapter {

    private Context mContext;

    private List<String> mList;

    public FollowersAdapter(Context context, List<String> data) {
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
        String screen_name = mList.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.sort_item, null);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvTitle.setText(screen_name);
        return convertView;
    }

    class ViewHolder {
        private TextView tvTitle;
    }
}
