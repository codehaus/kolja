package com.baulsupp.kolja.log.filter;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.line.type.Priority;

public class PriorityFilter implements Filter {
  public PriorityFilter() {
  }

  public boolean lineMatches(Line line) {
    Priority priority = (Priority) line.getValue("priority");

    return priority != null && priority.atleast(Priority.INFO);
  }
}
