/**
 * Copyright (c) 2002-2007 Yuri Schimke. All Rights Reserved.
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package com.baulsupp.kolja.widefinder.format;

import java.util.HashSet;
import java.util.Set;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.line.LineParser;
import com.baulsupp.kolja.widefinder.WideFinderConstants;

/**
 * @author Yuri Schimke
 */
public class WideFinderLineParser implements LineParser {

  // <regex-line-parser>
  // <field-pattern>([^ ]+) - - \[(.*?) .\d{4}\] "([A-Z]+) (.*?) HTTP.*?" (\d+)
  // ([^ ]+) "[^"]*" "([^"]*)".*</field-pattern>
  // <types>
  // <name-type name="ipaddress" />
  // <date-type name="date">
  // <pattern>dd/MMM/yyyy:HH:mm:ss</pattern>
  // </date-type>
  // <name-type name="action" />
  // <name-type name="url" />
  // <custom-type name="status"
  // class="com.baulsupp.kolja.widefinder.format.StatusType" />
  // <custom-type name="size" class="com.baulsupp.kolja.log.line.type.BytesType"
  // />
  // <custom-type name="useragent"
  // class="com.baulsupp.kolja.widefinder.format.UserAgentType" />
  // </types>
  // </regex-line-parser>

  private static final long serialVersionUID = 1L;
  private static final Set<String> NAMES = getColumnNames();

  public Set<String> getNames() {
    return NAMES;
  }

  private static Set<String> getColumnNames() {
    Set<String> set = new HashSet<String>();

    set.add(WideFinderConstants.IPADDRESS);
    set.add(WideFinderConstants.DATE);
    set.add(WideFinderConstants.ACTION);
    set.add(WideFinderConstants.URL);
    set.add(WideFinderConstants.STATUS);
    set.add(WideFinderConstants.USER_AGENT);

    return set;
  }

  public Line parse(CharSequence c) {
    WideFinderLine line = new WideFinderLine(c);
    return line;
  }
}
