package net.tatans.coeus.weibo.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import net.tatans.coeus.weibo.adapter.HomeFragmentAdapter;
import net.tatans.coeus.weibo.R;

public class HomeFragment extends Fragment {
	private ListView home_page_listview;
	private HomeFragmentAdapter adapter;
	private View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		view=LayoutInflater.from(getActivity()).inflate(R.layout.home_page,container, false);
		home_page_listview=(ListView)view.findViewById(R.id.home_page_listview);
		adapter=new HomeFragmentAdapter(getActivity());
		home_page_listview.setAdapter(adapter);
		return view;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			// 可见时执行的操作

		} else {
			// 不可见时执行的操作
		}
	}
}