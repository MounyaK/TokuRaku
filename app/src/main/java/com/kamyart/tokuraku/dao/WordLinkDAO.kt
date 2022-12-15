package com.kamyart.tokuraku.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kamyart.tokuraku.model.WordLink
import kotlinx.coroutines.flow.Flow


@Dao
interface WordLinkDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(wordLink: WordLink)

}
