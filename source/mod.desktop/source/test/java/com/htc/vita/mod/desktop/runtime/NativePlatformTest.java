package com.htc.vita.mod.desktop.runtime;

import com.htc.vita.core.runtime.Platform;
import com.htc.vita.core.util.StringUtils;
import org.junit.Assert;
import org.junit.Test;

public class NativePlatformTest {
    @Test
    public void java_0_getInstance() {
        Platform.register(NativePlatform.class);
        Platform platform = Platform.getInstance();
        Assert.assertNotNull(platform);
        Assert.assertTrue(platform instanceof NativePlatform);

        platform = Platform.getInstance(NativePlatform.class);
        Assert.assertNotNull(platform);
        Assert.assertTrue(platform instanceof NativePlatform);
    }

    @Test
    public void java_1_getMachineId() {
        Platform.register(NativePlatform.class);
        Platform platform = Platform.getInstance();
        Assert.assertNotNull(platform);

        String machineId = platform.getMachineId();
        Assert.assertFalse(StringUtils.isNullOrWhiteSpace(machineId));
        Assert.assertNotEquals(NativePlatform.UNKNOWN_MACHINE_ID, machineId);
    }
}
