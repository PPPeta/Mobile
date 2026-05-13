package ru.stroykrep.app.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import ru.stroykrep.app.R
import ru.stroykrep.app.app
import ru.stroykrep.app.databinding.FragmentCheckoutBinding
import ru.stroykrep.app.util.DeliveryType
import ru.stroykrep.app.util.Formatters
import ru.stroykrep.app.util.OrderStatus
import ru.stroykrep.app.util.PaymentType
import ru.stroykrep.app.util.Validators

class CheckoutFragment : Fragment() {

    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val ctx = requireContext()
        val userId = ctx.app.sessionManager.getCurrentUserId()

        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        // Prefill from current user
        viewLifecycleOwner.lifecycleScope.launch {
            val user = ctx.app.authRepository.currentUser()
            if (user != null) {
                binding.editName.setText(user.fullName)
                binding.editPhone.setText(user.phone)
            }
        }

        // Delivery radio -> show address field only for courier
        binding.rgDelivery.setOnCheckedChangeListener { _, checkedId ->
            binding.tilAddress.visibility =
                if (checkedId == R.id.rbCourier) View.VISIBLE else View.GONE
        }

        // Total
        viewLifecycleOwner.lifecycleScope.launch {
            val items = ctx.app.database.cartDao().getCart(userId)
            val total = items.sumOf { it.lineTotal }
            binding.txtTotal.text = getString(R.string.cart_total, Formatters.formatPrice(total))
        }

        binding.btnConfirm.setOnClickListener { submit() }
    }

    private fun submit() {
        val ctx = requireContext()
        val repo = ctx.app.shopRepository
        val userId = ctx.app.sessionManager.getCurrentUserId()

        val name = binding.editName.text?.toString()?.trim().orEmpty()
        val phone = binding.editPhone.text?.toString()?.trim().orEmpty()
        val deliveryCourier = binding.rbCourier.isChecked
        val address = binding.editAddress.text?.toString()?.trim().orEmpty()
        val paymentOnline = binding.rbOnline.isChecked
        val comment = binding.editComment.text?.toString()?.trim().orEmpty()

        binding.tilName.error = null
        binding.tilPhone.error = null
        binding.tilAddress.error = null

        var ok = true
        if (name.isBlank()) {
            binding.tilName.error = getString(R.string.err_required); ok = false
        }
        if (!Validators.isValidPhone(phone)) {
            binding.tilPhone.error = getString(R.string.err_phone); ok = false
        }
        if (deliveryCourier && address.isBlank()) {
            binding.tilAddress.error = getString(R.string.err_required); ok = false
        }
        if (!ok) return

        binding.btnConfirm.isEnabled = false

        val deliveryType = if (deliveryCourier) DeliveryType.COURIER else DeliveryType.PICKUP
        val paymentType = if (paymentOnline) PaymentType.ONLINE else PaymentType.COD

        viewLifecycleOwner.lifecycleScope.launch {
            val orderId = repo.createOrderFromCart(
                userId = userId,
                recipientName = name,
                recipientPhone = phone,
                deliveryType = deliveryType,
                address = address,
                paymentType = paymentType,
                comment = comment,
                status = OrderStatus.NEW
            )
            val nav = findNavController()
            if (paymentOnline) {
                nav.navigate(
                    R.id.action_global_paymentFragment,
                    bundleOf("orderId" to orderId)
                )
            } else {
                nav.navigate(
                    R.id.action_global_paymentSuccessFragment,
                    bundleOf("orderId" to orderId)
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
