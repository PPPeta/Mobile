package ru.stroykrep.app.util

object DeliveryType {
    const val PICKUP = "PICKUP"
    const val COURIER = "COURIER"
}

object PaymentType {
    const val COD = "COD"      // при получении
    const val ONLINE = "ONLINE" // картой онлайн
}

object OrderStatus {
    const val NEW = "NEW"
    const val PAID = "PAID"
    const val DELIVERY = "DELIVERY"
    const val DONE = "DONE"
}

object SortOrder {
    const val NAME_ASC = "NAME_ASC"
    const val PRICE_ASC = "PRICE_ASC"
    const val PRICE_DESC = "PRICE_DESC"
}
