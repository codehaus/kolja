package com.baulsupp.kolja.log.line.type;

import java.io.Serializable;

/**
 * @author yuri
 */
public abstract class Type implements Serializable {
  private String name;
  protected String nullValue;

  public Type() {
  }

  public Type(String name) {
    this.name = name;
  }

  public Type(String name, String nullValue) {
    this.name = name;
    this.nullValue = nullValue;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Object parse(String string) {
    if (isNull(string)) {
      return null;
    }

    return string;
  }

  protected boolean isNull(String string) {
    return string.equals(nullValue);
  }
}
