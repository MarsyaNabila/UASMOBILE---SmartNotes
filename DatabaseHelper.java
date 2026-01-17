package com.example.smartnotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Konfigurasi Database
    private static final String DB_NAME = "SmartNotesDB";
    private static final int DB_VERSION = 1;

    // Konfigurasi Tabel
    private static final String TABLE_NAME = "notes";
    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_CONTENT = "content";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Membuat tabel saat aplikasi pertama kali diinstal
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TITLE + " TEXT, "
                + COL_CONTENT + " TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Menghapus tabel lama jika ada pembaruan versi database
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // ---------------------------------------------------------
    // 1. FUNGSI SIMPAN (CREATE)
    // ---------------------------------------------------------
    public void addNote(String title, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, title);
        values.put(COL_CONTENT, content);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // ---------------------------------------------------------
    // 2. FUNGSI BACA SEMUA (READ)
    // ---------------------------------------------------------
    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Mengambil data, diurutkan dari yang terbaru (ID terbesar di atas)
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + COL_ID + " DESC", null);

        if (cursor.moveToFirst()) {
            do {
                // Pastikan class Note kamu punya constructor (int id, String title, String content)
                notes.add(new Note(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return notes;
    }

    // ---------------------------------------------------------
    // 3. FUNGSI UBAH (UPDATE)
    // ---------------------------------------------------------
    public void updateNote(int id, String title, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, title);
        values.put(COL_CONTENT, content);

        // Update baris yang ID-nya cocok
        db.update(TABLE_NAME, values, COL_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // ---------------------------------------------------------
    // 4. FUNGSI HAPUS (DELETE)
    // ---------------------------------------------------------
    public void deleteNote(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Hapus baris yang ID-nya cocok
        db.delete(TABLE_NAME, COL_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}