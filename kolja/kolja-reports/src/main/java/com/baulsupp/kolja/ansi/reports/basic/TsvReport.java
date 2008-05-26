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
package com.baulsupp.kolja.ansi.reports.basic;

import java.util.Arrays;

import com.baulsupp.kolja.ansi.reports.AbstractTextReport;
import com.baulsupp.kolja.log.line.Line;

public final class TsvReport extends AbstractTextReport<TsvReport> {
  private static final long serialVersionUID = 1585931861594446683L;

  private String[] fields;

  private StringBuilder sb = new StringBuilder(100);

  public TsvReport() {
    super(Detail.LINES);
  }

  @Override
  protected void validate() {
    if (fields == null) {
      throw new IllegalStateException("no fields specified");
    }
  }

  @Override
  public void processLine(Line line) {
    sb.setLength(0);

    boolean first = true;

    for (String f : fields) {
      if (first) {
        first = false;
      } else {
        sb.append('\t');
      }

      sb.append(format(line.getValue(f)));
    }

    println(sb.toString());
  }

  private Object format(Object value) {
    return value != null ? value.toString() : null;
  }

  public void setFields(String... fields) {
    this.fields = fields;
  }

  public String describe() {
    return "TSV: " + Arrays.toString(fields);
  }
}
