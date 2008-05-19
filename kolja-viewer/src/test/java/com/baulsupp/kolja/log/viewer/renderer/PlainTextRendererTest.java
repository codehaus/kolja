package com.baulsupp.kolja.log.viewer.renderer;

import java.util.List;

import junit.framework.TestCase;

import com.baulsupp.kolja.log.line.BasicLine;
import com.baulsupp.kolja.util.colours.MultiColourString;

public class PlainTextRendererTest extends TestCase {
  private BasicLine l;
  private PlainTextRenderer r;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    
    r = new PlainTextRenderer();  
    l = new BasicLine("abc");
  }
  
  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
    
    r = null;
    l = null;
  }
  
  public void testSingleLine() {
    TextDisplayRow row = r.getRow(l);
    
    List<MultiColourString> lines = row.getLines();
    
    assertEquals(1, lines.size());
    
    assertEquals(new MultiColourString("abc"), lines.get(0));
  }
  
  public void testHardWrap() {
    r.setWidth(2);
    
    TextDisplayRow row = r.getRow(l);
    
    List<MultiColourString> lines = row.getLines();
    
    assertEquals(2, lines.size());
    
    assertEquals(new MultiColourString("ab"), lines.get(0));
    assertEquals(new MultiColourString("c"), lines.get(1));
  }
}
