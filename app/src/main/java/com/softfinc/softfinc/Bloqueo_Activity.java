package com.softfinc.softfinc;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;

import java.util.Calendar;

public class Bloqueo_Activity extends AppCompatActivity implements View.OnClickListener {
    //tiempo para salir al pulsar dos veces##
    private static final int INTERVALO = 5000; //2 segundos para salir
    private long tiempoPrimerClick;


    Button btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;


    TextView txtimei;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bloqueo_activity);

        //crear un txt para cargar el imei al usuario
        txtimei = (TextView) findViewById(R.id.txtimei);
        txtimei.setText("( " + Globales.IMEI +" )");

      //  btnDatePicker=(Button)findViewById(R.id.btn_date);
       // btnTimePicker=(Button)findViewById(R.id.btn_time);
       // txtDate=(EditText)findViewById(R.id.in_date);
       // txtTime=(EditText)findViewById(R.id.in_time);

        //btnDatePicker.setOnClickListener(this);
       // btnTimePicker.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {


        /*
        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txtTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
        */
    }


    @Override
    public void onBackPressed(){
        if (tiempoPrimerClick + INTERVALO > System.currentTimeMillis()){


            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            //startActivity(launch);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            //abrir el form del login
            startActivity(intent);
            //overridePendingTransition(R.anim.zoom_back_in,R.anim.zoom_back_out);
            finish();


            // super.onBackPressed();
            return;
        }else {
            Toast.makeText(this, "Vuelve a presionar para salir", Toast.LENGTH_SHORT).show();
        }
        tiempoPrimerClick = System.currentTimeMillis();
    }

}
