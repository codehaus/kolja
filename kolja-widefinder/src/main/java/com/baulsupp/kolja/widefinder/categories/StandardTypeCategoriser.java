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

import java.util.HashMap;
import java.util.Map;

import com.baulsupp.kolja.util.MatcherInstance;

/**
 * @author Yuri Schimke
 * 
 */
public class StandardTypeCategoriser implements FileTypeCategoriser {
  private MatcherInstance extPattern = MatcherInstance.compile(".*\\.(\\w+)(\\?.*)?");

  public static final FileType UNKNOWN = new FileType(null, "Unknown", "Unknown Type");

  public static final String CATEGORY_FEED = "Feed";
  public static final String CATEGORY_TEXT = "Text";
  public static final String CATEGORY_IMAGE = "Image";
  public static final String CATEGORY_WEB = "Web";
  public static final String CATEGORY_XML = "XML";

  public static final FileType TYPE_ATOM = new FileType("application/atom+xml", CATEGORY_FEED, "Atom Feed");
  public static final FileType TYPE_RSS = new FileType("application/rss+xml", CATEGORY_FEED, "RSS Feed");
  public static final FileType TYPE_TEXT_PLAIN = new FileType("text/plain", CATEGORY_TEXT, "Plain Text File");
  public static final FileType TYPE_IMAGE_PNG = new FileType("image/png", CATEGORY_IMAGE, "PNG Image");
  public static final FileType TYPE_IMAGE_JPG = new FileType("image/jpeg", CATEGORY_IMAGE, "JPEG Image");
  public static final FileType TYPE_IMAGE_GIF = new FileType("image/gif", CATEGORY_IMAGE, "GIF Image");
  public static final FileType TYPE_CSS = new FileType("text/css", CATEGORY_WEB, "JPEG Image");
  public static final FileType TYPE_JS = new FileType("application/javascript", CATEGORY_WEB, "JavaScript");
  public static final FileType TYPE_PDF = new FileType("application/pdf", CATEGORY_WEB, "PDF");
  public static final FileType TYPE_ICO = new FileType("image/x-icon", CATEGORY_WEB, "Fav Icon");
  public static final FileType TYPE_XML = new FileType("application/xml", CATEGORY_XML, "XML");

  public static final FileType TYPE_TEXT_HTML = new FileType("text/html", CATEGORY_TEXT, "Web Page");

  private Map<String, FileType> types = new HashMap<String, FileType>();

  public StandardTypeCategoriser() {
    initTypes();
  }

  protected void initTypes() {
    types.put("txt", TYPE_TEXT_PLAIN);
    types.put("atom", TYPE_ATOM);
    types.put("png", TYPE_IMAGE_PNG);
    types.put("html", TYPE_TEXT_HTML);
    types.put("rss", TYPE_RSS);
    types.put("jpg", TYPE_IMAGE_JPG);
    types.put("gif", TYPE_IMAGE_GIF);
    types.put("css", TYPE_CSS);
    types.put("js", TYPE_JS);
    types.put("pdf", TYPE_PDF);
    types.put("ico", TYPE_ICO);
    types.put("xml", TYPE_XML);
  }

  public FileType getFileType(String url) {
    String extension = getExtension(url);

    FileType fileType = types.get(extension);

    if (fileType != null) {
      return fileType;
    } else {
      return UNKNOWN;
    }
  }

  public String getExtension(String url) {
    String text = extPattern.match(url);

    if (text != null) {
      return text.toLowerCase();
    } else {
      return null;
    }
  }
}
