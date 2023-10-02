package com.catnip.notepadku.ui.noteform

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import com.catnip.notepadku.R
import com.catnip.notepadku.base.BaseActivity
import com.catnip.notepadku.base.GenericViewModelFactory
import com.catnip.notepadku.constant.CommonConstant
import com.catnip.notepadku.data.room.entity.Category
import com.catnip.notepadku.data.room.entity.Note
import com.catnip.notepadku.data.room.model.NoteWithCategory
import com.catnip.notepadku.databinding.ActivityNoteFormBinding
import com.catnip.notepadku.di.ServiceLocator
import com.catnip.notepadku.wrapper.Resource

class NoteFormActivity : BaseActivity<ActivityNoteFormBinding>(ActivityNoteFormBinding::inflate) {

    private val viewModel: NoteFormViewModel by lazy {
        GenericViewModelFactory(NoteFormViewModel(ServiceLocator.provideLocalRepository(this))).create(
            NoteFormViewModel::class.java
        )
    }

    override fun onStart() {
        super.onStart()
        viewModel.setIntentData(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolbar()
        observeData()
        getInitialData()

    }

    private fun getInitialData() {
        viewModel.getInitialData()
    }


    private fun observeData() {
        viewModel.initialDataResult.observe(this) {
            when (it) {
                is Resource.Error -> {
                    binding.tvError.isVisible = true
                    binding.tvError.text = it.message
                    binding.pbForm.isVisible = false
                    binding.svForm.isVisible = false
                }
                is Resource.Loading -> {
                    binding.tvError.isVisible = false
                    binding.pbForm.isVisible = true
                    binding.svForm.isVisible = false
                }
                is Resource.Success -> {
                    binding.tvError.isVisible = false
                    binding.pbForm.isVisible = false
                    binding.svForm.isVisible = true

                    val note = it.data?.first
                    val categories = it.data?.second
                    val selectedPos = it.data?.third ?: 0
                    initCategory(
                        categories,
                        selectedPos
                    )
                    bindDataToForm(note)
                }
            }
        }
        viewModel.insertResult.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    setFormEnabled(false)
                    binding.pbForm.isVisible = true
                }
                is Resource.Success -> {
                    setFormEnabled(true)
                    binding.pbForm.isVisible = false
                    finish()
                    Toast.makeText(this, "Insert data Success", Toast.LENGTH_SHORT).show()
                }
                is Resource.Error -> {
                    setFormEnabled(true)
                    binding.pbForm.isVisible = false
                    finish()
                    Toast.makeText(this, "Error when update data", Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.updateResult.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    setFormEnabled(false)
                    binding.pbForm.isVisible = true
                }
                is Resource.Success -> {
                    setFormEnabled(true)
                    binding.pbForm.isVisible = false
                    finish()
                    Toast.makeText(this, "Update data Success", Toast.LENGTH_SHORT).show()
                }
                is Resource.Error -> {
                    setFormEnabled(true)
                    binding.pbForm.isVisible = false
                    finish()
                    Toast.makeText(this, "Error when update data", Toast.LENGTH_SHORT).show()
                }
            }
        }
        viewModel.deleteResult.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    setFormEnabled(false)
                    binding.pbForm.isVisible = true
                }
                is Resource.Success -> {
                    setFormEnabled(true)
                    binding.pbForm.isVisible = false
                    finish()
                    Toast.makeText(this, "Delete data Success", Toast.LENGTH_SHORT).show()
                }
                is Resource.Error -> {
                    setFormEnabled(true)
                    binding.pbForm.isVisible = false
                    finish()
                    Toast.makeText(this, "Error when delete data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initCategory(categories: List<Category>?, selectedPos: Int) {
        val categoriesTitle = categories?.map { category -> category.categoryName }.orEmpty()
        val adapter = ArrayAdapter(
            this@NoteFormActivity,
            android.R.layout.simple_list_item_1,
            categoriesTitle
        )
        binding.actvCategory.setOnItemClickListener { _, _, pos, _ ->
            viewModel.selectedCategoryId = categories?.get(pos)?.categoryId ?: CommonConstant.UNSET_ID
        }
        binding.actvCategory.setAdapter(adapter)
        if(selectedPos != -1){
            binding.actvCategory.setText(
                binding.actvCategory.adapter.getItem(selectedPos).toString(),
                false
            )
        }
    }

    private fun setFormEnabled(isFormEnabled: Boolean) {
        with(binding) {
            tilCategoryDropdown.isEnabled = isFormEnabled
            tilNoteBody.isEnabled = isFormEnabled
            tilNoteTitle.isEnabled = isFormEnabled
        }
    }

    private fun bindDataToForm(data: NoteWithCategory?) {
        data?.let {
            binding.etNoteTitle.setText(data.note.title)
            binding.etNoteBody.setText(data.note.body)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_form_note, menu)
        val menuDelete = menu?.findItem(R.id.menu_delete)
        menuDelete?.isVisible = viewModel.noteId != CommonConstant.UNSET_ID
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_save -> {
                saveNote()
                true
            }
            R.id.menu_delete -> {
                deleteNote()
                true
            }
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> {
                true
            }
        }
    }

    private fun checkFormValidation(): Boolean {
        val title = binding.etNoteTitle.text.toString()
        val body = binding.etNoteBody.text.toString()
        val categoryTitle = binding.actvCategory.text.toString()
        var isFormValid = true
        if (title.isEmpty()) {
            isFormValid = false
            binding.tilNoteTitle.isErrorEnabled = true
            binding.tilNoteTitle.error = getString(R.string.text_error_empty_title)
        } else {
            binding.tilNoteTitle.isErrorEnabled = false
        }
        if (body.isEmpty()) {
            isFormValid = false
            binding.tilNoteBody.isErrorEnabled = true
            binding.tilNoteBody.error = getString(R.string.text_error_empty_body)
        } else {
            binding.tilNoteBody.isErrorEnabled = false
        }
        if (categoryTitle.isEmpty() && viewModel.selectedCategoryId != CommonConstant.UNSET_ID) {
            isFormValid = false
            binding.tilCategoryDropdown.isErrorEnabled = true
            binding.tilCategoryDropdown.error = getString(R.string.text_error_empty_category)
        } else {
            binding.tilCategoryDropdown.isErrorEnabled = false
        }
        return isFormValid
    }

    private fun saveNote() {
        if (checkFormValidation()) {
            if (isEditAction()) {
                viewModel.updateNote(parseFormIntoEntity())
            } else {
                viewModel.insertNote(parseFormIntoEntity())
            }
        }
    }

    private fun deleteNote() {
        if (isEditAction()) {
            viewModel.deleteNote(parseFormIntoEntity())
        }
    }

    private fun parseFormIntoEntity(): Note {
        return Note(
            title = binding.etNoteTitle.text.toString().trim(),
            body = binding.etNoteBody.text.toString().trim(),
            categoryId = viewModel.selectedCategoryId
        ).apply {
            if (isEditAction()) {
                id = viewModel.noteId
            }
        }
    }

    private fun initToolbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title =
            if (isEditAction())
                getString(R.string.text_toolbar_edit)
            else
                getString(R.string.text_toolbar_insert)
    }

    private fun isEditAction(): Boolean {
        return viewModel.noteId != CommonConstant.UNSET_ID
    }
    companion object {
        fun startActivity(context: Context, id: Int? = null) {
            context.startActivity(Intent(context, NoteFormActivity::class.java).apply {
                id?.let {
                    putExtra(CommonConstant.EXTRAS_ID_NOTE, id)
                }
            })
        }
    }
}