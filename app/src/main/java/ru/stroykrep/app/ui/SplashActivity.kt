package ru.stroykrep.app.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.stroykrep.app.app
import ru.stroykrep.app.ui.auth.AuthActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val next = if (app.sessionManager.isLoggedIn()) {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, AuthActivity::class.java)
        }
        startActivity(next)
        finish()
    }
}
