package ru.stroykrep.app.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.stroykrep.app.R
import ru.stroykrep.app.databinding.FragmentContactsBinding

class ContactsFragment : Fragment() {

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        // Клик по карточке адреса → открыть в картах
        binding.cardShop1.setOnClickListener {
            openMap(getString(R.string.contacts_shop1_address))
        }
        binding.cardShop2.setOnClickListener {
            openMap(getString(R.string.contacts_shop2_address))
        }

        // Клик по телефону → открыть звонилку
        binding.cardPhone.setOnClickListener {
            dial(getString(R.string.contacts_phone_value))
        }

        // Клик по email → открыть почтовый клиент
        binding.cardEmail.setOnClickListener {
            email(getString(R.string.contacts_email_value))
        }
    }

    private fun openMap(address: String) {
        val uri = Uri.parse("geo:0,0?q=${Uri.encode(address)}")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        try {
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Приложение карт не найдено", Toast.LENGTH_SHORT).show()
        }
    }

    private fun dial(phone: String) {
        val uri = Uri.parse("tel:${phone.replace(" ", "").replace("(", "").replace(")", "").replace("-", "")}")
        val intent = Intent(Intent.ACTION_DIAL, uri)
        try {
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Не удалось открыть звонилку", Toast.LENGTH_SHORT).show()
        }
    }

    private fun email(addr: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$addr")
        }
        try {
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Почтовый клиент не найден", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
