package net.tatans.coeus.weibo.util;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

import java.io.IOException;

/**
 * 语音助手提示音频播放工具类
 *
 * @author zhongke
 */
public class SoundUtil {
    private static MediaPlayer mediaPlayer;

    static {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MyMediaPlayerListener());
    }

    public static boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public static void stop() {
        mediaPlayer.stop();
    }

    public static void reset() {
        mediaPlayer.reset();
    }

    public static void prepare() {
        try {
            mediaPlayer.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用这个方法需要先设置路径
     */
    public static void start() {
        mediaPlayer.start();
    }

    /**
     * 设置音频路径的方法
     *
     * @param fileDescriptor
     */
    public static void setDataSource(AssetFileDescriptor fileDescriptor) {
        try {
            mediaPlayer
                    .setDataSource(fileDescriptor.getFileDescriptor(),
                            fileDescriptor.getStartOffset(),
                            fileDescriptor.getLength());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void playMedia(AssetFileDescriptor fileDescriptor) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
        try {
            mediaPlayer
                    .setDataSource(fileDescriptor.getFileDescriptor(),
                            fileDescriptor.getStartOffset(),
                            fileDescriptor.getLength());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private final static class MyMediaPlayerListener implements
            OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            mp.reset();
        }
    }
}
