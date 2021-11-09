package dev.bahodir.networkentrancelessonmedium.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserForRoom(
    val Ccy: String = "",
    val CcyNm_UZ: String = "",
    val Date: String = "",
    val Diff: String = "",
    val Rate: String = "",
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var image: String = "",
    val like: Boolean = false,
)