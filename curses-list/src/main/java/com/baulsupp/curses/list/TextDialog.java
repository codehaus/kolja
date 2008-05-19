package com.baulsupp.curses.list;

import jcurses.event.ActionEvent;
import jcurses.event.ActionListener;
import jcurses.system.InputChar;
import jcurses.widgets.BorderLayoutManager;
import jcurses.widgets.Button;
import jcurses.widgets.Dialog;
import jcurses.widgets.TextField;
import jcurses.widgets.WidgetsConstants;

public class TextDialog extends Dialog {
  private static final long serialVersionUID = -2438075837387252360L;

  private TextField field;

  private String result;

  public TextDialog(String text, String okText) {
    super(32, 3, true, text);

    BorderLayoutManager manager = new BorderLayoutManager();
    getRootPanel().setLayoutManager(manager);

    field = new TextField(28);

    manager.addWidget(field, BorderLayoutManager.CENTER, WidgetsConstants.ALIGNMENT_CENTER,
        WidgetsConstants.ALIGNMENT_CENTER);

    if (okText != null) {
      Button ok = new Button("Goto");
      ok.addListener(new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
          saveAndClose();
        }
      });

      manager.addWidget(ok, BorderLayoutManager.SOUTH, WidgetsConstants.ALIGNMENT_CENTER,
          WidgetsConstants.ALIGNMENT_CENTER);
    }
  }

  protected void onChar(InputChar arg0) {
    if (Util.wasReturn(arg0))
      saveAndClose();
    else
      super.onChar(arg0);
  }

  public static String getValue(String prompt) {
    TextDialog d = new TextDialog(prompt, null);

    d.show();

    return d.result;
  }

  private void saveAndClose() {
    result = field.getText();
    close();
  }
}
