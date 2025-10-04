package com.example.freshkeeper.grocery.model.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GroceryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrocery(grocery: Grocery)

    @Delete
    suspend fun deleteGrocery(grocery: Grocery)

    @Update
    suspend fun updateGrocery(grocery: Grocery)

    @Query("SELECT * FROM groceries WHERE id = :id")
    suspend fun getGroceryById(id: Int): Grocery?

    @Query("SELECT * FROM groceries")
    fun getAllGroceries(): Flow<List<Grocery>>

    @Query("SELECT * FROM groceries ORDER BY name ASC")
    fun getGroceriesOrderedByName(): Flow<List<Grocery>>

    @Query("SELECT * FROM groceries ORDER BY expiryDate ASC")
    fun getGroceriesOrderedByExpiryDate(): Flow<List<Grocery>>

    @Query("Delete FROM groceries where id = :id")
    suspend fun deleteGroceryById(id: Int?)
}