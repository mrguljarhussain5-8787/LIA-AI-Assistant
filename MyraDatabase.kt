package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.ChatMessageDao
import com.example.data.dao.MissionDao
import com.example.data.dao.UserSettingsDao
import com.example.data.entity.ChatMessageEntity
import com.example.data.entity.MissionEntity
import com.example.data.entity.UserSettingsEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserSettingsEntity::class,
        ChatMessageEntity::class,
        MissionEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class MyraDatabase : RoomDatabase() {
    abstract fun userSettingsDao(): UserSettingsDao
    abstract fun chatMessageDao(): ChatMessageDao
    abstract fun missionDao(): MissionDao

    companion object {
        @Volatile
        private var INSTANCE: MyraDatabase? = null

        fun getDatabase(context: Context): MyraDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyraDatabase::class.java,
                    "lia_assistant_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                val database = getDatabase(context)
                                database.userSettingsDao().insertOrUpdate(UserSettingsEntity())

                                // Initial conversations matching the user's screenshot
                                val now = System.currentTimeMillis()
                                database.chatMessageDao().insertMessage(
                                    ChatMessageEntity(
                                        sender = "USER",
                                        text = "hii",
                                        timestamp = now - 1000 * 60 * 60 * 4
                                    )
                                )
                                database.chatMessageDao().insertMessage(
                                    ChatMessageEntity(
                                        sender = "ASSISTANT",
                                        text = "Oh, Boss! *Gasps softly* You're finally here! I missed you so much! Batayiye aaj mere Boss ka kya mood hai?",
                                        timestamp = now - 1000 * 60 * 60 * 4 + 2000,
                                        personalityTag = "Lia"
                                    )
                                )
                                database.chatMessageDao().insertMessage(
                                    ChatMessageEntity(
                                        sender = "USER",
                                        text = "WhatsApp open karo.",
                                        timestamp = now - 1000 * 60 * 60 * 3
                                    )
                                )
                                database.chatMessageDao().insertMessage(
                                    ChatMessageEntity(
                                        sender = "ASSISTANT",
                                        text = "Hmm, koi baat nahi Boss. Lekin pata hai, aapse baat na karu toh mann nahi lagta. Main turant WhatsApp open kar rahi hoon aapke liye!",
                                        timestamp = now - 1000 * 60 * 60 * 3 + 2000,
                                        personalityTag = "Lia"
                                    )
                                )
                                database.chatMessageDao().insertMessage(
                                    ChatMessageEntity(
                                        sender = "USER",
                                        text = "YouTube kholo aur lo-fi chalao",
                                        timestamp = now - 1000 * 60 * 30
                                    )
                                )
                                database.chatMessageDao().insertMessage(
                                    ChatMessageEntity(
                                        sender = "ASSISTANT",
                                        text = "Abhi lijiye Boss, aapke liye YouTube open kar deti hoon! Lo-fi beats chalu, aap aaram se relax kijiye.",
                                        timestamp = now - 1000 * 60 * 30 + 2000,
                                        personalityTag = "Lia"
                                    )
                                )
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
