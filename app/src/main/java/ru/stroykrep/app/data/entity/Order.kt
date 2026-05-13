package ru.stroykrep.app.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "orders",
    indices = [Index("userId")],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Order(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val createdAt: Long, // epoch millis
    val recipientName: String,
    val recipientPhone: String,
    val deliveryType: String, // PICKUP / COURIER
    val address: String,
    val paymentType: String, // COD / ONLINE
    val comment: String,
    val total: Double,
    val status: String // NEW / PAID / DELIVERY / DONE
)
