package com.baulsupp.curses.list;

import jcurses.system.InputChar;
import jcurses.util.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Util {
  private static final Logger log = LoggerFactory.getLogger(Util.class);

  private static final InputChar KEY_UP = new InputChar(InputChar.KEY_UP);

  private static final InputChar KEY_DOWN = new InputChar(InputChar.KEY_DOWN);

  private static final InputChar KEY_LEFT = new InputChar(InputChar.KEY_LEFT);

  private static final InputChar KEY_RIGHT = new InputChar(InputChar.KEY_RIGHT);

  public static boolean wasDown(InputChar inp) {
    return inp.isSpecialCode() && inp.equals(KEY_DOWN);
  }

  public static boolean wasUp(InputChar inp) {
    return inp.isSpecialCode() && inp.equals(KEY_UP);
  }

  public static boolean wasCtrlD(InputChar inp) {
    return !inp.isSpecialCode() && inp.getCode() == 4;
  }

  public static boolean wasCtrlU(InputChar inp) {
    return !inp.isSpecialCode() && inp.getCode() == 21;
  }

  public static boolean was_q(InputChar inp) {
    return !inp.isSpecialCode() && inp.getCharacter() == 'q';
  }

  public static boolean wasCtrlR(InputChar inp) {
    return !inp.isSpecialCode() && inp.getCode() == 18;
  }
//
//  public static boolean wasLeft(InputChar inp) {
//    return inp.isSpecialCode() && inp.getCode() == 260;
//  }
//
//  public static boolean wasRight(InputChar inp) {
//    return inp.isSpecialCode() && inp.getCode() == 261;
//  }

  public static boolean wasLetter(InputChar inp, char... values) {
    if (inp.isSpecialCode()) {
      return false;
    }
    
    for (char c : values) {
      if (inp.getCharacter() == c) {
        return true;
      }
    }
    
    return false;
  }

  public static void showError(Exception e) {
    Message d = new Message("error", e.toString(), "ok");
    d.show();
  }

  public static boolean wasReturn(InputChar arg0) {
    return !arg0.isSpecialCode() && arg0.getCharacter() == '\n';
  }

  public static boolean wasCtrlB(InputChar inp) {
    log.info("code " + inp.getCode());
    return !inp.isSpecialCode() && inp.getCode() == 2;
  }

  public static boolean wasCtrlN(InputChar inp) {
    return !inp.isSpecialCode() && inp.getCode() == 14;
  }

  public static boolean wasCtrlP(InputChar inp) {
    return !inp.isSpecialCode() && inp.getCode() == 16;
  }

  public static boolean isMovement(InputChar inp) {
    if (Util.wasDown(inp))
      return true;
    else if (Util.wasUp(inp))
      return true;
    else if (Util.wasRight(inp))
      return true;
    else if (Util.wasLeft(inp))
      return true;
    else if (Util.wasCtrlD(inp))
      return true;
    else if (Util.wasCtrlU(inp))
      return true;
    else
      return false;
  }

  public static boolean wasLeft(InputChar inp) {
    return inp.isSpecialCode() && inp.equals(KEY_LEFT);
  }

  public static boolean wasRight(InputChar inp) {
    return inp.isSpecialCode() && inp.equals(KEY_RIGHT);
  }

  public static boolean wasEnd(InputChar inp) {
    return false;
//    return inp.isSpecialCode() && inp.equals(KEY_LEFT);
  }

  public static boolean wasHome(InputChar inp) {
    return false;
//    return inp.isSpecialCode() && inp.equals(KEY_RIGHT);
  }

  public static boolean isCode(InputChar inp, int... key) {
    for (int i : key) {
      if (inp.getCode() == i) {
        return true;
      }
    }
    
    return false;
  }
}
