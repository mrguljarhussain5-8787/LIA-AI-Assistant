package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sender: String, // "USER" or "ASSISTANT"
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val voicePlayed: Boolean = false,
    val isFavorite: Boolean = false,
    val personalityTag: String = "MYRA"
)
