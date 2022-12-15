package com.kamyart.tokuraku.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
class User (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") private val id: Int,
    @Ignore private val profile_picture: String,
    @ColumnInfo(name = "name") private val name: String,
    @ColumnInfo(name = "email") private val email: String,
)