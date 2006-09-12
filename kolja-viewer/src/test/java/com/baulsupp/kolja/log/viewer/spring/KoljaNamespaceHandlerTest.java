package com.baulsupp.kolja.log.viewer.spring;

import java.util.List;

import junit.framework.TestCase;

import org.springframework.beans.factory.BeanFactory;

import com.baulsupp.kolja.log.viewer.event.EventMatcher;
import com.baulsupp.kolja.log.viewer.event.PatternEventMatcher;
import com.baulsupp.kolja.log.viewer.event.WarnEventMatcher;
import com.baulsupp.kolja.log.viewer.importing.ConfigurableEventFormat;
import com.baulsupp.kolja.log.viewer.importing.ConfigurableRequestFormat;
import com.baulsupp.kolja.log.viewer.importing.SpringBeanLogFormatLoader;

public class KoljaNamespaceHandlerTest extends TestCase {
  public void testEventFormat() {
    BeanFactory applicationContext = SpringBeanLogFormatLoader.loadBeanFactory("/kolja-test.xml");

    ConfigurableEventFormat cef = (ConfigurableEventFormat) applicationContext.getBean("events");

    List<EventMatcher> matchers = cef.getEventMatchers();
    assertEquals(2, matchers.size());

    WarnEventMatcher warn = (WarnEventMatcher) matchers.get(0);
    assertNotNull(warn);

    PatternEventMatcher pattern = (PatternEventMatcher) matchers.get(1);
    assertEquals("country:(.*)", pattern.getPattern().toString());
    assertEquals("Country Line Found (%s)", pattern.getMessage());
  }

  public void testRequestFormat() {
    BeanFactory applicationContext = SpringBeanLogFormatLoader.loadBeanFactory("/kolja-test.xml");

    ConfigurableRequestFormat cef = (ConfigurableRequestFormat) applicationContext.getBean("requests");
    
    assertEquals(true, cef.isOffsetIsEnd());
//    assertEquals("*** GET", cef.getStartPattern());
//    assertEquals("*** END", cef.getEndPattern());
  }
}
