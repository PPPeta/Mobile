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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import ru.stroykrep.app.R
import ru.stroykrep.app.app
import ru.stroykrep.app.data.entity.Product
import ru.stroykrep.app.databinding.DialogFilterBinding
import ru.stroykrep.app.databinding.FragmentProductsBinding
import ru.stroykrep.app.ui.common.ProductAdapter
import ru.stroykrep.app.ui.common.SpaceItemDecoration
import ru.stroykrep.app.util.SortOrder

class ProductsFragment : Fragment() {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!

    private var categoryId: Long = -1
    private var categoryName: String = ""

    private data class Filter(
        val query: String = "",
        val sort: String = SortOrder.NAME_ASC,
        val priceFrom: Double? = null,
        val priceTo: Double? = null,
        val materialContains: String = ""
    )

    private val filter = MutableStateFlow(Filter())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryId = arguments?.getLong("categoryId") ?: -1L
        categoryName = arguments?.getString("categoryName").orEmpty()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val nav = findNavController()

        binding.txtTitle.text = categoryName
        binding.btnBack.setOnClickListener { nav.navigateUp() }

        val adapter = ProductAdapter { product ->
            nav.navigate(
                R.id.action_global_productFragment,
                bundleOf("productId" to product.id)
            )
        }
        binding.rvProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            this.adapter = adapter
            addItemDecoration(SpaceItemDecoration(16, spanCount = 2))
        }

        binding.editSearch.doOnTextChanged { text ->
            val query = text.toString().trim()
            filter.value = filter.value.copy(query = query)
            adapter.highlightQuery = query
        }

        binding.btnSort.setOnClickListener { showSortDialog() }
        binding.btnFilter.setOnClickListener { showFilterDialog() }

        val repo = requireContext().app.shopRepository
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                combine(repo.observeProducts(categoryId), filter) { products, f ->
                    applyFilter(products, f)
                }.collect { list ->
                    adapter.submitList(list)
                    binding.txtEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
                }
            }
        }
    }

    private fun applyFilter(src: List<Product>, f: Filter): List<Product> {
        val filtered = src.asSequence()
            .filter { f.query.isEmpty() || it.name.contains(f.query, ignoreCase = true) }
            .filter { f.priceFrom == null || it.price >= f.priceFrom }
            .filter { f.priceTo == null || it.price <= f.priceTo }
            .filter {
                f.materialContains.isEmpty() ||
                    it.material.contains(f.materialContains, ignoreCase = true)
            }
            .toList()
        return when (f.sort) {
            SortOrder.NAME_ASC -> filtered.sortedBy { it.name }
            SortOrder.PRICE_ASC -> filtered.sortedBy { it.price }
            SortOrder.PRICE_DESC -> filtered.sortedByDescending { it.price }
            else -> filtered
        }
    }

    private fun showSortDialog() {
        val items = arrayOf(
            getString(R.string.sort_name_asc),
            getString(R.string.sort_price_asc),
            getString(R.string.sort_price_desc)
        )
        val current = when (filter.value.sort) {
            SortOrder.NAME_ASC -> 0
            SortOrder.PRICE_ASC -> 1
            SortOrder.PRICE_DESC -> 2
            else -> 0
        }
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.catalog_sort)
            .setSingleChoiceItems(items, current) { d, which ->
                val value = when (which) {
                    0 -> SortOrder.NAME_ASC
                    1 -> SortOrder.PRICE_ASC
                    else -> SortOrder.PRICE_DESC
                }
                filter.value = filter.value.copy(sort = value)
                d.dismiss()
            }
            .show()
    }

    private fun showFilterDialog() {
        val dialogBinding = DialogFilterBinding.inflate(layoutInflater)
        dialogBinding.editPriceFrom.setText(filter.value.priceFrom?.toString().orEmpty())
        dialogBinding.editPriceTo.setText(filter.value.priceTo?.toString().orEmpty())
        dialogBinding.editMaterial.setText(filter.value.materialContains)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.catalog_filter)
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.action_save) { _, _ ->
                filter.value = filter.value.copy(
                    priceFrom = dialogBinding.editPriceFrom.text?.toString()?.replace(',', '.')
                        ?.toDoubleOrNull(),
                    priceTo = dialogBinding.editPriceTo.text?.toString()?.replace(',', '.')
                        ?.toDoubleOrNull(),
                    materialContains = dialogBinding.editMaterial.text?.toString()?.trim().orEmpty()
                )
            }
            .setNegativeButton(R.string.action_cancel, null)
            .setNeutralButton("Сбросить") { _, _ ->
                filter.value = filter.value.copy(
                    priceFrom = null, priceTo = null, materialContains = ""
                )
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// Удобный extension для EditText
private fun com.google.android.material.textfield.TextInputEditText.doOnTextChanged(
    block: (CharSequence) -> Unit
) {
    addTextChangedListener(object : android.text.TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            block(s ?: "")
        }

        override fun afterTextChanged(s: android.text.Editable?) = Unit
    })
}
