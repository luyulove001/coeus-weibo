package net.tatans.rhea.demo;

import android.content.Intent;
import android.widget.TextView;

import net.tatans.coeus.network.tools.BaseActivity;
import net.tatans.rhea.demo.cache.CacheMainActivity;
import net.tatans.rhea.network.event.OnClick;
import net.tatans.rhea.network.view.ContentView;
import net.tatans.rhea.network.view.ViewIoc;

@ContentView(R.layout.main)
public class MainActivity extends BaseActivity {

    @OnClick(R.id.bt_global)
    public void btnGlobal(){
        /*Intent global_intent = new Intent();
        global_intent.setClass(this,GlobalActivity.class);
        startActivity(global_intent);*/
        TatansStartActivity(GlobalActivity.class);
    }

    @OnClick(R.id.bt_single)
    public void btnSingle(){
        /*Intent global_intent = new Intent();
        global_intent.setClass(this,SingleActivity.class);
        startActivity(global_intent);*/
        TatansStartActivity(SingleActivity.class);
    }

    @OnClick(R.id.bt_tiris)
    public void btTiris(){
       /* Intent tiris_intent = new Intent();
        tiris_intent.setClass(this,TestRirisActivity.class);
        startActivity(tiris_intent);*/
        TatansStartActivity(TestRirisActivity.class);
    }
    @OnClick(R.id.bt_cache)
    public void btCache(){
        /*Intent tiris_intent = new Intent();
        tiris_intent.setClass(this,CacheMainActivity.class);
        startActivity(tiris_intent);*/
        TatansStartActivity(CacheMainActivity.class);
    }

}
