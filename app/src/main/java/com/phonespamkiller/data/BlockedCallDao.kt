package com.phonespamkiller.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BlockedCallDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(blockedCall: BlockedCall)

    @Query("SELECT * FROM blocked_calls ORDER BY timestamp DESC")
    fun getAllCalls(): LiveData<List<BlockedCall>>

    @Query("SELECT COUNT(*) FROM blocked_calls")
    fun getTotalCount(): LiveData<Int>

    @Query("DELETE FROM blocked_calls")
    suspend fun deleteAll()
}
