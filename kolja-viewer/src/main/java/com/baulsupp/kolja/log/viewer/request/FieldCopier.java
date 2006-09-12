package com.baulsupp.kolja.log.viewer.request;

import com.baulsupp.kolja.log.line.Line;

public interface FieldCopier {
  void copy(Line line, RequestLine requestLine);
}
