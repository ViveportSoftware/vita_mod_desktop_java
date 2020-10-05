package com.htc.vita.mod.desktop.auth;

import com.htc.vita.core.auth.OAuth2AuthorizationCodeUserAgent;
import com.htc.vita.core.auth.OAuth2AuthorizationCodeUserAgentFactory;
import com.htc.vita.core.concurrent.CancellationToken;
import com.htc.vita.core.util.MapBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class DesktopOAuth2AuthorizationCodeUserAgentFactoryTest {
    @Test
    public void desktop_0_getInstance() {
        OAuth2AuthorizationCodeUserAgentFactory.register(DesktopOAuth2AuthorizationCodeUserAgentFactory.class);
        OAuth2AuthorizationCodeUserAgentFactory oAuth2AuthorizationCodeUserAgentFactory = OAuth2AuthorizationCodeUserAgentFactory.getInstance();
        Assert.assertNotNull(oAuth2AuthorizationCodeUserAgentFactory);
        Assert.assertTrue(oAuth2AuthorizationCodeUserAgentFactory instanceof DesktopOAuth2AuthorizationCodeUserAgentFactory);

        oAuth2AuthorizationCodeUserAgentFactory = OAuth2AuthorizationCodeUserAgentFactory.getInstance(DesktopOAuth2AuthorizationCodeUserAgentFactory.class);
        Assert.assertNotNull(oAuth2AuthorizationCodeUserAgentFactory);
        Assert.assertTrue(oAuth2AuthorizationCodeUserAgentFactory instanceof DesktopOAuth2AuthorizationCodeUserAgentFactory);
    }

    @Test
    public void desktop_1_getUserAgent() {
        OAuth2AuthorizationCodeUserAgentFactory.register(DesktopOAuth2AuthorizationCodeUserAgentFactory.class);
        OAuth2AuthorizationCodeUserAgentFactory oAuth2AuthorizationCodeUserAgentFactory = OAuth2AuthorizationCodeUserAgentFactory.getInstance();
        Assert.assertNotNull(oAuth2AuthorizationCodeUserAgentFactory);
        OAuth2AuthorizationCodeUserAgent oAuth2AuthorizationCodeUserAgent = oAuth2AuthorizationCodeUserAgentFactory.getUserAgent();
        Assert.assertNotNull(oAuth2AuthorizationCodeUserAgent);
    }

    @Test
    public void oAuth2AuthorizationCodeUserAgent_0_launch() {
        OAuth2AuthorizationCodeUserAgentFactory.register(DesktopOAuth2AuthorizationCodeUserAgentFactory.class);
        OAuth2AuthorizationCodeUserAgentFactory oAuth2AuthorizationCodeUserAgentFactory = OAuth2AuthorizationCodeUserAgentFactory.getInstance();
        Assert.assertNotNull(oAuth2AuthorizationCodeUserAgentFactory);
        OAuth2AuthorizationCodeUserAgent oAuth2AuthorizationCodeUserAgent = oAuth2AuthorizationCodeUserAgentFactory.getUserAgent();
        Assert.assertNotNull(oAuth2AuthorizationCodeUserAgent);

        OAuth2AuthorizationCodeUserAgent.LaunchResult launchResult = oAuth2AuthorizationCodeUserAgent.launch();
        OAuth2AuthorizationCodeUserAgent.LaunchStatus launchStatus = launchResult.getStatus();
        Assert.assertEquals(OAuth2AuthorizationCodeUserAgent.LaunchStatus.InvalidAuthorizationUri, launchStatus);
    }

    @Test
    public void oAuth2AuthorizationCodeUserAgent_0_launch_withAuthorizationUri() {
        OAuth2AuthorizationCodeUserAgentFactory.register(DesktopOAuth2AuthorizationCodeUserAgentFactory.class);
        OAuth2AuthorizationCodeUserAgentFactory oAuth2AuthorizationCodeUserAgentFactory = OAuth2AuthorizationCodeUserAgentFactory.getInstance();
        Assert.assertNotNull(oAuth2AuthorizationCodeUserAgentFactory);

        String fullAuthorizationUriString = "https://www.google.com/";
        OAuth2AuthorizationCodeUserAgent oAuth2AuthorizationCodeUserAgent = oAuth2AuthorizationCodeUserAgentFactory.getUserAgent(
                new MapBuilder<String, Object>().put(
                        OAuth2AuthorizationCodeUserAgent.OPTION_AUTHORIZATION_URL,
                        fullAuthorizationUriString
                ).toMap(),
                CancellationToken.NONE
        );
        Assert.assertNotNull(oAuth2AuthorizationCodeUserAgent);

        OAuth2AuthorizationCodeUserAgent.LaunchResult launchResult = oAuth2AuthorizationCodeUserAgent.launch();
        OAuth2AuthorizationCodeUserAgent.LaunchStatus launchStatus = launchResult.getStatus();
        Assert.assertEquals(OAuth2AuthorizationCodeUserAgent.LaunchStatus.Ok, launchStatus);
    }

    @Test
    public void oAuth2AuthorizationCodeUserAgent_1_close() throws IOException {
        OAuth2AuthorizationCodeUserAgentFactory.register(DesktopOAuth2AuthorizationCodeUserAgentFactory.class);
        OAuth2AuthorizationCodeUserAgentFactory oAuth2AuthorizationCodeUserAgentFactory = OAuth2AuthorizationCodeUserAgentFactory.getInstance();
        Assert.assertNotNull(oAuth2AuthorizationCodeUserAgentFactory);
        OAuth2AuthorizationCodeUserAgent oAuth2AuthorizationCodeUserAgent = oAuth2AuthorizationCodeUserAgentFactory.getUserAgent();
        Assert.assertNotNull(oAuth2AuthorizationCodeUserAgent);

        OAuth2AuthorizationCodeUserAgent.LaunchResult launchResult = oAuth2AuthorizationCodeUserAgent.launch();
        OAuth2AuthorizationCodeUserAgent.LaunchStatus launchStatus = launchResult.getStatus();
        Assert.assertEquals(OAuth2AuthorizationCodeUserAgent.LaunchStatus.InvalidAuthorizationUri, launchStatus);
        oAuth2AuthorizationCodeUserAgent.close();
        Exception exception = null;
        try {
            oAuth2AuthorizationCodeUserAgent.launch();
        } catch (Exception e) {
            exception = e;
        }
        Assert.assertNotNull(exception);
    }
}
