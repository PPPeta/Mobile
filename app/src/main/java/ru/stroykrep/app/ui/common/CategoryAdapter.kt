package ru.stroykrep.app.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.stroykrep.app.data.entity.Category
import ru.stroykrep.app.databinding.ItemCategoryTileBinding

class CategoryAdapter(
    private val onClick: (Category) -> Unit
) : ListAdapter<Category, CategoryAdapter.VH>(DIFF) {

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
        holder.binding.root.setOnClickListener { onClick(item) }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Category>() {
            override fun areItemsTheSame(a: Category, b: Category) = a.id == b.id
            override fun areContentsTheSame(a: Category, b: Category) = a == b
        }
    }
}
