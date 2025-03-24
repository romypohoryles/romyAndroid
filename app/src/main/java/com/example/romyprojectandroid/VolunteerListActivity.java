package com.example.romyprojectandroid;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class VolunteerListActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> volunteerList;
    private DatabaseReference volunteersRef;
    private String activityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_list);

        listView = findViewById(R.id.volunteerListView);
        volunteerList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, volunteerList);
        listView.setAdapter(adapter);

        // קבלת activityId מה-Intent
        activityId = getIntent().getStringExtra("activityId");

        if (activityId == null) {
            Toast.makeText(this, "שגיאה: לא התקבל מזהה פעילות", Toast.LENGTH_SHORT).show();
            Log.e("VolunteerListActivity", "activityId is null");
            finish();
            return;
        }

        // קישור לנתיב הנכון בפיירבייס
        volunteersRef = FirebaseDatabase.getInstance()
                .getReference("volunteers")
                .child(activityId)
                .child("volunteersList");

        loadVolunteers();
    }

    private void loadVolunteers() {
        volunteersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                volunteerList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    String volunteerName = data.getValue(String.class);
                    if (volunteerName != null) {
                        volunteerList.add(volunteerName);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(VolunteerListActivity.this, "שגיאה בטעינת הנתונים", Toast.LENGTH_SHORT).show();
                Log.e("VolunteerListActivity", "Database error: " + error.getMessage());
            }
        });
    }
}
