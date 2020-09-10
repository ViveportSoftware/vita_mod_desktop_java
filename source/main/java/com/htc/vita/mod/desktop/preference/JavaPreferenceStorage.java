package com.htc.vita.mod.desktop.preference;

import com.htc.vita.core.json.JsonFactory;
import com.htc.vita.core.json.JsonObject;
import com.htc.vita.core.log.Logger;
import com.htc.vita.core.preference.PreferenceStorage;
import com.htc.vita.core.util.StringUtils;
import com.htc.vita.mod.desktop.io.FileUtils;
import com.htc.vita.mod.desktop.io.PathUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class JavaPreferenceStorage extends PreferenceStorage {
    private String mPath;

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

    @Override
    protected boolean onSave(Map<String, String> data) {
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
                    Logger.getInstance().error(String.format(
                            Locale.ROOT,
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
            Logger.getInstance().error(String.format(
                    Locale.ROOT,
                    "Can not write text to file: \"%s\"",
                    mPath
            ));
        }
        return success;
    }
}
