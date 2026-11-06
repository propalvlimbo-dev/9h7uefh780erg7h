package sg.network;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.textures.GpuTextureView;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Function;
import lombok.Generated;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.joml.Vector4i;
import sg.ec.MinecraftAccess;
import sg.ec.PlayerLatency;
import sg.ec.ClientTextureLookup;
import sg.ec.EntityHealth;
import sg.ec.ClientFonts;
import sg.events.impl.HudRenderEvent;
import sg.render.RoseBatchedItemAtlas;
import sg.render.WorldProjection;
import sg.render.pipeline.UiDrawing;
import sg.render.pipeline.UiBatchDrawing;
import sg.settings.EntityEspStyleSettings;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ColorMath;

public final class EntityEspLabelRenderer implements MinecraftAccess {
   private static final long ЯЪ = 1000L;
   private static final long Ят = 50L;
   private static final long Яа = 250L;
   private static final Vector4f HEAD_CORNER_RADII = new Vector4f(EntityEspLabelRenderer.HEAD_CORNER_RADIUS);
   private static final Vector4f HEAD_UV_BOUNDS = new Vector4f(EntityEspLabelRenderer.HEAD_TEXTURE_UV_SCALE);
   private static final Vector4i HEAD_TINT_CORNERS = new Vector4i(-1);
   private static final EquipmentSlot[] EQUIPMENT_SLOT_ORDER = new EquipmentSlot[]{
      EquipmentSlot.OFFHAND, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.MAINHAND
   };
   private static final Comparator<EntityEspLabelRenderer.TrackedLabel> FARTHEST_FIRST = Comparator.comparingDouble(trackedLabel -> -trackedLabel.state.distanceSquared);
   private static final float field_4Н = 26.0F;
   private static final float field_4н = 22.0F;
   private static final float field_4Ц = 24.0F;
   private static final float field_4ч = 28.0F;
   private static final int FRIEND_ACCENT_COLOR = ColorMath.packArgb(64, 255, 64, EntityEspLabelRenderer.FRIEND_ACCENT_ALPHA);
   @SerializedName("Х")
   private final Function<Entity, EntityEspStyleSettings> styleResolver;
   private final @com.google.gson.annotations.SerializedName("сн")  Map<Integer, EntityEspLabelRenderer.TrackedLabel> trackedLabelsByEntityId = new HashMap<>();
   private final @com.google.gson.annotations.SerializedName("св")  List<EntityEspLabelRenderer.TrackedLabel> visibleLabels = new ArrayList<>();
   private @com.google.gson.annotations.SerializedName("ЯЮ")  long nextEntityScanMillis;
   private @com.google.gson.annotations.SerializedName("В")  EntityEspLabelRenderer.EntityLabelState previewState;
   private @com.google.gson.annotations.SerializedName("И")  ClientLevel trackedLevel;
   private static final float lК = 0.001F;
   private static final float lI = 0.001F;
   private static final float lТ = 0.001F;
   private static final long ENTITY_SCAN_INTERVAL_MILLIS = 50L;
   private static final float lФ = 100.0F;
   private static final float lЙ = 0.5F;
   private static final float lк = 256.0F;
   private static final float lг = 0.5F;
   private static final float l0 = 26.0F;
   private static final float lх = 24.0F;
   private static final float lУ = 18.0F;
   private static final float lЗ = 26.0F;
   private static final float lЕ = 0.001F;
   private static final float lП = 0.5F;
   private static final float l西 = 0.5F;
   private static final float lд = 7.0F;
   private static final float lБ = 7.0F;
   private static final float lС = 0.22F;
   private static final float lз = 5.0F;
   private static final float lц = 4.0F;
   private static final String lя = "N";
   private static final float field_8с = 3.5F;
   private static final float field_8В = 7.5F;
   private static final float field_8Я = 6.0F;
   private static final float field_8Х = 6.0F;
   private static final float field_8е = 8.0F;
   private static final String field_8А = "E";
   private static final float field_84 = 5.0F;
   private static final float field_8п = 3.0F;
   private static final float field_8Д = 6.0F;
   private static final float field_8Ъ = 8.0F;
   private static final String field_8т = "Q";
   private static final float field_8а = 6.0F;
   private static final float field_8Ю = 6.0F;
   private static final float field_8Ч = 6.0F;
   private static final float field_8Л = 0.5F;
   private static final float field_8诶 = 11.0F;
   private static final float field_81 = 22.0F;
   private static final float field_8Ь = 4.0F;
   private static final float field_8衣 = 5.0F;
   private static final float field_8ш = 3.0F;
   private static final float field_8И = 28.0F;
   private static final float field_8ь = 24.0F;
   private static final float field_8ж = 0.5F;
   private static final float field_8弟 = 24.0F;
   private static final float field_8б = 6.0F;
   private static final float field_8Ы = 28.0F;
   private static final float field_8l = 24.0F;
   private static final float field_88 = 24.0F;
   private static final float field_85 = 12.0F;
   private static final float field_8в = 12.0F;
   private static final float field_8ъ = 10.0F;
   private static final double field_8М = 255.0;
   private static final float field_8Н = 0.5F;
   private static final float field_8н = 10.0F;
   private static final float field_8Ц = 18.0F;
   private static final String field_8ч = "E";
   private static final String field_8Э = "|";
   private static final float field_8必 = 5.0F;
   private static final float field_8щ = 6.0F;
   private static final float field_8Г = 8.0F;
   private static final float field_8л = 3.0F;
   private static final float field_8э = 5.0F;
   private static final float field_8ю = 8.0F;
   private static final float field_8Щ = 6.0F;
   private static final float field_8у = 26.0F;
   private static final long CONTENT_UPDATE_INTERVAL_MILLIS = 50L;
   private static final long NAME_UPDATE_INTERVAL_MILLIS = 250L;
   private static final long ITEM_UPDATE_INTERVAL_MILLIS = 250L;
   private static final String ITEM_COUNT_SEPARATOR = " / ";
   private static final float field_8о = 10.0F;
   private static final float field_8O = 10.0F;
   private static final float field_87 = 1.2F;
   private static final float field_8Р = 1.2F;
   private static final double field_83 = 0.25;
   private static final double field_8р = 0.25;
   private static final float field_86 = 26.0F;
   private static final float field_8_ = 26.0F;
   private static final String SHARPNESS_ABBREVIATION = "Sh";
   private static final String PROTECTION_ABBREVIATION = "Pr";
   private static final float TRACKED_ALPHA_THRESHOLD = 0.001F;
   private static final float HEAD_CORNER_RADIUS = 5.0F;
   private static final float HEAD_TEXTURE_UV_SCALE = 0.125F;
   private static final double FRIEND_ACCENT_ALPHA = 255.0;

   private void renderLivingEntityLabel(EntityEspLabelRenderer.EntityLabelState state, LivingEntity entity) {
      EntityEspStyleSettings style = state.style;
      boolean showName = style.getShowName().getValue();
      EntityEspLabelRenderer.Bounds nameBounds = showName
         ? this.renderNameCard(state, entity)
         : new EntityEspLabelRenderer.Bounds(state.screenX, state.screenY, 0.0F, 0.0F);
      if (style.getShowArmor() != null) {
         float equipmentAnchorY = showName ? nameBounds.y : state.screenY;
         this.renderEquipmentRow(state, entity, equipmentAnchorY);
      }
   }

   private boolean isAboveRecognizedEnchantmentLimit(String abbreviation, int level) {
      return abbreviation.equalsIgnoreCase(SHARPNESS_ABBREVIATION) && level > 5 || abbreviation.equalsIgnoreCase(PROTECTION_ABBREVIATION) && level > 4;
   }

   private boolean isLocalPlayerInFirstPerson(Entity entity) {
      return entity == MINECRAFT_CLIENT.player && MINECRAFT_CLIENT.options.getCameraType().isFirstPerson();
   }

   public EntityEspLabelRenderer(Function<Entity, EntityEspStyleSettings> styleResolver) {
      this.styleResolver = styleResolver;
   }

   private EntityEspLabelRenderer.Bounds renderNameCard(EntityEspLabelRenderer.EntityLabelState state, LivingEntity entity) {
      EntityEspLabelRenderer.LabelLayout layout = state.layout;
      if (layout == null) {
         state.layout = layout = this.createLabelLayout(state);
      }

      float left = state.screenX - layout.width * lП;
      float top = state.screenY - layout.height * l西;
      int accentColor = ThemeEditor.getColorArgb(ThemeColorSlot.MAIN);
      int textColor = ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_TEXT);
      int healthAccentColor = state.isFriendCategory() ? FRIEND_ACCENT_COLOR : accentColor;
      UiBatchDrawing.drawLayeredWindowBackground(left, top, layout.width, layout.height, lд);
      if (state.isFriendCategory()) {
         UiBatchDrawing.drawRoundedRect(left, top, layout.width, layout.height, lБ, ColorMath.withAlpha(FRIEND_ACCENT_COLOR, lС));
      }

      float cursorX = left + lз;
      GpuTextureView skinTexture = state.skinTexture;
      if (entity instanceof Player && skinTexture != null && !skinTexture.isClosed()) {
         UiBatchDrawing.drawTextureNearestFlipped(cursorX, top + lц, layout.headSize, layout.headSize, HEAD_CORNER_RADII, HEAD_UV_BOUNDS, HEAD_TINT_CORNERS, skinTexture);
      } else {
         this.drawIcon(lя, cursorX + field_8с, top + field_8В, layout.iconSize, accentColor);
      }

      cursorX += layout.headSize + field_8Я;
      this.drawComponentText(state.displayName, cursorX, top + field_8Х, layout.fontSize, -1);
      cursorX += layout.nameWidth + field_8е;
      this.drawIcon(field_8А, cursorX, top + field_84, layout.healthIconSize, healthAccentColor);
      cursorX += layout.healthIconWidth + field_8п;
      this.drawPlainText(state.healthText, cursorX, top + field_8Д, layout.fontSize, textColor);
      cursorX += layout.healthWidth;
      if (!state.latencyText.isEmpty()) {
         cursorX += field_8Ъ;
         this.drawIcon(field_8т, cursorX, top + field_8а, layout.iconSize, healthAccentColor);
         cursorX += layout.pingIconWidth + field_8Ю;
         this.drawPlainText(state.latencyText, cursorX, top + field_8Ч, layout.fontSize, textColor);
      }

      return new EntityEspLabelRenderer.Bounds(left, top, layout.width, layout.height);
   }

   private boolean updateProjectedAnchor(EntityEspLabelRenderer.EntityLabelState state, float tickDelta) {
      Entity entity = state.entity;
      double interpolatedX = entity.xo + (entity.getX() - entity.xo) * tickDelta;
      double interpolatedY = entity.yo + (entity.getY() - entity.yo) * tickDelta;
      double interpolatedZ = entity.zo + (entity.getZ() - entity.zo) * tickDelta;
      if (entity instanceof ItemEntity) {
         state.setWorldAnchor(interpolatedX, interpolatedY + entity.getBbHeight() * field_87, interpolatedZ);
         Vector2f itemScreenPosition = WorldProjection.projectToScreen(new Vec3(interpolatedX, interpolatedY + entity.getBbHeight() * field_8Р, interpolatedZ), false);
         if (itemScreenPosition == null) {
            return false;
         }

         state.screenX = itemScreenPosition.x;
         state.screenY = itemScreenPosition.y;
         state.screenPositionReady = true;
         return true;
      } else {
         state.setWorldAnchor(interpolatedX, interpolatedY + entity.getBbHeight() + field_83, interpolatedZ);
         Vector2f entityScreenPosition = WorldProjection.projectToScreen(new Vec3(interpolatedX, interpolatedY + entity.getBbHeight() + field_8р, interpolatedZ), true);
         if (entityScreenPosition == null) {
            return false;
         }

         state.screenX = entityScreenPosition.x;
         state.screenY = entityScreenPosition.y - field_86;
         state.screenPositionReady = true;
         return true;
      }
   }

   private boolean hasLabelFeatures(EntityEspStyleSettings style) {
      return style.getShowName().getValue() ? true : style.getShowArmor() != null && style.getShowArmor().getValue();
   }

   private void renderEnchantments(EntityEspLabelRenderer.EquipmentLabelState equipment, float centerX, float bottomY) {
      float cursorY = bottomY;

      for (EntityEspLabelRenderer.EnchantmentLabel enchantment : equipment.enchantments) {
         int color = enchantment.overLevelled ? ColorMath.packArgb(212, 78, 74, field_8М) : ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_TEXT);
         UiBatchDrawing.drawText(ClientFonts.getMediumFont(), enchantment.text, centerX - enchantment.width * field_8Н, cursorY, 10, color);
         cursorY -= field_8н;
      }
   }

   private float measureComponentWidth(Component component, int fontSize) {
      float[] widthAccumulator = new float[]{0.0F};
      component.visit((textStyle, textSegment) -> {
         widthAccumulator[0] += ClientFonts.getMediumFont().getFontFace().measureTextWidth(textSegment, fontSize, 0.0F);
         return Optional.empty();
      }, Style.EMPTY);
      return widthAccumulator[0];
   }

   private Component resolveDisplayName(LivingEntity entity) {
      if (entity instanceof Player player && NameProtectionTextRewriter.Д2()) {
         String profileName = player.getGameProfile().name();
         String protectedName = NameProtectionTextRewriter.С(player);
         boolean localPlayer = MINECRAFT_CLIENT.player != null && Objects.equals(player.getUUID(), MINECRAFT_CLIENT.player.getUUID());
         if (!Objects.equals(profileName, protectedName)) {
            MutableComponent protectedComponent = Component.literal(protectedName).setStyle(player.getName().getStyle());
            MutableComponent teamFormattedName = PlayerTeam.formatNameForTeam(player.getTeam(), protectedComponent);
            return localPlayer ? NameProtectionTextRewriter.必(teamFormattedName) : teamFormattedName;
         } else {
            return localPlayer ? NameProtectionTextRewriter.必(player.getDisplayName()) : player.getDisplayName();
         }
      } else {
         return NameProtectionTextRewriter.Е(entity.getDisplayName());
      }
   }

   private void updateEquipmentCache(EntityEspLabelRenderer.EntityLabelState state, LivingEntity entity, boolean languageChanged) {
      if (state.style.getShowArmor() != null && state.style.getShowArmor().getValue()) {
         boolean showEnchantments = state.style.getShowEnchantments() != null && state.style.getShowEnchantments().getValue();
         state.visibleEquipmentStacks.clear();
         state.enchantmentHeight = 0.0F;

         for (int slotIndex = 0; slotIndex < EQUIPMENT_SLOT_ORDER.length; slotIndex++) {
            ItemStack equippedStack = entity.getItemBySlot(EQUIPMENT_SLOT_ORDER[slotIndex]);
            EntityEspLabelRenderer.EquipmentLabelState equipment = state.equipmentLabels[slotIndex];
            if (equipment == null) {
               state.equipmentLabels[slotIndex] = equipment = new EntityEspLabelRenderer.EquipmentLabelState();
            }

            if (!ItemStack.matches(equippedStack, equipment.itemStack)) {
               equipment.itemStack = equippedStack.copy();
               equipment.enchantmentsCached = false;
            }

            if (!equippedStack.isEmpty()) {
               state.visibleEquipmentStacks.add(equipment.itemStack);
               if (showEnchantments) {
                  if (!equipment.enchantmentsCached || languageChanged) {
                     equipment.enchantments = this.createEnchantmentLabels(equipment.itemStack);
                     equipment.enchantmentsCached = true;
                  }

                  state.enchantmentHeight = Math.max(state.enchantmentHeight, equipment.enchantments.size() * field_8O);
               }
            }
         }
      }
   }

   private EntityEspLabelRenderer.LabelLayout createLabelLayout(EntityEspLabelRenderer.EntityLabelState state) {
      byte fontSize = 12;
      byte iconSize = 13;
      byte healthIconSize = 14;
      float headSize = field_8Ц;
      float nameWidth = this.measureComponentWidth(state.displayName, fontSize);
      float healthWidth = ClientFonts.getMediumFont().getFontFace().measureTextWidth(state.healthText, fontSize, 0.0F);
      float latencyWidth = ClientFonts.getMediumFont().getFontFace().measureTextWidth(state.latencyText, fontSize, 0.0F);
      float healthIconWidth = ClientFonts.getIconsFont().getFontFace().measureTextWidth(field_8ч, iconSize, 0.0F);
      float latencyIconWidth = ClientFonts.getIconsFont().getFontFace().measureTextWidth(field_8Э, iconSize, 0.0F);
      float cardWidth = field_8必 + headSize + field_8щ + nameWidth + field_8Г + healthIconWidth + field_8л + healthWidth + field_8э;
      if (!state.latencyText.isEmpty()) {
         cardWidth += field_8ю + latencyIconWidth + field_8Щ + latencyWidth;
      }

      state.nameWidth = nameWidth;
      return new EntityEspLabelRenderer.LabelLayout(cardWidth, field_8у, fontSize, iconSize, healthIconSize, headSize, nameWidth, healthWidth, latencyWidth, healthIconWidth, latencyIconWidth);
   }

   private void renderEquipmentRow(EntityEspLabelRenderer.EntityLabelState state, LivingEntity entity, float anchorY) {
      EntityEspStyleSettings style = state.style;
      boolean showArmor = style.getShowArmor() != null && style.getShowArmor().getValue();
      if (showArmor) {
         if (!state.visibleEquipmentStacks.isEmpty()) {
            float rowWidth = (state.visibleEquipmentStacks.size() - 1) * field_8И + field_8ь;
            float left = state.screenX - rowWidth * field_8ж;
            float top = anchorY - field_8弟 - field_8б;
            boolean showEnchantments = style.getShowEnchantments() != null && style.getShowEnchantments().getValue();
            int visibleIndex = 0;

            for (EntityEspLabelRenderer.EquipmentLabelState equipment : state.equipmentLabels) {
               if (equipment != null && !equipment.itemStack.isEmpty()) {
                  float itemX = left + visibleIndex++ * field_8Ы;
                  UiBatchDrawing.drawItemWithOverlay(itemX, top, field_8l, field_88, equipment.itemStack, true, equipment.itemHandle);
                  if (showEnchantments) {
                     this.renderEnchantments(equipment, itemX + field_85, top - field_8в);
                  }
               }
            }
         }
      }
   }

   private void scanEntitiesIfDue(long nowMillis) {
      if (nowMillis >= this.nextEntityScanMillis) {
         this.nextEntityScanMillis = nowMillis + ENTITY_SCAN_INTERVAL_MILLIS;

         for (EntityEspLabelRenderer.TrackedLabel trackedLabel : this.trackedLabelsByEntityId.values()) {
            trackedLabel.presentInLastScan = false;
         }

         for (Entity entity : MINECRAFT_CLIENT.level.entitiesForRendering()) {
            EntityEspStyleSettings style = this.styleResolver.apply(entity);
            if (style != null && style.getEnabled().getValue() && this.hasLabelFeatures(style) && !entity.isRemoved() && !this.isLocalPlayerInFirstPerson(entity)) {
               EntityEspLabelRenderer.TrackedLabel tracked = this.trackedLabelsByEntityId.get(entity.getId());
               if (tracked == null || tracked.state.entity != entity) {
                  tracked = new EntityEspLabelRenderer.TrackedLabel(new EntityEspLabelRenderer.EntityLabelState(entity));
                  this.trackedLabelsByEntityId.put(entity.getId(), tracked);
               }

               tracked.presentInLastScan = true;
               tracked.state.style = style;
            }
         }
      }
   }

   private void renderItemLabel(EntityEspLabelRenderer.EntityLabelState state) {
      if (state.style.getShowName().getValue()) {
         float width = state.itemLabelWidth;
         float left = state.screenX - width * field_8Л;
         float top = state.screenY - field_8诶;
         UiBatchDrawing.drawLayeredWindowBackground(left, top, width, field_81, field_8Ь);
         UiBatchDrawing.drawText(ClientFonts.getMediumFont(), state.itemDisplayName, left + field_8衣, top + field_8ш, 12, ThemeEditor.getColorArgb(ThemeColorSlot.WINDOW_TEXT));
      }
   }

   private void renderLabel(EntityEspLabelRenderer.EntityLabelState state) {
      if (state.entity instanceof LivingEntity livingEntity) {
         this.renderLivingEntityLabel(state, livingEntity);
      } else {
         if (state.entity instanceof ItemEntity) {
            this.renderItemLabel(state);
         }
      }
   }

   private void updateContentCache(EntityEspLabelRenderer.EntityLabelState state, long nowMillis) {
      Language language = Language.getInstance();
      boolean languageChanged = state.cachedLanguage != language;
      boolean nameProtectionEnabled = NameProtectionTextRewriter.Д2();
      boolean nameProtectionChanged = nameProtectionEnabled != state.nameProtectionEnabled;
      if (languageChanged || nameProtectionChanged || nowMillis >= state.nextContentUpdateMillis) {
         state.cachedLanguage = language;
         state.nameProtectionEnabled = nameProtectionEnabled;
         if (languageChanged) {
            for (EntityEspLabelRenderer.EquipmentLabelState equipment : state.equipmentLabels) {
               if (equipment != null) {
                  equipment.enchantmentsCached = false;
               }
            }
         }

         state.nextContentUpdateMillis = nowMillis + CONTENT_UPDATE_INTERVAL_MILLIS;
         Entity entity = state.entity;
         if (entity instanceof LivingEntity livingEntity) {
            if (state.style.getShowName().getValue()) {
               boolean layoutChanged = languageChanged || nameProtectionChanged || state.layout == null;
               if (layoutChanged || nowMillis >= state.nextNameOrItemUpdateMillis) {
                  state.nextNameOrItemUpdateMillis = nowMillis + NAME_UPDATE_INTERVAL_MILLIS;
                  Component displayName = this.resolveDisplayName(livingEntity);
                  String latencyText = livingEntity instanceof Player player ? Math.max(0, PlayerLatency.getLatencyMs(player.getUUID())) + "ms" : "";
                  layoutChanged |= !state.displayName.equals(displayName) || !state.latencyText.equals(latencyText);
                  state.displayName = displayName;
                  state.latencyText = latencyText;
                  state.skinTexture = livingEntity instanceof Player ? ClientTextureLookup.Н_dd184401(livingEntity) : null;
               }

               int roundedHealth = Math.max(0, Math.round(EntityHealth.getHealth(livingEntity)));
               if (roundedHealth != state.roundedHealth || state.healthText.isEmpty()) {
                  state.roundedHealth = roundedHealth;
                  state.healthText = Integer.toString(roundedHealth);
                  layoutChanged = true;
               }

               if (layoutChanged) {
                  state.layout = this.createLabelLayout(state);
               }
            }

            this.updateEquipmentCache(state, livingEntity, languageChanged);
         } else {
            if (entity instanceof ItemEntity itemEntity) {
               ItemStack itemStack = itemEntity.getItem();
               if (languageChanged || nameProtectionChanged || nowMillis >= state.nextNameOrItemUpdateMillis || !ItemStack.matches(itemStack, state.cachedItemStack)) {
                  state.nextNameOrItemUpdateMillis = nowMillis + ITEM_UPDATE_INTERVAL_MILLIS;
                  state.cachedItemStack = itemStack.copy();
                  state.displayName = NameProtectionTextRewriter.Е(itemStack.getHoverName());
                  state.itemCount = itemStack.getCount();
                  MutableComponent itemDisplayName = state.displayName.copy();
                  if (state.itemCount > 1) {
                     itemDisplayName.append(Component.literal(ITEM_COUNT_SEPARATOR).setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_GRAY)));
                     itemDisplayName.append(Component.literal(state.itemCount + "x").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
                  }

                  state.itemDisplayName = itemDisplayName;
                  state.itemLabelWidth = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), itemDisplayName.getString(), 12) + field_8о;
               }
            }
         }
      }
   }

   public void renderLabels(HudRenderEvent event) {
      if (MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null && event.getGraphics() != null) {
         if (this.trackedLevel != MINECRAFT_CLIENT.level) {
            this.clearTrackedLabels();
            this.trackedLevel = MINECRAFT_CLIENT.level;
         }

         long nowMillis = System.currentTimeMillis();
         float tickDelta = MINECRAFT_CLIENT.getDeltaTracker().getGameTimeDeltaPartialTick(true);
         this.scanEntitiesIfDue(nowMillis);
         this.visibleLabels.clear();
         Vec3 cameraPosition = MINECRAFT_CLIENT.gameRenderer.mainCamera().position();
         Iterator iterator = this.trackedLabelsByEntityId.values().iterator();

         while (iterator.hasNext()) {
            EntityEspLabelRenderer.TrackedLabel tracked = (EntityEspLabelRenderer.TrackedLabel)iterator.next();
            EntityEspLabelRenderer.EntityLabelState state = tracked.state;
            if (this.isLocalPlayerInFirstPerson(state.entity)) {
               state.hideImmediately();
               if (!tracked.presentInLastScan) {
                  iterator.remove();
               }
            } else {
               boolean eligible = tracked.presentInLastScan && this.isEntityPresent(state.entity) && state.style.getEnabled().getValue() && this.hasLabelFeatures(state.style);
               boolean projected = eligible && this.updateProjectedAnchor(state, tickDelta);
               state.setProjectedVisibility(projected, nowMillis);
               state.updateFade(nowMillis);
               if (!projected && state.alpha > lК && state.worldPositionReady) {
                  this.reprojectStoredAnchor(state);
               }

               if (state.alpha > lI && state.screenPositionReady && this.intersectsViewport(state)) {
                  this.updateContentCache(state, nowMillis);
                  double relativeX = state.anchorWorldX - cameraPosition.x;
                  double relativeY = state.anchorWorldY - cameraPosition.y;
                  double relativeZ = state.anchorWorldZ - cameraPosition.z;
                  state.distanceSquared = relativeX * relativeX + relativeY * relativeY + relativeZ * relativeZ;
                  this.visibleLabels.add(tracked);
               }

               if (!tracked.presentInLastScan && state.alpha <= lТ) {
                  iterator.remove();
               }
            }
         }

         if (!this.visibleLabels.isEmpty()) {
            this.visibleLabels.sort(FARTHEST_FIRST);
            UiDrawing.resetTransforms();
            float previousGlobalAlpha = UiDrawing.getGlobalAlpha();
            UiBatchDrawing.discardBatch();
            UiBatchDrawing.beginBatch(ClientFonts.getMediumFont(), ClientFonts.getIconsFont());

            try {
               for (EntityEspLabelRenderer.TrackedLabel trackedLabel : this.visibleLabels) {
                  EntityEspLabelRenderer.EntityLabelState labelState = trackedLabel.state;
                  float previousLabelAlpha = UiDrawing.getGlobalAlpha();
                  UiDrawing.setGlobalAlpha(previousLabelAlpha * labelState.alpha);

                  try {
                     this.renderLabel(labelState);
                  } finally {
                     UiDrawing.setGlobalAlpha(previousLabelAlpha);
                  }
               }
            } finally {
               UiDrawing.setGlobalAlpha(previousGlobalAlpha);
               UiBatchDrawing.endBatch(ClientFonts.getMediumFont(), ClientFonts.getIconsFont());
            }
         }
      } else {
         this.clearTrackedLabels();
      }
   }

   private void drawIcon(String icon, float x, float y, int fontSize, int color) {
      UiBatchDrawing.drawText(ClientFonts.getIconsFont(), icon, x, y, fontSize, color);
   }

   private boolean intersectsViewport(EntityEspLabelRenderer.EntityLabelState state) {
      float halfWidth = Math.max(lФ, state.itemEntity ? state.itemLabelWidth * lЙ : (state.layout == null ? lк : state.layout.width * lг));
      float equipmentTop = state.screenY - l0 - lх - lУ - state.enchantmentHeight;
      return state.screenX + halfWidth >= 0.0F && state.screenX - halfWidth <= MINECRAFT_CLIENT.getWindow().getScreenWidth() && state.screenY + lЗ >= 0.0F && equipmentTop <= MINECRAFT_CLIENT.getWindow().getScreenHeight();
   }

   private void drawComponentText(Component component, float x, float y, int fontSize, int color) {
      UiBatchDrawing.drawText(ClientFonts.getMediumFont(), component, x, y, fontSize, color);
   }

   private void drawPlainText(String text, float x, float y, int fontSize, int color) {
      UiBatchDrawing.drawText(ClientFonts.getMediumFont(), text, x, y, fontSize, color);
   }

   public boolean isTrackedAndVisible(Entity entity) {
      EntityEspLabelRenderer.TrackedLabel tracked = entity == null ? null : this.trackedLabelsByEntityId.get(entity.getId());
      return tracked != null && tracked.state.alpha > TRACKED_ALPHA_THRESHOLD;
   }

   private List<EntityEspLabelRenderer.EnchantmentLabel> createEnchantmentLabels(ItemStack itemStack) {
      Set<? extends Entry<Holder<Enchantment>, Integer>> enchantmentEntries = EnchantmentHelper.getEnchantmentsForCrafting(itemStack).entrySet();
      if (enchantmentEntries.isEmpty()) {
         return List.of();
      }

      ArrayList<Entry<Holder<Enchantment>, Integer>> sortedEntries = new ArrayList<>(enchantmentEntries);
      sortedEntries.sort(Entry.comparingByValue(Comparator.reverseOrder()));
      ArrayList labels = new ArrayList(sortedEntries.size());

      for (Entry entry : sortedEntries) {
         String fullName = Enchantment.getFullname((Holder<Enchantment>)entry.getKey(), (Integer)entry.getValue()).getString();
         String abbreviation = fullName.substring(0, Math.min(2, fullName.length()));
         String text = abbreviation + entry.getValue();
         float width = ClientFonts.getMediumFont().getFontFace().measureTextWidth(text, field_8ъ, 0.0F);
         labels.add(new EntityEspLabelRenderer.EnchantmentLabel(text, width, this.isAboveRecognizedEnchantmentLimit((String)abbreviation, (Integer)entry.getValue())));
      }

      return labels;
   }

   private void reprojectStoredAnchor(EntityEspLabelRenderer.EntityLabelState state) {
      Vector2f screenPosition = WorldProjection.projectToScreen(new Vec3(state.anchorWorldX, state.anchorWorldY, state.anchorWorldZ), !state.itemEntity);
      if (screenPosition == null) {
         state.screenPositionReady = false;
      } else {
         state.screenX = screenPosition.x;
         state.screenY = screenPosition.y - (state.itemEntity ? 0.0F : field_8_);
         state.screenPositionReady = true;
      }
   }

   public void clearTrackedLabels() {
      this.trackedLabelsByEntityId.clear();
      this.visibleLabels.clear();
      this.nextEntityScanMillis = 0L;
      this.previewState = null;
      this.trackedLevel = null;
   }

   public void renderPreviewLabel(Entity entity, EntityEspStyleSettings style, float screenX, float screenY, float alpha) {
      if (entity != null && style != null && !(alpha <= lЕ)) {
         if (this.previewState == null || this.previewState.entity != entity) {
            this.previewState = new EntityEspLabelRenderer.EntityLabelState(entity);
         }

         EntityEspLabelRenderer.EntityLabelState state = this.previewState;
         state.entity = entity;
         state.style = style;
         state.screenX = screenX;
         state.screenY = screenY;
         state.screenPositionReady = true;
         state.alpha = Math.clamp(alpha, 0.0F, 1.0F);
         this.updateContentCache(state, System.currentTimeMillis());
         float previousGlobalAlpha = UiDrawing.getGlobalAlpha();
         UiBatchDrawing.discardBatch();
         UiBatchDrawing.beginBatch(ClientFonts.getMediumFont(), ClientFonts.getIconsFont());
         UiDrawing.setGlobalAlpha(previousGlobalAlpha * state.alpha);

         try {
            this.renderLabel(state);
         } finally {
            UiDrawing.setGlobalAlpha(previousGlobalAlpha);
            UiBatchDrawing.endBatch(ClientFonts.getMediumFont(), ClientFonts.getIconsFont());
         }
      }
   }

   public Collection<EntityEspLabelRenderer.TrackedLabel> getTrackedLabels() {
      return List.copyOf(this.trackedLabelsByEntityId.values());
   }

   private boolean isEntityPresent(Entity entity) {
      return !entity.isRemoved();
   }

   public static final class EntityLabelState {
      private final @com.google.gson.annotations.SerializedName("А西")  int entityId;
      private final @com.google.gson.annotations.SerializedName("съ")  List<ItemStack> visibleEquipmentStacks = new ArrayList<>(6);
      @SerializedName("В")
      private final EntityEspLabelRenderer.EquipmentLabelState[] equipmentLabels = new EntityEspLabelRenderer.EquipmentLabelState[EntityEspLabelRenderer.EQUIPMENT_SLOT_ORDER.length];
      @SerializedName("В")
      private Language cachedLanguage;
      private @com.google.gson.annotations.SerializedName("Яд")  boolean nameProtectionEnabled;
      @SerializedName("В")
      private EntityEspLabelRenderer.LabelLayout layout;
      private @com.google.gson.annotations.SerializedName("а")  GpuTextureView skinTexture;
      private @com.google.gson.annotations.SerializedName("诶")  ItemStack cachedItemStack = ItemStack.EMPTY;
      @SerializedName("Я")
      private Component itemDisplayName = Component.empty();
      private @com.google.gson.annotations.SerializedName("пп")  float itemLabelWidth;
      private @com.google.gson.annotations.SerializedName("пД")  float enchantmentHeight;
      private @com.google.gson.annotations.SerializedName("ВТ")  double distanceSquared;
      private @com.google.gson.annotations.SerializedName("Яl")  long nextContentUpdateMillis;
      private @com.google.gson.annotations.SerializedName("Я8")  long nextNameOrItemUpdateMillis;
      @SerializedName("4")
      private Entity entity;
      @SerializedName("Я")
      private EntityEspStyleSettings style;
      private @com.google.gson.annotations.SerializedName("ЯБ")  boolean itemEntity;
      private @com.google.gson.annotations.SerializedName("ЯС")  boolean screenPositionReady;
      private @com.google.gson.annotations.SerializedName("Яз")  boolean visible;
      private @com.google.gson.annotations.SerializedName("Х")  Component displayName = Component.empty();
      private @com.google.gson.annotations.SerializedName("В2")  String healthText = "";
      private @com.google.gson.annotations.SerializedName("Вф")  String latencyText = "";
      private @com.google.gson.annotations.SerializedName("пЪ")  float nameWidth;
      private @com.google.gson.annotations.SerializedName("пт")  float screenX;
      private @com.google.gson.annotations.SerializedName("па")  float screenY;
      private @com.google.gson.annotations.SerializedName("пЮ")  float alpha;
      private @com.google.gson.annotations.SerializedName("пЧ")  float fadeStartAlpha;
      private @com.google.gson.annotations.SerializedName("пЛ")  float fadeTargetAlpha;
      private @com.google.gson.annotations.SerializedName("Яц")  boolean worldPositionReady;
      private @com.google.gson.annotations.SerializedName("ВО")  double anchorWorldX;
      private @com.google.gson.annotations.SerializedName("ВФ")  double anchorWorldY;
      private @com.google.gson.annotations.SerializedName("ВЙ")  double anchorWorldZ;
      private @com.google.gson.annotations.SerializedName("Ад")  int roundedHealth;
      private @com.google.gson.annotations.SerializedName("АБ")  int itemCount;
      private @com.google.gson.annotations.SerializedName("Я5")  long fadeStartMillis;
      private static final float FADE_DURATION_MILLIS = 1000.0F;
      private static final float В0I = 6.0F;
      private static final float В0Т = 15.0F;
      private static final float В0О = 10.0F;
      private static final String FRIEND_CATEGORY_KEY = "players_friends";

      @Generated
      public float getItemLabelWidth() {
         return this.itemLabelWidth;
      }

      private void setWorldAnchor(double worldX, double worldY, double worldZ) {
         this.anchorWorldX = worldX;
         this.anchorWorldY = worldY;
         this.anchorWorldZ = worldZ;
         this.worldPositionReady = true;
      }

      private void setProjectedVisibility(boolean projected, long nowMillis) {
         if (projected) {
            this.showImmediately();
         } else if (!(this.entity instanceof Player)) {
            this.hideImmediately();
         } else if (this.visible != projected) {
            this.updateFade(nowMillis);
            this.visible = false;
            this.fadeStartAlpha = this.alpha;
            this.fadeTargetAlpha = 0.0F;
            this.fadeStartMillis = nowMillis;
         }
      }

      @Generated
      public float getEnchantmentHeight() {
         return this.enchantmentHeight;
      }

      @Generated
      public EntityEspStyleSettings getStyle() {
         return this.style;
      }

      @Generated
      public double getAnchorWorldY() {
         return this.anchorWorldY;
      }

      @Generated
      public float getAlpha() {
         return this.alpha;
      }

      @Generated
      public long getNextNameOrItemUpdateMillis() {
         return this.nextNameOrItemUpdateMillis;
      }

      @Generated
      public float getFadeTargetAlpha() {
         return this.fadeTargetAlpha;
      }

      @Generated
      public int getItemCount() {
         return this.itemCount;
      }

      @Generated
      public ItemStack getCachedItemStack() {
         return this.cachedItemStack;
      }

      @Generated
      public float getNameWidth() {
         return this.nameWidth;
      }

      @Generated
      public boolean isWorldPositionReady() {
         return this.worldPositionReady;
      }

      @Generated
      public EntityEspLabelRenderer.EquipmentLabelState[] getEquipmentLabels() {
         return this.equipmentLabels;
      }

      @Generated
      public long getFadeStartMillis() {
         return this.fadeStartMillis;
      }

      @Generated
      public EntityEspLabelRenderer.LabelLayout getLayout() {
         return this.layout;
      }

      @Generated
      public Component getItemDisplayName() {
         return this.itemDisplayName;
      }

      @Generated
      public boolean isNameProtectionEnabled() {
         return this.nameProtectionEnabled;
      }

      @Generated
      public float getScreenX() {
         return this.screenX;
      }

      @Generated
      public boolean isItemEntity() {
         return this.itemEntity;
      }

      @Generated
      public boolean isScreenPositionReady() {
         return this.screenPositionReady;
      }

      private void showImmediately() {
         if (!this.visible || !(this.alpha >= 1.0F) || this.fadeStartMillis != 0L) {
            this.visible = true;
            this.alpha = 1.0F;
            this.fadeStartAlpha = 1.0F;
            this.fadeTargetAlpha = 1.0F;
            this.fadeStartMillis = 0L;
         }
      }

      @Generated
      public String getHealthText() {
         return this.healthText;
      }

      @Generated
      public int getEntityId() {
         return this.entityId;
      }

      @Generated
      public Entity getEntity() {
         return this.entity;
      }

      @Generated
      public float getScreenY() {
         return this.screenY;
      }

      @Generated
      public double getDistanceSquared() {
         return this.distanceSquared;
      }

      @Generated
      public double getAnchorWorldX() {
         return this.anchorWorldX;
      }

      @Generated
      public double getAnchorWorldZ() {
         return this.anchorWorldZ;
      }

      private boolean isFriendCategory() {
         return this.style != null && FRIEND_CATEGORY_KEY.equals(this.style.getConfigKey());
      }

      @Generated
      public long getNextContentUpdateMillis() {
         return this.nextContentUpdateMillis;
      }

      @Generated
      public float getFadeStartAlpha() {
         return this.fadeStartAlpha;
      }

      @Generated
      public List<ItemStack> getVisibleEquipmentStacks() {
         return this.visibleEquipmentStacks;
      }

      private void updateFade(long nowMillis) {
         if (this.fadeStartMillis == 0L) {
            this.alpha = this.visible ? 1.0F : 0.0F;
         } else {
            float progress = Math.clamp((float)(nowMillis - this.fadeStartMillis) / FADE_DURATION_MILLIS, 0.0F, 1.0F);
            float easedProgress = progress * progress * progress * (progress * (progress * В0I - В0Т) + В0О);
            this.alpha = this.fadeStartAlpha + (this.fadeTargetAlpha - this.fadeStartAlpha) * easedProgress;
            if (progress >= 1.0F) {
               this.alpha = this.fadeTargetAlpha;
               this.fadeStartMillis = 0L;
            }
         }
      }

      @Generated
      public Language getCachedLanguage() {
         return this.cachedLanguage;
      }

      @Generated
      public int getRoundedHealth() {
         return this.roundedHealth;
      }

      @Generated
      public GpuTextureView getSkinTexture() {
         return this.skinTexture;
      }

      private void hideImmediately() {
         this.visible = false;
         this.alpha = 0.0F;
         this.fadeStartAlpha = 0.0F;
         this.fadeTargetAlpha = 0.0F;
         this.fadeStartMillis = 0L;
      }

      @Generated
      public Component getDisplayName() {
         return this.displayName;
      }

      private EntityLabelState(Entity entity) {
         this.entity = entity;
         this.entityId = entity.getId();
         this.itemEntity = entity instanceof ItemEntity;
      }

      @Generated
      public String getLatencyText() {
         return this.latencyText;
      }

      @Generated
      public boolean isVisible() {
         return this.visible;
      }
   }

   private record LabelLayout(
      float width,
      float height,
      int fontSize,
      int iconSize,
      int healthIconSize,
      float headSize,
      float nameWidth,
      float healthWidth,
      float pingWidth,
      float healthIconWidth,
      float pingIconWidth
   ) {
   }

   public record Bounds(float x, float y, float width, float height) {
   }

   public static final class TrackedLabel {
      private final @com.google.gson.annotations.SerializedName("Я")  EntityEspLabelRenderer.EntityLabelState state;
      private @com.google.gson.annotations.SerializedName("е6")  boolean presentInLastScan;

      @Generated
      public boolean isPresentInLastScan() {
         return this.presentInLastScan;
      }

      private TrackedLabel(EntityEspLabelRenderer.EntityLabelState state) {
         this.state = state;
      }

      @Generated
      public EntityEspLabelRenderer.EntityLabelState getState() {
         return this.state;
      }
   }

   private record EnchantmentLabel(String text, float width, boolean overLevelled) {
   }

   private static final class EquipmentLabelState {
      private @com.google.gson.annotations.SerializedName("Л")  ItemStack itemStack = ItemStack.EMPTY;
      private final @com.google.gson.annotations.SerializedName("Я")  RoseBatchedItemAtlas.ItemHandle itemHandle = new RoseBatchedItemAtlas.ItemHandle();
      private @com.google.gson.annotations.SerializedName("с5")  List<EntityEspLabelRenderer.EnchantmentLabel> enchantments = List.of();
      private @com.google.gson.annotations.SerializedName("Я7")  boolean enchantmentsCached;
   }
}
