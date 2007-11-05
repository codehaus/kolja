package wf;

import com.baulsupp.kolja.ansi.reports.ReportRunnerMain;

public class MFP20 {
  public static void main(String[] args) {
    ReportRunnerMain.main("-x", "src/main/resources/wf.xml", "-r", "pages?count=10", "src/test/resources/o1000k.ap");
  }
}
