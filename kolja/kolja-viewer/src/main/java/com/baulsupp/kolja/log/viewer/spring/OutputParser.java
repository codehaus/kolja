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

import org.springframework.util.ClassUtils;
import org.w3c.dom.Element;

import com.baulsupp.kolja.log.viewer.columns.ColumnWidths;
import com.baulsupp.kolja.log.viewer.format.CompressedPackageFormat;
import com.baulsupp.kolja.log.viewer.format.FormatFormat;
import com.baulsupp.kolja.log.viewer.format.JodaFormat;
import com.baulsupp.kolja.log.viewer.format.OutputFormat;
import com.baulsupp.kolja.log.viewer.format.ToStringFormat;
import com.baulsupp.kolja.log.viewer.importing.ConfigurableOutputFormat;
import com.baulsupp.kolja.log.viewer.event.EventMatcher;

/**
 * Output Parser
 * 
 * @author Yuri Schimke
 */
public class OutputParser {
  private Element element;
  private EventMatcher eventMatcher;

  public OutputParser(Element element, EventMatcher eventMatcher) {
    this.element = element;
    this.eventMatcher = eventMatcher;
  }

  public ConfigurableOutputFormat parse() {
    ConfigurableOutputFormat outputFormat = new ConfigurableOutputFormat();

    outputFormat.setWidths(ColumnWidths.parse(XmlReaderUtil.getElementString(element, "widths")));
    outputFormat.setAdditional(XmlReaderUtil.getElementString(element, "additional"));

    outputFormat.setNames(parseNames());
    outputFormat.setFormats(parseFormats());

    outputFormat.setHighlights(new HighlightParser(element, eventMatcher).parseHighlights());

    return outputFormat;
  }

  private List<OutputFormat> parseFormats() {
    List<OutputFormat> list = new ArrayList<OutputFormat>();

    for (Element e : XmlReaderUtil.getChildElements(element, "formats")) {
      list.add(parseFormat(e));
    }

    return list;
  }

  private List<String> parseNames() {
    List<String> list = new ArrayList<String>();

    for (Element e : XmlReaderUtil.getChildElements(element, "formats")) {
      list.add(e.getAttribute("field"));
    }

    return list;
  }

  private OutputFormat parseFormat(Element e) {
    if (e.getNodeName().equals("time-format")) {
      return parseTimeFormat();
    } else if (e.getNodeName().equals("string-format")) {
      return parseStringFormat();
    } else if (e.getNodeName().equals("package-format")) {
      return parsePackageFormat();
    } else if (e.getNodeName().equals("custom-format")) {
      return parseCustomFormat(e);
    }

    throw new IllegalArgumentException("unknown type '" + e.getNodeName() + "'");
  }

  private JodaFormat parseTimeFormat() {
    return new JodaFormat(FormatFormat.SHORT_TIME);
  }

  private ToStringFormat parseStringFormat() {
    return new ToStringFormat();
  }

  private CompressedPackageFormat parsePackageFormat() {
    return new CompressedPackageFormat();
  }

  @SuppressWarnings("unchecked")
  private OutputFormat parseCustomFormat(Element e) {
    String className = e.getAttribute("class");

    try {
      Class c = ClassUtils.forName(className);

      return (OutputFormat) c.newInstance();
    } catch (RuntimeException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }
}
