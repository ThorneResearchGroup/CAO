package tech.tresearchgroup.cao.controller.remote;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import tech.tresearchgroup.cao.model.CacheTypesEnum;

public class GlobalStaticRemoteCache {
    private static final RedisCommands<String, String> staticCache;
    private static final RedisCommands<String, String> domCache;

    static {
        RedisClient staticCacheClient = RedisClient.create("redis://localhost/Static");
        StatefulRedisConnection<String, String> staticCacheConnection = staticCacheClient.connect();
        staticCache = staticCacheConnection.sync();

        RedisClient domCacheClient = RedisClient.create("redis://localhost/DomCache");
        StatefulRedisConnection<String, String> domCacheConnection = domCacheClient.connect();
        domCache = domCacheConnection.sync();
    }

    public static void create(CacheTypesEnum cacheTypesEnum, String key, String value) {
        switch (cacheTypesEnum) {
            case STATIC -> staticCache.set(key, value);
            case DOM -> domCache.set(key, value);
        }
    }

    public static Object read(CacheTypesEnum cacheTypesEnum, String key) {
        return switch (cacheTypesEnum) {
            case STATIC -> staticCache.get(key);
            case DOM -> domCache.get(key);
            default -> null;
        };
    }

    public static void update(CacheTypesEnum cacheTypesEnum, String key, String value) {
        switch (cacheTypesEnum) {
            case STATIC -> staticCache.set(key, value);
            case DOM -> domCache.set(key, value);
        }
    }
}
