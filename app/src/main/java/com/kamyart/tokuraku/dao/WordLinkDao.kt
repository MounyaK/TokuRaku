package com.kamyart.tokuraku.dao

import androidx.room.*
import com.kamyart.tokuraku.model.WordLink


@Dao
interface WordLinkDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(wordLink: WordLink)

    @Query("DELETE FROM word_link_table")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(wordLink: WordLink)

}
