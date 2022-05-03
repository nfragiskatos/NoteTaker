package com.nfragiskatos.notetaker.feature_note.presentation.add_edit_notes

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nfragiskatos.notetaker.feature_note.domain.model.Note
import com.nfragiskatos.notetaker.feature_note.domain.use_case.NoteUseCases
import com.nfragiskatos.notetaker.feature_note.presentation.add_edit_notes.AddEditNoteEvent.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _noteTitle = mutableStateOf(NoteTextFieldState(hint = "Enter title..."))
    val noteTitle = _noteTitle

    private val _noteContent = mutableStateOf(NoteTextFieldState(hint = "Enter content..."))
    val noteContent = _noteContent

    private val _noteColor = mutableStateOf(
        Note.noteColors.random()
            .toArgb()
    )
    val noteColor = _noteColor

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNoteId: Int? = null

    init {
        savedStateHandle.get<Int>("noteId")
            ?.let { noteId ->
                if (noteId != -1) {
                    viewModelScope.launch {
                        noteUseCases.getNoteUseCase(noteId)
                            ?.also { note ->
                                currentNoteId = note.id
                                _noteTitle.value = noteTitle.value.copy(
                                    text = note.title,
                                    isHintVisible = false
                                )
                                _noteContent.value = noteContent.value.copy(
                                    text = note.content,
                                    isHintVisible = false
                                )
                                _noteColor.value = note.color
                            }
                    }
                }

            }
    }

    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is ChangeColor -> {
                _noteColor.value = event.color
            }
            is ChangeContentFocus -> {
                _noteContent.value = noteContent.value.copy(
                    isHintVisible = !event.focusState.isFocused && noteContent.value.text.isBlank()
                )
            }
            is ChangeTitleFocus -> {
                _noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused && noteTitle.value.text.isBlank()
                )
            }
            is EnteredContent -> {
                _noteContent.value = noteContent.value.copy(text = event.value)
            }
            is EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(text = event.value)
            }
            SaveNote -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.addNoteUseCase(
                            Note(
                                title = noteTitle.value.text,
                                content = noteContent.value.text,
                                timestamp = System.currentTimeMillis(),
                                color = noteColor.value,
                                id = currentNoteId
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch (e: Note.InvalidNoteException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save note"
                            )
                        )
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveNote : UiEvent()
    }
}