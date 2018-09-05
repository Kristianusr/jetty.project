//
//  ========================================================================
//  Copyright (c) 1995-2018 Mort Bay Consulting Pty. Ltd.
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
//

package org.eclipse.jetty.websocket.tests.server;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.eclipse.jetty.client.HttpResponseException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.eclipse.jetty.websocket.tests.SimpleServletServer;
import org.eclipse.jetty.websocket.tests.TrackingEndpoint;
import org.eclipse.jetty.websocket.tests.servlets.EchoServlet;

import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.websocket.api.UpgradeException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

public class WebSocketInvalidVersionTest
{
    private static SimpleServletServer server;

    @BeforeAll
    public static void startServer() throws Exception
    {
        server = new SimpleServletServer(new EchoServlet());
        server.start();
    }
    @AfterAll
    public static void stopServer() throws Exception
    {
        server.stop();
    }
    private static WebSocketClient client;

    @BeforeAll
    public static void startClient() throws Exception
    {
        client = new WebSocketClient();
        client.start();
    }

    @AfterAll
    public static void stopClient() throws Exception
    {
        client.stop();
    }

    /**
     * Test the requirement of responding with an http 400 when using a Sec-WebSocket-Version that is unsupported.
     * @throws Exception on test failure
     */
    @Test
    public void testRequestVersion29( TestInfo testInfo ) throws Exception
    {
        URI wsUri = server.getServerUri();

        Throwable expectedException = Assertions.assertThrows(ExecutionException.class, () -> {
                                    TrackingEndpoint clientSocket = new TrackingEndpoint( testInfo.getTestMethod().get().getName() );
                                    ClientUpgradeRequest upgradeRequest = new ClientUpgradeRequest();
                                    upgradeRequest.setHeader( "Sec-WebSocket-Version", "29" );
                                    Future<Session> clientConnectFuture = client.connect(clientSocket, wsUri, upgradeRequest);
                                    clientConnectFuture.get();
                                });

        assertEquals(expectedException.getCause(), instanceOf(HttpResponseException.class));
        assertTrue(expectedException.getMessage().contains("Unsupported websocket version"));

    }
}
