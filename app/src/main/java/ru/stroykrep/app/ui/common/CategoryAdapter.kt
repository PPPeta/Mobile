package ru.stroykrep.app.ui.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.stroykrep.app.data.entity.Category
import ru.stroykrep.app.databinding.ItemCategoryTileBinding

class CategoryAdapter(
    private val onClick: (Category) -> Unit
) : ListAdapter<Category, CategoryAdapter.VH>(DIFF) {

    /** Количество товаров в каждой категории (categoryId -> count). */
    var productCounts: Map<Long, Int> = emptyMap()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class VH(val binding: ItemCategoryTileBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemCategoryTileBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        holder.binding.txtName.text = item.name
        val iconId = holder.itemView.context.drawableIdByName(item.iconRes)
        holder.binding.imgIcon.setImageResource(iconId)

        val count = productCounts[item.id]
        if (count != null && count > 0) {
            holder.binding.txtCount.visibility = View.VISIBLE
            holder.binding.txtCount.text = pluralProducts(count)
        } else {
            holder.binding.txtCount.visibility = View.GONE
        }

        holder.binding.root.setOnClickListener { onClick(item) }
    }

    private fun pluralProducts(n: Int): String {
        val rem10 = n % 10
        val rem100 = n % 100
        val word = when {
            rem100 in 11..14 -> "товаров"
            rem10 == 1 -> "товар"
            rem10 in 2..4 -> "товара"
            else -> "товаров"
        }
        return "$n $word"
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Category>() {
            override fun areItemsTheSame(a: Category, b: Category) = a.id == b.id
            override fun areContentsTheSame(a: Category, b: Category) = a == b
        }
    }
}
