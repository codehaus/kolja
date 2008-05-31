/**
 * Copyright (c) 2002-2007 Yuri Schimke. All Rights Reserved.
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package com.baulsupp.kolja.ansi.reports.ruby;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import org.jruby.Ruby;
import org.jruby.RubyRuntimeAdapter;
import org.jruby.javasupport.JavaEmbedUtils;
import org.jruby.runtime.builtin.IRubyObject;
import org.springframework.core.io.Resource;

import com.baulsupp.kolja.ansi.reports.TextReport;

/**
 * @author Yuri Schimke
 */
public class JRubyReportFactory {
  private static final long serialVersionUID = -6048626637734368371L;

  @SuppressWarnings("unchecked")
  public TextReport<?> buildReport(Resource resource) throws Exception {
    Ruby runtime = JavaEmbedUtils.initialize(new ArrayList());
    RubyRuntimeAdapter evaler = JavaEmbedUtils.newRuntimeAdapter();

    String script = getContent(resource);
    IRubyObject eval = evaler.eval(runtime, script);

    TextReport<?> textReport = (TextReport<?>) JavaEmbedUtils.rubyToJava(runtime, eval, TextReport.class);

    if (textReport instanceof RbReport) {
      ((RbReport) textReport).setRuntime(runtime);
    }

    return textReport;
  }

  private String getContent(Resource resource) throws IOException {
    StringBuilder sb = new StringBuilder();

    Reader r = new InputStreamReader(resource.getInputStream());

    char[] buffy = new char[4096];
    int read = 0;
    while ((read = r.read(buffy)) != -1) {
      sb.append(buffy, 0, read);
    }

    return sb.toString();
  }
}
