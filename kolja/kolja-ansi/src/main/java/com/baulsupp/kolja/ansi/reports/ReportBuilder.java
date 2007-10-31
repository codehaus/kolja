/**
 * Copyright (c) 2002-2007 Yuri Schimke. All Rights Reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.baulsupp.kolja.ansi.reports;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import com.baulsupp.kolja.log.viewer.importing.KoljaPropertyEditorRegistrar;

public class ReportBuilder {
  private ConfigurableListableBeanFactory appCtxt;

  public ReportBuilder(ConfigurableListableBeanFactory appCtxt) {
    this.appCtxt = appCtxt;
  }

  public TextReport buildReport(String name) {
    String[] parts = null;

    if (name.contains("?")) {
      parts = name.split("\\?");

      if (parts.length != 2) {
        throw new IllegalArgumentException("bad query '" + name + "'");
      }

      name = parts[0];

      parts = parts[1].split("\\&");
    }

    TextReport bean = (TextReport) appCtxt.getBean(name);

    if (parts != null) {
      applyConfig(bean, parts);
    }

    return bean;
  }

  public void applyConfig(TextReport bean, String[] parts) {
    BeanWrapperImpl bw = new BeanWrapperImpl(bean);

    new KoljaPropertyEditorRegistrar().registerCustomEditors(bw);

    for (String string : parts) {
      String[] split = string.split("=");

      bw.setPropertyValue(split[0], split[1]);
    }
  }
}
