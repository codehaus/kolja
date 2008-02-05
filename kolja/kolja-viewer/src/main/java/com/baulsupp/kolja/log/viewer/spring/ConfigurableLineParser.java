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

  public Pattern parseEntryPattern() {
    return XmlReaderUtil.parsePattern(XmlReaderUtil.getSingleElement(root, "entry-format"), Pattern.MULTILINE);
  }

}
