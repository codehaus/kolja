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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CompressedPackageFormat implements OutputFormat {
  private static final long serialVersionUID = 6750671123052767172L;

  private Pattern pattern = Pattern.compile("(\\w)\\w*\\.");

  private transient Matcher matcher = null;

  public String format(Object value) {
    if (value == null) {
      return null;
    }

    if (matcher == null) {
      matcher = pattern.matcher("");
    }

    matcher.reset(value.toString());

    return matcher.replaceAll("$1.");
  }
}
