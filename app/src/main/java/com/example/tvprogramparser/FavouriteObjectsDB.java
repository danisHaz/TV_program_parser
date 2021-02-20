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
import java.util.List;

public class FavouriteObjectsDB {
    @Entity
    public static class Channel {
        @PrimaryKey
        public int id;

        @ColumnInfo(name = "name")
        public String name;

        @ColumnInfo(name = "link")
        public String link;

        Channel(int id, String name, String link) {
            this.id = id;
            this.name = name;
            this.link = link;
        }
    }

    @Entity
    public static class Program {
        @PrimaryKey
        public int id;

        @ColumnInfo(name = "name")
        public String name;

        Program(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    @Dao
    public interface ChannelsDao {
        @Query("SELECT * FROM channel")
        List<Channel> getAll();

        @Insert
        void insertAll(Channel... channels);

        @Insert
        void insertChannel(Channel channel);

        @Delete
        void delete(Channel channel);
    }

    @Dao public interface ProgramsDao {
        @Query("SELECT * FROM program")
        List<Program> getAll();

        @Insert
        void insertAll(Program... programs);

        @Insert
        void insertProgram(Program program);

        @Delete
        void delete(Program program);
    }

    @Database(entities={Channel.class, Program.class}, version = 2)
    public abstract static class DefaultDb extends RoomDatabase {
        public abstract ChannelsDao channelsDao();
        public abstract ProgramsDao programsDao();
    }

}
