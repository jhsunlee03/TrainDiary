package com.example.traintwo;

public class JournalEntry {
    int id;
    String imagePath;
    String text;
    String date;

    public JournalEntry(){}
    public JournalEntry(int id, String imagePath, String text, String data){
        this.id = id;
        this.imagePath = imagePath;
        this.text = text;
        this.date = date;
    }

    public JournalEntry(String imagePath, String text, String date){
        this.imagePath = imagePath;
        this.text = text;
        this.date = date;
    }

    public int getId(){return this.id;}
    public void setId(int id){
        this.id = id;
    }
    public String getImagePath(){ return this.imagePath;}
    public void setImagePath(String imagePath){this.imagePath = imagePath;}
    public String getText(){return this.text;}
    public void setText(String text){this.text = text;}
    public String getDate(){return this.date;}
    public void setDate(String date){ this.date = date;}

}
