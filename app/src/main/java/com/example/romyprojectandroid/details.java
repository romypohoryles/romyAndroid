package com.example.romyprojectandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;

public class details extends AppCompatActivity {
    private EditText volunteerTypeInput, locationInput, dateInput, minimumAgeInput, hoursInput;
    private Button registerButton, viewVolunteersButton, myVolunteeringButton;
    private DatabaseReference volunteerRef;
    private FirebaseUser currentUser;
    private String volunteerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        // קישור לרכיבי המסך
        volunteerTypeInput = findViewById(R.id.volunteerTypeInput);
        locationInput = findViewById(R.id.locationInput);
        dateInput = findViewById(R.id.dateInput);
        minimumAgeInput = findViewById(R.id.minimumAgeInput);
        hoursInput = findViewById(R.id.hoursInput);
        registerButton = findViewById(R.id.button_register_volunteer);
        viewVolunteersButton = findViewById(R.id.button_view_volunteers);
        myVolunteeringButton = findViewById(R.id.button_my_volunteering);

        // אתחול המשתמש הנוכחי
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "עליך להתחבר תחילה", Toast.LENGTH_SHORT).show();
            Log.e("details", "משתמש לא מחובר");
            finish();
            return;
        }

        // קבלת ה-ID של ההתנדבות מה-Intent
        volunteerId = getIntent().getStringExtra("volunteerId");

        if (volunteerId != null) {
            fetchVolunteerDetails(volunteerId);
        } else {
            Toast.makeText(this, "שגיאה בטעינת ההתנדבות", Toast.LENGTH_SHORT).show();
            Log.e("details", "volunteerId is null");
            finish();
        }

        // העברת ה-ID של ההתנדבות למסך רשימת המתנדבים
        viewVolunteersButton.setOnClickListener(v -> {
            Intent intent = new Intent(details.this, VolunteerListActivity.class);
            intent.putExtra("activityId", volunteerId); // העברת ה-ID
            startActivity(intent);
        });

        // מעבר למסך ההתנדבויות שלי
        myVolunteeringButton.setOnClickListener(v -> startActivity(new Intent(details.this, MyVolunteeringActivity.class)));

        // הרשמה להתנדבות
        registerButton.setOnClickListener(v -> registerForVolunteering());
    }

    // טעינת פרטי ההתנדבות מהמסד נתונים
    private void fetchVolunteerDetails(String volunteerId) {
        volunteerRef = FirebaseDatabase.getInstance().getReference("volunteers").child(volunteerId);
        volunteerRef.get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                volunteerTypeInput.setText(snapshot.child("type").getValue(String.class));
                locationInput.setText(snapshot.child("location").getValue(String.class));
                dateInput.setText(snapshot.child("date").getValue(String.class));
                minimumAgeInput.setText(snapshot.child("minimumAge").getValue(String.class));
                hoursInput.setText(snapshot.child("hours").getValue(String.class));
            } else {
                Log.w("details", "Volunteer details not found in Firebase");
            }
        }).addOnFailureListener(e -> {
            Log.e("details", "Error loading volunteer data", e);
            Toast.makeText(this, "שגיאה בטעינת הנתונים", Toast.LENGTH_SHORT).show();
        });
    }

    // פונקציה לרישום משתמש להתנדבות
    private void registerForVolunteering() {
        if (currentUser == null) {
            Toast.makeText(this, "עליך להתחבר תחילה", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        userRef.get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                String firstName = snapshot.child("name").getValue(String.class);
                String lastName = snapshot.child("lastName").getValue(String.class);

                if (firstName == null || lastName == null) {
                    Toast.makeText(this, "שגיאה בקבלת פרטי המשתמש", Toast.LENGTH_SHORT).show();
                    return;
                }

                String fullName = firstName + " " + lastName;

                // שמירת המשתמש ברשימת המתנדבים
                DatabaseReference volunteersRef = FirebaseDatabase.getInstance()
                        .getReference("volunteers")
                        .child(volunteerId)
                        .child("volunteersList");

                volunteersRef.child(userId).setValue(fullName);

                // שמירת ההתנדבות תחת המשתמש
                DatabaseReference userVolunteeringRef = FirebaseDatabase.getInstance()
                        .getReference("Users")
                        .child(userId)
                        .child("myVolunteering");

                HashMap<String, String> volunteeringDetails = new HashMap<>();
                volunteeringDetails.put("volunteerId", volunteerId);
                volunteeringDetails.put("volunteerType", volunteerTypeInput.getText().toString());
                userVolunteeringRef.child(volunteerId).setValue(volunteeringDetails);

                Toast.makeText(this, "נרשמת בהצלחה!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "שגיאה בקבלת פרטי המשתמש", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Log.e("details", "Error loading user data", e);
            Toast.makeText(this, "שגיאה בטעינת הנתונים", Toast.LENGTH_SHORT).show();
        });
    }
}
