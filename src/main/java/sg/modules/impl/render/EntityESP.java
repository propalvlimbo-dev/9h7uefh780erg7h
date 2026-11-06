package sg.modules.impl.render;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import lombok.Generated;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import sg.events.impl.EntityRenderStateEvent;
import sg.events.impl.PlayerNameTagRenderEvent;
import sg.events.EventListener;
import sg.network.EntityEspLabelRenderer;
import sg.render.pipeline.EntityEspSurface;
import sg.settings.EntityEspStyleSettings;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ActionSetting;
import sg.gui.click.ClickGuiScreen;
import sg.events.impl.HudRenderEvent;

public class EntityESP extends ClientModule {
   @SerializedName("Х")
   private final EntityEspStyleSettings selfStyle;
   @SerializedName("е")
   private final EntityEspStyleSettings friendStyle;
   @SerializedName("А")
   private final EntityEspStyleSettings playerStyle;
   @SerializedName("4")
   private final EntityEspStyleSettings mobStyle;
   @SerializedName("п")
   private final EntityEspStyleSettings villagerStyle;
   private final @com.google.gson.annotations.SerializedName("Д")  EntityEspStyleSettings itemStyle;
   private final @com.google.gson.annotations.SerializedName("сР")  List<EntityEspStyleSettings> styles;
   @SerializedName("В")
   private final EntityEspLabelRenderer labelRenderer;
   @SerializedName("В")
   private final EntityEspSurface surfaceRenderer;
   @SerializedName("4")
   private final ActionSetting openSettingsAction;
   private final @com.google.gson.annotations.SerializedName("ЯО")  EventListener<HudRenderEvent> hudRenderListener;
   private final @com.google.gson.annotations.SerializedName("ЯФ")  EventListener<PlayerNameTagRenderEvent> nameTagListener;
   private final @com.google.gson.annotations.SerializedName("ЯЙ")  EventListener<EntityRenderStateEvent> renderStateListener;
   private static final String MODULE_NAME = "EntityESP";
   private static final String MODULE_DESCRIPTION = "Подсвечивает выбранные сущности";
   private static final String SELF_LABEL = "Себя";
   private static final String SELF_CATEGORY_ID = "players_self";
   private static final String FRIENDS_LABEL = "Друзей";
   private static final String FRIENDS_CATEGORY_ID = "players_friends";
   private static final String PLAYERS_LABEL = "Игроков";
   private static final String PLAYERS_CATEGORY_ID = "players_all";
   private static final String MOBS_LABEL = "Мобы";
   private static final String MOBS_CATEGORY_ID = "mobs";
   private static final String VILLAGERS_LABEL = "Жители";
   private static final String VILLAGERS_CATEGORY_ID = "villagers";
   private static final String ITEMS_LABEL = "Предметы";
   private static final String ITEMS_CATEGORY_ID = "items";
   private static final String OPEN_SETTINGS_LABEL = "Открыть настройки";
   private static final String CATEGORIES_SAVE_KEY = "Categories";
   private static final String CATEGORIES_LOAD_KEY = "Categories";

   private void openStyleEditor() {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (MINECRAFT_CLIENT.gui.screen() instanceof ClickGuiScreen clickGui) {
            clickGui.openEntityEsp1Editor(this);
         }
      }
   }

   @Generated
   public EventListener<EntityRenderStateEvent> getRenderStateListener() {
      return this.renderStateListener;
   }

   @Generated
   public EntityEspStyleSettings getMobStyle() {
      return this.mobStyle;
   }

   @Generated
   public EntityEspSurface getSurfaceRenderer() {
      return this.surfaceRenderer;
   }

   @Generated
   public List<EntityEspStyleSettings> getStyles() {
      return this.styles;
   }

   @Override
   public JsonObject saveConfig() {
      JsonObject config = super.saveConfig();
      JsonObject categories = new JsonObject();

      for (EntityEspStyleSettings style : this.styles) {
         categories.add(style.getConfigKey(), style.saveConfig());
      }

      config.add(CATEGORIES_SAVE_KEY, categories);
      return config;
   }

   @Generated
   public EntityEspStyleSettings getFriendStyle() {
      return this.friendStyle;
   }

   @Generated
   public ActionSetting getOpenSettingsAction() {
      return this.openSettingsAction;
   }

   @Generated
   public EntityEspStyleSettings getSelfStyle() {
      return this.selfStyle;
   }

   @Override
   public void loadConfig(JsonObject config) {
      super.loadConfig(config);
      if (config != null) {
         JsonElement categories = config.get(CATEGORIES_LOAD_KEY);
         if (categories != null && categories.isJsonObject()) {
            for (EntityEspStyleSettings style : this.styles) {
               JsonElement categoryConfig = categories.getAsJsonObject().get(style.getConfigKey());
               if (categoryConfig != null && categoryConfig.isJsonObject()) {
                  style.loadConfig(categoryConfig.getAsJsonObject());
               }
            }
         }
      }
   }

   @Generated
   public EventListener<PlayerNameTagRenderEvent> getNameTagListener() {
      return this.nameTagListener;
   }

   @Generated
   public EntityEspStyleSettings getPlayerStyle() {
      return this.playerStyle;
   }

   @Generated
   public EntityEspStyleSettings getVillagerStyle() {
      return this.villagerStyle;
   }

   public void renderLabels(HudRenderEvent event) {
      this.labelRenderer.renderLabels(event);
   }

   @Generated
   public EventListener<HudRenderEvent> getHudRenderListener() {
      return this.hudRenderListener;
   }

   private EntityEspStyleSettings getStyleForEntity(Entity entity) {
      if (entity == MINECRAFT_CLIENT.player) {
         return this.selfStyle;
      } else if (entity instanceof Player player) {
         return RoseClient.getInstance().getServices().getFriends().О(player.getGameProfile().name()) ? this.friendStyle : this.playerStyle;
      } else if (entity instanceof Villager) {
         return this.villagerStyle;
      } else if (entity instanceof ItemEntity) {
         return this.itemStyle;
      } else {
         return entity instanceof LivingEntity ? this.mobStyle : null;
      }
   }

   @Generated
   public EntityEspLabelRenderer getLabelRenderer() {
      return this.labelRenderer;
   }

   public EntityESP() {
      super(MODULE_NAME, MODULE_DESCRIPTION, ModuleCategory.RENDER);
      this.selfStyle = new EntityEspStyleSettings(SELF_LABEL, SELF_CATEGORY_ID, EntityEspStyleSettings.EntityKind.PLAYER, false);
      this.friendStyle = new EntityEspStyleSettings(FRIENDS_LABEL, FRIENDS_CATEGORY_ID, EntityEspStyleSettings.EntityKind.PLAYER, true);
      this.playerStyle = new EntityEspStyleSettings(PLAYERS_LABEL, PLAYERS_CATEGORY_ID, EntityEspStyleSettings.EntityKind.PLAYER, true);
      this.mobStyle = new EntityEspStyleSettings(MOBS_LABEL, MOBS_CATEGORY_ID, EntityEspStyleSettings.EntityKind.LIVING, false);
      this.villagerStyle = new EntityEspStyleSettings(VILLAGERS_LABEL, VILLAGERS_CATEGORY_ID, EntityEspStyleSettings.EntityKind.LIVING, false);
      this.itemStyle = new EntityEspStyleSettings(ITEMS_LABEL, ITEMS_CATEGORY_ID, EntityEspStyleSettings.EntityKind.ITEM, true);
      this.styles = List.of(this.selfStyle, this.friendStyle, this.playerStyle, this.mobStyle, this.villagerStyle, this.itemStyle);
      this.labelRenderer = new EntityEspLabelRenderer(this::getStyleForEntity);
      this.surfaceRenderer = new EntityEspSurface(this::getStyleForEntity, this::getStyles);
      this.openSettingsAction = new ActionSetting(OPEN_SETTINGS_LABEL).onClick(this::openStyleEditor).showOtherSettings();
      this.hudRenderListener = new EventListener<HudRenderEvent>() {
         @Override
         public int getPriority() {
            return -60;
         }

         public void onEvent(HudRenderEvent var1) {
            EntityESP.this.renderLabels(var1);
         }
      };
      this.nameTagListener = this::cancelVanillaNameTag;
      this.renderStateListener = this.surfaceRenderer::onEntityRenderState;
      this.addSettings(this.openSettingsAction);
   }

   public void cancelVanillaNameTag(PlayerNameTagRenderEvent event) {
      event.setCancelled(true);
   }

   @Generated
   public EntityEspStyleSettings getItemStyle() {
      return this.itemStyle;
   }
}
