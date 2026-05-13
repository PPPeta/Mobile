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
import ru.stroykrep.app.data.repo.AuthResult
import ru.stroykrep.app.databinding.FragmentChangePasswordBinding
import ru.stroykrep.app.util.Validators

class ChangePasswordFragment : Fragment() {

    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
        binding.btnSave.setOnClickListener { save() }
    }

    private fun save() {
        val ctx = requireContext()
        val old = binding.editOld.text?.toString().orEmpty()
        val new1 = binding.editNew.text?.toString().orEmpty()
        val new2 = binding.editNew2.text?.toString().orEmpty()

        binding.tilOld.error = null
        binding.tilNew.error = null
        binding.tilNew2.error = null

        var ok = true
        if (old.isBlank()) {
            binding.tilOld.error = getString(R.string.err_required); ok = false
        }
        if (!Validators.isValidPassword(new1)) {
            binding.tilNew.error = getString(R.string.err_password_short); ok = false
        }
        if (new1 != new2) {
            binding.tilNew2.error = getString(R.string.err_passwords_mismatch); ok = false
        }
        if (!ok) return

        val userId = ctx.app.sessionManager.getCurrentUserId()
        viewLifecycleOwner.lifecycleScope.launch {
            when (val r = ctx.app.authRepository.changePassword(userId, old, new1)) {
                is AuthResult.Success -> {
                    Toast.makeText(ctx, R.string.profile_saved, Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
                is AuthResult.Failure -> binding.tilOld.error = r.message
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
