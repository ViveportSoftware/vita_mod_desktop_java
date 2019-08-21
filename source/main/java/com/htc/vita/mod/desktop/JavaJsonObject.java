package com.htc.vita.mod.desktop;

import com.htc.vita.core.json.JsonArray;
import com.htc.vita.core.json.JsonObject;
import com.htc.vita.core.log.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

public class JavaJsonObject extends JsonObject {
    private final JSONObject mJSONObject;

    public JavaJsonObject(JSONObject jsonObject) {
        mJSONObject = jsonObject;
    }

    public JSONObject getInnerInstance() {
        return mJSONObject;
    }

    @Override
    protected Set<String> onAllKeys() {
        if (mJSONObject == null) {
            return new HashSet<String>();
        }
        return mJSONObject.keySet();
    }

    @Override
    protected boolean onHasKey(String key) {
        if (mJSONObject == null) {
            return false;
        }
        return mJSONObject.has(key);
    }

    @Override
    protected boolean onParseBoolean(String key, boolean defaultValue) {
        boolean result = defaultValue;
        if (mJSONObject == null) {
            return result;
        }
        try {
            result = mJSONObject.getBoolean(key);
        } catch (Exception e) {
            Logger.getInstance(JavaJsonObject.class.getSimpleName()).error("Can not parse boolean value by key: " + key);
        }
        return result;
    }

    @Override
    protected double onParseDouble(String key, double defaultValue) {
        double result = defaultValue;
        if (mJSONObject == null) {
            return result;
        }
        try {
            result = mJSONObject.getDouble(key);
        } catch (Exception e) {
            Logger.getInstance(JavaJsonObject.class.getSimpleName()).error("Can not parse double value by key: " + key);
        }
        return result;
    }

    @Override
    protected float onParseFloat(String key, float defaultValue) {
        float result = defaultValue;
        if (mJSONObject == null) {
            return result;
        }
        try {
            result = mJSONObject.getFloat(key);
        } catch (Exception e) {
            Logger.getInstance(JavaJsonObject.class.getSimpleName()).error("Can not parse float value by key: " + key);
        }
        return result;
    }

    @Override
    protected int onParseInt(String key, int defaultValue) {
        int result = defaultValue;
        if (mJSONObject == null) {
            return result;
        }
        try {
            result = mJSONObject.getInt(key);
        } catch (Exception e) {
            Logger.getInstance(JavaJsonObject.class.getSimpleName()).error("Can not parse int value by key: " + key);
        }
        return result;
    }

    @Override
    protected long onParseLong(String key, long defaultValue) {
        long result = defaultValue;
        if (mJSONObject == null) {
            return result;
        }
        try {
            result = mJSONObject.getLong(key);
        } catch (Exception e) {
            Logger.getInstance(JavaJsonObject.class.getSimpleName()).error("Can not parse long value by key: " + key);
        }
        return result;
    }

    @Override
    protected String onParseString(String key, String defaultValue) {
        String result = defaultValue;
        if (mJSONObject == null) {
            return result;
        }
        try {
            result = mJSONObject.getString(key);
        } catch (Exception e) {
            Logger.getInstance(JavaJsonObject.class.getSimpleName()).error("Can not parse String value by key: " + key);
        }
        return result;
    }

    @Override
    protected JsonArray onParseJsonArray(String key) {
        if (mJSONObject == null) {
            return null;
        }
        try {
            JSONArray jsonArray = mJSONObject.getJSONArray(key);
            if (jsonArray != null) {
                return new JavaJsonArray(jsonArray);
            }
        } catch (Exception e) {
            Logger.getInstance(JavaJsonObject.class.getSimpleName()).error("Can not parse JsonArray value by key: " + key);
        }
        return null;
    }

    @Override
    protected JsonObject onParseJsonObject(String key) {
        if (mJSONObject == null) {
            return null;
        }
        try {
            JSONObject jsonObject = mJSONObject.getJSONObject(key);
            if (jsonObject != null) {
                return new JavaJsonObject(jsonObject);
            }
        } catch (Exception e) {
            Logger.getInstance(JavaJsonObject.class.getSimpleName()).error("Can not parse JsonObject value by key: " + key);
        }
        return null;
    }

    @Override
    protected JsonObject onPutBoolean(String key, boolean value) {
        if (mJSONObject == null) {
            return this;
        }
        mJSONObject.put(key, value);
        return this;
    }

    @Override
    protected JsonObject onPutDouble(String key, double value) {
        if (mJSONObject == null) {
            return this;
        }
        mJSONObject.put(key, value);
        return this;
    }

    @Override
    protected JsonObject onPutFloat(String key, float value) {
        if (mJSONObject == null) {
            return this;
        }
        mJSONObject.put(key, value);
        return this;
    }

    @Override
    protected JsonObject onPutInt(String key, int value) {
        if (mJSONObject == null) {
            return this;
        }
        mJSONObject.put(key, value);
        return this;
    }

    @Override
    protected JsonObject onPutLong(String key, long value) {
        if (mJSONObject == null) {
            return this;
        }
        mJSONObject.put(key, value);
        return this;
    }

    @Override
    protected JsonObject onPutString(String key, String value) {
        if (mJSONObject == null) {
            return this;
        }
        mJSONObject.put(key, value);
        return this;
    }

    @Override
    protected JsonObject onPutJsonArray(String key, JsonArray value) {
        if (mJSONObject == null) {
            return this;
        }
        mJSONObject.put(key, ((JavaJsonArray)value).getInnerInstance());
        return this;
    }

    @Override
    protected JsonObject onPutJsonObject(String key, JsonObject value) {
        if (mJSONObject == null) {
            return this;
        }
        mJSONObject.put(key, ((JavaJsonObject)value).getInnerInstance());
        return this;
    }

    @Override
    public String toString() {
        if (mJSONObject == null) {
            return "";
        }
        return mJSONObject.toString();
    }

    @Override
    protected String onToPrettyString() {
        if (mJSONObject == null) {
            return "";
        }
        return mJSONObject.toString(2);
    }
}
