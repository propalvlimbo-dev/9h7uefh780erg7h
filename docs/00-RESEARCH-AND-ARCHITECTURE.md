# ROSE CLIENT — исследование и архитектура
**Minecraft Java 1.21.11 · Fabric · собственный рендер (Blaze3D) · Apple Glass UI**

Дата исследования: 06.11.2026 (по состоянию экосистемы на эту дату)
Статус: research / pre-production. Кода пока нет — только решения, версии, схемы.

---

## 0. TL;DR — что мы будем использовать

| Слой | Решение |
|---|---|
| Платформа | Minecraft **1.21.11**, Fabric Loader **0.20.1**, Fabric API **0.141.6+1.21.11**, Loom **1.19-SNAPSHOT**, Java **21** |
| Маппинги | **официальные Mojang** (`loom.officialMojangMappings()`) — Yarn для 1.21.11 последний и дальше не обновляется |
| UI | **своя библиотека `RoseUI`** поверх Blaze3D (`GuiGraphics` → `GuiRenderState`), retained-tree + diffing, MSDF-текст, SDF-фигуры, собственный glass-материал |
| Рендер мира/FX | свои `RenderPipeline` + `StagedVertexBuffer`/`SubmitNodeCollector`, extraction → drawing, свои пост-эффекты (bloom, blur, glass) |
| Модели | **Blockbench** (.bbmodel) + **GeckoLib 5.4.5** (1.21.11) + **Animated Java**; генерация моделей — программно в bbmodel-JSON |
| Картинки | генеративная модель + пост-обработка + стиль-гайд Rose (см. §9) |
| Отладка UI | опционально **ImGui** (`imgui-java` + `fabric-gui-imgui`) только как dev-tool, не в релиз |
| Что НЕ берём | Electron/CEF/Ultralight-подход (как в LiquidBounce) — вес, задержки, память; Skia/NanoVG как основу — дублирует Blaze3D |

---

## 1. Платформа: точные версии и почему именно они

Проверено по `FabricMC/fabric-example-mod` (ветка `1.21.11`) и блогу Fabric:

```properties
minecraft_version=1.21.11
loader_version=0.20.1
loom_version=1.19-SNAPSHOT
fabric_api_version=0.141.6+1.21.11
# Java release = 21
# plugin: id 'net.fabricmc.fabric-loom-remap'
# mappings: loom.officialMojangMappings()
```

Важные факты про 1.21.11 (он же «последний обфусцированный релиз», 09.12.2025):

- **1.21.11 — последняя версия, где существует Yarn/Intermediary.** После неё Mojang отдаёт игру без обфускации, версии переименованы в `26.1`, `26.2`, `26.3`… Поэтому **сразу стартуем на Mojang mappings** — тогда порт на 26.x будет дешёвым, а не «переписать всё».
- В 1.21.11 Mojang провернул «rename shuffle»: `ResourceLocation` → **`Identifier`**, `Util`/`FileUtil`/`BlockUtil` → `net.minecraft.util.*`, `critereon` → `criterion`, модели сущностей разложены по подпакетам (`client.model.animal.*`, `.monster.*`, `.object.*`). Это надо помнить при чтении любых гайдов для 1.21.10 и ниже.
- Java 21 — целевая (не 17 и не 25). Gradle 9.x, `configuration-cache=true`.
- Для CI/тестов: `runClient` + client gametest со скриншотами (в 1.21.x это реально работает, см. §11).

**Целевая матрица совместимости (обязательно тестировать):**
- ваниль без модов;
- **Sodium 0.8.14** (для 1.21.11);
- **Iris 1.10.8** + Sodium 0.8.14 (наш рендер не должен ломать шейдерпаки);
- **ImmediatelyFast 1.14.3+1.21.11** (переопределяет immediate-mode путь — конфликтует с «умным» батчингом, нужен флаг совместимости);
- **Blur+ (Motschen/Blur) 6.3.x** — прямой конкурент нашего glass-фона, поведение должно быть предсказуемым (мы переопределяем/выключаем).

---

## 2. Что изменилось в рендере Minecraft (1.21.5 → 1.21.11) — на этом стоит весь наш рендер

Это самая важная часть исследования: **старые гайды (1.20.x, 1.21.1) бесполезны**, там был `RenderSystem`+`BufferBuilder`+`RenderType`. Сейчас так:

### 2.1 Blaze3D 2.0 (с 1.21.5): `RenderPipeline`
- `RenderPipeline` — иммутабельный объект: шейдеры (vertex/fragment), blend (`ColorTargetState`), depth (`DepthStencilState`/`CompareOp`), cull, vertex format, primitive topology, samplers. Создаётся через `RenderPipeline.builder(snippets...)`, регистрируется через `RenderPipelines.register(...)`.
- `GpuDevice` — устройство: `createTexture`, `createBuffer`, `createSampler`, `createCommandEncoder`, `createRenderPass`.
- `CommandEncoder` / `RenderPass` — явная запись команд (writeToTexture, draw…), `GpuBuffer`, `GpuTexture`, `GpuSampler`.
- **Никакого прямого GL в игровом коде.** Всё — через device. Это плюс: наш рендер автоматически работает поверх backend'ов (и переживёт переход 26.2+ на Vulkan).

### 2.2 GUI стал отложенным (с 1.21.6): `GuiGraphics` → `GuiRenderState`
- `GuiGraphics` больше **не рисует**, а *сабмитит* элементы в `GuiRenderState`. У него есть `pose()` — это `Matrix3x2fStack` (**2D-матрицы**, не PoseStack!), `enableScissor/disableScissor`, `blit(...)`, `submit…RenderState(...)`.
- Каждый сабмит: `pipeline` + `textureSetup` (Sampler0/1/2 + `GpuSampler`) + `buildVertices(VertexConsumer)`. То есть **кастомный виджет = свой `GuiElementRenderState`** со своим пайплайном.
- Порядок/слои: `graphics.nextStratum()` — «слои» внутри GUI (нужно для blur-подложки под текстом).
- Следствие: **свой UI-движок — это естественный путь**, а не хак. Ваниль сама предлагает API под это.

### 2.3 Extraction / Drawing split (с 1.21.9)
- Мир рисуется в два такта: **extraction** (собрать иммутабельные render state, можно читать мир) → **drawing** (только рисовать, буфер уже собран). `OrderedRenderCommandQueue`, `LevelRenderState`, системы «features» для сущностей/блок-энтити/партиклов.
- Fabric вернул `WorldRenderEvents` (в 1.21.10/1.21.11) уже в новом виде — extraction-фаза + drawing-фаза, см. https://docs.fabricmc.net/develop/rendering/world.
- **Наш слой эффектов строится по этому же контракту**: `FxExtractor` (собирает `FxRenderState` из мира) → `FxRenderer` (рисует через свои пайплайны). Никаких «нарисуем в RenderTickEvent из мира» — это путь в рассинхрон и краши с Sodium.

### 2.4 Что нового конкретно в 1.21.11 (важно для нас)
- **Samplers отделены от текстур**: `GpuDevice#createSampler`, `GpuSampler` (address mode, min/mag filter, max LOD, anisotropy), `SamplerCache`. У `RenderTarget` больше нет `filterMode`/`setFilterMode`.
  → Для glass/blur это **подарок**: мы можем держать один и тот же RT и сэмплить его с `GpuSampler(LINEAR, CLAMP, maxLod=…)` для mip-блюра и с `NEAREST` для пиксель-арта.
- `ShaderSource` (функциональный интерфейс) — можно грузить шейдеры из своих ресурсов и генерировать варианты.
- `TextureUtil#solidify`, `fillEmptyAreasWithDarkColor` — аккуратные мипмапы для атласов (нам нужно для SDF/MSDF-атласов и иконок).
- Длинные offset'ы в `mapBufferRange`/`copyTextureToBuffer` — можно спокойно заливать большие вершинные буферы.

### 2.5 Что это значит для нас
1. Пишем **тонкий слой `rose/render`** (`RoseGpu`): обёртки над `GpuDevice`/`RenderPipeline`/`RenderTarget`, кэш пайплайнов по ключу (blend, depth, samplers, shader), пул буферов, ring-buffer вершин.
2. UI и мир рисуем **своими пайплайнами**, но через официальные точки входа (`submitGuiElementRenderState` / world render events / feature submission).
3. Пост-эффекты — **свои** (`PostChain`/`RenderPipeline` + ping-pong RT), не Satin (Satin фактически заморожен с 2025 для 1.20.x, для 1.21.11 не тащим).

---

## 3. UI: выбор технологии (сравнение и вердикт)

| Вариант | Плюсы | Минусы | Вердикт |
|---|---|---|---|
| **A. Свой движок на Blaze3D (RoseUI)** | полный контроль над материалом (наш liquid glass), нулевые лишние зависимости, «родная» интеграция с `GuiRenderState`, идеальный перф, полный контроль над шрифтами/иконками | дольше делать (layout, текст, инпут, скролл) | **БАЗА** |
| B. Dear ImGui (`imgui-java`, `fabric-gui-imgui`, `fabric-imgui-example-mod`) | мгновенный результат, отличные dev-панели, docking | «программистский» вид, свой шрифтовой атлас, рисует напрямую через GL-backend (конфликт с 26.x Vulkan и с Blaze3D-стейтом), сложно сделать настоящий liquid glass | **только debug/dev overlay** |
| C. HTML/CSS в CEF или Ultralight (как LiquidBounce Nextgen) | веб-дизайн, темы, итерации в браузере | +100–300 МБ зависимостей, отдельный процесс/поток, ввод, DPI, синхронизация кадра, «задержка клика», не работает в 26.x Vulkan без хаков | **нет** |
| D. `owo-lib` (owo-ui/braid) | готовый декларативный UI, SDF-шейдеры, 1.21.11 (0.13.0) | чужой стиль/архитектура, тяжёлый API, в 0.13 переименования под Mojang mappings, ограничивает кастомный glass | нет (но **читаем как референс**) |
| E. LibGui / SpruceUI / YACL / Cloth Config | быстрые конфиг-скрины | не для ClickGUI/HUD, «модовый» вид | только для внутренних конфигов, если лень |
| F. Skia (Skija) / NanoVG / FreeType | идеальный 2D-рендер, path rendering | ещё один рендер-стек рядом с Blaze3D (двойной стейт, двойная память, конфликт с Iris), нативные биндинги в бандле | нет |

**Вывод:** RoseUI — свой небольшой (целевой объём ~6–9k строк) UI-фреймворк. Ключевая идея: **мы не «рисуем прямоугольники», мы описываем материал** (см. §5), и рендерер батчит одинаковые материалы в один draw.

### 3.1 Архитектура RoseUI

```
rose/
  core/        события, модули, настройки, конфиги, бинды
  render/
    gpu/       RoseGpu: device/pipeline/RB-обёртки, кэш пайплайнов, пулы
    text/      шрифты (MSDF), шейпинг (HarfBuzz через LWJGL), атласы, fallback'и
    shape/     SDF-фигуры: rrect, squircle, circle, shadow, hairline, 9-slice
    material/  материалы: Flat, Frosted, Glass(liquid), Glow, Emission
    post/      bloom, blur-chain, tonemap, displacement, (опц.) motion blur
    fx/        мировые эффекты: Extract → RenderState → Render
  ui/
    tree/      UIComponent, layout (flex-подобный), measure/arrange, diff
    widgets/   Button, Slider, Toggle, Dropdown, SearchField, ColorPicker,
               Window, Panel, TabBar, Tooltip, ScrollView, Keybind, Chart
    anim/      spring / cubic-bezier / keyframes, единый AnimationClock
    input/     pointer capture, hover/active/focus, drag&drop, hotkeys, IME
    theme/     design tokens: цвета, радиусы, тени, blur-radius, типографика
    screens/   ClickGUI, HUD-Editor, Configs, AltManager, MainMenu, Notifications
  modules/     модули клиента (категории: Combat/Visuals/Player/Misc)
```

Принципы:
1. **Immediate API поверх retained tree.** Пишем как `button("Save").onClick(...)`, а дерево/состояние хранится по ключу (как React-хуки). Это даёт быструю итерацию и предсказуемые анимации.
2. **Никаких аллокаций в кадре** (после прогрева): пулы `VertexConsumer`, кэш строк, интернирование ключей, `FloatArrayList`/`IntArrayList` вместо `List<Float>`.
3. **UI-кадр = 1 проход сабмита + 1 проход батчинга** по (pipeline, blend, texture, scissor, stratum).
4. **Всё, что дороже 1 мс, кэшируется**: blur-подложка, тени, blur-tap таблицы, mip-цепочка.
5. **Разделение UI-слоёв** (stratum): `BACKGROUND_BLUR → PANELS → CONTENT → TEXT → OVERLAYS_TOOLTIP → DRAG`.
6. **DPI/масштаб**: всё верстается в «логических» единицах, scale factor из GUI Scale + опция `uiDensity {Compact, Normal, Comfortable}`.

### 3.2 Текст (это половина «красоты»)
- **MSDF-атласы** (msdfgen/msdf-atlas-gen) для шрифтов: чёткие буквы на любом масштабе, 1 draw на шрифт, эффекты (обводка, свечение, «стеклянный» блик) — бесплатно во фрагментнике.
- Кириллица + латиница обязательны; интерфейсные шрифты: Inter / Geist / SF-подобный (SF Pro — только если есть лицензия; иначе Inter Tight + Manrope).
- Шейпинг: **HarfBuzz** (через LWJGL, `lwjgl-harfbuzz`) для лигатур и кернинга; fallback — простой шейпер для CJK/эмодзи.
- Цифры: табличные (`tnum`) для HUD-таймеров/CPS/FPS, чтобы не «дёргалось».
- Fallback-цепочка: RoseUI Font → Vanilla (если MSDF-атлас не прогрузился) → текст без красоты, но читаемый.

### 3.3 Иконки
- Все иконки — **SDF-символы в одном атласе** (наш шрифт иконок), векторные, криспые, перекрашиваются в рантайме (нет 100 PNG в бандле).
- Экспорт из SVG → msdfgen (build-time задача Gradle).

---

## 4. Дизайн-система Rose (токены, а не «магия чисел»)

```
radius:  xs 6 · sm 10 · md 14 · lg 20 · xl 28
space:   2 4 6 8 12 16 20 24 32 40
type:    display 28/32 · title 20/26 · body 14/20 · caption 12/16 · mono 13/18
blur:    panel 18 · window 24 · popover 14 · hud 8     (в «пикселях экрана», не физических)
glass:   thickness 18dp · ior 1.5 · dispersion 1.6 · tint 0.06 · frost 0.35
motion:  spring(stiffness 420, damping 34) · появление 180 мс · hover 90 мс
palette: rose.500 #E4294B · rose.300 #FF6B85 · ruby #C1123A
         glass.edge #FFFFFF @ 0.55 · glass.inner #FFFFFF @ 0.10
         surface.0 #0B0B0E · surface.1 #121216 · ink.100 #F6F7FA · ink.60 #9AA0AE
```
Правила: 1 акцент в зоне видимости; тени — физические (смещение + размытие + 2 слоя), а не «чёрный прямоугольник»; hairline 1px (в логических единицах) на всех glass-поверхностях; никаких чистых #000 и #FFF в тексте.

---

## 5. Apple Glass / Liquid Glass — как это реально сделать в Minecraft

Это не «blur + белая рамка». Это **имитация преломления**: стекло имеет толщину, у края — фаска (bezel), лучи преломляются по Снеллу, на кромке — френелевский блик, внутри — рассеивание + лёгкая дисперсия (RGB-разъезд). Ровно та же математика, что в веб-реализациях (`apple-liquid-glass-webgl`, `ptu14/liquid-glass`), только на GLSL в Blaze3D.

### 5.1 Пайплайн кадра (порядок проходов)

```
1. VANILLA/LEVEL RENDER            → mainRT (color) + depth
2. [опц.] BLOOM EXTRACT            → brightRT (half-res, threshold+softknee)
3. BLUR CHAIN (downsample)         → mip0..mip3 (13-tap downsample / Kawase)
4. UI BACKGROUND CAPTURE           → sceneCopyRT = mainRT (или mip0..3 уже готовы)
5. UI SUBMIT (GuiRenderState)      → слои: blur panels → glass → content → text
6. UI POST                         → bloom add, dust/grain, vignette, tonemap
7. PRESENT                          → screen
```
Ключевое: **mip-цепочка blur считается один раз за кадр** и переиспользуется всеми стеклянными панелями. Glass-панель не «блюрит» сама — она **сэмплит нужный LOD** (это и есть тот самый трюк с переменным blur-радиусом и «дорогим» стеклом, которое не стоит 30 мс).

### 5.2 Математика материала (фрагментник)

```glsl
// 1. Форма: SDF (rrect / squircle), нормаль кромки из градиента SDF
float d  = sdSquircle(p, halfSize, radius);        // отрицательно внутри
float aa = fwidth(d);
vec2  n  = normalize(gradientOfSDF(p));            // направление «к кромке»

// 2. Толщина: bezel как выпуклый скос (профиль squircle), t ∈ [0..1]
float t  = smoothstep(-bezelWidth, 0.0, d);        // 0 в центре → 1 у кромки
vec3  N  = normalize(vec3(n * t * bezelStrength, sqrt(max(1.0 - t*t, 0.0))));

// 3. Преломление (Снелл, IOR 1.5) → смещение UV + дисперсия по каналам
vec2 uvR = uv + refractUV(N, ior - dispersion).xy * edgeReach;
vec2 uvG = uv + refractUV(N, ior              ).xy * edgeReach;
vec2 uvB = uv + refractUV(N, ior + dispersion).xy * edgeReach;

// 4. Рассеивание: выбор LOD по «толщине прохождения» + frosted
float lod = mix(lodCenter, lodEdge, t) + frost;
vec3 bg = vec3(textureLod(scene, uvR, lod).r,
               textureLod(scene, uvG, lod).g,
               textureLod(scene, uvB, lod).b);

// 5. Вибрация/тинт (vibrancy: saturate+contrast, а не «белая плёнка»)
bg = vibrance(bg, 1.18, 1.06) * mix(vec3(1.0), tint, tintAmount);

// 6. Френель + спекуляр по кромке + мениск + hairline
float fres = pow(1.0 - max(dot(N, V), 0.0), 3.0);
vec3  col  = bg + rimColor * fres * rimIntensity
                + specular(N, lightDir) * highlight
                + innerShadow(d) + hairline(d, aa) * hairWidth;
```

Технические детали, которые дают «дорогой» вид:
- **Dispersion** (RGB split) — только у кромки (в центре = 0), иначе мыло.
- **Frost** — контролируемый `lod bias`, а не отдельный 13-таповый блюр.
- **Hairline** рисуется с AA через `fwidth`, не «полоска 1px».
- **Grain** (2–3% шума) — обязателен, иначе на градиентах появляются бандинги; шум — из маленькой тайловой текстуры, а не `random()`.
- **Inner shadow** и **drop shadow** — отдельные квады с SDF-размытием (кэш по (размер, радиус, цвет)) — это дешевле и красивее, чем размытие настоящей альфы.
- **Мениск/эхо** (внутренняя светлая линия ~1.5dp внутри кромки) — то, что отличает iOS-стекло от «блюра».

### 5.3 Что нам даёт 1.21.11
- `GpuSampler` — свой sampler на каждый режим (LINEAR+CLAMP для blur, NEAREST для атласов, `maxLod` ограничен для «дорогого» LOD).
- `RenderTarget` без `filterMode` → фильтрация задаётся в сэмпле; проще держать один RT и разные LOD.
- `RenderPipeline` с blend/depth — стекло = `SRC_ALPHA/ONE_MINUS_SRC_ALPHA` + premultiplied вариант; для additive glow — `ONE/ONE`.
- GUI сабмит с `GpuSampler` в `textureSetup` → glass-квад встраивается в обычный GUI-проход (без «отдельного экрана»).

### 5.4 Производительность (бюджет)
| Проход | Цена (1080p, средняя GPU) | Приём |
|---|---|---|
| Bloom extract + 4 mip downsample | ~0.35 мс | half-res, 13-tap |
| Blur chain (общий) | ~0.5 мс | mip'ы вместо большого радиуса |
| Glass-панели (10–20 шт.) | ~0.2–0.6 мс | 1 draw на материал, LOD-сэмплинг |
| Тени/9-slice | ~0.15 мс | кэш RT |
| Текст MSDF | ~0.1 мс | батч 1 draw/шрифт |
**Правило:** glass/pipeline никогда не дороже 2 мс на кадр; при `fps < 40` — авто-degrade: `Glass → Frosted → Flat`, blur-mip -1, отключение grain.

### 5.5 Деградация и совместимость
- `RoseQuality {Ultra, High, Medium, Low, Potato}` — пресеты материала + blur.
- Если **Iris** активен с шейдерпаком → наши пост-эффекты автоматически в «lite» (или выключены), чтобы не спорить с пайплайном Iris; glass-материал остаётся (он читает уже готовый кадр).
- Если **Sodium** меняет цели рендера — берём финальный RT через официальные точки, а не «свой FBO в обход».
- Все эффекты — через `GpuDevice`, значит 26.x (Vulkan) не потребует переписывания (только импорты `com.mojang.renderpearl.api` в 26.3).

---

## 6. Визуальные функции в мире: как делать «красивый взрыв под ногами при прыжке»

Это шаблон для **любого** нашего визуального эффекта. Разберём на примере `JumpBurst`.

### 6.1 Событие
- Источник: `Mixin` в `LocalPlayer#jumpFromGround` (или `ClientPlayerEntity#aiStep` + `onGround→!onGround` переход; надёжнее — `LivingEntity#jumpFromGround` + проверка `level.isClientSide`).
- Отдаём в шину: `PlayerJumpEvent(pos, velocity, sprinting, onSlime, ...)`.
- Модуль `Visuals/JumpBurst` подписан, стреляет эффектом (с учётом кулдауна, «только на земле/в воздухе», фильтров по блокам под ногами).

### 6.2 Модель эффекта (не партиклы ванильные!)
Своя «FX-система» — **GPU-driven, инстансинг**:
```
FxEmitter { lifetime, birthTime, origin, seed, params }
FxLayer   { тип: Ring | Shockwave | Sparks | Shards | Decal | Trail, budget }
```
Каждый слой — один пул инстансов в одном `GpuBuffer`:
```
struct Instance { vec3 origin; float birth; float life; float seed; vec3 dir; float power; }
```
Вершинник разворачивает инстанс в камерный билборд (или в плоскость земли для decal). Фрагментник считает **процедурную форму по SDF + времени**, без текстур:
- `Ring`: `smoothstep` по радиусу, радиус = `easeOutCubic(t) * power`, затухание `1-t`;
- `Shockwave`: тонкое кольцо с «толщиной», растущей и истончающейся; у кромки — flash;
- `Sparks`: N=8..24 «искры» — лучи со скоростью и гравитацией, additive;
- `Shards`: 6–12 гранёных осколков (SDF-ромб) с вращением и блик — **наш rose-стеклянный материал в миниатюре**;
- `Decal`: «обожжённое» пятно на земле (проверка нормали блока), fade 800 мс;
- `Bloom` — эффект пишет emission в bright-буфер → получается настоящий «светящийся» взрыв, а не плоские круги.

### 6.3 Как рисуем (новый API)
```
Extraction (world render event):
  - собрать FxRenderState {camera pos, list of (layer, instanceRange, material params)}
  - зачесть budget: не более N инстансов, дальность/размер на экране (LOD)
Drawing:
  - StagedVertexBuffer / SubmitNodeCollector → свой RenderPipeline на слой
  - depth: ring/shockwave — depthTest ON, но с polygon offset/биасом к земле;
           sparks — depthTest ON + additive;
           decal — depth OFF c полигоном в 0.02 над поверхностью
  - bloom включается флагом материала (emissive intensity в vertex attr)
```
Важно: **никаких ванильных `Particle`** для этого (лимит 16k частиц, CPU-апдейт, нет инстансинга, нельзя нормально блендить). Ванильные партиклы — только для мелочи (искры/дым), через `ParticleProvider` registry, если нужен «совместимый» слой.

### 6.4 То же самое переиспользуется для:
`StepDust`, `LandRipple`, `SwordTrail` (ribbon-геометрия по истории позиций), `HitFlash`, `KillAura ring`, `BlockESP`, `Chams`, `Trajectory`, `Nametags` (в мире), `PearlTrail`, `BowTrail`, `AuraCircle`, `CapePhysics`, `CustomCapeEmitter`, `Particles при получении урона`.
**Всё это — один `FxLayer`-каркас + набор процедурных SDF-шейдеров.** Это и есть «свой рендер для функций визуалов».

### 6.5 HUD-эффекты
HUD — тот же RoseUI, но в stratum `HUD`, с материалом `Glass(thin)` и `bloom` подсветкой; блюр под HUD — из той же mip-цепочки (радиус 8).

---

## 7. Пост-эффекты (наш «киношный» слой)
1. **Bloom** — dual filtering: bright pass (threshold 0.85, soft knee) → 5 downsamples → upsample с `tent` фильтром → аддитивно. Полурезолюция, `emissive` из FX.
2. **Blur chain** — Kawase/13-tap downsample + 3×3 upsample (даёт «дорогой» frosted без 20 тапов).
3. **Displacement/heat** — для эффектов вроде взрыва (пост-искажение по шуму, ограниченное маской экранного радиуса).
4. **Motion blur** — опционально (по velocity-буферу из истории камеры; дорого, только Ultra).
5. **Chromatic aberration / vignette / grain** — финальный композит, все в одном шейдере.
6. **Tonemap** — ACES-подобный filmic, чтобы glow не «выжигал» белым (важно для красного стекла).

---

## 8. Модели и ассеты: Blockbench-пайплайн

### 8.1 Ручные модели
- **Blockbench** (GPL-3.0, но модели/ассеты — наши) → `.bbmodel` (источник, в git) → экспорт.
- Анимации: **Animated Java** (Blockbench-плагин) или **GeckoLib 5.4.5** (для 1.21.11, Fabric/NeoForge). GeckoLib — если нужны сущности/блоки/предметы в мире; Animated Java — если нужны «киношные» item/display-анимации (display entities + keyframes).
- Для наших визуальных функций (кейпы, крылья, аксессуары, кастомные「предметы-иконки」) — GeckoLib (5.4.5), т.к. он умеет и item, и armor, и block.

### 8.2 Программная генерация моделей (то, что ты просишь «генерировать модели»)
Реально и без ручного клика:
- `.bbmodel` — **открытый JSON-формат**. Схема: `meta.format_version`, `resolution`, `elements[]` (`from/to`, `origin`, `rotation`, `faces` с `uv`), `outliner[]` (иерархия групп/костей), `textures[]`.
- Значит можем генерировать: параметрические меши (розы, лепестки, кристаллы, кольца, HUD-3D-иконки, логотип-модель) скриптом (Python/Node/Java) → `.bbmodel` → открыть в Blockbench для доводки → экспорт в GeckoLib `.geo.json` + `.animation.json`, либо сразу в наш собственный `VertexModel`-формат.
- Пайплайн в репо:
```
tools/modelgen/            # генераторы (Python), детерминированные (seed)
  rose_logo.py             # 3D-логотип «роза» из гранёных лепестков
  shards.py                # генератор «осколков» для FX (SDF/меш)
  hud_icons.py             # 3D-иконки для HUD
assets/rose/models/*.bbmodel     # источники (в git)
assets/rose/geckolib/**.geo.json # сгенерированное (в git, воспроизводимо)
```
- Текстуры: генерим палитрой из стиль-гайда (rose-градиенты), UV-развёртка — автоматическая (box-uv как в Blockbench).
- Валидация: unit-тест «модель валидна по схеме + все UV в пределах текстуры».

### 8.3 Шрифты/атласы как «ассеты»
`tools/fontgen` (msdf-atlas-gen) → `assets/rose/font/*.json + .png`; версионируем атласы (в git LFS или просто бинарь < 2 МБ).

---

## 9. Генерация изображений (стиль Rose)

### 9.1 Стиль-гайд (единый «look»)
- Материал: **гранёное рубиново-красное стекло**, полупрозрачное, с белыми бликами на гранях, мягким внутренним свечением.
- Фон: почти чёрный (#000–#0A0507) с красным halo.
- Форма: low-poly / фасеточные лепестки, ломаные силуэты, «осколки», без мультяшности.
- Композиция: центр, много воздуха, один объект (логотип/иконка), без текста.
- Логотип-знак: роза, собранная из стеклянных граней (см. `assets/branding/`).

### 9.2 Пайплайн
1. **Промпт-шаблон** (фиксированный, с параметрами `{subject}`, `{variant}`) → 2 варианта на выбор (никакого «одного и того же»).
2. **Пост-обработка**: кроп в 1:1, лёгкий bloom, выравнивание фона в чистый #000 (для аватарки), экспорт: `1024` (мастер), `512`, `256`, `128`, `64` (иконка мода), `favicon`.
3. **Атласы**: HUD-иконки, кап-текстуры, превью-карточки модулей (в будущем — из 3D-модели рендером, а не картинкой).
4. **Использование в игре**: логотип в main menu (анимированный: медленное вращение/parallax + bloom), watermark, splash, иконка мода, скриншоты для релизов.

### 9.3 Что можно генерировать (список под твои запросы)
- логотип/аватар (сделано);
- баннеры (Modrinth/CurseForge/сайт);
- иконки модулей и категорий;
- текстуры частиц/осколков/капель (если уходим от чистого SDF);
- фоны для темы (subtle noise/gradient);
- карточки настроек и «промо» фоны для ClickGUI.

---

## 10. Исходники существующих UI-клиентов (что читать и что оттуда брать)

> ⚠️ **Лицензии.** Meteor, Wurst, LiquidBounce, Aoba — **GPL-3.0**. Если Rose будет закрытым/платным бинарём — код копировать нельзя (только идеи/архитектура, изучение API). GPL-код можно использовать, только если весь клиент GPL. Решаем **сейчас**: открытый GPL-проект или закрытый (тогда читаем, но не копируем).

| Проект | Стек | Что смотреть | Лицензия |
|---|---|---|---|
| **Meteor Client** (`MeteorDevelopment/meteor-client`) | Java, свой event-bus, свой GUI (Widget/Window), Sodium-совместимость | структура `Module/Settings/System`, как сделан HUD-editor и ClickGUI, как они держат совместимость с Sodium/Iris | GPL-3.0 |
| **Wurst7** (`Wurst-Imperium/Wurst7`) | Java, старый стиль `RenderUtils` | исторический reference по ClickGUI/навигации, но UI устарел | GPL-3.0 |
| **LiquidBounce** (`CCBlueX/LiquidBounce` + `LiquidBounce-Theme`) | Kotlin + **Svelte/React тема**, рендер в CEF/Ultralight, REST/WS мост | лучший пример «HTML-подхода»; смотреть `integration/theme`, `CefBrowser`, `FontManager` — и понять, почему мы так НЕ делаем | GPL-3.0 |
| **Aoba Client** (`Cocolots/Aoba-Client`) | Java, 26.3, современный GUI (rounded corners, effects, movable windows) | простой читаемый modern-GUI на новом API | GPL-3.0 |
| **LoVisual** (`loki5512344ss/LoVisual`) | Java 25, свой рендер (OpenGL/Vulkan), theme system, HUD editor | свежий пример архитектуры «клиент + сайт + бэкенд» | GPL-3.0 (уточнить) |
| **owo-lib** (`wisp-forest/owo-lib`, 0.13.0+1.21.11) | Java, owo-ui: декларативный UI + SDF-шейдеры | как устроен декларативный layout и SDF-компоненты (лучший «белый» референс) | MIT |
| **ImmediatelyFast** (`RaphiMC/ImmediatelyFast`) | Java | как они перехватывают immediate-путь GUI/HUD (нам важно не конфликтовать) | LGPL-3.0 |
| **Blur+** (`Motschen/Blur`) | Java | как в 1.21.11 аккуратно делается blur фона GUI | MIT |
| **Iris** (`IrisShaders/Iris`) / **Sodium** (`CaffeineMC/sodium`) | Java | точки интеграции пост-эффектов, чтобы не ломать шейдерпаки | LGPL-3.0 |
| **fabric-gui-imgui** (`Enaium/fabric-mod-ImGui`), `florianreuth/fabric-imgui-example-mod` | Java+ImGui | dev-overlay для отладки UI | Apache-2.0 / MIT |

**Полезные инструменты:** `mcsrc.dev` (исходники MC в браузере), `mappings.dev` (сравнение Yarn/Mojang/Intermediary до 1.21.11), `docs.fabricmc.net`, `docs.neoforged.net/primer` (гайды по миграциям — единственный нормальный источник по изменениям рендера).

---

## 11. Инженерия: качество, перф, тесты

- **Профайлер**: свои GPU-таймеры (`TimerQuery`) по проходам (UI, glass, bloom, FX) + Spark для CPU. В dev-режиме — ImGui-оверлей с графиком мс по слоям.
- **Скриншот-тесты**: client gametest, который открывает ClickGUI/HUD и снимает кадр; сравнение с baseline (perceptual diff, порог). Защищает от «поехавшего» дизайна.
- **Перф-бюджет в CI**: 1080p, iGPU-профиль; падение > 15% фпс на тестовой сцене — красный билд.
- **Конфиги**: JSON + версионирование схемы; миграции; «профили» (PvP, Visuals, Anarchy) с шаринг-кодами (как LoVisual).
- **События**: свой быстрый event bus (без отражений в горячем пути, `@Subscribe` + сгенерированные индексы или `MethodHandle`).
- **Модули/настройки**: `Setting<T>` c сериализацией, видимостью (`visibleWhen`), валидацией; UI рисует настройки автоматически из метаданных (не хардкодим формы).
- **Безопасность/этика**: клиент клиентский, но серверные правила (античиты) — не наша забота; при этом никаких «майнеров/стилеров», всё офлайн, конфиги локальные (это ещё и требование доверия).

---

## 12. Roadmap (что делаем после этого документа)

| Этап | Содержание | Готово, когда |
|---|---|---|
| **E0. Скелет** | Gradle/Loom, `fabric.mod.json`, entrypoint, split sourceSets, CI | `runClient` стартует, пустой мод грузится |
| **E1. RoseGpu** | обёртки device/pipeline/RT, кэш пайплайнов, ring-буфер, шейдер-загрузчик из ресурсов | рисуем тестовый квад своим пайплайном в HUD |
| **E2. Text/Shape** | MSDF-текст, SDF rrect/squircle/тени, батчинг, DPI | экран «Hello Rose» с идеальным текстом и AA-фигурами, 1 drawcall на материал |
| **E3. Glass v1** | blur-chain + glass-материал + hairline + grain + деградация | одна панель, которая выглядит как iOS-стекло, ≤0.4 мс |
| **E4. RoseUI** | layout, виджеты, скролл, фокус, анимации, темы | рабочий ClickGUI с настройками из метаданных модулей |
| **E5. FX-каркас** | extraction/drawing, инстансинг, слои Ring/Shockwave/Sparks/Shards, bloom | `JumpBurst` при прыжке + `SwordTrail` и `StepDust` |
| **E6. Клиент-ядро** | модули, конфиги, бинды, HUD-editor, команды | можно играть с набором базовых модулей |
| **E7. Полировка** | совместимость Sodium/Iris/ImmediatelyFast, скриншот-тесты, профайлер, релиз-пайплайн | публичная бета |

---

## 13. Риски и открытые вопросы

**Риски**
1. **API-хаос**: 1.21.11 → 26.x переедет часть Blaze3D в `com.mojang.renderpearl.api`. Митигация: изоляция (`RoseGpu` — единственное место, где импортируются GPU-классы).
2. **Конфликты с Iris/Sodium/ImmediatelyFast** — приоритетный тест-набор с первого дня.
3. **Свой UI = долго.** Митигация: E2/E3 делаем на минимальном наборе виджетов, дальше наращиваем по модулям.
4. **Glass на слабых GPU** — жёсткая деградация + пресеты, иначе «красивый клиент» будет 30 фпс.
5. **Лицензия**: если закрытый проект — нельзя тащить GPL-код из Meteor/Wurst/LB.

**Нужно от тебя (ответы влияют на архитектуру)**
1. Лицензия/распространение: открытый GPL или закрытый бинарь? (влияет на «что можно копировать»)
2. Целевые GPU/железо (есть ли iGPU-аудитория) — от этого глубина glass и bloom.
3. Приоритет: сначала ClickGUI-красота или сначала модули/функционал?
4. Нужен ли ImGui dev-overlay в релизных билдах (скрытый)?
5. Локализация: только RU/EN или сразу все?
6. Стиль HUD: минимализм-«стекло» или «кибер-рубин» (логотип тяготеет ко второму)?

---

## 14. Источники (проверено 06.11.2026)

- Fabric: «Fabric for Minecraft 1.21.11» — https://fabricmc.net/2025/12/05/12111.html (Yarn EOL, Loom, 26.x без обфускации)
- Fabric example mod, ветка `1.21.11` (точные версии из `gradle.properties`/`build.gradle`)
- Fabric Docs: Rendering in the World — https://docs.fabricmc.net/develop/rendering/world
- NeoForge Primer 1.21.10→1.21.11 — https://docs.neoforged.net/primer/docs/1.21.11/ (samplers, `Identifier`, `ShaderSource`, atlases)
- NeoForge Docs 1.21.11: Screens / GuiGraphics / GuiElementRenderState — https://docs.neoforged.net/docs/1.21.11/rendering/screens/
- NeoForge Primer 1.21.8→1.21.9 (features, extraction/drawing, removal of world render events)
- Iris 1.10.8 + Sodium 0.8.14 для 1.21.11; ImmediatelyFast 1.14.3+1.21.11; owo-lib 0.13.0+1.21.11; GeckoLib 5.4.5; Blur+ 6.3.x
- Liquid Glass (веб): ptu14/liquid-glass, Oliverrr2424/webgl-apple-liquid-glass, mitige/portfolio (SDF→squircle bezel→Snell→dispersion→golden-angle blur на mip LOD)
- Клиенты-референсы: Meteor, Wurst7, LiquidBounce (+Theme), Aoba, LoVisual
