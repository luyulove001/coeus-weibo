package net.tatans.coeus.weibo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import net.tatans.coeus.weibo.R;
import net.tatans.coeus.weibo.util.Const;

import java.util.List;


/**
 * Created by SiLiPing on 2016/4/1.
 * 有索引的添加替换应用列表适配器
 */
public class ContactListAdapter extends BaseAdapter  {

    private Context ctx;
    private List<String>  list;

    final static class ViewHolder {
        private TextView tvLetter;
        private TextView tvTitle;
        private View line;
    }

    public ContactListAdapter(Context mContext, List<String> list) {
        this.ctx = mContext;
        this.list = list;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     */
//    public void updateListView(List<Person> list){
//        this.list = list;
//        notifyDataSetChanged();
//    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final String  mContent = list.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(ctx).inflate(R.layout.sort_item, null);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.title);
            viewHolder.line = convertView.findViewById(R.id.line);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvTitle.setText(this.list.get(position));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("OnClick","contact:::"+mContent);
                Intent intent = new Intent();
                intent.putExtra(Const.CONTACT,mContent);
                ((Activity)ctx).setResult(0,intent);
                ((Activity)ctx).finish();
            }
        });
        return convertView;
    }

}
