package com.example.romyprojectandroid;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailsActivity extends AppCompatActivity {
    private TextView volunteerTypeTextView;
    private ImageView iconImageView;
    private ImageView starImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // קישור ל-Views במסך
        volunteerTypeTextView = findViewById(R.id.volunteerTypeTextView);
        iconImageView = findViewById(R.id.iconImageView);
        starImageView = findViewById(R.id.starImageView);

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
                        Long iconResource = snapshot.child("iconResource").getValue(Long.class);

                        if (volunteerType != null && iconResource != null) {
                            // הצגת נתונים ב-UI
                            volunteerTypeTextView.setText(volunteerType);
                            iconImageView.setImageResource(iconResource.intValue());

                            // הצגת אייקון כוכב
                            starImageView.setImageResource(R.drawable.star);
                        }
                    } catch (Exception e) {
                        Toast.makeText(DetailsActivity.this, "שגיאה בעיבוד הנתונים", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DetailsActivity.this, "ההתנדבות לא נמצאה", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetailsActivity.this, "שגיאה בטעינת הנתונים: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
