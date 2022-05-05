package com.nfragiskatos.notetaker.feature_note.presentation

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nfragiskatos.notetaker.core.util.TestTags
import com.nfragiskatos.notetaker.di.AppModule
import com.nfragiskatos.notetaker.feature_note.presentation.add_edit_notes.components.AddEditNoteScreen
import com.nfragiskatos.notetaker.feature_note.presentation.notes.components.NotesScreen
import com.nfragiskatos.notetaker.feature_note.presentation.util.Screen
import com.nfragiskatos.notetaker.ui.theme.NoteTakerTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class NotesEndToEndTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()

        composeRule.setContent {
            NoteTakerTheme() {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screen.NotesScreen.route
                ) {
                    composable(route = Screen.NotesScreen.route) {
                        NotesScreen(navController = navController)
                    }
                    composable(
                        route = Screen.AddEditNoteScreen.route
                                + "?noteId={noteId}&noteColor={noteColor}",
                        arguments = listOf(
                            navArgument(
                                name = "noteId"
                            ) {
                                type = NavType.IntType
                                defaultValue = -1
                            },
                            navArgument(
                                name = "noteColor"
                            ) {
                                type = NavType.IntType
                                defaultValue = -1
                            }
                        )
                    ) {
                        val color = it.arguments?.getInt("noteColor") ?: -1
                        AddEditNoteScreen(
                            navController = navController,
                            noteColor = color
                        )
                    }
                }
            }
        }
    }

    @Test
    fun saveNewNote_editAfterwards() {
        // NotesScreen
        // - Click on FAB to navigate to add note screen
        composeRule.onNodeWithContentDescription("Add Note")
            .performClick()

        // AddEditNotesScreen
        // - add input to title and content text fields
        // - save
        composeRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD)
            .performTextInput("TEST-TITLE")
        composeRule.onNodeWithTag(TestTags.CONTENT_TEXT_FIELD)
            .performTextInput("TEST-CONTENT")
        composeRule.onNodeWithContentDescription("Save Note")
            .performClick()

        // NotesScreen
        // - assert new node is displayed in the list
        // - assert the title and content is correct.
        // - click on node to navigate to the edit screen
        composeRule.onNodeWithText("TEST-TITLE")
            .assertIsDisplayed()
        composeRule.onNodeWithText("TEST-CONTENT")
            .assertIsDisplayed()
        composeRule.onNodeWithText("TEST-TITLE")
            .performClick()

        // AddEditNotesScreen
        // - assert edit screen has the correct title and content
        // - edit the title by appending to it
        // - save
        composeRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD)
            .assertTextEquals("TEST-TITLE")
        composeRule.onNodeWithTag(TestTags.CONTENT_TEXT_FIELD)
            .assertTextEquals("TEST-CONTENT")
        composeRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD)
            .performTextInput("2")
        composeRule.onNodeWithContentDescription("Save Note")
            .performClick()

        // NotesScreen
        // - ensure the node is displayed in the list with the new title
        composeRule.onNodeWithText("TEST-TITLE2")
            .assertIsDisplayed()
    }
}