package com.nfragiskatos.notetaker.feature_note.presentation.notes

import com.nfragiskatos.notetaker.feature_note.domain.model.Note
import com.nfragiskatos.notetaker.feature_note.domain.util.NoteOrderBy

sealed class NotesEvent {
    data class Order(val noteOrderBy: NoteOrderBy) : NotesEvent()
    data class DeleteNote(val note: Note) : NotesEvent()
    object RestoreNote : NotesEvent()
    object ToggleOrderSectionVisible : NotesEvent()
}
