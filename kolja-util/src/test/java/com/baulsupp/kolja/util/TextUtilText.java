package com.baulsupp.kolja.util;

import java.util.Arrays;

import junit.framework.TestCase;

public class TextUtilText extends TestCase {
  public void testSoftWrap() {
    assertEquals(Arrays.asList("a", "b", "c"), Arrays.asList(TextUtil.softWrap("a\nb\nc")));
    assertEquals(Arrays.asList("a", "b", "c"), Arrays.asList(TextUtil.softWrap("a\nb\nc\n")));
    assertEquals(Arrays.asList("a"), Arrays.asList(TextUtil.softWrap("a")));
  }

  public void testHardWrap() {
    assertEquals(Arrays.asList("a", "b", "c"), Arrays.asList(TextUtil.hardWrap("a\nb\nc", 2)));
    assertEquals(Arrays.asList("aa", "a", "b"), Arrays.asList(TextUtil.hardWrap("aaa\nb\n", 2)));
    assertEquals(Arrays.asList("aa", "a"), Arrays.asList(TextUtil.hardWrap("aaa", 2)));
  }

  public void testSoftWrapAndCrop() {
    assertEquals(Arrays.asList("aa", "b"), Arrays.asList(TextUtil.softWrapAndCrop("aaa\nb", 2)));
  }
  
  public void testBlanks() {
    assertEquals("", TextUtil.blank(0));
    assertEquals(" ", TextUtil.blank(1));
    assertEquals("     ", TextUtil.blank(5));
  }
}
