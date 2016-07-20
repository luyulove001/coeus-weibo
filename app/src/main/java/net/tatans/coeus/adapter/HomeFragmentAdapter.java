package net.tatans.coeus.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.tatans.coeus.weibo.R;

/**
 * Created by Administrator on 2016/7/19.
 */
public class HomeFragmentAdapter extends BaseAdapter{

    private Context mContext;
    public HomeFragmentAdapter(Context context){
        this.mContext=context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null){
            holder=new ViewHolder();
            convertView= LayoutInflater.from(mContext).inflate(R.layout.home_page_fragment_item,null);
            holder.home_page_username=(TextView)convertView.findViewById(R.id.home_page_username);
            holder.home_page_usertime=(TextView)convertView.findViewById(R.id.home_page_usertime);
            holder.home_page_usercontent=(TextView)convertView.findViewById(R.id.home_page_usercontent);
            holder.home_page_usercomments=(TextView)convertView.findViewById(R.id.home_page_usercomments);
        }
        else{
            if (position==0){

            }
            else{

            }
        }

        return convertView;
    }
    class ViewHolder{
        private TextView home_page_username;
        private TextView home_page_usertime;
        private TextView home_page_usercontent;
        private TextView home_page_usercomments;
    }
}
