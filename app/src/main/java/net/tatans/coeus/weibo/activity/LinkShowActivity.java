package net.tatans.coeus.weibo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import net.tatans.coeus.weibo.R;

/**
 * Created by Administrator on 2016/8/1.
 */
public class LinkShowActivity extends Activity{
    private WebView webview_link;
    private String urls;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linkshow);
        webview_link=(WebView)findViewById(R.id.webview_link);
        urls=getIntent().getStringExtra("urls");
        webview_link.loadUrl(urls);
        webview_link.getSettings().setJavaScriptEnabled(true);
        webview_link.getSettings().setPluginState(WebSettings.PluginState.ON);
        webview_link.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview_link.getSettings().setAllowFileAccess(true);
        webview_link.getSettings().setLoadWithOverviewMode(true);
        webview_link.getSettings().setUseWideViewPort(true);
    }
}
