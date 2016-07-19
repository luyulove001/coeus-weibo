package net.tatans.rhea.demo.cache;


import net.tatans.coeus.network.tools.TatansCache;
import net.tatans.rhea.demo.R;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @ClassName: SaveObjectActivity
 * @Description: 缓存jsonobject
 * @Author Yuliang
 * @Date 2013-8-8 下午2:13:16
 * 
 */
public class SaveObjectActivity extends Activity {

	private TextView mTv_object_original, mTv_object_res;
	private UserBean userBean;

	private TatansCache mCache;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_save_object);
		// 初始化控�?
		initView();

		mCache = TatansCache.get();
		userBean = new UserBean();
		userBean.setAge("18");
		userBean.setName("HaoYoucai");
		mTv_object_original.setText(userBean.toString());
	}

	/**
	 * 初始化控�?
	 */
	private void initView() {
		mTv_object_original = (TextView) findViewById(R.id.tv_object_original);
		mTv_object_res = (TextView) findViewById(R.id.tv_object_res);
	}

	/**
	 * 点击save事件
	 * 
	 * @param v
	 */
	public void save(View v) {
		mCache.put("testObject", userBean);
	}

	/**
	 * 点击read事件
	 * 
	 * @param v
	 */
	public void read(View v) {
		UserBean testObject = (UserBean) mCache.getAsObject("testObject");
		if (testObject == null) {
			Toast.makeText(this, "Object cache is null ...", Toast.LENGTH_SHORT)
					.show();
			mTv_object_res.setText(null);
			return;
		}
		mTv_object_res.setText(testObject.toString());
	}

	/**
	 * 点击clear事件
	 * 
	 * @param v
	 */
	public void clear(View v) {
		mCache.remove("testObject");
	}
}
