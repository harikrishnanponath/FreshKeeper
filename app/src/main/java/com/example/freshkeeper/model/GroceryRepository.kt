package com.example.freshkeeper.model

import com.example.freshkeeper.model.db.Grocery
import com.example.freshkeeper.model.db.GroceryDao
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GroceryRepository @Inject constructor(
    private val dao: GroceryDao
) {

    // Flow emits updates whenever DB changes
    val allGroceries: Flow<List<Grocery>> = dao.getAllGroceries()

    suspend fun insert(grocery: Grocery) {
        dao.insertGrocery(grocery)
    }

    suspend fun update(grocery: Grocery) {
        dao.updateGrocery(grocery)
    }

    suspend fun delete(grocery: Grocery) {
        dao.deleteGrocery(grocery)
    }

    suspend fun getById(id: Int): Grocery? {
        return dao.getGroceryById(id)
    }

    suspend fun deleteById(id: Int?) {
        return dao.deleteGroceryById(id)
    }
}
