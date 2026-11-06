package sg.modules.impl.render;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.textures.GpuTextureView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.joml.Vector4i;
import settings.render.ClientResourceIds;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.ec.ClientFonts;
import sg.handler.PartyTrace;
import sg.handler.PartyClient;
import sg.handler.PartySession;
import sg.handler.PartyPosition;
import sg.render.pipeline.UiDrawing;
import sg.render.pipeline.UiBatchDrawing;
import sg.settings.BooleanSetting;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.animation.Easings;
import sg.util.time.ColorMath;
import sg.animation.FloatAnimation;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.settings.ColorSetting;
import sg.settings.FloatSetting;
import sg.events.impl.HudRenderEvent;
import sg.settings.MultiChoiceSetting;

public class Arrows extends ClientModule {
   private static final Pattern Ъ_a62c09ff = Pattern.compile(Arrows.с9в);
   private static final Identifier Ь = ClientResourceIds.getImageId(Arrows.с9ъ);
   private static final Vector4f В_50d413ac = new Vector4f(0.0F);
   private static final Vector4f Я_at_5df03283 = new Vector4f(0.0F, 0.0F, 1.0F, 1.0F);
   @SerializedName("А")
   private final MultiChoiceSetting А_at_5df03283;
   private final FloatSetting К;
   @SerializedName("3")
   private final BooleanSetting field_3;
   private final ColorSetting 诶;
   private final Map<Integer, Vec3> и;
   @SerializedName("9")
   private final Map<Integer, Vec3> field_9;
   private final List<Arrows.ArrowRenderState> щ;
   private final Matrix4f Д;
   @SerializedName("Ъ")
   private final Matrix4f Ъ_20497804;
   @SerializedName("В")
   private final Vector4i В_1a8864ae;
   private long сМ;
   private int В弟;
   private int Вб;
   private int ВЫ;
   private String са;
   private final FloatAnimation Г;
   private final EventListener<PlayerTickEvent> ц;
   private final EventListener<HudRenderEvent> я;
   private static final String сиР = "Arrows";
   private static final String си3 = "Указатели на игроков";
   private static final String сир = "Настройки";
   private static final String си6 = "Имя";
   private static final String си_ = "Дистанция";
   private static final String сиШ = "Размер";
   private static final float сиы = 5.0F;
   private static final float сий = 0.1F;
   private static final String сиЖ = "Игроки группы";
   private static final String сим = "Цвет игроков группы";
   private static final int сиК = -9840385;
   private static final String сиI = "never-rendered";
   private static final float сиТ = 6.6667F;
   private static final String сиО = "arrows-frame";
   private static final long сиФ = 5000L;
   private static final String сиЙ = "no-world-or-player";
   private static final String сик = "ARROWS_SKIP";
   private static final float сиг = 0.1F;
   private static final float си0 = 0.5F;
   private static final float сих = 0.5F;
   private static final float сиУ = 30.0F;
   private static final float сиЗ = 6.0F;
   private static final String сиЕ = "Имя";
   private static final String сиП = "Дистанция";
   private static final String си西 = "rendering";
   private static final String сид = "party-visibility-off";
   private static final String сиБ = "ARROWS_READ";
   private static final String сиС = "ARROW_FILTER";
   private static final String сиз = "ARROW_FILTER";
   private static final String сиц = "ARROW_QUEUE";
   private static final String сия = "ARROW_FILTER";
   private static final String с9с = "ARROW_FILTER";
   private static final String с9В = "ARROW_DEDUP";
   private static final String с9Я = "ARROW_QUEUE";
   private static final String с9Х = "loaded-entity";
   private static final String с9е = "stored-irc-position";
   private static final String с9А = "no-arrow-entries";
   private static final String с94 = "party-visibility-off";
   private static final String с9п = "ARROWS_EMPTY";
   private static final float с9Д = -1.0F;
   private static final float с9Ъ = 0.5F;
   private static final float с9т = 0.5F;
   private static final String с9а = "ARROW_SUBMIT";
   private static final float с9Ю = 0.5F;
   private static final float с9Ч = 14.0F;
   private static final float с9Л = 0.5F;
   private static final float с9诶 = 6.0F;
   private static final String с91 = "batch-completed";
   private static final String с9Ь = "ARROWS_BATCH_END";
   private static final String с9衣 = "ARROWS_ENABLE";
   private static final String с9ш = "ARROWS_DISABLE";
   private static final double с9И = 90.0;
   private static final float с9ь = 60.0F;
   private static final float с9ж = 1.15F;
   private static final float с9弟 = 0.5F;
   private static final String с9б = "ARROW_FILTER";
   private static final double с9Ы = 150.0;
   private static final String с9l = "ARROW_FILTER";
   private static final long с98 = -1L;
   private static final long с95 = 1000000L;
   private static final String с9в = "^\\w{3,16}$";
   private static final String с9ъ = "hud/arrow";

   private int 诶(
      int var1,
      String var2,
      String var3,
      double var4,
      double var6,
      float var8,
      float var9,
      float var10,
      float var11,
      float var12,
      int var13,
      int var14,
      int var15,
      int var16,
      boolean var17,
      boolean var18,
      boolean var19,
      boolean var20
   ) {
      float var21 = (float)(Math.toDegrees(Math.atan2(var6, var4)) - var8 - с9И);
      double var22 = Math.toRadians(var21);
      float var24 = с9ь * var11 * (var19 ? с9ж : 1.0F);
      float var25 = var9 + var24 * (float)Math.sin(var22);
      float var26 = var10 - var24 * (float)Math.cos(var22);
      float var27 = (var12 + 1.0F) * с9弟;
      if (!(var25 + var27 <= 0.0F) && !(var25 - var27 >= var13) && !(var26 + var27 <= 0.0F) && !(var26 - var27 >= var14)) {
         int var28 = var19 ? this.诶.getValue() : (RoseClient.getInstance().getServices().getFriends().О(var2) ? ColorMath.packArgb(0, 255, 0, с9Ы) : var15);
         if (var28 >>> 24 == 0) {
            if (var20) {
               PartyTrace.Ы(с9l, "reason=zero-color-alpha name=" + var2);
            }

            return var1;
         } else {
            Arrows.ArrowRenderState var29;
            if (var1 < this.щ.size()) {
               var29 = this.щ.get(var1);
            } else {
               var29 = new Arrows.ArrowRenderState();
               this.щ.add(var29);
            }

            var29.method_3(
               var25,
               var26,
               var12,
               var21,
               var11,
               var28,
               var16,
               var3,
               (int)Mth.sqrt((float)(var4 * var4 + var6 * var6)),
               var17 && var3 != null && !var3.isBlank(),
               var18
            );
            return var1 + 1;
         }
      } else {
         if (var20) {
            PartyTrace.Ы(с9б, "reason=outside-viewport name=" + var2 + " screenXY=" + var25 + "," + var26);
         }

         return var1;
      }
   }

   public Arrows() {
      super(сиР, си3, ModuleCategory.RENDER);
      this.А_at_5df03283 = new MultiChoiceSetting(сир, new ChoiceOption(си6, true), new ChoiceOption(си_, true));
      this.К = new FloatSetting(сиШ, 2.0F, 1.0F, сиы).withStep(сий);
      this.field_3 = new BooleanSetting(сиЖ, true);
      this.诶 = new ColorSetting(сим, true, сиК).visibleWhen(this.field_3::getValue);
      this.и = new HashMap<>();
      this.field_9 = new HashMap<>();
      this.щ = new ArrayList<>();
      this.Д = new Matrix4f();
      this.Ъ_20497804 = new Matrix4f();
      this.В_1a8864ae = new Vector4i();
      this.са = сиI;
      this.Г = new FloatAnimation(0.0F, сиТ, Easings.QUAD_OUT);
      this.ц = this::Ю;
      this.я = this::ь;
      this.addSettings(this.А_at_5df03283, this.field_3, this.诶, this.К);
   }

   private void Ю(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null) {
         for (Player var3 : MINECRAFT_CLIENT.level.players()) {
            if (var3 != MINECRAFT_CLIENT.player) {
               this.и.put(var3.getId(), this.field_9.getOrDefault(var3.getId(), var3.position()));
               this.field_9.put(var3.getId(), var3.position());
            }
         }

         this.и.keySet().removeIf(var0 -> MINECRAFT_CLIENT.level.getEntity(var0) == null);
         this.field_9.keySet().removeIf(var0 -> MINECRAFT_CLIENT.level.getEntity(var0) == null);
      }
   }

   public String О西() {
      return "arrowsEnabled="
         + this.isEnabled()
         + " partyVisible="
         + this.field_3.getValue()
         + " stored="
         + this.В弟
         + " queued="
         + this.Вб
         + " submitted="
         + this.ВЫ
         + " frame="
         + this.са
         + " renderAgeMs="
         + (this.сМ == 0L ? с98 : (System.nanoTime() - this.сМ) / с95);
   }

   private void ь(HudRenderEvent var1) {
      this.сМ = System.nanoTime();
      this.Вб = 0;
      this.ВЫ = 0;
      boolean var2 = PartyTrace.Р(сиО, сиФ);
      if (MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null) {
         this.Г.updateTarget(this.Г.getTarget());
         float var3 = MINECRAFT_CLIENT.gameRenderer.mainCamera().yRot();
         if (Math.abs(var3 - this.Г.getTarget()) >= сиг) {
            this.Г.updateTarget(var3);
         }

         float var4 = MINECRAFT_CLIENT.getDeltaTracker().getGameTimeDeltaPartialTick(true);
         double var5 = Mth.lerp(var4, MINECRAFT_CLIENT.player.xOld, MINECRAFT_CLIENT.player.getX());
         double var7 = Mth.lerp(var4, MINECRAFT_CLIENT.player.zOld, MINECRAFT_CLIENT.player.getZ());
         float var9 = this.К.getValue();
         int var10 = MINECRAFT_CLIENT.getWindow().getScreenWidth();
         int var11 = MINECRAFT_CLIENT.getWindow().getScreenHeight();
         float var12 = var10 * си0;
         float var13 = var11 * сих;
         float var14 = сиУ * var9;
         float var15 = (float)var10 / var11;
         float var16 = this.Г.getValue();
         int var17 = ThemeEditor.getColorArgb(ThemeColorSlot.MAIN);
         int var18 = Math.max(1, Math.round(сиЗ * var9));
         boolean var19 = this.А_at_5df03283.isSelected(сиЕ);
         boolean var20 = this.А_at_5df03283.isSelected(сиП);
         int var21 = 0;
         PartyClient var22 = PartyClient.Х_4d6328fc();
         boolean var23 = this.field_3.getValue();
         this.В弟 = var22.К().size();
         this.са = var23 ? си西 : сид;
         if (var2) {
            PartyTrace.Ы(сиБ, this.О西() + " viewport=" + var10 + "x" + var11);
         }

         HashSet var24 = new HashSet();

         for (Player var26 : MINECRAFT_CLIENT.level.players()) {
            if (var26 != MINECRAFT_CLIENT.player && var26.getId() != 1337 && !var26.isRemoved()) {
               String var27 = var26.getGameProfile().name();
               boolean var28 = var22.Х_bbeebd87(var26.getUUID(), var27);
               if (var28) {
                  var24.add(var26.getUUID());
               }

               if (var28 && !var23) {
                  if (var2) {
                     PartyTrace.Ы(сиС, "reason=party-visibility-off loaded=true uuid=" + var26.getUUID());
                  }
               } else if (var28 || var26.isAlive() && !var26.isSpectator() && !var26.isInvisible()) {
                  if (!Ъ_a62c09ff.matcher(var27).matches()) {
                     if (var2 && var28) {
                        PartyTrace.Ы(сиз, "reason=invalid-player-name name=" + var27);
                     }
                  } else {
                     Vec3 var29 = this.field_9.getOrDefault(var26.getId(), var26.position());
                     Vec3 var30 = this.и.getOrDefault(var26.getId(), var29);
                     double var31 = Mth.lerp(var4, var30.x, var29.x) - var5;
                     double var33 = Mth.lerp(var4, var30.z, var29.z) - var7;
                     int var35 = var21;
                     String var36 = var28 ? var22.Х_388008bb(var26.getUUID(), var27) : var27;
                     var21 = this.诶(
                        var21, var27, var36, var31, var33, var16, var12, var13, var9, var14, var10, var11, var17, var18, var19, var20, var28, var2 && var28
                     );
                     if (var21 > var35 && var28) {
                        this.щ.get(var35).ХО = true;
                        this.Вб++;
                        if (var2) {
                           PartyTrace.Ы(сиц, "source=loaded-member uuid=" + var26.getUUID() + " name=" + var27);
                        }
                     }
                  }
               }
            }
         }

         long var38 = System.nanoTime();

         for (PartySession.SmoothedPosition var41 : var22.К().values()) {
            if (!var23) {
               if (var2) {
                  PartyTrace.Ы(сия, "reason=party-visibility-off stored=" + this.В弟);
               }
               break;
            }

            PartyPosition var43 = var41.Д();
            if (var43.uuid().equals(MINECRAFT_CLIENT.player.getUUID())) {
               if (var2) {
                  PartyTrace.Ы(с9с, "reason=self-uuid uuid=" + var43.uuid());
               }
            } else if (var24.contains(var43.uuid())) {
               if (var2) {
                  PartyTrace.Ы(с9В, "reason=loaded-party-member-priority uuid=" + var43.uuid());
               }
            } else {
               boolean var46 = var22.Х(var43);
               if (var46) {
                  Player var49 = var46 ? MINECRAFT_CLIENT.level.getPlayerByUUID(var43.uuid()) : null;
                  double var32 = var41.O(var38);
                  double var34 = var41.method__(var38);
                  if (var49 != null && !var49.isRemoved()) {
                     Vec3 var55 = this.field_9.getOrDefault(var49.getId(), var49.position());
                     Vec3 var37 = this.и.getOrDefault(var49.getId(), var55);
                     var32 = Mth.lerp(var4, var37.x, var55.x);
                     var34 = Mth.lerp(var4, var37.z, var55.z);
                  }

                  int var56 = var21;
                  var21 = this.诶(
                     var21,
                     var43.name(),
                     var43.member(),
                     var32 - var5,
                     var34 - var7,
                     var16,
                     var12,
                     var13,
                     var9,
                     var14,
                     var10,
                     var11,
                     var17,
                     var18,
                     var19,
                     var20,
                     true,
                     var2
                  );
                  if (var21 > var56) {
                     Arrows.ArrowRenderState var57 = this.щ.get(var56);
                     var57.ХО = true;
                     this.Вб++;
                     if (var2) {
                        PartyTrace.Ы(
                           с9Я,
                           "irc="
                              + var43.member()
                              + " uuid="
                              + var43.uuid()
                              + " source="
                              + (var49 != null && !var49.isRemoved() ? с9Х : с9е)
                              + " localWorld="
                              + var46
                              + " worldXZ="
                              + var32
                              + ","
                              + var34
                              + " screenXY="
                              + var57.Д弟
                              + ","
                              + var57.Дб
                              + " angle="
                              + var57.Дl
                              + " alpha="
                              + (var57.пЦ >>> 24)
                        );
                     }
                  }
               }
            }
         }

         if (var21 == 0) {
            this.са = var23 ? с9А : с94;
            if (var2) {
               PartyTrace.Ы(с9п, this.О西());
            }
         } else {
            UiDrawing.setTransform(this.Д.identity());
            this.Ъ_20497804.set(UiDrawing.getTransform());
            UiBatchDrawing.beginBatch(ClientFonts.getMediumFont());
            UiBatchDrawing.setAdditiveBlending(true);
            GpuTextureView var40 = MINECRAFT_CLIENT.getTextureManager().getTexture(Ь).getTextureView();
            Matrix4f var42 = UiDrawing.getTransform();

            for (int var44 = 0; var44 < var21; var44++) {
               Arrows.ArrowRenderState var47 = this.щ.get(var44);
               float var50 = с9Д + var47.Д弟 * 2.0F / var10;
               float var53 = 1.0F - var47.Дб * 2.0F / var11;
               var42.set(this.Ъ_20497804)
                  .translate(var50, var53, 0.0F)
                  .scale(1.0F, var15, 1.0F)
                  .rotateZ((float)(-Math.toRadians(var47.Дl)))
                  .scale(1.0F, 1.0F / var15, 1.0F)
                  .translate(-var50, -var53, 0.0F);
               this.В_1a8864ae.set(var47.пЦ);
               UiBatchDrawing.drawTextureFlipped(
                  var47.Д弟 - var47.ДЫ * с9Ъ, var47.Дб - var47.ДЫ * с9т, var47.ДЫ + 1.0F, var47.ДЫ + 1.0F, В_50d413ac, Я_at_5df03283, this.В_1a8864ae, var40
               );
               if (var47.ХО) {
                  this.ВЫ++;
                  if (var2) {
                     PartyTrace.Ы(с9а, "name=" + var47.Яп + " screenXY=" + var47.Д弟 + "," + var47.Дб + " texture=hud/arrow batch=true alpha=" + (var47.пЦ >>> 24));
                  }
               }
            }

            var42.set(this.Ъ_20497804);
            UiBatchDrawing.setAdditiveBlending(false);

            for (int var45 = 0; var45 < var21; var45++) {
               Arrows.ArrowRenderState var48 = this.щ.get(var45);
               if (var48.ХI) {
                  float var51 = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var48.Яп, var48.пч);
                  UiBatchDrawing.drawText(ClientFonts.getMediumFont(), var48.Яп, var48.Д弟 - var51 * с9Ю, var48.Дб - с9Ч * var48.Д8, var48.пч, -1);
               }

               if (var48.ХТ) {
                  String var52 = var48.ХД();
                  float var54 = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var52, var48.пч);
                  UiBatchDrawing.drawText(ClientFonts.getMediumFont(), var52, var48.Д弟 - var54 * с9Л, var48.Дб + с9诶 * var48.Д8, var48.пч, -1);
               }
            }

            UiBatchDrawing.endBatch(ClientFonts.getMediumFont());
            var42.set(this.Ъ_20497804);
            this.са = с91;
            if (var2) {
               PartyTrace.Ы(с9Ь, "allEntries=" + var21 + " " + this.О西());
            }
         }
      } else {
         this.са = сиЙ;
         if (var2) {
            PartyTrace.Ы(сик, this.О西());
         }
      }
   }

   @Override
   public void onEnable() {
      PartyTrace.Ы(с9衣, this.О西());
   }

   @Override
   public void onDisable() {
      PartyTrace.Ы(с9ш, this.О西());
      this.и.clear();
      this.field_9.clear();
   }

   private static final class ArrowRenderState {
      private float Д弟;
      private float Дб;
      private float ДЫ;
      private float Дl;
      private float Д8;
      private int пЦ;
      private int пч;
      private String Яп;
      private int пЭ;
      private int п必;
      private String ЯД;
      private boolean ХI;
      private boolean ХТ;
      private boolean ХО;
      private static final int ВЙ弟 = Integer.MIN_VALUE;

      private ArrowRenderState() {
         this.п必 = ВЙ弟;
      }

      private void method_3(float var1, float var2, float var3, float var4, float var5, int var6, int var7, String var8, int var9, boolean var10, boolean var11) {
         this.Д弟 = var1;
         this.Дб = var2;
         this.ДЫ = var3;
         this.Дl = var4;
         this.Д8 = var5;
         this.пЦ = var6;
         this.пч = var7;
         this.Яп = var8;
         this.пЭ = var9;
         this.ХI = var10;
         this.ХТ = var11;
         this.ХО = false;
      }

      private String ХД() {
         if (this.пЭ != this.п必) {
            this.п必 = this.пЭ;
            this.ЯД = this.пЭ + "m";
         }

         return this.ЯД;
      }
   }
}
