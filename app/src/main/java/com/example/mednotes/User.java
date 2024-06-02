package com.example.mednotes;

public class User {
    String email;
    String fullName;
    String fullName_ruc;

    String password;

    int user_id;

    String examples_practice;

    String terms_practice;

    byte[] image;

    public User(String email, String fullName, String fullName_ruc, String password, int user_id, String examples_practice, String terms_practice, byte[] image) {
        this.email = email;
        this.fullName = fullName;
        this.fullName_ruc = fullName_ruc;
        this.password = password;
        this.user_id = user_id;
        this.examples_practice = examples_practice;
        this.terms_practice = terms_practice;
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName_ruc() {
        return fullName_ruc;
    }

    public void setFullName_ruc(String fullName_ruc) {
        this.fullName_ruc = fullName_ruc;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getExamples_practice() {
        return examples_practice;
    }

    public void setExamples_practice(String examples_practice) {
        this.examples_practice = examples_practice;
    }

    public String getTerms_practice() {
        return terms_practice;
    }

    public void setTerms_practice(String terms_practice) {
        this.terms_practice = terms_practice;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
