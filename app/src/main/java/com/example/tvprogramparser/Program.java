package com.example.tvprogramparser;

public class Program {
    private String programName;
    private String timeBegin;
    private String timeEnd;

    Program(String programName, String timeBegin) {
        this.programName = programName;
        this.timeBegin = timeBegin;
        this.timeEnd = timeBegin;
    }

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