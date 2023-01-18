package tech.tresearchgroup.cao.controller;

import tech.tresearchgroup.cao.model.CacheTypesEnum;

public interface GenericCache {
    void create(CacheTypesEnum cacheTypesEnum, Object id, Object data);

    Object read(CacheTypesEnum cacheTypesEnum, Object id);

    void update(CacheTypesEnum cacheTypesEnum, Object id, Object data);

    void delete(CacheTypesEnum cacheTypesEnum, Object id);
}
