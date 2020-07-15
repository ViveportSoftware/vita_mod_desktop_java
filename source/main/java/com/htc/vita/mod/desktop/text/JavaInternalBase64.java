package com.htc.vita.mod.desktop.text;

import com.htc.vita.core.log.Logger;
import com.htc.vita.core.text.Base64Option;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

/* package */ class JavaInternalBase64 {
    private static boolean isReady = false;
    private static boolean isReadyChecked = false;

    private JavaInternalBase64() {
    }

    private static boolean checkIsReady() {
        try {
            Class.forName("sun.misc.BASE64Decoder");
            return true;
        } catch (ClassNotFoundException e) {
            // Skip
        }
        return false;
    }

    /* package */ static byte[] decode(String data) throws IOException {
        String buffer = data.replace(
                "\r\n",
                ""
        ).replace(
                "-",
                "+"
        ).replace(
                "_",
                "/"
        );
        int missingPaddingLength = 4 - buffer.length() % 4;
        if (missingPaddingLength == 1) {
            buffer += "=";
        }
        if (missingPaddingLength == 2) {
            buffer += "==";
        }
        if (missingPaddingLength == 3) {
            buffer += "===";
        }
        return new BASE64Decoder().decodeBuffer(buffer);
    }

    /* package */ static String encodeToString(
            byte[] data,
            Base64Option base64Option) {
        String mimeEncoded = new BASE64Encoder().encode(data);
        if (base64Option == Base64Option.Mime) {
            return mimeEncoded;
        }

        String basicEncoded = mimeEncoded.replace(
                "\r\n",
                ""
        ).replace(
                "-",
                "+"
        ).replace(
                "_",
                "/"
        );
        if (base64Option == Base64Option.Basic) {
            return basicEncoded;
        }

        String urlSafeEncoded = basicEncoded.replace(
                "=",
                ""
        );
        if (base64Option == Base64Option.UrlSafe) {
            return urlSafeEncoded;
        }
        Logger.getInstance(JavaInternalBase64.class.getSimpleName()).error("Can not find available implementation");
        return null;
    }

    /* package */ static boolean isReady() {
        if (isReadyChecked) {
            return isReady;
        }

        isReady = checkIsReady();
        isReadyChecked = true;
        return isReady;
    }
}
