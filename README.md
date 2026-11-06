# Rose

Клиент-мод для **Minecraft 26.2 (Fabric, Java 25)**.

Собран из двух присланных архивов: **База_1** (исходники клиента) + **База_2**
(вложенные библиотеки `luaj-jse` и `onnxruntime`, без которых База_1 не собирается).
Всё переименовано в **Rose**, логотип заменён на розу. Опасные части удалены — см. ниже.

## Требования

| Что | Версия |
|---|---|
| Minecraft | 26.2 (Fabric) |
| JDK | **25** (toolchain в `build.gradle`, `fabric.mod.json` требует `>=25`) |
| Gradle | 9.5.1 (wrapper в комплекте) |
| Loom | 1.17.17 |

JDK 25: `File → Project Structure → SDK → Add SDK → Download JDK → 25`, затем
`Settings → Build Tools → Gradle → Gradle JVM = 25` и `Java Compiler → target = 25`.
Gradle сам найдёт установленный JDK 25 (автодетект не отключён).

## Запуск

```
./gradlew runClient      # Windows: gradlew.bat runClient
```

или в IDEA: `Tasks → fabric → runClient`. Сборка: `./gradlew build` →
`build/libs/rose-0.1.0+26.2.jar`.

Клиентский конфиг создаётся в `run/config/rose/`.

## Что удалено из базы

При разборе Базы_1 найден встроенный удалённый доступ и связанные с ним механизмы —
они вырезаны полностью:

| Удалено | Что делало |
|---|---|
| `sg/modules/impl/misc/AnyDesk.java`, `init/AnyDeskRelayClient.java`, `init/AnyDeskTlsIdentity.java`, `init/AnyDeskError.java`, `settings/render/AnyDeskRelayProtocol.java`, `sg/ec/AnyDeskPublicAddressDecoder.java`, `assets/rose/anydesk/*` | Поднимало TLS-канал к `boot-01.net.anydesk.com`, регистрировало AnyDesk-ID с автопринятием входящих подключений — то есть удалённое управление ПК |
| `sg/ec/CertificateSigner.java`, `init/SignedIrcPayloadHandler.java` | Подпись сетевых пакетов приватным ключом, вшитым в ресурсы |

Ссылки на удалённые классы вычищены из `ModuleRegistry` и `IrcAuthPacket`.
Остальное (рендер, модельки, GUI, HUD, косметика) перенесено целиком.

Что осталось и стоит знать: мод содержит боевые модули (в т.ч. aimbot на ONNX),
систему «panic/unhook» и IRC-подсистему связи с внешним сервером (`sg/network/irc`).
Это не бэкдор в смысле удалённого доступа к ПК, но обмен с внешним сервером есть.

## Брендинг

- mod id `rose`, jar `rose-0.1.0+26.2.jar`, namespace ассетов `assets/rose`, конфиг `config/rose`.
- Классы `RoseClient`, `RoseVoicechatPlugin`, `RoseIrc`; строки `dimasik`/`NowDLC` заменены на `rose`/`Rose` (0 остатков).
- Логотипы: `assets/rose/icon.png` (иконка мода, рубиновая роза),
  `assets/rose/fonts/client_logo.png` (SDF-атлас глифа логотипа — новая марка),
  `assets/rose/images/mainmenu/*logo*`, `images/clickgui/logo.png`,
  `images/hud/killcards/logo.png`, `images/killspray/logo.png` — белые силуэты новой марки.
- Исходники брендинга — `assets/branding/` (не попадают в jar).

## Структура

```
src/main/java/
  settings/render/          точка входа (ClientInitializer), ресурсы, локализация
  sg/client/                ядро (RoseClient, сервисы)
  sg/render/                рендер: pipeline, шрифты, логотип, аватары
  sg/gui/                   GUI: click gui, HUD-редактор, меню, косметика
  sg/modules/               модули (combat, render, misc, hud)
  sg/mixin/                 125 миксинов + ConditionalMixinPlugin
  sg/network/irc/           IRC-подсистема (сообщения, пати, синхронизация)
  com/deluxe/, HdG/         вспомогательные подсистемы из базы
src/main/resources/
  fabric.mod.json, rose.mixins.json, rose.accesswidener
  assets/rose/              89 МБ: fonts, shaders, images, sounds, cosmetics, models
  assets/minecraft/         оверрайды ванильных моделей/шрифтов
  META-INF/jars/            luaj-jse-3.0.1.jar, onnxruntime-1.27.0.jar (nested libraries)
```

## Дальше

1. Запустить `runClient`, проверить загрузку, меню и HUD.
2. Отсмотреть новую марку в loading-экране, click gui и kill-эффектах.
3. Перекрасить палитру под рубиновую (`sg/theme`), если текущая не совпадает с брендом.
4. Решить судьбу IRC-подсистемы и «unhook».
