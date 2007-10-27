package launch;

import com.baulsupp.kolja.ansi.reports.ReportRunnerMain;

public class MFP {
  public static void main(String[] args) {
    ReportRunnerMain.main(new String[] { "-x", "src/main/resources/wf.xml", "-r", "pages,status", "src/test/resources/o10k.ap" });
  }
}
