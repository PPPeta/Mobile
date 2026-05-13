package ru.stroykrep.app.ui.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import ru.stroykrep.app.R
import ru.stroykrep.app.app
import ru.stroykrep.app.data.entity.Product
import ru.stroykrep.app.databinding.FragmentProductBinding
import ru.stroykrep.app.ui.common.drawableIdByName
import ru.stroykrep.app.util.Formatters

class ProductFragment : Fragment() {

    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!

    private var productId: Long = -1
    private var product: Product? = null
    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productId = arguments?.getLong("productId") ?: -1L
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        val ctx = requireContext()
        val repo = ctx.app.shopRepository
        val userId = ctx.app.sessionManager.getCurrentUserId()

        viewLifecycleOwner.lifecycleScope.launch {
            val p = repo.getProduct(productId) ?: return@launch
            product = p
            bind(p)
        }

        binding.btnAddToCart.setOnClickListener {
            val p = product ?: return@setOnClickListener
            viewLifecycleOwner.lifecycleScope.launch {
                repo.addToCart(userId, p.id, delta = 1)
                Toast.makeText(ctx, ctx.getString(R.string.product_in_cart), Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnFavorite.setOnClickListener {
            val p = product ?: return@setOnClickListener
            viewLifecycleOwner.lifecycleScope.launch {
                repo.toggleFavorite(userId, p.id, isFavorite)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                repo.observeIsFavorite(userId, productId).collect { fav ->
                    isFavorite = fav
                    binding.btnFavorite.setImageResource(
                        if (fav) R.drawable.ic_favorite else R.drawable.ic_favorite_border
                    )
                }
            }
        }
    }

    private fun bind(p: Product) {
        val ctx = requireContext()
        binding.txtName.text = p.name
        binding.txtSku.text = getString(R.string.product_sku, p.sku)
        binding.txtPrice.text = getString(R.string.product_price, Formatters.formatPrice(p.price))
        binding.imgProduct.setImageResource(ctx.drawableIdByName(p.imageRes))
        binding.txtDescription.text = p.description

        fun row(include: ru.stroykrep.app.databinding.RowCharacteristicBinding, label: Int, value: String) {
            include.txtLabel.text = getString(label)
            include.txtValue.text = value
        }
        row(binding.rowStandard, R.string.product_standard, p.standard)
        row(binding.rowSize, R.string.product_size, p.size)
        row(binding.rowMaterial, R.string.product_material, p.material)
        row(binding.rowCoating, R.string.product_coating, p.coating)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
