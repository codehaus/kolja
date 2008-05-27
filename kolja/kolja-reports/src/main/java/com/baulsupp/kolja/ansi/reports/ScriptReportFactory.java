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
package com.baulsupp.kolja.ansi.reports;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.baulsupp.kolja.ansi.reports.groovy.GroovyReportFactory;
import com.baulsupp.kolja.ansi.reports.ruby.JRubyReportFactory;
import com.baulsupp.kolja.util.services.BeanFactory;

/**
 * @author Yuri Schimke
 * 
 */
public class ScriptReportFactory implements BeanFactory<TextReport<?>> {
  private BeanFactory<TextReport<?>> delegate;

  public ScriptReportFactory(BeanFactory<TextReport<?>> delegate) {
    this.delegate = delegate;
  }

  public TextReport<?> create(String name) throws Exception {
    if (name.endsWith(".groovy")) {
      return createGroovyReport(name);
    } else if (name.endsWith(".rb")) {
      return createRubyReport(name);
    }

    return delegate.create(name);
  }

  private TextReport<?> createRubyReport(String name) throws Exception {
    return new JRubyReportFactory().buildReport(createResource(name));
  }

  private TextReport<?> createGroovyReport(String name) throws Exception {
    return new GroovyReportFactory().buildReport(createResource(name));
  }

  private Resource createResource(String name) {
    return new FileSystemResource(name);
  }
}
