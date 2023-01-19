package tech.tresearchgroup.cao.controller.local;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import tech.tresearchgroup.cao.controller.GenericCache;
import tech.tresearchgroup.cao.model.CacheTypesEnum;

import java.util.List;

public class GenericLocalCache implements GenericCache {
    private final Cache<Long, byte[]> apiCache;
    private final Cache<Long, byte[]> databaseCache;
    private final Cache<Long, byte[]> pageApiCache;
    private final Cache<Long, List<List<Long>>> pageDatabaseCache;

    public GenericLocalCache(long apiCacheSize, long databaseCacheSize, long pageApiCacheSize, long pageDatabaseCacheSize) {
        this.apiCache = Caffeine.newBuilder().maximumSize(apiCacheSize).build();
        this.databaseCache = Caffeine.newBuilder().maximumSize(databaseCacheSize).build();
        this.pageApiCache = Caffeine.newBuilder().maximumSize(pageApiCacheSize).build();
        this.pageDatabaseCache = Caffeine.newBuilder().maximumSize(pageDatabaseCacheSize).build();
    }

    @Override
    public void create(CacheTypesEnum cacheTypesEnum, Object key, Object data) {
        switch (cacheTypesEnum) {
            case API -> apiCache.put((Long) key, (byte[]) data);
            case DATABASE -> databaseCache.put((Long) key, (byte[]) data);
            case PAGE_API -> pageApiCache.put((Long) key, (byte[]) data);
            case PAGE_DATABASE -> pageDatabaseCache.put((Long) key, (List<List<Long>>) data);
        }
    }

    @Override
    public Object read(CacheTypesEnum cacheTypesEnum, Object key) {
        return switch (cacheTypesEnum) {
            case API -> apiCache.getIfPresent((Long) key);
            case DATABASE -> databaseCache.getIfPresent((Long) key);
            case PAGE_API -> pageApiCache.getIfPresent((Long) key);
            case PAGE_DATABASE -> pageDatabaseCache.getIfPresent((Long) key);
            default -> null;
        };
    }

    @Override
    public void update(CacheTypesEnum cacheTypesEnum, Object key, Object value) {
        switch (cacheTypesEnum) {
            case API: {
                apiCache.invalidate((Long) key);
                apiCache.put((Long) key, (byte[]) value);
            }
            case DATABASE: {
                databaseCache.invalidate((Long) key);
                databaseCache.put((Long) key, (byte[]) value);
            }
            case PAGE_DATABASE: {
                pageDatabaseCache.invalidate((Long) key);
                pageDatabaseCache.put((Long) key, (List<List<Long>>) value);
            }
            case PAGE_API: {
                pageApiCache.invalidate((Long) key);
                pageApiCache.put((Long) key, (byte[]) value);
            }
        }
    }

    @Override
    public void delete(CacheTypesEnum cacheTypesEnum, Object id) {
        switch (cacheTypesEnum) {
            case API, DATABASE -> {
                apiCache.invalidate((Long) id);
                databaseCache.invalidate((Long) id);
            }
            case PAGE_API -> pageApiCache.invalidate((Long) id);
            case PAGE_DATABASE -> pageDatabaseCache.invalidate((Long) id);
        }
    }
}
