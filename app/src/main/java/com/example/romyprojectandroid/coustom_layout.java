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
    private ArrayList<Toy> toys = new ArrayList<>(); // רשימת ההתנדבויות
    private ToyAdapter adapter; // מתאם לרשימה

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coustom_layout);

        // חיבור RecyclerView ל-Adapter
        RecyclerView recyclerView = findViewById(R.id.recyclerView_list);
        adapter = new ToyAdapter(toys);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // שליפת נתונים מ-Firebase
        fetchVolunteersFromFirebase();
    }

    private void fetchVolunteersFromFirebase() {
        DatabaseReference volunteersRef = FirebaseDatabase.getInstance().getReference("volunteers");

        volunteersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                toys.clear(); // מנקה את הרשימה לפני שמוסיף את הנתונים
                for (DataSnapshot data : snapshot.getChildren()) {
                    Volunteer volunteer = data.getValue(Volunteer.class); // ודא שהמחלקה Volunteer קיימת
                    if (volunteer != null) {
                        // יוצר אובייקט Toy לכל התנדבות עם אייקון כוכב
                        toys.add(new Toy(volunteer.type, volunteer.icon)); // שם אייקון כוכב כברירת מחדל
                    }
                }
                adapter.notifyDataSetChanged(); // עדכון הרשימה ב-RecyclerView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(coustom_layout.this, "שגיאה בטעינת הנתונים: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // מחלקה פנימית ל-Volunteer (אם אינה קיימת בקובץ נפרד)
    public static class Volunteer {
        public String type;
        public String icon; // שדה עבור שם האייקון

        public Volunteer() {} // קונסטרקטור ריק עבור Firebase

        public Volunteer(String type, String icon) {
            this.type = type;
            this.icon = icon;
        }
    }
}
