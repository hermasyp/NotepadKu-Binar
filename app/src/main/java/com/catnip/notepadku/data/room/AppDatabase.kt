package com.catnip.notepadku.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.RoomDatabase.Callback
import androidx.sqlite.db.SupportSQLiteDatabase
import com.catnip.notepadku.data.room.AppDatabase.Companion.getInstance
import com.catnip.notepadku.data.room.dao.CategoriesDao
import com.catnip.notepadku.data.room.dao.NotesDao
import com.catnip.notepadku.data.room.entity.Category
import com.catnip.notepadku.data.room.entity.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
@Database(entities = [Note::class, Category::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notesDao(): NotesDao
    abstract fun categoriesDao(): CategoriesDao

    companion object {
        private const val DB_NAME = "NotepadKu.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseSeederCallback(context))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}

class DatabaseSeederCallback(private val context: Context) : Callback() {
    private val job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        scope.launch {
            getInstance(context).categoriesDao().insertCategories(prepopulateCategory())
            getInstance(context).notesDao().insertNotes(prepopulateNotes())
        }
    }

    private fun prepopulateCategory(): List<Category> {
        return mutableListOf(
            Category(categoryId = 1, categoryName = "Important", hexCategoryColor = "#ffb3ba"),
            Category(categoryId = 2, categoryName = "Private", hexCategoryColor = "#bae1ff"),
            Category(categoryId = 3, categoryName = "Daily", hexCategoryColor = "#baffc9"),
        )
    }

    private fun prepopulateNotes(): List<Note> {
        return mutableListOf(
            Note(categoryId = 1, title = "My Diary 1", body = "Loren ipsum set dolot almet"),
            Note(categoryId = 1, title = "My Diary 2", body = "Loren ipsum set dolot almet"),
            Note(categoryId = 2, title = "My Diary 3", body = "Loren ipsum set dolot almet"),
            Note(categoryId = 2, title = "My Diary 4", body = "Loren ipsum set dolot almet"),
            Note(categoryId = 3, title = "My Diary 5", body = "Loren ipsum set dolot almet"),
            Note(categoryId = 3, title = "My Diary 6", body = "Loren ipsum set dolot almet"),
        )
    }
}