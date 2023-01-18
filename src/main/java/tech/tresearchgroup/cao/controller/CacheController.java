package tech.tresearchgroup.cao.controller;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import tech.tresearchgroup.cao.model.CachedEntity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CacheController {
    private static final Cache<String, CachedEntity> filePartCache = Caffeine.newBuilder().maximumSize(1000).build();

    public static boolean existsInCache(int start, int end, long id) {
        return filePartCache.getIfPresent(start + "-" + end + "_" + id) != null;
    }

    public static CachedEntity get(int start, int end, long id) {
        return filePartCache.getIfPresent(start + "-" + end + "_" + id);
    }

    public static void put(int start, int end, long id, byte[] data) {
        CachedEntity cachedEntity = new CachedEntity(currentDateTime(), data);
        filePartCache.put(start + "-" + end + "_" + id, cachedEntity);
    }

    public static String currentDateTime() {
        String pattern = "E dd MMMMM yyyy HH:mm:ss z";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());
        return date.replace(".", ",");
    }
}
