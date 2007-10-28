package bank;

import com.baulsupp.kolja.ansi.CatMain;

public class CatLog {
  public static void main(String[] args) {
    CatMain.main(new String[] { "-x", "src/main/resources/bank.xml", "..\\test-files\\dmsserver-1.log" });
  }
}
