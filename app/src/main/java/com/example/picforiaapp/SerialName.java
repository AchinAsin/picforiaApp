package com.example.picforiaapp;

import com.google.firebase.database.Exclude;

public class SerialName {
    private String Link;
    private String FileName;
    private String Key;
    private String MetaData;

    public SerialName() {
    }

    public SerialName(String link, String fileName, String key, String metaData) {
        Link = link;
        FileName = fileName;
        Key = key;
        MetaData = metaData;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getMetaData() {
        return MetaData;
    }

    public void setMetaData(String metaData) {
        MetaData = metaData;
    }
}

