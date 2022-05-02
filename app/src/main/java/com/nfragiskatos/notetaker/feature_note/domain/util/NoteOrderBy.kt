package com.nfragiskatos.notetaker.feature_note.domain.util

sealed class NoteOrderBy(val orderType: OrderType) {
    class Title(orderType: OrderType) : NoteOrderBy(orderType)
    class Date(orderType: OrderType) : NoteOrderBy(orderType)
    class Color(orderType: OrderType) : NoteOrderBy(orderType)

}