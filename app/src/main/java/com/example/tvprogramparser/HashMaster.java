package com.example.tvprogramparser;

public class HashMaster {
    private int hash;
    private String str;
    private static final int p = 31;
    private static final int mod = 27644437;

    HashMaster(String strToHash) {
        this.str = strToHash;
        this.countHash();
    }

    private void countHash() {
        hash = (str.charAt(0) - '0');
        int locP = p;
        for (int i = 1; i < str.length(); i++) {
            hash = (hash + ((str.charAt(i) - '0') * locP) % mod) % mod;
            locP = (locP * p) % mod;
        }
    }


    public int getHash() {
        return hash;
    }

}
