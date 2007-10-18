package com.baulsupp.kolja.log.viewer.importing;

import java.io.File;
import java.io.IOException;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.baulsupp.kolja.util.SerializationUtil;

public class SavedLogFormatLoader {
  public static final LogFormat load(String name) throws Exception {
    if (!isShortName(name)) {
      return SpringBeanLogFormatLoader.load(new FileSystemResource(name));
    } else {
      Resource serFile = getConfigFile(name, "ser");
      Resource xmlFile = getConfigFile(name, "xml");

      if (needsRefresh(serFile, xmlFile)) {
        LogFormat lf = SpringBeanLogFormatLoader.load(xmlFile);

        SerializationUtil.serialize(serFile.getFile(), lf);

        return lf;
      } else {
        return SerializationUtil.deserialize(LogFormat.class, serFile);
      }
    }
  }

  private static boolean needsRefresh(Resource serFile, Resource xmlFile) throws IOException {
    return !serFile.exists() || serFile.getFile().lastModified() < xmlFile.getFile().lastModified();
  }

  private static Resource getConfigFile(String configName, String extension) {
    ResourceLoader r = new DefaultResourceLoader();

    Resource resource;
    if (isShortName(configName)) {
      File confDirectory = getConfigDirectory();

      configName = configName + "." + extension;

      resource = new FileSystemResource(new File(confDirectory, configName));
    } else {
      resource = r.getResource(configName);
    }

    return resource;
  }

  private static File getConfigDirectory() {
    String koljaHome = System.getenv("KOLJA_HOME");

    if (koljaHome == null) {
      koljaHome = ".";
    }

    return new File(koljaHome, "conf");
  }

  private static boolean isShortName(String configName) {
    return configName.matches("\\w+");
  }
}
