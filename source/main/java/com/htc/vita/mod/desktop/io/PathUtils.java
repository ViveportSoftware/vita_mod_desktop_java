package com.htc.vita.mod.desktop.io;

import com.htc.vita.core.util.StringUtils;

import java.io.File;

public class PathUtils {
    public static String combine(String... params) {
        if (params == null) {
            return null;
        }
        if (params.length <= 0) {
            return null;
        }

        if (params.length == 1) {
            return params[0];
        }

        File file = new File(params[0]);
        for (int i = 1; i < params.length; i++) {
            file = new File(
                    file,
                    params[i]
            );
        }
        return file.getPath();
    }

    public static String getAppDataPath() {
        String path = getAppDataPathForWindows();
        if (StringUtils.isNullOrWhiteSpace(path)) {
            path = getAppDataPathForUnix();
        }
        return path;
    }

    private static String getAppDataPathForWindows() {
        String path = System.getenv("LocalAppData");
        if (StringUtils.isNullOrWhiteSpace(path)) {
            return "";
        }
        return PathUtils.combine(
                path,
                "HTC"
        );
    }

    private static String getAppDataPathForUnix() {
        String path = System.getenv("HOME");
        if (StringUtils.isNullOrWhiteSpace(path)) {
            return "";
        }
        return PathUtils.combine(
                path,
                ".htc"
        );
    }
}
