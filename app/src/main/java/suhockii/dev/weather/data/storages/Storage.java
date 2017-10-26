package suhockii.dev.weather.data.storages;

/**
 * Created by alexander on 29/04/2017.
 */

public interface Storage<K> {
    <T> Storage<K> put(K key, T obj);

    boolean getBoolean(K key, boolean defaultValue);

    int getInteger(K key, int defaultValue);

    float getFloat(K key, float defaultValue);

    long getLong(K key, long defaultValue);

    String getString(K key, String defaultValue);

    void remove(K key);

    void clear();
}
