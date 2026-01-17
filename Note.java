package com.example.smartnotes;

public class Note {
    private int id;
    private String title;
    private String content;

    // Constructor 1: Digunakan saat mengambil data dari Database (Lengkap dengan ID)
    public Note(int id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    // Constructor 2: Digunakan saat ingin membuat catatan baru (ID belum ada)
    public Note(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // --- GETTER METHODS ---
    // Digunakan untuk mengambil nilai data

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    // --- SETTER METHODS ---
    // Sangat penting untuk proses pembaruan data (Edit)

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }
}