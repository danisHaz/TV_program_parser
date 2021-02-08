package com.example.tvprogramparser;

import java.util.ArrayList;

public class FavouriteObject {
    public static ArrayList<Channel> favouriteChannels = new ArrayList<Channel>();
    public static ArrayList<Program> favouritePrograms = new ArrayList<Program>();

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

    class Channel {
        // Write code
        private String channelLink;
        private String channelName;

        Channel(String channelLink, String channelName) {
            this.channelLink = channelLink;
            this.channelName = channelName;
        }

        public String getLink() {
            return this.channelLink;
        }

        public String getName() {
            return this.channelName;
        }
    }

    class Program {
        private String programName;
        private String timeBegin;
        private String timeEnd;

        Program(String programName, String timeBegin, String timeEnd) {
            this.programName = programName;
            this.timeBegin = timeBegin;
            this.timeEnd = timeEnd;
        }

        public String getName() {
            return this.programName;
        }

        public String getTimeBegin() {
            return this.timeBegin;
        }

        public String getTimeEnd() {
            return this.timeEnd;
        }
    }
}
