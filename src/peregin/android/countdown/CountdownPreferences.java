package peregin.android.countdown;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import android.widget.*;
import peregin.util.DateUtil;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class CountdownPreferences extends Activity {

    static private final int START_DATE_DIALOG_ID = 10;

    private int appWidgetId;

    private DatePicker dpTargetDate;
    private Spinner spShowDays;
    private CheckBox cbShowProgress;
    private CheckBox cbShowEventName;
    private EditText editEventName;
    private TextView textProgressStartDate;
    private long progressStartDate;

    private ImageReel reelBack;
    private SeekBar barAlpha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get the appWidgetId of the appWidget being configured
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        Log.d(getClass().getSimpleName(), "appWidgetId=" + appWidgetId);

        // set the result for cancel first
        // if the user cancels, then the appWidget should not appear
        Intent cancelResultValue = new Intent();
        cancelResultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_CANCELED, cancelResultValue);

        // show the user interface of configuration
        setContentView(R.layout.preferences);

        Setup setup = Setup.load(this, appWidgetId);

        // setup the date in the date picker
        Log.d(getClass().getSimpleName(), "load:targetDate=" + new Date(setup.targetDate) + " - " + setup.targetDate);
        dpTargetDate = (DatePicker) findViewById(R.id.pickerTargetDate);
        dpTargetDate.init(DateUtil.getYear(setup.targetDate), DateUtil.getMonth(setup.targetDate) - 1,
                DateUtil.getDay(setup.targetDate), null);

        // calendar or/and week days
        spShowDays = (Spinner) findViewById(R.id.spinnerShowDays);
        spShowDays.setSelection(setup.showDays.getIndex());

        cbShowProgress = (CheckBox) findViewById(R.id.checkBoxShowProgress);
        cbShowProgress.setChecked(setup.showProgress);
        textProgressStartDate = (TextView) findViewById(R.id.textShowProgress);
        progressStartDate = setup.startDate;
        textProgressStartDate.setText(DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault()).format(
                progressStartDate));
        Button btnStartDate = (Button) findViewById(R.id.buttonStartDate);
        btnStartDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialog(START_DATE_DIALOG_ID);
            }
        });

        reelBack = (ImageReel) findViewById(R.id.galleryBack);
        BackgroundImageAdapter bgAdapter = new BackgroundImageAdapter(this);
        reelBack.setAdapter(bgAdapter);
        reelBack.setSelection(setup.backgroundIndex, true);
        reelBack.setSpacing(bgAdapter.dip2pixel(-2));
        reelBack.setAnimationDuration(1000);

        barAlpha = (SeekBar) findViewById(R.id.seekBarAlpha);
        barAlpha.setMax(80);
        float alpha = 100 * (1f - setup.alpha); // between 0-1, 1 not transparent = 0%
        Log.d(getClass().getSimpleName(), "alpha=" + setup.alpha + ", progress="+alpha);
        barAlpha.setProgress((int)alpha);

        // name of the event
        cbShowEventName = (CheckBox) findViewById(R.id.checkBoxEventName);
        cbShowEventName.setChecked(setup.showEventName);
        editEventName = (EditText) findViewById(R.id.editTextEventName);
        editEventName.setText(setup.eventName);

        // the OK button
        Button btnOk = (Button) findViewById(R.id.buttonOk);
        btnOk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Setup setup = new Setup();

                // get the target date
                setup.targetDate = DateUtil.toMillis(dpTargetDate.getYear(), dpTargetDate.getMonth() + 1,
                        dpTargetDate.getDayOfMonth());
                Log.d(getClass().getSimpleName(), "save:targetDate=" + new Date(setup.targetDate) + " - "
                        + setup.targetDate);

                // get the selection index of the days to show
                int index = spShowDays.getSelectedItemPosition();
                if (index == AdapterView.INVALID_POSITION) {
                    index = ShowDaysType.BOTH.getIndex();
                }
                setup.showDays = ShowDaysType.fromIndex(index);

                // progress
                setup.showProgress = cbShowProgress.isChecked();
                setup.startDate = progressStartDate;

                // background image index
                setup.backgroundIndex = reelBack.getSelectedItemPosition();
                if (setup.backgroundIndex == AdapterView.INVALID_POSITION) {
                    setup.backgroundIndex = BackgroundImageAdapter.DEFAULT_BACKGROUND_INDEX;
                }

                // alpha channel for the picture
                int s = barAlpha.getProgress();
                setup.alpha = 1f - (s / 100f);

                // name of the event
                setup.showEventName = cbShowEventName.isChecked();
                setup.eventName = editEventName.getText().toString();

                // persist the settings
                setup.save(CountdownPreferences.this, appWidgetId);

                // change the result to OK
                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                setResult(RESULT_OK, resultValue);

                // broadcast update message to the widget
                sendBroadcast(new Intent(CountdownWidget.FORCE_UPDATE));

                // finish closes activity and sends the OK result
                // the widget will be be placed on the home screen
                finish();
            }
        });

        // cancel button
        Button btnCancel = (Button) findViewById(R.id.buttonCancel);
        btnCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // finish sends the already configured cancel result and closes activity
                finish();
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == START_DATE_DIALOG_ID) {
            DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    progressStartDate = DateUtil.toMillis(year, monthOfYear + 1, dayOfMonth);
                    textProgressStartDate.setText(DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault())
                            .format(progressStartDate));
                }
            };
            return new DatePickerDialog(this, mDateSetListener, DateUtil.getYear(progressStartDate),
                    DateUtil.getMonth(progressStartDate) - 1, DateUtil.getDay(progressStartDate));
        }
        return null;
    }
}
