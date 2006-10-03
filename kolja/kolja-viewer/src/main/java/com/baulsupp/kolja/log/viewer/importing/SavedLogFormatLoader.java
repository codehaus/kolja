package com.baulsupp.kolja.log.viewer.importing;
import java.io.File;
import java.io.IOException;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

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
    File resource = null;
    
    if (isShortName(configName)) {
      File confDirectory = getConfigDirectory();

      configName = configName + "." + extension;
      
      resource = new File(confDirectory, configName);
    } else {
      if (!configName.endsWith("." + extension)) {
        configName += "." + extension;
      }
      
      resource = new File(configName);
    }

    return new FileSystemResource(resource);
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
