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
package com.baulsupp.kolja.widefinder;

import java.util.Arrays;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.joda.time.format.DateTimeFormatter;

import com.baulsupp.kolja.ansi.reports.AbstractFrequencyReport;
import com.baulsupp.kolja.log.line.Line;

public class TimeReport extends AbstractFrequencyReport<Partial> {

  private DateTimeFieldType[] types = { DateTimeFieldType.hourOfDay() };
  private DateTimeFormatter formatter = JodaUtil.buildDateTimeFormatter(types);

  public void setFields(String fields) {
    types = JodaUtil.parseFields(fields);

    formatter = JodaUtil.buildDateTimeFormatter(types);
  }

  @Override
  protected Partial getValue(Line line) {
    DateTime time = (DateTime) line.getValue(WideFinderConstants.DATE);

    Partial range = JodaUtil.buildPartial(time, types);

    return range;
  }

  @Override
  protected String toString(Partial item) {
    return formatter.print(item);
  }

  public String describe() {
    return "Time Frequency " + Arrays.toString(types);
  }
}
