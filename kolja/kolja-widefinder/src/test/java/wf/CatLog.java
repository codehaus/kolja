package wf;

import com.baulsupp.kolja.ansi.CatMain;

public class CatLog {
  public static void main(String[] args) {
    CatMain.main(new String[] { "-x", "src/main/resources/wf.xml", "-a", "src/test/resources/o10k.ap" });
  }
}
