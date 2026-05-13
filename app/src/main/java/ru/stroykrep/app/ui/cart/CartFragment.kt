package ru.stroykrep.app.ui.cart

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
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import ru.stroykrep.app.R
import ru.stroykrep.app.app
import ru.stroykrep.app.databinding.FragmentCartBinding
import ru.stroykrep.app.util.Formatters

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val ctx = requireContext()
        val repo = ctx.app.shopRepository
        val userId = ctx.app.sessionManager.getCurrentUserId()

        val adapter = CartAdapter(
            onIncrement = { item ->
                viewLifecycleOwner.lifecycleScope.launch {
                    repo.addToCart(userId, item.product.id, delta = 1)
                }
            },
            onDecrement = { item ->
                viewLifecycleOwner.lifecycleScope.launch {
                    repo.addToCart(userId, item.product.id, delta = -1)
                }
            },
            onRemove = { item ->
                viewLifecycleOwner.lifecycleScope.launch {
                    repo.removeFromCart(userId, item.product.id)
                }
            }
        )

        binding.rvCart.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }

        binding.btnCheckout.setOnClickListener {
            if (adapter.itemCount == 0) {
                Toast.makeText(ctx, R.string.cart_empty, Toast.LENGTH_SHORT).show()
            } else {
                findNavController().navigate(R.id.action_global_checkoutFragment)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                repo.observeCart(userId).collect { items ->
                    adapter.submitList(items)
                    val total = items.sumOf { it.lineTotal }
                    binding.txtTotal.text = getString(
                        R.string.cart_total, Formatters.formatPrice(total)
                    )
                    val empty = items.isEmpty()
                    binding.txtEmpty.visibility = if (empty) View.VISIBLE else View.GONE
                    binding.layoutFooter.visibility = if (empty) View.GONE else View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
