# rose client — автообновление исходников
# Запуск: правый клик по файлу -> "Выполнить с помощью PowerShell"
# либо:  powershell -ExecutionPolicy Bypass -File .\apply-update.ps1
param([string]$Root = "C:\Users\elytr\IdeaProjects\rose-client")

$ErrorActionPreference = 'Stop'
$Base = 'https://raw.githubusercontent.com/propalvlimbo-dev/9h7uefh780erg7h/arena/5f83f091-9h7uefh780erg7h'

$Files = @(
  @{ p = 'src/main/java/com/deluxe/modules/impl/misc/ModuleCategory.java'; v = 'fe5178db'; s = 1022 }
  @{ p = 'src/main/java/com/deluxe/util/time/ClickGuiTheme.java'; v = 'e7fc6242'; s = 5499 }
  @{ p = 'src/main/java/settings/render/PinnedServers.java'; v = '2edf446e'; s = 3640 }
  @{ p = 'src/main/java/sg/command/CommandRegistry.java'; v = 'bfd9a003'; s = 35360 }
  @{ p = 'src/main/java/sg/command/impl/TargetCommand.java'; v = '5c352533'; s = 5476 }
  @{ p = 'src/main/java/sg/ec/RoseWingModel.java'; v = 'f71d8c5d'; s = 4819 }
  @{ p = 'src/main/java/sg/ec/WingsRenderLayer.java'; v = 'd5126da0'; s = 6444 }
  @{ p = 'src/main/java/sg/gui/click/ClickGuiScreen.java'; v = '9058632d'; s = 104670 }
  @{ p = 'src/main/java/sg/gui/cosmetics/CosmeticsPanel.java'; v = '25d30d4d'; s = 71987 }
  @{ p = 'src/main/java/sg/gui/third/ThirdGuiCategory.java'; v = 'efd11415'; s = 1161 }
  @{ p = 'src/main/java/sg/gui/third/ThirdGuiScreen.java'; v = '8ec35a70'; s = 102308 }
  @{ p = 'src/main/java/sg/hud/HudElementRegistry.java'; v = '2aeec840'; s = 30904 }
  @{ p = 'src/main/java/sg/hud/TargetHud.java'; v = '7513269e'; s = 56529 }
  @{ p = 'src/main/java/sg/integration/discord/ipc/DiscordIpcClient.java'; v = '9b9eb770'; s = 4024 }
  @{ p = 'src/main/java/sg/integration/discord/ipc/DiscordIpcConnection.java'; v = '6f3676b2'; s = 2048 }
  @{ p = 'src/main/java/sg/integration/discord/ipc/WindowsDiscordIpcConnection.java'; v = 'a9b2fcdb'; s = 2991 }
  @{ p = 'src/main/java/sg/integration/discord/presence/DiscordPresenceConfig.java'; v = '92f76200'; s = 4119 }
  @{ p = 'src/main/java/sg/integration/discord/presence/DiscordPresenceDefaults.java'; v = '9ee90000'; s = 1869 }
  @{ p = 'src/main/java/sg/mixin/EntityMovementPoseStateMixin.java'; v = 'cdb40871'; s = 5913 }
  @{ p = 'src/main/java/sg/mixin/GameRendererWorldEffectsMixin.java'; v = 'ec49a543'; s = 16889 }
  @{ p = 'src/main/java/sg/mixin/HudCustomizationMixin.java'; v = 'a5956e01'; s = 7990 }
  @{ p = 'src/main/java/sg/mixin/InventoryScreenAnimationMixin.java'; v = '839e82a5'; s = 7986 }
  @{ p = 'src/main/java/sg/mixin/ItemInHandRendererAnimationMixin.java'; v = '22402e82'; s = 38866 }
  @{ p = 'src/main/java/sg/mixin/LocalPlayerMovementEventsMixin.java'; v = '8c990819'; s = 6973 }
  @{ p = 'src/main/java/sg/mixin/MinecraftLifecycleEventsMixin.java'; v = '688d835a'; s = 7644 }
  @{ p = 'src/main/java/sg/mixin/PictureInPictureRendererEntityEspMixin.java'; v = '58402602'; s = 6467 }
  @{ p = 'src/main/java/sg/modules/ModuleCategory.java'; v = '004a275e'; s = 737 }
  @{ p = 'src/main/java/sg/modules/ModuleRegistry.java'; v = 'ff58abf8'; s = 37653 }
  @{ p = 'src/main/java/sg/modules/impl/misc/AimingItems.java'; v = '53d470fa'; s = 7466 }
  @{ p = 'src/main/java/sg/modules/impl/misc/DiscordRPC.java'; v = 'bf4dd87e'; s = 1251 }
  @{ p = 'src/main/java/sg/modules/impl/misc/KillMessage.java'; v = 'f29f2b2f'; s = 13023 }
  @{ p = 'src/main/java/sg/modules/impl/movement/AutoSprint.java'; v = '33b90dcb'; s = 1648 }
  @{ p = 'src/main/java/sg/modules/impl/movement/ElytraBooster.java'; v = '1d428735'; s = 31338 }
  @{ p = 'src/main/java/sg/modules/impl/movement/ElytraMotion.java'; v = '0d8ffbf1'; s = 1650 }
  @{ p = 'src/main/java/sg/modules/impl/movement/Jesus.java'; v = 'cfc7f737'; s = 18754 }
  @{ p = 'src/main/java/sg/modules/impl/movement/Speed.java'; v = '3db64c5c'; s = 26675 }
  @{ p = 'src/main/java/sg/modules/impl/movement/Strafe.java'; v = 'e35cbdfc'; s = 20124 }
  @{ p = 'src/main/java/sg/modules/impl/movement/TargetStrafe.java'; v = 'd4ec500a'; s = 12039 }
  @{ p = 'src/main/java/sg/modules/impl/movement/WindHop.java'; v = 'f5264cf8'; s = 6965 }
  @{ p = 'src/main/java/sg/modules/impl/render/CatPetModule.java'; v = '51086b7a'; s = 14618 }
  @{ p = 'src/main/java/sg/modules/impl/render/Chams.java'; v = '51298248'; s = 17385 }
  @{ p = 'src/main/java/sg/modules/impl/render/ESP.java'; v = '3ac1cbe2'; s = 14038 }
  @{ p = 'src/main/java/sg/modules/impl/render/HoleESP.java'; v = '8d706136'; s = 24339 }
  @{ p = 'src/main/java/sg/modules/impl/render/KillCards.java'; v = 'f8c81c15'; s = 24911 }
  @{ p = 'src/main/java/sg/modules/impl/render/KillEffects.java'; v = '3c4385ba'; s = 81407 }
  @{ p = 'src/main/java/sg/modules/impl/render/KillSpray.java'; v = '040a02b1'; s = 14025 }
  @{ p = 'src/main/java/sg/modules/impl/render/Particles.java'; v = '0d8dfde1'; s = 94023 }
  @{ p = 'src/main/java/sg/modules/impl/render/Sakura.java'; v = '673cf791'; s = 20935 }
  @{ p = 'src/main/java/sg/modules/impl/render/SkeletonESP.java'; v = 'e634365f'; s = 11302 }
  @{ p = 'src/main/java/sg/modules/impl/render/SwingAnimations.java'; v = 'dd0443b1'; s = 101424 }
  @{ p = 'src/main/java/sg/render/AnimatedWingVariant.java'; v = '4c0755e7'; s = 4409 }
  @{ p = 'src/main/java/sg/render/AttachedCosmeticsRenderLayer.java'; v = '5cea42eb'; s = 7343 }
  @{ p = 'src/main/java/sg/render/BackpackPreview.java'; v = '9cfd9cf1'; s = 3678 }
  @{ p = 'src/main/java/sg/render/CosmeticGlow.java'; v = 'b44f362c'; s = 1909 }
  @{ p = 'src/main/java/sg/render/GuiCoordinateTransform.java'; v = '0d96543d'; s = 1460 }
  @{ p = 'src/main/java/sg/render/HatPreview.java'; v = 'c6b4c476'; s = 3380 }
  @{ p = 'src/main/java/sg/render/HatPreviewQueue.java'; v = 'fd68e7c3'; s = 1861 }
  @{ p = 'src/main/java/sg/render/PreviewDiag.java'; v = '504749ca'; s = 1807 }
  @{ p = 'src/main/java/sg/render/WingPreview.java'; v = 'f2c4385d'; s = 3626 }
  @{ p = 'src/main/java/sg/render/WingSelection.java'; v = '5eb6ba2a'; s = 1414 }
  @{ p = 'src/main/java/sg/render/pipeline/AimRaycast.java'; v = '642a1093'; s = 2469 }
  @{ p = 'src/main/java/sg/rotation/AcceleratingAimRotation.java'; v = 'e4d02821'; s = 4909 }
  @{ p = 'src/main/java/sg/rotation/DefensiveJitterRotation.java'; v = '0775980c'; s = 13454 }
  @{ p = 'src/main/java/sg/rotation/FunTimeSmoothMode.java'; v = '95cb31c1'; s = 2208 }
  @{ p = 'src/main/java/sg/rotation/HumanAimRotation.java'; v = 'ba5e8931'; s = 15046 }
  @{ p = 'src/main/java/sg/rotation/HumanizedRotation.java'; v = 'e22189aa'; s = 51196 }
  @{ p = 'src/main/java/sg/rotation/OscillatingAimRotation.java'; v = 'd1d61975'; s = 4711 }
  @{ p = 'src/main/java/sg/rotation/RandomizedSmoothRotation.java'; v = 'f356afa9'; s = 10298 }
  @{ p = 'src/main/java/sg/rotation/RotationController.java'; v = '5b6d65a6'; s = 15948 }
  @{ p = 'src/main/java/sg/rotation/SnapAimRotation.java'; v = '62173ceb'; s = 31390 }
  @{ p = 'src/main/java/sg/rotation/SpookyTimeSmoothMode.java'; v = '1d180298'; s = 3691 }
  @{ p = 'src/main/resources/assets/rose/cosmetics/catalog.json'; v = '34555197'; s = 32671 }
  @{ p = 'src/main/resources/assets/rose/cosmetics/hats/rose/animation.json'; v = 'e348b200'; s = 2196 }
  @{ p = 'src/main/resources/assets/rose/cosmetics/hats/rose/model.geo.json'; v = '8f7c8920'; s = 46862 }
  @{ p = 'src/main/resources/assets/rose/cosmetics/hats/rose/texture.png'; v = '16dea3bf'; s = 189 }
  @{ p = 'src/main/resources/assets/rose/images/mainmenu/gavs_loading_background.png'; v = '1c8840dd'; s = 283635 }
  @{ p = 'src/main/resources/assets/rose/textures/cosmetics/rose_hat_glow.png'; v = 'b0952d56'; s = 148 }
  @{ p = 'src/main/resources/assets/rose/textures/wings/rose_wings.png'; v = '3dc8637a'; s = 1198 }
  @{ p = 'src/main/resources/assets/rose/textures/wings/rose_wings_glow.png'; v = '29145efe'; s = 327 }
  @{ p = 'src/main/resources/rose.mixins.json'; v = 'ecf0998c'; s = 5314 }
)

$Remove = @(
  'src/main/java/sg/ec/AttackReadiness.java'
  'src/main/java/sg/ec/FallingTotemTrigger.java'
  'src/main/java/sg/ec/HealthTotemTrigger.java'
  'src/main/java/sg/ec/MissingArmorTotemTrigger.java'
  'src/main/java/sg/ec/NearbyAnchorTotemTrigger.java'
  'src/main/java/sg/ec/NearbyTntTotemTrigger.java'
  'src/main/java/sg/ec/TotemTrigger.java'
  'src/main/java/sg/gui/ArrowSelectorScreen.java'
  'src/main/java/sg/gui/SwapItemSelectorScreen.java'
  'src/main/java/sg/hud/PvPAssist.java'
  'src/main/java/sg/mixin/ProjectileUtilHitBoxMixin.java'
  'src/main/java/sg/modules/impl/combat/AiAimbot.java'
  'src/main/java/sg/modules/impl/combat/AiTester.java'
  'src/main/java/sg/modules/impl/combat/AimAssist.java'
  'src/main/java/sg/modules/impl/combat/Aimbot.java'
  'src/main/java/sg/modules/impl/combat/AntiBot.java'
  'src/main/java/sg/modules/impl/combat/AntiThors.java'
  'src/main/java/sg/modules/impl/combat/ArrowSwap.java'
  'src/main/java/sg/modules/impl/combat/Aura.java'
  'src/main/java/sg/modules/impl/combat/AutoAnchor.java'
  'src/main/java/sg/modules/impl/combat/AutoExplosion.java'
  'src/main/java/sg/modules/impl/combat/AutoPotion.java'
  'src/main/java/sg/modules/impl/combat/AutoShift.java'
  'src/main/java/sg/modules/impl/combat/AutoSwap.java'
  'src/main/java/sg/modules/impl/combat/AutoThrow.java'
  'src/main/java/sg/modules/impl/combat/AutoTotem.java'
  'src/main/java/sg/modules/impl/combat/AutoWebTrap.java'
  'src/main/java/sg/modules/impl/combat/Backtrack.java'
  'src/main/java/sg/modules/impl/combat/Blocker.java'
  'src/main/java/sg/modules/impl/combat/Criticals.java'
  'src/main/java/sg/modules/impl/combat/CrystalAura.java'
  'src/main/java/sg/modules/impl/combat/CrystalOptimizer.java'
  'src/main/java/sg/modules/impl/combat/ElytraTarget.java'
  'src/main/java/sg/modules/impl/combat/FastBow.java'
  'src/main/java/sg/modules/impl/combat/HitBox.java'
  'src/main/java/sg/modules/impl/combat/NoEntityTrace.java'
  'src/main/java/sg/modules/impl/combat/NoFriendDamage.java'
  'src/main/java/sg/modules/impl/combat/PvPHelper.java'
  'src/main/java/sg/modules/impl/combat/TargetPearl.java'
  'src/main/java/sg/modules/impl/combat/TriggerBot.java'
  'src/main/java/sg/modules/impl/combat/Velocity.java'
  'src/main/java/sg/modules/impl/init/ArrowSwapScreen.java'
  'src/main/java/sg/modules/impl/init/AutoSwapScreen.java'
  'src/main/java/sg/modules/impl/init/CombatTargetSelector.java'
  'src/main/java/sg/modules/impl/init/CrystalTotemCondition.java'
  'src/main/java/sg/modules/impl/init/StandaloneOnnxAim60hz.java'
  'src/main/java/sg/modules/impl/init/TotemConditionRegistry.java'
  'src/main/java/sg/util/time/AimDatasetRecorder.java'
)

Write-Host "=== Rose: обновление исходников ===" -ForegroundColor Magenta
Write-Host "Проект: $Root"

$ok = 0; $bad = 0
foreach ($f in $Files) {
  $dest = Join-Path $Root $f.p
  $dir = Split-Path -Parent $dest
  if (-not (Test-Path $dir)) { New-Item -ItemType Directory -Path $dir -Force | Out-Null }
  try {
    $cb = [guid]::NewGuid().ToString('N')
    Invoke-WebRequest -UseBasicParsing -Uri "$Base/$($f.p)?v=$($f.v)&cb=$cb" -Headers @{ 'Cache-Control' = 'no-cache'; 'Pragma' = 'no-cache' } -OutFile $dest
    $len = (Get-Item $dest).Length
    if ($len -eq $f.s) { $ok++; Write-Host ("  OK   {0,-9} {1}" -f $len, $f.p) -ForegroundColor DarkGreen }
    else { $bad++; Write-Host ("  SIZE {0} != {1}  {2}" -f $len, $f.s, $f.p) -ForegroundColor Red }
  } catch { $bad++; Write-Host ("  FAIL {0} -> {1}" -f $f.p, $_.Exception.Message) -ForegroundColor Red }
}

$removed = 0
foreach ($r in $Remove) {
  $path = Join-Path $Root $r
  if (Test-Path $path) {
    if (Test-Path $path -PathType Container) {
      Get-ChildItem -Path $path -Recurse -File | ForEach-Object { $removed++ }
      Remove-Item -Path $path -Recurse -Force
    } else { Remove-Item -Path $path -Force; $removed++ }
    Write-Host ("  DEL  {0}" -f $r) -ForegroundColor DarkYellow
  }
}

Write-Host ""
Write-Host ("Файлов обновлено: {0}   с ошибкой: {1}   удалено: {2}" -f $ok, $bad, $removed) -ForegroundColor Magenta
if ($bad -gt 0) { Write-Host "Есть ошибки — проверь вывод выше." -ForegroundColor Red; exit 1 }
Write-Host "Готово. В IntelliJ: File -> Reload from Disk, затем runClient." -ForegroundColor Green
