package wf;

import com.baulsupp.kolja.ansi.CatMain;

public class DebugLog {
  public static void main(String[] args) {
    CatMain.main("-x", "src/main/resources/wf.xml", "-a", "-d", "src/test/resources/o10k.ap");
  }
}
