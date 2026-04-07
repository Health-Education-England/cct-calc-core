package uk.nhs.tis.cctcalccore;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class CctCalculationEngine {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ISO_LOCAL_DATE;

    public static int calculateInclusiveDaySpan(String startDate, String endDate) {
        LocalDate start = parseDate(startDate, "startDate");
        LocalDate end = parseDate(endDate, "endDate");
        if (end.isBefore(start)) {
            throw new IllegalArgumentException(
                    String.format("endDate (%s) must not be before startDate (%s).", endDate, startDate)
            );
        }
        return (int) ChronoUnit.DAYS.between(start, end) + 1;
    }

    public static int calculateAbsenceExtensionDays(int fullTimeDays) {
        return calculateAbsenceExtensionDays(fullTimeDays, 0);
    }

    public static int calculateAbsenceExtensionDays(int fullTimeDays, double trainingDaysRebate) {
        if (fullTimeDays <= 0) {
            throw new IllegalArgumentException(
                    String.format("Invalid fullTimeDays: %s. Must be > 0.", fullTimeDays)
            );
        }

        if (trainingDaysRebate < 0 || trainingDaysRebate > fullTimeDays) {
            throw new IllegalArgumentException(
                    String.format(
                            "Invalid training days rebate: %s. Must be between 0 and fullTimeDays (%s).",
                            trainingDaysRebate,
                            fullTimeDays)
            );
        }

        return fullTimeDays - (int) Math.round(trainingDaysRebate);
    }

    public static int calculateLtftExtensionDays(int fullTimeDays, double ltftWte) {
        return calculateLtftExtensionDays(fullTimeDays, ltftWte, 1);
    }

    public static int calculateLtftExtensionDays(int fullTimeDays, double ltftWte, double currentWte) {
        if (fullTimeDays <= 0) {
            throw new IllegalArgumentException(
                    String.format("Invalid fullTimeDays: %s. Must be > 0.", fullTimeDays)
            );
        }
        if (ltftWte < 0.1 || ltftWte > 1) {
            throw new IllegalArgumentException(
                    String.format("Invalid ltftWte: %s. Must be between 0.1 and 1.", ltftWte)
            );
        }
        if (currentWte < 0.1 || currentWte > 1) {
            throw new IllegalArgumentException(
                    String.format("Invalid currentWte: %s. Must be between 0.1 and 1.", currentWte)
            );
        }

        double wteDays = fullTimeDays * (ltftWte / currentWte);
        return (int) Math.round(fullTimeDays - wteDays);
    }

    public static String calculateNewCct(String baseDate, int extensionDays) {
        return parseDate(baseDate, "baseDate")
                .plusDays(extensionDays)
                .format(FMT);
    }

    private static LocalDate parseDate(String dateValue, String fieldName) {
        try {
            return LocalDate.parse(dateValue, FMT);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException(
                    String.format("Invalid %s: %s.", fieldName, dateValue),
                    ex
            );
        }
    }
}
