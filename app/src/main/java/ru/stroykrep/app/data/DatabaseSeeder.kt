package ru.stroykrep.app.data

import ru.stroykrep.app.data.entity.Category
import ru.stroykrep.app.data.entity.Product

/**
 * Первичное заполнение БД данными о категориях и товарах.
 * Вызывается при первом запуске приложения, если таблицы пустые.
 */
object DatabaseSeeder {

    suspend fun seedIfEmpty(db: AppDatabase) {
        val categoryDao = db.categoryDao()
        val productDao = db.productDao()

        if (categoryDao.count() > 0) return

        val categories = listOf(
            Category(id = 1, name = "Саморезы", iconRes = "ic_bolt"),
            Category(id = 2, name = "Болты", iconRes = "ic_bolt"),
            Category(id = 3, name = "Гайки", iconRes = "ic_bolt"),
            Category(id = 4, name = "Шайбы", iconRes = "ic_bolt"),
            Category(id = 5, name = "Анкеры", iconRes = "ic_bolt"),
            Category(id = 6, name = "Дюбели", iconRes = "ic_bolt"),
            Category(id = 7, name = "Гвозди", iconRes = "ic_bolt"),
            Category(id = 8, name = "Хомуты", iconRes = "ic_bolt")
        )
        categoryDao.insertAll(categories)

        val products = listOf(
            // Саморезы
            Product(
                categoryId = 1, name = "Саморез по дереву 3.5x35",
                sku = "СМ-3.5-35", price = 1.20,
                standard = "DIN 7505",
                size = "3.5 x 35 мм",
                material = "Сталь",
                coating = "Фосфатированное",
                description = "Саморез по дереву с потайной головкой, шлиц PZ2. Применяется для крепления древесины, ДСП, ОСБ.",
                imageRes = "ic_placeholder_product",
                isPopular = true
            ),
            Product(
                categoryId = 1, name = "Саморез по дереву 4.2x70",
                sku = "СМ-4.2-70", price = 2.80,
                standard = "DIN 7505",
                size = "4.2 x 70 мм",
                material = "Сталь",
                coating = "Жёлтый цинк",
                description = "Универсальный саморез для сквозного крепления деревянных конструкций.",
                imageRes = "ic_placeholder_product"
            ),
            Product(
                categoryId = 1, name = "Саморез по металлу 4.2x16",
                sku = "СМ-Мт-4.2-16", price = 1.50,
                standard = "DIN 7504",
                size = "4.2 x 16 мм",
                material = "Сталь",
                coating = "Оцинкованное",
                description = "Саморез со сверлом по металлу, для крепления профлиста и металлоконструкций.",
                imageRes = "ic_placeholder_product",
                isPopular = true
            ),
            Product(
                categoryId = 1, name = "Саморез с пресс-шайбой 4.2x19",
                sku = "СМ-ПШ-4.2-19", price = 1.80,
                standard = "DIN 968",
                size = "4.2 x 19 мм",
                material = "Сталь",
                coating = "Оцинкованное",
                description = "Саморез с пресс-шайбой для крепления тонколистовых материалов.",
                imageRes = "ic_placeholder_product"
            ),

            // Болты
            Product(
                categoryId = 2, name = "Болт М8x40 с шестигранной головкой",
                sku = "БТ-М8-40", price = 9.50,
                standard = "ГОСТ 7798-70",
                size = "М8 x 40 мм",
                material = "Сталь 5.8",
                coating = "Оцинкованное",
                description = "Болт с шестигранной головкой, класс прочности 5.8. Резьба метрическая, шаг 1.25.",
                imageRes = "ic_placeholder_product",
                isPopular = true
            ),
            Product(
                categoryId = 2, name = "Болт М10x60 с шестигранной головкой",
                sku = "БТ-М10-60", price = 15.00,
                standard = "ГОСТ 7798-70",
                size = "М10 x 60 мм",
                material = "Сталь 8.8",
                coating = "Оцинкованное",
                description = "Болт повышенной прочности для ответственных соединений.",
                imageRes = "ic_placeholder_product"
            ),
            Product(
                categoryId = 2, name = "Болт мебельный М6x50",
                sku = "БТ-Мб-М6-50", price = 6.00,
                standard = "DIN 603",
                size = "М6 x 50 мм",
                material = "Сталь",
                coating = "Оцинкованное",
                description = "Болт с полукруглой головкой и квадратным подголовком для сборки мебели.",
                imageRes = "ic_placeholder_product"
            ),

            // Гайки
            Product(
                categoryId = 3, name = "Гайка шестигранная М8",
                sku = "ГК-М8", price = 2.00,
                standard = "ГОСТ 5915-70",
                size = "М8",
                material = "Сталь",
                coating = "Оцинкованное",
                description = "Гайка шестигранная, класс прочности 6. Для сборки болтовых соединений.",
                imageRes = "ic_placeholder_product",
                isPopular = true
            ),
            Product(
                categoryId = 3, name = "Гайка шестигранная М10",
                sku = "ГК-М10", price = 3.00,
                standard = "ГОСТ 5915-70",
                size = "М10",
                material = "Сталь",
                coating = "Оцинкованное",
                description = "Гайка под болт М10, стандартная.",
                imageRes = "ic_placeholder_product"
            ),
            Product(
                categoryId = 3, name = "Гайка колпачковая М8",
                sku = "ГК-Кп-М8", price = 4.50,
                standard = "DIN 1587",
                size = "М8",
                material = "Сталь",
                coating = "Никелированное",
                description = "Гайка с закрытым куполом — закрывает торец резьбы, декоративная.",
                imageRes = "ic_placeholder_product"
            ),

            // Шайбы
            Product(
                categoryId = 4, name = "Шайба плоская М8",
                sku = "ШБ-М8", price = 0.80,
                standard = "ГОСТ 11371-78",
                size = "М8 (d=8.4, D=16)",
                material = "Сталь",
                coating = "Оцинкованное",
                description = "Шайба плоская для равномерного распределения нагрузки.",
                imageRes = "ic_placeholder_product"
            ),
            Product(
                categoryId = 4, name = "Шайба пружинная (гровер) М8",
                sku = "ШБ-Гр-М8", price = 1.00,
                standard = "ГОСТ 6402-70",
                size = "М8",
                material = "Пружинная сталь",
                coating = "Оцинкованное",
                description = "Шайба-гровер, предотвращает самопроизвольное откручивание соединения.",
                imageRes = "ic_placeholder_product",
                isPopular = true
            ),
            Product(
                categoryId = 4, name = "Шайба увеличенная М10",
                sku = "ШБ-Ув-М10", price = 1.80,
                standard = "DIN 9021",
                size = "М10 (d=10.5, D=30)",
                material = "Сталь",
                coating = "Оцинкованное",
                description = "Шайба увеличенного диаметра для крепления к мягким материалам.",
                imageRes = "ic_placeholder_product"
            ),

            // Анкеры
            Product(
                categoryId = 5, name = "Анкер клиновой М10x100",
                sku = "АН-М10-100", price = 45.00,
                standard = "—",
                size = "М10 x 100 мм",
                material = "Сталь",
                coating = "Оцинкованное",
                description = "Клиновой анкер для крепления в бетоне и полнотелом кирпиче.",
                imageRes = "ic_placeholder_product",
                isPopular = true
            ),
            Product(
                categoryId = 5, name = "Анкер-болт с гайкой М12x120",
                sku = "АН-БТ-М12-120", price = 85.00,
                standard = "—",
                size = "М12 x 120 мм",
                material = "Сталь",
                coating = "Оцинкованное",
                description = "Анкер-болт с шестигранной головкой и распорной втулкой.",
                imageRes = "ic_placeholder_product"
            ),
            Product(
                categoryId = 5, name = "Анкер забивной М8",
                sku = "АН-Зб-М8", price = 15.00,
                standard = "—",
                size = "М8",
                material = "Сталь",
                coating = "Оцинкованное",
                description = "Забивной анкер для крепления резьбовых шпилек в бетон.",
                imageRes = "ic_placeholder_product"
            ),

            // Дюбели
            Product(
                categoryId = 6, name = "Дюбель распорный 6x40",
                sku = "ДБ-6-40", price = 1.20,
                standard = "—",
                size = "6 x 40 мм",
                material = "Полипропилен",
                coating = "—",
                description = "Распорный нейлоновый дюбель для бетона и кирпича. Используется с шурупом 4-5 мм.",
                imageRes = "ic_placeholder_product",
                isPopular = true
            ),
            Product(
                categoryId = 6, name = "Дюбель-гвоздь 6x40",
                sku = "ДБ-Гв-6-40", price = 2.00,
                standard = "—",
                size = "6 x 40 мм",
                material = "Полипропилен + сталь",
                coating = "Оцинкованное",
                description = "Дюбель-гвоздь с потайным бортиком для быстрого монтажа.",
                imageRes = "ic_placeholder_product"
            ),
            Product(
                categoryId = 6, name = "Дюбель для газобетона 8x50",
                sku = "ДБ-Гз-8-50", price = 5.00,
                standard = "—",
                size = "8 x 50 мм",
                material = "Полиамид",
                coating = "—",
                description = "Специальный дюбель для пористых материалов (газобетон, пенобетон).",
                imageRes = "ic_placeholder_product"
            ),

            // Гвозди
            Product(
                categoryId = 7, name = "Гвозди строительные 3.0x70, 1 кг",
                sku = "ГВ-3.0-70", price = 120.00,
                standard = "ГОСТ 4028-63",
                size = "3.0 x 70 мм",
                material = "Сталь",
                coating = "—",
                description = "Гвозди строительные с плоской головкой, упаковка 1 кг.",
                imageRes = "ic_placeholder_product"
            ),
            Product(
                categoryId = 7, name = "Гвозди финишные 1.6x40, 0.5 кг",
                sku = "ГВ-Фн-1.6-40", price = 95.00,
                standard = "—",
                size = "1.6 x 40 мм",
                material = "Сталь",
                coating = "Оцинкованное",
                description = "Финишные гвозди с маленькой головкой для декоративных работ.",
                imageRes = "ic_placeholder_product"
            ),
            Product(
                categoryId = 7, name = "Гвозди шиферные 4.0x100, 1 кг",
                sku = "ГВ-Шф-4.0-100", price = 180.00,
                standard = "ГОСТ 7811-7",
                size = "4.0 x 100 мм",
                material = "Сталь",
                coating = "Оцинкованное",
                description = "Гвозди с резиновой шайбой для крепления шиферных листов.",
                imageRes = "ic_placeholder_product"
            ),

            // Хомуты
            Product(
                categoryId = 8, name = "Хомут червячный 16-27 мм",
                sku = "ХМ-Чр-16-27", price = 18.00,
                standard = "W2",
                size = "16-27 мм",
                material = "Нерж. сталь",
                coating = "—",
                description = "Хомут металлический червячный для шлангов и трубопроводов.",
                imageRes = "ic_placeholder_product"
            ),
            Product(
                categoryId = 8, name = "Хомут пластиковый 3.6x200 (100 шт)",
                sku = "ХМ-Пл-3.6-200", price = 75.00,
                standard = "—",
                size = "3.6 x 200 мм",
                material = "Нейлон",
                coating = "—",
                description = "Стяжка-хомут из нейлона, упаковка 100 шт. Для связывания кабелей и проводов.",
                imageRes = "ic_placeholder_product",
                isPopular = true
            ),
            Product(
                categoryId = 8, name = "Хомут металлический сантехнический 3/4\"",
                sku = "ХМ-Мт-3-4", price = 35.00,
                standard = "—",
                size = "3/4\"",
                material = "Оцинк. сталь",
                coating = "Оцинкованное",
                description = "Металлический хомут с резиновой прокладкой для крепления сантехнических труб.",
                imageRes = "ic_placeholder_product"
            )
        )
        productDao.insertAll(products)
    }
}
