package ru.stroykrep.app.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "products",
    indices = [Index("categoryId")],
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val categoryId: Long,
    val name: String,
    val sku: String,
    val price: Double,
    val standard: String, // ГОСТ/DIN
    val size: String,
    val material: String,
    val coating: String,
    val description: String,
    val imageRes: String, // drawable name
    val isPopular: Boolean = false
)
