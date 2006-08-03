package com.baulsupp.kolja.log.line.type;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.baulsupp.kolja.log.LogConstants;

/**
 * @author yuri
 */
public class DateType extends Type {
  private static final long serialVersionUID = 5057545504316883709L;

  private DateFormat dateFormat;
  
  public DateType() {
    super(LogConstants.DATE);
  }

  public DateType(String name, String dateFormat) {
    super(name);
    this.dateFormat = new SimpleDateFormat(dateFormat);
  }

  public DateFormat getDateFormat() {
    return dateFormat;
  }

  public void setDateFormat(DateFormat dateFormat) {
    this.dateFormat = dateFormat;
  }

  public Object parse(String string) {
    try {
      return dateFormat.parse(string);
    } catch (ParseException e) {
      // TODO debug
      return "INVALID";
    }
  }
}
