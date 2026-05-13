package ru.stroykrep.app.ui.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.stroykrep.app.R
import ru.stroykrep.app.data.model.OrderWithItems
import ru.stroykrep.app.databinding.ItemOrderBinding
import ru.stroykrep.app.util.Formatters

class OrdersAdapter(
    private val onClick: (OrderWithItems) -> Unit
) : ListAdapter<OrderWithItems, OrdersAdapter.VH>(DIFF) {

    inner class VH(val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        val ctx = holder.itemView.context
        holder.binding.txtNumber.text = ctx.getString(R.string.order_number, item.order.id)
        holder.binding.txtDate.text = ctx.getString(
            R.string.order_date,
            Formatters.formatDate(item.order.createdAt)
        )
        holder.binding.txtSum.text = ctx.getString(
            R.string.order_sum,
            Formatters.formatPrice(item.order.total)
        )

        holder.binding.chipStatus.text = OrderStatusUi.label(ctx, item.order.status)
        val bg = holder.binding.chipStatus.background.mutate()
        DrawableCompat.setTint(
            bg, ContextCompat.getColor(ctx, OrderStatusUi.color(item.order.status))
        )
        holder.binding.chipStatus.background = bg

        holder.binding.root.setOnClickListener { onClick(item) }
    }

    companion object {
        val DIFF = object : DiffUtil.ItemCallback<OrderWithItems>() {
            override fun areItemsTheSame(a: OrderWithItems, b: OrderWithItems) =
                a.order.id == b.order.id
            override fun areContentsTheSame(a: OrderWithItems, b: OrderWithItems) = a == b
        }
    }
}
