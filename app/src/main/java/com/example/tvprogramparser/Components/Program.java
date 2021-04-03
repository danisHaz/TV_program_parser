package com.example.tvprogramparser.Components;

public class Program {
    private String programName;
    private String timeBegin;
    private String timeEnd;
    private String undefined = "undefined";
    private int id;

    // Called only for temporary needs
    public Program(String programName) {
        this.programName = programName;
        this.timeBegin = undefined;
        this.timeEnd = undefined;
        this.id = (new HashMaster(programName)).getHash();
    }

    public Program(String programName, String timeBegin) {
        this.programName = programName;
        this.timeBegin = timeBegin;
        this.timeEnd = undefined;
        this.id = (new HashMaster(programName)).getHash();
    }

    public Program(String programName, String timeBegin, String timeEnd) {
        this.programName = programName;
        this.timeBegin = timeBegin;
        this.timeEnd = timeEnd;
        this.id = (new HashMaster(programName)).getHash();
    }

    public void updateTimeBegin(String timeBegin) {
        this.timeBegin = timeBegin;
    }

    public boolean isEqual(Program pr) {
        return (pr.getId() == this.id);
    }

    public String getName() {
        return this.programName;
    }

    public int getId() {
        return this.id;
    }

    public String getTimeBegin() {
        return this.timeBegin;
    }

    public String getTimeEnd() {
        return this.timeEnd;
    }
}
