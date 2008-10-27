package com.baulsupp.kolja.ansi;

public class ColorUtil {
  public static final String STYLE_CLEAR = "0";

  public static final String STYLE_BRIGHT = "1";

  public static final String STYLE_NORMAL = "2";

  public static final String STYLE_UNDERLINE = "4";

  public static final String STYLE_BLINK = "5";

  public static final String STYLE_REVERSE = "7";

  public static final String STYLE_CONCEALED = "8";

  public static final String FOREGROUND_BLACK = "30";

  public static final String FOREGROUND_RED = "31";

  public static final String FOREGROUND_GREEN = "32";

  public static final String FOREGROUND_YELLOW = "33";

  public static final String FOREGROUND_BLUE = "34";

  public static final String FOREGROUND_MAGENTA = "35";

  public static final String FOREGROUND_CYAN = "36";

  public static final String FOREGROUND_WHITE = "37";

  public static final String BACKGROUND_BLACK = "40";

  public static final String BACKGROUND_RED = "41";

  public static final String BACKGROUND_GREEN = "42";

  public static final String BACKGROUND_YELLOW = "43";

  public static final String BACKGROUND_BLUE = "44";

  public static final String BACKGROUND_MAGENTA = "45";

  public static final String BACKGROUND_CYAN = "46";

  public static final String BACKGROUND_WHITE = "47";

  public static final void sendCode(String code) {
    System.out.println(((char) 27) + "[" + code + "m");
    System.out.flush();
  }

  public static void main(String[] args) {
    sendCode(BACKGROUND_GREEN);
    System.out.println("Test");
  }
}
