package com.catnip.notepadku.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.catnip.notepadku.R
import com.catnip.notepadku.base.BaseActivity
import com.catnip.notepadku.base.GenericViewModelFactory
import com.catnip.notepadku.data.repository.LocalRepositoryImpl
import com.catnip.notepadku.data.room.AppDatabase
import com.catnip.notepadku.data.room.datasource.NotesDataSourceImpl
import com.catnip.notepadku.databinding.ActivityMainBinding
import com.catnip.notepadku.di.ServiceLocator
import com.catnip.notepadku.ui.main.adapter.NotesAdapter
import com.catnip.notepadku.ui.noteform.NoteFormActivity
import com.catnip.notepadku.wrapper.Resource
import kotlinx.coroutines.launch

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    private val viewModel: MainViewModel by lazy {
        GenericViewModelFactory(MainViewModel(ServiceLocator.provideLocalRepository(this))).create(
            MainViewModel::class.java
        )
    }
    private val adapter: NotesAdapter by lazy {
        NotesAdapter {
            NoteFormActivity.startActivity(this,it.note.id)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initRecyclerView()
        observeData()
        setOnClickListeners()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllNotes()
    }

    private fun setOnClickListeners() {
        binding.fabAdd.setOnClickListener {
            NoteFormActivity.startActivity(this)
        }
    }

    private fun observeData() {
        viewModel.notes.observe(this) {
            when (it) {
                is Resource.Error -> {
                    binding.tvError.isVisible = true
                    binding.tvError.text = it.message
                    binding.pbNotes.isVisible = false
                    binding.rvNotes.isVisible = false
                }
                is Resource.Loading -> {
                    binding.tvError.isVisible = false
                    binding.pbNotes.isVisible = true
                    binding.rvNotes.isVisible = false
                }
                is Resource.Success -> {
                    binding.tvError.isVisible = false
                    binding.pbNotes.isVisible = false
                    binding.rvNotes.isVisible = true
                    if (it.data.isNullOrEmpty()) {
                        showEmptyData()
                    } else {
                        adapter.setItems(it.data)
                    }
                }
            }
        }
    }


    private fun initRecyclerView() {
        binding.rvNotes.adapter = this@MainActivity.adapter
    }

    private fun showEmptyData() {
        binding.tvError.text = getString(R.string.text_empty_notes)
        binding.tvError.isVisible = true
    }


}