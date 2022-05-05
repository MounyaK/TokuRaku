package com.tokuraku.models;

public class PageInfo {
    private long id;
    private int pdfId;
    private int pageNumber;

    public PageInfo(int id, int pdfId, int pageNumber) {

        this.pdfId = pdfId;
        this.pageNumber = pageNumber;
    }
}
