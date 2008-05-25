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
package com.baulsupp.kolja.util.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.PropertyEditorRegistrar;

public class BeanBuilder<T> implements BeanFactory<T> {
  private Logger log = LoggerFactory.getLogger(BeanBuilder.class);

  private PropertyEditorRegistrar registrar;

  private BeanFactory<T> serviceFactory;

  public BeanBuilder() {
  }

  public BeanBuilder(PropertyEditorRegistrar registrar, BeanFactory<T> serviceFactory) {
    this.registrar = registrar;
    this.serviceFactory = serviceFactory;
  }

  public void setRegistrar(PropertyEditorRegistrar registrar) {
    this.registrar = registrar;
  }

  public void setServiceFactory(BeanFactory<T> serviceFactory) {
    this.serviceFactory = serviceFactory;
  }

  public T create(String name) throws Exception {
    String[] parts = null;

    if (name.contains("?")) {
      parts = name.split("\\?");

      if (parts.length != 2) {
        throw new IllegalArgumentException("bad query '" + name + "'");
      }

      name = parts[0];

      parts = parts[1].split("\\&");
    }

    T bean = createBean(name);

    if (parts != null) {
      applyConfig(bean, parts);
    }

    return bean;
  }

  private T createBean(String name) throws Exception {
    return serviceFactory.create(name);
  }

  public void applyConfig(T bean, String[] parts) {
    BeanWrapperImpl bw = new BeanWrapperImpl(bean);

    if (registrar != null) {
      registrar.registerCustomEditors(bw);
    }

    for (String string : parts) {
      String[] split = string.split("=");

      log.info("setting " + split[0] + " to value " + split[1]);

      bw.setPropertyValue(split[0], split[1]);
    }
  }
}
