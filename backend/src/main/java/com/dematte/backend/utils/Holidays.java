package com.dematte.backend.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Holidays {
    public static List<LocalDate> calculate(int year) {
        List<LocalDate> holidays = new ArrayList<>();
        holidays.add( LocalDate.of( year, 1, 1 ) );
        holidays.add( calculateEasterSunday( year ).minusDays( 48 ) );
        holidays.add( calculateEasterSunday( year ).minusDays( 47 ) );
        holidays.add( calculateEasterSunday( year ).minusDays( 2 ) );
        holidays.add( LocalDate.of( year, 4, 21 ) );
        holidays.add( LocalDate.of( year, 5, 1 ) );
        holidays.add( LocalDate.of( year, 9, 7 ) );
        holidays.add( LocalDate.of( year, 10, 12 ) );
        holidays.add( LocalDate.of( year, 11, 2 ) );
        holidays.add( LocalDate.of( year, 11, 15 ) );
        holidays.add( LocalDate.of( year, 12, 25 ) );
        holidays.addAll( getAdditionalWeekdayHolidays( year ) );
        return holidays;
    }

    private static List<LocalDate> getAdditionalWeekdayHolidays(int year) {
        List<LocalDate> additionalHolidays = new ArrayList<>();
        LocalDate       easterSunday       = calculateEasterSunday( year );
        // Calculate the date for Corpus Christi (60 days after Easter Sunday)
        LocalDate corpusChristi = easterSunday.plusDays( 60 );
        additionalHolidays.add( corpusChristi );
        // Calculate the date for the Day of Our Lady of Conception Aparecida (October 12 - only if it's a Tuesday)
        LocalDate ladyOfAparecida = LocalDate.of( year, 10, 12 );
        if (ladyOfAparecida.getDayOfWeek() == DayOfWeek.TUESDAY) {
            additionalHolidays.add( ladyOfAparecida );
        }
        return additionalHolidays;
    }

    private static LocalDate calculateEasterSunday(int year) {
        int a     = year % 19;
        int b     = year / 100;
        int c     = year % 100;
        int d     = b / 4;
        int e     = b % 4;
        int f     = (b + 8) / 25;
        int g     = (b - f + 1) / 3;
        int h     = (19 * a + b - d - g + 15) % 30;
        int i     = c / 4;
        int k     = c % 4;
        int l     = (32 + 2 * e + 2 * i - h - k) % 7;
        int m     = (a + 11 * h + 22 * l) / 451;
        int i1    = h + l - 7 * m + 114;
        int month = i1 / 31;
        int day   = (i1 % 31) + 1;
        return LocalDate.of( year, month, day );
    }
}