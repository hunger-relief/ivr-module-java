package com.dothat.objectify;

import com.google.api.server.spi.types.DateAndTime;
import com.google.api.server.spi.types.SimpleDate;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Utils to convert dates from and to Joda Date and Time.
 *
 * @author abhideep@ (Abhideep Singh)
 */
public class JodaUtils {

  public static LocalDate toLocalDate(SimpleDate date, boolean useDefaultTimezone) {
    if (date == null) {
      return null;
    }
    if (useDefaultTimezone) {
      return new LocalDate(date.getYear(), date.getMonth(), date.getDay(), ISOChronology.getInstance());
    } else {
      return new LocalDate(date.getYear(), date.getMonth(), date.getDay());
    }
  }
  
  public static SimpleDate toSimpleDate(LocalDate date) {
    if (date == null) {
      return null;
    }
    return new SimpleDate(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth());
  }
  
  public static DateAndTime toDateAndTime(DateTime date) {
    if (date == null) {
      return null;
    }
    DateTimeFormatter dateFormatter = ISODateTimeFormat.dateTime();
    return DateAndTime.parseRfc3339String(date.toString(dateFormatter));
  }
  
  public static DateTime toDateTime(DateAndTime date) {
    if (date == null) {
      return null;
    }
    DateTimeFormatter dateFormatter = ISODateTimeFormat.dateTime();
    return dateFormatter.parseDateTime(date.toRfc3339String());
  }
}
