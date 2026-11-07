# rose client — автообновление исходников
# Запуск: правый клик по файлу -> "Выполнить с помощью PowerShell"
# либо:  powershell -ExecutionPolicy Bypass -File .\apply-update.ps1
param([string]$Root = "C:\Users\elytr\IdeaProjects\rose-client")

$ErrorActionPreference = 'Stop'
$Base = 'https://raw.githubusercontent.com/propalvlimbo-dev/9h7uefh780erg7h/arena/5f83f091-9h7uefh780erg7h'

$Files = @(
  @{ p = 'src/main/java/com/deluxe/modules/impl/misc/ModuleCategory.java'; v = '5fb8de32'; s = 1022 }
  @{ p = 'src/main/java/com/deluxe/util/time/ClickGuiTheme.java'; v = 'bc2b5b79'; s = 5499 }
  @{ p = 'src/main/java/settings/render/PinnedServers.java'; v = '1827151a'; s = 3640 }
  @{ p = 'src/main/java/sg/command/CommandRegistry.java'; v = '677d3519'; s = 35360 }
  @{ p = 'src/main/java/sg/command/impl/TargetCommand.java'; v = '3cf93851'; s = 5476 }
  @{ p = 'src/main/java/sg/ec/RoseWingModel.java'; v = '4fa8416d'; s = 2458 }
  @{ p = 'src/main/java/sg/ec/WingAnimationMath.java'; v = 'efba952f'; s = 1576 }
  @{ p = 'src/main/java/sg/ec/WingModel.java'; v = 'd9cc7adf'; s = 1141 }
  @{ p = 'src/main/java/sg/ec/WingPreviewRenderState.java'; v = 'da620a65'; s = 1005 }
  @{ p = 'src/main/java/sg/ec/WingsRenderLayer.java'; v = 'd7f6a2d7'; s = 5645 }
  @{ p = 'src/main/java/sg/gui/click/ClickGuiScreen.java'; v = '8af92fcf'; s = 104670 }
  @{ p = 'src/main/java/sg/gui/cosmetics/CosmeticsPanel.java'; v = '2aa673a2'; s = 71987 }
  @{ p = 'src/main/java/sg/gui/third/ThirdGuiCategory.java'; v = '4c583ed0'; s = 1161 }
  @{ p = 'src/main/java/sg/gui/third/ThirdGuiScreen.java'; v = 'b677129e'; s = 102308 }
  @{ p = 'src/main/java/sg/hud/HudElementRegistry.java'; v = '6af384a8'; s = 30904 }
  @{ p = 'src/main/java/sg/hud/TargetHud.java'; v = '968a0639'; s = 56529 }
  @{ p = 'src/main/java/sg/integration/discord/ipc/DiscordIpcClient.java'; v = '7b741a7e'; s = 4024 }
  @{ p = 'src/main/java/sg/integration/discord/ipc/DiscordIpcConnection.java'; v = '1226c278'; s = 2048 }
  @{ p = 'src/main/java/sg/integration/discord/ipc/WindowsDiscordIpcConnection.java'; v = '05dc85c9'; s = 2991 }
  @{ p = 'src/main/java/sg/integration/discord/presence/DiscordPresenceConfig.java'; v = 'b2ac73ed'; s = 4119 }
  @{ p = 'src/main/java/sg/integration/discord/presence/DiscordPresenceDefaults.java'; v = 'f51ca634'; s = 1869 }
  @{ p = 'src/main/java/sg/mixin/EntityMovementPoseStateMixin.java'; v = '2e6191c3'; s = 5913 }
  @{ p = 'src/main/java/sg/mixin/GameRendererWorldEffectsMixin.java'; v = '5e3be865'; s = 16889 }
  @{ p = 'src/main/java/sg/mixin/HudCustomizationMixin.java'; v = 'fe5b728b'; s = 7990 }
  @{ p = 'src/main/java/sg/mixin/InventoryScreenAnimationMixin.java'; v = 'ef042347'; s = 7986 }
  @{ p = 'src/main/java/sg/mixin/ItemInHandRendererAnimationMixin.java'; v = '4a63c133'; s = 38866 }
  @{ p = 'src/main/java/sg/mixin/LocalPlayerMovementEventsMixin.java'; v = '36b48931'; s = 6973 }
  @{ p = 'src/main/java/sg/mixin/MinecraftLifecycleEventsMixin.java'; v = 'fb2954a5'; s = 7644 }
  @{ p = 'src/main/java/sg/mixin/PictureInPictureRendererEntityEspMixin.java'; v = '22f1e5d2'; s = 6467 }
  @{ p = 'src/main/java/sg/modules/ModuleCategory.java'; v = 'baa4c2e2'; s = 737 }
  @{ p = 'src/main/java/sg/modules/ModuleRegistry.java'; v = 'eb71f62a'; s = 37653 }
  @{ p = 'src/main/java/sg/modules/impl/misc/AimingItems.java'; v = '95ebc96a'; s = 7466 }
  @{ p = 'src/main/java/sg/modules/impl/misc/DiscordRPC.java'; v = '703fbb90'; s = 1251 }
  @{ p = 'src/main/java/sg/modules/impl/misc/KillMessage.java'; v = '19e563f4'; s = 13023 }
  @{ p = 'src/main/java/sg/modules/impl/movement/AutoSprint.java'; v = '1b830516'; s = 1648 }
  @{ p = 'src/main/java/sg/modules/impl/movement/ElytraBooster.java'; v = '579e9621'; s = 31338 }
  @{ p = 'src/main/java/sg/modules/impl/movement/ElytraMotion.java'; v = '6311925c'; s = 1650 }
  @{ p = 'src/main/java/sg/modules/impl/movement/Jesus.java'; v = '589a9cd1'; s = 18754 }
  @{ p = 'src/main/java/sg/modules/impl/movement/Speed.java'; v = '14f67e0b'; s = 26675 }
  @{ p = 'src/main/java/sg/modules/impl/movement/Strafe.java'; v = '9b6c4627'; s = 20124 }
  @{ p = 'src/main/java/sg/modules/impl/movement/TargetStrafe.java'; v = '81f65639'; s = 12039 }
  @{ p = 'src/main/java/sg/modules/impl/movement/WindHop.java'; v = 'c30cbb44'; s = 6965 }
  @{ p = 'src/main/java/sg/modules/impl/render/CatPetModule.java'; v = '438f0c43'; s = 14618 }
  @{ p = 'src/main/java/sg/modules/impl/render/Chams.java'; v = '8c3166b7'; s = 17385 }
  @{ p = 'src/main/java/sg/modules/impl/render/ESP.java'; v = '0a5cb5c2'; s = 14038 }
  @{ p = 'src/main/java/sg/modules/impl/render/HoleESP.java'; v = '52ec6151'; s = 24339 }
  @{ p = 'src/main/java/sg/modules/impl/render/KillCards.java'; v = 'fa885922'; s = 24911 }
  @{ p = 'src/main/java/sg/modules/impl/render/KillEffects.java'; v = 'd0d822b1'; s = 81407 }
  @{ p = 'src/main/java/sg/modules/impl/render/KillSpray.java'; v = '9e49bf1d'; s = 14025 }
  @{ p = 'src/main/java/sg/modules/impl/render/Particles.java'; v = 'd9fe13c0'; s = 94023 }
  @{ p = 'src/main/java/sg/modules/impl/render/Sakura.java'; v = '9f775512'; s = 20935 }
  @{ p = 'src/main/java/sg/modules/impl/render/SkeletonESP.java'; v = 'd395fdce'; s = 11302 }
  @{ p = 'src/main/java/sg/modules/impl/render/SwingAnimations.java'; v = 'c6e1676c'; s = 101424 }
  @{ p = 'src/main/java/sg/render/AnimatedWingVariant.java'; v = '877d5b70'; s = 4451 }
  @{ p = 'src/main/java/sg/render/AttachedCosmeticsRenderLayer.java'; v = 'a2b4c0a3'; s = 6606 }
  @{ p = 'src/main/java/sg/render/AvianWingAnimator.java'; v = 'a9d33cf0'; s = 22853 }
  @{ p = 'src/main/java/sg/render/AvianWingModel.java'; v = 'dc34a954'; s = 10150 }
  @{ p = 'src/main/java/sg/render/BackpackColorVariant.java'; v = 'c60e418d'; s = 1384 }
  @{ p = 'src/main/java/sg/render/BackpackModel.java'; v = '9795b7bd'; s = 24534 }
  @{ p = 'src/main/java/sg/render/BackpackPreview.java'; v = '9761fe65'; s = 3678 }
  @{ p = 'src/main/java/sg/render/BackpackPreviewQueue.java'; v = '7ff14992'; s = 1981 }
  @{ p = 'src/main/java/sg/render/ClientCosmeticSelections.java'; v = '5b0d9497'; s = 15241 }
  @{ p = 'src/main/java/sg/render/ClientLogo.java'; v = '26014c9d'; s = 3464 }
  @{ p = 'src/main/java/sg/render/CosmeticAnimationClip.java'; v = 'ff557085'; s = 8489 }
  @{ p = 'src/main/java/sg/render/CosmeticAttachmentPoint.java'; v = '1f0cc6bc'; s = 926 }
  @{ p = 'src/main/java/sg/render/CosmeticCatalog.java'; v = 'b3503064'; s = 6073 }
  @{ p = 'src/main/java/sg/render/CosmeticCategory.java'; v = 'e6fbf39e'; s = 902 }
  @{ p = 'src/main/java/sg/render/CosmeticDefinition.java'; v = 'e96c9ee9'; s = 829 }
  @{ p = 'src/main/java/sg/render/CosmeticModel.java'; v = '97d76660'; s = 30065 }
  @{ p = 'src/main/java/sg/render/CosmeticModelRenderer.java'; v = 'f92a9e87'; s = 30024 }
  @{ p = 'src/main/java/sg/render/EmojiMaskVariant.java'; v = '53f67899'; s = 1589 }
  @{ p = 'src/main/java/sg/render/EmoteAnimationClip.java'; v = 'd795dacc'; s = 17220 }
  @{ p = 'src/main/java/sg/render/EmoteDefinition.java'; v = '991dd43b'; s = 3634 }
  @{ p = 'src/main/java/sg/render/EmoteFadeTimeline.java'; v = '59e63720'; s = 1534 }
  @{ p = 'src/main/java/sg/render/FontFace.java'; v = '1f5c112b'; s = 17199 }
  @{ p = 'src/main/java/sg/render/FontGlyph.java'; v = '46197465'; s = 10045 }
  @{ p = 'src/main/java/sg/render/FontMetadata.java'; v = '011ffef6'; s = 3031 }
  @{ p = 'src/main/java/sg/render/GeometryBoundsConsumer.java'; v = '2d9ed270'; s = 3285 }
  @{ p = 'src/main/java/sg/render/GuiCoordinateTransform.java'; v = '650fe995'; s = 1460 }
  @{ p = 'src/main/java/sg/render/HatCatalog.java'; v = '90d25941'; s = 7200 }
  @{ p = 'src/main/java/sg/render/HatDefinition.java'; v = 'd89c42b3'; s = 169 }
  @{ p = 'src/main/java/sg/render/HatPreview.java'; v = '3e557c8a'; s = 3380 }
  @{ p = 'src/main/java/sg/render/HatPreviewQueue.java'; v = '793eb31d'; s = 1625 }
  @{ p = 'src/main/java/sg/render/HeadCosmeticsRenderLayer.java'; v = '7d038b75'; s = 4898 }
  @{ p = 'src/main/java/sg/render/KaguneWingModel.java'; v = '9ce88980'; s = 15489 }
  @{ p = 'src/main/java/sg/render/LuaTransformMath.java'; v = 'e585397a'; s = 3903 }
  @{ p = 'src/main/java/sg/render/PlayerEmoteController.java'; v = '8dc17637'; s = 15788 }
  @{ p = 'src/main/java/sg/render/PlayerWingState.java'; v = 'c6b41385'; s = 1554 }
  @{ p = 'src/main/java/sg/render/PreviewDiag.java'; v = '102e42b3'; s = 1807 }
  @{ p = 'src/main/java/sg/render/RoseBatchedItemAtlas.java'; v = '21f947b1'; s = 15960 }
  @{ p = 'src/main/java/sg/render/ScriptParticleFactory.java'; v = 'f8c22488'; s = 5165 }
  @{ p = 'src/main/java/sg/render/ScriptedParticle.java'; v = '5f4f32b5'; s = 14324 }
  @{ p = 'src/main/java/sg/render/SimpleWingAnimator.java'; v = 'c104f905'; s = 2558 }
  @{ p = 'src/main/java/sg/render/UiProjectionBuffer.java'; v = 'a9242490'; s = 1045 }
  @{ p = 'src/main/java/sg/render/WeaponSkinVariant.java'; v = '190097f1'; s = 7471 }
  @{ p = 'src/main/java/sg/render/WingAnimator.java'; v = 'a8193ca9'; s = 198 }
  @{ p = 'src/main/java/sg/render/WingModelPart.java'; v = '85b24bb8'; s = 9470 }
  @{ p = 'src/main/java/sg/render/WingMotionState.java'; v = 'f8021e48'; s = 4874 }
  @{ p = 'src/main/java/sg/render/WingOutlineQueue.java'; v = '4e82ce78'; s = 2725 }
  @{ p = 'src/main/java/sg/render/WingPreview.java'; v = '6637688a'; s = 3663 }
  @{ p = 'src/main/java/sg/render/WingPreviewQueue.java'; v = '98cd046f'; s = 3510 }
  @{ p = 'src/main/java/sg/render/WingSelection.java'; v = '3c714c9a'; s = 1414 }
  @{ p = 'src/main/java/sg/render/WorldProjection.java'; v = 'f38186fc'; s = 7079 }
  @{ p = 'src/main/java/sg/render/avatar/AsyncTextureAtlas.java'; v = '25a2f209'; s = 5341 }
  @{ p = 'src/main/java/sg/render/avatar/ClasspathReloadableTexture.java'; v = 'de683f49'; s = 2397 }
  @{ p = 'src/main/java/sg/render/avatar/CustomParticleTextureCatalog.java'; v = 'f8913ef3'; s = 11665 }
  @{ p = 'src/main/java/sg/render/avatar/CustomPictureCatalog.java'; v = 'e4675751'; s = 8941 }
  @{ p = 'src/main/java/sg/render/avatar/HolyWorldStunRenderer.java'; v = 'd6c7d813'; s = 16038 }
  @{ p = 'src/main/java/sg/render/avatar/MainMenuLoadingRenderer.java'; v = '9de216ff'; s = 8940 }
  @{ p = 'src/main/java/sg/render/avatar/RainRoofHeightmap.java'; v = '6ffac6d6'; s = 6008 }
  @{ p = 'src/main/java/sg/render/avatar/WindowsMediaSession.java'; v = '9e518d15'; s = 22522 }
  @{ p = 'src/main/java/sg/render/effects/TntBlastEffect.java'; v = '72f624cc'; s = 17321 }
  @{ p = 'src/main/java/sg/render/pipeline/AimRaycast.java'; v = 'b2a1dd35'; s = 2469 }
  @{ p = 'src/main/java/sg/render/pipeline/AmbienceVolumeFog.java'; v = '208051f4'; s = 9583 }
  @{ p = 'src/main/java/sg/render/pipeline/CatPetModel.java'; v = 'ac171554'; s = 10660 }
  @{ p = 'src/main/java/sg/render/pipeline/CatPetRenderer.java'; v = 'ef155ee9'; s = 13224 }
  @{ p = 'src/main/java/sg/render/pipeline/CrystalPlacementPlanner.java'; v = '140ad666'; s = 16702 }
  @{ p = 'src/main/java/sg/render/pipeline/EntityEspOuterGlowRenderer.java'; v = 'a2deefd0'; s = 14083 }
  @{ p = 'src/main/java/sg/render/pipeline/EntityEspSurface.java'; v = '5b2db01c'; s = 103176 }
  @{ p = 'src/main/java/sg/render/pipeline/FontRenderPass.java'; v = '14cbd38d'; s = 24303 }
  @{ p = 'src/main/java/sg/render/pipeline/GlowDistortionRenderer.java'; v = 'd68ec672'; s = 9904 }
  @{ p = 'src/main/java/sg/render/pipeline/GravityGrid.java'; v = '88bf675a'; s = 4016 }
  @{ p = 'src/main/java/sg/render/pipeline/HandsItemBloom.java'; v = '300db8fd'; s = 80708 }
  @{ p = 'src/main/java/sg/render/pipeline/HitMarker.java'; v = '3274b160'; s = 928 }
  @{ p = 'src/main/java/sg/render/pipeline/InventoryActions.java'; v = '0a5a5f7a'; s = 6945 }
  @{ p = 'src/main/java/sg/render/pipeline/LightningTargetEspRenderer.java'; v = '74aaaed6'; s = 31711 }
  @{ p = 'src/main/java/sg/render/pipeline/ModuleGlare.java'; v = '233eb399'; s = 3661 }
  @{ p = 'src/main/java/sg/render/pipeline/NightixAurora.java'; v = '5cde5aa7'; s = 5549 }
  @{ p = 'src/main/java/sg/render/pipeline/NightixCubeGlow.java'; v = '8871f9ad'; s = 7753 }
  @{ p = 'src/main/java/sg/render/pipeline/OcclusionVoxelGrid.java'; v = 'e1297452'; s = 8115 }
  @{ p = 'src/main/java/sg/render/pipeline/OnnxAimPredictor.java'; v = '60d95722'; s = 7219 }
  @{ p = 'src/main/java/sg/render/pipeline/OutlinePass.java'; v = 'db6607e8'; s = 4415 }
  @{ p = 'src/main/java/sg/render/pipeline/PartyMarker.java'; v = 'be449e4a'; s = 32380 }
  @{ p = 'src/main/java/sg/render/pipeline/PotionWheel.java'; v = '72b9774d'; s = 6831 }
  @{ p = 'src/main/java/sg/render/pipeline/PreviewFade.java'; v = '71edfddc'; s = 7724 }
  @{ p = 'src/main/java/sg/render/pipeline/PrismaticChams.java'; v = 'a8d634f9'; s = 18471 }
  @{ p = 'src/main/java/sg/render/pipeline/RectangleRenderPass.java'; v = 'de3389b3'; s = 5135 }
  @{ p = 'src/main/java/sg/render/pipeline/RenderPipelineBuilder.java'; v = 'd5e20e43'; s = 8445 }
  @{ p = 'src/main/java/sg/render/pipeline/RoseChunkOffsets.java'; v = '4392b12e'; s = 5637 }
  @{ p = 'src/main/java/sg/render/pipeline/SharedGeometryBuffer.java'; v = '5492e031'; s = 1146 }
  @{ p = 'src/main/java/sg/render/pipeline/StaffNameText.java'; v = '22f087bb'; s = 2323 }
  @{ p = 'src/main/java/sg/render/pipeline/SurfaceButterfly.java'; v = '9c08812d'; s = 25658 }
  @{ p = 'src/main/java/sg/render/pipeline/TargetEspMask.java'; v = 'a2cd8a8e'; s = 279611 }
  @{ p = 'src/main/java/sg/render/pipeline/TargetLighting.java'; v = '7a66fd47'; s = 18257 }
  @{ p = 'src/main/java/sg/render/pipeline/TexturePass.java'; v = 'b720b3c2'; s = 6572 }
  @{ p = 'src/main/java/sg/render/pipeline/ThemeWave.java'; v = '2dc5a5de'; s = 13246 }
  @{ p = 'src/main/java/sg/render/pipeline/TimeDial.java'; v = '38517bb4'; s = 3823 }
  @{ p = 'src/main/java/sg/render/pipeline/TrailPathHistory.java'; v = '4c950667'; s = 3417 }
  @{ p = 'src/main/java/sg/render/pipeline/UiBatchDrawing.java'; v = '08148cd6'; s = 19664 }
  @{ p = 'src/main/java/sg/render/pipeline/UiBatchRenderer.java'; v = '452c00fb'; s = 35677 }
  @{ p = 'src/main/java/sg/render/pipeline/UiBlurPass.java'; v = '727c48b8'; s = 5658 }
  @{ p = 'src/main/java/sg/render/pipeline/UiDrawing.java'; v = '135dd699'; s = 24167 }
  @{ p = 'src/main/java/sg/render/pipeline/UiItemStackPass.java'; v = '71064a1b'; s = 18613 }
  @{ p = 'src/main/java/sg/render/pipeline/UiRenderPassSupport.java'; v = '8d9a8128'; s = 7404 }
  @{ p = 'src/main/java/sg/render/pipeline/ViewModelEditorCompositor.java'; v = '95130e67'; s = 9067 }
  @{ p = 'src/main/java/sg/render/pipeline/WardenFarmArea.java'; v = '3b3c4a04'; s = 2276 }
  @{ p = 'src/main/java/sg/render/pipeline/WorldColorGradingPass.java'; v = 'c8469aa0'; s = 5165 }
  @{ p = 'src/main/java/sg/render/pose/IndexedPoseTransform.java'; v = 'f0955b09'; s = 461 }
  @{ p = 'src/main/java/sg/render/pose/PoseTransformMarker.java'; v = 'c94b0562'; s = 66 }
  @{ p = 'src/main/java/sg/render/pose/RotateXPoseTransform.java'; v = 'e70e3d03'; s = 665 }
  @{ p = 'src/main/java/sg/render/pose/RotateYPoseTransform.java'; v = 'ed3af435'; s = 668 }
  @{ p = 'src/main/java/sg/render/pose/RotateZPoseTransform.java'; v = 'e944cbab'; s = 662 }
  @{ p = 'src/main/java/sg/render/pose/ScalePoseTransform.java'; v = 'c0cc0cfd'; s = 506 }
  @{ p = 'src/main/java/sg/render/pose/TranslateXPoseTransform.java'; v = 'cb1ede36'; s = 408 }
  @{ p = 'src/main/java/sg/render/pose/TranslateYPoseTransform.java'; v = 'e5b5a06d'; s = 500 }
  @{ p = 'src/main/java/sg/render/pose/TranslateZPoseTransform.java'; v = '48e5555a'; s = 408 }
  @{ p = 'src/main/java/sg/rotation/AcceleratingAimRotation.java'; v = '57294358'; s = 4909 }
  @{ p = 'src/main/java/sg/rotation/DefensiveJitterRotation.java'; v = '8d35df07'; s = 13454 }
  @{ p = 'src/main/java/sg/rotation/FunTimeSmoothMode.java'; v = '36436a91'; s = 2208 }
  @{ p = 'src/main/java/sg/rotation/HumanAimRotation.java'; v = '4693d577'; s = 15046 }
  @{ p = 'src/main/java/sg/rotation/HumanizedRotation.java'; v = '591f3d11'; s = 51196 }
  @{ p = 'src/main/java/sg/rotation/OscillatingAimRotation.java'; v = '7b962d88'; s = 4711 }
  @{ p = 'src/main/java/sg/rotation/RandomizedSmoothRotation.java'; v = '7fb7499c'; s = 10298 }
  @{ p = 'src/main/java/sg/rotation/RotationController.java'; v = '41d28b8d'; s = 15948 }
  @{ p = 'src/main/java/sg/rotation/SnapAimRotation.java'; v = '4d1e81d5'; s = 31390 }
  @{ p = 'src/main/java/sg/rotation/SpookyTimeSmoothMode.java'; v = '84dff756'; s = 3691 }
  @{ p = 'src/main/resources/assets/rose/cosmetics/catalog.json'; v = '9a8bceb6'; s = 32671 }
  @{ p = 'src/main/resources/assets/rose/cosmetics/hats/rose/animation.json'; v = '812382cc'; s = 2196 }
  @{ p = 'src/main/resources/assets/rose/cosmetics/hats/rose/model.geo.json'; v = 'b57c0ec0'; s = 46862 }
  @{ p = 'src/main/resources/assets/rose/cosmetics/hats/rose/texture.png'; v = 'bea645b1'; s = 189 }
  @{ p = 'src/main/resources/assets/rose/images/mainmenu/gavs_loading_background.png'; v = 'b132975a'; s = 283635 }
  @{ p = 'src/main/resources/assets/rose/textures/wings/rose_wings.png'; v = 'a3c37721'; s = 349439 }
  @{ p = 'src/main/resources/assets/rose/textures/wings/rose_wings_preview.png'; v = '47015256'; s = 50618 }
  @{ p = 'src/main/resources/rose.mixins.json'; v = 'fda2c525'; s = 5314 }
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
