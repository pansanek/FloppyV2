package com.potemkinas.floppy.model;

public class Category {
    int id;
    String title;
    String img;
    String color;

    public Category(int id, String title, String img, String color) {
        this.id = id;
        this.title = title;
        this.img = img;
        this.color = color;

    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
