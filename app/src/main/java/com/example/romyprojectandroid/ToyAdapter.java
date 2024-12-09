package com.example.romyprojectandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ToyAdapter extends RecyclerView.Adapter<ToyAdapter.ToyViewHolder> {
    private ArrayList<Toy> toys;

    public ToyAdapter(ArrayList<Toy> toys) {
        this.toys = toys;
    }

    @NonNull
    @Override
    public ToyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // טוען את התצוגה של כל פריט ב-RecyclerView
        View toyView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recylclerwriter, parent, false);
        return new ToyViewHolder(toyView);
    }

    @Override
    public void onBindViewHolder(@NonNull ToyViewHolder holder, int position) {
        Toy current = toys.get(position);
        holder.textTextView.setText(current.getText());

        // טיפול באייקון
        int iconResourceId = holder.itemView.getContext().getResources().getIdentifier(current.getIcon(), "drawable", holder.itemView.getContext().getPackageName());
        if (iconResourceId != 0) {
            holder.iconImageView.setImageResource(iconResourceId);
        } else {
            holder.iconImageView.setVisibility(View.GONE); // מסתיר את התמונה אם לא נמצא
        }

        // טיפול בכוכב
        if (current.getStar() != null && !current.getStar().isEmpty()) {
            int starResourceId = holder.itemView.getContext().getResources().getIdentifier(current.getStar(), "drawable", holder.itemView.getContext().getPackageName());
            if (starResourceId != 0) {
                holder.starImageView.setImageResource(starResourceId);
            }
        }
    }

    @Override
    public int getItemCount() {
        return toys.size();
    }

    public static class ToyViewHolder extends RecyclerView.ViewHolder {
        public TextView textTextView;
        public ImageView iconImageView;
        public ImageView starImageView;

        public ToyViewHolder(@NonNull View itemView) {
            super(itemView);
            textTextView = itemView.findViewById(R.id.textView3);
            iconImageView = itemView.findViewById(R.id.imageView4);
            starImageView = itemView.findViewById(R.id.star);
        }
    }
}
