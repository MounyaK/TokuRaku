package com.kamyart.tokuraku.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kamyart.tokuraku.model.PageInfo
import com.kamyart.tokuraku.model.Word
import kotlinx.coroutines.flow.Flow

@Dao
interface PageInfoDAO {

    @Query("SELECT * FROM page_info_table ORDER BY pdf_id ASC")
    fun getAll(): List<PageInfo>

    @Query("SELECT * FROM word_table INNER JOIN word_link_table ON :current_page_info_id == word_link_table.page_info_id WHERE word_table.id == word_link_table.word_id")
    fun getWords(current_page_info_id: Int): Flow<List<Word>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(pageInfo: PageInfo)

    @Query("DELETE FROM page_info_table WHERE page_info_table.pdf_id == :pdf_id")
    suspend fun deleteAll(pdf_id:Int)

}