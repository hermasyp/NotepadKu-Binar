package com.catnip.notepadku.data.room.dao

import androidx.room.*
import com.catnip.notepadku.data.room.entity.Note
import com.catnip.notepadku.data.room.model.NoteWithCategory

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
@Dao
interface NotesDao {

    @Query("SELECT * FROM NOTES")
    suspend fun getAllNotes(): List<NoteWithCategory>

    @Query("SELECT * FROM NOTES WHERE category_id == :categoryId")
    suspend fun getNotesByCategoryId(categoryId: Int): List<NoteWithCategory>

    @Query("SELECT * FROM NOTES WHERE id == :id")
    suspend fun getNoteById(id: Int): NoteWithCategory

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(note: List<Note>)

    @Delete
    suspend fun deleteNote(note : Note) : Int

    @Update
    suspend fun updateNote(note : Note) : Int

}