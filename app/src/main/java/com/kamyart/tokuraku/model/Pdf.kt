package com.kamyart.tokuraku.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pdf_table")
class Pdf (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") private val id: Int,
    @ColumnInfo(name = "user_id") private val user_id: Int,
    @ColumnInfo(name = "name") private val name: String,
    @ColumnInfo(name = "path") private val path: String,
    @ColumnInfo(name = "last_modified") private val last_modified: String, //date
    @ColumnInfo(name = "last_viewed_page") private val last_viewed_page: Int,
)