package com.baulsupp.kolja.log.line.type;

import com.baulsupp.kolja.log.LogConstants;

/**
 * @author yuri
 */
public class ExceptionType extends Type {
  private static final long serialVersionUID = -576545673735135749L;

  public ExceptionType() {
    super(LogConstants.EXCEPTION);
  }
  
  public ExceptionType(String string) {
    super(string);
  }
}
