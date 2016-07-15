package com.ybg.rp.vm.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.request.target.ViewTarget;
import com.cnpay.tigerbalm.utils.SimpleUtil;
import com.cnpay.tigerbalm.utils.TbLog;
import com.ybg.rp.vm.R;

/**
 * 包            名:      com.ck.cloudknown.glide
 * 类            名:      CustomCachingGlideModule
 * 修 改 记 录:     // 修改历史记录，包括修改日期、修改者及修改内容
 * 版 权 所 有:     版权所有(C)2010-2015
 * 公             司:
 *
 * @author liyuanming
 * @version V1.0
 * @date 2016/5/11
 */
public class CustomCachingGlideModule implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        ViewTarget.setTagId(R.id.glide_tag_id); // 设置别的get/set tag id，以免占用View默认的
        // set size & external vs. internal
        int cacheSize100MegaBytes = 104857600;

        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();

        int customMemoryCacheSize = (int) (1.2 * defaultMemoryCacheSize);
        int customBitmapPoolSize = (int) (1.2 * defaultBitmapPoolSize);

        //设置内存缓存
        builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));

        //设置位图池
        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));
//        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888); // 设置图片质量为高质量
        builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565); // 默认，比较8888 少耗一半内存
        // 设置SD卡缓存目录
        String path = SimpleUtil.getSDCardPath() + "/imageVmCache";
        TbLog.e("----" + path);
//                new InternalCacheDiskCacheFactory(context, cacheSize100MegaBytes)
        builder.setDiskCache(new DiskLruCacheFactory(path, cacheSize100MegaBytes));

        //builder.setDiskCache(
        //new ExternalCacheDiskCacheFactory(context, cacheSize100MegaBytes));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        // nothing to do here

    }
}
