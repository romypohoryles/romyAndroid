package com.example.romyprojectandroid;

public class Toy {
    private String text;
    private String icon;
    private String star;

    // Constructor
    public Toy(String text, String icon) {
        this.text = text;
        this.icon = icon;
        this.star = "star"; // תמיד יכיל את שם האייקון של הכוכב
    }

    // Getters
    public String getText() {
        return text;
    }

    public String getIcon() {
        return icon;
    }

    public String getStar() {
        return star;
    }

    // Setters
    public void setText(String text) {
        this.text = text;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setStar(String star) {
        this.star = star;
    }
}
