package com.catnip.notepadku.data.repository

import com.catnip.notepadku.data.pref.UserPreferenceDataSource
import com.catnip.notepadku.data.room.datasource.CategoryDataSource
import com.catnip.notepadku.data.room.datasource.NotesDataSource
import com.catnip.notepadku.data.room.entity.Category
import com.catnip.notepadku.data.room.entity.Note
import com.catnip.notepadku.data.room.model.NoteWithCategory
import com.catnip.notepadku.wrapper.Resource

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
interface LocalRepository {
    suspend fun getAllNotes(): Resource<List<NoteWithCategory>>
    suspend fun deleteNote(note: Note): Resource<Number>
    suspend fun updateNote(note: Note): Resource<Number>
    suspend fun insertNote(note: Note): Resource<Number>
    suspend fun getNoteById(id : Int): Resource<NoteWithCategory>
    suspend fun getAllCategories(): Resource<List<Category>>
}

class LocalRepositoryImpl(
    private val userPreferenceDataSource: UserPreferenceDataSource,
    private val notesDataSource: NotesDataSource,
    private val categoryDataSource: CategoryDataSource
) : LocalRepository {
    override suspend fun getAllNotes(): Resource<List<NoteWithCategory>> {
        return proceed { notesDataSource.getAllNotes() }
    }

    override suspend fun deleteNote(note: Note): Resource<Number> {
        return proceed { notesDataSource.deleteNote(note) }
    }

    override suspend fun updateNote(note: Note): Resource<Number> {
        return proceed { notesDataSource.updateNote(note) }
    }

    override suspend fun insertNote(note: Note): Resource<Number> {
        return proceed { notesDataSource.insertNote(note) }
    }

    override suspend fun getNoteById(id: Int): Resource<NoteWithCategory> {
        return proceed { notesDataSource.getNoteById(id) }
    }

    override suspend fun getAllCategories(): Resource<List<Category>> {
        return proceed { categoryDataSource.getAllCategory() }
    }

    private suspend fun <T> proceed(coroutine: suspend () -> T): Resource<T> {
        return try {
            Resource.Success(coroutine.invoke())
        } catch (exception: Exception) {
            Resource.Error(message = exception.message)
        }
    }
}