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

    @Delete
    void delete(Pdf pdf);

    @Query("UPDATE pdf SET last_modified=date('now'), last_viewed_page=:page")
    void update(int page);

    @Query("SELECT * from pdf ORDER BY last_modified DESC")
    LiveData<List<Pdf>> getAllPdf();

    @Query("DELETE FROM pdf")
    void deleteAll();
}
