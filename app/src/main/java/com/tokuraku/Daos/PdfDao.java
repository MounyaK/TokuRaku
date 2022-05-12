package com.tokuraku.Daos;
import com.tokuraku.models.Pdf;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PdfDao {
    //Add new Pdf file to database
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(Pdf pdf);

    @Query("DELETE FROM pdf WHERE path=:path")
    void delete(String path);

    @Query("UPDATE pdf SET last_modified=date('now'), last_viewed_page=:page WHERE path=:path")
    void update(int page,String path);

    @Query("SELECT * from pdf ORDER BY last_modified DESC")
    LiveData<List<Pdf>> getAllPdf();

    @Query("DELETE FROM pdf")
    void deleteAll();
}
