package com.baulsupp.kolja.log.line.type;

import com.baulsupp.kolja.log.LogConstants;

/**
 * @author yuri
 */
public class PriorityType extends Type {
  private static final long serialVersionUID = 3381706252532109589L;
  
  public PriorityType() {
    super(LogConstants.PRIORITY);
  }

  public PriorityType(String string) {
    super(string);
  }

  @Override
  public Object parse(String string) {
    return Priority.valueOf(string);
  }
}
