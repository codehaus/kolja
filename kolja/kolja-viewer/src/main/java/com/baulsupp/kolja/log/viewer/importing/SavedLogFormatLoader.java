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

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.io.Resource;

import com.baulsupp.kolja.util.SerializationUtil;

public class SavedLogFormatLoader {
  public static final ConfigurableListableBeanFactory loadAppContext(String name) throws Exception {
    Resource xmlFile = ConfigUtils.getConfigFile(name, "xml");

    return SpringBeanLogFormatLoader.loadBeanFactory(xmlFile);
  }

  public static final LogFormat load(String name) throws Exception {
    Resource serFile = ConfigUtils.getConfigFile(name, "ser");
    Resource xmlFile = ConfigUtils.getConfigFile(name, "xml");

    if (ConfigUtils.needsRefresh(serFile, xmlFile)) {
      LogFormat lf = SpringBeanLogFormatLoader.load(xmlFile);

      SerializationUtil.serialize(serFile.getFile(), lf);

      return lf;
    } else {
      return SerializationUtil.deserialize(LogFormat.class, serFile);
    }
  }
}
