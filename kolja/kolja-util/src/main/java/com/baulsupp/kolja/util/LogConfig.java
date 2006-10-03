package com.baulsupp.kolja.util;

public class LogConfig {
  public static void config(String string) {
    if (string != null) {
      System.setProperty("logname", string);
    }

//    DOMConfigurator.configure("conf/log4j.xml");
  }
}
