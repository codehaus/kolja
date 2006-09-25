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