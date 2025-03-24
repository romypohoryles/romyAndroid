package com.example.romyprojectandroid;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserAdmin extends User {
    private final String role;
    private DatabaseReference databaseRef;

    public UserAdmin(String name, String lastName, String email, String dateOfBirth) {
        super(name, lastName, email, dateOfBirth);
        this.role = "Admin";
        this.databaseRef = FirebaseDatabase.getInstance().getReference("VolunteerActivities");
    }

    public String getRole() {
        return role;
    }

    // מחיקת התנדבות מהמסד
    public void deleteVolunteerActivity(String activityId) {
        databaseRef.child(activityId).removeValue()
                .addOnSuccessListener(aVoid -> System.out.println("ההתנדבות נמחקה בהצלחה"))
                .addOnFailureListener(e -> System.out.println("שגיאה במחיקת ההתנדבות: " + e.getMessage()));
    }

    // עריכת פרטי התנדבות
    public void editVolunteerActivity(String activityId, String newTitle, String newDescription) {
        databaseRef.child(activityId).child("title").setValue(newTitle);
        databaseRef.child(activityId).child("description").setValue(newDescription)
                .addOnSuccessListener(aVoid -> System.out.println("ההתנדבות עודכנה בהצלחה"))
                .addOnFailureListener(e -> System.out.println("שגיאה בעריכת ההתנדבות: " + e.getMessage()));
    }
}
