package net.tatans.coeus.weibo.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by LCM on 2016/8/5. 13:38
 * 链接跳转
 */

public class HomeSpan extends ClickableSpan {

    private String mtext;
    private Context mContext;
    private static HomeSpan homeSpan;

    private HomeSpan(String text, Context context) {
        this.mtext = text;
        this.mContext = context;
    }

    public static HomeSpan getInstance(String text, Context context) {
        homeSpan = new HomeSpan(text, context);
        return homeSpan;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setColor(Color.parseColor("#ff00ffff"));
        ds.setUnderlineText(true);
    }

    @Override
    public void onClick(View widget) {
        String vvv = mtext;
        System.out.println(vvv);
        final Uri uri = Uri.parse(mtext);
        final Intent it = new Intent(Intent.ACTION_VIEW, uri);
        mContext.startActivity(it);
    }
}
