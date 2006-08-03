package com.baulsupp.kolja.log.line.type;

import java.io.Serializable;

/**
 * @author yuri
 */
public abstract class Type implements Serializable {
  private String name;

  public Type() {
  }

  public Type(String name) {
    this.name = name;
  }
  
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Object parse(String string) {
    return string;
  }
}
