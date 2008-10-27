package jez;

import com.baulsupp.kolja.ansi.CatMain;

public class CatLog {
  public static void main(String[] args) {
    CatMain.main("-x", "src/main/config/jez.xml", "-a", "../test-files/log4j-nyssa.log");
  }
}
