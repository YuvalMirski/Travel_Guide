package com.example.travel_guide.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.travel_guide.MyApplication;

import java.util.List;

@Dao
public interface
UserPostDao {
    @Query("select * from UserPost")
    List<UserPost> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(UserPost... userPosts);

    @Delete
    void delete(UserPost userPost) ;
}
