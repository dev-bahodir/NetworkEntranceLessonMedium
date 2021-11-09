package dev.bahodir.networkentrancelessonmedium.share

import android.content.Context
import android.content.SharedPreferences

class Shared(var context: Context) {
    internal lateinit var shared: SharedPreferences

    init {
        shared = context.getSharedPreferences("share", Context.MODE_PRIVATE)
    }

    fun setNightModeState(state: Boolean?) {
        val editor: SharedPreferences.Editor = shared.edit()
        editor.putBoolean("Night Mode", state!!)
        editor.commit()
    }

    fun loadNightModeState(): Boolean? {
        return shared.getBoolean("Night Mode", false)
    }


/*
    companion object {

        private val shared: Shared = Shared()
        private var sharedPreferences: SharedPreferences? = null
        private var editor: SharedPreferences.Editor? = null

        fun getInstance(context: Context): Shared {
            if (sharedPreferences == null) {
                sharedPreferences = context.getSharedPreferences("share", Context.MODE_PRIVATE)
            }
            return shared
        }
    }


    fun clearCache() {
        editor!!.clear()
    }*/
}