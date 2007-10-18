/**
 * Copyright (c) 2002-2007 Yuri Schimke. All Rights Reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.baulsupp.kolja.log.viewer.spring;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.baulsupp.kolja.util.colours.Colour;
import com.baulsupp.kolja.util.colours.ColourPair;

/**
 * XML Utilities
 * 
 * @author Yuri Schimke
 */
public class XmlReaderUtil {
  public static boolean hasElement(Element element, String name) {
    NodeList children = element.getElementsByTagName(name);
    return children.getLength() > 1;
  }

  public static String getElementString(Element element, String name) {
    NodeList children = element.getElementsByTagName(name);

    String s = null;

    if (children.getLength() >= 1) {
      Node child = children.item(0);

      s = child.getTextContent();
    }

    return s;
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

  public static Element getSingleElement(Element element, String tag) {
    return (Element) element.getElementsByTagName(tag).item(0);
  }

  public static Pattern parsePattern(Element element, int defaultMode) {
    return Pattern.compile(element.getTextContent(), defaultMode);
  }

  public static List<Element> getChildElements(Element element, String tag) {
    Element group = getSingleElement(element, tag);

    if (group == null) {
      return Collections.emptyList();
    }

    NodeList childNodes = group.getChildNodes();

    return elements(childNodes);
  }

  public static List<Element> elements(NodeList childNodes) {
    List<Element> result = new ArrayList<Element>();

    for (int i = 0; i < childNodes.getLength(); i++) {
      Node item = childNodes.item(i);

      if (item instanceof Element) {
        result.add((Element) item);
      }
    }

    return result;
  }

  public static ColourPair parseColours(Element element) {
    Colour foreground = Colour.valueOf(element.getAttribute("foreground"));
    Colour background = Colour.valueOf(element.getAttribute("background"));

    return new ColourPair(foreground, background);
  }
}
