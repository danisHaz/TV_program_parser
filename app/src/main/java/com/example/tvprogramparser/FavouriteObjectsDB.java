package com.example.tvprogramparser;

import androidx.room.ColumnInfo;
import androidx.room.Database;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.RoomDatabase;

import java.util.ArrayList;

public class FavouriteObjectsDB {
    @Entity
    public static class Channel {
        @PrimaryKey
        public int id;

        @ColumnInfo(name = "name")
        public String name;

        @ColumnInfo(name = "link")
        public String link;
    }

    @Entity
    public static class Program {
        @PrimaryKey
        public int id;

        @ColumnInfo(name = "name")
        public String name;

        @ColumnInfo(name = "timeBegin")
        public String timeBegin;
    }

    @Dao
    public interface ChannelsDao {
        @Query("SELECT * FROM channel")
        ArrayList<Channel> getAll();

        @Insert
        void insertAll(Channel... channels);

        @Delete
        void delete(Channel channel);
    }

    @Dao public interface ProgramsDao {
        @Query("SELECT * FROM program")
        ArrayList<Program> getAll();

        @Insert
        void insertAll(Program... programs);

        @Delete
        void delete(Program program);
    }

    @Database(entities={Channel.class, Program.class}, version = 1)
    public abstract class DefaultDb extends RoomDatabase {
        public abstract ChannelsDao channelsDao();
        public abstract ProgramsDao programsDao();
    }

}
