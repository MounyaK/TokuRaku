package com.kamyart.tokuraku.dao

import androidx.room.*
import com.kamyart.tokuraku.model.Word

@Dao
interface WordDao {

    @Query("SELECT * FROM word_table ORDER BY id ASC")
    fun getAll(): List<Word>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: Word)

    @Query("DELETE FROM word_table")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(word: Word)

}