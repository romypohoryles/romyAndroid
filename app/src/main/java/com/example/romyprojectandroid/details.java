package com.example.romyprojectandroid;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class details extends AppCompatActivity {
    // קישור ל-Views במסך
    private EditText volunteerTypeInput, locationInput, dateInput, minimumAgeInput, hoursInput;
    private ImageView selectedIconPreview;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        // קישור ל-Views במסך (לפי ה-ID המופיעים ב-XML)
        volunteerTypeInput = findViewById(R.id.volunteerTypeInput);
        locationInput = findViewById(R.id.locationInput);
        dateInput = findViewById(R.id.dateInput);
        minimumAgeInput = findViewById(R.id.minimumAgeInput);
        hoursInput = findViewById(R.id.hoursInput);

        // קבלת ה-ID של ההתנדבות מהIntent
        String volunteerId = getIntent().getStringExtra("volunteerId");
        if (volunteerId != null) {
            fetchVolunteerDetails(volunteerId);
        } else {
            Toast.makeText(this, "שגיאה בהבאת נתוני ההתנדבות", Toast.LENGTH_SHORT).show();
        }
    }

    // פונקציה להורדת פרטי ההתנדבות מ-Firebase
    private void fetchVolunteerDetails(String volunteerId) {
        // יצירת חיבור למסד הנתונים
        DatabaseReference volunteerRef = FirebaseDatabase.getInstance().getReference("volunteers").child(volunteerId);

        volunteerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // אם יש נתונים
                if (snapshot.exists()) {
                    try {
                        // הוצאת נתונים ממסד הנתונים
                        String volunteerType = snapshot.child("type").getValue(String.class);
                        String location = snapshot.child("location").getValue(String.class);
                        String date = snapshot.child("date").getValue(String.class);
                        String minimumAge = snapshot.child("minimumAge").getValue(String.class);
                        String hours = snapshot.child("hours").getValue(String.class);

                        // הצגת הנתונים אם הם לא null
                        if (volunteerType != null && location != null && date != null &&
                                minimumAge != null) {
                            volunteerTypeInput.setText(volunteerType);
                            locationInput.setText(location);
                            dateInput.setText(date);
                            minimumAgeInput.setText(minimumAge);
                            hoursInput.setText(hours);
                        }

                    } catch (Exception e) {
                        Toast.makeText(details.this, "שגיאה בעיבוד הנתונים", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(details.this, "ההתנדבות לא נמצאה", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(details.this, "שגיאה בטעינת הנתונים: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
