package org.remdev.android.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import org.remdev.android.common.Constants;
import org.remdev.timlog.LogFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import timber.log.Timber;

/**
 * Created by Alexandr.Salin on 7/23/15.
 */
public class PrefsUtil {
    public static final String TAG = "PREFS";
    public static final String PREFERENCES_NAME = "APP_SHARED_PREFS";
    private static PrefsUtil instance;
    private SharedPreferences mSharedPreferences;

    public PrefsUtil(Context ctx) {
        Validator.requireNonNull(ctx, "context can't be null, inherit BaseApplication.class or use getInstance(Context ctx) method");
        mSharedPreferences = ctx.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static PrefsUtil getInstance() {
        if (instance == null) {
            synchronized (PrefsUtil.class) {
                if (instance == null) {
                    instance = new PrefsUtil(ContextUtil.getAppContext());
                }
            }
        }
        return instance;
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void clearValue(String key) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public String getString(String key) {
        String value = mSharedPreferences.getString(key, Constants.EMPTY_STRING);
        return value;
    }

    public String getStringThrowIfNotSet(String key) {
        String value = mSharedPreferences.getString(key, Constants.EMPTY_STRING);
        Validator.requireNonNull(value, "Please setup value for key: " + key);
        return value;
    }

    public String getString(String key, String defValue) {
        return mSharedPreferences.getString(key, defValue);
    }

    public int getInt(String key) {
        return getInt(key, -1);
    }

    public int getInt(String key, int defValue) {
        int value = mSharedPreferences.getInt(key, defValue);
        return value;
    }

    public void putLong(String key, long value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public long getLong(String key, long defValue) {
        long result = mSharedPreferences.getLong(key, defValue);
        return result;
    }

    public void putBool(String key, boolean value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(key, value);
        Timber.tag(TAG).d("put bool key %s, value {}", key, value);
        editor.apply();
    }

    public boolean getBoolean(String key) {
        boolean value = mSharedPreferences.getBoolean(key, false);
        Timber.tag(TAG).d("get bool key {}, value %s", key, value);
        return value;
    }

    public boolean getBoolean(String key, boolean defValue) {
        boolean value = mSharedPreferences.getBoolean(key, defValue);
        Timber.tag(TAG).d("get bool key %s, value %s", key, value);
        return value;
    }

    public void putStringSet(String key, Set<String> value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putStringSet(key, value);
        editor.apply();
    }

    public Set<String> getStringSet(String key) {
        return mSharedPreferences.getStringSet(key, new HashSet<String>());
    }

    public Map<String, Object> getAllPrefs() {
        Map<String, Object> result = new HashMap<>();
        Map<String, ?> keys = mSharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public void putFloat(String key, float value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public void put(String key, @Nullable String value, Class<?> valueClass) {
        if (valueClass.isAssignableFrom(Integer.class)) {
            int intValue = 0;
            try {
                intValue = Integer.parseInt(value);
            } catch (NumberFormatException ignored) {
            }
            putInt(key, intValue);

        } else if (valueClass.isAssignableFrom(Long.class)) {
            long longValue = 0;
            try {
                longValue = Long.parseLong(value);
            } catch (NumberFormatException ignored) {
            }
            putLong(key, longValue);

        } else if (valueClass.isAssignableFrom(Float.class)) {
            float floatValue = 0.0f;
            try {
                floatValue = Float.parseFloat(value);
            } catch (NumberFormatException ignored) {
            }
            putFloat(key, floatValue);

        } else if (valueClass.isAssignableFrom(Boolean.class)) {
            boolean booleanValue = false;
            try {
                booleanValue = Boolean.parseBoolean(value);
            } catch (NumberFormatException ignored) {
            }
            putBoolean(key, booleanValue);

        } else if (valueClass.isAssignableFrom(Set.class)) {
            putStringSet(key, (value == null) ? null : Collections.singleton(value));

        } else {
            putString(key, value);
        }
    }

    public void flushPrefsToLogs() {
        int count = 0;
        for (Map.Entry<String, Object> entry : getAllPrefs().entrySet()) {
            LogFactory.create(PrefsUtil.class).w("PREFS " + "Entry #" + (count++) + ", key: " + entry.getKey() + ", value: " + entry.getValue() + ", type: ");
        }
    }

    public void cleanAllPrefs() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}