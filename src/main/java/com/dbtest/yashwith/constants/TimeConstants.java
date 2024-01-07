package com.dbtest.yashwith.constants;

import java.util.concurrent.TimeUnit;

public class TimeConstants {

    public static final int ONE_YEAR_IN_MONTHS = 12;
    public static final long TEN_SECONDS = 10;
    public static final long FIVE_MINUTES_IN_SECONDS = 300;

    private TimeConstants() {}

    public static final Integer THIRTY_MIN_IN_SEC = 60 * 30; // 30 minutes in seconds
    public static final Integer ONE_WEEK_IN_SECONDS = 7 * 86400; // One Week in Seconds
    public static final Integer SIX_HOURS_IN_SECONDS = 6 * 60 * 60;
    public static final long SIX_HOURS_IN_MILLISECONDS = 6 * 60 * 60 * 1000;
    public static final Integer HOURS_IN_A_MONTH = 30 * 24;
    public static final Integer HOURS_IN_FIVE_DAYS = 120;
    public static final Integer TEN_MINUTES_IN_MILLISECONDS = 10 * 60 * 1000;
    public static final Integer ONE_HOUR_IN_MILLISECONDS = 60 * 60 * 1000;
    public static final Integer ONE_WEEK_IN_MILLISECONDS = 7 * 24 * 60 * 60 * 1000;
    public static final Integer FIVE_MINUTES_IN_MILLISECONDS = 5 * 60 * 1000;
    public static final Integer TWO_SECONDS = 2;
    public static final Integer THIRTY_SECONDS = 30;
    public static final Integer ONE_HOUR_IN_SECONDS = 60 * 60;
    public static final Integer FOUR_HOURS_IN_SECONDS = 60 * 60 * 4;
    public static final Integer ONE_DAY_IN_SECONDS = 24 * 60 * 60;
    public static final Integer ONE_MINUTES_IN_SECONDS = 60;
    public static final long ONE_MONTH_IN_SECONDS = 2678400;

    public static final long TWO_SECONDS_IN_MILLISECONDS = 2 * 1000;

    public static final Integer ONE_DAY_IN_MILLISECONDS = 86400000;

    /**
     * Get current time in given unit
     *
     * @param timeUnit
     * @return
     */
    public static Long getCurrentTime(TimeUnit timeUnit) {
        Long epoch = System.currentTimeMillis();
        switch (timeUnit) {
            case MILLISECONDS:
                return epoch;
            case MICROSECONDS:
                return timeUnit.toMicros(epoch);
            case NANOSECONDS:
                return timeUnit.toNanos(epoch);
        }
        return null;
    }
}
