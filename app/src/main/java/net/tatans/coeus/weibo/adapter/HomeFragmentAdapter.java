package net.tatans.coeus.weibo.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;

import net.tatans.coeus.weibo.R;
import net.tatans.coeus.weibo.util.NewHomeUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/7/19.
 */
public class HomeFragmentAdapter extends BaseAdapter {

    private Context mContext;
    private List<StatusList> mlist;

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
            holder.home_page_pic=(TextView)convertView.findViewById(R.id.home_page_pic);
            holder.home_page_usercomments = (TextView) convertView.findViewById(R.id.home_page_usercomments);
            holder.home_page_he_user=(TextView)convertView.findViewById(R.id.home_page_he_user);
            holder.home_page_he_pic=(TextView)convertView.findViewById(R.id.home_page_he_pic);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        Status status = mlist.get(0).statusList.get(position);
        holder.home_page_username.setText(status.user.name);
        holder.home_page_usertime.setText(status.created_at);
        holder.home_page_usercontent.setText(status.text);
        String zzz=status.original_pic.toString();
        System.out.println(zzz);

        if (status.original_pic.equals("")||status.original_pic==""){
            holder.home_page_pic.setVisibility(View.GONE);
        }
        else{
            holder.home_page_pic.setVisibility(View.VISIBLE);
            holder.home_page_pic.setText(status.original_pic);
        }
        if (status.retweeted_status==null){
            holder.home_page_he_user.setVisibility(View.GONE);
            holder.home_page_usercomments.setVisibility(View.GONE);
            holder.home_page_he_pic.setVisibility(View.GONE);
        }
        else{
            holder.home_page_he_user.setVisibility(View.VISIBLE);
            holder.home_page_he_user.setText("@" + status.retweeted_status.user.name + ":");

            holder.home_page_usercomments.setVisibility(View.VISIBLE);
            holder.home_page_usercomments.setText(status.retweeted_status.text);

            if (status.retweeted_status.original_pic==null){
                holder.home_page_he_pic.setVisibility(View.GONE);
            }
            else {
                holder.home_page_he_pic.setVisibility(View.VISIBLE);
                holder.home_page_he_pic.setText(status.retweeted_status.original_pic);
            }
        }
        return convertView;
    }

    class ViewHolder {
        private TextView home_page_username;
        private TextView home_page_usertime;
        private TextView home_page_usercontent;
        private TextView home_page_pic;
        private TextView home_page_he_user;
        private TextView home_page_usercomments;
        private TextView home_page_he_pic;
    }
}
