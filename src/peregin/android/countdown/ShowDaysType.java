package peregin.android.countdown;

public enum ShowDaysType {

    BOTH(0), CALENDAR(1), WEEK(2);

    // it is persisted in the preferences and tells the index in the spinner widget as well
    private int index;

    private ShowDaysType(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    static public ShowDaysType fromIndex(int index) {
        for (ShowDaysType d : values()) {
            if (index == d.getIndex()) {
                return d;
            }
        }
        throw new IllegalArgumentException("unknown index ["+index+"]");
    }

    public boolean showCalendarDays() {
        if (this == BOTH) {
            return true;
        }
        if (this == CALENDAR) {
            return true;
        }
        return false;
    }

    public boolean showWeekDays() {
        if (this == BOTH) {
            return true;
        }
        if (this == WEEK) {
            return true;
        }
        return false;
    }

}