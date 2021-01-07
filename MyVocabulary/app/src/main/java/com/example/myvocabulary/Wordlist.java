package com.example.myvocabulary;

public class Wordlist {
    String name;
    String subname;
    int resId;

    public Wordlist(String name, String subname, int resId){
        this.name = name;
        this.subname = subname;
        this.resId = resId;
    }

    public String getName() {
        return name;
    }

    public void setName() {
        this.name = name;
    }

    public String getSubname() {
        return subname;
    }

    public void setSubname(String subname){
        this.subname = subname;
    }

    public int getResId() {
        return resId;
    }

    @Override
    public String toString() {
        return "Wordlist{" +
                "name='" + name + '\'' +
                ", subname='" + subname + '\'' +
                ", resId=" + resId +
                '}';
    }
}