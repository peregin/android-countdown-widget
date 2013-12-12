package peregin.android.countdown;

import java.util.Calendar;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import peregin.util.DateUtil;

/**
 * Main entry point of the widget.
 * <p/>
 * Version information is stored in the <tt>android:versionName</tt> resource.
 * Version counter is stored in the <tt>android:versionCode</tt> attribute.
 * Same for email and web address as well.
 * <p/>
 * AT RELEASE TIME DON'T FORGET:
 * <ul>
 * <li>1.) build package with latest Android
 * <li>2.) check AndroidManifest.xml (e.g. version number, screens setup, etc.)
 * </ul>
 * <p/>
 * <pre>
 * 1.) to zoom Nexus One or S versions use: -scale 0.75
 * 2.) to rotate emulator: FN+Ctrl+F12
 * </pre>
 * <p/>
 * <code><pre>
 * BUGS/TODOS/FIXMES
 * =================
 * <p/>
 * TODO
 * - show remaining weeks/months
 * - custom colors
 * - holidays
 * - cleanup config at widget remove
 * - about & version dialog - webgui?
 * <p/>
 * ----- release 1.02.00 ------
 * - multi size widgets
 * DONE
 * ----- release 1.01.00 ------
 * - transparency
 * ----- release 1.00.07 ------
 * - adjust the text size of the weekdays only mode (text size is smaller than calendar days mode)
 * - enable selecting either calendar days or workdays or both in the widget
 * - preferences button bar is shown always at the bottom of the screen and the content is scrollable
 * - show version number in info activity
 * - fix for developed year in the info activity
 * ----- release 1.00.06 ------
 * - how it started - scanned picture
 * - new icon, showing week days and progress bar (show why and how is different than the other countdown widgets)
 * - NOPE: add timer for midnight - DST problem? - update on hourly bases 3600000 = 1 hour
 * - more frequent ticking: 21600000 = 6 hours, 86400000 = 1 day
 * - NOPE - embed the widget in the config dialog - not enough space
 * - change the color of the calendar widget
 * - have a default activity that can be opened after install (and describe how to add a widget)
 * - add common libraries (android util)
 * - when target is reached, show 'days since' or 'elapsed', etc.
 * - finish config dialog
 * - scrollable conf layout
 * - when 1 day left change the default message from days->day
 * - config dialog back button - no need - acts like cancel
 * - at first config disable the cancel button
 * - config dialog style as it is in the PhotoUploader
 * - layout with weekdays and progressbar
 * - config entity should read/store data
 * <p/>
 * WEB TUTORIAL
 * - shiny calendar with vector graphics - tutorial
 * - what went strange :
 *   - widget configuration (Intent with Uri)
 *   - preferences are not usable
 *   - built in mechanism for instanceId, preference save/load/DELETE
 * <p/>
 * </pre></code>
 * 
 * @author levi
 */
public abstract class CountdownWidget extends AppWidgetProvider {

    // it is declared in the manifest XML as well
    public static final String FORCE_UPDATE = "peregin.android.countdown.WIDGET_UPDATE";

    private final int layoutId; // specific to size
    private final int backgroundResId;

    protected CountdownWidget(int layoutId, int backgroundResId) {
        this.layoutId = layoutId;
        this.backgroundResId = backgroundResId;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        update(context);
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(FORCE_UPDATE)
                || action.equals(Intent.ACTION_TIMEZONE_CHANGED)
                || action.equals(Intent.ACTION_TIME_CHANGED)) {
            update(context);
        }
        super.onReceive(context, intent);
    }

    private void update(Context context) {
        Log.d("component class", "name = " + this.getClass().getName());
        ComponentName widget = new ComponentName(context, this.getClass());
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(widget);

        for (int widgetId : appWidgetIds) {
            RemoteViews views = updateRemoteViews(context, widgetId);
            appWidgetManager.updateAppWidget(widgetId, views);
        }
    }

    private RemoteViews updateRemoteViews(Context context, int widgetId) {
        Setup setup = Setup.load(context, widgetId);

        long now = Calendar.getInstance().getTimeInMillis();
        int daysLeft = DateUtil.daysBetween(now, setup.targetDate);
        int weekDaysLeft = DateUtil.weekDaysBetween(now, setup.targetDate);
        int daysFromStart = DateUtil.daysBetween(setup.startDate, setup.targetDate);
        Log.d(getClass().getSimpleName(), daysLeft + "/" + weekDaysLeft + ", max=" + daysFromStart);

        RemoteViews views = new RemoteViews(context.getPackageName(), layoutId);

        Bitmap backImage = BackgroundImageAdapter.createColoredBackground(context, setup.backgroundIndex, setup.alpha, backgroundResId);
        views.setImageViewBitmap(R.id.image_back, backImage);

        if (daysLeft > 0) {
            if (setup.showDays.showCalendarDays()) {
                views.setViewVisibility(R.id.text_calendar_days, View.VISIBLE);
                views.setTextViewText(R.id.text_calendar_days, daysLeft + "");
                views.setTextColor(R.id.text_calendar_days, Color.BLACK);
            } else {
                views.setViewVisibility(R.id.text_calendar_days, View.GONE);
            }

            if (setup.showDays.showWeekDays()) {
                views.setViewVisibility(R.id.text_week_days, View.VISIBLE);
                views.setTextViewText(R.id.text_week_days, weekDaysLeft + "");
                views.setTextColor(R.id.text_week_days, Color.rgb(100, 100, 100));
                if (setup.showDays == ShowDaysType.WEEK) {
                    views.setFloat(R.id.text_week_days, "setTextSize", 26);
                } else {
                    views.setFloat(R.id.text_week_days, "setTextSize", 16);
                }
            } else {
                views.setViewVisibility(R.id.text_week_days, View.GONE);
            }

            if (setup.showEventName) {
                views.setTextViewText(R.id.text_footer, setup.eventName);
            } else {
                if (daysLeft == 1) {
                    views.setTextViewText(R.id.text_footer, context.getString(R.string.label_day_left));
                } else {
                    views.setTextViewText(R.id.text_footer, context.getString(R.string.label_days_left));
                }
            }

            if (setup.showProgress) {
                views.setViewVisibility(R.id.layout_progress, View.VISIBLE);
                views.setProgressBar(R.id.progress_days, daysFromStart, daysFromStart - daysLeft, false);
            } else {
                views.setViewVisibility(R.id.layout_progress, View.GONE);
            }
        } else {
            views.setViewVisibility(R.id.text_week_days, View.GONE);
            views.setViewVisibility(R.id.layout_progress, View.GONE);

            // check how many days elapsed since the target date
            views.setViewVisibility(R.id.text_calendar_days, View.VISIBLE);
            int daysSince = DateUtil.daysBetween(setup.targetDate, now);
            views.setTextViewText(R.id.text_calendar_days, daysSince + "");

            if (daysSince > 0) {
                views.setTextColor(R.id.text_calendar_days, Color.GRAY); // in the past
            } else {
                views.setTextColor(R.id.text_calendar_days, Color.RED); // today
            }

            if (setup.showEventName) {
                views.setTextViewText(R.id.text_footer, setup.eventName);
            } else {
                if (daysSince <= 0) {
                    views.setTextViewText(R.id.text_footer, context.getString(R.string.label_today));
                } else if (daysSince == 1) {
                    views.setTextViewText(R.id.text_footer, context.getString(R.string.label_day_since));
                } else {
                    views.setTextViewText(R.id.text_footer, context.getString(R.string.label_days_since));
                }
            }
        }

        // configure the click behavior of the widget
        Intent intent = new Intent(context, CountdownPreferences.class);
        // this causes each widget to have a unique PendingIntent
        Uri data = Uri.withAppendedPath(Uri.parse("peregin://widget/id/"), String.valueOf(widgetId));
        intent.setData(data);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.counter_widget, pendingIntent);

        return views;
    }
}