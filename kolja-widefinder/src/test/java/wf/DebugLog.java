package wf;

import com.baulsupp.kolja.ansi.CatMain;

public class DebugLog {
  public static void main(String[] args) {
    CatMain.main("-x", "src/main/config/wf.xml", "-a", "-d", "src/test/logs/o10k.ap");
  }
}
