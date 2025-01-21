package com.example.romyprojectandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // מוצאים את הכפתור ואת שדה הסיסמה
        Button save = findViewById(R.id.save);
        EditText passwordEditText = findViewById(R.id.Password); // ה-ID של שדה הסיסמה

        // מאזינים ללחיצה על כפתור ההתחברות
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = passwordEditText.getText().toString();

                // בודקים אם הסיסמה תקינה
                if (isPasswordValid(password)) {
                    // אם הסיסמה תקינה, מעבירים למסך הבא
                    Intent intent = new Intent(login.this, choice.class);
                    startActivity(intent);
                } else {
                    // מציגים הודעה על סיסמה לא תקינה
                    passwordEditText.setError("הסיסמה אינה עומדת בתנאים");
                }
            }
        });
    }

    // פונקציה לבדוק אם הסיסמה תקינה
    private boolean isPasswordValid(String password) {
//        if (password.length() < 8) {
//            return false; // אורך מינימלי של 8 תווים
//        }
//
//        if (!password.matches(".*[A-Z].*")) {
//            return false; // חייבת להיות לפחות אות גדולה אחת
//        }
//
//        if (!password.matches(".*[a-z].*")) {
//            return false; // חייבת להיות לפחות אות קטנה אחת
//        }
//
//        if (!password.matches(".*\\d.*")) {
//            return false; // חייבת להיות לפחות ספרה אחת
//        }
//
//        if (!password.matches(".*[@#$%^&+=!].*")) {
//            return false; // חייבת להיות לפחות תו מיוחד אחד
//        }

        return true; // הסיסמה תקינה
    }
}
