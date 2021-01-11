package com.example.myvocabulary;

public class Wordlist {
    String name;
    String subname;
    String Url;

    public Wordlist(String name, String subname, String Url){
        this.name = name;
        this.subname = subname;
        this.Url = Url;
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

    public String getUrl() {
        return Url;
    }

    @Override
    public String toString() {
        return "Wordlist{" +
                "name='" + name + '\'' +
                ", subname='" + subname + '\'' +
                ", Url=" + Url +
                '}';
    }
}
