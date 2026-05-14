package ru.stroykrep.app.ui.common

import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.text.style.StyleSpan
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

    /** Если задан — части совпадения подсвечиваются в названии товара. */
    var highlightQuery: String = ""
        set(value) {
            if (field != value) {
                field = value
                notifyDataSetChanged()
            }
        }

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
        holder.binding.txtName.text = highlight(item.name)
        holder.binding.txtPrice.text = ctx.getString(R.string.product_price, Formatters.formatPrice(item.price))
        val imgId = ctx.drawableIdByName(item.imageRes)
        holder.binding.imgProduct.setImageResource(imgId)

        // Скидка для популярных: фейковая старая цена +20%
        if (item.isPopular) {
            holder.binding.badgeDiscount.visibility = android.view.View.VISIBLE
            holder.binding.txtPriceOld.visibility = android.view.View.VISIBLE
            val oldPrice = item.price * 1.2
            holder.binding.txtPriceOld.text =
                ctx.getString(R.string.product_price, Formatters.formatPrice(oldPrice))
            holder.binding.txtPriceOld.paintFlags =
                holder.binding.txtPriceOld.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.binding.badgeDiscount.visibility = android.view.View.GONE
            holder.binding.txtPriceOld.visibility = android.view.View.GONE
        }

        holder.binding.root.setOnClickListener { onClick(item) }
    }

    /** Подсвечивает совпадения с highlightQuery в строке. */
    private fun highlight(text: String): CharSequence {
        if (highlightQuery.isBlank()) return text
        val lower = text.lowercase()
        val q = highlightQuery.lowercase()
        val idx = lower.indexOf(q)
        if (idx < 0) return text
        val span = SpannableString(text)
        span.setSpan(
            BackgroundColorSpan(0xFFFFE082.toInt()),
            idx, idx + q.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        span.setSpan(
            StyleSpan(android.graphics.Typeface.BOLD),
            idx, idx + q.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return span
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(a: Product, b: Product) = a.id == b.id
            override fun areContentsTheSame(a: Product, b: Product) = a == b
        }
    }
}
