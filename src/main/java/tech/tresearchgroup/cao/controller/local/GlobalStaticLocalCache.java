package tech.tresearchgroup.cao.controller.local;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import j2html.tags.DomContent;
import tech.tresearchgroup.cao.model.CacheTypesEnum;

import java.util.List;

public class GlobalStaticLocalCache {
    private static final Cache<String, List<String>> staticCache;
    private static final Cache<String, List<DomContent>> domCache;

    static {
        staticCache = Caffeine.newBuilder().build();
        domCache = Caffeine.newBuilder().build();
    }

    public static void create(CacheTypesEnum cacheTypesEnum, String key, List data) {
        switch (cacheTypesEnum) {
            case STATIC -> staticCache.put(key, (List<String>) data);
            case DOM -> domCache.put(key, (List<DomContent>) data);
        }
    }

    public static Object read(CacheTypesEnum cacheTypesEnum, String key) {
        return switch (cacheTypesEnum) {
            case STATIC -> staticCache.getIfPresent(key);
            case DOM -> domCache.getIfPresent(key);
            default -> null;
        };
    }

    public static void update(CacheTypesEnum cacheTypesEnum, String key, List value) {
        switch (cacheTypesEnum) {
            case STATIC -> {
                staticCache.invalidate(key);
                staticCache.put(key, (List<String>) value);
            }
            case DOM -> {
                domCache.invalidate(key);
                domCache.put(key, (List<DomContent>) value);
            }
        }
    }
}
