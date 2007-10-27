package com.baulsupp.kolja.widefinder.format;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class KoljaNamespaceHandlerTest extends TestCase {
  public void testLoadsWideFinderConfig() {
    ApplicationContext ac = new ClassPathXmlApplicationContext("wf.xml");

    assertNotNull(ac);
  }
}
