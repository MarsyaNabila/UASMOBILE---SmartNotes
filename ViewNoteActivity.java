package com.example.smartnotes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ViewNoteActivity extends AppCompatActivity {

    private TextView tvTitleHeader, tvNoteContent;
    private DatabaseHelper dbHelper;
    private int noteId;
    private String currentTitle, currentContent; // Simpan ke variabel agar mudah dikirim kembali

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        dbHelper = new DatabaseHelper(this);

        // 1. Inisialisasi View
        tvTitleHeader = findViewById(R.id.tv_title_header);
        tvNoteContent = findViewById(R.id.tv_note_content);
        Button btnEdit = findViewById(R.id.btn_edit_note);
        Button btnDelete = findViewById(R.id.btn_delete_note);

        // 2. MENANGKAP DATA DARI INTENT
        noteId = getIntent().getIntExtra("NOTE_ID", -1);
        currentTitle = getIntent().getStringExtra("NOTE_TITLE");
        currentContent = getIntent().getStringExtra("NOTE_CONTENT");

        // 3. MENAMPILKAN DATA
        tvTitleHeader.setText(currentTitle != null ? currentTitle : "Tanpa Judul");
        tvNoteContent.setText(currentContent != null ? currentContent : "");

        // 4. Logika Tombol Edit
        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(ViewNoteActivity.this, CreateNoteActivity.class);
            intent.putExtra("IS_EDIT", true);
            intent.putExtra("NOTE_ID", noteId);
            intent.putExtra("NOTE_TITLE", currentTitle);
            intent.putExtra("NOTE_CONTENT", currentContent);
            startActivity(intent);
            finish();
        });

        // 5. Logika Tombol Hapus
        btnDelete.setOnClickListener(v -> {
            if (noteId != -1) {
                dbHelper.deleteNote(noteId);
                Toast.makeText(this, "Catatan Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        BottomNavigationView nav = findViewById(R.id.bottom_navigation);
        // Pastikan tidak ada item yang terpilih secara otomatis saat melihat catatan
        nav.getMenu().setGroupCheckable(0, true, false);
        for (int i = 0; i < nav.getMenu().size(); i++) {
            nav.getMenu().getItem(i).setChecked(false);
        }

        nav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                finish();
                return true;
            } else if (itemId == R.id.nav_add) {
                startActivity(new Intent(this, CreateNoteActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_settings) {
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            }
            return false;
        });
    }
}