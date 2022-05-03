package com.nfragiskatos.notetaker.feature_note.domain.use_case

import com.google.common.truth.Truth
import com.google.common.truth.Truth.*
import com.nfragiskatos.notetaker.feature_note.data.repository.MockNoteRepository
import com.nfragiskatos.notetaker.feature_note.domain.model.Note
import com.nfragiskatos.notetaker.feature_note.domain.util.NoteOrderBy
import com.nfragiskatos.notetaker.feature_note.domain.util.OrderType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetNotesUseCaseTest {

    private lateinit var getNoteUseCase: GetNotesUseCase
    private lateinit var mockRepository: MockNoteRepository

    @Before
    fun setUp() {
        mockRepository = MockNoteRepository()
        getNoteUseCase = GetNotesUseCase(mockRepository)

        val notes = mutableListOf<Note>()
        ('a'..'z').forEachIndexed { index, c ->
            notes.add(
                Note(
                    id = index,
                    title = c.toString(),
                    content = "Content for note $c",
                    timestamp = index.toLong(),
                    color = index
                )
            )
        }
        notes.shuffle()
        runBlocking {
            notes.forEach { mockRepository.insertNote(it) }
        }
    }

    @Test
    fun `Return notes by title in ascending order`() = runBlocking {

        val notes = getNoteUseCase(NoteOrderBy.Title(OrderType.Ascending)).first()

        for (i in 0..notes.size - 2) {
            assertThat(notes[i].title)
                .isLessThan(notes[i + 1].title)
        }
    }

    @Test
    fun `Return notes by title in descending order`() = runBlocking {

        val notes = getNoteUseCase(NoteOrderBy.Title(OrderType.Descending)).first()

        for (i in 0..notes.size - 2) {
            assertThat(notes[i].title)
                .isGreaterThan(notes[i + 1].title)
        }
    }

    @Test
    fun `Return notes by date in ascending order`() = runBlocking {

        val notes = getNoteUseCase(NoteOrderBy.Date(OrderType.Ascending)).first()

        for (i in 0..notes.size - 2) {
            assertThat(notes[i].timestamp)
                .isLessThan(notes[i + 1].timestamp)
        }
    }

    @Test
    fun `Return notes by date in descending order`() = runBlocking {

        val notes = getNoteUseCase(NoteOrderBy.Date(OrderType.Descending)).first()

        for (i in 0..notes.size - 2) {
            assertThat(notes[i].timestamp)
                .isGreaterThan(notes[i + 1].timestamp)
        }
    }

    @Test
    fun `Return notes by color in ascending order`() = runBlocking {

        val notes = getNoteUseCase(NoteOrderBy.Color(OrderType.Ascending)).first()

        for (i in 0..notes.size - 2) {
            assertThat(notes[i].color)
                .isLessThan(notes[i + 1].color)
        }
    }

    @Test
    fun `Return notes by color in descending order`() = runBlocking {

        val notes = getNoteUseCase(NoteOrderBy.Color(OrderType.Descending)).first()

        for (i in 0..notes.size - 2) {
            assertThat(notes[i].color)
                .isGreaterThan(notes[i + 1].color)
        }
    }
}