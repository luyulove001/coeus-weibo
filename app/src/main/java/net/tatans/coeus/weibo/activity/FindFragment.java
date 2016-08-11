package net.tatans.coeus.weibo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import net.tatans.coeus.network.tools.StartActivity;
import net.tatans.coeus.weibo.R;

public class FindFragment extends Fragment implements View.OnClickListener{
    /**
     * 界面的view
     */
    private View view;
    private LinearLayout searchUser, searchWeibo;
    private Intent intent;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.find_page, container, false);
        initView();
        intent = new Intent(getActivity(), SearchActivity.class);
        return view;
    }

    private void initView() {
        searchWeibo = (LinearLayout) view.findViewById(R.id.search_weibo);
        searchUser = (LinearLayout) view.findViewById(R.id.search_user);
        searchWeibo.setOnClickListener(this);
        searchUser.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_weibo:
                intent.putExtra("isStatues", true);
                startActivity(intent);
                break;
            case R.id.search_user:
                intent.putExtra("isStatues", false);
                startActivity(intent);
                break;
        }
    }
}
