package tech.tresearchgroup.cao.controller;

import tech.tresearchgroup.cao.controller.local.GenericLocalCache;
import tech.tresearchgroup.cao.controller.local.GlobalStaticLocalCache;
import tech.tresearchgroup.cao.controller.remote.GenericRemoteCache;
import tech.tresearchgroup.cao.controller.remote.GlobalStaticRemoteCache;
import tech.tresearchgroup.cao.model.CacheMethodEnum;
import tech.tresearchgroup.cao.model.CacheTypesEnum;

import java.util.List;

public class GenericCAO implements GenericCache {
    private final GenericCache genericCache;
    private final CacheMethodEnum cacheMethodEnum;

    public GenericCAO(long apiCacheSize, long databaseCacheSize, long pageApiCacheSize, long pageDatabaseCacheSize) {
        this.cacheMethodEnum = CacheMethodEnum.LOCAL;
        this.genericCache = new GenericLocalCache(apiCacheSize, databaseCacheSize, pageApiCacheSize, pageDatabaseCacheSize);
    }

    public GenericCAO(CacheMethodEnum cacheMethodEnum, long apiCacheSize, long databaseCacheSize, long pageApiCacheSize, long pageDatabaseCacheSize, Class theClass) {
        this.cacheMethodEnum = cacheMethodEnum;
        if (cacheMethodEnum.equals(CacheMethodEnum.REDIS)) {
            this.genericCache = new GenericRemoteCache(theClass);
        } else {
            this.genericCache = new GenericLocalCache(apiCacheSize, databaseCacheSize, pageApiCacheSize, pageDatabaseCacheSize);
        }
    }

    @Override
    public void create(CacheTypesEnum cacheTypesEnum, Object id, Object data) {
        switch (cacheTypesEnum) {
            case DOM:
            case STATIC: {
                switch (cacheMethodEnum) {
                    case LOCAL -> GlobalStaticLocalCache.create(cacheTypesEnum, (String) id, (List) data);
                    case REDIS -> GlobalStaticRemoteCache.create(cacheTypesEnum, (String) id, (String) data);
                }
            }
            default: {
                genericCache.create(cacheTypesEnum, id, data);
            }
        }
    }

    @Override
    public Object read(CacheTypesEnum cacheTypesEnum, Object id) {
        switch (cacheTypesEnum) {
            case DOM, STATIC -> {
                return switch (cacheMethodEnum) {
                    case LOCAL -> GlobalStaticLocalCache.read(cacheTypesEnum, (String) id);
                    case REDIS -> GlobalStaticRemoteCache.read(cacheTypesEnum, (String) id);
                };
            }
            default -> {
                return genericCache.read(cacheTypesEnum, id);
            }
        }
    }

    @Override
    public void update(CacheTypesEnum cacheTypesEnum, Object id, Object data) {
        switch (cacheTypesEnum) {
            case DOM:
            case STATIC: {
                switch (cacheMethodEnum) {
                    case LOCAL -> GlobalStaticLocalCache.update(cacheTypesEnum, (String) id, (List) data);
                    case REDIS -> GlobalStaticRemoteCache.update(cacheTypesEnum, (String) id, (String) data);
                }
            }
            default: {
                genericCache.update(cacheTypesEnum, id, data);
            }
        }
    }

    @Override
    public void delete(CacheTypesEnum cacheTypesEnum, Object id) {
        genericCache.delete(cacheTypesEnum, id);
    }
}
