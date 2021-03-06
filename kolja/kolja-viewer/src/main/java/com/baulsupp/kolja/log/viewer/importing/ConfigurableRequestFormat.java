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
package com.baulsupp.kolja.log.viewer.importing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.baulsupp.kolja.log.LogConstants;
import com.baulsupp.kolja.log.line.LineIndex;
import com.baulsupp.kolja.log.viewer.LineFormatter;
import com.baulsupp.kolja.log.viewer.request.FieldCopier;
import com.baulsupp.kolja.log.viewer.request.StandardRequestIndex;

public class ConfigurableRequestFormat implements Serializable {
  private static final long serialVersionUID = -4557634359018078203L;

  private boolean offsetIsEnd;
  private LineFormatter statusFormatter;
  private String startPattern;
  private String endPattern;
  private List<FieldCopier> matchers = new ArrayList<FieldCopier>();
  private String[] fields;

  private String requestField = LogConstants.REQUEST_ID;

  private String dateField = LogConstants.DATE;

  private String contentField = LogConstants.CONTENT;

  public String getEndPattern() {
    return endPattern;
  }

  public void setEndPattern(String endPattern) {
    this.endPattern = endPattern;
  }

  public String[] getFields() {
    return fields;
  }

  public void setFields(String[] fields) {
    this.fields = fields;
  }

  public List<FieldCopier> getMatchers() {
    return matchers;
  }

  public void setMatchers(List<FieldCopier> matchers) {
    this.matchers = matchers;
  }

  public boolean isOffsetIsEnd() {
    return offsetIsEnd;
  }

  public void setOffsetIsEnd(boolean offsetIsEnd) {
    this.offsetIsEnd = offsetIsEnd;
  }

  public String getStartPattern() {
    return startPattern;
  }

  public void setStartPattern(String startPattern) {
    this.startPattern = startPattern;
  }

  public LineFormatter getStatusFormatter() {
    return statusFormatter;
  }

  public void setStatusFormatter(LineFormatter statusFormatter) {
    this.statusFormatter = statusFormatter;
  }

  public String getRequestField() {
    return requestField;
  }

  public void setRequestField(String requestField) {
    this.requestField = requestField;
  }

  public String getDateField() {
    return dateField;
  }

  public void setDateField(String dateField) {
    this.dateField = dateField;
  }

  public String getContentField() {
    return contentField;
  }

  public void setContentField(String contentField) {
    this.contentField = contentField;
  }

  public void addMatcher(FieldCopier fieldCopier) {
    matchers.add(fieldCopier);
  }

  public StandardRequestIndex getRequestIndex(LineIndex lineIndex, ConfigurableLineFormat lineFormat) {
    StandardRequestIndex requestIndex = new StandardRequestIndex(requestField, contentField, Arrays.asList(fields), lineIndex);

    requestIndex.setBeginPattern(startPattern);
    requestIndex.setEndPattern(endPattern);
    requestIndex.setDateField(dateField);
    requestIndex.setStatusFormatter(statusFormatter);
    requestIndex.setOffsetIsEnd(offsetIsEnd);
    requestIndex.setMatchers(matchers);

    lineIndex.addLineListener(requestIndex);
    return requestIndex;
  }
}
