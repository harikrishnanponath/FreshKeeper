package com.example.freshkeeper.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freshkeeper.model.GroceryRepository
import com.example.freshkeeper.model.db.Grocery
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class GroceryViewModel @Inject constructor(
    private val repository: GroceryRepository
) : ViewModel() {


    private val _groceryName: MutableStateFlow<String> = MutableStateFlow("")
    val groceryName: StateFlow<String> = _groceryName

    private val _groceryExpiryDate: MutableStateFlow<Long?> = MutableStateFlow(null)
    val groceryExpiryDate: MutableStateFlow<Long?> = _groceryExpiryDate

    private val _groceryCategory: MutableStateFlow<String> = MutableStateFlow("")
    val groceryCategory: StateFlow<String> = _groceryCategory

    private val _groceryQuantity: MutableStateFlow<String> = MutableStateFlow("")
    val groceryQuantity: MutableStateFlow<String> = _groceryQuantity

    private val _groceryUnit: MutableStateFlow<String> = MutableStateFlow("")
    val groceryUnit: StateFlow<String> = _groceryUnit

    private val _groceryIsConsumed: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val groceryIsConsumed: StateFlow<Boolean> = _groceryIsConsumed

    private val _noExpiry: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val noExpiry: StateFlow<Boolean> = _noExpiry

    fun clearAllStates() {
        _groceryName.value = ""
        _groceryExpiryDate.value = null
        _groceryCategory.value = ""
        _groceryQuantity.value = ""
        _groceryUnit.value = ""
        _groceryIsConsumed.value = false
        _noExpiry.value = false
    }

    fun loadGroceryById(id: Int) {
        viewModelScope.launch {
            val grocery = repository.getById(id)
            grocery?.let {
                _groceryName.value = it.name
                _groceryQuantity.value = it.quantity.toString()
                _groceryExpiryDate.value = it.expiryDate
                _groceryCategory.value = it.category
                _groceryUnit.value = it.unit
                _groceryIsConsumed.value = it.isConsumed
                _noExpiry.value = it.expiryDate == null
            }
        }
    }

    fun setGroceryCategory(category: String) {
        _groceryCategory.value = category
    }

    fun setGroceryQuantity(quantity: String) {
        _groceryQuantity.value = quantity
    }

    fun setGroceryUnit(unit: String) {
        _groceryUnit.value = unit
    }

    fun setGroceryIsConsumed(isConsumed: Boolean) {
        _groceryIsConsumed.value = isConsumed
    }

    suspend fun getGroceryById(id: Int): Grocery? {
        return repository.getById(id)
    }

    fun setGroceryName(name: String) {
        _groceryName.value = name
    }

    fun setGroceryExpiryDate(expiryDate: Long?) {
        if (expiryDate != null && !noExpiry.value) {
            _groceryExpiryDate.value = expiryDate
        }
    }

    fun setNoExpiry(noExpiry: Boolean) {
        _noExpiry.value = noExpiry
    }

    // Expose groceries as StateFlow
    val groceries: StateFlow<List<Grocery>> =
        repository.allGroceries.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addGrocery(id: Int?) {
        val grocery = Grocery(
            name = groceryName.value,
            category = groceryCategory.value,
            quantity = groceryQuantity.value.toDoubleOrNull() ?: 0.0,
            unit = groceryUnit.value,
            expiryDate = if (noExpiry.value) null else groceryExpiryDate.value
        )
        viewModelScope.launch {
            if (id == -1 || id == null) {
                // Add new grocery
                repository.insert(grocery)
            } else {
                // Update existing grocery
                val existing = repository.getById(id)
                existing?.let {
                    val updated = it.copy(
                        name = groceryName.value,
                        quantity = groceryQuantity.value.toDoubleOrNull() ?: 0.0,
                        expiryDate = if (_noExpiry.value) null else groceryExpiryDate.value ,
                        category = groceryCategory.value,
                        unit = groceryUnit.value,
                        isConsumed = groceryIsConsumed.value
                    )
                    repository.update(updated)
                }
            }
        }
    }

    fun updateGrocery(grocery: Grocery) {
        viewModelScope.launch {
            repository.update(grocery)
        }
    }

    fun deleteGroceryById(id: Int) {
        viewModelScope.launch {
            repository.deleteById(id)
        }
    }
}
