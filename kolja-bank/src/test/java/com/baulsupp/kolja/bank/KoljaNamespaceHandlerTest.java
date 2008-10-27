package com.baulsupp.kolja.bank;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class KoljaNamespaceHandlerTest extends TestCase {
  public void testLoadsWideFinderConfig() {
    ApplicationContext ac = new FileSystemXmlApplicationContext("src/main/config/bank.xml");

    assertNotNull(ac);
  }
}
