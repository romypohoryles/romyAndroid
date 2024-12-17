package com.example.romyprojectandroid;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class new_volnteer extends AppCompatActivity {
    // שדות להזנת הנתונים
    private EditText volunteerTypeInput;
    private EditText locationInput;
    private EditText dateInput;
    private EditText minimumAgeInput;
    private EditText hoursInput;
    private Spinner iconSpinner;
    private ImageView selectedIconPreview;

    // רשימת שישה אייקונים
    private Integer[] icons = {
            R.drawable.icon0, R.drawable.icon1, R.drawable.icon2,
            R.drawable.icon3, R.drawable.icon4, R.drawable.icon5
    };
    private int selectedIconResource = R.drawable.icon0; // ברירת מחדל

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_volnteer);

        // קישור השדות ל-ID ב-XML
        volunteerTypeInput = findViewById(R.id.volunteerTypeInput);
        locationInput = findViewById(R.id.locationInput);
        dateInput = findViewById(R.id.dateInput);
        minimumAgeInput = findViewById(R.id.minimumAgeInput);
        hoursInput = findViewById(R.id.hoursInput);
        iconSpinner = findViewById(R.id.iconSpinner);
        selectedIconPreview = findViewById(R.id.selectedIconPreview);

        // חיבור Adapter מותאם ל-Spinner
        IconAdapter adapter = new IconAdapter(icons);
        iconSpinner.setAdapter(adapter);

        // פעולה בעת בחירת אייקון
        iconSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // שמירת האייקון הנבחר
                selectedIconResource = icons[position];
                selectedIconPreview.setImageResource(selectedIconResource);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // ברירת מחדל אם לא נבחר אייקון
            }
        });

        // כפתור לשמירת הנתונים
        findViewById(R.id.saveVolunteerButton).setOnClickListener(this::saveVolunteer);
    }

    // פונקציה לשמירת הנתונים ב-Firebase
    public void saveVolunteer(View view) {
        // קבלת ערכים מהשדות
        String volunteerType = volunteerTypeInput.getText().toString().trim();
        String location = locationInput.getText().toString().trim();
        String date = dateInput.getText().toString().trim();
        String minimumAge = minimumAgeInput.getText().toString().trim();
        String hours = hoursInput.getText().toString().trim();

        // בדיקת תקינות הערכים
        if (TextUtils.isEmpty(volunteerType)) {
            Toast.makeText(this, "אנא הזן את סוג ההתנדבות", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(location)) {
            Toast.makeText(this, "אנא הזן מיקום", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(date)) {
            Toast.makeText(this, "אנא הזן תאריך", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(minimumAge)) {
            Toast.makeText(this, "אנא הזן גיל מינימלי", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(hours)) {
            Toast.makeText(this, "אנא הזן מספר שעות", Toast.LENGTH_SHORT).show();
            return;
        }

        // יצירת חיבור ל-Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference volunteersRef = database.getReference("volunteers");

        // יצירת עצם מתנדב כולל האייקון
        Volunteer volunteer = new Volunteer(volunteerType, location, date, minimumAge, hours, selectedIconResource);

        // שליחת הנתונים ל-Firebase
        volunteersRef.push().setValue(volunteer).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "הנתונים נשמרו בהצלחה!", Toast.LENGTH_SHORT).show();
                clearFields(); // איפוס השדות לאחר שמירה
            } else {
                Toast.makeText(this, "שגיאה בשמירת הנתונים: " + task.getException(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // פונקציה לאיפוס השדות לאחר שמירה
    private void clearFields() {
        volunteerTypeInput.setText("");
        locationInput.setText("");
        dateInput.setText("");
        minimumAgeInput.setText("");
        hoursInput.setText("");
        iconSpinner.setSelection(0); // איפוס האייקון לברירת מחדל
        selectedIconPreview.setImageResource(icons[0]);
    }

    // מחלקה לייצוג מתנדב
    public static class Volunteer {
        private String type;
        private String location;
        private String date;
        private String minimumAge;
        private String hours;
        private int iconResource; // מזהה האייקון

        // קונסטרקטור ריק עבור Firebase
        public Volunteer() {}

        // קונסטרקטור מותאם אישית
        public Volunteer(String type, String location, String date, String minimumAge, String hours, int iconResource) {
            this.type = type;
            this.location = location;
            this.date = date;
            this.minimumAge = minimumAge;
            this.hours = hours;
            this.iconResource = iconResource;
        }

        // Getters
        public String getType() {
            return type;
        }

        public String getLocation() {
            return location;
        }

        public String getDate() {
            return date;
        }

        public String getMinimumAge() {
            return minimumAge;
        }

        public String getHours() {
            return hours;
        }

        public int getIconResource() {
            return iconResource;
        }

        // Setters
        public void setType(String type) {
            this.type = type;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setMinimumAge(String minimumAge) {
            this.minimumAge = minimumAge;
        }

        public void setHours(String hours) {
            this.hours = hours;
        }

        public void setIconResource(int iconResource) {
            this.iconResource = iconResource;
        }
    }

    // Adapter מותאם אישית עבור Spinner
    private class IconAdapter extends BaseAdapter {
        private final Integer[] icons;

        public IconAdapter(Integer[] icons) {
            this.icons = icons;
        }

        @Override
        public int getCount() {
            return icons.length;
        }

        @Override
        public Object getItem(int position) {
            return icons[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = new ImageView(new_volnteer.this);
            imageView.setImageResource(icons[position]);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(100, 100)); // גודל אייקון בתצוגה הראשית
            return imageView;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = new ImageView(new_volnteer.this);
            imageView.setImageResource(icons[position]);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(150, 150)); // גודל אייקון בתפריט הנפתח
            return imageView;
        }
    }
}
