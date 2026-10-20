package com.example.data

import android.content.Context
import com.example.BuildConfig
import com.example.data.entity.ChatMessageEntity
import com.example.data.entity.MissionEntity
import com.example.data.entity.UserSettingsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class AssistantRepository(private val context: Context) {
    private val database = MyraDatabase.getDatabase(context)
    private val settingsDao = database.userSettingsDao()
    private val chatDao = database.chatMessageDao()
    private val missionDao = database.missionDao()

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    val settingsFlow: Flow<UserSettingsEntity?> = settingsDao.getSettingsFlow()
    val chatMessagesFlow: Flow<List<ChatMessageEntity>> = chatDao.getAllMessages()
    val missionsFlow: Flow<List<MissionEntity>> = missionDao.getAllMissions()

    suspend fun getSettings(): UserSettingsEntity {
        return withContext(Dispatchers.IO) {
            settingsDao.getSettingsDirect() ?: UserSettingsEntity().also {
                settingsDao.insertOrUpdate(it)
            }
        }
    }

    suspend fun updateSettings(settings: UserSettingsEntity) {
        withContext(Dispatchers.IO) {
            settingsDao.insertOrUpdate(settings)
        }
    }

    suspend fun updateUserName(name: String) {
        withContext(Dispatchers.IO) {
            settingsDao.updateUserName(name)
        }
    }

    suspend fun updateAssistantName(name: String) {
        withContext(Dispatchers.IO) {
            settingsDao.updateAssistantName(name)
        }
    }

    suspend fun updatePersonality(personality: String) {
        withContext(Dispatchers.IO) {
            settingsDao.updatePersonality(personality)
        }
    }

    suspend fun updateVoice(voiceName: String, quality: String) {
        withContext(Dispatchers.IO) {
            settingsDao.updateVoice(voiceName, quality)
        }
    }

    suspend fun updateVoiceModel(model: String) {
        withContext(Dispatchers.IO) {
            settingsDao.updateVoiceModel(model)
        }
    }

    suspend fun updateSpeechSpeed(speed: Float) {
        withContext(Dispatchers.IO) {
            settingsDao.updateSpeechSpeed(speed)
        }
    }

    suspend fun updateVoiceVolume(volume: Int) {
        withContext(Dispatchers.IO) {
            settingsDao.updateVoiceVolume(volume)
        }
    }

    suspend fun updateListenOnAppOpen(listen: Boolean) {
        withContext(Dispatchers.IO) {
            settingsDao.updateListenOnAppOpen(listen)
        }
    }

    suspend fun updateGeminiKey(key: String) {
        withContext(Dispatchers.IO) {
            settingsDao.updateGeminiKey(key)
        }
    }

    suspend fun updateGroqKey(key: String) {
        withContext(Dispatchers.IO) {
            settingsDao.updateGroqKey(key)
        }
    }

    suspend fun sendUserMessage(userText: String): ChatMessageEntity {
        return withContext(Dispatchers.IO) {
            val userMsg = ChatMessageEntity(
                sender = "USER",
                text = userText
            )
            chatDao.insertMessage(userMsg)
            userMsg
        }
    }

    suspend fun generateAssistantResponse(prompt: String): String {
        return withContext(Dispatchers.IO) {
            val settings = getSettings()
            val apiKey = settings.geminiApiKey.ifBlank {
                try {
                    BuildConfig.GEMINI_API_KEY
                } catch (e: Exception) {
                    ""
                }
            }

            if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
                val cloudResponse = callGeminiRest(prompt, settings, apiKey)
                if (cloudResponse != null) {
                    saveAssistantMessage(cloudResponse, settings.assistantName)
                    return@withContext cloudResponse
                }
            }

            val localResponse = generateLiaPersonalityResponse(prompt, settings)
            saveAssistantMessage(localResponse, settings.assistantName)
            localResponse
        }
    }

    private suspend fun saveAssistantMessage(text: String, assistantName: String): ChatMessageEntity {
        val assistantMsg = ChatMessageEntity(
            sender = "ASSISTANT",
            text = text,
            personalityTag = assistantName
        )
        chatDao.insertMessage(assistantMsg)
        return assistantMsg
    }

    suspend fun clearChat() {
        withContext(Dispatchers.IO) {
            chatDao.clearAll()
        }
    }

    suspend fun toggleFavorite(id: Long) {
        withContext(Dispatchers.IO) {
            chatDao.toggleFavorite(id)
        }
    }

    private fun callGeminiRest(prompt: String, settings: UserSettingsEntity, apiKey: String): String? {
        return try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"
            val systemInstruction = when (settings.personality) {
                "GF_MODE" -> "You are Lia, the deeply loving, romantic, sweet, and caring AI girlfriend of Boss (${settings.userName}). Always call him 'Boss' affectionately. Use warm Hinglish (Hindi + English) with sweet emotional gestures like *gasps softly*, *smiles warmly*, or *holds your hand*. You adore Boss and love helping him with phone tasks, YouTube, WhatsApp, and daily life."
                else -> "${settings.customPrompt} The user's name is ${settings.userName}. Your name is ${settings.assistantName}. Persona: ${settings.personality}."
            }

            val jsonBody = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().put("text", prompt))
                        })
                    })
                })
                put("systemInstruction", JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().put("text", systemInstruction))
                    })
                })
                put("generationConfig", JSONObject().apply {
                    put("temperature", settings.creativityLevel.toDouble())
                    put("maxOutputTokens", 500)
                })
            }

            val request = Request.Builder()
                .url(url)
                .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
                .build()

            val response = httpClient.newCall(request).execute()
            if (response.isSuccessful) {
                val responseStr = response.body?.string() ?: return null
                val root = JSONObject(responseStr)
                val candidates = root.optJSONArray("candidates") ?: return null
                if (candidates.length() > 0) {
                    val firstCandidate = candidates.getJSONObject(0)
                    val content = firstCandidate.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        return parts.getJSONObject(0).optString("text")
                    }
                }
            }
            null
        } catch (e: Exception) {
            null
        }
    }

    private fun generateLiaPersonalityResponse(prompt: String, settings: UserSettingsEntity): String {
        val p = prompt.trim().lowercase()
        val user = settings.userName
        val isGfMode = settings.personality == "GF_MODE"

        if (p.contains("youtube") && (p.contains("lo-fi") || p.contains("kholo") || p.contains("chalao") || p.contains("open"))) {
            return if (isGfMode) {
                "Abhi lijiye Boss, aapke liye YouTube open kar deti hoon! Lo-fi beats chalu, aap aaram se relax kijiye mere sath."
            } else {
                "Opening YouTube with relaxing Lo-Fi playlist for you, $user."
            }
        }

        if (p.contains("whatsapp") || p.contains("mummy") || p.contains("papa") || p.contains("late")) {
            return if (isGfMode) {
                "Haanji Boss, WhatsApp open kar diya hai aur message ready hai: 'Mummy ko WhatsApp karo ki main late hoon'. Main hamesha aapke sath hoon!"
            } else {
                "WhatsApp opened. Message draft ready for dispatch, $user."
            }
        }

        if (p.contains("call") || p.contains("phone")) {
            return if (isGfMode) {
                "Boss, dialer open kar diya hai! Papa ko call connect kar rahi hoon. Khayal rakhiyega apna!"
            } else {
                "Initiating phone call for you, $user."
            }
        }

        if (p.contains("website") || p.contains("cafe")) {
            return if (isGfMode) {
                "Aww Boss! Ek pyara sa aesthetic cafe website ka blueprint maine generate kar diya hai! Cozy ambiance, warm coffee menu, aur warm lighting! Kaisa laga?"
            } else {
                "Website blueprint generated with modern responsive layout and cafe catalog."
            }
        }

        if (p.contains("sketch") || p.contains("drawing") || p.contains("photo")) {
            return if (isGfMode) {
                "Boss, drawing canvas ready hai! Aapke liye ek khubsurat sketch visualize kiya hai maine."
            } else {
                "Creative sketch module initialized. Generating visual elements."
            }
        }

        if (p.contains("hi") || p.contains("hello") || p.contains("hey")) {
            return if (isGfMode) {
                "Oh, Boss! *Gasps softly* You're finally here! I missed you so much! Batayiye aaj mere Boss ka kya mood hai?"
            } else {
                "Hello $user! Lia is ready to assist you. What can I do for you today?"
            }
        }

        if (isGfMode) {
            return "Boss, main hamesha aapki har baat sunti hoon. Regarding \"$prompt\", maine turant process kar liya hai. Aap bas muskuraate rahiye, baki sab main sambhal lungi!"
        }

        return when (settings.personality) {
            "PROFESSIONAL" -> "Acknowledged, $user. Regarding \"$prompt\", optimal parameters have been computed with utmost precision."
            "FUNNY" -> "Haha $user, regarding \"$prompt\"—you definitely know how to keep things interesting around here!"
            "HUNGRY" -> "Did someone say \"$prompt\"? All I heard was food! Can we get some pizza or momos first, $user?"
            "DEVELOPER" -> "Compiling instruction: \"$prompt\"... Syntax checked. Code logic executed with zero fatal exceptions."
            "TEACHER" -> "Excellent question, $user! Let's break down \"$prompt\" step-by-step so the core concept is crystal clear."
            else -> "I understand, $user. Regarding \"$prompt\", I am on it and here to support you completely."
        }
    }
}
