package com.baulsupp.kolja.log.line;

import java.io.IOException;

public interface Reloadable {
  boolean reload() throws IOException;
}
