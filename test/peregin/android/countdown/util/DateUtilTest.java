package peregin.android.countdown.util;

import org.junit.Test;

import java.util.Calendar;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class DateUtilTest {

    // it is a Wednesday
    static private Calendar now = DateUtil.toCalendar("2012-01-25");

    @Test
    public void testIsoDateConverter() {
        Calendar c = DateUtil.toCalendar("2012-01-31");
        assertThat(c.get(Calendar.YEAR), is(2012));
        assertThat(c.get(Calendar.MONTH), is(Calendar.JANUARY));
        assertThat(c.get(Calendar.DAY_OF_MONTH), is(31));
    }

    @Test
    public void testSameDay() {
        testDaysBetween(now, now, 0);
        testWeekDaysBetween(now, now, 0);
    }

    @Test
    public void testOneDay() {
        Calendar tomorrow = (Calendar) now.clone();
        tomorrow.add(Calendar.DAY_OF_MONTH, 1);
        testDaysBetween(now, tomorrow, 1);
        testWeekDaysBetween(now, tomorrow, 1);
    }

    @Test
    public void testDateInThePast() {
        Calendar tomorrow = (Calendar) now.clone();
        tomorrow.add(Calendar.DAY_OF_MONTH, -1);
        testDaysBetween(now, tomorrow, 0);
        testWeekDaysBetween(now, tomorrow, 0);
    }

    @Test
    public void testOneWeek() {
        Calendar tomorrow = (Calendar) now.clone();
        tomorrow.add(Calendar.DAY_OF_MONTH, 7);
        testDaysBetween(now, tomorrow, 7);
        testWeekDaysBetween(now, tomorrow, 5);
    }

    @Test
    public void testWeekDaysFromWednesdayUntilSunday() {
        Calendar sunday = DateUtil.toCalendar("2012-01-29");
        testDaysBetween(now, sunday, 4);
        testWeekDaysBetween(now, sunday, 2);
    }

    @Test
    public void testWeekDaysFromWednesdayUntilSaturday() {
        Calendar sunday = DateUtil.toCalendar("2012-01-28");
        testDaysBetween(now, sunday, 3);
        testWeekDaysBetween(now, sunday, 2);
    }

    @Test
    public void testPeriodWithTwoWeekends() {
        Calendar tuesday = DateUtil.toCalendar("2012-02-07");
        testDaysBetween(now, tuesday, 13);
        testWeekDaysBetween(now, tuesday, 9);
    }

    @Test
    public void testWeekendOnly() {
        Calendar saturday = DateUtil.toCalendar("2012-01-28");
        Calendar sunday = DateUtil.toCalendar("2012-01-29");
        testDaysBetween(saturday, sunday, 1);
        testWeekDaysBetween(saturday, sunday, 0);
    }

    @Test
    public void testTenYears() {
        Calendar tomorrow = (Calendar) now.clone();
        tomorrow.add(Calendar.YEAR, 10);
        testDaysBetween(now, tomorrow, 3653);
        testWeekDaysBetween(now, tomorrow, 2609);
    }

    // 27 - because it's Réka's birthday and it is on the widget icon.
    @Test
    public void test27Days() {
        Calendar twentySevenDays = (Calendar) now.clone();
        twentySevenDays.add(Calendar.DAY_OF_MONTH, 27);
        testDaysBetween(now, twentySevenDays, 27);
        testWeekDaysBetween(now, twentySevenDays, 19);
    }

    private void testDaysBetween(Calendar c1, Calendar c2, int expected) {
        int days = DateUtil.daysBetween(DateUtil.getMillisWithDST(c1), DateUtil.getMillisWithDST(c2));
        assertThat(days, is(expected));
    }

    private void testWeekDaysBetween(Calendar c1, Calendar c2, int expected) {
        int days = DateUtil.weekDaysBetween(DateUtil.getMillisWithDST(c1), DateUtil.getMillisWithDST(c2));
        assertThat(days, is(expected));
    }
}
