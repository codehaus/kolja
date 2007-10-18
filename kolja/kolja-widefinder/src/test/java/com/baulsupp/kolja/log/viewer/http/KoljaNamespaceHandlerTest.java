package com.baulsupp.kolja.log.viewer.http;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@SuppressWarnings("deprecation")
public class KoljaNamespaceHandlerTest extends TestCase {
  public void testLoadsWideFinderConfig() {
    ApplicationContext ac = new ClassPathXmlApplicationContext("wf.xml");

    assertNotNull(ac);
  }
}
