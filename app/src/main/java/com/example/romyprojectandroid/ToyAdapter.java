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

    private final ArrayList<Toy> toys;

    public ToyAdapter(ArrayList<Toy> toys) {
        this.toys = toys;
    }

    @NonNull
    @Override
    public ToyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View toyView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recylclerwriter, parent, false);
        return new ToyViewHolder(toyView);
    }

    @Override
    public void onBindViewHolder(@NonNull ToyViewHolder holder, int position) {
        if (position >= toys.size()) return;

        Toy currentToy = toys.get(position);

        // Set text
        holder.textTextView.setText(currentToy.getText());

        // Set icon
        int iconResourceId = (int) currentToy.getIcon(); // Convert long to int
        if (iconResourceId != 0) {
            holder.iconImageView.setImageResource(iconResourceId);
        } else {
            holder.iconImageView.setImageResource(R.drawable.img); // Default icon
        }

        // Set star icon (always the same)
        holder.starImageView.setImageResource(R.drawable.star);
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
            starImageView = itemView.findViewById(R.id.starImageView); // Reference for star icon
        }
    }
}
