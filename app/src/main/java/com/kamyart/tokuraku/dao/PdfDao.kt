package com.kamyart.tokuraku.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kamyart.tokuraku.model.PageInfo
import com.kamyart.tokuraku.model.Pdf
import kotlinx.coroutines.flow.Flow

@Dao
interface PdfDao {

    @Query("SELECT * FROM pdf_table INNER JOIN user_table ON pdf_table.user_id == :user_id ORDER BY last_modified ASC")
    fun getAll(user_id: Int): Flow<List<Pdf>>

    @Query("SELECT * FROM page_info_table WHERE :pdf_id == page_info_table.pdf_id")
    fun getAllPageInfo(pdf_id: Int): Flow<List<PageInfo>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(pdf: Pdf)

    @Query("DELETE FROM pdf_table")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(pdf: Pdf)
}