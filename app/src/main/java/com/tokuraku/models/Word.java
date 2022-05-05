package com.tokuraku.models;

public class Word {
    private long    id;
    private String kanji;
    private String furigana;
    private String translation;
    private String ex_jp;
    private String ex_eng;

    public Word(String kanji, String furigana, String translation, String ex_jp, String ex_eng) {

        this.kanji = kanji;
        this.furigana = furigana;
        this.translation = translation;
        this.ex_jp = ex_jp;
        this.ex_eng = ex_eng;
    }
}
