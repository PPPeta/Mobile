package ru.stroykrep.app.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import ru.stroykrep.app.databinding.ActivityAuthBinding
import ru.stroykrep.app.ui.MainActivity

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            showLogin()
        }
    }

    fun showLogin() {
        supportFragmentManager.commit {
            replace(binding.authContainer.id, LoginFragment())
        }
    }

    fun showRegister() {
        supportFragmentManager.commit {
            replace(binding.authContainer.id, RegisterFragment())
            addToBackStack(null)
        }
    }

    fun onAuthSuccess() {
        startActivity(
            Intent(this, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        )
        finish()
    }
}
