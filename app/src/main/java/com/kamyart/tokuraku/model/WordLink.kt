package com.kamyart.tokuraku.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word_link_table")
class WordLink (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") private val id: Int,
    @ColumnInfo(name = "word_id") private val word_id: Int,
    @ColumnInfo(name = "page_info_id") private val page_info_id: Int,
    @ColumnInfo(name = "last_modified") private val last_modified: String, //date
)