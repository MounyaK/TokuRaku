package com.kamyart.tokuraku.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word_table")
class Word (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") private val id: Int,
    @ColumnInfo(name = "kanji") private val kanji: String,
    @ColumnInfo(name = "furigana") private val furigana: String,
    @ColumnInfo(name = "translation") private val translation: Array<String>,
    @ColumnInfo(name = "examples") private val examples: Map<String, String>,
)
