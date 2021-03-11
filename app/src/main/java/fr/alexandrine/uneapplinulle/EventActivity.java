package fr.alexandrine.uneapplinulle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventActivity extends AppCompatActivity {

    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private Spinner eventSpinner;
    private int year, month, day;
    private int selectedYear;
    private String selectedMonth, selectedDay;
    private final DecimalFormat twodigits = new DecimalFormat("00");
    private Map<String, String> events_map;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        dateView = (TextView) findViewById(R.id.dateText);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        selectedYear = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        selectedMonth = String.valueOf(calendar.get(Calendar.MONTH));
        day = calendar.get(Calendar.DAY_OF_MONTH);
        selectedDay = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

        showDate(year, twodigits.format(month+1), twodigits.format(day));

        eventSpinner = findViewById(R.id.event_spinner);

        String[] eventsTitle = this.getResources().getStringArray(R.array.events_array);
        String[] eventsDescription = this.getResources().getStringArray(R.array.events_desc_array);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, eventsTitle);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        eventSpinner.setAdapter(dataAdapter);

        events_map = new HashMap<>();

        for (int i = 0; i < eventsTitle.length; i++) {
            events_map.put(eventsTitle[i], eventsDescription[i]);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, 0);

        }


    }

    private void showDate(int year, String month, String day) {
        selectedDay = day;
        selectedMonth = month;
        selectedYear = year;

        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private final DatePickerDialog.OnDateSetListener myDateListener = (arg0, arg1, arg2, arg3) -> showDate(arg1, twodigits.format(arg2+1),  twodigits.format(arg3));

    public void createEvent(View view)
    {
        long calID = 1;
        long startMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(selectedYear, Integer.parseInt(selectedMonth) - 1, Integer.parseInt(selectedDay));


        startMillis = beginTime.getTimeInMillis();
        String title = eventSpinner.getSelectedItem().toString();
        String desc = events_map.get(title);

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, startMillis);
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.ALL_DAY, 1);
        values.put(CalendarContract.Events.DESCRIPTION, desc);
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "Europe/Paris");
        cr.insert(CalendarContract.Events.CONTENT_URI, values);

        Log.d("EventActivity", values.toString());

        Toast toast = Toast.makeText(getApplicationContext(), R.string.event_added, Toast.LENGTH_LONG);
        toast.show();
    }



}