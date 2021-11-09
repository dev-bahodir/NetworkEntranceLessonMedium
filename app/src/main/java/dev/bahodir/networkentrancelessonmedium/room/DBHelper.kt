package dev.bahodir.networkentrancelessonmedium.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserForRoom::class], version = 1)
abstract class DBHelper : RoomDatabase() {

    abstract fun getDao(): DAO

    companion object {
        private var instance: DBHelper? = null

        @Synchronized
        fun getInstance(context: Context): DBHelper {
            if (instance == null) {
                instance = Room.databaseBuilder(context, DBHelper::class.java, "helper")
                    .allowMainThreadQueries().build()
            }
            return instance!!
        }
    }

}