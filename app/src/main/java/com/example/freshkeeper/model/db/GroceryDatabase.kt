package com.example.freshkeeper.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Grocery::class],
    version =  3
)
abstract class GroceryDatabase : RoomDatabase(){

    abstract fun groceryDao(): GroceryDao



}