package com.htc.vita.mod.desktop.runtime;

import com.htc.vita.core.log.Logger;
import com.htc.vita.core.runtime.Platform;
import com.htc.vita.core.util.StringUtils;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;

public class NativePlatform extends Platform {
    public static final String UNKNOWN_MACHINE_ID = "UNKNOWN-DESKTOP-MACHINE-ID";

    @Override
    protected String onGetMachineId() {
        String machineId = onOverrideGetMachineId();
        if (!StringUtils.isNullOrWhiteSpace(machineId)) {
            return machineId;
        }
        Logger.getInstance(NativePlatform.class.getSimpleName()).error("Can not find valid machine id");
        return UNKNOWN_MACHINE_ID;
    }

    protected String onOverrideGetMachineId() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            return Advapi32Util.registryGetStringValue(
                    WinReg.HKEY_LOCAL_MACHINE,
                    "SOFTWARE\\Microsoft\\Cryptography",
                    "MachineGuid"
            );
        }

        return null;
    }
}
