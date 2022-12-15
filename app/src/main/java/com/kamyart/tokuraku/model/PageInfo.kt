package com.kamyart.tokuraku.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "page_info_table")
class PageInfo(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") private val id: Int,
    @ColumnInfo(name = "pdf_id") private val pdf_id: Int,
    @ColumnInfo(name = "page_number") private val page_number: Int,

)

