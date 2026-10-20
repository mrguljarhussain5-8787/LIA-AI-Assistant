package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entity.MissionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MissionDao {
    @Query("SELECT * FROM missions ORDER BY id ASC")
    fun getAllMissions(): Flow<List<MissionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(missions: List<MissionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mission: MissionEntity): Long

    @Update
    suspend fun update(mission: MissionEntity)

    @Query("DELETE FROM missions WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("SELECT COUNT(*) FROM missions")
    suspend fun getMissionCount(): Int
}
