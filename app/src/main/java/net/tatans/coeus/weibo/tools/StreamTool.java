package net.tatans.coeus.weibo.tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by LCM on 2016/8/8. 13:32
 * 解析图片
 */

public class StreamTool {

    public static byte[] getBytes(InputStream is) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024*1024];
        int len = 0;
        try {
            while ((len = is.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                bos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        byte[] result = bos.toByteArray();
        return result;
    }
}
