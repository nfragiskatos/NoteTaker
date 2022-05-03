package com.nfragiskatos.notetaker.feature_note.domain.use_case

import com.google.common.truth.Truth
import com.google.common.truth.Truth.*
import com.nfragiskatos.notetaker.feature_note.data.repository.MockNoteRepository
import com.nfragiskatos.notetaker.feature_note.domain.model.Note
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class DeleteNoteUseCaseTest {
    private lateinit var deleteNoteUseCase: DeleteNoteUseCase
    private lateinit var getNoteUseCase: GetNoteUseCase
    private lateinit var mockRepository: MockNoteRepository

    @Before
    fun setUp() {
        mockRepository = MockNoteRepository()
        deleteNoteUseCase = DeleteNoteUseCase(mockRepository)
        getNoteUseCase = GetNoteUseCase(mockRepository)

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
    fun `Delete note successfully`() = runBlocking {
        val noteToDelete = getNoteUseCase(5)

        assertThat(noteToDelete).isNotNull()

        val beforeDeleteSize = mockRepository.notes.size
        deleteNoteUseCase(noteToDelete!!)

        assertThat(mockRepository.notes.size).isEqualTo(beforeDeleteSize-1)
    }
}