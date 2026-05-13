package ru.stroykrep.app.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.stroykrep.app.R
import ru.stroykrep.app.data.entity.Product
import ru.stroykrep.app.databinding.ItemProductCardBinding
import ru.stroykrep.app.util.Formatters

class ProductAdapter(
    private val onClick: (Product) -> Unit
) : ListAdapter<Product, ProductAdapter.VH>(DIFF) {

    inner class VH(val binding: ItemProductCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemProductCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        val ctx = holder.itemView.context
        holder.binding.txtName.text = item.name
        holder.binding.txtPrice.text = ctx.getString(R.string.product_price, Formatters.formatPrice(item.price))
        val imgId = ctx.drawableIdByName(item.imageRes)
        holder.binding.imgProduct.setImageResource(imgId)
        holder.binding.root.setOnClickListener { onClick(item) }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(a: Product, b: Product) = a.id == b.id
            override fun areContentsTheSame(a: Product, b: Product) = a == b
        }
    }
}
