package com.example.romyprojectandroid;

public class Toy {
    private String text;
    private long icon;
    private String key;

    // Constructor with default values in case parameters are null
    public Toy(String text, long icon,String key) {
        this.text = text != null ? text : "לא צוין"; // Default if text is null
        this.icon = icon; // Default value for icon
        this.key = key;
    }

    public String getKey(){
        return this.key;
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
