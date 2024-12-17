package com.example.romyprojectandroid;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class coustom_layout extends AppCompatActivity {
    private final ArrayList<Toy> toys = new ArrayList<>();
    private ToyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coustom_layout);

        RecyclerView recyclerView = findViewById(R.id.recyclerView_list);
        adapter = new ToyAdapter(toys);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchVolunteersFromFirebase();
    }

    private void fetchVolunteersFromFirebase() {
        DatabaseReference volunteersRef = FirebaseDatabase.getInstance().getReference("volunteers");

        volunteersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                toys.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    try {
                        // Extracting data directly
                        String text = data.child("type").getValue(String.class);
                        Long icon = data.child("iconResource").getValue(Long.class);

                        if (text != null && icon != null) {
                            toys.add(new Toy(text, icon));
                        }
                    } catch (Exception e) {
                        Toast.makeText(coustom_layout.this, "שגיאה בעיבוד הנתונים", Toast.LENGTH_SHORT).show();
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(coustom_layout.this, "שגיאה בטעינת הנתונים: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
