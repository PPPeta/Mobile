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
            Category(id = 1, name = "Саморезы", iconRes = "ic_cat_samorez"),
            Category(id = 2, name = "Болты", iconRes = "ic_cat_bolt"),
            Category(id = 3, name = "Гайки", iconRes = "ic_cat_gaika"),
            Category(id = 4, name = "Шайбы", iconRes = "ic_cat_shaiba"),
            Category(id = 5, name = "Анкеры", iconRes = "ic_cat_anker"),
            Category(id = 6, name = "Дюбели", iconRes = "ic_cat_dubel"),
            Category(id = 7, name = "Гвозди", iconRes = "ic_cat_gvozd"),
            Category(id = 8, name = "Хомуты", iconRes = "ic_cat_homut")
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
                imageRes = "ic_cat_samorez",
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
                imageRes = "ic_cat_samorez"
            ),
            Product(
                categoryId = 1, name = "Саморез по металлу 4.2x16",
                sku = "СМ-Мт-4.2-16", price = 1.50,
                standard = "DIN 7504",
                size = "4.2 x 16 мм",
                material = "Сталь",
                coating = "Оцинкованное",
                description = "Саморез со сверлом по металлу, для крепления профлиста и металлоконструкций.",
                imageRes = "ic_cat_samorez",
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
                imageRes = "ic_cat_samorez"
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
                imageRes = "ic_cat_bolt",
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
                imageRes = "ic_cat_bolt"
            ),
            Product(
                categoryId = 2, name = "Болт мебельный М6x50",
                sku = "БТ-Мб-М6-50", price = 6.00,
                standard = "DIN 603",
                size = "М6 x 50 мм",
                material = "Сталь",
                coating = "Оцинкованное",
                description = "Болт с полукруглой головкой и квадратным подголовком для сборки мебели.",
                imageRes = "ic_cat_bolt"
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
                imageRes = "ic_cat_gaika",
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
                imageRes = "ic_cat_gaika"
            ),
            Product(
                categoryId = 3, name = "Гайка колпачковая М8",
                sku = "ГК-Кп-М8", price = 4.50,
                standard = "DIN 1587",
                size = "М8",
                material = "Сталь",
                coating = "Никелированное",
                description = "Гайка с закрытым куполом — закрывает торец резьбы, декоративная.",
                imageRes = "ic_cat_gaika"
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
                imageRes = "ic_cat_shaiba"
            ),
            Product(
                categoryId = 4, name = "Шайба пружинная (гровер) М8",
                sku = "ШБ-Гр-М8", price = 1.00,
                standard = "ГОСТ 6402-70",
                size = "М8",
                material = "Пружинная сталь",
                coating = "Оцинкованное",
                description = "Шайба-гровер, предотвращает самопроизвольное откручивание соединения.",
                imageRes = "ic_cat_shaiba",
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
                imageRes = "ic_cat_shaiba"
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
                imageRes = "ic_cat_anker",
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
                imageRes = "ic_cat_anker"
            ),
            Product(
                categoryId = 5, name = "Анкер забивной М8",
                sku = "АН-Зб-М8", price = 15.00,
                standard = "—",
                size = "М8",
                material = "Сталь",
                coating = "Оцинкованное",
                description = "Забивной анкер для крепления резьбовых шпилек в бетон.",
                imageRes = "ic_cat_anker"
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
                imageRes = "ic_cat_dubel",
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
                imageRes = "ic_cat_dubel"
            ),
            Product(
                categoryId = 6, name = "Дюбель для газобетона 8x50",
                sku = "ДБ-Гз-8-50", price = 5.00,
                standard = "—",
                size = "8 x 50 мм",
                material = "Полиамид",
                coating = "—",
                description = "Специальный дюбель для пористых материалов (газобетон, пенобетон).",
                imageRes = "ic_cat_dubel"
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
                imageRes = "ic_cat_gvozd"
            ),
            Product(
                categoryId = 7, name = "Гвозди финишные 1.6x40, 0.5 кг",
                sku = "ГВ-Фн-1.6-40", price = 95.00,
                standard = "—",
                size = "1.6 x 40 мм",
                material = "Сталь",
                coating = "Оцинкованное",
                description = "Финишные гвозди с маленькой головкой для декоративных работ.",
                imageRes = "ic_cat_gvozd"
            ),
            Product(
                categoryId = 7, name = "Гвозди шиферные 4.0x100, 1 кг",
                sku = "ГВ-Шф-4.0-100", price = 180.00,
                standard = "ГОСТ 7811-7",
                size = "4.0 x 100 мм",
                material = "Сталь",
                coating = "Оцинкованное",
                description = "Гвозди с резиновой шайбой для крепления шиферных листов.",
                imageRes = "ic_cat_gvozd"
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
                imageRes = "ic_cat_homut"
            ),
            Product(
                categoryId = 8, name = "Хомут пластиковый 3.6x200 (100 шт)",
                sku = "ХМ-Пл-3.6-200", price = 75.00,
                standard = "—",
                size = "3.6 x 200 мм",
                material = "Нейлон",
                coating = "—",
                description = "Стяжка-хомут из нейлона, упаковка 100 шт. Для связывания кабелей и проводов.",
                imageRes = "ic_cat_homut",
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
                imageRes = "ic_cat_homut"
            ),

            // Доп. саморезы
            Product(
                categoryId = 1, name = "Саморез по гипсокартону 3.5x25",
                sku = "СМ-ГК-3.5-25", price = 0.90,
                standard = "DIN 18182",
                size = "3.5 x 25 мм",
                material = "Сталь",
                coating = "Фосфатированное",
                description = "Чёрный саморез с двухзаходной резьбой для крепления гипсокартона к металлическому профилю.",
                imageRes = "ic_cat_samorez",
                isPopular = true
            ),
            Product(
                categoryId = 1, name = "Саморез кровельный 4.8x35",
                sku = "СМ-Кр-4.8-35", price = 3.50,
                standard = "DIN 7504",
                size = "4.8 x 35 мм",
                material = "Сталь",
                coating = "Оцинк. + RAL",
                description = "Кровельный саморез с EPDM-шайбой и сверлом, окрашен по таблице RAL. Для металлочерепицы и профлиста.",
                imageRes = "ic_cat_samorez"
            ),
            Product(
                categoryId = 1, name = "Саморез универсальный 5x80",
                sku = "СМ-Ун-5-80", price = 4.20,
                standard = "DIN 7505",
                size = "5 x 80 мм",
                material = "Сталь",
                coating = "Жёлтый цинк",
                description = "Универсальный саморез под отвёртку PZ2 для дерева, ДСП и пластика.",
                imageRes = "ic_cat_samorez"
            ),

            // Доп. болты
            Product(
                categoryId = 2, name = "Болт М6x30 с шестигранной головкой",
                sku = "БТ-М6-30", price = 5.50,
                standard = "ГОСТ 7798-70",
                size = "М6 x 30 мм",
                material = "Сталь 5.8",
                coating = "Оцинкованное",
                description = "Стандартный болт под ключ S10. Применяется в общем машиностроении и сборке металлоконструкций.",
                imageRes = "ic_cat_bolt"
            ),
            Product(
                categoryId = 2, name = "Болт М12x80 с шестигранной головкой",
                sku = "БТ-М12-80", price = 28.00,
                standard = "ГОСТ 7798-70",
                size = "М12 x 80 мм",
                material = "Сталь 8.8",
                coating = "Оцинкованное",
                description = "Высокопрочный болт для ответственных нагруженных соединений.",
                imageRes = "ic_cat_bolt",
                isPopular = true
            ),
            Product(
                categoryId = 2, name = "Болт сантехнический (шуруп-глухарь) 8x80",
                sku = "БТ-Сн-8-80", price = 12.00,
                standard = "DIN 571",
                size = "8 x 80 мм",
                material = "Сталь",
                coating = "Оцинкованное",
                description = "Шестигранный шуруп-глухарь по дереву. Для крепления массивных деревянных конструкций.",
                imageRes = "ic_cat_bolt"
            ),

            // Доп. гайки
            Product(
                categoryId = 3, name = "Гайка самоконтрящаяся М8 с нейлон. кольцом",
                sku = "ГК-Нл-М8", price = 4.00,
                standard = "DIN 985",
                size = "М8",
                material = "Сталь",
                coating = "Оцинкованное",
                description = "Гайка с нейлоновым стопорным кольцом — не откручивается от вибраций.",
                imageRes = "ic_cat_gaika",
                isPopular = true
            ),
            Product(
                categoryId = 3, name = "Гайка-барашек М6",
                sku = "ГК-Бр-М6", price = 6.00,
                standard = "DIN 315",
                size = "М6",
                material = "Сталь",
                coating = "Оцинкованное",
                description = "Гайка-барашек для затяжки руками без инструмента. Удобна для частого монтажа/демонтажа.",
                imageRes = "ic_cat_gaika"
            ),
            Product(
                categoryId = 3, name = "Гайка квадратная М10",
                sku = "ГК-Кв-М10", price = 5.00,
                standard = "DIN 557",
                size = "М10",
                material = "Сталь",
                coating = "Оцинкованное",
                description = "Гайка квадратной формы для установки в пазы и фиксации в Т-образных профилях.",
                imageRes = "ic_cat_gaika"
            ),

            // Доп. шайбы
            Product(
                categoryId = 4, name = "Шайба плоская М6 (упак. 100 шт)",
                sku = "ШБ-М6-100", price = 60.00,
                standard = "ГОСТ 11371-78",
                size = "М6 (d=6.4, D=12)",
                material = "Сталь",
                coating = "Оцинкованное",
                description = "Шайба плоская в упаковке по 100 шт.",
                imageRes = "ic_cat_shaiba"
            ),
            Product(
                categoryId = 4, name = "Шайба стопорная зубчатая М8",
                sku = "ШБ-Ст-М8", price = 1.50,
                standard = "DIN 6797",
                size = "М8",
                material = "Сталь",
                coating = "Оцинкованное",
                description = "Шайба с внешними зубцами — врезается в материал и фиксирует соединение.",
                imageRes = "ic_cat_shaiba"
            ),

            // Доп. анкеры
            Product(
                categoryId = 5, name = "Анкер химический капсульный M12",
                sku = "АН-Хм-М12", price = 165.00,
                standard = "—",
                size = "М12",
                material = "Сталь + смола",
                coating = "Оцинкованное",
                description = "Капсула со смолой для химического крепления шпильки в бетоне. Высокая нагрузка.",
                imageRes = "ic_cat_anker"
            ),
            Product(
                categoryId = 5, name = "Анкер потолочный для подвесных конструкций",
                sku = "АН-Пт-6", price = 22.00,
                standard = "—",
                size = "6 мм",
                material = "Сталь",
                coating = "Оцинкованное",
                description = "Специальный анкер для крепления подвесных потолков, светильников и вентиляции.",
                imageRes = "ic_cat_anker"
            ),

            // Доп. дюбели
            Product(
                categoryId = 6, name = "Дюбель универсальный 8x52",
                sku = "ДБ-Ун-8-52", price = 3.50,
                standard = "—",
                size = "8 x 52 мм",
                material = "Полиэтилен",
                coating = "—",
                description = "Дюбель для разных оснований: бетон, кирпич, газобетон. Раскрывается в форме узла.",
                imageRes = "ic_cat_dubel"
            ),
            Product(
                categoryId = 6, name = "Дюбель «бабочка» для гипсокартона",
                sku = "ДБ-Бб-ГК", price = 8.00,
                standard = "—",
                size = "10 мм",
                material = "Нейлон",
                coating = "—",
                description = "Складной дюбель для пустотелых стен (гипсокартон, ДСП). Раскрывается с обратной стороны листа.",
                imageRes = "ic_cat_dubel",
                isPopular = true
            ),
            Product(
                categoryId = 6, name = "Дюбель-зонтик для теплоизоляции 10x140",
                sku = "ДБ-Зн-10-140", price = 7.00,
                standard = "—",
                size = "10 x 140 мм",
                material = "Полипропилен",
                coating = "—",
                description = "Тарельчатый дюбель для крепления утеплителя (минвата, пенопласт) к фасадам.",
                imageRes = "ic_cat_dubel"
            ),

            // Доп. гвозди
            Product(
                categoryId = 7, name = "Гвозди винтовые 3.4x90, 1 кг",
                sku = "ГВ-Вн-3.4-90", price = 240.00,
                standard = "—",
                size = "3.4 x 90 мм",
                material = "Сталь",
                coating = "Оцинкованное",
                description = "Винтовые гвозди с повышенной удерживающей способностью. Для деревянных конструкций под нагрузкой.",
                imageRes = "ic_cat_gvozd"
            ),
            Product(
                categoryId = 7, name = "Гвозди ершёные 2.5x40, 0.3 кг",
                sku = "ГВ-Ер-2.5-40", price = 75.00,
                standard = "—",
                size = "2.5 x 40 мм",
                material = "Сталь",
                coating = "Оцинкованное",
                description = "Гвозди с насечками — практически не выдёргиваются. Для крепления вагонки и тонкой доски.",
                imageRes = "ic_cat_gvozd"
            ),

            // Доп. хомуты
            Product(
                categoryId = 8, name = "Хомут червячный 32-50 мм",
                sku = "ХМ-Чр-32-50", price = 28.00,
                standard = "W2",
                size = "32-50 мм",
                material = "Нерж. сталь",
                coating = "—",
                description = "Червячный хомут для труб и шлангов среднего диаметра.",
                imageRes = "ic_cat_homut"
            ),
            Product(
                categoryId = 8, name = "Хомут пластиковый 2.5x100 (100 шт)",
                sku = "ХМ-Пл-2.5-100", price = 45.00,
                standard = "—",
                size = "2.5 x 100 мм",
                material = "Нейлон",
                coating = "—",
                description = "Стяжки малого размера для тонких кабелей. 100 шт в упаковке.",
                imageRes = "ic_cat_homut"
            ),
            Product(
                categoryId = 8, name = "Хомут силовой пружинный 20 мм",
                sku = "ХМ-Пр-20", price = 22.00,
                standard = "—",
                size = "20 мм",
                material = "Пружинная сталь",
                coating = "Оцинкованное",
                description = "Пружинный хомут для топливных и охлаждающих шлангов автомобиля.",
                imageRes = "ic_cat_homut"
            )
        )
        productDao.insertAll(products)
    }
}
