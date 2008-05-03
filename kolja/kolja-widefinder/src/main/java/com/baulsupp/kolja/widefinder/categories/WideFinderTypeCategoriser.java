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
package com.baulsupp.kolja.widefinder.categories;

import com.baulsupp.kolja.util.MatcherInstance;

/**
 * @author Yuri Schimke
 */
public class WideFinderTypeCategoriser extends StandardTypeCategoriser {
  private MatcherInstance matcher = MatcherInstance.compile("/ongoing/When/200x/.*/[^.]+");

  @Override
  public FileType getFileType(String url) {
    if (url.endsWith(".comments")) {
      return StandardTypeCategoriser.TYPE_TEXT_HTML;
    }

    FileType fileType = super.getFileType(url);

    if (fileType == StandardTypeCategoriser.UNKNOWN) {
      if (matcher.matches(url)) {
        fileType = StandardTypeCategoriser.TYPE_TEXT_HTML;
      }
    }

    return fileType;
  }
}
