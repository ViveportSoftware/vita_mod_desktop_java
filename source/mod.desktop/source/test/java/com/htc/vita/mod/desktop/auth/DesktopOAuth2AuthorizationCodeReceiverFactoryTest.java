package com.htc.vita.mod.desktop.auth;

import com.htc.vita.core.auth.OAuth2AuthorizationCodeReceiver;
import com.htc.vita.core.auth.OAuth2AuthorizationCodeReceiverFactory;
import com.htc.vita.core.concurrent.CancellationToken;
import com.htc.vita.core.net.WebRequestFactory;
import com.htc.vita.core.util.MapBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

public class DesktopOAuth2AuthorizationCodeReceiverFactoryTest {
    @Test
    public void desktop_0_getInstance() {
        OAuth2AuthorizationCodeReceiverFactory.register(DesktopOAuth2AuthorizationCodeReceiverFactory.class);
        OAuth2AuthorizationCodeReceiverFactory oAuth2AuthorizationCodeReceiverFactory = OAuth2AuthorizationCodeReceiverFactory.getInstance();
        Assert.assertNotNull(oAuth2AuthorizationCodeReceiverFactory);
        Assert.assertTrue(oAuth2AuthorizationCodeReceiverFactory instanceof DesktopOAuth2AuthorizationCodeReceiverFactory);

        oAuth2AuthorizationCodeReceiverFactory = OAuth2AuthorizationCodeReceiverFactory.getInstance(DesktopOAuth2AuthorizationCodeReceiverFactory.class);
        Assert.assertNotNull(oAuth2AuthorizationCodeReceiverFactory);
        Assert.assertTrue(oAuth2AuthorizationCodeReceiverFactory instanceof DesktopOAuth2AuthorizationCodeReceiverFactory);
    }

    @Test
    public void desktop_1_getReceiver() {
        OAuth2AuthorizationCodeReceiverFactory.register(DesktopOAuth2AuthorizationCodeReceiverFactory.class);
        OAuth2AuthorizationCodeReceiverFactory oAuth2AuthorizationCodeReceiverFactory = OAuth2AuthorizationCodeReceiverFactory.getInstance();
        Assert.assertNotNull(oAuth2AuthorizationCodeReceiverFactory);
        OAuth2AuthorizationCodeReceiver oAuth2AuthorizationCodeReceiver = oAuth2AuthorizationCodeReceiverFactory.getReceiver();
        Assert.assertNotNull(oAuth2AuthorizationCodeReceiver);
    }

    @Test
    public void oAuth2AuthorizationCodeReceiver_0_receive() {
        OAuth2AuthorizationCodeReceiverFactory.register(DesktopOAuth2AuthorizationCodeReceiverFactory.class);
        OAuth2AuthorizationCodeReceiverFactory oAuth2AuthorizationCodeReceiverFactory = OAuth2AuthorizationCodeReceiverFactory.getInstance();
        Assert.assertNotNull(oAuth2AuthorizationCodeReceiverFactory);
        OAuth2AuthorizationCodeReceiver oAuth2AuthorizationCodeReceiver = oAuth2AuthorizationCodeReceiverFactory.getReceiver();
        Assert.assertNotNull(oAuth2AuthorizationCodeReceiver);

        OAuth2AuthorizationCodeReceiver.ReceiveResult receiveResult = oAuth2AuthorizationCodeReceiver.receive();
        OAuth2AuthorizationCodeReceiver.ReceiveStatus receiveStatus = receiveResult.getStatus();
        Assert.assertEquals(OAuth2AuthorizationCodeReceiver.ReceiveStatus.UnsupportedReceiver, receiveStatus);
    }

    @Test
    public void oAuth2AuthorizationCodeReceiver_0_receive_withRedirectUriAndCode() throws IOException {
        OAuth2AuthorizationCodeReceiverFactory.register(DesktopOAuth2AuthorizationCodeReceiverFactory.class);
        OAuth2AuthorizationCodeReceiverFactory oAuth2AuthorizationCodeReceiverFactory = OAuth2AuthorizationCodeReceiverFactory.getInstance();
        Assert.assertNotNull(oAuth2AuthorizationCodeReceiverFactory);

        String redirectUriString = "http://localhost:12346/";
        String code = "testCode";
        OAuth2AuthorizationCodeReceiver oAuth2AuthorizationCodeReceiver = oAuth2AuthorizationCodeReceiverFactory.getReceiver(
                new MapBuilder<String, String>().put(
                        OAuth2AuthorizationCodeReceiver.OPTION_REDIRECT_URI,
                        redirectUriString
                ).toMap(),
                CancellationToken.NONE
        );
        Assert.assertNotNull(oAuth2AuthorizationCodeReceiver);
        String fullRedirectUriString = redirectUriString + "?code=" + code;
        WebRequestFactory.getInstance()
                .getHttpWebRequest(new URL(fullRedirectUriString))
                .getResponse()
                .readStringByUtf8();

        OAuth2AuthorizationCodeReceiver.ReceiveResult receiveResult = oAuth2AuthorizationCodeReceiver.receive();
        OAuth2AuthorizationCodeReceiver.ReceiveStatus receiveStatus = receiveResult.getStatus();
        Assert.assertEquals(OAuth2AuthorizationCodeReceiver.ReceiveStatus.Ok, receiveStatus);
    }

    @Test
    public void oAuth2AuthorizationCodeReceiver_1_close() throws IOException {
        OAuth2AuthorizationCodeReceiverFactory.register(DesktopOAuth2AuthorizationCodeReceiverFactory.class);
        OAuth2AuthorizationCodeReceiverFactory oAuth2AuthorizationCodeReceiverFactory = OAuth2AuthorizationCodeReceiverFactory.getInstance();
        Assert.assertNotNull(oAuth2AuthorizationCodeReceiverFactory);
        OAuth2AuthorizationCodeReceiver oAuth2AuthorizationCodeReceiver = oAuth2AuthorizationCodeReceiverFactory.getReceiver();
        Assert.assertNotNull(oAuth2AuthorizationCodeReceiver);

        OAuth2AuthorizationCodeReceiver.ReceiveResult receiveResult = oAuth2AuthorizationCodeReceiver.receive();
        OAuth2AuthorizationCodeReceiver.ReceiveStatus receiveStatus = receiveResult.getStatus();
        Assert.assertEquals(OAuth2AuthorizationCodeReceiver.ReceiveStatus.UnsupportedReceiver, receiveStatus);
        oAuth2AuthorizationCodeReceiver.close();
        Exception exception = null;
        try {
            oAuth2AuthorizationCodeReceiver.receive();
        } catch (Exception e) {
            exception = e;
        }
        Assert.assertNotNull(exception);
    }
}
