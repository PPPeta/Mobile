package ru.stroykrep.app.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import ru.stroykrep.app.R
import ru.stroykrep.app.app
import ru.stroykrep.app.databinding.FragmentProfileBinding
import ru.stroykrep.app.ui.SplashActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Menu labels
        binding.rowEdit.txtTitle.text = getString(R.string.profile_edit)
        binding.rowChangePassword.txtTitle.text = getString(R.string.profile_change_password)
        binding.rowOrders.txtTitle.text = getString(R.string.profile_orders)
        binding.rowFavorites.txtTitle.text = getString(R.string.profile_favorites)
        binding.rowContacts.txtTitle.text = getString(R.string.profile_contacts)
        binding.rowAbout.txtTitle.text = getString(R.string.profile_about)

        val nav = findNavController()
        binding.rowEdit.root.setOnClickListener { nav.navigate(R.id.action_global_editProfileFragment) }
        binding.rowChangePassword.root.setOnClickListener { nav.navigate(R.id.action_global_changePasswordFragment) }
        binding.rowOrders.root.setOnClickListener { nav.navigate(R.id.action_global_ordersFragment) }
        binding.rowFavorites.root.setOnClickListener { nav.navigate(R.id.action_global_favoritesFragment) }
        binding.rowContacts.root.setOnClickListener { nav.navigate(R.id.action_global_contactsFragment) }
        binding.rowAbout.root.setOnClickListener { nav.navigate(R.id.action_global_aboutFragment) }

        binding.btnLogout.setOnClickListener {
            requireContext().app.authRepository.logout()
            val intent = Intent(requireContext(), SplashActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    override fun onResume() {
        super.onResume()
        loadUser()
    }

    private fun loadUser() {
        viewLifecycleOwner.lifecycleScope.launch {
            val user = requireContext().app.authRepository.currentUser() ?: return@launch
            binding.txtName.text = user.fullName
            binding.txtEmail.text = user.email
            binding.txtPhone.text = user.phone
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
