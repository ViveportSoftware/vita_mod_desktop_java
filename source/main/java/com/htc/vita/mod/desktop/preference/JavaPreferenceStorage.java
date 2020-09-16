package com.htc.vita.mod.desktop.preference;

import com.htc.vita.core.internal.TaskRunner;
import com.htc.vita.core.json.JsonFactory;
import com.htc.vita.core.json.JsonObject;
import com.htc.vita.core.log.Logger;
import com.htc.vita.core.preference.PreferenceStorage;
import com.htc.vita.core.util.StringUtils;
import com.htc.vita.mod.desktop.io.FileUtils;
import com.htc.vita.mod.desktop.io.PathUtils;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class JavaPreferenceStorage extends PreferenceStorage {
    private final Object mLock = new Object();

    private String mPath;

    private Map<String, String> doLoad() {
        synchronized (mLock) {
            Map<String, String> result = new HashMap<String, String>();
            if (StringUtils.isNullOrWhiteSpace(mPath)) {
                mPath = getFilePath(
                        getCategory(),
                        getLabel()
                );
            }
            if (StringUtils.isNullOrWhiteSpace(mPath)) {
                return result;
            }

            File file = new File(mPath);
            if (!file.exists() || !file.isFile()) {
                return result;
            }

            String data = FileUtils.readAllText(file);
            JsonObject jsonObject = JsonFactory.getInstance().getJsonObject(data);
            if (jsonObject == null) {
                return result;
            }
            return jsonObject.toStringMap();
        }
    }

    private boolean doSave(Map<String, String> data) {
        synchronized (mLock) {
            if (data == null) {
                return false;
            }

            if (StringUtils.isNullOrWhiteSpace(mPath)) {
                mPath = getFilePath(
                        getCategory(),
                        getLabel()
                );
            }
            if (StringUtils.isNullOrWhiteSpace(mPath)) {
                return false;
            }

            boolean success;
            File file = new File(mPath);
            if (!file.exists()) {
                File parent = file.getParentFile();
                if (parent == null) {
                    return false;
                }

                if (!parent.exists()) {
                    success = parent.mkdirs();
                    if (!success) {
                        Logger.getInstance().error(StringUtils.rootLocaleFormat(
                                "Can not create parent folder for path: \"%s\"",
                                mPath
                        ));
                        return false;
                    }
                }
            }

            success = FileUtils.writeAllText(
                    file,
                    JsonFactory.getInstance()
                            .createJsonObject()
                            .putAllString(data)
                            .toPrettyString()
            );
            if (!success) {
                Logger.getInstance().error(StringUtils.rootLocaleFormat(
                        "Can not write text to file: \"%s\"",
                        mPath
                ));
            }
            return success;
        }
    }

    private static String getFilePath(
            String category,
            String label) {
        String path = PathUtils.getAppDataPath();
        if (StringUtils.isNullOrWhiteSpace(path))
        {
            return "";
        }
        return PathUtils.combine(
                path,
                category,
                label + ".pref"
        );
    }

    @Override
    protected Map<String, String> onLoad() {
        return doLoad();
    }

    @Override
    protected Future<Map<String, String>> onLoadAsync() {
        return TaskRunner.submit(new Callable<Map<String, String>>() {
                @Override
                public Map<String, String> call() {
                    return doLoad();
                }
        });
    }

    @Override
    protected boolean onSave(Map<String, String> data) {
        return doSave(data);
    }

    @Override
    protected Future<Boolean> onSaveAsync(final Map<String, String> data) {
        return TaskRunner.submit(new Callable<Boolean>() {
                @Override
                public Boolean call() {
                    return doSave(Collections.unmodifiableMap(data));
                }
        });
    }
}
