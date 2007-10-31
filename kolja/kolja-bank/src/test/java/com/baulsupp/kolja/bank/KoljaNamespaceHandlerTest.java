package com.baulsupp.kolja.bank;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class KoljaNamespaceHandlerTest extends TestCase {
  public void testLoadsWideFinderConfig() {
    ApplicationContext ac = new ClassPathXmlApplicationContext("bank.xml");

    assertNotNull(ac);
  }
}
