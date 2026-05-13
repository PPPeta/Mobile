package ru.stroykrep.app.ui.orders

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
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import ru.stroykrep.app.R
import ru.stroykrep.app.app
import ru.stroykrep.app.databinding.FragmentOrdersBinding

class OrdersFragment : Fragment() {

    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val nav = findNavController()
        binding.btnBack.setOnClickListener { nav.navigateUp() }

        val adapter = OrdersAdapter { order ->
            nav.navigate(
                R.id.action_global_orderDetailsFragment,
                bundleOf("orderId" to order.order.id)
            )
        }
        binding.rvOrders.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }

        val ctx = requireContext()
        val userId = ctx.app.sessionManager.getCurrentUserId()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                ctx.app.shopRepository.observeOrders(userId).collect { list ->
                    adapter.submitList(list)
                    binding.txtEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
