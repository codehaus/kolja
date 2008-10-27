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

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * User Agent Format
 * 
 * @author Yuri Schimke
 */
public class BytesFormat implements OutputFormat {
  private static final long serialVersionUID = -2166842740967293740L;

  public static final long KB = 1024;
  public static final long MB = KB * KB;
  public static final long GB = KB * KB * KB;

  private DecimalFormat format;

  public BytesFormat() {
    this(0);
  }

  public BytesFormat(int i) {
    format = new DecimalFormat();
    format.setMaximumFractionDigits(1);
  }

  public String format(Object value) {
    if (value == null) {
      return null;
    }

    return formatBytes(((Number) value).longValue(), format);
  }

  public static String formatBytes(Long value, NumberFormat format) {
    if (value == null) {
      return null;
    }

    long bytes = ((Number) value).longValue();

    if (bytes >= GB) {
      return div(bytes, GB, format) + "GB";
    }

    if (bytes >= MB) {
      return div(bytes, MB, format) + "MB";
    }

    if (bytes >= KB) {
      return div(bytes, KB, format) + "KB";
    }

    return bytes + "b";
  }

  public static String formatBytes(Long value) {
    return formatBytes(value, null);
  }

  private static String div(long bytes, long size, NumberFormat format) {
    if (format == null) {
      return String.valueOf(bytes / size);
    } else {
      return format.format((double) bytes / size);
    }
  }
}
