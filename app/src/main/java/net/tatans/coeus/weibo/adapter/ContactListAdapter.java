package net.tatans.coeus.weibo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import net.tatans.coeus.weibo.R;
import net.tatans.coeus.weibo.bean.Person;
import net.tatans.coeus.weibo.model.imp.ITatansItemClick;

import java.util.List;


/**
 * Created by SiLiPing on 2016/4/1.
 * 有索引的添加替换应用列表适配器
 */
public class ContactListAdapter extends BaseAdapter implements SectionIndexer {

    private Context ctx;
    private ITatansItemClick itemClick;
    private List<Person>  list;

    final static class ViewHolder {
        private TextView tvLetter;
        private TextView tvTitle;
        private View line;
    }

    public ContactListAdapter(Context mContext, List<Person> list, ITatansItemClick itemClick) {
        this.ctx = mContext;
        this.list = list;
        this.itemClick = itemClick;
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
        Log.e("list","adapter::"+list.get(0).getName()+"::"+list.size());
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
        final Person mContent = list.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(ctx).inflate(R.layout.sort_item, null);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.title);
            viewHolder.tvLetter = (TextView) convertView.findViewById(R.id.catalog);
            viewHolder.line = convertView.findViewById(R.id.line);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);
        // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            if (mContent.equals("★")) {
                viewHolder.tvLetter.setContentDescription("收藏");
                viewHolder.tvLetter.setText("★");
            } else {
                String firstName = mContent.getPinYinName().substring(0, 1);
                viewHolder.tvLetter.setVisibility(View.VISIBLE);
                viewHolder.tvLetter.setText(firstName);
                viewHolder.tvLetter.setContentDescription(firstName);
            }

        } else {
            viewHolder.tvLetter.setVisibility(View.GONE);
        }
        if (position < list.size() - 1) {
            final Person mContents = list.get(position+1);
            if (!mContent.equals(mContents)) {
                viewHolder.line.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.line.setVisibility(View.VISIBLE);
            }
        }
        if (position == list.size() - 1) {
            viewHolder.line.setVisibility(View.INVISIBLE);
        }
        viewHolder.tvTitle.setText(this.list.get(position).getName());
        viewHolder.tvTitle.setOnClickListener(new ItemOnClickListener(position));
        viewHolder.tvTitle.setOnLongClickListener(new ItemOnClickListener(position));
        return convertView;
    }

    /**
     * 单击事件
     */
    private class ItemOnClickListener implements View.OnClickListener,View.OnLongClickListener{
        private int mPosition;
        public ItemOnClickListener(int position) {
            this.mPosition = position;
        }
        @Override
        public void onClick(View v) {
            itemClick.OnTatansItemClick(mPosition,list.get(mPosition).getPinYinName());
        }

        @Override
        public boolean onLongClick(View view) {
            itemClick.OnTatansItemLongClick(mPosition,list.get(mPosition).getPinYinName());
            return false;
        }
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }
    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    @Override
    public int getPositionForSection(int sectionIndex) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getPinYinName();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == sectionIndex) {
                return i;
            }
        }
        return -1;
    }
    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    @Override
    public int getSectionForPosition(int position) {
        return list.get(position).getPinYinName().charAt(0);
    }


}
