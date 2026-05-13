package ru.stroykrep.app.ui.cart

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.stroykrep.app.R
import ru.stroykrep.app.app
import ru.stroykrep.app.databinding.FragmentPaymentBinding
import ru.stroykrep.app.util.Formatters
import ru.stroykrep.app.util.OrderStatus
import ru.stroykrep.app.util.Validators

/**
 * Имитация платёжного шлюза ЮKassa.
 * Номер карты проверяется по алгоритму Луна, но никакой реальный запрос не отправляется.
 */
class PaymentFragment : Fragment() {

    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!

    private var orderId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderId = arguments?.getLong("orderId") ?: -1L
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }

        // Форматирование: "1234 5678 9012 3456"
        binding.editCard.addTextChangedListener(CardNumberFormatter(binding.editCard))
        // Форматирование: "12/34"
        binding.editExpiry.addTextChangedListener(ExpiryFormatter(binding.editExpiry))

        // Подтягиваем сумму из заказа
        val ctx = requireContext()
        val repo = ctx.app.shopRepository
        viewLifecycleOwner.lifecycleScope.launch {
            val order = repo.getOrder(orderId)
            if (order != null) {
                binding.txtAmount.text = getString(
                    R.string.payment_amount,
                    Formatters.formatPrice(order.order.total)
                )
            }
        }

        binding.btnPay.setOnClickListener { pay() }
    }

    private fun pay() {
        val card = binding.editCard.text?.toString().orEmpty()
        val expiry = binding.editExpiry.text?.toString().orEmpty()
        val cvc = binding.editCvc.text?.toString().orEmpty()
        val holder = binding.editHolder.text?.toString()?.trim().orEmpty()

        binding.tilCard.error = null
        binding.tilExpiry.error = null
        binding.tilCvc.error = null
        binding.tilHolder.error = null

        var ok = true
        if (!Validators.isValidCardNumber(card)) {
            binding.tilCard.error = getString(R.string.payment_err_card); ok = false
        }
        if (!Validators.isValidExpiry(expiry)) {
            binding.tilExpiry.error = getString(R.string.payment_err_expiry); ok = false
        }
        if (!Validators.isValidCvc(cvc)) {
            binding.tilCvc.error = getString(R.string.payment_err_cvc); ok = false
        }
        if (holder.isBlank()) {
            binding.tilHolder.error = getString(R.string.err_required); ok = false
        }
        if (!ok) return

        binding.btnPay.visibility = View.INVISIBLE
        binding.progress.visibility = View.VISIBLE

        val repo = requireContext().app.shopRepository
        viewLifecycleOwner.lifecycleScope.launch {
            delay(1500) // имитация обработки платежа
            repo.updateOrderStatus(orderId, OrderStatus.PAID)
            findNavController().navigate(
                R.id.action_global_paymentSuccessFragment,
                bundleOf("orderId" to orderId)
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

/** Вставляет пробелы через каждые 4 цифры. */
private class CardNumberFormatter(
    private val et: com.google.android.material.textfield.TextInputEditText
) : TextWatcher {
    private var editing = false
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
    override fun afterTextChanged(s: Editable?) {
        if (editing || s == null) return
        val digits = s.toString().filter { it.isDigit() }.take(19)
        val formatted = digits.chunked(4).joinToString(" ")
        if (formatted != s.toString()) {
            editing = true
            et.setText(formatted)
            et.setSelection(formatted.length)
            editing = false
        }
    }
}

/** Форматирует в "MM/YY". */
private class ExpiryFormatter(
    private val et: com.google.android.material.textfield.TextInputEditText
) : TextWatcher {
    private var editing = false
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
    override fun afterTextChanged(s: Editable?) {
        if (editing || s == null) return
        val digits = s.toString().filter { it.isDigit() }.take(4)
        val formatted = if (digits.length >= 3) "${digits.substring(0, 2)}/${digits.substring(2)}"
        else digits
        if (formatted != s.toString()) {
            editing = true
            et.setText(formatted)
            et.setSelection(formatted.length)
            editing = false
        }
    }
}
