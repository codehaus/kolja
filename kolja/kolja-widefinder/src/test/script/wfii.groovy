import com.baulsupp.kolja.widefinder.format.*;
import com.baulsupp.kolja.widefinder.report.*;
import com.baulsupp.kolja.ansi.reports.basic.*;
import com.baulsupp.kolja.ansi.reports.*;
import com.baulsupp.kolja.log.line.*;
import java.util.regex.*;

class WFIIArticles extends BaseTextReport {
  def MB = new MegaBytesFormat()
  def NUMBER = new SimpleFormat()
  def TruncatedFormat ITEM = new TruncatedFormat(60)
  
  def articles = new Frequencies()
  def bytes = new Frequencies()
  def _404 = new Frequencies()
  def client = new Frequencies()
  def referrer = new Frequencies()
  
  transient Matcher matcher;

  void processLine(Line line) {
    if (matchArticle(line.url) && line.status.isHit()) {
      articles.increment(line.url)
    }
  }
  
  void completed() {
    printSection("Top URIs by hit", articles, NUMBER, ITEM)
    printSection("Top URIs by bytes", bytes, MB, ITEM)
    printSection("Top 404s", _404, NUMBER, ITEM)
    printSection("Top client addresses", client, NUMBER, ITEM)
    printSection("Top referrers", referrer, NUMBER, ITEM)
  }

  void printSection(title, frequencies, format, itemFormat) {
    println(title);
    for (count in frequencies.getMostFrequent(10)) {
      println(count, format, itemFormat);
    }
    println("");
  }

  void println(count, format, itemFormat) {
    println(String.format(" %10s: %s", format.format(count.count), itemFormat.format(count.item)));
  }

  protected boolean matchArticle(String url) {
    if (matcher == null) {
      matcher = Pattern.compile("/ongoing/When/\\d\\d\\dx/\\d\\d\\d\\d/\\d\\d/\\d\\d/[^ .]+").matcher(url);
    } else {
      matcher.reset(url);
    }

    return matcher.matches();
  }
}
