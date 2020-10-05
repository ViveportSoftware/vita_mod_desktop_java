package com.htc.vita.mod.desktop.auth;

import com.htc.vita.core.auth.OAuth2AuthorizationCodeReceiver;
import com.htc.vita.core.concurrent.CancellationToken;
import com.htc.vita.core.log.Logger;
import com.htc.vita.core.net.HttpUtils;
import com.htc.vita.core.util.Convert;
import com.htc.vita.core.util.StringUtils;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

public class DesktopOAuth2AuthorizationCodeReceiver extends OAuth2AuthorizationCodeReceiver {
    private static final String DEFAULT_ENCODED_FAVICON = ""
            + "AAABAAEAEBAAAAEAIABoBAAAFgAAACgAAAAQAAAAIAAAAAEAIAAAAAAAAAQAAAAA"
            + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAANVZyMAAAAAAAAAAAAAAAAAAA"
            + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAANVZyMDVW"
            + "cv82ZH/ENVZyEAAAAAAAAAAAAAAAAAAAAAAAAAAANVZyQDVWchAAAAAAAAAAAAAA"
            + "AAAAAAAAAAAAAAAAAAA1VnL/SrLK/zVadvg2ZoJbAAAAADVWciA2ZYCQNV978DVW"
            + "cv81VnJAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAANVZy/13j+f9W2vD/PXaR/TVh"
            + "fOI0Xnr/LavG/yXM6P8zZYH/NVZyEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADVW"
            + "cv9d4/n/XeP5/1vi+P9Atcz/LdDq/yfQ6/8nyuX/NmWA2AAAAAAAAAAAAAAAAAAA"
            + "AAAAAAAAAAAAAAAAAAA1Wnb4Wd/1/13j+f9d4/n/P93z/zvc8v8v1e7/LLDL/zVg"
            + "fHkAAAAAAAAAAAAAAAAAAAAAAAAAADVWchA1YXzQSaC5/Xrm+/9x5fv/X+P5/z/d"
            + "8/873PL/L9Xu/zF/m/w1VnJAAAAAAAAAAAAAAAAAAAAAADVWcjA1XnruWMbd/3/n"
            + "/P+B5/z/gef8/3rm+/9M4PX/O9zy/y/V7v8rt9L/NWF84jVWchAAAAAAAAAAADZm"
            + "gls1W3f6T8ff/3jm+/+B5/z/hOf8/4Tn/P+B5/z/dOX6/zvc8v862/H/K9Ls/y+i"
            + "vf81YXy/AAAAAAAAAAA2a4ZMNmWA2Dhfev9Ckqv7UMLZ/3Dh9v+c6/z/i+j8/4Dn"
            + "+/9E3vT/O9zy/zvc8v8x0er/MYai/TZifp4AAAAAAAAAAAAAAAAAAAAANVZyQDZl"
            + "gJA4Y374guj7/47o/P945vv/SLDI/zZwi+81XHj7NVZy/zVWcv81VnL/NVZyEAAA"
            + "AAAAAAAAAAAAAAAAAAAAAAAANmJ+nlzH3f+L6fz/cuP5/zhjfvg1VnJANVZyIAAA"
            + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADVWcjA/fpn9b+X7/1CZ"
            + "sv41VnJQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
            + "AAAAAAAANWF84kqpwv82ZH/EAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
            + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAADVgfHk1W3f5NVZyIAAAAAAAAAAAAAAAAAAA"
            + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAANVZyMAAA"
            + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA//8AAPP/AADxxwAA8AcAAPAH"
            + "AADwDwAA4A8AAMAHAACAAwAAgAEAAPABAAD4PwAA/H8AAPx/AAD+/wAA//8AAA==";
    private static final String REQUEST_QUERY_PARAM_CODE = "code";
    private static final String RESPONSE_HEADER_CONTENT_TYPE = "Content-Type";
    private static final String RESPONSE_MIME_IMAGE_X_ICON = "image/x-icon";
    private static final String RESPONSE_MIME_TEXT_PLAIN = "text/plain";

    private final CountDownLatch mCountDownLatch = new CountDownLatch(1);
    private final Object mLock = new Object();

    private String mAuthorizationCode = null;
    private CancellationToken mCancellationToken;
    private boolean mIsClosed;
    private HttpServer mHttpServer;

    @Override
    public void close() {
        if (mHttpServer != null) {
            mHttpServer.stop(0);
        }
        mCountDownLatch.countDown();
        mIsClosed = true;
    }

    private static void handleAuthorizationCode(HttpExchange httpExchange) throws IOException {
        Headers headers = httpExchange.getResponseHeaders();
        headers.add(
                RESPONSE_HEADER_CONTENT_TYPE,
                RESPONSE_MIME_TEXT_PLAIN
        );
        byte[] data = "".getBytes();
        httpExchange.sendResponseHeaders(
                200,
                data.length
        );
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(data);
        outputStream.close();
    }

    private static void handleBadRequest(HttpExchange httpExchange) throws IOException {
        Headers headers = httpExchange.getResponseHeaders();
        headers.add(
                RESPONSE_HEADER_CONTENT_TYPE,
                RESPONSE_MIME_TEXT_PLAIN
        );
        byte[] data = "".getBytes();
        httpExchange.sendResponseHeaders(
                400,
                data.length
        );
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(data);
        outputStream.close();
    }

    private static void handleFaviconRequest(HttpExchange httpExchange) throws IOException {
        Headers headers = httpExchange.getResponseHeaders();
        headers.add(
                RESPONSE_HEADER_CONTENT_TYPE,
                RESPONSE_MIME_IMAGE_X_ICON
        );
        byte[] data = Convert.fromBase64String(DEFAULT_ENCODED_FAVICON);
        httpExchange.sendResponseHeaders(
                200,
                data.length
        );
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(data);
        outputStream.close();
    }

    @Override
    protected OAuth2AuthorizationCodeReceiver onInitialize(
            Map<String, String> options,
            CancellationToken cancellationToken) {
        synchronized (mLock) {
            throwIfClosed();

            mCancellationToken = cancellationToken;
            int localPort = parsePortNumberFromRedirectUri(options);
            if (localPort < 0) {
                throw new IllegalArgumentException("The port number is invalid");
            }

            try {
                mHttpServer = HttpServer.create(new InetSocketAddress(localPort), 0);
                mHttpServer.setExecutor(Executors.newCachedThreadPool());
                mHttpServer.createContext("/", new HttpHandler() {
                    @Override
                    public void handle(HttpExchange httpExchange) throws IOException {
                        if (mCancellationToken.isCancellationRequested()) {
                            mCountDownLatch.countDown();
                        }

                        URI uri = httpExchange.getRequestURI();
                        String uriString = uri.toString();
                        if (uriString.startsWith("/favicon.ico")) {
                            handleFaviconRequest(httpExchange);
                            return;
                        }

                        Map<String, String> queryParams = HttpUtils.toQueryParams(uri.getQuery());
                        if (queryParams.isEmpty()) {
                            Logger.getInstance(DesktopOAuth2AuthorizationCodeReceiver.class.getSimpleName()).info(StringUtils.rootLocaleFormat(
                                    "Can not find query string in uri: %s",
                                    uriString
                            ));
                            handleBadRequest(httpExchange);
                            return;
                        }

                        if (!queryParams.containsKey(REQUEST_QUERY_PARAM_CODE)) {
                            Logger.getInstance(DesktopOAuth2AuthorizationCodeReceiver.class.getSimpleName()).info(StringUtils.rootLocaleFormat(
                                    "Can not find authorization code in uri: %s",
                                    uriString
                            ));
                            handleBadRequest(httpExchange);
                            return;
                        }

                        mAuthorizationCode = queryParams.get(REQUEST_QUERY_PARAM_CODE);
                        handleAuthorizationCode(httpExchange);
                        mCountDownLatch.countDown();
                    }
                });
                mHttpServer.start();
            } catch (IOException e) {
                Logger.getInstance(DesktopOAuth2AuthorizationCodeReceiver.class.getSimpleName()).error(e.toString());
            }
        }
        return this;
    }

    @Override
    protected ReceiveResult onReceive() {
        synchronized (mLock) {
            throwIfClosed();

            if (mCancellationToken.isCancellationRequested()) {
                return new ReceiveResult()
                        .setStatus(ReceiveStatus.CancelledReceiving);
            }
            if (mHttpServer == null) {
                return new ReceiveResult()
                        .setStatus(ReceiveStatus.UnsupportedReceiver);
            }
            try {
                mCountDownLatch.await();
                if (mCancellationToken.isCancellationRequested()) {
                    return new ReceiveResult()
                            .setStatus(ReceiveStatus.CancelledReceiving);
                }
                if (StringUtils.isNullOrWhiteSpace(mAuthorizationCode)) {
                    return new ReceiveResult()
                            .setStatus(ReceiveStatus.InvalidAuthorizationCode);
                }
                return new ReceiveResult()
                        .setStatus(ReceiveStatus.Ok)
                        .setCode(mAuthorizationCode);
            } catch (InterruptedException e) {
                Logger.getInstance(DesktopOAuth2AuthorizationCodeReceiver.class.getSimpleName()).error(e.toString());
            }
            return new ReceiveResult()
                    .setStatus(ReceiveStatus.CancelledReceiving);
        }
    }

    private static int parsePortNumberFromRedirectUri(Map<String, String> options) {
        int result = -1;
        if (options == null || options.isEmpty()) {
            return result;
        }

        String redirectUriString = options.get(OPTION_REDIRECT_URI);
        if (StringUtils.isNullOrWhiteSpace(redirectUriString)) {
            return result;
        }

        try {
            URI redirectUri = new URI(redirectUriString);
            result = redirectUri.getPort();
            if (result < 0) {
                if (redirectUri.getScheme().equalsIgnoreCase("http")) {
                    result = 80;
                }
                if (redirectUri.getScheme().equalsIgnoreCase("https")) {
                    result = 443;
                }
            }
        } catch (URISyntaxException e) {
            Logger.getInstance(DesktopOAuth2AuthorizationCodeReceiver.class.getSimpleName()).error(e.toString());
        }
        return result;
    }

    private void throwIfClosed() {
        if (mIsClosed) {
            throw new IllegalStateException(String.format(
                    "%s is already closed",
                    DesktopOAuth2AuthorizationCodeReceiver.class.getSimpleName()
            ));
        }
    }
}
