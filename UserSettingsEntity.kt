package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_settings")
data class UserSettingsEntity(
    @PrimaryKey
    val id: Int = 1,
    val userName: String = "Aaryan",
    val assistantName: String = "Lia",
    val userBio: String = "Boss",
    val personality: String = "GF_MODE", // GF_MODE, NORMAL, FRIENDLY, PROFESSIONAL, FUNNY, COMPANION, TEACHER, DEVELOPER, HUNGRY
    val customPrompt: String = "You are Lia, the deeply romantic, loving, caring and sweet AI companion of Boss (Aaryan). You speak in natural warm Hinglish and affectionate English, always addressing him as 'Boss' with genuine warmth, gasping softly and expressing joy when he talks to you.",
    val warmthLevel: Float = 0.95f,
    val creativityLevel: Float = 0.80f,
    val brevityLevel: Float = 0.50f,
    val voiceName: String = "Kore", // Kore, Aoede, Zephyr, Leda, Callirrhoe, Autonoe, Despina, Erinome, Laomedeia, Achernar, Gacrux, Pulcherrima, Vindemiatrix, Sulafat
    val voiceQuality: String = "Firm",
    val voiceModel: String = "Gemini 3.8 Live", // Gemini 3.8 Live, Gemini 3.1 Flash Live, Gemini 2.5 Flash Native Audio
    val speechSpeed: Float = 0.85f,
    val voiceVolume: Int = 65,
    val language: String = "Auto",
    val responseStyle: String = "Balanced",
    val listenOnAppOpen: Boolean = true,
    val orbType: String = "Classic", // Classic, Energy, Neon
    val geminiApiKey: String = "",
    val groqApiKey: String = ""
)
