package net.tatans.coeus.weibo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sina.weibo.sdk.openapi.models.User;

import net.tatans.coeus.weibo.R;
import net.tatans.coeus.weibo.activity.MyHomePageActivity;
import net.tatans.coeus.weibo.util.Const;

import java.util.List;


/**
 * Created by SiLiPing on 2016/4/1.
 * 有索引的添加替换应用列表适配器
 */
public class ContactListAdapter extends BaseAdapter {

    private Context ctx;
    private List<User> list;
    //用于判定是 关注 粉丝 还是联系人
    private int mType;

    final static class ViewHolder {
        private TextView tvLetter;
        private TextView tvTitle;
        private View line;
    }

    public ContactListAdapter(Context mContext, List<User> list) {
        this.ctx = mContext;
        this.list = list;
    }


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
        final User user = list.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(ctx).inflate(R.layout.sort_item, null);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.title);
            viewHolder.line = convertView.findViewById(R.id.line);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvTitle.setText(user.screen_name);
        convertView.setOnClickListener(new OnClickListenerIml(user));
        return convertView;
    }


    private class OnClickListenerIml implements View.OnClickListener {
        private User user;

        public OnClickListenerIml(User user) {
            this.user = user;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();

            switch (mType) {
                case 0://从关注进入
                    startActivity(user);
                    break;
                case 1://从联系人进入
                    intent.putExtra(Const.CONTACT, user.screen_name);
                    ((Activity) ctx).setResult(0, intent);
                    ((Activity) ctx).finish();
                    break;
                case 2://粉丝
                    startActivity(user);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 进入个人主页
     *
     * @param user
     */
    private void startActivity(User user) {
        Intent intent = new Intent();
        Long uid = Long.parseLong(user.id);
        intent.setClass(ctx, MyHomePageActivity.class);
        intent.putExtra(Const.UID, uid);
        intent.putExtra("fansNum", String.valueOf(user.followers_count));
        intent.putExtra("friends", String.valueOf(user.friends_count));
        intent.putExtra("desc1", user.verified_reason);
        ctx.startActivity(intent);
    }

    public int getmType() {
        return mType;
    }

    public void setmType(int mType) {
        this.mType = mType;
    }
}
