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
package com.baulsupp.kolja.widefinder.format;

import org.springframework.util.Assert;

/**
 * HTTP Status Code.
 * 
 * @author Yuri Schimke
 */
public class HttpStatus {
  public static final HttpStatus SUCCESS_OK = new HttpStatus("200");
  public static final HttpStatus CLIENT_ERROR_NOT_FOUND = new HttpStatus("404");
  public static final HttpStatus SERVER_ERROR_INTERNAL = new HttpStatus("500");

  private String code = null;

  public HttpStatus(String code) {
    Assert.notNull(code);

    this.code = code;
  }

  public String getCode() {
    return code;
  }

  public boolean isInformational() {
    return code.startsWith("1");
  }

  public boolean isSuccess() {
    return code.startsWith("2");
  }

  public boolean isRedirect() {
    return code.startsWith("3");
  }

  public boolean isClientError() {
    return code.startsWith("4");
  }

  public boolean isServerError() {
    return code.startsWith("5");
  }

  @Override
  public String toString() {
    return code;
  }

  @Override
  public int hashCode() {
    return code.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof HttpStatus)) {
      return false;
    }

    HttpStatus s = (HttpStatus) obj;

    return code.equals(s.code);
  }
}
