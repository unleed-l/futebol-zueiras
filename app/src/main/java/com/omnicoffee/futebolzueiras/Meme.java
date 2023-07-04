package com.omnicoffee.futebolzueiras;

public class Meme {

    private int id;
    private String description;
    private String tag;
    private byte[] image;
    private String memeURL;


    public Meme(int id, String description, String tag, byte[] image) {
        this.id = id;
        this.description = description;
        this.tag = tag;
        this.image = image;
    }

    public Meme(String description, String tag){
        this.description = description;
        this.tag = tag;
    }

    public Meme() {

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

    public String getMemeURL() {
        return memeURL;
    }

    public void setMemeURL(String memeURL) {
        this.memeURL = memeURL;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
