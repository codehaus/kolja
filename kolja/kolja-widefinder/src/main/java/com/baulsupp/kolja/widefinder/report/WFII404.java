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
package com.baulsupp.kolja.widefinder.report;

import com.baulsupp.kolja.widefinder.format.HttpStatus;
import com.baulsupp.kolja.widefinder.format.WideFinderLine;

/**
 * @author Yuri Schimke
 * 
 */
public class WFII404 extends WFIIBase<WFII404> {
  private static final long serialVersionUID = 5637074836971432761L;

  public String describe() {
    return "Top 404s";
  }

  @Override
  public void processLine(WideFinderLine line) {
    if (line.getStatus().equals(HttpStatus.CLIENT_ERROR_NOT_FOUND)) {
      counts.increment(line.getUrl());
    }
  }
}
