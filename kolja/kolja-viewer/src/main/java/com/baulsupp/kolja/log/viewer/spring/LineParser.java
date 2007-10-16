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

import java.util.regex.Pattern;

import org.w3c.dom.Element;

import com.baulsupp.kolja.log.line.type.DateType;
import com.baulsupp.kolja.log.line.type.ExceptionType;
import com.baulsupp.kolja.log.line.type.MessageType;
import com.baulsupp.kolja.log.line.type.NameType;
import com.baulsupp.kolja.log.line.type.PriorityType;
import com.baulsupp.kolja.log.line.type.Type;
import com.baulsupp.kolja.log.line.type.TypeList;
import com.baulsupp.kolja.log.viewer.importing.ConfigurableLineFormat;

/**
 * Line Parser
 * 
 * @author Yuri Schimke
 */
public class LineParser {
  private Element root;

  public LineParser(Element root) {
    this.root = root;
  }

  public ConfigurableLineFormat parse() {
    ConfigurableLineFormat lineFormat = new ConfigurableLineFormat();

    lineFormat.setEntryPattern(parseEntryPattern());
    lineFormat.setFieldPattern(parseFieldPattern());

    TypeList types = new TypeList();

    for (Element e : XmlReaderUtil.getChildElements(root, "types")) {
      types.add(parseType(e));
    }

    lineFormat.setTypes(types);

    return lineFormat;
  }

  public Type parseType(Element e) {
    if (e.getNodeName().equals("date-type")) {
      return parseDateType(e);
    } else if (e.getNodeName().equals("name-type")) {
      return parseNameType(e);
    } else if (e.getNodeName().equals("priority-type")) {
      return parsePriorityType(e);
    } else if (e.getNodeName().equals("message-type")) {
      return parseMessageType(e);
    } else if (e.getNodeName().equals("exception-type")) {
      return parseExceptionType(e);
    }

    throw new IllegalArgumentException("unknown type '" + e.getNodeName() + "'");
  }

  private ExceptionType parseExceptionType(Element e) {
    return new ExceptionType(e.getAttribute("name"));
  }

  private MessageType parseMessageType(Element e) {
    return new MessageType(e.getAttribute("name"));
  }

  private PriorityType parsePriorityType(Element e) {
    return new PriorityType(e.getAttribute("name"));
  }

  private NameType parseNameType(Element e) {
    return new NameType(e.getAttribute("name"));
  }

  private DateType parseDateType(Element e) {
    String pattern = XmlReaderUtil.getElementString(e, "pattern");

    return new DateType(e.getAttribute("name"), pattern);
  }

  public Pattern parseEntryPattern() {
    return XmlReaderUtil.parsePattern(XmlReaderUtil.getSingleElement(root, "entry-format"), Pattern.MULTILINE);
  }

  public Pattern parseFieldPattern() {
    return XmlReaderUtil.parsePattern(XmlReaderUtil.getSingleElement(root, "field-pattern"), Pattern.MULTILINE | Pattern.DOTALL);
  }

}
