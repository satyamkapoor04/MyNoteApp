package com.example.root.mynoteapp;

public class Notes {

    public static final String TABLE_NAME = "notes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + COLUMN_ID +
            " INTEGER PRIMARY KEY, " + COLUMN_NOTE + " TEXT, " + COLUMN_TIMESTAMP +
            " DATETIME DEFAULT CURRENT_TIMESTAMP)";

    private int id;
    private String note;
    private String timestamp;

    public Notes (int id, String note, String timestamp) {
        this.id = id;
        this.note = note;
        this.timestamp = timestamp;
    }

    public int getId () {
        return this.id;
    }

    public String getNote () {
        return this.note;
    }

    public String getTimestamp () {
        return this.timestamp;
    }

    public void setNote (String note) {
        this.note = note;
    }
}
