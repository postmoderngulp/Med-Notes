package com.example.mednotes;

public class item {

    int id;

    String name;

    int user_id;
    String description;
    String date;
    String example;
    String finishing;

    public item(int id, String name, int user_id, String description, String date, String example, String finishing) {
        this.id = id;
        this.name = name;
        this.user_id = user_id;
        this.description = description;
        this.date = date;
        this.example = example;
        this.finishing = finishing;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getFinishing() {
        return finishing;
    }

    public void setFinishing(String finishing) {
        this.finishing = finishing;
    }
}
