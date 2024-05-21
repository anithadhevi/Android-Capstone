package com.jblearning.helloandroid;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class Fourth2Activity extends AppCompatActivity
{
    private EditText accetDate,nameInput1,genderInput,heightInput,weightInput;
    Button accSaveButton,accDeletButton;
    private static final String SHARED_PREF_NAME ="mypref";
    private static final String key_NAME ="NAME";
    private static final String key_GENDER="GENDER";
    private static final String key_HEIGHT="HEIGHT";
    private static final String key_WEIGHT="GOALWEIGHT";
    private static final String key_DATE="DATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fourth2);
        //Initialize Textview
        nameInput1 = findViewById(R.id.enterName);
        genderInput = findViewById(R.id.entergender);
        heightInput = findViewById(R.id.Height);
        weightInput = findViewById(R.id.GoalWeight);
        accetDate = findViewById(R.id.GoalDate);
        accSaveButton = findViewById(R.id.accountSavebtn);
        accDeletButton=findViewById(R.id.accountDeletebtn);
        Calendar calender = Calendar.getInstance();
        final int year = calender.get(Calendar.YEAR);
        final int month = calender.get(Calendar.MONTH);
        final int day = calender.get(Calendar.DAY_OF_MONTH);

        accetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Fourth2Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = month + "/" + day + "/" + year;
                        accetDate.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        accSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //data transfer on shared preference when button clicked
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(key_NAME,nameInput1.getText().toString());
                editor.putString(key_GENDER,genderInput.getText().toString());
                editor.putString(key_HEIGHT,heightInput.getText().toString());
                editor.putString(key_WEIGHT,weightInput.getText().toString());
                editor.putString(key_DATE,accetDate.getText().toString());
                editor.apply();
                Toast.makeText(Fourth2Activity.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
            }
        });

        accDeletButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //clear the entered information
                nameInput1.setText("");
                genderInput.setText("");
                heightInput.setText("");
                weightInput.setText("");
                accetDate.setText("");
            }
        });

        Button btnSecond = findViewById(R.id.AddActivityBtn);
        btnSecond.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Fourth2Activity.this, SecondActivity.class);
                startActivity(intent);
            }
        });

        Button btnFirst = findViewById(R.id.HomeActivityBtn);
        btnFirst.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Fourth2Activity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button btnThird= findViewById(R.id.HistoryActivityBtn);
        btnThird.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Fourth2Activity.this, ThirdActivity.class);
                startActivity(intent);
            }
        });
    }
}