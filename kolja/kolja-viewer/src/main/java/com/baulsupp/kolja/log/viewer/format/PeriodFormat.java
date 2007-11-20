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
package com.baulsupp.kolja.log.viewer.format;

import org.joda.time.ReadableDuration;
import org.joda.time.ReadableInterval;
import org.joda.time.ReadablePeriod;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

public class PeriodFormat implements OutputFormat {
  private static final long serialVersionUID = -1379795126257670566L;

  private PeriodFormatter format;

  public PeriodFormat() {
  }

  public String format(Object value) {
    if (value == null) {
      return null;
    }

    if (format == null) {
      format = new PeriodFormatterBuilder().printZeroRarelyLast().appendDays().appendSuffix("d").appendHours().appendSuffix("h")
          .appendMinutes().appendSuffix("m").appendSecondsWithOptionalMillis().appendSuffix("s").toFormatter();
    }

    ReadablePeriod p = null;

    if (value instanceof ReadableInterval) {
      p = ((ReadableInterval) value).toPeriod();
    } else if (value instanceof ReadableDuration) {
      p = ((ReadableDuration) value).toPeriod();
    } else if (value instanceof ReadablePeriod) {
      p = (ReadablePeriod) value;
    } else {
      return "";
    }

    return format.print(p);
  }
}
