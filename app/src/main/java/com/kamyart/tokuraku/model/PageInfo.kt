package com.kamyart.tokuraku.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "page_info_table",
        foreignKeys = [ForeignKey(
            entity = Pdf::class,
            parentColumns = ["id"],
            childColumns = ["pdf_id"]
        )]
)
class PageInfo(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") private var id: Int,
    @ColumnInfo(name = "pdf_id") private var pdfId: Int,
    @ColumnInfo(name = "page_number") private var pageNumber: Int,
){
    //getters
    fun getId() = id
    fun getPdfId() = pdfId
    fun getPageNumber() = pageNumber

    //setters
    fun setPageNumber(nb: Int){pageNumber = nb}


}

