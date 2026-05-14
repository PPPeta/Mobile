package ru.stroykrep.app.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import ru.stroykrep.app.R
import ru.stroykrep.app.app
import ru.stroykrep.app.databinding.FragmentHomeBinding
import ru.stroykrep.app.ui.common.CategoryAdapter
import ru.stroykrep.app.ui.common.ProductAdapter
import java.util.Calendar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val nav = findNavController()

        val categoryAdapter = CategoryAdapter { category ->
            nav.navigate(
                R.id.action_global_productsFragment,
                bundleOf("categoryId" to category.id, "categoryName" to category.name)
            )
        }
        val popularAdapter = ProductAdapter { product ->
            nav.navigate(
                R.id.action_global_productFragment,
                bundleOf("productId" to product.id)
            )
        }

        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(requireContext(), 4)
            adapter = categoryAdapter
            isNestedScrollingEnabled = false
        }
        binding.rvPopular.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = popularAdapter
            isNestedScrollingEnabled = false
        }

        binding.txtAllCategories.setOnClickListener {
            nav.navigate(R.id.action_global_categoriesFragment)
        }

        // Quick actions
        binding.quickCall.setOnClickListener {
            val phone = getString(R.string.contacts_phone_value)
            val digits = phone.filter { it.isDigit() || it == '+' }
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$digits"))
            try { startActivity(intent) } catch (_: Exception) {}
        }
        binding.quickAddress.setOnClickListener {
            nav.navigate(R.id.action_global_contactsFragment)
        }
        binding.quickHours.setOnClickListener {
            nav.navigate(R.id.action_global_contactsFragment)
        }

        // Статус «открыто/закрыто» по часам
        updateOpenStatus()

        val repo = requireContext().app.shopRepository
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    repo.observeCategories().collect { categoryAdapter.submitList(it) }
                }
                launch {
                    repo.observePopular().collect { popularAdapter.submitList(it) }
                }
            }
        }
    }

    /**
     * Магазин «Строй Креп» в Шуе работает Пн-Пт 08:00–20:00, Сб-Вс 08:00–17:00.
     * Показываем актуальный статус прямо на главной.
     */
    private fun updateOpenStatus() {
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)
        val nowMinutes = hour * 60 + minute
        val day = cal.get(Calendar.DAY_OF_WEEK)
        val isWeekend = day == Calendar.SATURDAY || day == Calendar.SUNDAY

        val openMinutes = 8 * 60
        val closeMinutes = if (isWeekend) 17 * 60 else 20 * 60

        val isOpen = nowMinutes in openMinutes until closeMinutes
        if (isOpen) {
            binding.txtHoursStatus.text = "Сейчас открыто"
            binding.txtHoursStatus.setTextColor(requireContext().getColor(R.color.status_paid))
        } else {
            binding.txtHoursStatus.text = "Сейчас закрыто"
            binding.txtHoursStatus.setTextColor(requireContext().getColor(R.color.error))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
