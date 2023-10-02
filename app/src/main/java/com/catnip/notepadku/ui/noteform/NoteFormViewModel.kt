package com.catnip.notepadku.ui.noteform

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catnip.notepadku.constant.CommonConstant
import com.catnip.notepadku.data.repository.LocalRepository
import com.catnip.notepadku.data.room.entity.Category
import com.catnip.notepadku.data.room.entity.Note
import com.catnip.notepadku.data.room.model.NoteWithCategory
import com.catnip.notepadku.wrapper.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
class NoteFormViewModel(private val repository: LocalRepository) : ViewModel() {

    var noteId: Int = CommonConstant.UNSET_ID
    var selectedCategoryId: Int = CommonConstant.UNSET_ID

    val initialDataResult =
        MutableLiveData<Resource<Triple<NoteWithCategory?, List<Category>, Int>>>()

    val insertResult = MutableLiveData<Resource<Number>>()
    val deleteResult = MutableLiveData<Resource<Number>>()
    val updateResult = MutableLiveData<Resource<Number>>()

    fun getInitialData() {
        viewModelScope.launch(Dispatchers.IO) {
            initialDataResult.postValue(Resource.Loading())
            delay(1000)
            val note = repository.getNoteById(noteId).data
            val categories = repository.getAllCategories().data.orEmpty()
            val selectedCategory = categories.find {
                it.categoryId == note?.category?.categoryId
            }
            val selectedCategoryPos = categories.indexOf(selectedCategory)
            selectedCategoryId = note?.note?.categoryId ?: CommonConstant.UNSET_ID
            initialDataResult.postValue(
                Resource.Success(
                    Triple(
                        note,
                        categories,
                        selectedCategoryPos
                    )
                )
            )
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteResult.postValue(Resource.Loading())
            delay(1000)
            deleteResult.postValue(repository.deleteNote(note))
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            updateResult.postValue(Resource.Loading())
            delay(1000)
            updateResult.postValue(repository.updateNote(note))
        }
    }

    fun insertNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            insertResult.postValue(Resource.Loading())
            delay(1000)
            insertResult.postValue(repository.insertNote(note))
        }
    }

    fun setIntentData(intent: Intent) {
        noteId = intent.getIntExtra(CommonConstant.EXTRAS_ID_NOTE, CommonConstant.UNSET_ID)
    }


}