package com.example.romyprojectandroid;

public class Toy {
    private String text;
    private long icon;

    // Constructor with default values in case parameters are null
    public Toy(String text, long icon) {
        this.text = text != null ? text : "לא צוין"; // Default if text is null
        this.icon = icon; // Default value for icon
    }

    // Getters
    public String getText() {
        return text;
    }

    public long getIcon() {
        return icon;
    }

    // Setters
    public void setText(String text) {
        this.text = text;
    }

    public void setIcon(long icon) {
        this.icon = icon;
    }
}
