package com.example.tvprogramparser;

import java.io.IOException;
import java.util.ArrayList;

public class FavouriteObject {
    private static ArrayList<Channel> favouriteChannels = new ArrayList<Channel>();
    private static ArrayList<Program> favouritePrograms = new ArrayList<Program>();

    public static ArrayList<String> getArrayOfFavouriteChannels() {
        ArrayList<String> channels = new ArrayList<>();
        for (Channel chan: favouriteChannels) {
            channels.add(chan.getName());
        }
        return channels;
    }

    public static ArrayList<String> getArrayOfFavouritePrograms() {
        ArrayList<String> programs = new ArrayList<>();
        for (Program prog: favouritePrograms) {
            programs.add(prog.getName());
        }
        return programs;
    }

    public static void addToFavouriteChannels(Channel ch) {
        favouriteChannels.add(ch);
    }

    public static void addToFavouritePrograms(Program pr) {
        favouritePrograms.add(pr);
    }

    static void parseFavouriteProgram(String programName) {
        String[] temp = programName.split("\\(");
        addToFavouritePrograms(new Program(temp[0].trim()));
    }
}
