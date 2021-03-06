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

/**
 * Bytes type that parse the access log bytes into a long.
 * 
 * @author Yuri Schimke
 */
public class BytesType extends Type {
  private static final long serialVersionUID = -469580635494842870L;

  public BytesType(String string, String nullValue) {
    super(string, nullValue);
  }

  public BytesType(String string) {
    super(string);
  }

  public BytesType() {
  }

  @Override
  public Object parse(String string) {
    return parseBytes(string, nullValue);
  }

  public static Long parseBytes(String string, String nullValue) {
    if (string.equals(nullValue)) {
      return null;
    }

    return Long.parseLong(string);
  }
}
