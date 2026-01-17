package com.example.smartnotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.content.Intent;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

public class NoteListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private List<Note> noteList;
    private DatabaseHelper dbHelper;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        // 1. Inisialisasi Database dan List
        dbHelper = new DatabaseHelper(this);
        noteList = new ArrayList<>();

        // 2. Inisialisasi RecyclerView
        recyclerView = findViewById(R.id.rv_notes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 3. Inisialisasi Adapter (Kirim list dan context)
        noteAdapter = new NoteAdapter(noteList, this);
        recyclerView.setAdapter(noteAdapter);

        setupBottomNavigation();
    }

    // Fungsi untuk mengambil data terbaru dari database SQLite
    private void refreshData() {
        if (dbHelper != null) {
            noteList.clear();
            noteList.addAll(dbHelper.getAllNotes()); // Ambil data terbaru dari DB
            noteAdapter.notifyDataSetChanged(); // Beritahu adapter agar layar di-refresh
        }
    }

    private void setupBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                return true;
            } else if (itemId == R.id.nav_add) {
                startActivity(new Intent(this, CreateNoteActivity.class));
                return true;
            } else if (itemId == R.id.nav_settings) {
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // SANGAT PENTING: Mengambil data ulang saat pengguna kembali ke layar ini
        refreshData();

        // Memastikan ikon 'Home' terpilih kembali
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }
    }
}