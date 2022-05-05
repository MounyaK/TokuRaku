package com.tokuraku.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.File;
import java.sql.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity(tableName = "pdf", indices = {@Index(value = "path", unique = true)})
public class Pdf {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "last_modified", defaultValue = "CURRENT_TIMESTAMP")
    private Date lastModified;

    @ColumnInfo(name = "last_viewed_page", defaultValue = "1")
    private int lastViewedPage;

    private String path;

    private String name;

    public Pdf(String path) {
        this.name = new File(path).getName();
        this.path = path;
    }

    public long getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public int getLastViewedPage() {
        return lastViewedPage;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public void setLastViewedPage(int lastViewedPage) {
        this.lastViewedPage = lastViewedPage;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setName(String name) {
        this.name = name;
    }
}
