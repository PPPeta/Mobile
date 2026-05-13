package ru.stroykrep.app.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import ru.stroykrep.app.R
import ru.stroykrep.app.app
import ru.stroykrep.app.data.entity.OrderItem
import ru.stroykrep.app.data.model.OrderWithItems
import ru.stroykrep.app.databinding.FragmentOrderDetailsBinding
import ru.stroykrep.app.databinding.ItemOrderDetailBinding
import ru.stroykrep.app.ui.common.drawableIdByName
import ru.stroykrep.app.util.DeliveryType
import ru.stroykrep.app.util.Formatters
import ru.stroykrep.app.util.PaymentType

class OrderDetailsFragment : Fragment() {

    private var _binding: FragmentOrderDetailsBinding? = null
    private val binding get() = _binding!!

    private var orderId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderId = arguments?.getLong("orderId") ?: -1L
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        val ctx = requireContext()
        viewLifecycleOwner.lifecycleScope.launch {
            val order = ctx.app.shopRepository.getOrder(orderId) ?: return@launch
            bind(order)
        }

        binding.btnRepeat.setOnClickListener {
            val userId = ctx.app.sessionManager.getCurrentUserId()
            viewLifecycleOwner.lifecycleScope.launch {
                ctx.app.shopRepository.repeatOrder(userId, orderId)
                Toast.makeText(ctx, "Товары добавлены в корзину", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.cartFragment)
            }
        }
    }

    private fun bind(data: OrderWithItems) {
        val ctx = requireContext()
        binding.txtNumber.text = getString(R.string.order_number, data.order.id)
        binding.txtDate.text = Formatters.formatDate(data.order.createdAt)
        binding.txtRecipient.text = "${data.order.recipientName}\n${data.order.recipientPhone}"

        val delivery = when (data.order.deliveryType) {
            DeliveryType.PICKUP -> getString(R.string.checkout_delivery_pickup)
            DeliveryType.COURIER -> getString(R.string.checkout_delivery_courier) +
                if (data.order.address.isNotBlank()) " — ${data.order.address}" else ""
            else -> data.order.deliveryType
        }
        binding.txtDelivery.text = "Доставка: $delivery"

        val payment = when (data.order.paymentType) {
            PaymentType.COD -> getString(R.string.checkout_payment_cod)
            PaymentType.ONLINE -> getString(R.string.checkout_payment_online)
            else -> data.order.paymentType
        }
        binding.txtPayment.text = "Оплата: $payment"
        binding.txtComment.text =
            "Комментарий: ${if (data.order.comment.isBlank()) "—" else data.order.comment}"

        binding.txtTotal.text = getString(
            R.string.cart_total, Formatters.formatPrice(data.order.total)
        )

        binding.chipStatus.text = OrderStatusUi.label(ctx, data.order.status)
        val bg = binding.chipStatus.background.mutate()
        DrawableCompat.setTint(bg, ContextCompat.getColor(ctx, OrderStatusUi.color(data.order.status)))
        binding.chipStatus.background = bg

        // Items
        binding.layoutItems.removeAllViews()
        data.items.forEach { item -> addItemRow(item) }
    }

    private fun addItemRow(item: OrderItem) {
        val ctx = requireContext()
        val row = ItemOrderDetailBinding.inflate(layoutInflater, binding.layoutItems, false)
        row.txtName.text = item.productName
        row.txtQty.text = "${item.quantity} шт × ${Formatters.formatPrice(item.price)} ₽"
        row.txtLineTotal.text = "${Formatters.formatPrice(item.price * item.quantity)} ₽"
        row.imgProduct.setImageResource(ctx.drawableIdByName(item.productImageRes))
        binding.layoutItems.addView(row.root)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
