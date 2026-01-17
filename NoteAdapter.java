package com.example.smartnotes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<Note> noteList;
    private Context context;

    public NoteAdapter(List<Note> noteList, Context context) {
        this.noteList = noteList;
        this.context = context;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Sesuaikan nama layout kamu (item_note_preview atau item_note)
        View view = LayoutInflater.from(context).inflate(R.layout.item_note_preview, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.titleTextView.setText(note.getTitle());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ViewNoteActivity.class);

            // --- PERBAIKAN: WAJIB KIRIM ID ---
            intent.putExtra("NOTE_ID", note.getId());
            intent.putExtra("NOTE_TITLE", note.getTitle());
            intent.putExtra("NOTE_CONTENT", note.getContent());

            context.startActivity(intent);

            Toast.makeText(context, "Membuka: " + note.getTitle(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.tv_note_title);
        }
    }
}