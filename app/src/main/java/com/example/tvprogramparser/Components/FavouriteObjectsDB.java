package com.example.tvprogramparser.Components;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Database;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class FavouriteObjectsDB {
    private static volatile FavouriteObjectsDB objectsDB;
    private DefaultDb myDB;

    private FavouriteObjectsDB(Context context) {
        myDB = Room.databaseBuilder(context, FavouriteObjectsDB.DefaultDb.class,
                "defDb").addMigrations(MIGRATION_2_3).build();

        new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                FavouriteObjectsDB.ChannelsDao chanDao = myDB.channelsDao();
                for (FavouriteObjectsDB.Channel ch: chanDao.getAll()) {
                    FavouriteObject.favouriteChannels.add(
                            new com.example.tvprogramparser.Components.Channel(ch.name, ch.link)
                    );
                }

                FavouriteObjectsDB.ProgramsDao progDao = myDB.programsDao();
                for (FavouriteObjectsDB.Program prog: progDao.getAll()) {
                    FavouriteObject.favouritePrograms.add(
                            new com.example.tvprogramparser.Components.Program(prog.name)
                    );
                }
            }
        }).start();
    }

    public static FavouriteObjectsDB createInstance(Context context) {
        FavouriteObjectsDB localDB = objectsDB;
        if (localDB == null) {
            synchronized (FavouriteObjectsDB.class) {
                localDB = objectsDB;
                if (localDB == null) {
                    objectsDB = localDB = new FavouriteObjectsDB(context);
                }
            }
        }

        return localDB;
    }

//    Instead of working with real db class easy-to-understand API created
    @Deprecated
    public DefaultDb getDB() {
        return myDB;
    }

    public List<com.example.tvprogramparser.Components.Channel> getAllFavouriteChannels() {
        FavouriteObjectsDB.ChannelsDao channelsDao = myDB.channelsDao();
        List<com.example.tvprogramparser.Components.Channel> channelList
                = new ArrayList<>();

        synchronized (FavouriteObjectsDB.class) {
            for (FavouriteObjectsDB.Channel channel : channelsDao.getAll()) {
                channelList.add(new com.example.tvprogramparser.Components.Channel(
                        channel.name,
                        channel.link,
                        channel.pathToIcon
                ));
            }
        }

        return channelList;
    }

    public void insertFavouriteChannel(com.example.tvprogramparser.Components.Channel channel) {
        FavouriteObjectsDB.ChannelsDao channelsDao = myDB.channelsDao();
        synchronized (FavouriteObjectsDB.class) {
            channelsDao.insertChannel(new FavouriteObjectsDB.Channel(
                    channel.getId(),
                    channel.getName(),
                    channel.getLink(),
                    channel.getPathToIcon()
            ));
        }
    }

    public void deleteFavouriteChannel(com.example.tvprogramparser.Components.Channel channel) {
        FavouriteObjectsDB.ChannelsDao channelsDao = myDB.channelsDao();
        synchronized (FavouriteObjectsDB.class) {
            channelsDao.delete(new FavouriteObjectsDB.Channel(
                    channel.getId(),
                    channel.getName(),
                    channel.getLink(),
                    channel.getPathToIcon()
            ));
        }
    }

    public List<com.example.tvprogramparser.Components.Channel> getAllMainChannels() {
        FavouriteObjectsDB.MainChannelsDao mainDao = myDB.mainChannelsDao();
        List<com.example.tvprogramparser.Components.Channel> channelList
                = new ArrayList<>();

        synchronized (FavouriteObjectsDB.class) {
            for (FavouriteObjectsDB.Channel channel : mainDao.getAll()) {
                channelList.add(new com.example.tvprogramparser.Components.Channel(
                        channel.name,
                        channel.link,
                        channel.pathToIcon
                ));
            }
        }

        return channelList;
    }

    public void insertMainChannel(com.example.tvprogramparser.Components.Channel channel) {
        FavouriteObjectsDB.MainChannelsDao mainDao = myDB.mainChannelsDao();
        synchronized (FavouriteObjectsDB.class) {
            mainDao.insertChannel(new FavouriteObjectsDB.MainChannels(
                    channel.getId(),
                    channel.getName(),
                    channel.getLink(),
                    channel.getPathToIcon()
            ));
        }
    }

    public void deleteMainChannel(com.example.tvprogramparser.Components.Channel channel) {
        FavouriteObjectsDB.MainChannelsDao mainDao = myDB.mainChannelsDao();
        synchronized (FavouriteObjectsDB.class) {
            mainDao.delete(new FavouriteObjectsDB.MainChannels(
                    channel.getId(),
                    channel.getName(),
                    channel.getLink(),
                    channel.getPathToIcon()
            ));
        }
    }

    public List<com.example.tvprogramparser.Components.Program> getAllFavouritePrograms() {
        FavouriteObjectsDB.ProgramsDao programsDao = myDB.programsDao();
        List<com.example.tvprogramparser.Components.Program> programList
                = new ArrayList<>();

        synchronized (FavouriteObjectsDB.class) {
            for (FavouriteObjectsDB.Program program : programsDao.getAll()) {
                programList.add(new com.example.tvprogramparser.Components.Program(
                        program.name
                ));
            }
        }

        return programList;
    }

    public void insertFavouriteProgram(com.example.tvprogramparser.Components.Program program) {
        FavouriteObjectsDB.ProgramsDao programsDao = myDB.programsDao();
        synchronized (FavouriteObjectsDB.class) {
            programsDao.insertProgram(new FavouriteObjectsDB.Program(
                    program.getId(),
                    program.getName()
            ));
        }
    }

    public void deleteFavouriteProgram(com.example.tvprogramparser.Components.Program program) {
        FavouriteObjectsDB.ProgramsDao programsDao = myDB.programsDao();
        synchronized (FavouriteObjectsDB.class) {
            programsDao.delete(new FavouriteObjectsDB.Program(
                    program.getId(),
                    program.getName()
            ));
        }
    }

    @Entity
    public static class Channel {
        @PrimaryKey
        public int id;

        @ColumnInfo(name = "name")
        public String name;

        @ColumnInfo(name = "link")
        public String link;

        @ColumnInfo(name = "icon")
        public String pathToIcon;

        public Channel(int id, String name, String link, String pathToIcon) {
            this.id = id;
            this.name = name;
            this.link = link;
            this.pathToIcon = pathToIcon;
        }
    }

    @Entity
    public static class MainChannels extends Channel {

        public MainChannels(int id, String name, String link, String pathToIcon) {
            super(id, name, link, pathToIcon);
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

    @Dao
    public interface MainChannelsDao {
        @Query("SELECT * FROM mainchannels")
        List<MainChannels> getAll();

        @Insert
        void insertAll(MainChannels... channels);

        @Insert
        void insertChannel(MainChannels channel);

        @Delete
        void delete(MainChannels channel);
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

//    Database update for caching channels info ( especially images )
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE channel ADD COLUMN icon TEXT");
            database.execSQL("CREATE TABLE mainchannels ("
            + "id INTEGER PRIMARY KEY NOT NULL,"
            + "name TEXT,"
            + "link TEXT,"
            + "icon TEXT)");
        }
    };

    @Database(entities={Channel.class, Program.class, MainChannels.class}, version = 3)
    public abstract static class DefaultDb extends RoomDatabase {
        public abstract ChannelsDao channelsDao();
        public abstract ProgramsDao programsDao();
        public abstract MainChannelsDao mainChannelsDao();
    }

}
