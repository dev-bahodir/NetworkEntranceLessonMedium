package dev.bahodir.networkentrancelessonmedium.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


data class User(
    var Ccy: String = "",
    var CcyNm_EN: String = "",
    var CcyNm_RU: String = "",
    var CcyNm_UZ: String = "",
    var CcyNm_UZC: String = "",
    var Code: String = "",
    var Date: String = "",
    var Diff: String = "",
    var Nominal: String = "",
    var Rate: String = "",
    var id: Int = 0,
    var image: String = "",
    var like: Boolean = false
)