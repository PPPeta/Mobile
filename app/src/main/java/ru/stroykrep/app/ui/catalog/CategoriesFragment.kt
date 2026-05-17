package ru.stroykrep.app.ui.catalog

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
import kotlinx.coroutines.launch
import ru.stroykrep.app.R
import ru.stroykrep.app.app
import ru.stroykrep.app.databinding.FragmentCategoriesBinding
import ru.stroykrep.app.ui.common.CategoryAdapter

class CategoriesFragment : Fragment() {

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val nav = findNavController()
        val adapter = CategoryAdapter { category ->
            nav.navigate(
                R.id.action_global_productsFragment,
                bundleOf("categoryId" to category.id, "categoryName" to category.name)
            )
        }
        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            this.adapter = adapter
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                requireContext().app.shopRepository
                    .observeCategories()
                    .collect { adapter.submitList(it) }
            }
        }

        // Количество товаров в каждой категории
        viewLifecycleOwner.lifecycleScope.launch {
            val counts = requireContext().app.database.productDao().countPerCategory()
            adapter.productCounts = counts.associate { it.categoryId to it.cnt }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
