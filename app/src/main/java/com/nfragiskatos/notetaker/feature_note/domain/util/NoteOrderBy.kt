package com.nfragiskatos.notetaker.feature_note.domain.util

sealed class NoteOrderBy(val orderType: OrderType) {
    class Title(orderType: OrderType) : NoteOrderBy(orderType)
    class Date(orderType: OrderType) : NoteOrderBy(orderType)
    class Color(orderType: OrderType) : NoteOrderBy(orderType)

    fun copy(orderType: OrderType): NoteOrderBy {
        return when (this) {
            is Color -> Color(orderType)
            is Date -> Date(orderType)
            is Title -> Title(orderType)
        }
    }
}