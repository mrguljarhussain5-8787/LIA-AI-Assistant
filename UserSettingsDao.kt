package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.entity.UserSettingsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserSettingsDao {
    @Query("SELECT * FROM user_settings WHERE id = 1 LIMIT 1")
    fun getSettingsFlow(): Flow<UserSettingsEntity?>

    @Query("SELECT * FROM user_settings WHERE id = 1 LIMIT 1")
    suspend fun getSettingsDirect(): UserSettingsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(settings: UserSettingsEntity)

    @Query("UPDATE user_settings SET userName = :userName WHERE id = 1")
    suspend fun updateUserName(userName: String)

    @Query("UPDATE user_settings SET assistantName = :assistantName WHERE id = 1")
    suspend fun updateAssistantName(assistantName: String)

    @Query("UPDATE user_settings SET personality = :personality WHERE id = 1")
    suspend fun updatePersonality(personality: String)

    @Query("UPDATE user_settings SET voiceName = :voiceName, voiceQuality = :quality WHERE id = 1")
    suspend fun updateVoice(voiceName: String, quality: String)

    @Query("UPDATE user_settings SET voiceModel = :model WHERE id = 1")
    suspend fun updateVoiceModel(model: String)

    @Query("UPDATE user_settings SET speechSpeed = :speed WHERE id = 1")
    suspend fun updateSpeechSpeed(speed: Float)

    @Query("UPDATE user_settings SET voiceVolume = :volume WHERE id = 1")
    suspend fun updateVoiceVolume(volume: Int)

    @Query("UPDATE user_settings SET listenOnAppOpen = :listen WHERE id = 1")
    suspend fun updateListenOnAppOpen(listen: Boolean)

    @Query("UPDATE user_settings SET geminiApiKey = :key WHERE id = 1")
    suspend fun updateGeminiKey(key: String)

    @Query("UPDATE user_settings SET groqApiKey = :key WHERE id = 1")
    suspend fun updateGroqKey(key: String)
}
