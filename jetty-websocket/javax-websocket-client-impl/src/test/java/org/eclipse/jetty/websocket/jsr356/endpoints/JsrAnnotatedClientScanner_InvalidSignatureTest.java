//
//  ========================================================================
//  Copyright (c) 1995-2013 Mort Bay Consulting Pty. Ltd.
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

package org.eclipse.jetty.websocket.jsr356.endpoints;

import static org.hamcrest.Matchers.*;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.websocket.WebSocketClose;
import javax.websocket.WebSocketError;
import javax.websocket.WebSocketOpen;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.websocket.common.events.annotated.InvalidSignatureException;
import org.eclipse.jetty.websocket.jsr356.endpoints.samples.InvalidCloseIntSocket;
import org.eclipse.jetty.websocket.jsr356.endpoints.samples.InvalidErrorErrorSocket;
import org.eclipse.jetty.websocket.jsr356.endpoints.samples.InvalidErrorExceptionSocket;
import org.eclipse.jetty.websocket.jsr356.endpoints.samples.InvalidErrorIntSocket;
import org.eclipse.jetty.websocket.jsr356.endpoints.samples.InvalidOpenCloseReasonSocket;
import org.eclipse.jetty.websocket.jsr356.endpoints.samples.InvalidOpenIntSocket;
import org.eclipse.jetty.websocket.jsr356.endpoints.samples.InvalidOpenSessionIntSocket;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Test {@link JsrAnnotatedClientScanner} against various simple, single method annotated classes with invalid signatures.
 */
@RunWith(Parameterized.class)
public class JsrAnnotatedClientScanner_InvalidSignatureTest
{
    private static final Logger LOG = Log.getLogger(JsrAnnotatedClientScanner_InvalidSignatureTest.class);

    @Parameters
    public static Collection<Class<?>[]> data()
    {
        List<Class<?>[]> data = new ArrayList<>();

        // @formatter:off
        data.add(new Class<?>[]{ InvalidCloseIntSocket.class, WebSocketClose.class });
        data.add(new Class<?>[]{ InvalidErrorErrorSocket.class, WebSocketError.class });
        data.add(new Class<?>[]{ InvalidErrorExceptionSocket.class, WebSocketError.class });
        data.add(new Class<?>[]{ InvalidErrorIntSocket.class, WebSocketError.class });
        data.add(new Class<?>[]{ InvalidOpenCloseReasonSocket.class, WebSocketOpen.class });
        data.add(new Class<?>[]{ InvalidOpenIntSocket.class, WebSocketOpen.class });
        data.add(new Class<?>[]{ InvalidOpenSessionIntSocket.class, WebSocketOpen.class });
        // @formatter:on

        return data;
    }

    // The pojo to test
    private Class<?> pojo;
    // The annotation class expected to be mentioned in the error message
    private Class<? extends Annotation> expectedAnnoClass;

    public JsrAnnotatedClientScanner_InvalidSignatureTest(Class<?> pojo, Class<? extends Annotation> expectedAnnotation)
    {
        this.pojo = pojo;
        this.expectedAnnoClass = expectedAnnotation;
    }

    @Test
    public void testScan_InvalidSignature()
    {
        JsrAnnotatedClientScanner scanner = new JsrAnnotatedClientScanner(pojo);
        try
        {
            scanner.scan();
            Assert.fail("Expected " + InvalidSignatureException.class + " with message that references " + expectedAnnoClass + " annotation");
        }
        catch (InvalidSignatureException e)
        {
            LOG.debug("{}:{}",e.getClass(),e.getMessage());
            Assert.assertThat("Message",e.getMessage(),containsString(expectedAnnoClass.getSimpleName()));
        }
    }
}
