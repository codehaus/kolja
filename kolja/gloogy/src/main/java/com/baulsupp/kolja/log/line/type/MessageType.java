package com.baulsupp.kolja.log.line.type;

import com.baulsupp.kolja.log.LogConstants;

/**
 * @author yuri
 */
public class MessageType extends Type {
  private static final long serialVersionUID = 3908770671585945889L;

  public MessageType() {
    super(LogConstants.CONTENT);
  }
  
  public MessageType(String string) {
    super(string);
  }
}
