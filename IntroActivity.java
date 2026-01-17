package com.example.smartnotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 1. Hubungkan layout activity_intro.xml dengan file Java ini
        setContentView(R.layout.activity_intro);

        // 2. Temukan tombol berdasarkan ID-nya dari layout
        Button startButton = findViewById(R.id.btn_start_now);

        // 3. Atur listener untuk menangani klik pada tombol
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Panggil method untuk memulai activity baru
                openNoteList();
            }
        });
    }

    // Method untuk berpindah ke NoteListActivity
    private void openNoteList() {
        // Buat Intent untuk berpindah dari IntroActivity ke NoteListActivity
        Intent intent = new Intent(IntroActivity.this, NoteListActivity.class);
        startActivity(intent);

        // (Opsional) Tutup IntroActivity agar tidak bisa kembali dengan tombol back
        finish();
    }
}
