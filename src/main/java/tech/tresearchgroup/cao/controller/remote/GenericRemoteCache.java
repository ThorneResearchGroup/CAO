package tech.tresearchgroup.cao.controller.remote;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import tech.tresearchgroup.cao.controller.GenericCache;
import tech.tresearchgroup.cao.model.CacheTypesEnum;

public class GenericRemoteCache implements GenericCache {
    private final RedisCommands<String, String> apiCache;
    private final RedisCommands<String, String> databaseCache;
    private final RedisCommands<String, String> pageApiCache;
    private final RedisCommands<String, String> pageDatabaseCache;

    public GenericRemoteCache(Class theClass) {
        RedisClient apiCacheClient = RedisClient.create("redis://localhost/" + theClass.getSimpleName() + "Api");
        StatefulRedisConnection<String, String> apiCacheConnection = apiCacheClient.connect();
        this.apiCache = apiCacheConnection.sync();

        RedisClient databaseCacheClient = RedisClient.create("redis://localhost/" + theClass.getSimpleName() + "Database");
        StatefulRedisConnection<String, String> databaseCacheConnection = databaseCacheClient.connect();
        this.databaseCache = databaseCacheConnection.sync();

        RedisClient pageApiCacheClient = RedisClient.create("redis://localhost/" + theClass.getSimpleName() + "PageApi");
        StatefulRedisConnection<String, String> pageApiCacheConnection = pageApiCacheClient.connect();
        this.pageApiCache = pageApiCacheConnection.sync();

        RedisClient pageDatabaseCacheClient = RedisClient.create("redis://localhost/" + theClass.getSimpleName() + "PageDatabase");
        StatefulRedisConnection<String, String> pageDatabaseCacheConnection = pageDatabaseCacheClient.connect();
        this.pageDatabaseCache = pageDatabaseCacheConnection.sync();
    }

    @Override
    public void create(CacheTypesEnum cacheTypesEnum, Object id, Object data) {
        switch (cacheTypesEnum) {
            case API -> apiCache.set((String) id, (String) data);
            case DATABASE -> databaseCache.set((String) id, (String) data);
        }
    }

    @Override
    public String read(CacheTypesEnum cacheTypesEnum, Object id) {
        switch (cacheTypesEnum) {
            case API -> {
                return apiCache.get((String) id);
            }
            case DATABASE -> {
                return databaseCache.get((String) id);
            }
        }
        return null;
    }

    @Override
    public void update(CacheTypesEnum cacheTypesEnum, Object id, Object data) {
        switch (cacheTypesEnum) {
            case API: {
                apiCache.del((String) id);
                apiCache.set((String) id, (String) data);
            }
            case DATABASE: {
                databaseCache.del((String) id);
                databaseCache.set((String) id, (String) data);
            }
        }
    }

    @Override
    public void delete(CacheTypesEnum cacheTypesEnum, Object id) {
        switch (cacheTypesEnum) {
            case DATABASE, API: {
                apiCache.del((String) id);
                databaseCache.del((String) id);
            }
            case PAGE_API: {
                pageApiCache.del((String) id);
            }
            case PAGE_DATABASE: {
                pageDatabaseCache.del((String) id);
            }
        }
    }
}
