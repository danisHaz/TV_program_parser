package com.example.tvprogramparser;

public class Program {
    private String programName;
    private String timeBegin;
    private String timeEnd;
    private int id;

    // Called only for temporary needs
    Program(String programName) {
        this.programName = programName;
        this.timeBegin = null;
        this.timeEnd = null;
        this.id = (new HashMaster(programName)).getHash();
    }

    Program(String programName, String timeBegin) {
        this.programName = programName;
        this.timeBegin = timeBegin;
        this.timeEnd = timeBegin;
        this.id = (new HashMaster(programName)).getHash();
    }

    Program(String programName, String timeBegin, String timeEnd) {
        this.programName = programName;
        this.timeBegin = timeBegin;
        this.timeEnd = timeEnd;
        this.id = (new HashMaster(programName)).getHash();
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
