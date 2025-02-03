package com.example.romyprojectandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class userAdapter extends RecyclerView.Adapter<userAdapter.UserViewHolder> {

    private final ArrayList<User> users;
    private final Context context;

    public userAdapter(ArrayList<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View userView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_user, parent, false);
        return new UserViewHolder(userView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        if (position >= users.size()) return;

        User currentUser = users.get(position);

        // הצגת שם ושם משפחה
        holder.nameTextView.setText(currentUser.name + " " + currentUser.lastName);

        // הצגת אימייל
        holder.emailTextView.setText(currentUser.email);

        // הצגת תאריך לידה
        holder.birthDateTextView.setText("Date of Birth: " + currentUser.dateOfBirth);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView emailTextView;
        public TextView birthDateTextView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.userNameTextView);
            emailTextView = itemView.findViewById(R.id.userEmailTextView);
            birthDateTextView = itemView.findViewById(R.id.userBirthDateTextView);
        }
    }
}
