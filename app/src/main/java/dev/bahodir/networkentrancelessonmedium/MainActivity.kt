package dev.bahodir.networkentrancelessonmedium

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import dev.bahodir.networkentrancelessonmedium.databinding.ActivityMainBinding
import dev.bahodir.networkentrancelessonmedium.databinding.DarkModeLayoutBinding
import dev.bahodir.networkentrancelessonmedium.share.Shared

class MainActivity : AppCompatActivity() {
    private lateinit var shared: Shared
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        shared = Shared(this)
        if (shared.loadNightModeState() == true) {
            setTheme(R.style.DarkTheme)
        }
        else {
            setTheme(R.style.AppTheme)
        }
        setContentView(binding.root)

        binding.mode.setOnClickListener {
            var bind = DarkModeLayoutBinding.inflate(layoutInflater)
            var dialog = AlertDialog.Builder(this)

            dialog.setView(bind.root)
            var builder = dialog.create()

            if (shared.loadNightModeState() == true) {
                bind.switchClick.isChecked = true
            }

            bind.switchClick.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    shared.setNightModeState(true)
                    restartApp()
                }
                else {
                    shared.setNightModeState(false)
                    restartApp()
                }
            }
            builder.show()
        }

    }
    private fun restartApp() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_).navigateUp()
    }
}