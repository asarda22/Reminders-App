package com.example.reminderapp.data.room

import androidx.room.*
import com.example.reminderapp.data.entity.Category
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CategoryDao {

    @Query(value = "Select * from categories where name = :name")
    abstract fun getCategoryByName(name:String): Category?

    @Query(value = "Select * from categories where id = :categoryId")
    abstract fun getCategoryById(categoryId:Long) : Category?

    @Query(value = "Select * From categories LIMIT 15")
    abstract fun categories(): Flow<List<Category>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(entity: Category): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(entities:Collection<Category>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun update(entity:Category)

    @Delete
    abstract suspend fun delete(entity: Category): Int
}