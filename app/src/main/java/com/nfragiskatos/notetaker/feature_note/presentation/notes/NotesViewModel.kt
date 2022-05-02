package com.nfragiskatos.notetaker.feature_note.presentation.notes

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nfragiskatos.notetaker.feature_note.domain.model.Note
import com.nfragiskatos.notetaker.feature_note.domain.use_case.NoteUseCases
import com.nfragiskatos.notetaker.feature_note.domain.util.NoteOrderBy
import com.nfragiskatos.notetaker.feature_note.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _state = mutableStateOf(NotesState())
    val state = _state

    private var recentlyDeletedNote: Note? = null

    private var getNotesJob: Job? = null

    init {
        getNotes(NoteOrderBy.Date(OrderType.Descending))
    }

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    noteUseCases.deleteNoteUseCase(event.note)
                    recentlyDeletedNote = event.note
                }
            }
            is NotesEvent.Order -> {
                if (state.value.noteOrderBy::class == event.noteOrderBy::class
                    && state.value.noteOrderBy.orderType == event.noteOrderBy.orderType
                ) {
                    return
                }
                getNotes(event.noteOrderBy)
            }
            is NotesEvent.RestoreNote -> {
                viewModelScope.launch {
                    noteUseCases.addNoteUseCase(recentlyDeletedNote ?: return@launch)
                    recentlyDeletedNote = null
                }
            }
            is NotesEvent.ToggleOrderSectionVisible -> {
                _state.value =
                    state.value.copy(isOrderSectionVisible = !state.value.isOrderSectionVisible)
            }
        }
    }

    private fun getNotes(noteOrderBy: NoteOrderBy) {
        getNotesJob?.cancel()
        getNotesJob = noteUseCases.getNotesUseCase(noteOrderBy)
            .onEach { notes ->
                _state.value = state.value.copy(
                    notes = notes,
                    noteOrderBy = noteOrderBy
                )
            }
            .launchIn(viewModelScope)
    }

}