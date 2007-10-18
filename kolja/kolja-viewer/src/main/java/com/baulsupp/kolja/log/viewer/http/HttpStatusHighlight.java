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
package com.baulsupp.kolja.log.viewer.http;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.viewer.highlight.Highlight;
import com.baulsupp.kolja.log.viewer.highlight.HighlightResult;
import com.baulsupp.kolja.util.colours.ColourPair;

public class HttpStatusHighlight implements Highlight<Line> {

  public HighlightResult getHighlights(Line viewRow) {
    String status = (String) viewRow.getValue("status");

    if (status == null) {
      return null;
    }

    if (status.equals("200")) {
      return null;
    }

    if (status.startsWith("5")) {
      return HighlightResult.row(ColourPair.RED_ON_BLACK);

    }

    return HighlightResult.column("status", ColourPair.CYAN_ON_BLACK);
  }

}
