package com.example.ksbllc.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity

class SettingsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences("pref", MODE_PRIVATE)
        if (!prefs.getBoolean("firstRun", false)){
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
        else if (!prefs.getBoolean("Start_Login_activity", false)) {
            startActivity(Intent(this, AuthentificationActivity::class.java))
        } else {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
