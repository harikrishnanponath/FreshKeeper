package com.example.freshkeeper.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "groceries")
data class Grocery(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val category: String,
    val quantity: Double,
    val unit: String,
    val expiryDate: Long?,
    val addedDate: Long = System.currentTimeMillis(),
    val isConsumed: Boolean = false
)