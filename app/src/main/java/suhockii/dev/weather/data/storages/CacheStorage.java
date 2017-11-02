package suhockii.dev.weather.data.storages;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

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
    public <T> Storage<String> put(@NonNull String key, @NonNull T value) {
        String representation = String.valueOf(value);
        FileOutputStream outputStream = null;

        try {
            File file = new File(cacheDirectory, key);
            outputStream = new FileOutputStream(file);
            outputStream.write(representation.getBytes());
            Log.d(TAG, "Putted key (/folder): " + key + " value: " + value);
        } catch (IOException exception) {
            Log.e(TAG, "Cache cannot be writtable");
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                Log.e("tag", e.toString());
            }
        }

        return this;
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        try {
            BufferedReader reader = openFile(key);
            String buffer = reader.readLine();
            reader.close();
            return Boolean.valueOf(buffer);
        } catch (Exception exception) {
            exception.printStackTrace();
            return defaultValue;
        }
    }

    @Override
    public int getInteger(String key, int defaultValue) {
        try {
            BufferedReader reader = openFile(key);
            String buffer = reader.readLine();
            reader.close();
            return Integer.valueOf(buffer);
        } catch (Exception exception) {
            exception.printStackTrace();
            return defaultValue;
        }
    }

    @Override
    public float getFloat(String key, float defaultValue) {
        try {
            BufferedReader reader = openFile(key);
            String buffer = reader.readLine();
            reader.close();
            return Float.valueOf(buffer);
        } catch (Exception exception) {
            exception.printStackTrace();
            return defaultValue;
        }
    }

    @Override
    public long getLong(String key, long defaultValue) {
        try {
            BufferedReader reader = openFile(key);
            String buffer = reader.readLine();
            reader.close();
            return Long.valueOf(buffer);
        } catch (Exception exception) {
            exception.printStackTrace();
            return defaultValue;
        }
    }

    @Override
    public String getString(String key, String defaultValue) {
        try {
            BufferedReader reader = openFile(key);
            StringBuilder sb = new StringBuilder();
            String line;
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
    public void remove(String key) {
        File file = new File(cacheDirectory, key);
        file.delete();
    }

    @Override
    public void clear() {
        cacheDirectory.delete();
    }

    private BufferedReader openFile(String key) throws IOException {
        File file = new File(cacheDirectory, key);
        FileInputStream inputStream = new FileInputStream(file);
        return new BufferedReader(new InputStreamReader(inputStream));
    }
}
