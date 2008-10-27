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

import java.io.File;
import java.io.IOException;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class ConfigUtils {
  private static File koljaHome;

  public static boolean needsRefresh(Resource serFile, Resource xmlFile) throws IOException {
    return !serFile.exists() || serFile.getFile().lastModified() < xmlFile.getFile().lastModified();
  }

  public static Resource getConfigFile(String configName, String extension) {
    Resource resource;

    if (isShortName(configName)) {
      File confDirectory = getConfigDirectory();

      configName = configName + "." + extension;

      resource = new FileSystemResource(new File(confDirectory, configName));
    } else {
      File file = new File(configName);

      file = convertExtension(file, extension);

      resource = new FileSystemResource(file);
    }

    return resource;
  }

  public static File convertExtension(File file, String extension) {
    if (!file.getName().endsWith("." + extension)) {
      String name = file.getName();
      String newName = name.split("\\.")[0] + "." + extension;

      return new File(file.getParentFile(), newName);
    } else {

      return file;
    }
  }

  public static File getConfigDirectory() {
    if (koljaHome == null) {
      String s = System.getenv("KOLJA_HOME");

      if (s != null) {
        koljaHome = new File(s);
      } else {
        koljaHome = new File(".");
      }
    }

    return new File(koljaHome, "conf");
  }

  private static boolean isShortName(String configName) {
    return configName.matches("\\w+");
  }

  public static void setKoljaHome(File dir) {
    koljaHome = dir;
  }
}
