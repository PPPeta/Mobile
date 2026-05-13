package ru.stroykrep.app.ui.orders

import android.content.Context
import ru.stroykrep.app.R
import ru.stroykrep.app.util.OrderStatus

object OrderStatusUi {

    fun label(ctx: Context, status: String): String = when (status) {
        OrderStatus.NEW -> ctx.getString(R.string.order_status_new)
        OrderStatus.PAID -> ctx.getString(R.string.order_status_paid)
        OrderStatus.DELIVERY -> ctx.getString(R.string.order_status_delivery)
        OrderStatus.DONE -> ctx.getString(R.string.order_status_done)
        else -> status
    }

    fun color(status: String): Int = when (status) {
        OrderStatus.NEW -> R.color.status_new
        OrderStatus.PAID -> R.color.status_paid
        OrderStatus.DELIVERY -> R.color.status_delivery
        OrderStatus.DONE -> R.color.status_done
        else -> R.color.status_new
    }
}
