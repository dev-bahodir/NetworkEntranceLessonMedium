package dev.bahodir.networkentrancelessonmedium.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Flowable

@Dao
interface DAO {

    @Insert(onConflict = REPLACE)
    fun addUser(userForRoom: UserForRoom)

    @Query("SELECT * FROM users")
    fun getUser() : Flowable<List<UserForRoom>>

    @Update
    fun updateUser(userForRoom: UserForRoom)
}