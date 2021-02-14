package com.example.tvprogramparser;

import java.io.Serializable;

public class Channel implements Serializable {
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
