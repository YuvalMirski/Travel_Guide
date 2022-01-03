package com.example.travel_guide.model;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.travel_guide.MyApplication;

@Database(entities = {UserPost.class}, version = 2)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract UserPostDao userPostDao();
}

public class AppLocalDB {
    static public AppLocalDbRepository db =
            Room.databaseBuilder(MyApplication.getContext(),
                    AppLocalDbRepository.class,
                    "dbFileName.db")
                    .fallbackToDestructiveMigration()
                    .build();
}
