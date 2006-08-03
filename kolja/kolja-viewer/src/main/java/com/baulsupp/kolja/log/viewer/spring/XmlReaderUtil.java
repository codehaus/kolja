package com.baulsupp.kolja.log.viewer.spring;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XmlReaderUtil {
  public static boolean hasElement(Element element, String name) {
    NodeList children = element.getElementsByTagName(name);
    return children.getLength() > 1;
  }

  public static String[] getElementStringArray(Element element, String name) {
    String s = getElementString(element, name);
    
    return s != null ? s.split("\\s*,\\s*") : null;
  }

  public static String getElementString(Element element, String name) {
    NodeList children = element.getElementsByTagName(name);
    String s;
    if (children.getLength() > 1) {
      s = children.item(0).getTextContent();
    } else {
      s = null;
    }
    return s;
  }
  
  public static void setStringArrayProperty(Element element, BeanDefinitionBuilder builder, String elementName, String property) {
    if (hasElement(element, elementName)) {
      builder.addPropertyValue(property, getElementStringArray(element, elementName));
    }
  }

  public static void setStringProperty(Element element, BeanDefinitionBuilder builder, String elementName, String property) {
    if (hasElement(element, elementName)) {
      builder.addPropertyValue(property, getElementString(element, elementName));
    }
  }

  public static List<Element> getChildElements(Element element) {
    NodeList children = element.getChildNodes();
    List<Element> result = new ArrayList<Element>();
    
    for (int i = 0; i < children.getLength(); i++) {
      if (children.item(i) instanceof Element) {
        result.add((Element) children.item(i));
      }
    }
    
    return result;
  }
}
