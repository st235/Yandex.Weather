package sasd97.java_blog.xyz.yandexweather.data.storages;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;

/**
 * Created by alexander on 14/07/2017.
 */

public class CacheStorage implements Storage<String> {

    private static final String TAG = CacheStorage.class.getCanonicalName();

    private File cacheDirectory;

    public CacheStorage(@NonNull File cacheDirectory) {
            this.cacheDirectory = cacheDirectory;
    }

    @Override
    public <T> Storage<String> put(String key, T value) {
        String representation = String.valueOf(value);
        FileOutputStream outputStream = null;

        try {
            File file = new File(cacheDirectory, key);
            outputStream = new FileOutputStream(file);
            outputStream.write(representation.getBytes());
            outputStream.close();
            Log.d(TAG, "Putted key (/folder): " + key + " value: " + value);
        } catch (IOException exception) {
            Log.e(TAG, "Cache cannot be writtable");
        }

        return this;
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return false;
    }

    @Override
    public int getInteger(String key, int defaultValue) {
        return 0;
    }

    @Override
    public float getFloat(String key, float defaultValue) {
        return 0;
    }

    @Override
    public long getLong(String key, long defaultValue) {
        return 0;
    }

    @Override
    public String getString(String key, String defaultValue) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(openFile(key)));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
            return sb.toString();
        } catch (IOException io) {
            io.printStackTrace();
            return defaultValue;
        }
    }

    @Override
    public Set<String> getStringSet(String key) {
        return null;
    }

    @Override
    public void remove(String key) {

    }

    @Override
    public void clear() {
    }

    private FileInputStream openFile(String key) throws IOException {
        File file = new File(cacheDirectory, key);
        return new FileInputStream(file);
    }
}
