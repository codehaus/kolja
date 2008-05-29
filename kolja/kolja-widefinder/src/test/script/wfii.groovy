import com.baulsupp.kolja.widefinder.format.*;
import com.baulsupp.kolja.widefinder.report.*;
import com.baulsupp.kolja.ansi.reports.basic.*;
import com.baulsupp.kolja.ansi.reports.*;
import com.baulsupp.kolja.ansi.reports.script.*;
import com.baulsupp.kolja.log.line.*;
import java.util.regex.*;

class WFIIArticles extends ScriptReport {
  static MB = new MegaBytesFormat()
  static NUMBER = new SimpleFormat()
  static ITEM = new TruncatedFormat(60)
  
  static pattern = ~/\/ongoing\/When\/\d\d\dx\/\d\d\d\d\/\d\d\/\d\d\/[^ .]+/
  
  def articles = new Frequencies()
  def bytes = new Frequencies()
  def _404 = new Frequencies()
  def client = new Frequencies()
  def referrer = new Frequencies()
  
  void processLine(Line line) {
    if (!line.action.equals("GET")) {
      return
    }
  
    if (line.status.isHit() && pattern.matcher(line.url).matches()) {
      articles.increment(line.url)
      client.increment(line.ipaddress)
      
      if (line.referrer && !line.referrer.startsWith("http://www.tbray.org/ongoing/")) {
          referrer.increment(line.referrer)
      }  
    }
    
    if (line.size && line.status.is("200")) {
      bytes.incrementBy(line.url, line.size)
    }
    
    if (line.status.is("404")) {
      _404.increment(line.url)
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
    println(title)
    for (count in frequencies.getMostFrequent(10)) {
      println(count, format, itemFormat)
    }
    println("")
  }

  void println(count, format, itemFormat) {
    println(String.format(" %10s: %s", format.format(count.count), itemFormat.format(count.item)))
  }
}
