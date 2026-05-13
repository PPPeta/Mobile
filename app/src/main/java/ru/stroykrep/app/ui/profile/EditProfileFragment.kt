package ru.stroykrep.app.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import ru.stroykrep.app.R
import ru.stroykrep.app.app
import ru.stroykrep.app.databinding.FragmentEditProfileBinding
import ru.stroykrep.app.util.Validators

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        viewLifecycleOwner.lifecycleScope.launch {
            val user = requireContext().app.authRepository.currentUser() ?: return@launch
            binding.editFullName.setText(user.fullName)
            binding.editPhone.setText(user.phone)
            binding.editEmail.setText(user.email)
        }

        binding.btnSave.setOnClickListener { save() }
    }

    private fun save() {
        val ctx = requireContext()
        val fullName = binding.editFullName.text?.toString()?.trim().orEmpty()
        val phone = binding.editPhone.text?.toString()?.trim().orEmpty()
        val email = binding.editEmail.text?.toString()?.trim().orEmpty()

        binding.tilFullName.error = null
        binding.tilPhone.error = null
        binding.tilEmail.error = null

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
        if (!ok) return

        viewLifecycleOwner.lifecycleScope.launch {
            val user = ctx.app.authRepository.currentUser() ?: return@launch
            ctx.app.authRepository.updateProfile(
                user.copy(fullName = fullName, phone = phone, email = email.lowercase())
            )
            Toast.makeText(ctx, R.string.profile_saved, Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
