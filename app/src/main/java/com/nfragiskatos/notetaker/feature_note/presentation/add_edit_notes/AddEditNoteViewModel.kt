package com.nfragiskatos.notetaker.feature_note.presentation.add_edit_notes

import androidx.lifecycle.ViewModel
import com.nfragiskatos.notetaker.feature_note.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val useCases: NoteUseCases
) : ViewModel() {

}