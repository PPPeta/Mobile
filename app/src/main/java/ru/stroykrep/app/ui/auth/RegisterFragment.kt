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
import ru.stroykrep.app.databinding.FragmentRegisterBinding
import ru.stroykrep.app.util.Validators

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnRegister.setOnClickListener { submit() }
        binding.txtGoLogin.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun submit() {
        val fullName = binding.editFullName.text?.toString()?.trim().orEmpty()
        val phone = binding.editPhone.text?.toString()?.trim().orEmpty()
        val email = binding.editEmail.text?.toString()?.trim().orEmpty()
        val password = binding.editPassword.text?.toString().orEmpty()
        val password2 = binding.editPassword2.text?.toString().orEmpty()

        binding.tilFullName.error = null
        binding.tilPhone.error = null
        binding.tilEmail.error = null
        binding.tilPassword.error = null
        binding.tilPassword2.error = null

        var ok = true
        if (fullName.isBlank()) {
            binding.tilFullName.error = getString(R.string.err_required); ok = false
        }
        if (!Validators.isValidPhone(phone)) {
            binding.tilPhone.error = getString(R.string.err_phone); ok = false
        }
        if (!Validators.isValidEmail(email)) {
            binding.tilEmail.error = getString(R.string.err_email); ok = false
        }
        if (!Validators.isValidPassword(password)) {
            binding.tilPassword.error = getString(R.string.err_password_short); ok = false
        }
        if (password != password2) {
            binding.tilPassword2.error = getString(R.string.err_passwords_mismatch); ok = false
        }
        if (!ok) return

        binding.btnRegister.isEnabled = false
        viewLifecycleOwner.lifecycleScope.launch {
            val result = requireContext().app.authRepository.register(fullName, phone, email, password)
            when (result) {
                is AuthResult.Success -> (requireActivity() as AuthActivity).onAuthSuccess()
                is AuthResult.Failure -> {
                    binding.tilEmail.error = result.message
                    binding.btnRegister.isEnabled = true
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
