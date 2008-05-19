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

import com.baulsupp.kolja.log.line.matcher.EntryPattern;
import com.baulsupp.kolja.log.line.matcher.NewLineEntryPattern;
import com.baulsupp.kolja.log.line.matcher.RegexEntryPattern;
import com.baulsupp.kolja.log.viewer.importing.ConfigurableLineFormat;

/**
 * Line Parser
 * 
 * @author Yuri Schimke
 */
public class ConfigurableLineParser {
  private Element root;

  public ConfigurableLineParser(Element root) {
    this.root = root;
  }

  public ConfigurableLineFormat parse() {
    IndividualLineParser lp = new IndividualLineParser(root);

    ConfigurableLineFormat lineFormat = new ConfigurableLineFormat();

    lineFormat.setEntryPattern(parseEntryPattern());
    lineFormat.setLineParser(lp.parseLineParser());

    return lineFormat;
  }

  // public EntryPattern parseEntryPattern() {
  // return XmlReaderUtil.parsePattern(XmlReaderUtil.getSingleElement(root,
  // "entry-format"), Pattern.MULTILINE);
  // }

  public EntryPattern parseEntryPattern() {
    NodeList children = root.getChildNodes();

    for (int i = 0; i < children.getLength(); i++) {
      Node item = children.item(i);
      if (item instanceof Element && ((Element) item).getTagName().endsWith("-entry-pattern")) {
        return parseEntryPattern((Element) item);
      }
    }

    throw new IllegalStateException("line parser not found");
  }

  private EntryPattern parseEntryPattern(Element e) {
    if (e.getTagName().equals("new-line-entry-pattern")) {
      return new NewLineEntryPattern();
    }

    if (e.getTagName().equals("regex-entry-pattern")) {
      return parseRegexEntryPattern(e);
    }

    if (e.getTagName().equals("custom-entry-pattern")) {
      return parseCustomEntryPattern(e);
    }

    throw new IllegalStateException("unknown line pattern " + e.getTagName());
  }

  private RegexEntryPattern parseRegexEntryPattern(Element r) {
    Pattern pattern = XmlReaderUtil.parsePattern(XmlReaderUtil.getSingleElement(r, "pattern"), Pattern.MULTILINE);
    return new RegexEntryPattern(pattern);
  }

  @SuppressWarnings("unchecked")
  private EntryPattern parseCustomEntryPattern(Element e) {
    String className = e.getAttribute("class");

    try {
      Class c = ClassUtils.forName(className);

      Constructor constructor = ClassUtils.getConstructorIfAvailable(c, new Class[] {});

      return (EntryPattern) constructor.newInstance();
    } catch (RuntimeException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
