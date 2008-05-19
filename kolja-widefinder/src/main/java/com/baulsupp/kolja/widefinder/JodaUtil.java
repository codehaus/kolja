package com.baulsupp.kolja.widefinder;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

public final class JodaUtil {
  public static final char MONTH = 'M';
  public static final char DAY = 'd';
  public static final char HOUR = 'H';
  public static final char MINUTE = 'm';
  public static final char SECOND = 's';
  public static final char MILLIS = 'S';

  private JodaUtil() {
  }

  public static Partial buildPartial(DateTime time, DateTimeFieldType... types) {
    int[] values = new int[types.length];

    for (int i = 0; i < types.length; i++) {
      values[i] = time.get(types[i]);
    }

    Partial range = new Partial(types, values);
    return range;
  }

  public static DateTimeFieldType[] parseFields(String fields) {
    List<DateTimeFieldType> types = new ArrayList<DateTimeFieldType>();

    for (char c : fields.toCharArray()) {
      switch (c) {
      case MONTH:
        types.add(DateTimeFieldType.monthOfYear());
        break;
      case DAY:
        types.add(DateTimeFieldType.dayOfMonth());
        break;
      case HOUR:
        types.add(DateTimeFieldType.hourOfDay());
        break;
      case MINUTE:
        types.add(DateTimeFieldType.minuteOfHour());
        break;
      case SECOND:
        types.add(DateTimeFieldType.secondOfMinute());
        break;
      case MILLIS:
        types.add(DateTimeFieldType.millisOfSecond());
        break;
      default:
        throw new IllegalArgumentException("unknown type " + c);
      }
    }

    return types.toArray(new DateTimeFieldType[0]);
  }

  public static DateTimeFormatter buildDateTimeFormatter(DateTimeFieldType... dateTimeFieldTypes) {
    DateTimeFormatterBuilder b = new DateTimeFormatterBuilder();
    for (DateTimeFieldType dt : dateTimeFieldTypes) {
      b.appendText(dt);
    }
    return b.toFormatter();
  }
}
