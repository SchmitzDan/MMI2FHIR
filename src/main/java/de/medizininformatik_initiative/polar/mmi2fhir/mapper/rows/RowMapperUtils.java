package de.medizininformatik_initiative.polar.mmi2fhir.mapper.rows;

import java.time.LocalDate;

public class RowMapperUtils {

  public static Boolean toBoolean(final String s) {
    return s == null ? null : "1".equals(s);
  }

  public static Double toDouble(final String s) {
    return s == null ? null : Double.valueOf(s.replace(',', '.'));
  }

  public static Integer toInteger(final String s) {
    return s == null ? null : Integer.valueOf(s);
  }

  public static LocalDate toLocalDate(final String s) {
    return s == null || s.isEmpty() ? null
        : LocalDate.of(
            Integer.parseInt(s.substring(6)), // year
            Integer.parseInt(s.substring(3, 5)), // month
            Integer.parseInt(s.substring(0, 2))); // day
  }
}
