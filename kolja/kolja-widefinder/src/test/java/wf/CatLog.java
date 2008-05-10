package wf;

import com.baulsupp.kolja.ansi.CatMain;

public class CatLog {
  public static void main(String[] args) {
    CatMain.main("-x", "src/main/config/wf.xml", "-a", "src/test/logs/o10k.ap");
  }
}
