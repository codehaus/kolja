package com.baulsupp.kolja.log.viewer.event;

import com.baulsupp.kolja.log.line.Line;

public interface EventMatcher {
  Event match(Line l);
}
