package com.nfragiskatos.notetaker.feature_note.data.repository

import com.nfragiskatos.notetaker.feature_note.data.data_source.local.NoteDao
import com.nfragiskatos.notetaker.feature_note.data.mapper.toNote
import com.nfragiskatos.notetaker.feature_note.data.mapper.toNoteEntity
import com.nfragiskatos.notetaker.feature_note.domain.model.Note
import com.nfragiskatos.notetaker.feature_note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteRepositoryImpl(
    private val dao: NoteDao
) : NoteRepository {
    override fun getNotes(): Flow<List<Note>> {
        return dao.getNotes()
            .map { notes -> notes.map { note -> note.toNote() } }
    }

    override suspend fun getNoteById(id: Int): Note? {
        return dao.getNoteById(id)
            ?.toNote()
    }

    override suspend fun insertNote(note: Note) {
        dao.insertNote(note.toNoteEntity())
    }

    override suspend fun deleteNote(note: Note) {
        dao.deleteNote(note.toNoteEntity())
    }
}