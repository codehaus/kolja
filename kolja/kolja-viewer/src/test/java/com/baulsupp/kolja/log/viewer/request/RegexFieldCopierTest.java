package com.baulsupp.kolja.log.viewer.request;

import junit.framework.TestCase;

import com.baulsupp.kolja.log.line.BasicLine;
import com.baulsupp.kolja.log.line.Line;

public class RegexFieldCopierTest extends TestCase {
  public void testCopiesFieldFromInputLine() {
    RegexFieldCopier copier = new RegexFieldCopier("content", "(aaa)", "content2");
    
    Line line = new BasicLine("aaa");
    line.setValue("content", "baaab");
    RequestLine requestLine = new RequestLine("a", "ok");
    copier.copy(line, requestLine);
    
    assertEquals("aaa", requestLine.getValue("content2"));
  }
  
  public void testWorksWithNullField() {
    RegexFieldCopier copier = new RegexFieldCopier("content", "(.*)", "content2");
    
    Line line = new BasicLine("aaa");
    RequestLine requestLine = new RequestLine("a", "ok");
    copier.copy(line, requestLine);
    
    assertNull(requestLine.getValue("content2"));
  }
}
