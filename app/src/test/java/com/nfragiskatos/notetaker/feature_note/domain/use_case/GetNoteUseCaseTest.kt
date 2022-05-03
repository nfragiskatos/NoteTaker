package com.nfragiskatos.notetaker.feature_note.domain.use_case

import com.google.common.truth.Truth
import com.google.common.truth.Truth.*
import com.nfragiskatos.notetaker.feature_note.data.repository.MockNoteRepository
import com.nfragiskatos.notetaker.feature_note.domain.model.Note
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GetNoteUseCaseTest {
    private lateinit var getNoteUseCase: GetNoteUseCase
    private lateinit var mockRepository: MockNoteRepository

    @Before
    fun setUp() {
        mockRepository = MockNoteRepository()
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
    fun `Get existing note successfully`() = runBlocking {

        val note = getNoteUseCase(5)
        assertThat(note).isNotNull()
    }

    @Test
    fun `Get note that does not exist`() = runBlocking {
        val note = getNoteUseCase(500)
        assertThat(note).isNull()
    }
}