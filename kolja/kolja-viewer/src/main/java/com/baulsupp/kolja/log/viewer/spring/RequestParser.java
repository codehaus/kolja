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
import java.util.List;

import org.w3c.dom.Element;

import com.baulsupp.kolja.log.viewer.LineFormatter;
import com.baulsupp.kolja.log.viewer.PrintfLineFormatter;
import com.baulsupp.kolja.log.viewer.importing.ConfigurableRequestFormat;
import com.baulsupp.kolja.log.viewer.request.BasicFieldCopier;
import com.baulsupp.kolja.log.viewer.request.FieldCopier;
import com.baulsupp.kolja.log.viewer.request.RegexFieldCopier;

/**
 * Request Parser
 * 
 * @author Yuri Schimke
 */
public class RequestParser {
  private Element element;

  public RequestParser(Element element) {
    this.element = element;
  }

  public ConfigurableRequestFormat parse() {
    ConfigurableRequestFormat requestFormat = new ConfigurableRequestFormat();

    requestFormat.setOffsetIsEnd(Boolean.parseBoolean(element.getAttribute("offset-is-end")));
    requestFormat.setFields(parseFields(XmlReaderUtil.getChildElements(element, "fields")));
    requestFormat.setStartPattern(XmlReaderUtil.getElementString(element, "start-pattern"));
    requestFormat.setEndPattern(XmlReaderUtil.getElementString(element, "end-pattern"));

    requestFormat.setMatchers(parseMatchers());

    requestFormat.setStatusFormatter(parseStatusFormatter());

    return requestFormat;
  }

  private LineFormatter parseStatusFormatter() {
    Element e = XmlReaderUtil.getSingleElement(element, "printf-line-formatter");

    String pattern = XmlReaderUtil.getElementString(e, "pattern");
    String[] fields = parseFields(XmlReaderUtil.getChildElements(e, "fields"));
    PrintfLineFormatter statusFormatter = new PrintfLineFormatter(pattern, fields);

    return statusFormatter;
  }

  private List<FieldCopier> parseMatchers() {
    List<FieldCopier> matchers = new ArrayList<FieldCopier>();

    for (Element e : XmlReaderUtil.getChildElements(element, "matchers")) {
      matchers.add(parseMatcher(e));
    }

    return matchers;
  }

  private FieldCopier parseMatcher(Element e) {
    if (e.getNodeName().equals("regex-field-copier")) {
      return parseRegexFieldCopier(e);
    } else if (e.getNodeName().equals("field-copier")) {
      return parseFieldCopier(e);
    }

    throw new IllegalArgumentException("unknown type '" + e.getNodeName() + "'");
  }

  private BasicFieldCopier parseFieldCopier(Element e) {
    return new BasicFieldCopier(e.getAttribute("field"));
  }

  private FieldCopier parseRegexFieldCopier(Element e) {
    String pattern = XmlReaderUtil.getElementString(e, "pattern");
    String[] fields = parseFields(XmlReaderUtil.getChildElements(e, "fields"));
    return new RegexFieldCopier(e.getAttribute("field"), pattern, fields);
  }

  private String[] parseFields(List<Element> childElements) {
    List<String> result = new ArrayList<String>();

    for (Element e : childElements) {
      result.add(e.getTextContent());
    }

    return result.toArray(new String[0]);
  }
}
