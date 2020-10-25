package com.htc.vita.mod.desktop.auth;

import com.htc.vita.core.auth.OAuth2AuthorizationCodeUserAgent;
import com.htc.vita.core.concurrent.CancellationToken;
import com.htc.vita.core.log.Logger;
import com.htc.vita.core.util.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class DesktopOAuth2AuthorizationCodeUserAgent extends OAuth2AuthorizationCodeUserAgent {
    private final Object mLock = new Object();

    private URI mAuthorizationUri;
    private CancellationToken mCancellationToken;
    private boolean mIsClosed;

    @Override
    public void close() {
        mIsClosed = true;
    }

    @Override
    protected OAuth2AuthorizationCodeUserAgent onInitialize(
            Map<String, Object> options,
            CancellationToken cancellationToken) {
        synchronized (mLock) {
            throwIfClosed();

            mCancellationToken = cancellationToken;
            mAuthorizationUri = parseAuthorizationUri(options);
            try {
                onOverrideInitialize(options);
            } catch (Exception e) {
                Logger.getInstance(DesktopOAuth2AuthorizationCodeUserAgent.class.getSimpleName()).error(e.toString());
            }
        }
        return this;
    }

    @Override
    protected LaunchResult onLaunch() {
        synchronized (mLock) {
            throwIfClosed();

            if (mAuthorizationUri == null) {
                return new LaunchResult()
                        .setStatus(LaunchStatus.InvalidAuthorizationUri);
            }
            if (mCancellationToken.isCancellationRequested()) {
                return new LaunchResult()
                        .setStatus(LaunchStatus.CancelledLaunching);
            }
            String scheme = mAuthorizationUri.getScheme();
            if (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme)) {
                Logger.getInstance(DesktopOAuth2AuthorizationCodeUserAgent.class.getSimpleName()).error(StringUtils.rootLocaleFormat(
                        "Can not find valid scheme: %s",
                        scheme
                ));
                return new LaunchResult()
                        .setStatus(LaunchStatus.InvalidAuthorizationUri);
            }

            try {
                if (onOverrideLaunch()) {
                    return new LaunchResult()
                            .setStatus(LaunchStatus.Ok);
                }
            } catch (Exception e) {
                Logger.getInstance(DesktopOAuth2AuthorizationCodeUserAgent.class.getSimpleName()).error(e.toString());
            }

            return new LaunchResult()
                    .setStatus(LaunchStatus.UnsupportedUserAgent);
        }
    }

    protected boolean onOverrideInitialize(Map<String, Object> options) throws Exception {
        return true;
    }

    protected boolean onOverrideLaunch() throws Exception {
        String osName = StringUtils.toRootLocaleLowerCase(System.getProperty("os.name"));
        if (osName.contains("win")) {
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(StringUtils.rootLocaleFormat(
                    // "explorer \"%s\"",
                    "rundll32 url.dll,FileProtocolHandler \"%s\"",
                    mAuthorizationUri.toString()
            ));
            return true;
        }

        return false;
    }

    protected static URI parseAuthorizationUri(Map<String, Object> options) {
        if (options == null || options.isEmpty()) {
            return null;
        }
        Object authorizationUriAsObject = options.get(OPTION_AUTHORIZATION_URL);
        if (authorizationUriAsObject == null) {
            return null;
        }
        String authorizationUriString = null;
        if (authorizationUriAsObject instanceof String) {
            authorizationUriString = (String) authorizationUriAsObject;
        }
        if (StringUtils.isNullOrWhiteSpace(authorizationUriString)) {
            return null;
        }

        try {
            return new URI(authorizationUriString);
        } catch (URISyntaxException e) {
            Logger.getInstance(DesktopOAuth2AuthorizationCodeUserAgent.class.getSimpleName()).error(e.toString());
        }
        return null;
    }

    private void throwIfClosed() {
        if (mIsClosed) {
            throw new IllegalStateException(String.format(
                    "%s is already closed",
                    DesktopOAuth2AuthorizationCodeUserAgent.class.getSimpleName()
            ));
        }
    }
}
