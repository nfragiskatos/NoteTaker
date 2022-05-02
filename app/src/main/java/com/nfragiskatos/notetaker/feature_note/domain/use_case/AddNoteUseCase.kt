package com.nfragiskatos.notetaker.feature_note.domain.use_case

import com.nfragiskatos.notetaker.feature_note.domain.model.Note
import com.nfragiskatos.notetaker.feature_note.domain.repository.NoteRepository

class AddNoteUseCase(
    private val repository: NoteRepository
) {

    @Throws(Note.InvalidNoteException::class)
    suspend operator fun invoke(note: Note) {
        if (note.title.isBlank()) {
            throw Note.InvalidNoteException("The title of the note can't be empty.")
        }
        if (note.content.isBlank()) {
            throw Note.InvalidNoteException("The content of the note can't be empty.")
        }
        repository.insertNote(note)
    }
}