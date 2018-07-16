package com.example.root.mynoteapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "my_db";
    public static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL (Notes.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL ("DROP TABLE IF EXISTS " + Notes.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public long insert (String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put (Notes.COLUMN_NOTE,note);
        long id = db.insert (Notes.TABLE_NAME,null,values);
        db.close();
        return id;
    }

    public Notes getNotes (long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query (Notes.TABLE_NAME,new String[]{Notes.COLUMN_ID,Notes.COLUMN_NOTE,Notes.COLUMN_TIMESTAMP},
                Notes.COLUMN_ID + "=?", new String[] {String.valueOf(id)},null,null,null,null);

        if (cursor != null)
            cursor.moveToFirst();
        Notes note = new Notes(cursor.getInt(cursor.getColumnIndex(Notes.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(Notes.COLUMN_NOTE)),
                    cursor.getString(cursor.getColumnIndex(Notes.COLUMN_TIMESTAMP)));
        cursor.close();
        return note;
    }

    public List<Notes> getAllNotes () {
        List<Notes> list = new ArrayList<>();
        String notesQuery = "SELECT * FROM " + Notes.TABLE_NAME + " ORDER BY " + Notes.COLUMN_TIMESTAMP + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(notesQuery,null);
        if (cursor.moveToFirst()) {
            do {
                Notes notes = new Notes (cursor.getInt(cursor.getColumnIndex(Notes.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(Notes.COLUMN_NOTE)),
                        cursor.getString(cursor.getColumnIndex(Notes.COLUMN_TIMESTAMP)));
                list.add(notes);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public int getNotesCount () {
        SQLiteDatabase db = this.getReadableDatabase();
        String rawQuery = "SELECT * FROM " + Notes.TABLE_NAME;
        Cursor cursor = db.rawQuery(rawQuery,null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public long updateNote (Notes notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Notes.COLUMN_NOTE,notes.getNote());
        return db.update (Notes.TABLE_NAME,values,Notes.COLUMN_ID + "=?",new String[] {String.valueOf(notes.getId())});
    }

    public void deleteNote (Notes notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + Notes.TABLE_NAME + " WHERE _id=" + String.valueOf(notes.getId()));
        db.close();
    }
}
