package ru.stroykrep.app.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.stroykrep.app.R
import ru.stroykrep.app.databinding.FragmentPaymentSuccessBinding

class PaymentSuccessFragment : Fragment() {

    private var _binding: FragmentPaymentSuccessBinding? = null
    private val binding get() = _binding!!

    private var orderId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderId = arguments?.getLong("orderId") ?: -1L
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentSuccessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.txtOrderNumber.text = getString(R.string.order_number, orderId)
        val nav = findNavController()
        binding.btnOrders.setOnClickListener {
            nav.navigate(
                R.id.action_global_ordersFragment,
                null,
                androidx.navigation.NavOptions.Builder()
                    .setPopUpTo(R.id.homeFragment, false)
                    .build()
            )
        }
        binding.btnHome.setOnClickListener {
            nav.popBackStack(R.id.homeFragment, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
