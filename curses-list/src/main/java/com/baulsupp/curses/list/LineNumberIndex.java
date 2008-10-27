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
package com.baulsupp.curses.list;

/**
 * A Zero based index of lines in a file.
 */
public interface LineNumberIndex {
  /**
   * Returns the line number of a given byte offset in a file.
   * 
   * @param position
   *          the byte offset in a file.
   * @return the line number.
   */
  int lineNumber(int position);

  /**
   * Returns the total number of lines of file.
   * 
   * @return number of lines.
   */
  int numberOfLines();

  /**
   * Returns the file offset of a given line number.
   * 
   * @param lineNumber
   *          the file offset.
   * @return the offset of the line number, or -1 if if past end of file.
   */
  int offset(int lineNumber);
}
