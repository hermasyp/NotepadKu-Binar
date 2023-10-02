package com.catnip.notepadku.data.room.datasource

import com.catnip.notepadku.data.room.dao.CategoriesDao
import com.catnip.notepadku.data.room.entity.Category

/**
Written with love by Muhammad Hermas Yuda Pamungkas
Github : https://github.com/hermasyp
 **/
interface CategoryDataSource {
    suspend fun getAllCategory(): List<Category>

    suspend fun getCategoryById(id: Int): Category

    suspend fun insertCategory(category: Category): Long
}


class CategoryDataSourceImpl(private val dao: CategoriesDao) : CategoryDataSource{
    override suspend fun getAllCategory(): List<Category> {
        return dao.getAllCategory()
    }

    override suspend fun getCategoryById(id: Int): Category {
        return dao.getCategoryById(id)
    }

    override suspend fun insertCategory(category: Category): Long {
        return dao.insertCategory(category)
    }

}