package com.jblearning.helloandroid;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.ContextWrapper;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.DatePicker;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import android.provider.MediaStore;
import android.Manifest;
import androidx.activity.result.ActivityResultLauncher;
import java.util.Calendar;
import java.util.Date;

public class SecondActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PERMISSIONS = 2;
    private DatabaseHelper dbHelper;
    private Button btnCapture;
    private EditText nameInput, weightInput, dateInput;
    private Button saveButton;
    private Button deletButton;
    private ImageView ivTrackerImage;
    private EditText etDate;
    private String ImagePath;
    public static final String READ_MEDIA_IMAGES = "android.permission.READ_MEDIA_IMAGES";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second2);

        dbHelper = new DatabaseHelper(this);
        weightInput = findViewById(R.id.enterWeightBtn);
        dateInput = findViewById(R.id.date_picker);
        saveButton = findViewById(R.id.WeightSaveBtn);
        deletButton = findViewById(R.id.WeightDeleteBtn);
        btnCapture = findViewById(R.id.CameraBtn);
        ivTrackerImage = findViewById(R.id.ivImage);
        btnCapture.setOnClickListener(v -> getPermissions());

        etDate = findViewById(R.id.date_picker);
        Calendar calender = Calendar.getInstance();
        final int year = calender.get(Calendar.YEAR);
        final int month = calender.get(Calendar.MONTH);
        final int day = calender.get(Calendar.DAY_OF_MONTH);
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(SecondActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = month + "/" + day + "/" + year;
                        etDate.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                saveWeightEntry();
            }
        });
        //Navigation Buttons
        Button btnFirst = findViewById(R.id.HomeActivityBtn);
        btnFirst.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v){
                Intent intent = new Intent(SecondActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button btnThird = findViewById(R.id.HistoryActivityBtn);
        btnThird.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v){
                //create intent to navigate the third activity
                Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
                //pass data as extras to the Intent
                startActivity(intent);
            }
        });

        Button btnFourth = findViewById(R.id.AccountActivityBtn);
        btnFourth.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v){
                //create intent to navigate the third activity
                Intent intent = new Intent(SecondActivity.this, Fourth2Activity.class);
                //pass data as extras to the Intent
                startActivity(intent);
            }
        });
    }

    private void getPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS);
        } else {
            capturePhoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                capturePhoto();
            } else {
                Toast.makeText(this, "Permissions are required to use the camera", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void capturePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            //startActivityIntent.launch(takePictureIntent);
        //}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ivTrackerImage.setImageBitmap(imageBitmap);
            ImagePath = saveImageToFile(imageBitmap);
        }
    }
    private String saveImageToFile(Bitmap bitmap) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("WeightTracker", Context.MODE_PRIVATE);
        String fileName = "WeightTracker_" + System.currentTimeMillis() + ".jpg";
        File mypath = new File(directory, fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.getAbsolutePath();
    }

    private void saveWeightEntry() {

        String weightString = weightInput.getText().toString().trim();
        if (weightString.isEmpty()) {
            Toast.makeText(SecondActivity.this, "Please enter the weight", Toast.LENGTH_LONG).show();
            return;
        }
        double weight = Double.parseDouble(weightString);
        String date = etDate.getText().toString();
        String imagePath = ImagePath;
        boolean isInserted = dbHelper.addWeightEntry(weight,date, imagePath);
        if (isInserted) {
            Toast.makeText(SecondActivity.this, "Weight Entry Saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(SecondActivity.this, "Error in saving Weight Entry", Toast.LENGTH_SHORT).show();
        }

        deletButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clear the entered information
                nameInput.setText("");
                weightInput.setText("");
                dateInput.setText("");
            }
        });
    }

}