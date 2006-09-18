package com.baulsupp.kolja.log.viewer.renderer;

import java.util.List;

import com.baulsupp.kolja.util.colours.MultiColourString;

public interface TextDisplayRow {

  int getHeight();

  List<MultiColourString> getLines();

}