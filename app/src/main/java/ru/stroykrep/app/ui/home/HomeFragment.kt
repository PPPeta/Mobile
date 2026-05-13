package ru.stroykrep.app.ui.home

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
