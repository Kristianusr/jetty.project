//
// ========================================================================
// Copyright (c) 1995-2020 Mort Bay Consulting Pty Ltd and others.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v. 2.0 which is available at
// https://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
// which is available at https://www.apache.org/licenses/LICENSE-2.0.
//
// SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
// ========================================================================
//

import org.eclipse.jetty.http.Http1FieldPreEncoder;
import org.eclipse.jetty.http.HttpFieldPreEncoder;

module org.eclipse.jetty.http
{
    exports org.eclipse.jetty.http;
    exports org.eclipse.jetty.http.pathmap;

    requires transitive org.eclipse.jetty.io;
    requires org.slf4j;

    uses HttpFieldPreEncoder;

    provides HttpFieldPreEncoder with Http1FieldPreEncoder;
}
