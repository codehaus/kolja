package com.baulsupp.kolja.log.line.type;

public enum Priority {
  TRACE(10, "TRACE"), DEBUG(20, "DEBUG"), INFO(30, "INFO"), WARN(40, "WARN"), ERROR(50, "ERROR"), FATAL(60, "FATAL"), UNKNOWN(
      70, "UNKNOWN");

  private int level;

  private String name;

  Priority(int level, String name) {
    this.level = level;
    this.name = name;
  }

  public int getLevel() {
    return level;
  }

  public String getName() {
    return name;
  }

  public boolean atleast(Priority other) {
    return level >= other.level;
  }

  public static final Priority getByName(String name) {
    return Priority.valueOf(name);
  }
}
