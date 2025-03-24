package com.example.romyprojectandroid;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class MyVolunteeringActivity extends AppCompatActivity {
    private ListView myVolunteeringListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> myVolunteeringList;
    private DatabaseReference userVolunteeringRef;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_volunteering);

        myVolunteeringListView = findViewById(R.id.myVolunteeringListView);
        myVolunteeringList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, myVolunteeringList);
        myVolunteeringListView.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();

        // מאזין לשינויים באימות
        authStateListener = firebaseAuth -> {
            currentUser = firebaseAuth.getCurrentUser();
            if (currentUser == null) {
                Toast.makeText(this, "עליך להתחבר תחילה", Toast.LENGTH_SHORT).show();
                Log.e("MyVolunteeringActivity", "משתמש לא מחובר");
                finish();
            } else {
                Log.d("MyVolunteeringActivity", "משתמש מחובר: " + currentUser.getUid());
                userVolunteeringRef = FirebaseDatabase.getInstance().getReference("Users")
                        .child(currentUser.getUid())
                        .child("myVolunteering");

                fetchMyVolunteering();
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null) {
            mAuth.removeAuthStateListener(authStateListener);
        }
    }

    private void fetchMyVolunteering() {
        userVolunteeringRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myVolunteeringList.clear();
                if (!snapshot.exists()) {
                    myVolunteeringList.add("אין לך התנדבויות כרגע");
                    Log.w("MyVolunteeringActivity", "אין נתונים להתנדבויות");
                    adapter.notifyDataSetChanged();
                    return;
                }

                for (DataSnapshot activitySnapshot : snapshot.getChildren()) {
                    String volunteerId = activitySnapshot.child("volunteerId").getValue(String.class);
                    if (volunteerId != null) {
                        loadVolunteerDetails(volunteerId);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MyVolunteeringActivity", "שגיאה בטעינת הנתונים: " + error.getMessage());
                Toast.makeText(MyVolunteeringActivity.this, "שגיאה בטעינת הנתונים", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadVolunteerDetails(String volunteerId) {
        DatabaseReference volunteerRef = FirebaseDatabase.getInstance().getReference("volunteers").child(volunteerId);

        volunteerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot volunteerSnapshot) {
                if (volunteerSnapshot.exists()) {
                    String activityName = volunteerSnapshot.child("type").getValue(String.class);
                    String activityDate = volunteerSnapshot.child("date").getValue(String.class);

                    if (activityName != null && activityDate != null) {
                        myVolunteeringList.add(activityName + " - " + activityDate);
                    } else {
                        myVolunteeringList.add("התנדבות לא ידועה - ללא תאריך");
                    }
                } else {
                    myVolunteeringList.add("התנדבות לא נמצאה");
                    Log.w("MyVolunteeringActivity", "התנדבות עם ID " + volunteerId + " לא נמצאה במסד הנתונים");
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MyVolunteeringActivity", "שגיאה בשליפת נתוני ההתנדבות: " + error.getMessage());
            }
        });
    }
}
