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
public class UserAgent {
  private String fullId = null;

  public UserAgent(String fullId) {
    Assert.notNull(fullId);

    this.fullId = fullId;
  }

  public String getFullId() {
    return fullId;
  }

  public String getShortName() {
    if (fullId.matches(".*MSIE 5.*")) {
      return "MSIE 5.5";
    }

    if (fullId.matches(".*Mozilla/4.*")) {
      return "Netscape 5.5";
    }

    if (fullId.matches(".*Mozilla/5.*")) {
      return "Firefox 1.x";
    }

    if (fullId.matches(".*Opera/7.*")) {
      return "Opera 7.x";
    }

    if (fullId.matches(".*Mac.*Safari.*")) {
      return "Safari 5.x";
    }

    if (fullId.matches(".*MSIE 6.*")) {
      return "MSIE 6.0";
    }

    if (fullId.matches("NetNewsWire.*")) {
      return "NetNewsWire";
    }

    return fullId;
  }

  @Override
  public String toString() {
    return fullId;
  }

  @Override
  public int hashCode() {
    return fullId.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof UserAgent)) {
      return false;
    }

    UserAgent s = (UserAgent) obj;

    return fullId.equals(s.fullId);
  }
}
