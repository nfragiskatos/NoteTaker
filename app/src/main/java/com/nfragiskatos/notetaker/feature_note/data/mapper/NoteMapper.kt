package com.nfragiskatos.notetaker.feature_note.data.mapper

import com.nfragiskatos.notetaker.feature_note.data.data_source.local.NoteEntity
import com.nfragiskatos.notetaker.feature_note.domain.model.Note

fun NoteEntity.toNote(): Note {
    return Note(
        id = id,
        title = title,
        content = content,
        timestamp = timestamp,
        color = color
    )
}

fun Note.toNoteEntity(): NoteEntity {
    return NoteEntity(
        id = id,
        title = title,
        content = content,
        timestamp = timestamp,
        color = color
    )
}