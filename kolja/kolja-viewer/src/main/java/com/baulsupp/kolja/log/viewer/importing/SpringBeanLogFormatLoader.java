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
package com.baulsupp.kolja.log.viewer.importing;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * Kolja Log Viewer Configuration Loader.
 * 
 * @author Yuri Schimke
 */
public class SpringBeanLogFormatLoader {
  public static final LogFormat load(Resource r) throws Exception {
    BeanFactory beanFactory = loadBeanFactory(r);

    return (LogFormat) beanFactory.getBean("logFormat");
  }

  public static BeanFactory loadBeanFactory(Resource r) throws Exception {
    XmlBeanFactory beanFactory = new XmlBeanFactory(r);
    beanFactory.addPropertyEditorRegistrar(new KoljaPropertyEditorRegistrar());

    ApplicationContext appContext = new GenericApplicationContext(beanFactory);

    return appContext;
  }

  public static BeanFactory loadBeanFactory(String string) throws Exception {
    ResourceLoader r = new DefaultResourceLoader();

    return loadBeanFactory(r.getResource(string));
  }
}
