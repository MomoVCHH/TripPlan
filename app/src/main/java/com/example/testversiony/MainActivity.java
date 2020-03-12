package com.example.testversiony;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
//the two below rows is about place api autocompletion
import android.widget.AutoCompleteTextView;
import android.widget.TimePicker;
import android.widget.Toast;
//import androidx.room.Database;
import java.util.Calendar;
import android.database.sqlite.SQLiteException;
import java.text.SimpleDateFormat;

import com.example.testversiony.PlaceAutoSuggestAdapter;




/*Replace YOUR_API_KEY in String url with your API KEY obtained by registering your application with google.

 */
public class MainActivity extends AppCompatActivity implements GeoTask.Geo {
    EditText setDateDeparturetime, setDate, departure_time;
    Button btn_get, bcheck;
    String str_from, str_to;
    TextView tv_result1, tv_result2, tv_result3, tv_result4;
    AutoCompleteTextView autoCompleteTextView, edttxt_from, edttxt_to;
    PlaceAutoSuggestAdapter placeAutoSuggestAdapter;
    //EditText editName,editSurname,editMarks;

    Double min;
    int dist;

    //Database SQLite;
    ForSQLite myDb;

    int cyear, cmonth, cdayOfMonth, chourOfDay, cminute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();



        // autoCompleteTextView.setAdapter(new PlaceAutoSuggestAdapter(MainActivity.this,android.R.layout.simple_list_item_1));
        edttxt_from.setAdapter(new PlaceAutoSuggestAdapter(MainActivity.this, android.R.layout.simple_list_item_1));
        edttxt_to.setAdapter(new PlaceAutoSuggestAdapter(MainActivity.this, android.R.layout.simple_list_item_1));
        myDb = new ForSQLite(this);

        btn_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_from = edttxt_from.getText().toString();
                str_to = edttxt_to.getText().toString();
                AddData();
                String url = "https://maps.googleapis.com/maps/api/distancematrix/json?&units=metric&origins=" + str_from + "&destinations=" + str_to + "&mode=driving&language=de-DE&avoid=tolls&key=AIzaSyCtzDB3LY-UsAPs1DodfYAqxBuNNgEW6E0";
                new GeoTask(MainActivity.this).execute(url);

            }
        });


        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDateDialog(setDate);

            }
        });

        departure_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               showTimeDialog(departure_time);

            }
        });

    }

    public void AddData() {
        btn_get.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        boolean isInserted = myDb.inserData(str_to,
                                str_to);
                        if (isInserted == true) {
                            Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Data Not Inserted", Toast.LENGTH_LONG).show();
                        }

                    }
                }
        );

    }

    // the below method is about travel time and distance
    @Override
    public void setDouble(String result) {
        String res[] = result.split(",");
        min = Double.parseDouble(res[0]) / 60;
        dist = Integer.parseInt(res[1]) / 1000;
        tv_result1.setText("Duration= " + (int) (min / 60) + " hr " + (int) (min % 60) + " mins");
        tv_result2.setText("Distance= " + dist + " kilometers");
    }

    // the below method is about arraivel time (calculation time) arrival time calculator
    public void arraivelTime(String timeResult) {
        String res2[] = timeResult.split(",");
        min = Double.parseDouble(res2[0]) / 60;
        /*
        if()

        tv_result3.setText("Arrival time= " (int)());
        */
    }


    public void initialize() {
        departure_time = (EditText) findViewById(R.id.departure_time);
        setDate = (EditText) findViewById(R.id.setDate);
        // setDateDeparturetime = (EditText) findViewById(R.id.setdateandtime);
        edttxt_from = (AutoCompleteTextView) findViewById(R.id.editText_from);
        edttxt_to = (AutoCompleteTextView) findViewById(R.id.editText_to);
        // autoCompleteTextView= (AutoCompleteTextView)findViewById(R.id.auto);
        btn_get = (Button) findViewById(R.id.button_get);
        tv_result1 = (TextView) findViewById(R.id.textView_result1);
        tv_result2 = (TextView) findViewById(R.id.textView_result2);

        // setDateDeparturetime.setInputType(InputType.TYPE_NULL);
        setDate.setInputType(InputType.TYPE_NULL);
        departure_time.setInputType(InputType.TYPE_NULL);

    }

    private void showDateDialog(final EditText setDate) {
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd");
                setDate.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };
        new DatePickerDialog(MainActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void showTimeDialog(final EditText departure_time) {
        final Calendar calendar = Calendar.getInstance();

        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                departure_time.setText(simpleDateFormat.format(calendar.getTime()));

            }
        };
        new TimePickerDialog(MainActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
    }

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, final int year, int month, int dayOfMonth) {
            final Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd");
            setDate.setText(simpleDateFormat.format(calendar.getTime()));
        }
    };
    TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            final Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            departure_time.setText(simpleDateFormat.format(calendar.getTime()));

        }
    };
}










