package com.nfragiskatos.notetaker.feature_note.domain.use_case

import com.nfragiskatos.notetaker.feature_note.domain.model.Note
import com.nfragiskatos.notetaker.feature_note.domain.repository.NoteRepository
import com.nfragiskatos.notetaker.feature_note.domain.util.NoteOrderBy
import com.nfragiskatos.notetaker.feature_note.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNotesUseCase(
    private val repository: NoteRepository
) {
    operator fun invoke(
        noteOrderBy: NoteOrderBy = NoteOrderBy.Date(OrderType.Descending)
    ) : Flow<List<Note>> {
        return repository.getNotes().map {notes ->
            when(noteOrderBy.orderType) {
                is OrderType.Ascending -> {
                    when (noteOrderBy) {
                        is NoteOrderBy.Title -> {
                            notes.sortedBy { it.title.lowercase() }
                        }
                        is NoteOrderBy.Date -> {
                            notes.sortedBy { it.timestamp }
                        }
                        is NoteOrderBy.Color -> {
                            notes.sortedBy { it.color }
                        }
                    }
                }
                is OrderType.Descending -> {
                    when (noteOrderBy) {
                        is NoteOrderBy.Title -> {
                            notes.sortedByDescending { it.title.lowercase() }
                        }
                        is NoteOrderBy.Date -> {
                            notes.sortedByDescending { it.timestamp }
                        }
                        is NoteOrderBy.Color -> {
                            notes.sortedByDescending { it.color }
                        }
                    }
                }
            }
        }
    }
}