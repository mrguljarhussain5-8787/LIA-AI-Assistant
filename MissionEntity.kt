package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "missions")
data class MissionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val category: String, // "COMMUNICATION", "RESEARCH", "DEVICE", "ROUTINE"
    val status: String = "PENDING", // "PENDING", "ACTIVE", "COMPLETED"
    val progress: Float = 0f,
    val stepsSummary: String = ""
)
