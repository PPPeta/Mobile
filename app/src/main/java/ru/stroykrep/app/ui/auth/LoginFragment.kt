package ru.stroykrep.app.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.stroykrep.app.R
import ru.stroykrep.app.app
import ru.stroykrep.app.data.repo.AuthResult
import ru.stroykrep.app.databinding.FragmentLoginBinding
import ru.stroykrep.app.util.Validators

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnLogin.setOnClickListener { submit() }
        binding.txtGoRegister.setOnClickListener {
            (requireActivity() as AuthActivity).showRegister()
        }
    }

    private fun submit() {
        val email = binding.editEmail.text?.toString().orEmpty()
        val password = binding.editPassword.text?.toString().orEmpty()

        binding.tilEmail.error = null
        binding.tilPassword.error = null

        var ok = true
        if (!Validators.isValidEmail(email)) {
            binding.tilEmail.error = getString(R.string.err_email); ok = false
        }
        if (!Validators.isValidPassword(password)) {
            binding.tilPassword.error = getString(R.string.err_password_short); ok = false
        }
        if (!ok) return

        binding.btnLogin.isEnabled = false
        viewLifecycleOwner.lifecycleScope.launch {
            when (val result = requireContext().app.authRepository.login(email, password)) {
                is AuthResult.Success -> (requireActivity() as AuthActivity).onAuthSuccess()
                is AuthResult.Failure -> {
                    binding.tilPassword.error = result.message
                    binding.btnLogin.isEnabled = true
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
