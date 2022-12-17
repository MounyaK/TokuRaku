package com.kamyart.tokuraku.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "word_link_table",
        foreignKeys = [ForeignKey(
            entity = Word::class,
            parentColumns = ["id"],
            childColumns = ["word_id"]
        ),
        ForeignKey(
            entity = PageInfo::class,
            parentColumns = ["id"],
            childColumns = ["page_info_id"]
        )
        ]
)
class WordLink (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") private var id: Int,
    @ColumnInfo(name = "word_id") private var wordId: Int,
    @ColumnInfo(name = "page_info_id") private var pageInfoId: Int,
    @ColumnInfo(name = "last_modified") private var lastModified: String, //date
){
    //getters
    fun getId() = id
    fun getWordId() = wordId
    fun getPageInfoId() = pageInfoId
    fun getLastModified() = lastModified

    //setters
    fun setPageInfoId(pageInfoId: Int){this.pageInfoId = pageInfoId}
    fun setLastModified(lastModified: String){this.lastModified = lastModified}
}