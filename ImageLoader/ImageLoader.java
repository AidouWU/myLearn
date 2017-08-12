package com.example.test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;
import android.widget.ImageView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 图片加载类
 */

public class ImageLoader {
    //图片缓存
    ImageCache mImageCache = new ImageCache();
    //SD卡缓存
    DiskCache mDiskcache = new DiskCache();
    //是否使用SD卡缓存
    boolean isUseDiskCache = false;
    //线程池，线程数量为CPU数量
    ExecutorService mExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());


    public void displayImage(final String url, final ImageView imageView) {
        //判断使用哪种缓存
        Bitmap bitmap = isUseDiskCache ? mDiskcache.get(url) : mImageCache.get(url);
        if (bitmap != null){
            imageView.setImageBitmap(bitmap);
            return;
        }
        //没有缓存，则提交给线程池进行下载
        imageView.setTag(url);
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = downloadImage(url);
                if (bitmap == null) {
                    return;
                }
                if (imageView.getTag().equals(url)) {
                    imageView.setImageBitmap(bitmap);
                }
                mImageCache.put(url,bitmap);
            }
        });
    }

    public Bitmap downloadImage (String imageUrl) {
        Bitmap bitmap = null;
        try{
            URL url = new URL(imageUrl);
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            bitmap = BitmapFactory.decodeStream(conn.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public void useDiskCache (boolean useDiskCache) {
        isUseDiskCache = useDiskCache;
    }

}
