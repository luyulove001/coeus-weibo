package net.tatans.coeus.weibo.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import net.tatans.coeus.weibo.tools.StreamTool;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by LCM on 2016/8/8. 13:40
 */

public class ImageUtil  {
    /**
     * 获取网络url地址对应的图片
     * @param url
     * @return bitmap的类型
     */
    public static Bitmap getImages(String url){
        Bitmap bitmap = null;
        try {
            //通过代码模拟浏览器访问图片
            URL  mUrl = new URL(url);
            HttpURLConnection  conn = (HttpURLConnection) mUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            InputStream is = conn.getInputStream();
            byte[] imagebytes = StreamTool.getBytes(is);
            bitmap = BitmapFactory.decodeByteArray(imagebytes, 0, imagebytes.length);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
