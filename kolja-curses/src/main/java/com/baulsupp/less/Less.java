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
package com.baulsupp.less;

import com.baulsupp.curses.list.CursesListWindow;
import com.baulsupp.curses.list.LineNumberIndex;
import com.baulsupp.kolja.log.line.BasicLineIterator;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.line.LineIndex;
import com.baulsupp.kolja.log.viewer.commands.GotoLineCommand;
import com.baulsupp.kolja.log.viewer.commands.SearchCommand;
import com.baulsupp.kolja.log.viewer.highlight.Highlight;
import com.baulsupp.kolja.log.viewer.highlight.HighlightList;

public class Less extends CursesListWindow<Line, Less> {
  public HighlightList<Line> extraHighlights = new HighlightList<Line>();

  private LineIndex lineIndex;

  public Less() {
  }

  public void createDefaultCommands() {
    super.createDefaultCommands();

    commands.add(new GotoLineCommand());
    commands.add(new SearchCommand(this));
  }

  public void setLineIndex(LineIndex lineIndex) {
    LineIndex oldIndex = this.lineIndex;

    this.lineIndex = lineIndex;

    setModel(new LineIndexItemModel(new BasicLineIterator(lineIndex)));

    listeners.firePropertyChangeEvent(this, "lineIndex", oldIndex, lineIndex);
  }

  public LineNumberIndex getLineNumbers() {
    return lineNumbers;
  }

  public void end() {
    list.end();
  }

  public void moveTo(int i) {
    list.moveTo(i);
  }

  public int getOffset() {
    return list.getOffset();
  }

  public void addFieldHighlighter(Highlight<Line> h) {
    extraHighlights.addHighlight(h);
  }

  public HighlightList<Line> getExtraHighlights() {
    return extraHighlights;
  }

  public LineIndex getLineIndex() {
    return lineIndex;
  }
}