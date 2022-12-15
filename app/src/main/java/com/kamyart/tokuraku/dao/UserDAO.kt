package com.kamyart.tokuraku.dao

import androidx.room.*
import com.kamyart.tokuraku.model.User

@Dao
interface UserDAO {

    @Query("SELECT * FROM user_table ORDER BY id ASC")
    fun getAll(): List<User>

    @Query("SELECT * FROM user_table WHERE :email == user_table.email ORDER BY id ASC")
    fun get(email: String): User


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    @Query("DELETE FROM pdf_table")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(user: User)
}