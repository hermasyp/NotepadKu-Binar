package com.catnip.notepadku.di

import android.content.Context
import com.catnip.notepadku.data.pref.UserPreference
import com.catnip.notepadku.data.pref.UserPreferenceDataSource
import com.catnip.notepadku.data.pref.UserPreferenceDataSourceImpl
import com.catnip.notepadku.data.repository.LocalRepository
import com.catnip.notepadku.data.repository.LocalRepositoryImpl
import com.catnip.notepadku.data.room.AppDatabase
import com.catnip.notepadku.data.room.dao.CategoriesDao
import com.catnip.notepadku.data.room.dao.NotesDao
import com.catnip.notepadku.data.room.datasource.CategoryDataSource
import com.catnip.notepadku.data.room.datasource.CategoryDataSourceImpl
import com.catnip.notepadku.data.room.datasource.NotesDataSource
import com.catnip.notepadku.data.room.datasource.NotesDataSourceImpl

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
object ServiceLocator {

    fun provideUserPreference(context: Context): UserPreference {
        return UserPreference(context)
    }

    fun provideAppDatabase(context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    fun provideNotesDao(context: Context): NotesDao {
        return provideAppDatabase(context).notesDao()
    }

    fun provideCategoryDao(context: Context): CategoriesDao {
        return provideAppDatabase(context).categoriesDao()
    }

    fun provideNotesDataSource(context: Context): NotesDataSource {
        return NotesDataSourceImpl(provideNotesDao(context))
    }

    fun provideCategoryDataSource(context: Context): CategoryDataSource {
        return CategoryDataSourceImpl(provideCategoryDao(context))
    }

    fun provideUserPreferenceDataSource(context: Context): UserPreferenceDataSource {
        return UserPreferenceDataSourceImpl(provideUserPreference(context))
    }

    fun provideLocalRepository(context: Context): LocalRepository {
        return LocalRepositoryImpl(
            provideUserPreferenceDataSource(context),
            provideNotesDataSource(context),
            provideCategoryDataSource(context)
        )
    }


}