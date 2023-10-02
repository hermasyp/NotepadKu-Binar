package com.catnip.notepadku.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catnip.notepadku.data.repository.LocalRepository
import com.catnip.notepadku.data.room.model.NoteWithCategory
import com.catnip.notepadku.wrapper.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
class MainViewModel(private val repository: LocalRepository) : ViewModel() {

    val notes: MutableLiveData<Resource<List<NoteWithCategory>>> = MutableLiveData()

    fun getAllNotes() {
        viewModelScope.launch(Dispatchers.IO) {
            notes.postValue(Resource.Loading())
            delay(1000)
            notes.postValue(repository.getAllNotes())
        }
    }

}