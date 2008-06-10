package com.baulsupp.kolja.log.line;

import java.util.Map;

public interface Line extends CharSequence {
  Object getValue(String name);

  void setValue(String name, Object value);

  boolean isFailed();

  long getOffset();

  void setOffset(long from);

  Map<String, Object> getValues();

  int getIntOffset();
}
