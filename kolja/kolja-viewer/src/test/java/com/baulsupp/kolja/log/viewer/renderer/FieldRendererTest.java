package com.baulsupp.kolja.log.viewer.renderer;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import com.baulsupp.kolja.log.line.BasicLine;
import com.baulsupp.kolja.log.viewer.columns.ColumnWidths;
import com.baulsupp.kolja.log.viewer.format.OutputFormat;
import com.baulsupp.kolja.log.viewer.format.ToStringFormat;
import com.baulsupp.kolja.util.MultiColourString;

public class FieldRendererTest extends TestCase {
  private BasicLine l;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    
    l = new BasicLine("abc");
    l.setValue("A", "aaa");
    l.setValue("B", "bbb");
    l.setValue("C", "01234567890123456789");
  }
  
  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
    
    l = null;
  }
  
  public void testSingleLine() {
    FieldRenderer r = new FieldRenderer(ColumnWidths.fixed(3, 10), Arrays.asList("A", "B"), Arrays.asList((OutputFormat) new ToStringFormat(), new ToStringFormat()), null);  
    r.setWidth(80);
    r.setWrappingMode(Wrap.NONE);
    
    TextDisplayRow row = r.getRow(l);
    
    List<MultiColourString> lines = row.getLines();
    
    assertEquals(1, lines.size());
    
    assertEquals("aaa bbb", lines.get(0).toString());
  }
  
  public void testNoWrapping() {
    FieldRenderer r = new FieldRenderer(ColumnWidths.fixed(20), Arrays.asList("C"), Arrays.asList((OutputFormat) new ToStringFormat()), null);  
    r.setWidth(10);
    r.setWrappingMode(Wrap.NONE);
    
    TextDisplayRow row = r.getRow(l);
    
    List<MultiColourString> lines = row.getLines();
    
    assertEquals(1, lines.size());
    
    assertEquals("01234567890123456789", lines.get(0).toString());
  }
  
  public void testWrapping() {
    FieldRenderer r = new FieldRenderer(ColumnWidths.fixed(20), Arrays.asList("C"),  Arrays.asList((OutputFormat) new ToStringFormat()), null);  
    r.setWidth(10);
    r.setWrappingMode(Wrap.LAST_COLUMN);
    
    TextDisplayRow row = r.getRow(l);
    
    List<MultiColourString> lines = row.getLines();
    
    assertEquals(2, lines.size());
    
    assertEquals("0123456789", lines.get(0).toString());
    assertEquals("0123456789", lines.get(1).toString());
  }  
}
