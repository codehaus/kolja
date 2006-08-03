package com.baulsupp.kolja.log.viewer.format;

import java.io.Serializable;

public interface OutputFormat extends Serializable {
  String format(Object value);
}
