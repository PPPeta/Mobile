# Строй Креп — мобильное приложение магазина крепежа

Дипломный проект. Android-приложение, работающее автономно (без сервера) — все данные хранятся локально в SQLite через Room.

## Стек

- **Язык:** Kotlin
- **UI:** XML + Material Components (Views, ViewBinding)
- **Навигация:** Jetpack Navigation Component + Bottom Navigation
- **БД:** Room (SQLite), KSP
- **Асинхронность:** Kotlin Coroutines + Flow
- **minSdk:** 24 (Android 7.0)
- **compileSdk / targetSdk:** 34

## Функционал

- Регистрация и вход (данные пользователей в локальной БД)
- Главный экран: баннер, категории, популярные товары
- Каталог с поиском, сортировкой и фильтром (цена, материал)
- Карточка товара: характеристики (ГОСТ/DIN, размер, материал, покрытие), добавление в корзину и избранное
- Избранное
- Корзина: изменение количества, удаление, итоговая сумма
- Оформление заказа: доставка (самовывоз / курьер), оплата (при получении / картой онлайн)
- **Имитация оплаты ЮKassa**: форма карты с валидацией номера по алгоритму Луна и форматированием (`1234 5678 9012 3456`, `MM/YY`). Реальный платёжный шлюз не подключается.
- История заказов со статусами (Новый / Оплачен / В доставке / Выполнен) и повтор заказа
- Профиль: редактирование данных, смена пароля, контакты магазина, о приложении

## Как запустить

1. Открыть каталог `Mobile/` в Android Studio (Giraffe / Hedgehog или новее).
2. Дождаться Gradle Sync — IDE подтянет все зависимости (Room, Navigation, Material и т. д.).
3. Подключить Android-устройство или запустить эмулятор (API 24+).
4. Нажать **Run** (`Shift+F10`).

При первом запуске БД заполняется тестовыми данными: 8 категорий и 25 товаров крепежа.

## Структура проекта

```
app/
├── src/main/
│   ├── AndroidManifest.xml
│   ├── java/ru/stroykrep/app/
│   │   ├── StroyKrepApp.kt            # Application, сборка зависимостей
│   │   ├── data/
│   │   │   ├── AppDatabase.kt         # Room database
│   │   │   ├── DatabaseSeeder.kt      # Первичное наполнение
│   │   │   ├── entity/                # User, Category, Product, CartItem, FavoriteItem, Order, OrderItem
│   │   │   ├── dao/                   # DAO-интерфейсы
│   │   │   ├── model/                 # CartProduct, OrderWithItems
│   │   │   ├── repo/                  # AuthRepository, ShopRepository
│   │   │   └── session/               # SessionManager (SharedPreferences)
│   │   ├── ui/
│   │   │   ├── SplashActivity.kt
│   │   │   ├── MainActivity.kt        # BottomNav + NavHost
│   │   │   ├── auth/                  # Login, Register
│   │   │   ├── home/                  # Главная
│   │   │   ├── catalog/               # Категории, список товаров, карточка
│   │   │   ├── favorites/
│   │   │   ├── cart/                  # Корзина, оформление, оплата, успех
│   │   │   ├── orders/                # Список и детали заказа
│   │   │   ├── profile/               # Профиль, редактирование, пароль, контакты, о приложении
│   │   │   └── common/                # Общие адаптеры и утилиты
│   │   └── util/                      # Validators, Formatters, константы
│   └── res/                           # layouts, drawables, strings, themes
└── build.gradle.kts
```

## Защита дипломного проекта

Ключевые моменты для защиты:

- Архитектура: **MVVM-light** на базе ViewBinding + Repository + Coroutines/Flow.
- Работа с БД: **Room** (7 сущностей, DAO, миграции, relations).
- Навигация: **Jetpack Navigation Component** (single-activity + фрагменты).
- Валидация пользовательского ввода (email, телефон, пароль, номер карты по алгоритму Луна).
- Имитация интеграции с платёжным шлюзом ЮKassa без реального эквайринга.
- Полностью автономная работа без интернета.
