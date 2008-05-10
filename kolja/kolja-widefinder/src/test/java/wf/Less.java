package wf;

import com.baulsupp.less.LessMain;

public class Less {
  public static void main(String[] args) {
    LessMain.main("-x", "src/main/config/wf.xml", "-a", "src/test/logs/o10k.ap");
  }
}
