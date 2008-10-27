package com.baulsupp.kolja.log.viewer.spring;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.joda.time.DateTime;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.line.RegexLineParser;
import com.baulsupp.kolja.log.line.matcher.EntryPattern;
import com.baulsupp.kolja.log.line.matcher.RegexEntryPattern;
import com.baulsupp.kolja.log.line.type.DateType;
import com.baulsupp.kolja.log.line.type.ExceptionType;
import com.baulsupp.kolja.log.line.type.MessageType;
import com.baulsupp.kolja.log.line.type.NameType;
import com.baulsupp.kolja.log.line.type.PriorityType;
import com.baulsupp.kolja.log.line.type.TypeList;
import com.baulsupp.kolja.log.viewer.PrintfLineFormatter;
import com.baulsupp.kolja.log.viewer.event.EventMatcher;
import com.baulsupp.kolja.log.viewer.event.WarnEventMatcher;
import com.baulsupp.kolja.log.viewer.format.CompressedPackageFormat;
import com.baulsupp.kolja.log.viewer.format.JodaFormat;
import com.baulsupp.kolja.log.viewer.format.OutputFormat;
import com.baulsupp.kolja.log.viewer.format.ToStringFormat;
import com.baulsupp.kolja.log.viewer.highlight.HighlightList;
import com.baulsupp.kolja.log.viewer.highlight.PriorityHighlight;
import com.baulsupp.kolja.log.viewer.highlight.RegexHighlight;
import com.baulsupp.kolja.log.viewer.importing.ConfigurableEventFormat;
import com.baulsupp.kolja.log.viewer.importing.ConfigurableLineFormat;
import com.baulsupp.kolja.log.viewer.importing.ConfigurableLogFormat;
import com.baulsupp.kolja.log.viewer.importing.ConfigurableOutputFormat;
import com.baulsupp.kolja.log.viewer.importing.ConfigurableRequestFormat;
import com.baulsupp.kolja.log.viewer.request.BasicFieldCopier;
import com.baulsupp.kolja.log.viewer.request.FieldCopier;
import com.baulsupp.kolja.log.viewer.request.RegexFieldCopier;
import com.baulsupp.kolja.util.colours.Colour;

@SuppressWarnings("deprecation")
public class KoljaNamespaceHandlerTest extends TestCase {
  private static final DateTime DATE = new DateTime(2007, 10, 12, 0, 0, 0, 0);

  public void testLoadsXml() {
    ApplicationContext ac = new ClassPathXmlApplicationContext("sample.xml");

    Map<?, ?> types = ac.getBeansOfType(ConfigurableLogFormat.class);

    assertEquals(1, types.size());

    ConfigurableLogFormat format = (ConfigurableLogFormat) types.values().iterator().next();

    assertNotNull(format);

    ConfigurableLineFormat clf = format.getLineFormat();

    checkLineFormat(clf);

    ConfigurableOutputFormat cof = format.getOutputFormat();

    checkOutputFormat(cof);

    ConfigurableEventFormat cef = format.getEventFormat();

    checkEventFormat(cef);

    ConfigurableRequestFormat crf = format.getRequestFormat();

    checkRequestFormat(crf);
  }

  private void checkLineFormat(ConfigurableLineFormat clf) {
    assertNotNull(clf);

    EntryPattern entryPattern = clf.getEntryPattern();
    assertTrue(entryPattern instanceof RegexEntryPattern);
    Pattern p = ((RegexEntryPattern) entryPattern).getRegexPattern();
    assertEquals(Pattern.MULTILINE, p.flags());
    assertEquals("^20", p.pattern());

    Pattern fieldPattern = ((RegexLineParser) clf.getLineParser()).getPattern();
    assertEquals(Pattern.MULTILINE | Pattern.DOTALL, fieldPattern.flags());
    assertEquals("([\\d-]+ [\\d:,]+) \\[(.*?)\\] \\[(.*?)\\] ?([A-Z]{4,5}) (\\S+) - (.*?)$\\n?(.*)\\z", fieldPattern.pattern());

    TypeList types = ((RegexLineParser) clf.getLineParser()).getColumns();
    assertEquals(7, types.size());

    DateType date = (DateType) types.getTypes().get(0);
    assertEquals("date", date.getName());
    assertEquals(DATE, date.parse("2007-10-12 00:00:00,000"));

    NameType thread = (NameType) types.getTypes().get(1);
    assertEquals("thread", thread.getName());

    NameType request_id = (NameType) types.getTypes().get(2);
    assertEquals("request_id", request_id.getName());

    PriorityType priority = (PriorityType) types.getTypes().get(3);
    assertEquals("priority", priority.getName());

    NameType logger_id = (NameType) types.getTypes().get(4);
    assertEquals("logger_id", logger_id.getName());

    MessageType content = (MessageType) types.getTypes().get(5);
    assertEquals("content", content.getName());

    ExceptionType exception = (ExceptionType) types.getTypes().get(6);
    assertEquals("exception", exception.getName());
  }

  private void checkOutputFormat(ConfigurableOutputFormat cof) {
    assertNotNull(cof);

    assertEquals("[8, 5, 15, 30, 102]", cof.getWidths().toString());
    assertEquals("exception", cof.getAdditional());

    List<OutputFormat> formats = cof.getFormats();
    assertNotNull(formats);
    assertEquals(5, formats.size());

    List<String> names = cof.getNames();
    assertNotNull(names);
    assertEquals(5, names.size());

    JodaFormat time = (JodaFormat) formats.get(0);
    assertEquals("date", names.get(0));
    assertEquals("00:00.00", time.format(DATE));

    ToStringFormat priority = (ToStringFormat) formats.get(1);
    assertNotNull(priority);
    assertEquals("priority", names.get(1));

    ToStringFormat requestId = (ToStringFormat) formats.get(2);
    assertNotNull(requestId);
    assertEquals("request_id", names.get(2));

    CompressedPackageFormat loggerId = (CompressedPackageFormat) formats.get(3);
    assertNotNull(loggerId);
    assertEquals("logger_id", names.get(3));

    ToStringFormat content = (ToStringFormat) formats.get(4);
    assertNotNull(content);
    assertEquals("content", names.get(4));

    HighlightList<Line> highlights = cof.getHighlights();
    assertNotNull(highlights);
    assertEquals(2, highlights.size());

    PriorityHighlight priorityHighlight = (PriorityHighlight) highlights.getHighlights().get(0);
    assertEquals("priority", priorityHighlight.getPriorityField());

    RegexHighlight regexHighlight = (RegexHighlight) highlights.getHighlights().get(1);
    assertEquals("content", regexHighlight.getContentField());
    assertEquals(0, regexHighlight.getPattern().flags());
    assertEquals("(BEGIN|END)", regexHighlight.getPattern().pattern());
    assertEquals(Colour.BLUE, regexHighlight.getColours().getForeground());
    assertEquals(Colour.BLACK, regexHighlight.getColours().getBackground());
  }

  private void checkEventFormat(ConfigurableEventFormat cef) {
    assertNotNull(cef);

    List<EventMatcher> matchers = cef.getEventMatchers();
    assertEquals(1, matchers.size());

    WarnEventMatcher warn = (WarnEventMatcher) matchers.get(0);
    assertNotNull(warn);
  }

  private void checkRequestFormat(ConfigurableRequestFormat crf) {
    assertNotNull(crf);

    assertEquals(true, crf.isOffsetIsEnd());
    assertEquals("BEGIN", crf.getStartPattern());
    assertEquals("END request", crf.getEndPattern());

    String[] fields = crf.getFields();
    assertArrayEquals(new String[] { "request_id", "thread" }, fields);

    List<FieldCopier> matchers = crf.getMatchers();
    assertEquals(3, matchers.size());

    RegexFieldCopier userMatcher = (RegexFieldCopier) matchers.get(0);
    assertEquals("user: (.*)", userMatcher.getPattern().pattern());
    assertEquals(0, userMatcher.getPattern().flags());
    assertArrayEquals(new String[] { "user" }, userMatcher.getFields());

    RegexFieldCopier methodMatcher = (RegexFieldCopier) matchers.get(1);
    assertEquals("method: (.*)", methodMatcher.getPattern().pattern());
    assertEquals(0, methodMatcher.getPattern().flags());
    assertArrayEquals(new String[] { "method" }, methodMatcher.getFields());

    BasicFieldCopier exceptionMatcher = (BasicFieldCopier) matchers.get(2);
    assertEquals("exception", exceptionMatcher.getField());

    PrintfLineFormatter formatter = (PrintfLineFormatter) crf.getStatusFormatter();
    assertEquals("%s %s %s (%s ms)", formatter.getPattern());
    assertArrayEquals(new String[] { "request_id", "user", "method", "duration" }, formatter.getFields());
  }

  private void assertArrayEquals(String[] strings, String[] fields) {
    assertEquals(Arrays.asList(strings), Arrays.asList(fields));
  }
}
