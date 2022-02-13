package com.example.reminderapp.data.repository

import com.example.reminderapp.data.entity.Category
import com.example.reminderapp.data.room.CategoryDao
import kotlinx.coroutines.flow.Flow

class CategoryRepository(
    private val categoryDao : CategoryDao
) {
    fun categories() : Flow<List<Category>> = categoryDao.categories()

    fun getCategoryById(categoryId : Long): Category? = categoryDao.getCategoryById(categoryId)

    suspend fun addCategory(category: Category): Long{
        return when (val local = categoryDao.getCategoryByName(category.name)){
            null -> categoryDao.insert(category)
            else -> local.id
        }
    }
}