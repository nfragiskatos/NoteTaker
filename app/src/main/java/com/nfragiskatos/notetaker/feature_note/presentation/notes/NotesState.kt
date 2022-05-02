package com.nfragiskatos.notetaker.feature_note.presentation.notes

import com.nfragiskatos.notetaker.feature_note.domain.model.Note
import com.nfragiskatos.notetaker.feature_note.domain.util.NoteOrderBy
import com.nfragiskatos.notetaker.feature_note.domain.util.OrderType

data class NotesState(
    val notes: List<Note> = emptyList(),
    val noteOrderBy: NoteOrderBy = NoteOrderBy.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)