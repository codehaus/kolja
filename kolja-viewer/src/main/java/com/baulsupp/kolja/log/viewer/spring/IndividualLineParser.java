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

import java.lang.reflect.Constructor;
import java.util.regex.Pattern;

import org.springframework.util.ClassUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.baulsupp.kolja.log.line.LineParser;
import com.baulsupp.kolja.log.line.MultipleLineParser;
import com.baulsupp.kolja.log.line.RegexLineParser;
import com.baulsupp.kolja.log.line.type.BytesType;
import com.baulsupp.kolja.log.line.type.DateType;
import com.baulsupp.kolja.log.line.type.ExceptionType;
import com.baulsupp.kolja.log.line.type.MessageType;
import com.baulsupp.kolja.log.line.type.NameType;
import com.baulsupp.kolja.log.line.type.PriorityType;
import com.baulsupp.kolja.log.line.type.Type;
import com.baulsupp.kolja.log.line.type.TypeList;

public class IndividualLineParser {
  private Element root;

  public IndividualLineParser(Element root) {
    this.root = root;
  }

  public LineParser parseLineParser() {
    NodeList children = root.getChildNodes();

    for (int i = 0; i < children.getLength(); i++) {
      Node item = children.item(i);
      if (item instanceof Element && ((Element) item).getTagName().endsWith("-line-parser")) {
        return parseLineParser((Element) item);
      }
    }

    throw new IllegalStateException("line parser not found");
  }

  private LineParser parseLineParser(Element e) {
    if (e.getTagName().equals("multiple-line-parser")) {
      return parseMultipleLineParser(e);
    }

    if (e.getTagName().equals("regex-line-parser")) {
      return parseRegexLineParser(e);
    }

    if (e.getTagName().equals("custom-line-parser")) {
      return parseCustomLineParser(e);
    }

    throw new IllegalStateException("unknown line parser " + e.getTagName());
  }

  private LineParser parseMultipleLineParser(Element e) {
    MultipleLineParser lp = new MultipleLineParser();

    NodeList children = e.getChildNodes();
    for (int i = 0; i < children.getLength(); i++) {
      Node item = children.item(i);
      if (item instanceof Element) {
        lp.addLineParser(parseLineParser((Element) item));
      }
    }

    return lp;
  }

  private LineParser parseRegexLineParser(Element r) {
    TypeList types = new TypeList();

    for (Element e : XmlReaderUtil.getChildElements(r, "types")) {
      types.add(parseType(e));
    }

    return new RegexLineParser(parseFieldPattern(r), types);
  }

  public Pattern parseFieldPattern(Element r) {
    return XmlReaderUtil.parsePattern(XmlReaderUtil.getSingleElement(r, "field-pattern"), Pattern.MULTILINE | Pattern.DOTALL);
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
    } else if (e.getNodeName().equals("long-type")) {
      return parseLongType(e);
    } else if (e.getNodeName().equals("exception-type")) {
      return parseExceptionType(e);
    } else if (e.getNodeName().equals("custom-type")) {
      return parseCustomType(e);
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

  private BytesType parseLongType(Element e) {
    return new BytesType(e.getAttribute("name"));
  }

  private NameType parseNameType(Element e) {
    return new NameType(e.getAttribute("name"));
  }

  @SuppressWarnings("unchecked")
  private Type parseCustomType(Element e) {
    String className = e.getAttribute("class");

    try {
      Class c = ClassUtils.forName(className);

      Constructor constructor = ClassUtils.getConstructorIfAvailable(c, new Class[] { String.class });

      return Type.class.cast(constructor.newInstance(e.getAttribute("name")));
    } catch (RuntimeException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  @SuppressWarnings("unchecked")
  private LineParser parseCustomLineParser(Element e) {
    String className = e.getAttribute("class");

    try {
      Class c = ClassUtils.forName(className);

      Constructor constructor = ClassUtils.getConstructorIfAvailable(c, new Class[] {});

      return (LineParser) constructor.newInstance();
    } catch (RuntimeException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private DateType parseDateType(Element e) {
    String pattern = XmlReaderUtil.getElementString(e, "pattern");

    return new DateType(e.getAttribute("name"), pattern);
  }

}
