package wf;

import com.baulsupp.kolja.ansi.reports.ReportRunnerMain;

public class Tsv {
  public static void main(String[] args) {
    ReportRunnerMain.main("-x", "src/main/config/wf.xml", "-r", "tsv?fields=url,status", "src/test/resources/o10k.ap");
  }
}