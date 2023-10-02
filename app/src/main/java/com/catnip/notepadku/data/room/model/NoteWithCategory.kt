package com.catnip.notepadku.data.room.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import com.catnip.notepadku.data.room.entity.Category
import com.catnip.notepadku.data.room.entity.Note
import kotlinx.parcelize.Parcelize

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
@Parcelize
data class NoteWithCategory(
    @Embedded
    val note : Note,
    @Relation(parentColumn = "category_id", entityColumn = "category_id")
    val category: Category
): Parcelable
