package com.baulsupp.kolja.log.filter;

import com.baulsupp.kolja.log.line.Line;

/**
 * @TODO allow batch filters i.e. request id
 */
public interface Filter {
  boolean lineMatches(Line line);
}
