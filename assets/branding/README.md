# Rose — брендинг

## Файлы
| Файл | Назначение |
|---|---|
| `rose-avatar.png` | мастер 1024×1024 (аватар, соцсети, превью) |
| `rose-avatar-512.png` / `-256.png` | сайт, Modrinth/CurseForge, баннеры |
| `rose-avatar-128.png` | иконка мода (`pack.png` / `icon.png` в `fabric.mod.json` — положить копию в `src/main/resources/assets/rose/icon.png`) |
| `rose-avatar-64.png` | favicon / мелкие иконки |

## Стиль-гайд (для всех будущих генераций)
- Материал: гранёное рубиново-красное **полупрозрачное стекло**, белые блики по фаскам, лёгкое внутреннее свечение.
- Фон: чистый чёрный (#000000) + красный bloom-halo.
- Форма: фасеточные лепестки/осколки, low-poly, ломаные силуэты. Никакой мультяшности и градиентных «мыльных» кнопок.
- Композиция: один объект по центру, много пустого пространства, без текста.
- Палитра: `#E4294B` (rose.500), `#FF6B85` (rose.300), `#C1123A` (ruby), белый блик ~`#FFD9E1` @ 0.6.
- Промпт-шаблон:
  > square logo on pure black background: {subject} built from sharp faceted shards of translucent ruby-red glass, low-poly angular pieces, glossy 3D render, thin bright white-pink specular highlights on every bevelled edge, deep red glow and soft bloom halo, subtle inner refraction, centered with generous empty space, clean minimal high-end product render, no text, no letters.

## Правила
- Никогда не использовать логотип на светлом фоне без «тёмной подложки» (стекло читается только на чёрном).
- Минимальный размер читаемости знака — 32 px; для 16–24 px использовать упрощённый одноцветный вариант (сделаем отдельным ассетом).
