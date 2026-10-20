package com.example.voice

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Locale

class RealisticVoiceEngine(context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var isInitialized = false

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _audioVisualizerLevel = MutableStateFlow(0.15f)
    val audioVisualizerLevel: StateFlow<Float> = _audioVisualizerLevel.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.Default)
    private var visualizerJob: Job? = null

    init {
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                _isSpeaking.value = true
                startVisualizerAnimation()
            }

            override fun onDone(utteranceId: String?) {
                _isSpeaking.value = false
                stopVisualizerAnimation()
            }

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) {
                _isSpeaking.value = false
                stopVisualizerAnimation()
            }
        })
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            // Default to Indian English / Hinglish or available locale
            val loc = Locale("en", "IN")
            val result = tts?.setLanguage(loc)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                tts?.setLanguage(Locale.US)
            }
        }
    }

    fun speak(
        text: String,
        pitch: Float = 1.05f,
        speed: Float = 1.0f,
        persona: String = "FEMININE_WARM",
        languageCode: String = "en-IN"
    ) {
        if (!isInitialized || tts == null) return

        // Set Language
        try {
            val locale = when (languageCode) {
                "hi-IN" -> Locale("hi", "IN")
                "en-GB" -> Locale.UK
                "en-US" -> Locale.US
                else -> Locale("en", "IN")
            }
            tts?.language = locale
        } catch (_: Exception) {}

        // Adjust Pitch & Speed based on persona
        val adjustedPitch = when (persona) {
            "JARVIS_BRITISH" -> 0.82f // Deeper, robotic analytical tone
            "FEMININE_WARM" -> pitch * 1.12f // Warm melodious frequency
            "FRIENDLY" -> pitch * 1.02f
            "CONFIDENT" -> pitch * 0.95f
            else -> pitch
        }

        val adjustedSpeed = when (persona) {
            "JARVIS_BRITISH" -> speed * 0.96f
            "CONFIDENT" -> speed * 1.08f
            else -> speed
        }

        tts?.setPitch(adjustedPitch.coerceIn(0.5f, 2.0f))
        tts?.setSpeechRate(adjustedSpeed.coerceIn(0.5f, 2.0f))

        val params = Bundle()
        params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MYRA_VOICE_${System.currentTimeMillis()}")
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, params, "MYRA_VOICE_UTTERANCE")
    }

    fun stop() {
        tts?.stop()
        _isSpeaking.value = false
        stopVisualizerAnimation()
    }

    private fun startVisualizerAnimation() {
        visualizerJob?.cancel()
        visualizerJob = scope.launch {
            var step = 0
            while (isActive && _isSpeaking.value) {
                step++
                // Generate natural pulsating wave values
                val wave = 0.35f + (0.45f * kotlin.math.sin(step * 0.4f).toFloat().coerceAtLeast(0f))
                _audioVisualizerLevel.value = wave
                delay(60)
            }
            _audioVisualizerLevel.value = 0.15f
        }
    }

    private fun stopVisualizerAnimation() {
        visualizerJob?.cancel()
        _audioVisualizerLevel.value = 0.15f
    }

    fun shutdown() {
        stop()
        tts?.shutdown()
        tts = null
        isInitialized = false
    }
}
