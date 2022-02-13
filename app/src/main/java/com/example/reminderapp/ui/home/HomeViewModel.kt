package com.example.reminderapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reminderapp.Graph
import com.example.reminderapp.data.entity.Category
import com.example.reminderapp.data.repository.CategoryRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(
    private val categoryRepository : CategoryRepository = Graph.categoryRepository
) : ViewModel() {
    private val _state = MutableStateFlow(HomeViewState())
    private val _selectedCategory = MutableStateFlow<Category?>(null)

    val state: StateFlow<HomeViewState>
        get() = _state

    fun onCategorySelected(category: Category) {
        _selectedCategory.value = category
    }

    init {

        viewModelScope.launch {
            combine(
                categoryRepository.categories().onEach { categories ->
                    if (categories.isNotEmpty() && _selectedCategory.value == null) {
                        _selectedCategory.value = categories[0]
                    }
                },
                _selectedCategory
            ) { categories, selectedCategory ->
                HomeViewState(
                    categories = categories,
                    selectedCategory = selectedCategory
                )
            }.collect { _state.value = it }
        }
        getCategoriesFromDb()
    }

    private fun getCategoriesFromDb() {
        val list = mutableListOf(
            Category(name = "Study"),
            Category(name = "Home"),
            Category(name = "Events"),
            Category(name = "Important"),
            Category(name = "Bills"),
            Category(name = "Misc")
        )
        viewModelScope.launch {
            list.forEach { category -> categoryRepository.addCategory(category) }
        }
    }
}



data class HomeViewState(
    val categories : List<Category> = emptyList(),
    val selectedCategory: Category? = null
)