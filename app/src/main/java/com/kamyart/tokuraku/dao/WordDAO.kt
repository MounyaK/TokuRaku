package com.kamyart.tokuraku.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kamyart.tokuraku.model.PageInfo
import com.kamyart.tokuraku.model.Word

@Dao
interface WordDAO {

    @Query("SELECT * FROM word_table ORDER BY id ASC")
    fun getAll(): List<Word>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: Word)

}