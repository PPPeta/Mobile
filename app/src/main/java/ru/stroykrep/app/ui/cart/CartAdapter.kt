package ru.stroykrep.app.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.stroykrep.app.R
import ru.stroykrep.app.data.model.CartProduct
import ru.stroykrep.app.databinding.ItemCartBinding
import ru.stroykrep.app.ui.common.drawableIdByName
import ru.stroykrep.app.util.Formatters

class CartAdapter(
    private val onIncrement: (CartProduct) -> Unit,
    private val onDecrement: (CartProduct) -> Unit,
    private val onRemove: (CartProduct) -> Unit
) : ListAdapter<CartProduct, CartAdapter.VH>(DIFF) {

    inner class VH(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        val ctx = holder.itemView.context
        holder.binding.txtName.text = item.product.name
        holder.binding.txtPrice.text = ctx.getString(
            R.string.product_price,
            Formatters.formatPrice(item.lineTotal)
        )
        holder.binding.txtQty.text = item.quantity.toString()
        holder.binding.imgProduct.setImageResource(ctx.drawableIdByName(item.product.imageRes))

        holder.binding.btnPlus.setOnClickListener { onIncrement(item) }
        holder.binding.btnMinus.setOnClickListener { onDecrement(item) }
        holder.binding.btnRemove.setOnClickListener { onRemove(item) }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<CartProduct>() {
            override fun areItemsTheSame(a: CartProduct, b: CartProduct) =
                a.product.id == b.product.id
            override fun areContentsTheSame(a: CartProduct, b: CartProduct) = a == b
        }
    }
}
