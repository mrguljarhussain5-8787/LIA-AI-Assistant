package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AssistantRepository
import com.example.data.entity.ChatMessageEntity
import com.example.data.entity.UserSettingsEntity
import com.example.tools.DeviceTools
import com.example.voice.RealisticVoiceEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppNavDestination {
    HOME,
    CHAT,
    HISTORY,
    SETTINGS,
    QUICK_ACTIONS,
    VOICE_LANGUAGE,
    CONNECTIONS,
    PERSONALITY,
    PROFILE
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AssistantRepository(application)
    val voiceEngine = RealisticVoiceEngine(application)

    val settings: StateFlow<UserSettingsEntity?> = repository.settingsFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UserSettingsEntity()
    )

    val chatMessages: StateFlow<List<ChatMessageEntity>> = repository.chatMessagesFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _currentScreen = MutableStateFlow(AppNavDestination.HOME)
    val currentScreen: StateFlow<AppNavDestination> = _currentScreen.asStateFlow()

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()

    fun navigateTo(destination: AppNavDestination) {
        _currentScreen.value = destination
    }

    fun updateUserName(newName: String) {
        viewModelScope.launch {
            repository.updateUserName(newName.trim())
        }
    }

    fun updateAssistantName(newName: String) {
        viewModelScope.launch {
            repository.updateAssistantName(newName.trim())
        }
    }

    fun updatePersonality(personality: String) {
        viewModelScope.launch {
            repository.updatePersonality(personality)
        }
    }

    fun updateVoice(voiceName: String, quality: String) {
        viewModelScope.launch {
            repository.updateVoice(voiceName, quality)
        }
    }

    fun updateVoiceModel(model: String) {
        viewModelScope.launch {
            repository.updateVoiceModel(model)
        }
    }

    fun updateSpeechSpeed(speed: Float) {
        viewModelScope.launch {
            repository.updateSpeechSpeed(speed)
        }
    }

    fun updateVoiceVolume(volume: Int) {
        viewModelScope.launch {
            repository.updateVoiceVolume(volume)
        }
    }

    fun updateListenOnAppOpen(listen: Boolean) {
        viewModelScope.launch {
            repository.updateListenOnAppOpen(listen)
        }
    }

    fun updateGeminiKey(key: String) {
        viewModelScope.launch {
            repository.updateGeminiKey(key.trim())
        }
    }

    fun updateGroqKey(key: String) {
        viewModelScope.launch {
            repository.updateGroqKey(key.trim())
        }
    }

    fun testRealisticVoice() {
        val current = settings.value ?: return
        val testText = when (current.personality) {
            "GF_MODE" -> "Oh, Boss! *Gasps softly* You're finally here! I missed you so much! Batayiye aaj mere Boss ka kya mood hai?"
            "PROFESSIONAL" -> "Greetings, ${current.userName}. All core neural subroutines are online and functioning within nominal parameters."
            "FUNNY" -> "Well hello ${current.userName}! Guess who just booted up ready to entertain? That's right, your favorite AI!"
            "HUNGRY" -> "Hey ${current.userName}! Before we start, did you order food? Because I am starving!"
            else -> "Hello ${current.userName}! I am ${current.assistantName}. All systems are ready to assist you today."
        }
        voiceEngine.speak(
            text = testText,
            pitch = 1.08f,
            speed = current.speechSpeed,
            persona = current.voiceName,
            languageCode = "en-IN"
        )
    }

    fun previewVoice(voiceName: String) {
        val current = settings.value ?: return
        val sampleText = "Hello Boss! This is the $voiceName voice. How do I sound?"
        voiceEngine.speak(
            text = sampleText,
            pitch = 1.05f,
            speed = current.speechSpeed,
            persona = voiceName,
            languageCode = "en-IN"
        )
    }

    fun sendMessage(userText: String) {
        if (userText.isBlank()) return
        viewModelScope.launch {
            repository.sendUserMessage(userText.trim())
            _isGenerating.value = true

            val response = repository.generateAssistantResponse(userText.trim())
            _isGenerating.value = false

            val current = settings.value
            voiceEngine.speak(
                text = response,
                pitch = 1.08f,
                speed = current?.speechSpeed ?: 0.85f,
                persona = current?.voiceName ?: "Kore",
                languageCode = "en-IN"
            )
        }
    }

    fun speakMessage(text: String) {
        val current = settings.value ?: return
        voiceEngine.speak(
            text = text,
            pitch = 1.08f,
            speed = current.speechSpeed,
            persona = current.voiceName,
            languageCode = "en-IN"
        )
    }

    fun stopSpeaking() {
        voiceEngine.stop()
    }

    fun clearChat() {
        viewModelScope.launch {
            repository.clearChat()
        }
    }

    fun toggleFavorite(id: Long) {
        viewModelScope.launch {
            repository.toggleFavorite(id)
        }
    }

    override fun onCleared() {
        super.onCleared()
        voiceEngine.shutdown()
    }
}
