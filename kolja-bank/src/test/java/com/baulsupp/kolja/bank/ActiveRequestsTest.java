package com.baulsupp.kolja.bank;

import static junit.framework.Assert.assertEquals;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Test;

import com.baulsupp.kolja.log.LogConstants;
import com.baulsupp.kolja.log.viewer.request.RequestLine;

public class ActiveRequestsTest {
  private BankActiveRequests activeRequests;
  private DateTime noonDate;
  private DateTime beforeDate;
  private DateTime afterDate;

  @Before
  public void setup() {
    LocalDate date = new LocalDate();
    LocalTime noon = new LocalTime(12, 0);
    LocalTime before = new LocalTime(11, 59);
    LocalTime after = new LocalTime(12, 1);
    noonDate = date.toDateTime(noon);
    beforeDate = date.toDateTime(before);
    afterDate = date.toDateTime(after);

    activeRequests = new BankActiveRequests();
    activeRequests.setFrom(noon);
    activeRequests.setTo(after.minusSeconds(1));
  }

  @Test
  public void testMatchesExactLine() {
    RequestLine l = new RequestLine("abc", "Request abc at noon");
    l.setValue(LogConstants.INTERVAL, new Interval(noonDate, noonDate));

    activeRequests.processRequest(l);

    List<RequestLine> lines = activeRequests.getRequests();

    assertEquals(1, lines.size());
    assertEquals(l, lines.get(0));
  }

  @Test
  public void testDoesNotMatchEarlierLine() {
    RequestLine l = new RequestLine("abc", "Request abc at noon - 1");
    l.setValue(LogConstants.INTERVAL, new Interval(beforeDate, beforeDate));

    activeRequests.processRequest(l);

    List<RequestLine> lines = activeRequests.getRequests();

    assertEquals(0, lines.size());
  }

  @Test
  public void testDoesNotMatchLaterLine() {
    RequestLine l = new RequestLine("abc", "Request abc at noon + 1");
    l.setValue(LogConstants.INTERVAL, new Interval(afterDate, afterDate));

    activeRequests.processRequest(l);

    List<RequestLine> lines = activeRequests.getRequests();

    assertEquals(0, lines.size());
  }

  // @Test
  // public void testMatchesWithUnknownStart() {
  // RequestLine l = new RequestLine("abc", "Request abc at noon + 1");
  // l.setValue(LogConstants.DATE_END, afterDate);
  //
  // activeRequests.processRequest(l);
  //
  // List<RequestLine> lines = activeRequests.getRequests();
  //
  // assertEquals(1, lines.size());
  // assertEquals(l, lines.get(0));
  // }
  //
  // @Test
  // public void testMatchesWithUnknownEnd() {
  // RequestLine l = new RequestLine("abc", "Request abc at noon - 1");
  // l.setValue(LogConstants.DATE, beforeDate);
  //
  // activeRequests.processRequest(l);
  //
  // List<RequestLine> lines = activeRequests.getRequests();
  //
  // assertEquals(1, lines.size());
  // assertEquals(l, lines.get(0));
  // }

  @Test
  public void testDoesNotMatchWithUnknownStartAndEnd() {
    RequestLine l = new RequestLine("abc", "Request abc at noon - 1");

    activeRequests.processRequest(l);

    List<RequestLine> lines = activeRequests.getRequests();

    assertEquals(0, lines.size());
  }
}
