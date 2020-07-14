package com.htc.vita.mod.desktop.text;

import com.htc.vita.core.log.Logger;
import com.htc.vita.core.text.Base64;
import com.htc.vita.core.text.Base64Option;

public class JavaBase64 extends Base64 {
    @Override
    protected byte[] onDecode(
            String data,
            Base64Option base64Option) throws Exception {
        if (Java8Base64.isReady()) {
            return Java8Base64.decode(
                    data,
                    base64Option
            );
        }
        if (JavaInternalBase64.isReady()) {
            return JavaInternalBase64.decode(data);
        }
        Logger.getInstance(JavaBase64.class.getSimpleName()).error("Can not find available implementation");
        return null;
    }

    @Override
    protected String onEncodeToString(
            byte[] data,
            Base64Option base64Option) throws Exception {
        if (Java8Base64.isReady()) {
            return Java8Base64.encodeToString(
                    data,
                    base64Option
            );
        }
        if (JavaInternalBase64.isReady()) {
            return JavaInternalBase64.encodeToString(
                    data,
                    base64Option
            );
        }
        Logger.getInstance(JavaBase64.class.getSimpleName()).error("Can not find available implementation");
        return null;
    }
}
