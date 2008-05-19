package com.baulsupp.kolja.util;

import junit.framework.TestCase;

public class PlatformUtilTest extends TestCase {
  public void testWindows() {
    System.setProperty("os.name", "Windows 2000");
    assertTrue(PlatformUtil.isWindows());
  }
  
  public void testLinux() {
    System.setProperty("os.name", "Linux Something");
    assertFalse(PlatformUtil.isWindows());
  }
}
