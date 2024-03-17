package com.fiap.techchallenge.production.gateway.port;

public interface CachePort {

    Object getValueByKey(String key);

    void setKeyWithoutExpirationTime(String key, Object value);

}
