package com.example.freshkeeper.grocery.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freshkeeper.grocery.model.GroceryRepository
import com.example.freshkeeper.grocery.model.db.Grocery
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class GroceryViewModel @Inject constructor(
    private val repository: GroceryRepository,
    @ApplicationContext private val context: Context
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


    private val _nameError : MutableStateFlow<Boolean> = MutableStateFlow(false)
    val nameError : StateFlow<Boolean> = _nameError

    private val _quantityError : MutableStateFlow<Boolean> = MutableStateFlow(false)
    val quantityError : StateFlow<Boolean> = _quantityError

    private val _categoryError : MutableStateFlow<Boolean> = MutableStateFlow(false)
    val categoryError : StateFlow<Boolean> = _categoryError

    private val _unitError : MutableStateFlow<Boolean> = MutableStateFlow(false)
    val unitError : StateFlow<Boolean> = _unitError

    private val _expiryError : MutableStateFlow<Boolean> = MutableStateFlow(false)
    val expiryError : StateFlow<Boolean> = _expiryError

    fun validateFields(): Boolean {
        if (groceryName.value.isBlank()) {
            _nameError.value = true

        }
        if (groceryCategory.value.isBlank()) {
            _categoryError.value = true

        }
        if (groceryQuantity.value.isBlank()) {
            _quantityError.value = true

        }
        if (groceryQuantity.value.toDoubleOrNull() == null) {
            _quantityError.value = true

        }
        if (groceryUnit.value.isBlank()) {

            _unitError.value = true

        }
        if (groceryExpiryDate.value == null && !noExpiry.value) {
            _expiryError.value = true

        }
        if (_nameError.value || _quantityError.value || _categoryError.value || _unitError.value || _expiryError.value) {
            return false
        }
        _nameError.value = false
        _quantityError.value = false
        _categoryError.value = false
        _unitError.value = false
        _expiryError.value = false

        return true
    }

    fun clearAllStates() {
        _groceryName.value = ""
        _groceryExpiryDate.value = null
        _groceryCategory.value = ""
        _groceryQuantity.value = ""
        _groceryUnit.value = ""
        _groceryIsConsumed.value = false
        _noExpiry.value = false
        _nameError.value = false
        _quantityError.value = false
        _categoryError.value = false
        _unitError.value = false
        _expiryError.value = false

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
        if (_categoryError.value)
            _categoryError.value = false
    }

    fun setGroceryQuantity(quantity: String) {
        _groceryQuantity.value = quantity
        if (_quantityError.value)
            _quantityError.value = false
    }

    fun setGroceryUnit(unit: String) {
        _groceryUnit.value = unit
        if (_unitError.value)
            _unitError.value = false
    }

    fun setGroceryIsConsumed(isConsumed: Boolean) {
        _groceryIsConsumed.value = isConsumed
    }

    suspend fun getGroceryById(id: Int): Grocery? {
        return repository.getById(id)
    }

    fun setGroceryName(name: String) {
        _groceryName.value = name
        if (_nameError.value)
            _nameError.value = false
    }

    fun setGroceryExpiryDate(expiryDate: Long?) {
        if (expiryDate != null && !noExpiry.value) {
            _groceryExpiryDate.value = expiryDate
        }
        if (_expiryError.value)
            _expiryError.value = false
    }

    fun setNoExpiry(noExpiry: Boolean) {
        _noExpiry.value = noExpiry
        if (_expiryError.value)
            _expiryError.value = false
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

    fun deleteGroceryById(id: Int?) {
        viewModelScope.launch {
            repository.deleteById(id)
        }
    }
}
