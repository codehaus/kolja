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
package com.baulsupp.kolja.log.line;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Multiple Line Parser that tries different parsers
 * 
 * @author Yuri Schimke
 */
public class MultipleLineParser implements LineParser {
  private static final long serialVersionUID = -5520246786515827705L;

  private List<LineParser> parsers = new ArrayList<LineParser>();

  public Set<String> getNames() {
    Set<String> names = new HashSet<String>();

    for (LineParser lp : parsers) {
      names.addAll(lp.getNames());
    }

    return names;
  }

  public Line parse(CharSequence c) {
    String string = c.toString();

    for (LineParser lp : parsers) {
      Line l = lp.parse(string);

      if (!l.isFailed()) {
        return l;
      }
    }

    BasicLine basicLine = new BasicLine(string);
    basicLine.setFailed();
    return basicLine;
  }

  public void addLineParser(LineParser lineParser) {
    parsers.add(lineParser);
  }
}
