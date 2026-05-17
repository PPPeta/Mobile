# Как заменить иконки на реальные фотографии товаров

## Шаг 1. Скачать фото

Бесплатные источники:
- https://unsplash.com (поиск: bolt, screw, nail, washer, anchor)
- https://www.pexels.com
- https://pixabay.com

Размер: **300x300 px** (квадратные). Формат: **PNG** или **WEBP** (лучше webp — легче).

## Шаг 2. Подготовить файлы

Переименуй каждое фото по такой схеме:
```
img_samorez_01.webp
img_samorez_02.webp
img_bolt_01.webp
img_gaika_01.webp
img_shaiba_01.webp
img_anker_01.webp
img_dubel_01.webp
img_gvozd_01.webp
img_homut_01.webp
img_rent_drill.webp
img_rent_grinder.webp
...
```

## Шаг 3. Положить в проект

Скопируй файлы в папку:
```
app/src/main/res/drawable/
```

> ВАЖНО: имя файла должно быть **только маленькие буквы, цифры и подчёркивания**. Никаких пробелов, дефисов, заглавных букв.

## Шаг 4. Обновить DatabaseSeeder.kt

В файле `app/src/main/java/ru/stroykrep/app/data/DatabaseSeeder.kt` для каждого товара поле `imageRes` замени на имя файла **без расширения**:

```kotlin
Product(
    categoryId = 1, name = "Саморез по дереву 3.5x35",
    ...
    imageRes = "img_samorez_01",  // ← было "ic_cat_samorez"
    ...
)
```

## Шаг 5. Удалить приложение и пересобрать

Удали приложение с устройства/эмулятора (чтобы БД пересоздалась), затем Run.

## Шаг 6 (необязательно). Для картинок из интернета

Если хочешь загружать фото из URL (не из ресурсов), нужно:
1. Добавить библиотеку Glide или Coil в build.gradle
2. Вместо `setImageResource()` использовать `Glide.with(ctx).load(url).into(imageView)`
3. Добавить `<uses-permission android:name="android.permission.INTERNET"/>` в AndroidManifest

Но для дипломного проекта **достаточно локальных фото** в drawable.
