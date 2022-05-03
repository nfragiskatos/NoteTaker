package com.nfragiskatos.notetaker.feature_note.domain.use_case

import com.google.common.truth.Truth.assertThat
import com.nfragiskatos.notetaker.feature_note.data.repository.MockNoteRepository
import com.nfragiskatos.notetaker.feature_note.domain.model.Note
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class AddNoteUseCaseTest {
    private lateinit var addNoteUseCase: AddNoteUseCase
    private lateinit var mockRepository: MockNoteRepository

    @Before
    fun setUp() {
        mockRepository = MockNoteRepository()
        addNoteUseCase = AddNoteUseCase(mockRepository)

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

    @Test(expected = Note.InvalidNoteException::class)
    fun `Adding note with blank title throws InvalidNoteException`() = runBlocking {
        val noteToAdd = Note(
            id = 500,
            title = "    ",
            content = "content",
            timestamp = 100L,
            color = 1
        )
        addNoteUseCase(noteToAdd)
    }

    @Test(expected = Note.InvalidNoteException::class)
    fun `Adding note with blank content throws InvalidNoteException`() = runBlocking {
        val noteToAdd = Note(
            id = 500,
            title = "title",
            content = "    ",
            timestamp = 100L,
            color = 1
        )
        addNoteUseCase(noteToAdd)
    }

    @Test
    fun `Add note successfully`() = runBlocking {
        val noteToAdd = Note(
            id = 500,
            title = "title",
            content = "content",
            timestamp = 100L,
            color = 1
        )
        val beforeInsertSize = mockRepository.notes.size
        addNoteUseCase(noteToAdd)
        assertThat(mockRepository.getNoteById(noteToAdd.id!!)).isNotNull()
        assertThat(mockRepository.notes.size).isEqualTo(beforeInsertSize+1)
    }
}