package com.example.freshkeeper.grocery.model.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Grocery::class],
    version =  3
)
abstract class GroceryDatabase : RoomDatabase(){

    abstract fun groceryDao(): GroceryDao



}