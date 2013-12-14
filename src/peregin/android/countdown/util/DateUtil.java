package peregin.android.countdown.util;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtil {

    private static final int MILLISECONDS_IN_DAY = 1000 * 60 * 60 * 24;

    static public int daysBetween(long t1, long t2) {
        if (t1 > t2) {
            return 0;
        }
        Calendar c1 = getNormalizedCalendar(t1);
        Calendar c2 = getNormalizedCalendar(t2);
        return daysBetween(c1, c2);
    }

    static public int daysBetween(Calendar c1, Calendar c2) {
        if (c1.after(c2)) {
            return 0;
        }
        long o1 = getMillisWithDST(c1);
        long o2 = getMillisWithDST(c2);

        long diff = o2 - o1;
        diff /= MILLISECONDS_IN_DAY;
        return (int) diff;
    }

    static public int weekDaysBetween(long t1, long t2) {
        int days = 0;
        Calendar c1 = getNormalizedCalendar(t1);
        Calendar c2 = getNormalizedCalendar(t2);

        // go from c1 forward until the next Sunday
        while ((c1.before(c2) || c1.equals(c2)) && !isSunday(c1)) {
            if (!isWeekend(c1)) {
                days++;
            }
            c1.add(Calendar.DAY_OF_MONTH, 1);
        }
        // go from c2 backward until the previous Sunday
        while (c2.after(c1) && !isSunday(c2)) {
            if (!isWeekend(c2)) {
                days++;
            }
            c2.add(Calendar.DAY_OF_MONTH, -1);
        }
        if (days > 0) {
            days--;
        }
        // calculate the weekdays from Sunday to Sunday, should be multiple of 7
        int sundayToSunday = daysBetween(c1, c2);
        days += sundayToSunday * 5 / 7;
        return days;
    }

    // contains the days only
    static public Calendar getNormalizedCalendar(long t) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(t);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c;
    }

    static public long getMillisWithDST(Calendar c) {
        return c.getTimeInMillis() + c.get(Calendar.DST_OFFSET);
    }

    static public boolean isSaturday(Calendar c) {
        return c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY;
    }

    static public boolean isSunday(Calendar c) {
        return c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
    }

    static public boolean isWeekend(Calendar c) {
        if (isSaturday(c) || isSunday(c)) {
            return true;
        }
        return false;
    }

    static public long toMillis(int year, int month, int day) {
        return getMillisWithDST(toCalendar(year, month, day));
    }

    /**
     * @param date expressed in ISO format (yyyy-mm-dd)
     * @return the time expressed in UTC format
     */
    static public long toMillis(String date) {
        return getMillisWithDST(toCalendar(date));
    }

    static public Calendar toCalendar(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month - 1);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c;
    }

    static public Calendar toCalendar(String date) {
        // this is stupid, use a simple date formatter which validates the date as well
        Pattern isoDatePattern = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})");
        Matcher matcher = isoDatePattern.matcher(date);
        if (matcher.lookingAt()) {
            int year = Integer.parseInt(matcher.group(1));
            int month = Integer.parseInt(matcher.group(2));
            int day = Integer.parseInt(matcher.group(3));
            return toCalendar(year, month, day);
        }
        throw new IllegalArgumentException(date + " is not matching " + isoDatePattern.pattern());
    }

    static public int getYear(long millis) {
        Calendar c = getNormalizedCalendar(millis);
        return c.get(Calendar.YEAR);
    }

    static public int getMonth(long millis) {
        Calendar c = getNormalizedCalendar(millis);
        return c.get(Calendar.MONTH) + 1;
    }

    static public int getDay(long millis) {
        Calendar c = getNormalizedCalendar(millis);
        return c.get(Calendar.DAY_OF_MONTH);
    }
}
