package com.baulsupp.kolja.util;

import java.io.IOException;

public class LogConfig {
  public static void config(String string) throws IOException {
    if (string != null) {
      System.setProperty("logname", string);
    }

//    DOMConfigurator.configure("conf/log4j.xml");
  }
}
