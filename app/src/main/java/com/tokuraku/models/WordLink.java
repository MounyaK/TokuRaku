package com.tokuraku.models;

public class WordLink {
    private long id;
    private int wordId;
    private int PageId;
    private int frequency;

    public WordLink(int id, int wordId, int pageId, int frequency) {

        this.wordId = wordId;
        PageId = pageId;
        this.frequency = frequency;
    }
}
