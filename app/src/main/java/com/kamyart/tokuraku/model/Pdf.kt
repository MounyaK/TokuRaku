package com.kamyart.tokuraku.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "pdf_table",
        foreignKeys = [ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"]
        )]
)
class Pdf (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") private var id: Int,
    @ColumnInfo(name = "user_id") private var userId: Int,
    @ColumnInfo(name = "name") private var name: String,
    @ColumnInfo(name = "path") private var path: String,
    @ColumnInfo(name = "last_modified") private var lastModified: String, //date
    @ColumnInfo(name = "last_viewed_page") private var lastViewedPage: Int,
){
    //getters
    fun getId() = id
    fun getUserId() = userId
    fun getName() = name
    fun getPath() = path
    fun getLastModified() = lastModified
    fun getLastViewedPage() = lastViewedPage

    //setters
    fun setName(name: String){this.name = name}
    fun setPath(path: String){this.path = path}
    fun setLastModified(lastModified: String){this.lastModified = lastModified}
    fun setLastViewedPage(lastViewedPage: Int){this.lastViewedPage = lastViewedPage}
}