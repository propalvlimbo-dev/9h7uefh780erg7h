# Rose Client

Мод-клиент для **Minecraft Java 1.21.11** (Fabric) с собственным рендером и UI в стиле «рубиновое стекло».

```
Minecraft 1.21.11  ·  Fabric Loader 0.20.1  ·  Fabric API 0.141.6+1.21.11
Loom 1.19-SNAPSHOT  ·  Java 21  ·  официальные Mojang mappings
```

## Что уже есть (Milestone 1)

| # | Возможность | Где смотреть |
|---|---|---|
| 1 | Замена логотипа и **полностью кастомное главное меню** (анимированное появление, живой фон: небулы, пыль, виньетка) | `client/screens/RoseTitleScreen.java`, `client/mixin/MinecraftMixin.java` |
| 2 | **ClickGUI** — стеклянное окно, анимации открытия/ховера/переключателей/раскрытия, свои слайдеры | `client/screens/RoseClickGui.java` |
| 3 | **RoseUI** — собственный слой отрисовки: градиенты, скругления, тени, глоу, hairline, текст — всё на per-vertex квадах через `GuiElementRenderState` | `client/render/RoseDraw.java`, `RoseQuad.java`, `RoseTheme.java` |
| 4 | **Custom Sky** — процедурные рубиновые облака со своим шейдером и своим `RenderPipeline` | `client/render/RubyCloudRenderer.java`, `RoseRenderTypes.java`, `assets/rose/shaders/core/ruby_clouds.*` |

## Требования

- **JDK 21** (Eclipse Temurin / Adoptium). Проверка: `java -version` → `21.x`
- IntelliJ IDEA (Community или Ultimate)
- Интернет при первой сборке (Gradle скачает Minecraft, mappings и Fabric API)

## Запуск в IntelliJ IDEA

1. `File → Open…` → выбрать папку проекта (`rose-client`) → **Trust Project**
2. Дождаться Gradle sync. Если IDEA ругается на wrapper — `Settings → Build, Execution, Deployment → Build Tools → Gradle` → **Distribution: `gradle-wrapper.properties file`** (IDEA сама скачает Gradle 9.8.2).
3. В правой панели Gradle: `Tasks → fabric → runClient` (или `Tasks → application → runClient`).
   Либо в терминале:

```powershell
.\gradlew.bat runClient
```

## Сборка мода

```powershell
.\gradlew.bat build
```

Готовый jar: `build\libs\rose-0.1.0.jar` → положить в `mods` вместе с **Fabric API** для 1.21.11.

## Управление

- **Right Shift** — открыть/закрыть ClickGUI
- **Esc** — закрыть ClickGUI

## Как включить облака

1. Right Shift → категория **Visuals** → **Custom Sky** → переключатель справа.
2. Клик по строке модуля — раскрыть настройки: `Density`, `Altitude`, `Brightness`, `Layers`.
3. Настройки сохраняются в `config/rose.json`.

## Как это устроено (коротко)

- **GUI**: `RoseDraw` строит всё из 4-вершинных квадов с цветом на вершину и отправляет их в `GuiGraphics.guiRenderState` через собственный `GuiElementRenderState` (`RoseQuad`). Это даёт настоящие градиенты, per-vertex alpha, мягкие тени и глоу без единой текстуры.
- **Облака**: сетка world-anchored квадов на высоте `altitude`. Вершинный шейдер даёт мировые XZ + дистанцию до камеры, фрагментный — процедурный fbm-шум, псевдо-объёмное освещение по градиенту плотности, серебристую кромку и затухание к горизонту. Дрейф — от `GameTime`. Ванильные облака выключаются через `options.cloudStatus`.
- **Рендер-хук**: `@Inject` в `LevelRenderer#renderLevel` (RETURN), свой `PoseStack` с камерой, отрисовка через `MultiBufferSource` + собственный `RenderType`/`RenderPipeline`.

## Если что-то не работает

| Симптом | Что делать |
|---|---|
| Игра падает на старте, в логе `Mixin apply failed` | проверить, что версия MC ровно **1.21.11** и Fabric Loader ≥ 0.20.1 |
| В логе `Couldn't compile ... ruby_clouds` | шейдер не скомпилировался: смотреть текст ошибки, править `assets/rose/shaders/core/ruby_clouds.fsh` |
| Модуль включён, ванильных облаков нет, но рубиновых не видно | `RoseRenderTypes.RUBY_CLOUDS` → попробовать убрать `.setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET)` или сменить на другой target; также проверить `withDepthTestFunction` |
| Главное меню осталось ванильным | миксин `MinecraftMixin#setScreen` не применился (проверить `rose.mixins.json`) |
| ClickGUI не открывается | бинд Right Shift занят другим модом → сменить в `Options → Controls → Rose` |

## Документация

- [Исследование и архитектура](docs/00-RESEARCH-AND-ARCHITECTURE.md)
- [Milestone 1 — что сделано и что дальше](docs/10-MILESTONE-1.md)
- [Брендинг и стиль-гайд](assets/branding/README.md)
