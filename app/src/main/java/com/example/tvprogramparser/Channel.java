package com.example.tvprogramparser;

public class Channel {
    private String name;
    private String link;
    private boolean favourite;

    Channel(String name, String link) {
        this.name = name;
        this.link = link;
        this.favourite = false;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public boolean isFavourite() {
        return favourite;
    }
}
