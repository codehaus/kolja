/**
 * Copyright (c) 2002-2007 Yuri Schimke. All Rights Reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.baulsupp.kolja.log.viewer.commands;

import java.util.Collection;
import java.util.Collections;

import jcurses.system.InputChar;

import com.baulsupp.curses.application.Command;
import com.baulsupp.curses.application.KeyBinding;
import com.baulsupp.curses.list.TextDialog;
import com.baulsupp.curses.list.Util;
import com.baulsupp.less.Less;

public class GotoLineCommand implements Command<Less> {
  public boolean handle(Less less, InputChar input) {
    if (!Util.wasLetter(input, 'g')) {
      return false;
    }

    String a = TextDialog.getValue("Goto Line");

    if (a != null) {
      if (a.equals("")) {
        less.end();
      } else {
        int line = Integer.parseInt(a);
        int offset = less.getLineNumbers().offset(line - 1);

        if (offset != -1) {
          less.moveTo(offset);
        }
      }
    }

    return true;
  }

  public Collection<KeyBinding> getDescription() {
    return Collections.singleton(new KeyBinding(new InputChar('g'), "Movement", "Goto Line"));
  }
}
