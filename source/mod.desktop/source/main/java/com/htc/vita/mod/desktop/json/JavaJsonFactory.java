package com.htc.vita.mod.desktop.json;

import com.htc.vita.core.json.JsonArray;
import com.htc.vita.core.json.JsonFactory;
import com.htc.vita.core.json.JsonObject;
import com.htc.vita.core.log.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class JavaJsonFactory extends JsonFactory {
    @Override
    protected JsonArray onCreateJsonArray() {
        return new JavaJsonArray(new JSONArray());
    }

    @Override
    protected JsonObject onCreateJsonObject() {
        return new JavaJsonObject(new JSONObject());
    }

    @Override
    protected <T> T onDeserializeObject(
            String content,
            Class<T> type) {
        Logger.getInstance(JavaJsonFactory.class.getSimpleName()).error(
                "This implementation of JsonFactory does not support object deserialization!!"
        );
        return null;
    }

    @Override
    protected JsonArray onGetJsonArray(String content) {
        return new JavaJsonArray(new JSONArray(content));
    }

    @Override
    protected JsonObject onGetJsonObject(String content) {
        return new JavaJsonObject(new JSONObject(content));
    }

    @Override
    protected String onSerializeObject(Object content) {
        Logger.getInstance(JavaJsonFactory.class.getSimpleName()).error(
                "This implementation of JsonFactory does not support object serialization!!"
        );
        return null;
    }
}
