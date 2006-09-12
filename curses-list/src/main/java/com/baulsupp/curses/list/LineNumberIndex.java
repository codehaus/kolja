package com.baulsupp.curses.list;

/**
 * A Zero based index of lines in a file.
 */
public interface LineNumberIndex {
  /**
   * Returns the line number of a given byte offset in a file.
   * 
   * @param position the byte offset in a file.
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
   * @param lineNumber the file offset.
   * @return
   */
  int offset(int lineNumber);
}
