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

import static com.baulsupp.kolja.log.viewer.spring.XmlReaderUtil.getSingleElement;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

import com.baulsupp.kolja.log.viewer.importing.ConfigurableEventFormat;
import com.baulsupp.kolja.log.viewer.importing.ConfigurableLineFormat;
import com.baulsupp.kolja.log.viewer.importing.ConfigurableLogFormat;
import com.baulsupp.kolja.log.viewer.importing.ConfigurableOutputFormat;
import com.baulsupp.kolja.log.viewer.importing.ConfigurableRequestFormat;

/**
 * &lt;format&gt; element parser.
 * 
 * @author Yuri Schimke
 */
public class FormatParser extends AbstractSingleBeanDefinitionParser {
  @Override
  protected Class<ConfigurableLogFormat> getBeanClass(Element element) {
    return ConfigurableLogFormat.class;
  }

  @Override
  protected void doParse(Element element, BeanDefinitionBuilder bean) {
    bean.addPropertyValue("lineFormat", parseLineFormat(element));

    bean.addPropertyValue("outputFormat", parseOutputFormat(element));

    bean.addPropertyValue("requestOutputFormat", parseRequestOutputFormat(element));

    ConfigurableRequestFormat requestFormat = parseRequestFormat(element);
    if (requestFormat != null) {
      bean.addPropertyValue("requestFormat", requestFormat);
    }

    ConfigurableEventFormat eventFormat = parseEventFormat(element);
    if (eventFormat != null) {
      bean.addPropertyValue("eventFormat", eventFormat);
    }
  }

  private ConfigurableLineFormat parseLineFormat(Element element) {
    return new LineParser(getSingleElement(element, "line-format")).parse();
  }

  private ConfigurableOutputFormat parseOutputFormat(Element element) {
    return new OutputParser(getSingleElement(element, "output-format")).parse();
  }

  private ConfigurableOutputFormat parseRequestOutputFormat(Element element) {
    Element singleElement = getSingleElement(element, "request-output-format");
    return singleElement != null ? new OutputParser(singleElement).parse() : null;
  }

  private ConfigurableEventFormat parseEventFormat(Element element) {
    Element e = getSingleElement(element, "events");
    return e != null ? new EventParser(e).parse() : null;
  }

  private ConfigurableRequestFormat parseRequestFormat(Element element) {
    Element e = getSingleElement(element, "requests");
    return e != null ? new RequestParser(e).parse() : null;
  }
}
