package com.example.smartnotes;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateNoteActivity extends AppCompatActivity {

    private EditText etTitle, etContent;
    private Button btnSave;
    private DatabaseHelper dbHelper;
    private int noteId = -1;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        dbHelper = new DatabaseHelper(this);
        etTitle = findViewById(R.id.et_note_title);
        etContent = findViewById(R.id.et_note_content);
        btnSave = findViewById(R.id.btn_save_note);

        // CEK APAKAH DATA ID MASUK
        if (getIntent().hasExtra("IS_EDIT")) {
            isEditMode = true;
            noteId = getIntent().getIntExtra("NOTE_ID", -1);
            etTitle.setText(getIntent().getStringExtra("NOTE_TITLE"));
            etContent.setText(getIntent().getStringExtra("NOTE_CONTENT"));
            btnSave.setText("PERBARUI CATATAN");
        }

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString();
            String content = etContent.getText().toString();

            if (isEditMode) {
                // PROSES UPDATE
                dbHelper.updateNote(noteId, title, content);
                Toast.makeText(this, "Berhasil Diperbarui", Toast.LENGTH_SHORT).show();
            } else {
                // PROSES SIMPAN BARU
                dbHelper.addNote(title, content);
                Toast.makeText(this, "Berhasil Disimpan", Toast.LENGTH_SHORT).show();
            }
            finish(); // Kembali ke NoteListActivity
        });
    }
}