package com.jblearning.helloandroid;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.jar.Attributes;


public class MainActivity extends AppCompatActivity
{
    private DatabaseHelper dbHelper;
    private TextView weightSummaryTextView;

    private String name;
    private String weight;
    private Date date;

    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME ="mypref";
    private static final String key_NAME ="name";
    private static final String key_WEIGHT="weight";
    private static final String key_DATE="date";
    private Button HomeButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        //weightSummaryTextView = findViewById(R.id.displayTextView);
        TextView weightValueTextView = findViewById(R.id.weightview);
        String weight = getIntent().getStringExtra("weight");
        String date = getIntent().getStringExtra("date");
        TextView dateValueTextView = findViewById(R.id.dateview);

        // Set the weight and date values in the TextViews
        weightValueTextView.setText(weight);
        dateValueTextView.setText(date);

        Button btnFirst = findViewById(R.id.HomeActivityBtn);

        WeightEntry entries = dbHelper.getLatestWeights();
        if (entries != null) {
            weightValueTextView.setText(String.valueOf(entries.getWeight()));
            dateValueTextView.setText(entries.getDate());
        }
        //weightSummaryTextView.setText(  + );

        //sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        //String name = sharedPreferences.getString("NAME", null);
        //String sumdate = sharedPreferences.getString("DATE", null);
        //String sumweight = sharedPreferences.getString("WEIGHT", null);
        //displayWeightSummary();


       /* if (name != null && weight !=null && date !=null)
        {
            TextView textWelcome = findViewById(R.id.displayTextView);
            textWelcome.setText("Hello AS  /n Your weight is " + weight + " as on " + date);
        }*/

        btnFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                finish();
            }
        });

        Button btnSecond = findViewById(R.id.AddActivityBtn);
        btnSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });

        Button btnThird = findViewById(R.id.HistoryActivityBtn);
        btnThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ThirdActivity.class);
                startActivity(intent);
            }
        });

        Button btnFourth = findViewById(R.id.AccountActivityBtn);
        btnFourth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Fourth2Activity.class);
                startActivity(intent);
            }
        });
    }

       /* private void displayWeightSummary()
        {
            List<WeightEntry> entries =dbHelper.getAllWeights();
            {

              if(!entries.isEmpty())
              {
                  WeightEntry latestEntry = entries.get(entries.size()-1);
                  weightSummaryTextView.setText(latestEntry.getWeight()  + latestEntry.getDate());
              }

            }*/
        }





