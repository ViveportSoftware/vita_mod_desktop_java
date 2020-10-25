package com.htc.vita.mod.desktop.config;

import com.htc.vita.core.config.Config;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class NativeConfigTest {
    @Test
    public void java_0_getInstance() {
        Config.register(NativeConfig.class);
        Config config = Config.getInstance();
        Assert.assertNotNull(config);
        Assert.assertTrue(config instanceof NativeConfig);

        config = Config.getInstance(NativeConfig.class);
        Assert.assertNotNull(config);
        Assert.assertTrue(config instanceof NativeConfig);
    }

    @Ignore("PredefinedGlobalDataNeeded")
    @Test
    public void java_1_hasKey() {
        Config.register(NativeConfig.class);
        Config config = Config.getInstance();
        Assert.assertNotNull(config);

        Assert.assertTrue(config.hasKey("Test1"));
    }

    @Ignore("PredefinedGlobalDataNeeded")
    @Test
    public void java_2_get() {
        Config.register(NativeConfig.class);
        Config config = Config.getInstance();
        Assert.assertNotNull(config);

        String value = config.get("Test1");
        Assert.assertNotNull(value);
    }
}
