package net.tatans.rhea.demo;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import net.tatans.coeus.network.tools.BaseActivity;
import net.tatans.coeus.network.tools.TatansToast;
import net.tatans.rhea.demo.utils.RegCertificateUtil;
import net.tatans.rhea.network.event.OnClick;
import net.tatans.rhea.network.view.ContentView;
import net.tatans.rhea.network.view.ViewIoc;
import net.tatans.rhea.tiris.TirisDemo;
import net.tatans.rhea.tiris.onTirisListener;

/**
 * Created by Lion on 2016/5/16.
 */
@ContentView(R.layout.tiris)
public class TestRirisActivity extends BaseActivity {

    @ViewIoc(R.id.edt_key) EditText edt_key;
    private TirisDemo mTirisDemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RegCertificateUtil regCertificateUtil = new RegCertificateUtil(this);
    }



    @OnClick(R.id.btn_tiris)
    public void btnTiris(){
        Log.d("ssssssssss","btn_tiris");
        int key = Integer.valueOf(edt_key.getText().toString());
        mTirisDemo = new TirisDemo(this,key);
        mTirisDemo.setOnTirisListener(new onTirisListener() {
            @Override
            public void onRegistration() {
                TatansToast.showAndCancel("在线注册");
            }

            @Override
            public void onContinue() {
                TatansToast.showAndCancel("继续使用");
            }
        });
    }

}
