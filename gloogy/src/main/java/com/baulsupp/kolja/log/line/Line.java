package com.baulsupp.kolja.log.line;

import java.util.Map;

public interface Line extends CharSequence {
  Object getValue(String name);

  void setValue(String name, Object value);

  boolean isFailed();

  int getOffset();

  void setOffset(int lineStart);

  Map<String, Object> getValues();

  Object[] getValues(String[] fields);
}
