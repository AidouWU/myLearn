package com.example.test;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * 图片内存缓存类
 */

public class MemoryCache implements ImageCache{
    //图片LRU缓存
    LruCache<String,Bitmap> mMemoryCache;

    public MemoryCache () {
        initImageCache();
    }

    private void initImageCache() {
        //计算可使用的最大内存
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory()/1024);
        //取四分之一的可用内存作为缓存
        final int cacheSize = maxMemory/4;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
    }

    @Override
    public void put (String url, Bitmap bitmap) {
        mMemoryCache.put(url, bitmap);
    }

    @Override
    public Bitmap get (String url) {
        return mMemoryCache.get(url);
    }
}
