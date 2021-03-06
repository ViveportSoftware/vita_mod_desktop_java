package com.htc.vita.mod.desktop.config;

import com.htc.vita.core.config.Config;
import com.htc.vita.core.log.Logger;
import com.htc.vita.core.util.StringUtils;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinReg;

import java.util.*;

public class NativeConfig extends Config {
    private static final String KEY_PATH = "SOFTWARE\\HTC\\Vita\\Config";

    private Map<String, String> mConfigMap;

    public NativeConfig() {
        mConfigMap = new HashMap<String, String>();
        mConfigMap = appendByStringValue(
                mConfigMap,
                WinReg.HKEY_LOCAL_MACHINE,
                KEY_PATH,
                WinNT.KEY_WOW64_32KEY
        );
        mConfigMap = appendByStringValue(
                mConfigMap,
                WinReg.HKEY_LOCAL_MACHINE,
                KEY_PATH,
                WinNT.KEY_WOW64_64KEY
        );
    }

    @Override
    protected Set<String> onAllKeys() {
        if (mConfigMap == null) {
            return new HashSet<String>();
        }
        return mConfigMap.keySet();
    }

    private static Map<String, String> appendByStringValue(
            Map<String, String> properties,
            WinReg.HKEY hKey,
            String keyPath,
            int samDesiredExtra) {
        if (properties == null || StringUtils.isNullOrWhiteSpace(keyPath)) {
            return properties;
        }

        String osName = StringUtils.toRootLocaleLowerCase(System.getProperty("os.name"));
        if (!osName.contains("win")) {
            return properties;
        }

        try {
            boolean isKeyPathExists = Advapi32Util.registryKeyExists(
                    hKey,
                    keyPath,
                    samDesiredExtra
            );
            if (!isKeyPathExists) {
                return properties;
            }

            TreeMap<String, Object> valueMap = Advapi32Util.registryGetValues(
                    hKey,
                    keyPath,
                    samDesiredExtra
            );
            for (String valueName: valueMap.keySet()) {
                if (valueName == null) {
                    continue;
                }

                Object valueData = valueMap.get(valueName);
                if (!(valueData instanceof String)) {
                    continue;
                }

                properties.put(
                        valueName,
                        (String)valueData
                );
            }
        } catch (Exception e) {
            Logger.getInstance(NativeConfig.class.getSimpleName()).error(e.toString());
        }
        return properties;
    }

    @Override
    protected String onGet(String key) {
        String result = null;
        if (mConfigMap != null && mConfigMap.containsKey(key)) {
            result = mConfigMap.get(key);
        }
        return result;
    }

    @Override
    protected boolean onHasKey(String key) {
        return mConfigMap != null && mConfigMap.containsKey(key);
    }
}
