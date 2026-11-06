# Milestone 1 — главное меню, ClickGUI, Custom Sky

## Сделано

### 1. Логотип и главное меню
- `MinecraftMixin` перехватывает `Minecraft#setScreen(Screen)` и подменяет ванильный `TitleScreen` на `RoseTitleScreen` — ванильное меню не рисуется вообще.
- Логотип: `assets/rose/textures/gui/logo_192.png` (роза из рубинового стекла, альфа из яркости).
- Анимации: fade+scale+rise логотипа, появление вордмарка, «стаггер» кнопок, живой фон (3 дрейфующие небулы, вертикальные aurora-полосы, 64 пылинки, виньетка).
- Кнопки: Singleplayer / Multiplayer / Settings / Rose GUI / Quit — обычные ванильные экраны, но в нашем оформлении.

### 2. RoseUI (свой слой отрисовки)
- `RoseQuad implements GuiElementRenderState` → прямые квады в GUI-пас: `RenderPipelines.GUI`, `TextureSetup.noTexture()`, `addVertexWith2DPose` + `setColor`.
- `RoseDraw`: `rect`, `gradientV/H`, `rrect`, `rrectOutline`, `circle`, `line`, `glow`, `shadow`, `panel`, текст (в т.ч. масштабированный и с letter-spacing), картинки.
- Скоординация со `scissorStack` учтена — наши примитивы корректно режутся `enableScissor`.

### 3. ClickGUI
- Стеклянное окно 452×272, шапка с лого и версией, сайдбар категорий, карточки модулей.
- Анимации: открытие (scale+fade), ховер, переключатель (ruby-трек + белая «таблетка» с глоу), раскрытие настроек.
- Слайдеры генерируются из метаданных `Setting` автоматически; значение тянется мышью (drag реализован в `render` по `mouseDown`, без ненадёжных `mouseDragged`-сигнатур).
- Перетаскивание окна за шапку, ограничение в пределах экрана.
- Сохранение/загрузка: `config/rose.json` (Gson).

### 4. Custom Sky
- `CustomSky` (модуль) + `RubyCloudRenderer` (геометрия) + `RoseRenderTypes` (pipeline/render type) + `rose:core/ruby_clouds` (шейдеры).
- Геометрия: сетка 20×20 блоков, радиус 260 блоков, до 3 слоёв с шагом 13 блоков.
- Шейдер: 5-октавный value-noise fbm, порог по «покрытию» (приходит в альфе вершины), псевдо-нормаль из градиента плотности, ламберт-освещение, silver lining по маске, затухание по дистанции, дрейф от `GameTime`.
- Ванильные облака отключаются через `options.cloudStatus`.

## Осознанные ограничения этого этапа

1. **Blur/refraction пока нет** — стекло нарисовано градиентами, hairline и глоу. Настоящий Apple-glass (SDF-скругления, преломление по Снеллу, дисперсия, mip-блюр) — этап 3 по роадмапу, он требует капчура кадра в `RenderTarget` и своих пост-проходов.
2. **Шрифт ванильный** — MSDF-текст (Inter/Geist + иконки в SDF-атласе) на следующем этапе.
3. **ImGui-оверлея нет** — по решению этапа не тащим зависимость.

## Чек-лист первого запуска (что проверить руками)

- [ ] `runClient` стартует, главное меню — Rose.
- [ ] Кнопки ведут в Singleplayer/Multiplayer/Options, Quit закрывает игру.
- [ ] Right Shift открывает ClickGUI, Esc/RShift закрывают, окно таскается.
- [ ] Custom Sky: включение убирает ванильные облака и показывает рубиновые.
- [ ] Слайдеры влияют на облака (Density/Altitude/Brightness/Layers).
- [ ] `config/rose.json` появляется и переживает перезапуск.
- [ ] В логе нет `Mixin apply failed` и ошибок компиляции шейдера.

## Следующие шаги

1. **Glass v1**: капчур кадра → mip-блюр → стеклянный материал (SDF+Снелл+дисперсия) для панелей и HUD.
2. **MSDF-текст** и SDF-иконки.
3. **FX-каркас** (`JumpBurst` — взрыв под ногами при прыжке) + bloom-пайплайн.
4. Совместимость Sodium 0.8.14 / Iris 1.10.8 / ImmediatelyFast.
