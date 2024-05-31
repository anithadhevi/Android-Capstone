package com.jblearning.helloandroid;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.HashMap;

public class Fourth2Activity extends AppCompatActivity
{
    private EditText accetDate,startweight,heightInput,weightInput;
    Button accSaveButton,accDeletButton;
    private static final String SHARED_PREF_NAME ="mypref";
    private static final String key_NAME ="STARTWEIGHT";
    private static final String key_GENDER="GENDER";
    private static final String key_HEIGHT="HEIGHT";
    private static final String key_WEIGHT="GOALWEIGHT";
    private static final String key_DATE="DATE";
    private DatabaseHelper dbHelper;
    private String genderValue;
    private RadioGroup radioGroup;
    private RadioButton rdMale;
    private RadioButton rdFemale;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fourth2);
        //Initialize Textview
        startweight = findViewById(R.id.startweight);
        //genderInput = findViewById(R.id.rdgGender);
        heightInput = findViewById(R.id.Height);
        weightInput = findViewById(R.id.GoalWeight);
        accetDate = findViewById(R.id.GoalDate);
        accSaveButton = findViewById(R.id.accountSavebtn);
        accDeletButton=findViewById(R.id.accountDeletebtn);
        radioGroup = findViewById(R.id.rdgGender);
        rdMale = findViewById(R.id.rdbMale);
        rdFemale = findViewById(R.id.rdbFemale);
        Calendar calender = Calendar.getInstance();
        final int year = calender.get(Calendar.YEAR);
        final int month = calender.get(Calendar.MONTH);
        final int day = calender.get(Calendar.DAY_OF_MONTH);
        dbHelper = new DatabaseHelper(this);

        SetAccountInfo();

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
                if (validateAndSubmit()) {
                    boolean success = dbHelper.AddUpdateAccountInfo(startweight.getText().toString(),
                            genderValue,
                            Double.parseDouble(heightInput.getText().toString()),
                            accetDate.getText().toString(),
                            Double.parseDouble(weightInput.getText().toString()));

                    if (success)
                        Toast.makeText(Fourth2Activity.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(Fourth2Activity.this, "Error occured while saving, please retry", Toast.LENGTH_SHORT).show();
                }
                    else
                    {
                        Toast.makeText(Fourth2Activity.this, "All information is required", Toast.LENGTH_SHORT).show();
                    }
                }

        });

        accDeletButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //clear the entered information
                startweight.setText("");
                rdFemale.setChecked(false);
                rdMale.setChecked(false);
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

    private void SetAccountInfo()
    {
        HashMap<String, String> myAccountData = dbHelper.GetAccountInfo();
        startweight.setText(myAccountData.get("StartWeight"));
        weightInput.setText(myAccountData.get("GoalWeight"));
        heightInput.setText(myAccountData.get("Height"));
        accetDate.setText(myAccountData.get("GoalDate"));
        String gender = myAccountData.get("Gender");

        if (gender != null && gender.equalsIgnoreCase("Male"))
        {
            rdMale.setChecked(true);
        }
        else if (gender != null && gender.equalsIgnoreCase("Female"))
        {
            rdFemale.setChecked(true);
        }
    }
    public void onRadioButtonClicked(View view)
    {
        boolean checked=((RadioButton) view).isChecked();
        String str="";
        int selectedId = radioGroup.getCheckedRadioButtonId();

        RadioButton selectedGender = findViewById(selectedId);
        genderValue = selectedGender.getText().toString();
    }

    private boolean validateAndSubmit() {
        String stweight = startweight.getText().toString().trim();
        String gender = genderValue;
        String height = heightInput.getText().toString().trim();
        String goalweight = weightInput.getText().toString().trim();
        String date = accetDate.getText().toString().trim();

        if (stweight.isEmpty() || gender.isEmpty() || height.isEmpty() || goalweight.isEmpty() || date.isEmpty())
        {
           // Toast.makeText(this, "All information is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            return true;
        }
    }



}