package com.baulsupp.kolja.log.viewer.io;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import com.baulsupp.kolja.log.line.Line;
import com.baulsupp.kolja.log.line.RegexLineParser;
import com.baulsupp.kolja.log.line.type.NameType;
import com.baulsupp.kolja.log.line.type.TypeList;

public class FileLineIteratorTest extends TestCase {
  private File myFile;
  private FileLineIterator i;
  
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    
    myFile = File.createTempFile("test-", ".log");
    myFile.deleteOnExit();

    BufferingStringBuilder content = new FileBufferingStringBuilder(myFile);

    Pattern entryPattern = Pattern.compile("^\\d", Pattern.MULTILINE);
    Pattern fieldPattern = Pattern.compile("(\\d+) - (.*)");
    TypeList types = TypeList.build(new NameType("order"), new NameType("content"));
    i = new FileLineIterator(content, entryPattern, new RegexLineParser(fieldPattern, types));
  }
  
  @Override
  protected void tearDown() throws Exception {
    myFile.delete();
    
    super.tearDown();
  }
  
  public void testStandard() throws IOException {
    assertFalse(i.hasNext());
    
    IoUtil.writeContent(myFile, "A", "B", "C");
    
    assertTrue(i.hasNext());
    Line l = i.next();
    assertNotNull(l);
    assertEquals("0 - A", l.toString().trim());
    assertFalse(l.isFailed());
    assertEquals("A", l.getValue("content"));

    assertTrue(i.hasNext());
    l = i.next();
    assertNotNull(l);
    assertEquals("1 - B", l.toString().trim());
    assertFalse(l.isFailed());
    assertEquals("B", l.getValue("content"));

    assertTrue(i.hasNext());
    l = i.next();
    assertNotNull(l);
    assertEquals("2 - C", l.toString().trim());
    assertFalse(l.isFailed());
    assertEquals("C", l.getValue("content"));

    assertFalse(i.hasNext());
  }

  public void testTailing() throws IOException {
    i.setTailing(true);
    
    assertTrue(i.hasNext());
    
    IoUtil.writeContent(myFile, "A", "B");
    
    assertTrue(i.hasNext());
    Line l = i.next();
    assertNotNull(l);
    assertFalse(l.isFailed());
    assertEquals("A", l.getValue("content"));

    assertTrue(i.hasNext());
    l = i.next();
    assertNotNull(l);
    assertFalse(l.isFailed());
    assertEquals("B", l.getValue("content"));

    assertTrue(i.hasNext());
    
    IoUtil.writeContent(myFile, "A", "B");
    
    assertTrue(i.hasNext());
    l = i.next();
    assertNotNull(l);
    assertFalse(l.isFailed());
    assertEquals("A", l.getValue("content"));

    assertTrue(i.hasNext());
    l = i.next();
    assertNotNull(l);
    assertFalse(l.isFailed());
    assertEquals("B", l.getValue("content"));
  }
}
