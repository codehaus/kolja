package com.baulsupp.kolja.log.viewer.io;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.line.type.NameType;
import com.baulsupp.kolja.log.line.type.TypeList;
import com.baulsupp.kolja.log.viewer.importing.ConfigurableLineFormat;
import com.baulsupp.kolja.log.viewer.importing.ConfigurableLogFormat;

public class IoUtilTest extends TestCase {
  public void testLoadFromFile() throws IOException {
    File f = IoUtil.createTestFile();
    
    ConfigurableLogFormat format = new ConfigurableLogFormat();
    ConfigurableLineFormat lineFormat = new ConfigurableLineFormat();
    lineFormat.setEntryPattern(Pattern.compile("^\\d", Pattern.MULTILINE));
    lineFormat.setFieldPattern(Pattern.compile("(\\d+) - (.*)"));
    lineFormat.setTypes(TypeList.build(new NameType("order"), new NameType("content")));
    format.setLineFormat(lineFormat);

    IoUtil.writeContent(f, "A", "B", "C");
    
    Iterator<Line> i = IoUtil.loadFiles(format, Arrays.asList(f));

    assertTrue(i.hasNext());
    Line l = i.next();
    assertNotNull(l);
    assertEquals("A", l.getValue("content"));

    assertTrue(i.hasNext());
    l = i.next();
    assertNotNull(l);
    assertEquals("B", l.getValue("content"));

    assertTrue(i.hasNext());
    l = i.next();
    assertNotNull(l);
    assertEquals("C", l.getValue("content"));

    assertFalse(i.hasNext());
  }
}
