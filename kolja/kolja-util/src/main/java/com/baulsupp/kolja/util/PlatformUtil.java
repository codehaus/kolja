package com.baulsupp.kolja.util;

public class PlatformUtil {
  public static boolean isWindows() {
    return System.getProperty("os.name", "").toLowerCase().contains("windows");
  }
}
