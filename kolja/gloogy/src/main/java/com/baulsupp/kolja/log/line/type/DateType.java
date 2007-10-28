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
package com.baulsupp.kolja.log.line.type;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.baulsupp.kolja.log.LogConstants;

/**
 * Date Field Type.
 * 
 * @author yuri
 */
public class DateType extends Type {
  private static final long serialVersionUID = 5057545504316883709L;

  private DateTimeFormatter dateFormat;

  public DateType() {
    super(LogConstants.DATE);
  }

  public DateType(String name, String dateFormat) {
    super(name);

    this.dateFormat = DateTimeFormat.forPattern(dateFormat);
  }

  public DateTimeFormatter getDateFormat() {
    return dateFormat;
  }

  public void setDateFormat(DateTimeFormatter dateFormat) {
    this.dateFormat = dateFormat;
  }

  public Object parse(String string) {
    try {
      return dateFormat.parseDateTime(string);
    } catch (IllegalArgumentException e) {
      // TODO debug
      return "INVALID";
    }
  }
}
