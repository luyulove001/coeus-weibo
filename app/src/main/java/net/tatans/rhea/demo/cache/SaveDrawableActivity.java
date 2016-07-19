package net.tatans.rhea.demo.cache;



import net.tatans.coeus.network.tools.TatansCache;
import net.tatans.rhea.demo.R;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

/**
 * 
 * @ClassName: SaveDrawableActivity
 * @Description: 缓存drawable
 * @Author Yuliang
 * @Date 2013-8-8 上午10:40:47
 * 
 */
public class SaveDrawableActivity extends Activity {

	private ImageView mIv_drawable_res;

	private TatansCache mCache;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_save_drawable);
		// 初始化控�?
		initView();

		mCache = TatansCache.get();
	}

	/**
	 * 初始化控�?
	 */
	private void initView() {
		mIv_drawable_res = (ImageView) findViewById(R.id.iv_drawable_res);
	}

	/**
	 * 点击save事件
	 * 
	 * @param v
	 */
	public void save(View v) {
		Resources res = getResources();
		Drawable drawable = res.getDrawable(R.drawable.img_test);
		mCache.put("testDrawable", drawable);
	}

	/**
	 * 点击read事件
	 * 
	 * @param v
	 */
	public void read(View v) {
		Drawable testDrawable = mCache.getAsDrawable("testDrawable");
		if (testDrawable == null) {
			Toast.makeText(this, "Drawable cache is null ...",
					Toast.LENGTH_SHORT).show();
			mIv_drawable_res.setImageDrawable(null);
			return;
		}
		mIv_drawable_res.setImageDrawable(testDrawable);
	}

	/**
	 * 点击clear事件
	 * 
	 * @param v
	 */
	public void clear(View v) {
		mCache.remove("testDrawable");
	}
}