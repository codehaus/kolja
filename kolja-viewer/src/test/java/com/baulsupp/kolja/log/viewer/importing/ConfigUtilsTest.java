package com.baulsupp.kolja.log.viewer.importing;

import static junit.framework.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.springframework.core.io.Resource;

public class ConfigUtilsTest {
  @Test
  public void testReturnsResourceForFile() throws IOException {
    Resource r = ConfigUtils.getConfigFile("src/test/resources/sample.xml", "xml");

    assertEquals("src/test/resources/sample.xml", r.getFile().getPath().replace('\\', '/'));
  }

  @Test
  public void testConvertsExtensionsForFiles() throws IOException {
    Resource r = ConfigUtils.getConfigFile("/tmp/a.xml", "ser");

    assertEquals("/tmp/a.ser", r.getFile().getPath().replace('\\', '/'));
  }

  @Test
  public void testReturnsResourceForShortFile() throws IOException {
    ConfigUtils.setKoljaHome(new File("/tmp"));

    Resource r = ConfigUtils.getConfigFile("a", "xml");

    assertEquals("/tmp/conf/a.xml", r.getFile().getPath().replace('\\', '/'));
  }
}
