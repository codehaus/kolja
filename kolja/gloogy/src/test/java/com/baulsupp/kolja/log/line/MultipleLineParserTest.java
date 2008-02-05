package com.baulsupp.kolja.log.line;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class MultipleLineParserTest {
  private BasicLine line1;
  private BasicLine line2;
  private BasicLine failedLine;

  @Before
  public void setup() {
    line1 = new BasicLine("line1");
    line1.setValue("a", "a");

    line2 = new BasicLine("line2");
    line2.setValue("1", "1");

    failedLine = new BasicLine("failedLine");
    failedLine.setFailed();
  }

  @Test
  public void testReturnsAllNames() {
    MultipleLineParser lp = new MultipleLineParser();

    lp.addLineParser(new DummyLineParser(line1));
    lp.addLineParser(new DummyLineParser(line2));

    Set<String> names = lp.getNames();
    assertEquals(2, names.size());
    assertTrue(names.contains("a"));
    assertTrue(names.contains("1"));
  }

  @Test
  public void testReturnsFirstNonFailingLine() {
    MultipleLineParser lp = new MultipleLineParser();

    lp.addLineParser(new DummyLineParser(failedLine));
    lp.addLineParser(new DummyLineParser(line1));
    lp.addLineParser(new DummyLineParser(line2));

    Line line = lp.parse("");

    assertEquals(line1, line);
  }

  private class DummyLineParser implements LineParser {
    private static final long serialVersionUID = -3589336861526640283L;

    private Line line;

    public DummyLineParser(Line line) {
      this.line = line;
    }

    public Set<String> getNames() {
      return line.getValues().keySet();
    }

    public Line parse(CharSequence c) {
      return line;
    }
  }
}
