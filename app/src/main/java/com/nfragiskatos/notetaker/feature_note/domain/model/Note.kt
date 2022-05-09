package com.nfragiskatos.notetaker.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nfragiskatos.notetaker.ui.theme.*
import java.lang.Exception


data class Note(
    val id: Int?,
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Int
) {
    companion object {
        val noteColors = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink)
    }

    class InvalidNoteException(message: String) : Exception(message)
}
