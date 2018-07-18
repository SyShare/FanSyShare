package sy.com.initproject.root.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.pince.ut.AndroidUtils;
import com.pince.ut.constans.FileConstants;

import java.io.InputStream;

/**
 * @date：2018/7/18
 * @author: SyShare
 */
@GlideModule
public class CustomGlideModule extends AppGlideModule {
    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
    }

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
                .setMemoryCacheScreens(2)
                .build();
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();

        int customMemoryCacheSize = (int) (1.5 * defaultMemoryCacheSize);
        int customBitmapPoolSize = (int) (1.5 * defaultBitmapPoolSize);

        builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));
        builder.setDiskCache(new DiskLruCacheFactory(FileConstants.CACHE_IMAGE_DIR, FileConstants.MAX_DISK_CACHE_SIZE));

        boolean isMemoryAvail = AndroidUtils.isMemoryAvail(context);
        DecodeFormat decodeFormat = (isMemoryAvail ? DecodeFormat.PREFER_ARGB_8888 : DecodeFormat.PREFER_RGB_565);
        builder.setDefaultRequestOptions(new RequestOptions().format(decodeFormat));
    }

    @Override
    public boolean isManifestParsingEnabled() {
        //跳过Manifest解析，直接使用@GlideModule标注的module
        return false;
    }
}
