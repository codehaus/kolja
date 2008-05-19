package wf;

import com.baulsupp.kolja.ansi.reports.ReportRunnerMain;

public class Types {
  public static void main(String[] args) {
    ReportRunnerMain.main("-x", "src/main/config/wf.xml", "-r", "types", "src/test/logs/o10k.ap");
  }
}
