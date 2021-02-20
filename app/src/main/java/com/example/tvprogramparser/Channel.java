package com.example.tvprogramparser;

import java.io.Serializable;

public class Channel implements Serializable {
    private String name;
    private String link;
    private boolean favourite;
    private int id;

    Channel(String name, String link) {
        this.name = name;
        this.link = link;
        this.favourite = false;

        this.id = Integer.parseInt(link.split("/")[3]);
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public int getId() {
        return id;
    }

    public boolean isFavourite() {
        return favourite;
    }
}
