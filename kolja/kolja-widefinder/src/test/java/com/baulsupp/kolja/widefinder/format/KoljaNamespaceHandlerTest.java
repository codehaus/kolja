package com.baulsupp.kolja.widefinder.format;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.baulsupp.kolja.util.LogConfig;

public class KoljaNamespaceHandlerTest extends TestCase {
  public void testLoadsWideFinderConfig() {
    LogConfig.config("tests");

    ApplicationContext ac = new FileSystemXmlApplicationContext("src/main/config/wf.xml");

    assertNotNull(ac);
  }
}
