package com.baulsupp.kolja.log.viewer.spring;

import static com.baulsupp.kolja.log.viewer.spring.XmlReaderUtil.getChildElements;
import static com.baulsupp.kolja.log.viewer.spring.XmlReaderUtil.setStringArrayProperty;
import static com.baulsupp.kolja.log.viewer.spring.XmlReaderUtil.setStringProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.w3c.dom.Element;

import com.baulsupp.kolja.log.viewer.event.EventMatcher;
import com.baulsupp.kolja.log.viewer.event.PatternEventMatcher;
import com.baulsupp.kolja.log.viewer.event.WarnEventMatcher;
import com.baulsupp.kolja.log.viewer.importing.ConfigurableEventFormat;
import com.baulsupp.kolja.log.viewer.importing.ConfigurableRequestFormat;
import com.baulsupp.kolja.util.PatternPropertyEditor;

public class KoljaNamespaceHandler extends NamespaceHandlerSupport {

  public void init() {
    registerBeanDefinitionParser("event-format", new EventFormatParser());
    registerBeanDefinitionParser("request-format", new RequestFormatParser());
  }

  private EventMatcher readMatcher(Element element) {
    if (element.getLocalName().equals("warn")) {
      return readWarnEvent(element);
    } else if (element.getLocalName().equals("pattern")) {
      return readPatternEvent(element);
    } else {
      throw new IllegalStateException("unknown node " + element.getNodeName());
    }
  }

  private PatternEventMatcher readPatternEvent(Element element) {
    PatternEventMatcher patternEventMatcher = new PatternEventMatcher();

    if (element.hasAttribute("pattern")) {
      Pattern pattern = PatternPropertyEditor.parsePattern(element.getAttribute("pattern"));
      patternEventMatcher.setPattern(pattern);
    }

    if (element.hasAttribute("message")) {
      patternEventMatcher.setMessage(element.getAttribute("message"));
    }

    return patternEventMatcher;
  }

  private WarnEventMatcher readWarnEvent(Element element) {
    WarnEventMatcher warnEventMatcher = new WarnEventMatcher();

    if (element.hasAttribute("message")) {
      warnEventMatcher.setMessageField(element.getAttribute("message"));
    }

    if (element.hasAttribute("priority-field")) {
      warnEventMatcher.setMessageField(element.getAttribute("priority-field"));
    }

    return warnEventMatcher;
  }

  private class EventFormatParser extends AbstractSingleBeanDefinitionParser {
    protected Class getBeanClass(Element element) {
      return ConfigurableEventFormat.class;
    }

    protected void doParse(Element element, BeanDefinitionBuilder builder) {
      List<EventMatcher> eventMatchers = new ArrayList<EventMatcher>();

      List<Element> childNodes = getChildElements(element);

      for (Element childElement : childNodes) {
        eventMatchers.add(readMatcher(childElement));
      }

      builder.addPropertyValue("eventMatchers", eventMatchers);
    }
  }

  public class RequestFormatParser extends AbstractSingleBeanDefinitionParser {
    protected Class getBeanClass(Element element) {
      return ConfigurableRequestFormat.class;
    }
    
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
      if (element.hasAttribute("offset-is-end")) {
        builder.addPropertyValue("offsetIsEnd", element.getAttribute("offset-is-end"));
      }
      
      setStringArrayProperty(element, builder, "fields", "fields");
      setStringProperty(element, builder, "start-pattern", "startPattern");
      setStringProperty(element, builder, "end-pattern", "endPattern");
    }
  }
}
