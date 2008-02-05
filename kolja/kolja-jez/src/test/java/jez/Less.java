package jez;

import com.baulsupp.less.LessMain;

public class Less {
  public static void main(String[] args) {
    LessMain.main("-x", "src/main/config/jez.xml", "-a", "../test-files/log4j-nyssa.log");
  }
}
