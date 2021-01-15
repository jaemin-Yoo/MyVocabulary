package com.example.myvocabulary;

public class Wordlist {
    String name;
    String subname;
    String Url;
    String Url2;

    public Wordlist(String name, String subname, String Url, String Url2){
        this.name = name;
        this.subname = subname;
        this.Url = Url;
        this.Url2 = Url2;
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

    public String getUrl2() {
        return Url2;
    }

    @Override
    public String toString() {
        return "Wordlist{" +
                "name='" + name + '\'' +
                ", subname='" + subname + '\'' +
                ", Url=" + Url +
                ", Url2=" + Url2 +
                '}';
    }
}
