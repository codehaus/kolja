package com.baulsupp.kolja.log.viewer.highlight;

import java.io.Serializable;

import com.baulsupp.kolja.log.LogConstants;
import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.line.type.Priority;
import com.baulsupp.kolja.util.ColourPair;

public class PriorityHighlight implements Highlight<Line>, Serializable {
  private static final long serialVersionUID = 2264643873100280184L;

  private String priorityField = LogConstants.PRIORITY;
  
  public PriorityHighlight() {
  }

  public String getPriorityField() {
    return priorityField;
  }

  public void setPriorityField(String priorityField) {
    this.priorityField = priorityField;
  }

  public HighlightResult getHighlights(Line viewRow) {
    Priority priority = (Priority) viewRow.getValue(priorityField);

    if (priority != null) {
      if (priority.atleast(Priority.ERROR)) {
        return HighlightResult.row(ColourPair.RED_ON_WHITE);
      } else if (priority.atleast(Priority.WARN)) {
        return HighlightResult.row(ColourPair.MAGENTA_ON_BLACK);
      } else if (priority.atleast(Priority.INFO)) {
        return HighlightResult.column(priorityField, ColourPair.GREEN_ON_BLACK);
      }
    }
    
    return null;
  }
}
