package com.example.romyprojectandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton, viewUsersButton, adminButton;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private static final String ADMIN_EMAIL = "romy.poho@gmail.com"; // אימייל מנהל
    private boolean isAdmin = false; // משתנה לבדיקה אם המשתמש הוא מנהל

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        emailEditText = findViewById(R.id.editTextTextEmailAddress);
        passwordEditText = findViewById(R.id.Password);
        loginButton = findViewById(R.id.save);
        viewUsersButton = findViewById(R.id.save2);
        adminButton = findViewById(R.id.adminButton);

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");

        // הכפתור מוסתר כברירת מחדל
        adminButton.setVisibility(View.GONE);

        loginButton.setOnClickListener(v -> {
            if (isAdmin) {
                // אם המשתמש כבר זוהה כמנהל, לחיצה שנייה מעבירה אותו למסך הבחירה
                startActivity(new Intent(login.this, choice.class));
                finish();
            } else {
                loginUser();
            }
        });

        adminButton.setOnClickListener(v -> {
            Intent intent = new Intent(login.this, coustom_layout.class);
            startActivity(intent);
        });

        viewUsersButton.setOnClickListener(v -> {
            Intent intent = new Intent(login.this, list.class);
            startActivity(intent);
        });
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(login.this, "נא למלא את כל השדות", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    checkUserRole(user);
                }
            } else {
                Toast.makeText(login.this, "התחברות נכשלה", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUserRole(FirebaseUser user) {
        usersRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String userEmail = user.getEmail();
                    if (userEmail != null && userEmail.equals(ADMIN_EMAIL)) {
                        // המשתמש הוא מנהל - מציגים את כפתור הניהול והוא לא עובר עדיין למסך הבחירה
                        adminButton.setVisibility(View.VISIBLE);
                        isAdmin = true;
                        Toast.makeText(login.this, "זוהית כמנהל, לחץ שוב כדי להמשיך", Toast.LENGTH_SHORT).show();
                    } else {
                        // משתמש רגיל - עובר מיד למסך הבחירה
                        startActivity(new Intent(login.this, choice.class));
                        finish();
                    }
                } else {
                    Toast.makeText(login.this, "משתמש לא נמצא במסד הנתונים", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(login.this, "שגיאה בגישה למסד הנתונים", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
