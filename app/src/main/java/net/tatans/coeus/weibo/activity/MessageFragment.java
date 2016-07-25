package net.tatans.coeus.weibo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.openapi.StatusesAPI;

import net.tatans.coeus.network.tools.TatansToast;
import net.tatans.coeus.weibo.R;
import net.tatans.coeus.weibo.tools.AccessTokenKeeper;
import net.tatans.coeus.weibo.util.Const;
import net.tatans.coeus.weibo.util.Constants;

public class MessageFragment extends Fragment  implements View.OnClickListener{
    //获取视图
    private View view;
    private TextView mAboutMe;
    private TextView mComment;
    private TextView mWriteWeibo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.message_page, container, false);
        initView();
        return view;
    }

    /**
     * 初始化
     */
    private void initView() {
        mAboutMe = (TextView) view.findViewById(R.id.about_me);
        mComment = (TextView) view.findViewById(R.id.comment);
        mWriteWeibo = (TextView) view.findViewById(R.id.write_weibo);

        //设置监听事件
        mAboutMe.setOnClickListener(this);
        mComment.setOnClickListener(this);
        mWriteWeibo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch(v.getId()){
            case R.id.about_me://@我的
                TatansToast.showAndCancel("@我的");
                break;
            case R.id.comment://评论
                TatansToast.showAndCancel("评论");
                break;
            case  R.id.write_weibo://写微博
                intent.setClass(getActivity(),CommentsActivity.class);
                intent.putExtra(Const.TYPE,Const.WRITE_WEIBO);
                getActivity().startActivity(intent);
                break;
            default:
                break;
        }
    }
}
