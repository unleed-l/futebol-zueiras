package com.example.futebolzueiras;

public class Meme {

    private int id;
    private String description;
    private String tag;
    private byte[] image;


    public Meme(int id, String description, String tag, byte[] image) {
        this.id = id;
        this.description = description;
        this.tag = tag;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
