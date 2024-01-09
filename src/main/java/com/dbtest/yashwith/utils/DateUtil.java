package com.dbtest.yashwith.utils;

import static java.util.Calendar.*;
import static java.util.Calendar.HOUR_OF_DAY;

import com.dbtest.yashwith.constants.TimeConstants;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DateUtil {

    private static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    private static final int oneDayTimeInMillis = 24 * 60 * 60 * 1000;
    private static final List<String> WEEK_DAYS =
            List.of("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");

    public static void main(String[] args) {
        Calendar today = Calendar.getInstance();
        System.out.println("today date: " + today.getTime());
        Calendar next = Calendar.getInstance();
        next.clear();
        next.set(YEAR, today.get(YEAR));
        next.set(MONTH, today.get(MONTH) + 1);
        next.set(DAY_OF_MONTH, 1); // optional, default: 1, our need
        System.out.println("next  date: " + next.getTime());
    }

    public static String getFormattedDate(Date date) {
        try {
            DateFormat formatter = new SimpleDateFormat("dd MMM ''yy, hh:mma");
            formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
            String dateString = formatter.format(date);
            return dateString.replace("AM", "am").replace("PM", "pm");
        } catch (Exception e) {
            log.error("Error occurred in formatting");
        }
        return null;
    }

    /**
     * get Date as a string from one format to other format
     *
     * @param inputDateString - inputDateString
     * @param inputFormat - inputFormat
     * @param outputFormat - outputFormat
     * @return inputDateString in string
     */
    public static String convertFormattedDateString(
            String inputDateString, String inputFormat, String outputFormat) {
        try {
            Date date = convertStringToDate(inputDateString, inputFormat);
            if (date == null) return inputDateString;
            return getFormattedDate(date, outputFormat);
        } catch (Exception e) {
            log.error("Error occurred in formatting inputDateString " + e.getMessage());
        }
        return inputDateString;
    }

    public static int minsDifference(Date date1, Date date2) {
        final int MILLI_TO_MINUTE = 1000 * 60;
        return (int) ((date1.getTime() - date2.getTime()) / MILLI_TO_MINUTE);
    }

    public static String getZonedFormattedDate(Date date) {
        DateFormat formatter = new SimpleDateFormat("dd MMM ''yy, hh:mma");
        String dateString = formatter.format(date);
        return dateString.replace("AM", "am").replace("PM", "pm");
    }

    public static boolean isFromSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
                && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    }

    /**
     * takes input as string and parses to Date object according to input format returns as IST
     *
     * @param date
     * @param format
     * @return Date object
     */
    public static Date convertStringToDate(String date, String format) {
        DateFormat formatter = new SimpleDateFormat(format);
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            log.error("Exception occurred in converting string to data: ", e);
        }
        return null;
    }

    public static Date getDateFromString(String date) {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            log.error("Exception occurred in getting date from string: ", e);
        }
        return null;
    }

    public static long getDateFromString(String date, String format) {
        DateFormat formatter = new SimpleDateFormat(format);
        try {
            return formatter.parse(date).getTime();
        } catch (ParseException e) {
            log.error("Exception occurred in getting date from string in particular format: ", e);
        }
        return 0;
    }

    public static String getFormattedDate(String dateStr, String format) {
        DateFormat formatter = new SimpleDateFormat(format);
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        Date date = getDateFromString(dateStr);
        String dateString = formatter.format(date);
        return dateString.replace("AM", "am").replace("PM", "pm");
    }

    public static String getFormattedDate(Date date, String format) {
        DateFormat formatter = new SimpleDateFormat(format);
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        String dateString = formatter.format(date);
        return dateString.replace("AM", "am").replace("PM", "pm");
    }

    /**
     * difference between current time and input time converted to IST in milliseconds
     *
     * @param date
     * @param format
     * @param millisecondBuffer
     * @return
     */
    public static long getDiffInMillis(String date, String format, long millisecondBuffer) {
        DateFormat formatter = new SimpleDateFormat(format);
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        try {
            return formatter.parse(date).getTime() - (new Date().getTime()) - millisecondBuffer;
        } catch (ParseException e) {
            log.error("Exception occurred in getting difference in millis", e);
        }
        return 0;
    }

    /**
     * difference between current date and input date in milliseconds if input date is before
     * current date returns positive number if input date is after current date returns negative
     * number
     *
     * @return
     */
    public static long getDiffInMilliseconds(Date inputDate) {
        return new Date().getTime() - inputDate.getTime();
    }

    /**
     * difference between input date object and current date object in milliseconds
     *
     * @param date
     * @return
     */
    public static long dateDiffInMillisec(Date date) {
        return date.getTime() - (new Date().getTime());
    }

    /**
     * check if datetime for auspicious time occasion has passed current datetime
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static long getDateValidity(Date startDate, Date endDate) {
        try {
            Date currentDate = new Date();
            if (startDate.before(currentDate) && endDate.after(currentDate)) {
                return endDate.getTime() - new Date().getTime();
            }
        } catch (Exception e) {
            log.error("Exception occurred in getting date validity: ", e);
        }
        return 0;
    }

    public static String getDateInFormat(String timezone) {
        SimpleDateFormat sd = new SimpleDateFormat(yyyy_MM_dd_HH_mm_ss);
        try {
            sd.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
            Date date = sd.parse(timezone);
            return getFormattedDate(date);
        } catch (Exception e) {
            log.error("Exception occurred in getting date in format: ", e);
        }
        return null;
    }

    public static String getDateInFormat(String timezone, String format) {
        SimpleDateFormat sd = new SimpleDateFormat(format);
        try {
            Date date = sd.parse(timezone);
            return getFormattedDate(date);
        } catch (Exception e) {
            log.error("Exception occurred in getting date in format: ", e);
        }
        return null;
    }

    // method to return a Date object from string in format "yyyy-MM-dd HH:mm:ss"
    public static Date getDateTimestampFromString(String timestamp) {
        SimpleDateFormat dateForm = new SimpleDateFormat(yyyy_MM_dd_HH_mm_ss);
        try {
            return dateForm.parse(timestamp);
        } catch (ParseException e) {
            log.error("Exception occurred in getting date timestamp from string: ", e);
        }
        return new Date();
    }

    public static Date getDateFromISTDateString(String timestamp) {
        SimpleDateFormat dateForm = new SimpleDateFormat(yyyy_MM_dd_HH_mm_ss);
        dateForm.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        try {
            return dateForm.parse(timestamp);
        } catch (ParseException e) {
            log.error("Exception occurred in getting date from IST date string: ", e);
        }
        return new Date();
    }

    // method to return a Date object from string in format "yyyy-MM-dd HH:mm:ss"

    /**
     * Epoch string (milliseconds) to Date
     *
     * @param epochString: Epoch timestamp string in Milliseconds. e.g.: 1642528634000
     * @return: Date Object
     */
    public static Date getDateFromEpochStringMS(String epochString) {
        long timestamp = Long.parseLong(epochString);
        return new Date(timestamp);
    }

    public static Date getNextMonthStartingDate() {
        Calendar today = Calendar.getInstance();
        Calendar next = Calendar.getInstance();
        next.clear();
        next.set(YEAR, today.get(YEAR));
        next.set(MONTH, today.get(MONTH) + 1);
        next.set(DAY_OF_MONTH, 1);
        return next.getTime();
    }

    public static Date getNextDateByDayOfMonth(int dayOfMonth, int skipDays) {
        Calendar today = Calendar.getInstance();
        Calendar next = Calendar.getInstance();
        next.clear();
        if (dayOfMonth > today.get(DAY_OF_MONTH) + skipDays % 30) {
            next.set(YEAR, today.get(YEAR));
            next.set(MONTH, today.get(MONTH) + (skipDays / 30));
            next.set(DAY_OF_MONTH, dayOfMonth);
        } else {
            next.set(YEAR, today.get(YEAR));
            next.set(MONTH, today.get(MONTH) + 1 + (skipDays / 30));
            next.set(DAY_OF_MONTH, dayOfMonth);
        }

        // Exception for Feb set last day of month
        if (next.get(DAY_OF_MONTH) != dayOfMonth) {
            next.set(MONTH, next.get(MONTH) - 1);
            next.set(DAY_OF_MONTH, next.getActualMaximum(Calendar.DAY_OF_MONTH));
        }

        return next.getTime();
    }

    /**
     * @param dayOfWeek 1 => Sunday, 7 => Saturday
     * @return
     */
    public static Date getNextDateByDayOfWeek(int dayOfWeek, int skipDays) {
        Calendar today = Calendar.getInstance();
        Calendar next = Calendar.getInstance();
        next.clear();
        if (dayOfWeek > today.get(DAY_OF_WEEK) + skipDays % 7) {
            next.set(YEAR, today.get(YEAR));
            next.set(MONTH, today.get(MONTH));
            next.set(
                    DAY_OF_MONTH,
                    today.get(DAY_OF_MONTH)
                            + 7 * (skipDays / 7)
                            + dayOfWeek
                            - today.get(DAY_OF_WEEK));
        } else {
            next.set(YEAR, today.get(YEAR));
            next.set(MONTH, today.get(MONTH));
            next.set(
                    DAY_OF_MONTH,
                    today.get(DAY_OF_MONTH)
                            + 7 * (skipDays / 7)
                            + 7
                            + dayOfWeek
                            - today.get(DAY_OF_WEEK));
        }
        return next.getTime();
    }

    /**
     * Get day of week in IST timezone
     *
     * @return
     */
    public static int getDayOfWeekTodayIST() {
        Calendar c = Calendar.getInstance();
        c.setTime(getIstTimeNow());
        return c.get(DAY_OF_WEEK);
    }

    /**
     * Get day of week in IST timezone
     *
     * @return
     */
    public static int getDayOfWeekTodayISTStartingMonday() {
        Calendar c = Calendar.getInstance();
        c.setTime(getIstTimeNow());
        return (c.get(DAY_OF_WEEK) + 5) % 7;
    }

    /**
     * Get day name from the week days in IST timezone
     *
     * @return
     */
    public static String getWeekDayNameTodayIST() {
        Calendar c = Calendar.getInstance();
        c.setTime(getIstTimeNow());
        return WEEK_DAYS.get(c.get(DAY_OF_WEEK) - 1);
    }

    /**
     * Get day of month in IST timezone
     *
     * @return
     */
    public static int getDayOfMonthTodayIST() {
        Calendar c = Calendar.getInstance();
        c.setTime(getIstTimeNow());
        return c.get(DAY_OF_MONTH);
    }

    /** @return Date */
    public static Date getTomorrowsDateForSips() {
        Calendar today = Calendar.getInstance();
        Calendar next = Calendar.getInstance();
        next.clear();
        next.set(YEAR, today.get(YEAR));
        next.set(MONTH, today.get(MONTH));
        next.set(DAY_OF_MONTH, today.get(DAY_OF_MONTH) + 1);
        return next.getTime();
    }

    /** @return Date */
    public static Date getNthDayFutureDateForSips(int numberOfDays) {
        Calendar today = Calendar.getInstance();
        Calendar next = Calendar.getInstance();
        next.clear();
        next.set(YEAR, today.get(YEAR));
        next.set(MONTH, today.get(MONTH));
        next.set(DAY_OF_MONTH, today.get(DAY_OF_MONTH) + numberOfDays);
        return next.getTime();
    }

    /** @return Day of week using IST time 1 => Sunday, 7=> Saturday */
    public static int getDayOfWeekForNthDayInFuture(int numberOfDays) {
        Calendar today = Calendar.getInstance();
        today.setTime(getIstTimeNow());
        Calendar next = Calendar.getInstance();
        next.clear();
        next.set(YEAR, today.get(YEAR));
        next.set(MONTH, today.get(MONTH));
        next.set(DAY_OF_MONTH, today.get(DAY_OF_MONTH) + numberOfDays);
        return next.get(DAY_OF_WEEK);
    }

    /** @return Day of month using IST time 1 to 31 */
    public static int getDayOfMonthForNthDayInFuture(int numberOfDays) {
        Calendar today = Calendar.getInstance();
        today.setTime(getIstTimeNow());
        Calendar next = Calendar.getInstance();
        next.clear();
        next.set(YEAR, today.get(YEAR));
        next.set(MONTH, today.get(MONTH));
        next.set(DAY_OF_MONTH, today.get(DAY_OF_MONTH) + numberOfDays);
        return next.get(DAY_OF_MONTH);
    }

    public static Date getIstTimeNow() {
        // get date and add 5hr 30min
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR_OF_DAY, 5);
        c.add(Calendar.MINUTE, 30);
        return c.getTime();
    }

    public static Date convertDateToIst(Date date) {
        // add 5hr 30min to input date
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR_OF_DAY, 5);
        c.add(Calendar.MINUTE, 30);
        return c.getTime();
    }

    // method to get date today for tz aware logic
    public static Date getDateTodayIST() {
        // get date and add 5hr 30min
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR_OF_DAY, 5);
        c.add(Calendar.MINUTE, 30);
        Date istTimestamp = c.getTime();

        // fetch the start of the day
        Calendar c2 = Calendar.getInstance();
        c2.setTime(istTimestamp);
        c2.set(Calendar.HOUR_OF_DAY, 0);
        c2.set(Calendar.MINUTE, 0);
        c2.set(Calendar.SECOND, 0);
        c2.set(Calendar.MILLISECOND, 0);
        // and get that as a Date
        Date istDate = c2.getTime();

        // substract 5:30 to convert it to utc
        Calendar c3 = Calendar.getInstance();
        c3.setTime(istDate);
        c3.add(Calendar.HOUR_OF_DAY, -5);
        c3.add(Calendar.MINUTE, -30);
        // returns Date object with time zone adjustment
        return c3.getTime();
    }

    /**
     * get end of today in ist
     *
     * @return
     */
    public static Date getEndOfTodayIST() {
        // get date and add 5hr 30min
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR_OF_DAY, 5);
        c.add(Calendar.MINUTE, 30);
        Date istTimestamp = c.getTime();

        // fetch the start of the day
        Calendar c2 = Calendar.getInstance();
        c2.setTime(istTimestamp);
        c2.set(Calendar.HOUR_OF_DAY, 23);
        c2.set(Calendar.MINUTE, 59);
        c2.set(Calendar.SECOND, 0);
        c2.set(Calendar.MILLISECOND, 0);
        // and get that as a Date
        Date istDate = c2.getTime();

        // substract 5:30 to convert it to utc
        Calendar c3 = Calendar.getInstance();
        c3.setTime(istDate);
        c3.add(Calendar.HOUR_OF_DAY, -5);
        c3.add(Calendar.MINUTE, -30);
        // returns Date object with time zone adjustment
        return c3.getTime();
    }

    /**
     * set calendar to start of today and get that as a date
     *
     * @return
     */
    public static Date getStartOfDay() {
        Calendar c = Calendar.getInstance();
        // set the calendar to start of today
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * set calendar to start of input date
     *
     * @param date
     * @return
     */
    public static Date getStartOfSpecificDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // set the calendar to start of today
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * set calendar to start of input date
     *
     * @param date
     * @return
     */
    public static Date getStartOfSpecificISTDayInUTC(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // set the calendar to start of today
        c.setTime(addHours(c.getTime(), 5));
        c.setTime(addMinutes(c.getTime(), 30));
        c.setTime(addDays(c.getTime(), -1));

        c.set(Calendar.HOUR_OF_DAY, 18);
        c.set(Calendar.MINUTE, 30);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Date getOlderDateIST(int numberOfDaysOlder) {
        Date todayIst = getDateTodayIST();
        return new Date(todayIst.getTime() - (long) numberOfDaysOlder * 24 * 60 * 60 * 1000);
    }

    public static Date getOlderDate(int numberOfDaysOlder) {
        Date today = new Date();
        Date returnDate =
                new Date(today.getTime() - (long) numberOfDaysOlder * 24 * 60 * 60 * 1000);
        LocalDate localDate = returnDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDateTime startOfDay = localDate.atStartOfDay();
        return new Date(Timestamp.valueOf(startOfDay).getTime());
    }

    public static Date addHours(Date date, int numberOfHours) {
        return new Date(date.getTime() + (long) numberOfHours * 60 * 60 * 1000);
    }

    /**
     * being used only in staging to find end date of weekly challenge on passing start date when
     * challenge length is set to minutes for testing
     *
     * @param date
     * @param numberOfMinutes
     * @return
     */
    public static Date addMinutes(Date date, int numberOfMinutes) {
        return new Date(date.getTime() + (long) numberOfMinutes * 60 * 1000);
    }

    /**
     * add or subtract input number of days to given date and return a Date object
     *
     * @param date
     * @param numberOfDays
     * @return
     */
    public static Date addDays(Date date, int numberOfDays) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(DATE, numberOfDays);
        return c.getTime();
    }

    /** Subtract hours from current time stamp. - relative diff: timezone awareness not required */
    public static Date getOlderDateByHours(int numberOfHoursOlder) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR, cal.get(Calendar.HOUR) - numberOfHoursOlder);
        return cal.getTime();
    }

    // TODO : explore how to reuse Datefilter
    //    public static Date constructDateFromDateFilter(DateFilter dateFilter){
    //        Calendar cal = Calendar.getInstance();
    //        cal.set(Calendar.YEAR, dateFilter.getStartYear());
    //        cal.set(Calendar.MONTH, dateFilter.getStartMonth());
    //        cal.set(Calendar.DAY_OF_MONTH, dateFilter.getStartDay());
    //        Date startDate = cal.getTime();
    //        cal.set(Calendar.YEAR, dateFilter.getEndYear());
    //        cal.set(Calendar.MONTH, dateFilter.getEndMonth());
    //        cal.set(Calendar.DAY_OF_MONTH, dateFilter.getEndDay());
    //        Date endDate = cal.getTime();
    //    }

    public static Date getISTDateFromString(String date) {
        Date startDate = null;
        if (date != null) {
            startDate = DateUtil.getTimeZoneDate(date);
        }
        return startDate != null ? startDate : getDateInIst(new Date());
    }

    public static Date getDateInIst(Date date) {
        //        return new Date(date.getTime() + (TimeUnit.HOURS.toMillis(5) +
        // TimeUnit.MINUTES.toMillis(30)));
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        try {
            return formatter.parse(formatter.format(date));
        } catch (ParseException e) {
            log.error("Exception occurred in getting date in Ist: ", e);
        }
        return new Date();
    }

    public static Date getTimeZoneDate(String date) {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            log.error("ParseException occurred in getting TimeZoneDate: ", e);
        }
        return null;
    }

    /**
     * gets difference in date between two days starting with one
     *
     * @param endDate
     * @param startDate
     * @return
     */
    public static long getDiffInDays(Date endDate, Date startDate) {
        LocalDate startTime = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endTime = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return ChronoUnit.DAYS.between(startTime, endTime) + 1;
    }

    /**
     * gets difference in date between two days starting with zero
     *
     * @param endDate
     * @param startDate
     * @return
     */
    public static long getDiffInDaysZeroIndexed(Date endDate, Date startDate) {
        LocalDate startTime = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endTime = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return ChronoUnit.DAYS.between(startTime, endTime);
    }

    /**
     * If start and end date are equal this function outputs 1 it check in ist date
     *
     * @param endDate
     * @param startDate
     * @return
     */
    public static long getDiffInDaysZeroIndexedInIst(Date endDate, Date startDate) {
        LocalDate startTime =
                startDate
                        .toInstant()
                        .atZone(TimeZone.getTimeZone("Asia/Kolkata").toZoneId())
                        .toLocalDate();
        LocalDate endTime =
                endDate.toInstant()
                        .atZone(TimeZone.getTimeZone("Asia/Kolkata").toZoneId())
                        .toLocalDate();
        return ChronoUnit.DAYS.between(startTime, endTime);
    }

    /**
     * gets the difference between two dates in months if diff between date is 15 days then it will
     * return 0 there is another function which return 1 in this case
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static long getDiffInMonthsZeroIndexed(Date endDate, Date startDate) {
        LocalDate startTime = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endTime = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return ChronoUnit.MONTHS.between(startTime, endTime);
    }

    /**
     * Get difference between two dates in minutes
     *
     * @param endDate
     * @param startDate
     * @return
     */
    public static long getDiffInMinutes(Date endDate, Date startDate) {
        try {
            LocalDateTime startTime =
                    startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime endTime =
                    endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            return ChronoUnit.MINUTES.between(startTime, endTime);
        } catch (Exception e) {
            log.error("Error getting difference in minutes", e);
        }
        return 0;
    }

    public static long getDiffInHours(Date endDate, Date startDate) {
        try {
            LocalDateTime startTime =
                    startDate
                            .toInstant()
                            .atZone(TimeZone.getTimeZone("Asia/Kolkata").toZoneId())
                            .toLocalDateTime();
            LocalDateTime endTime =
                    endDate.toInstant()
                            .atZone(TimeZone.getTimeZone("Asia/Kolkata").toZoneId())
                            .toLocalDateTime();
            return ChronoUnit.HOURS.between(startTime, endTime) + 1;
        } catch (Exception e) {

        }
        return 0;
    }

    /**
     * checks if start date is one before of endDate or not
     *
     * @return
     */
    public static boolean isOneDayBefore(Date startDate, Date endDate) {
        return startDate.before(new Date(endDate.getTime() - oneDayTimeInMillis));
    }

    /**
     * Returns true if both the {@link Date}s are not null and are in the same day in IST
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDayInIST(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        String formattedD1 = DateUtil.getFormattedDate(date1, "yyyy-MM-dd");
        String formattedD2 = DateUtil.getFormattedDate(date2, "yyyy-MM-dd");
        return formattedD1.equals(formattedD2);
    }

    /**
     * get midnight of date in utc
     *
     * @return
     */
    public static Date getDateStart(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // set the calendar to start of today
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * get midnight of date in IST
     *
     * @return
     */
    public static Date getDateISTStart(Date date) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Asia/Kolkata"));
        c.setTime(date);
        // set the calendar to start of today
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * add or subtract input number of months to given date and return a Date object
     *
     * @param date
     * @param numberOfMonths
     * @return
     */
    public static Date addMonths(Date date, int numberOfMonths) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(MONTH, numberOfMonths);
        return c.getTime();
    }

    /**
     * this will give the date in below format EX : 25th Aug'23
     *
     * @param givenDate
     * @return
     */
    public static String getDateInDayOfMonthWithSuffixMMMYYFormat(Date givenDate) {
        SimpleDateFormat df = new SimpleDateFormat("d");
        String formattedDate = df.format(givenDate);
        if (formattedDate.endsWith("1") && !formattedDate.endsWith("11")) {
            df = new SimpleDateFormat("dd'st' MMM''yy");
        } else if (formattedDate.endsWith("2") && !formattedDate.endsWith("12")) {
            df = new SimpleDateFormat("dd'nd' MMM''yy");
        } else {
            df = new SimpleDateFormat("dd'th' MMM''yy");
        }
        formattedDate = df.format(givenDate);
        return formattedDate;
    }

    /**
     * this function will give you the date in the below format EX : 25th Feb,2023
     *
     * @param givenDate
     * @return
     */
    public static String getDateInFormat(Date givenDate) {
        SimpleDateFormat df = new SimpleDateFormat("d");
        String formattedDate = df.format(givenDate);
        if (formattedDate.endsWith("1") && !formattedDate.endsWith("11")) {
            df = new SimpleDateFormat("dd'st' MMM', 'yyyy");
        } else if (formattedDate.endsWith("2") && !formattedDate.endsWith("12")) {
            df = new SimpleDateFormat("dd'nd' MMM', 'yyyy");
        } else {
            df = new SimpleDateFormat("dd'th' MMM', 'yyyy");
        }
        formattedDate = df.format(givenDate);
        return formattedDate;
    }

    /**
     * this function will give you the date in the below format EX : 25th Feb,2023
     *
     * @param givenDate
     * @return
     */
    public static String getDateInFormatIst(Date givenDate) {
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
        df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        return df.format(givenDate);
    }

    /**
     * this function is to check whether data is past or not
     *
     * @param date
     * @param dateFormat
     * @return
     */
    public static boolean isDatePast(String date, String dateFormat) {
        LocalDate localDate = LocalDate.now(ZoneId.systemDefault());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateFormat);
        LocalDate inputDate = LocalDate.parse(date, dtf);
        return inputDate.isBefore(localDate);
    }

    /**
     * Get Date in the below format as shown format
     *
     * <p>EX : 8 Feb’23 | 3:43pm
     *
     * @param date
     * @return
     */
    public static String getFormattedDateStringWithTime(Date date) {
        try {
            DateFormat formatter = new SimpleDateFormat("dd MMM ''yy '|' hh:mma");
            formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
            String dateString = formatter.format(date);
            return dateString.replace("AM", "am").replace("PM", "pm");
        } catch (Exception e) {
            log.error("Error occurred in formatting");
        }
        return null;
    }

    /**
     * Get Date in the below format as shown format
     *
     * <p>EX : 8 Feb’23 , 3:43pm
     *
     * @param date
     * @return
     */
    public static String getFormattedDateStringComaTime(Date date) {
        try {
            DateFormat formatter = new SimpleDateFormat("dd MMM ''yy '|' hh:mma");
            formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
            String dateString = formatter.format(date);
            return dateString.replace("AM", "am").replace("PM", "pm");
        } catch (Exception e) {
            log.error("Error occurred in formatting");
        }
        return null;
    }

    /**
     * Get Date in the below format as shown format
     *
     * <p>EX : 8 Feb’23, 3:43 PM
     *
     * @param date
     * @return
     */
    public static String getSpecificFormattedDateStringComaTime(Date date) {
        try {
            DateFormat formatter = new SimpleDateFormat("dd MMM ''yy',' hh:mm a");
            formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
            String dateString = formatter.format(date);
            return dateString;
        } catch (Exception e) {
            log.error("Error occurred in formatting");
        }
        return null;
    }

    /**
     * Get Date in the below format as shown format
     *
     * <p>EX : 8 Feb’23
     *
     * @param date
     * @return
     */
    public static String getFormattedOnlyDateString(Date date) {
        try {
            DateFormat formatter = new SimpleDateFormat("dd MMM ''yy");
            formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
            String dateString = formatter.format(date);
            return dateString;
        } catch (Exception e) {
            log.error("Error occurred in formatting");
        }
        return null;
    }

    /**
     * add date in the below Format EX : 25th April 2023, 06:39AM
     *
     * @param date
     * @return
     */
    public static String getFullDateWithTime(Date date) {
        return getFullDateWithTime(date, null);
    }

    /**
     * add date in the below Format EX : 25th April 2023, 06:39AM
     *
     * @param date
     * @return
     */
    public static String getFullDateWithTime(Date date, String zone) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        if (zone != null && StringUtils.isNotBlank(zone)) {
            zoneId = ZoneId.of(zone);
        }
        ZonedDateTime zonedDateTime = instant.atZone(zoneId);
        LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "d'"
                                + getDayOfMonthSuffix(localDateTime.getDayOfMonth())
                                + "' MMMM yyyy, hh:mma");
        return localDateTime.format(formatter);
    }

    /**
     * get end of today in ist time
     *
     * @return
     */
    public static Date getEndOfTodayInIST() {
        // get date and add 5hr 30min
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR_OF_DAY, 5);
        c.add(Calendar.MINUTE, 30);
        Date istTimestamp = c.getTime();

        // fetch the start of the day
        Calendar c2 = Calendar.getInstance();
        c2.setTime(istTimestamp);
        c2.set(Calendar.HOUR_OF_DAY, 23);
        c2.set(Calendar.MINUTE, 59);
        c2.set(Calendar.SECOND, 0);
        c2.set(Calendar.MILLISECOND, 0);
        // and get that as a Date
        return c2.getTime();
    }

    /**
     * Get date and month in following format: 25th April
     *
     * @param date
     * @return
     */
    public static String getDateAndMonth(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = instant.atZone(zoneId);
        LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "d'" + getDayOfMonthSuffix(localDateTime.getDayOfMonth()) + "' MMMM");
        return localDateTime.format(formatter);
    }

    /**
     * Add suffix for days
     *
     * @param day
     * @return
     */
    public static String getDayOfMonthSuffix(final int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        switch (day % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    /**
     * get date in below format
     *
     * <p>EX : Feb'24
     *
     * @param date
     * @return
     */
    public static String getFormattedDateWithMonthAndYear(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM ''yy");
        return formatter.format(date);
    }

    /**
     * gets the starting time of current week
     *
     * @return Date
     */
    public static Date getStartOfThisWeek() {
        ZonedDateTime input = ZonedDateTime.now();
        return getStartOfSpecificDay(Date.from(input.with(DayOfWeek.MONDAY).toInstant()));
    }

    /**
     * gets the ending time of current week
     *
     * @return Date
     */
    public static Date getEndTimeOfThisWeek() {
        ZonedDateTime input = ZonedDateTime.now();
        return getStartOfSpecificDay(
                Date.from(input.plusWeeks(1).with(DayOfWeek.MONDAY).toInstant()));
    }

    /**
     * gets the starting time of current week
     *
     * @return Date
     */
    public static Date getStartOfThisWeekInIST() {
        ZonedDateTime input = ZonedDateTime.now(TimeZone.getTimeZone("Asia/Kolkata").toZoneId());
        return getStartOfSpecificISTDayInUTC(Date.from(input.with(DayOfWeek.MONDAY).toInstant()));
    }

    /**
     * gets the starting time of last week
     *
     * @return Date
     */
    public static Date getStartOfLastWeekInIST() {
        ZonedDateTime input = ZonedDateTime.now(TimeZone.getTimeZone("Asia/Kolkata").toZoneId());
        return getStartOfSpecificISTDayInUTC(
                Date.from(input.minusWeeks(1).with(DayOfWeek.MONDAY).toInstant()));
    }

    /**
     * gets the starting time of current month
     *
     * @return Date
     */
    public static Date getStartOfThisMonthInIST() {
        ZoneId istZone = ZoneId.of("Asia/Kolkata");

        LocalDateTime currentDateTimeInIST = LocalDateTime.now(istZone);

        LocalDateTime startDateTimeOfMonthInIST =
                currentDateTimeInIST.withDayOfMonth(1).with(LocalTime.MIDNIGHT);

        return Date.from(startDateTimeOfMonthInIST.atZone(istZone).toInstant());
    }

    /**
     * gets the starting time of last month
     *
     * @return Date
     */
    public static Date getStartOfLastMonthInIST() {
        ZoneId istZone = ZoneId.of("Asia/Kolkata");

        LocalDateTime currentDateTimeInIST = LocalDateTime.now(istZone);

        LocalDateTime startDateTimeOfMonthInIST =
                currentDateTimeInIST.minusMonths(1).withDayOfMonth(1).with(LocalTime.MIDNIGHT);

        return Date.from(startDateTimeOfMonthInIST.atZone(istZone).toInstant());
    }

    public static String getDateAsString(Date createdAt) {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        return formatter.format(createdAt);
    }

    public static long getDiffInMonths(Date startDate, Date endDate) {
        LocalDate startTime = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endTime = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return ChronoUnit.MONTHS.between(startTime, endTime) + 1;
    }

    /**
     * Get last date of month based on the date passed
     *
     * @param date - input date
     * @return Date
     */
    public static Date getLastDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        cal.setTime(date);
        int lastDateOfMonth = cal.getActualMaximum(Calendar.DATE);
        cal.set(Calendar.DATE, lastDateOfMonth);
        cal.set(HOUR, 11);
        cal.set(MINUTE, 59);
        cal.set(SECOND, 59);
        cal.set(Calendar.AM_PM, Calendar.PM);
        return cal.getTime();
    }

    /**
     * checks if input date is one week before current time
     *
     * @return
     */
    public static boolean isOneWeekBefore(Date inputDate) {
        return inputDate.before(
                new Date(new Date().getTime() - TimeConstants.ONE_WEEK_IN_MILLISECONDS));
    }

    /**
     * get no of days in between two dates
     *
     * @param expiryDate
     * @param todaysDate
     * @return
     */
    public static long getNoOfDaysInBetweenDates(Date expiryDate, Date todaysDate) {
        return (DateUtil.getDiffInDays(expiryDate, todaysDate) < 0)
                ? 0
                : DateUtil.getDiffInDays(expiryDate, todaysDate);
    }

    /**
     * pass expiry date and check if the date has expired or not by returning a boolean
     *
     * @param expiryDate
     * @return
     */
    public static boolean checkIfDateExpired(Date expiryDate) {
        SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");
        // Use format method on SimpleDateFormat
        String formattedDateStr = ymdFormat.format(expiryDate);
        return DateUtil.isDatePast(formattedDateStr, "yyyy-MM-dd");
    }

    /**
     * check if input date lies between the start date and end date
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static boolean isDateBetween(Date inputDate, Date startDate, Date endDate) {
        return (inputDate.after(startDate) && inputDate.before(endDate));
    }

    /**
     * Convert date time string to date object.
     *
     * @param str
     * @return
     */
    public static Date convertDateTimeStringToDateObj(String str) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
            return dateFormat.parse(str);
        } catch (Exception e) {
            log.error("Exception while converting string to date object: ", e);
            return null;
        }
    }

    /**
     * Function to check whether the provided date is within the last X days or not. This can be
     * modified to check for weeks, months and years as well.
     *
     * @param date Date
     * @param limit Day limit
     * @return true/false
     */
    public static boolean isDateWithinLimit(Date date, int limit) {
        if (Objects.nonNull(date) && Objects.nonNull(limit)) {
            LocalDate targetDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate dayLimit = LocalDate.now().minusDays(limit);
            return !targetDate.isBefore(dayLimit) && !targetDate.isAfter(LocalDate.now());
        }
        return false;
    }

    /**
     * returns the day of the month for current date
     *
     * @return
     */
    public static int getCurrentDateDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Find difference in current sdate and custom date.
     *
     * @param customeDate
     * @return
     */
    public static int differenceInDaysFromCustomDate(Date customeDate) {
        return Math.toIntExact(
                ChronoUnit.DAYS.between(
                        LocalDate.now(),
                        customeDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
    }

    /**
     * Gets end of specific day in IST
     *
     * @param date
     * @return
     */
    public static Date getEndOfSpecificDayIST(Date date) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Asia/Kolkata"));
        c.setTime(date);

        // set the calendar to end of day
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 99);
        return c.getTime();
    }

    /**
     * Gets start of specific day in IST
     *
     * @param date
     * @return
     */
    public static Date getStartOfSpecificDayIST(Date date) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Asia/Kolkata"));
        c.setTime(date);

        // set the calendar to start of day
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * get LocalDate from date
     *
     * @param date
     * @return
     */
    public static LocalDate getLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * get only time in IST
     *
     * @param date
     * @return
     */
    public static String getOnlyTimeIST(Date date) {
        try {
            DateFormat formatter = new SimpleDateFormat("hh:mma");
            String dateString = formatter.format(date);
            return dateString.replace("AM", " AM").replace("PM", " PM");
        } catch (Exception e) {
            log.error("Error occurred in formatting");
        }
        return null;
    }

    public static Date getISTDate(Date date) {
        TimeZone istTimeZone = TimeZone.getTimeZone("Asia/Kolkata");
        long utcTime = date.getTime();
        long istTime = utcTime + istTimeZone.getRawOffset();
        Date istDate = new Date(istTime);
        return istDate;
    }

    /**
     * Gets the date in MM-dd-yyyy format
     *
     * @param smsDate
     * @return
     */
    public static String getDateAsStringInMmDdYY(String smsDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss[.SSSSSS]");
        LocalDate localDate = LocalDate.parse(smsDate, formatter);
        String date = localDate.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
        return date;
    }

    /**
     * Convert hours into milliseconds.
     *
     * @param hours No. of hours.
     * @return
     */
    public long convertHoursToMilliSeconds(int hours) {
        return hours * 60 * 60 * 1000;
    }

    /**
     * gets hour of date in Ist
     *
     * @param date
     * @return
     */
    public static int getHourOfDate(Date date) {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Asia/Kolkata"));
        c.setTime(date);
        return c.get(HOUR_OF_DAY);
    }

    /**
     * Check if date object if after custom date string. This method is specific to yyyy-mm-dd
     *
     * @param yyyyMmDd Date format string
     * @param date Date object
     * @param identifier Identifier
     * @return true/false
     */
    public boolean isDateAfter(String yyyyMmDd, Date date, String identifier) {
        try {
            return date.after(new SimpleDateFormat("yyyy-MM-dd").parse(yyyyMmDd));
        } catch (Exception e) {
            log.error(
                    MessageFormat.format(
                            "Exception while checking if date is after yyyy-mm-dd string for"
                                    + " identifier: {0}! Returning false.",
                            identifier));
            return false;
        }
    }

    /**
     * returns date object from objectId
     *
     * @param objectId
     * @return
     */
    public static Date convertToDateFromObjectId(String objectId) {
        return new Date(convertToTimestampFromObjectId(objectId));
    }

    /**
     * returns timestamp from objectId
     *
     * @param objectId
     * @return
     */
    public static long convertToTimestampFromObjectId(String objectId) {
        return Long.parseLong(objectId.substring(0, 8), 16) * 1000;
    }
}
