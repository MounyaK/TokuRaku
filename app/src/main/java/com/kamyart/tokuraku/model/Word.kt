package com.kamyart.tokuraku.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word_table")
class Word (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") private var id: Int,
    @ColumnInfo(name = "kanji") private var kanji: String,
    @ColumnInfo(name = "furigana") private var furigana: String,
    @ColumnInfo(name = "translation") private var translation: ArrayList<String>,
    @ColumnInfo(name = "examples") private var examples: Map<String, String>,

    ){
    //getters
    fun getId() = id
    fun getKanji() = kanji
    fun getFurigana() = furigana
    fun getTranslation() = translation
    fun getExamples() = examples

    //setters
    fun setKanji(kanji: String){this.kanji = kanji}
    fun setFurigana(furigana: String){this.furigana = furigana}
    fun setTranslation(translation: ArrayList<String>){this.translation = translation}
    fun examples(examples: Map<String, String>){this.examples = examples}
}
