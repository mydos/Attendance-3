package com.sust.attendence.Others;

/**
 * Created by Ikhtiar on 8/18/2015.
 */
public class Drawer_item {
    String text;
    int image;

    public Drawer_item(String text, int image) {
        this.text = text;
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
