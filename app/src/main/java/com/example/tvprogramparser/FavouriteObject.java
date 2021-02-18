package com.example.tvprogramparser;

import java.io.IOException;
import java.util.ArrayList;
import java.lang.ArrayIndexOutOfBoundsException;

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

    public static void deleteFromFavouriteChannels(int position) {
        try {
            favouriteChannels.remove(position);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public static void deleteFromFavouritePrograms(int position) {
        try {
            favouritePrograms.remove(position);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    static void parseFavouriteProgram(String programName) {
        String[] temp = programName.split("\\(")[0].trim().split("    ");
        try {
            addToFavouritePrograms(new Program(temp[1].trim()));
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}
