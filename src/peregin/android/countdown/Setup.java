package peregin.android.countdown;

import android.content.Context;
import android.content.SharedPreferences;

public class Setup {

    public long targetDate;
    public ShowDaysType showDays;
    public boolean showProgress;
    public long startDate; // for showing the progress from the given start date
    public int backgroundIndex;
    public float alpha;
    public boolean showEventName;
    public String eventName;

    static public Setup load(Context context, int widgetId) {
        SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.pref_name), 0);

        long todayInMillis = System.currentTimeMillis();

        Setup setup = new Setup();
        setup.targetDate = prefs.getLong(context.getString(R.string.pref_target_date_key) + widgetId, todayInMillis);
        int index = prefs.getInt(context.getString(R.string.pref_show_days_key) + widgetId, -1);
        if (index == -1) {
            // try to load in the old style mode, when was only a boolean to show or not the weekdays.
            boolean showWeekDays = prefs.getBoolean(context.getString(R.string.pref_show_week_days_key) + widgetId, true);
            index = showWeekDays ? ShowDaysType.BOTH.getIndex() : ShowDaysType.CALENDAR.getIndex();
        }
        setup.showDays = ShowDaysType.fromIndex(index);
        setup.showProgress = prefs.getBoolean(context.getString(R.string.pref_show_progress_key) + widgetId, true);
        setup.startDate = prefs.getLong(context.getString(R.string.pref_start_date_key) + widgetId, todayInMillis);
        setup.backgroundIndex = prefs.getInt(context.getString(R.string.pref_background_index_key) + widgetId,
                BackgroundImageAdapter.DEFAULT_BACKGROUND_INDEX);
        setup.alpha = prefs.getFloat(context.getString(R.string.pref_alpha_key) + widgetId, 1f);
        setup.showEventName = prefs.getBoolean(context.getString(R.string.pref_show_event_name_key) + widgetId, false);
        setup.eventName = prefs.getString(context.getString(R.string.pref_event_name_key) + widgetId, "");

        return setup;
    }

    public void save(Context context, int widgetId) {
        SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.pref_name), 0);
        SharedPreferences.Editor edit = prefs.edit();

        edit.putLong(context.getString(R.string.pref_target_date_key) + widgetId, targetDate);
        edit.putInt(context.getString(R.string.pref_show_days_key) + widgetId, showDays.getIndex());
        edit.putBoolean(context.getString(R.string.pref_show_progress_key) + widgetId, showProgress);
        edit.putLong(context.getString(R.string.pref_start_date_key) + widgetId, startDate);
        edit.putInt(context.getString(R.string.pref_background_index_key) + widgetId, backgroundIndex);
        edit.putFloat(context.getString(R.string.pref_alpha_key) + widgetId, alpha);
        edit.putBoolean(context.getString(R.string.pref_show_event_name_key) + widgetId, showEventName);
        edit.putString(context.getString(R.string.pref_event_name_key) + widgetId, eventName);

        edit.commit();
    }
}
