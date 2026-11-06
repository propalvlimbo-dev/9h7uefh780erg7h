package sg.modules.impl.render;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.piston.PistonHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.joml.Vector4i;
import settings.render.ClientResourceIds;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.ec.ClientFonts;
import sg.render.WorldProjection;
import sg.render.pipeline.UiDrawing;
import sg.settings.BooleanSetting;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;
import sg.util.time.ColorMath;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.settings.ChoiceOption;
import sg.settings.FloatSetting;
import sg.events.impl.HudRenderEvent;
import sg.settings.MultiChoiceSetting;
import sg.settings.SingleChoiceSetting;

public final class TrapIndicator extends ClientModule {
   private static final Identifier Л = ClientResourceIds.getImageId(TrapIndicator.Х诶с);
   private static final int сШ = 3;
   private static final int сы = 70000;
   private static final int сй = 40;
   private static final int сЖ = 4;
   private final SingleChoiceSetting Ю;
   @SerializedName("6")
   private final FloatSetting field_6;
   @SerializedName("_")
   private final FloatSetting field__;
   @SerializedName("Х")
   private final MultiChoiceSetting Х_at_4ddd664d;
   private final BooleanSetting и;
   @SerializedName("9")
   private final BooleanSetting field_9;
   private final FloatSetting Ш;
   private final BooleanSetting о;
   private final Map<Long, TrapIndicator.TrapAccumulator> И;
   @SerializedName("8")
   private final List<TrapIndicator.TrapLocation> field_8;
   private final EventListener<PlayerTickEvent> Ж;
   private final EventListener<HudRenderEvent> м;
   @SerializedName("А")
   private ClientLevel А_at_4ddd664d;
   private TrapIndicator.ScanJob В;
   private int см;
   private int сК;
   private int сI;
   private int сТ;
   private static final String ХЛп = "TrapIndicator";
   private static final String ХЛД = "Указывает направление на трапки рядом";
   private static final String ХЛЪ = "Область";
   private static final String ХЛт = "Радиус";
   private static final String ХЛа = "Чанк";
   private static final String ХЛЮ = "Радиус";
   private static final float ХЛЧ = 50.0F;
   private static final float ХЛЛ = 16.0F;
   private static final float ХЛ诶 = 80.0F;
   private static final String ХЛ1 = "Порог";
   private static final float ХЛЬ = 20.0F;
   private static final float ХЛ衣 = 8.0F;
   private static final float ХЛш = 80.0F;
   private static final String ХЛИ = "Искать";
   private static final String ХЛь = "Стойки";
   private static final String ХЛж = "Поршни";
   private static final String ХЛ弟 = "Верстаки";
   private static final String ХЛб = "Ворота";
   private static final String ХЛЫ = "Метка";
   private static final String ХЛl = "GPS-указатель";
   private static final String ХЛ8 = "Размер указателя";
   private static final float ХЛ5 = 5.0F;
   private static final float ХЛв = 0.1F;
   private static final String ХЛъ = "Игнор. голограммы";
   private static final int ХЛМ = Integer.MIN_VALUE;
   private static final int ХЛН = Integer.MIN_VALUE;
   private static final String ХЛн = "TRAP";
   private static final String ХЛЦ = "Трапка";
   private static final float ХЛч = 16.0F;
   private static final float ХЛЭ = 12.0F;
   private static final float ХЛ必 = 12.0F;
   private static final float ХЛщ = 16.0F;
   private static final float ХЛГ = 32.0F;
   private static final float ХЛл = 0.5F;
   private static final float ХЛэ = 0.5F;
   private static final float ХЛю = 10.0F;
   private static final float ХЛЩ = 16.0F;
   private static final float ХЛу = 11.0F;
   private static final float ХЛ2 = 13.0F;
   private static final float ХЛф = 9.0F;
   private static final double ХЛи = 255.0;
   private static final float ХЛ9 = 12.0F;
   private static final float ХЛо = 11.0F;
   private static final float ХЛO = 0.5F;
   private static final float ХЛ7 = 90.0F;
   private static final float ХЛР = 0.5F;
   private static final float ХЛ3 = 0.5F;
   private static final float ХЛр = 60.0F;
   private static final float ХЛ6 = 60.0F;
   private static final float ХЛ_ = 30.0F;
   private static final float ХЛШ = -1.0F;
   private static final float ХЛы = 0.5F;
   private static final float ХЛй = 0.5F;
   private static final float ХЛЖ = 6.0F;
   private static final String ХЛм = "Трапка";
   private static final float ХЛК = 0.5F;
   private static final float ХЛI = 14.0F;
   private static final float ХЛТ = 0.5F;
   private static final float ХЛО = 6.0F;
   private static final String ХЛФ = "Чанк";
   private static final String ХЛЙ = "Поршни";
   private static final String ХЛк = "Верстаки";
   private static final String ХЛг = "Ворота";
   private static final int ХЛ0 = 70000;
   private static final int ХЛх = 70000;
   private static final double ХЛУ = 0.5;
   private static final double ХЛЗ = 0.5;
   private static final double ХЛЕ = 0.5;
   private static final String ХЛП = "Стойки";
   private static final String ХЛ西 = "Чанк";
   private static final String ХЛд = "Чанк";
   private static final String ХЛБ = "Чанк";
   private static final long ХЛС = 4294967295L;
   private static final int ХЛз = Integer.MIN_VALUE;
   private static final int ХЛц = Integer.MIN_VALUE;
   private static final String ХЛя = "Радиус";
   private static final String Х诶с = "hud/arrow";

   private static void М(Map<Long, Long> var0, long var1, long var3) {
      if (var0.containsKey(var3)) {
         long var5 = М(var0, var1);
         long var7 = М(var0, var3);
         if (var5 != var7) {
            var0.put(var5, var7);
         }
      }
   }

   private void М(Map<Long, TrapIndicator.TrapAccumulator> var1, BlockPos var2, int var3) {
      if (this.Х_at_4ddd664d.isSelected(ХЛП) && MINECRAFT_CLIENT.level != null && MINECRAFT_CLIENT.player != null) {
         int var4 = Math.max(MINECRAFT_CLIENT.level.getMinY(), var2.getY() - var3);
         int var5 = Math.min(MINECRAFT_CLIENT.level.getMaxY(), var2.getY() + var3);
         AABB var6;
         if (this.Ю.isSelected(ХЛ西)) {
            int var7 = var2.getX() >> 4 << 4;
            int var8 = var2.getZ() >> 4 << 4;
            var6 = new AABB(var7, var4, var8, var7 + 16, var5 + 1, var8 + 16);
         } else {
            var6 = MINECRAFT_CLIENT.player.getBoundingBox().inflate(var3);
         }

         boolean var13 = this.о.getValue();
         double var14 = (double)var3 * var3;

         for (ArmorStand var11 : MINECRAFT_CLIENT.level.getEntitiesOfClass(ArmorStand.class, var6, var0 -> true)) {
            if (var11.isAlive()
               && (!var13 || var11.getCustomName() == null)
               && (
                  this.Ю.isSelected(ХЛд)
                     ? var11.getBlockX() >> 4 == var2.getX() >> 4 && var11.getBlockZ() >> 4 == var2.getZ() >> 4
                     : !(var11.distanceToSqr(MINECRAFT_CLIENT.player) > var14)
               )) {
               Vec3 var12 = var11.position();
               var1.computeIfAbsent(М(var11.getBlockX(), var11.getBlockZ()), var0 -> new TrapIndicator.TrapAccumulator())
                  .Э(var12.x, var12.y, var12.z, TrapIndicator.BlockKind.STAND);
            }
         }
      }
   }

   private int п() {
      return this.Ю.isSelected(ХЛБ) ? 16 : Math.round(this.field_6.getValue());
   }

   private void Мю() {
      if (this.В != null && MINECRAFT_CLIENT.level != null) {
         boolean var1 = this.Х_at_4ddd664d.isSelected(ХЛЙ);
         boolean var2 = this.Х_at_4ddd664d.isSelected(ХЛк);
         boolean var3 = this.Х_at_4ddd664d.isSelected(ХЛг);
         int var4 = 0;

         while (var4 < ХЛ0 && this.В.ЯI < this.В.м_2571477d.size()) {
            ChunkPos var5 = this.В.м_2571477d.get(this.В.ЯI);
            if (!MINECRAFT_CLIENT.level.hasChunk(var5.x(), var5.z())) {
               this.В.ЯI++;
               this.В.ЯТ = 0;
               this.В.ЯО = 0;
            } else {
               LevelChunk var6 = (LevelChunk)MINECRAFT_CLIENT.level.getChunk(var5.x(), var5.z(), ChunkStatus.FULL, false);
               if (var6 == null) {
                  this.В.ЯI++;
                  this.В.ЯТ = 0;
                  this.В.ЯО = 0;
               } else {
                  LevelChunkSection[] var7 = var6.getSections();
                  if (this.В.ЯТ >= var7.length) {
                     this.В.ЯI++;
                     this.В.ЯТ = 0;
                     this.В.ЯО = 0;
                  } else {
                     LevelChunkSection var8 = var7[this.В.ЯТ];
                     int var9 = MINECRAFT_CLIENT.level.getSectionYFromSectionIndex(this.В.ЯТ) << 4;
                     if (var8 != null && !var8.hasOnlyAir() && var9 <= this.В.Ям && var9 + 15 >= this.В.Яы) {
                        int var10 = var5.x() << 4;
                        int var11 = var5.z() << 4;

                        while (var4 < ХЛх && this.В.ЯО < 4096) {
                           int var12 = this.В.ЯО++;
                           var4++;
                           int var13 = var12 & 15;
                           int var14 = var12 >> 4 & 15;
                           int var15 = var12 >> 8;
                           int var16 = var10 + var13;
                           int var17 = var9 + var15;
                           int var18 = var11 + var14;
                           if (var16 >= this.В.ЯШ && var16 <= this.В.ЯЖ && var17 >= this.В.Яы && var17 <= this.В.Ям && var18 >= this.В.Яй && var18 <= this.В.ЯК
                              )
                            {
                              BlockState var19 = var8.getBlockState(var13, var15, var14);
                              if (!var19.isAir()) {
                                 TrapIndicator.BlockKind var20 = М(var19.getBlock(), var1, var2, var3);
                                 if (var20 != null) {
                                    this.В
                                       .м_ea411899
                                       .computeIfAbsent(М(var16, var18), var0 -> new TrapIndicator.TrapAccumulator())
                                       .Э(var16 + ХЛУ, var17 + ХЛЗ, var18 + ХЛЕ, var20);
                                 }
                              }
                           }
                        }

                        if (this.В.ЯО >= 4096) {
                           this.В.ЯТ++;
                           this.В.ЯО = 0;
                        }
                     } else {
                        this.В.ЯТ++;
                        this.В.ЯО = 0;
                     }
                  }
               }
            }
         }

         if (this.В.ЯI >= this.В.м_2571477d.size()) {
            this.И.clear();
            this.И.putAll(this.В.м_ea411899);
            this.В = null;
            this.сТ = 0;
         }
      }
   }

   private void М(BlockPos var1, int var2) {
      if (MINECRAFT_CLIENT.level != null) {
         int var3;
         int var4;
         int var5;
         int var6;
         if (this.Ю.isSelected(ХЛФ)) {
            var3 = var1.getX() >> 4 << 4;
            var5 = var1.getZ() >> 4 << 4;
            var4 = var3 + 15;
            var6 = var5 + 15;
         } else {
            var3 = var1.getX() - var2;
            var4 = var1.getX() + var2;
            var5 = var1.getZ() - var2;
            var6 = var1.getZ() + var2;
         }

         int var7 = Math.max(MINECRAFT_CLIENT.level.getMinY(), var1.getY() - var2);
         int var8 = Math.min(MINECRAFT_CLIENT.level.getMaxY(), var1.getY() + var2);
         ArrayList var9 = new ArrayList();

         for (int var10 = var3 >> 4; var10 <= var4 >> 4; var10++) {
            for (int var11 = var5 >> 4; var11 <= var6 >> 4; var11++) {
               var9.add(new ChunkPos(var10, var11));
            }
         }

         this.В = new TrapIndicator.ScanJob(var9, var3, var7, var5, var4, var8, var6);
         this.сТ = 0;
      }
   }

   private void М(TrapIndicator.TrapLocation var1, Vec3 var2, float var3) {
      double var4 = var1.pos.x - var2.x;
      double var6 = var1.pos.z - var2.z;
      int var8 = (int)Math.round(Math.sqrt(var4 * var4 + var6 * var6));
      float var9 = Mth.wrapDegrees((float)Math.toDegrees(Math.atan2(var6, var4)) - var3 - ХЛ7);
      float var10 = this.Ш.getValue();
      float var11 = MINECRAFT_CLIENT.getWindow().getScreenWidth() * ХЛР;
      float var12 = MINECRAFT_CLIENT.getWindow().getScreenHeight() * ХЛ3;
      float var13 = var11 + ХЛр * var10 * (float)Math.sin(Math.toRadians(var9));
      float var14 = var12 - ХЛ6 * var10 * (float)Math.cos(Math.toRadians(var9));
      float var15 = ХЛ_ * var10;
      int var16 = ThemeEditor.getColorArgb(ThemeColorSlot.MAIN);
      Matrix4f var17 = UiDrawing.getTransform();
      float var18 = ХЛШ + var13 * 2.0F / MINECRAFT_CLIENT.getWindow().getScreenWidth();
      float var19 = 1.0F - var14 * 2.0F / MINECRAFT_CLIENT.getWindow().getScreenHeight();
      float var20 = (float)MINECRAFT_CLIENT.getWindow().getScreenWidth() / MINECRAFT_CLIENT.getWindow().getScreenHeight();
      var17.translate(var18, var19, 0.0F)
         .scale(1.0F, var20, 1.0F)
         .rotateZ((float)(-Math.toRadians(var9)))
         .scale(1.0F, 1.0F / var20, 1.0F)
         .translate(-var18, -var19, 0.0F);
      UiDrawing.drawTexture(var13 - var15 * ХЛы, var14 - var15 * ХЛй, var15 + 1.0F, var15 + 1.0F, new Vector4f(0.0F), new Vector4i(var16), Л);
      UiDrawing.setTransform(new Matrix4f());
      int var21 = Math.max(1, Math.round(ХЛЖ * var10));
      String var22 = ХЛм;
      String var23 = var8 + "m";
      float var24 = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var22, var21);
      float var25 = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var23, var21);
      UiDrawing.drawText(ClientFonts.getMediumFont(), var22, var13 - var24 * ХЛК, var14 - ХЛI * var10, var21, -1);
      UiDrawing.drawText(ClientFonts.getMediumFont(), var23, var13 - var25 * ХЛТ, var14 + ХЛО * var10, var21, -1);
   }

   private void М(Vector2f var1, TrapIndicator.TrapLocation var2, Vec3 var3) {
      String var4 = ХЛн;
      String var5 = ХЛЦ;
      String var6 = Math.round(var3.distanceTo(var2.pos)) + "m";
      float var7 = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var4, 10);
      float var8 = UiDrawing.getTextWidth(ClientFonts.getMediumFont(), var5, 13);
      float var9 = UiDrawing.getTextWidth(ClientFonts.getRegularFont(), var6, 11);
      float var10 = ХЛч + var7 + ХЛЭ + var8 + ХЛ必 + var9 + ХЛщ;
      float var11 = ХЛГ;
      float var12 = var1.x - var10 * ХЛл;
      float var13 = var1.y - var11 * ХЛэ;
      UiDrawing.drawWindowBackground(var12, var13, var10, var11, ХЛю);
      float var14 = var12 + ХЛЩ;
      int var15 = ThemeEditor.getColorArgb(ThemeColorSlot.MAIN);
      UiDrawing.drawText(ClientFonts.getMediumFont(), var4, var14, var13 + ХЛу, 11, var15);
      float var16 = var14 + var7 + ХЛ2;
      UiDrawing.drawText(ClientFonts.getMediumFont(), var5, var16, var13 + ХЛф, 13, ColorMath.packArgb(235, 239, 247, ХЛи));
      UiDrawing.drawText(ClientFonts.getRegularFont(), var6, var16 + var8 + ХЛ9, var13 + ХЛо, 11, ColorMath.withAlpha(var15, ХЛO));
   }

   private static boolean М(Block var0) {
      return var0 instanceof PistonBaseBlock || var0 instanceof PistonHeadBlock || var0 == Blocks.MOVING_PISTON;
   }

   private void Мя() {
      this.И.clear();
      this.field_8.clear();
      this.В = null;
      this.А_at_4ddd664d = null;
      this.см = ХЛз;
      this.сК = ХЛц;
      this.сI = -1;
      this.сТ = 0;
   }

   private void method_0(BlockPos var1, int var2) {
      this.field_8.clear();
      HashMap<Long, TrapIndicator.TrapAccumulator> var3 = new HashMap<>(this.И.size() + 8);

      for (Entry var5 : this.И.entrySet()) {
         var3.put((Long)var5.getKey(), ((TrapIndicator.TrapAccumulator)var5.getValue()).Э_21b48286());
      }

      this.М(var3, var1, var2);
      int var13 = Math.round(this.field__.getValue());
      ArrayList<Long> var14 = new ArrayList<>();

      for (Entry var7 : var3.entrySet()) {
         if (((TrapIndicator.TrapAccumulator)var7.getValue()).О(var13)) {
            var14.add((Long)var7.getKey());
         }
      }

      if (!var14.isEmpty()) {
         HashMap<Long, Long> var15 = new HashMap<>();

         for (long var8 : var14) {
            var15.put(var8, var8);
         }

         for (long var19 : var14) {
            int var10 = (int)(var19 >> 32);
            int var11 = (int)var19;
            М(var15, var19, М(var10 + 1 << 3, var11 << 3));
            М(var15, var19, М(var10 - 1 << 3, var11 << 3));
            М(var15, var19, М(var10 << 3, var11 + 1 << 3));
            М(var15, var19, М(var10 << 3, var11 - 1 << 3));
         }

         HashMap<Long, TrapIndicator.TrapAccumulator> var18 = new HashMap<>();

         for (long var9 : var14) {
            long var25 = М(var15, var9);
            var18.computeIfAbsent(var25, var0 -> new TrapIndicator.TrapAccumulator()).Э((TrapIndicator.TrapAccumulator)var3.get(var9));
         }

         Vec3 var21 = MINECRAFT_CLIENT.player.position();
         ArrayList<TrapIndicator.TrapLocation> var22 = new ArrayList<>(var18.size());

         for (TrapIndicator.TrapAccumulator var26 : var18.values()) {
            var22.add(var26.Э_f85c13cc());
         }

         var22.sort(Comparator.comparingDouble(var1x -> var1x.pos.distanceToSqr(var21)));
         int var24 = Math.min(4, var22.size());

         for (int var27 = 0; var27 < var24; var27++) {
            this.field_8.add((TrapIndicator.TrapLocation)var22.get(var27));
         }
      }
   }

   private static long М(int var0, int var1) {
      return (long)(var0 >> 3) << 32 | var1 >> 3 & ХЛС;
   }

   @Override
   public void onDisable() {
      this.Мя();
   }

   private void р(HudRenderEvent var1) {
      if (var1.getGraphics() != null && MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && !this.field_8.isEmpty()) {
         float var2 = MINECRAFT_CLIENT.gameRenderer.mainCamera().yRot();
         float var3 = MINECRAFT_CLIENT.getDeltaTracker().getGameTimeDeltaPartialTick(true);
         Vec3 var4 = new Vec3(
            Mth.lerp(var3, MINECRAFT_CLIENT.player.xOld, MINECRAFT_CLIENT.player.getX()), Mth.lerp(var3, MINECRAFT_CLIENT.player.yOld, MINECRAFT_CLIENT.player.getY()), Mth.lerp(var3, MINECRAFT_CLIENT.player.zOld, MINECRAFT_CLIENT.player.getZ())
         );
         UiDrawing.setTransform(new Matrix4f());

         for (TrapIndicator.TrapLocation var6 : this.field_8) {
            if (this.field_9.getValue()) {
               this.М(var6, var4, var2);
            }

            Vector2f var7 = WorldProjection.projectToScreen(var6.pos);
            if (var7 != null && this.и.getValue()) {
               this.М(var7, var6, var4);
            }
         }
      }
   }

   public TrapIndicator() {
      super(ХЛп, ХЛД, ModuleCategory.RENDER);
      this.Ю = new SingleChoiceSetting(ХЛЪ, new ChoiceOption(ХЛт), new ChoiceOption(ХЛа));
      this.field_6 = new FloatSetting(ХЛЮ, ХЛЧ, ХЛЛ, ХЛ诶).withStep(1.0F).visibleWhen(() -> this.Ю.isSelected(ХЛя));
      this.field__ = new FloatSetting(ХЛ1, ХЛЬ, ХЛ衣, ХЛш).withStep(1.0F);
      this.Х_at_4ddd664d = new MultiChoiceSetting(
         ХЛИ, new ChoiceOption(ХЛь, true), new ChoiceOption(ХЛж, true), new ChoiceOption(ХЛ弟, true), new ChoiceOption(ХЛб, true)
      );
      this.и = new BooleanSetting(ХЛЫ, true);
      this.field_9 = new BooleanSetting(ХЛl, true);
      this.Ш = new FloatSetting(ХЛ8, 2.0F, 1.0F, ХЛ5).withStep(ХЛв).visibleWhen(this.field_9::getValue);
      this.о = new BooleanSetting(ХЛъ, true);
      this.И = new HashMap<>();
      this.field_8 = new ArrayList<>();
      this.Ж = this::д;
      this.м = this::р;
      this.см = ХЛМ;
      this.сК = ХЛН;
      this.сI = -1;
      this.addSettings(this.Ю, this.field_6, this.field__, this.Х_at_4ddd664d, this.и, this.field_9, this.Ш, this.о);
   }

   private static long М(Map<Long, Long> var0, long var1) {
      long var3 = var0.getOrDefault(var1, var1);
      if (var3 != var1) {
         var3 = М(var0, var3);
         var0.put(var1, var3);
      }

      return var3;
   }

   private static TrapIndicator.BlockKind М(Block var0, boolean var1, boolean var2, boolean var3) {
      if (var1 && М(var0)) {
         return TrapIndicator.BlockKind.PISTON;
      } else if (var2 && var0 == Blocks.CRAFTING_TABLE) {
         return TrapIndicator.BlockKind.TABLE;
      } else {
         return var3 && var0 instanceof FenceGateBlock ? TrapIndicator.BlockKind.GATE : null;
      }
   }

   private void д(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (this.А_at_4ddd664d != MINECRAFT_CLIENT.level) {
            this.Мя();
            this.А_at_4ddd664d = MINECRAFT_CLIENT.level;
         }

         int var2 = this.п();
         BlockPos var3 = MINECRAFT_CLIENT.player.blockPosition();
         int var4 = var3.getX() >> 4;
         int var5 = var3.getZ() >> 4;
         boolean var6 = var4 != this.см || var5 != this.сК || var2 != this.сI;
         if (var6) {
            this.см = var4;
            this.сК = var5;
            this.сI = var2;
            this.М(var3, var2);
         }

         this.Мю();
         if (this.В == null && ++this.сТ >= 40) {
            this.М(var3, var2);
         }

         this.method_0(var3, var2);
      } else {
         this.Мя();
      }
   }

   @Override
   public void onEnable() {
      this.Мя();
   }

   private enum BlockKind {
      STAND,
      PISTON,
      TABLE,
      GATE;
   }

   private record TrapLocation(Vec3 pos, int score) {
   }

   private static final class ScanJob {
      @SerializedName("м")
      private final List<ChunkPos> м_2571477d;
      @SerializedName("м")
      private final Map<Long, TrapIndicator.TrapAccumulator> м_ea411899 = new HashMap<>();
      private final int ЯШ;
      private final int Яы;
      private final int Яй;
      private final int ЯЖ;
      private final int Ям;
      private final int ЯК;
      private int ЯI;
      private int ЯТ;
      private int ЯО;

      private ScanJob(List<ChunkPos> var1, int var2, int var3, int var4, int var5, int var6, int var7) {
         this.м_2571477d = var1;
         this.ЯШ = var2;
         this.Яы = var3;
         this.Яй = var4;
         this.ЯЖ = var5;
         this.Ям = var6;
         this.ЯК = var7;
      }
   }

   private static final class TrapAccumulator {
      private int А3;
      private int Ар;
      private int А6;
      private int А_;
      private double ВЩ;
      private double Ву;
      private double В2;
      private int АШ;

      private void Э(double var1, double var3, double var5, TrapIndicator.BlockKind var7) {
         switch (var7) {
            case STAND:
               this.А3++;
               break;
            case PISTON:
               this.Ар++;
               break;
            case TABLE:
               this.А6++;
               break;
            case GATE:
               this.А_++;
         }

         this.ВЩ += var1;
         this.Ву += var3;
         this.В2 += var5;
         this.АШ++;
      }

      private int ги() {
         return this.А3 * 3 + this.Ар * 2 + this.А6 * 2 + this.А_;
      }

      private boolean О(int var1) {
         return this.АШ > 0 && this.ги() >= var1 ? this.Ар > 0 || this.А6 > 0 || this.А3 >= 4 : false;
      }

      private void Э(TrapIndicator.TrapAccumulator var1) {
         if (var1 != null) {
            this.А3 = this.А3 + var1.А3;
            this.Ар = this.Ар + var1.Ар;
            this.А6 = this.А6 + var1.А6;
            this.А_ = this.А_ + var1.А_;
            this.ВЩ = this.ВЩ + var1.ВЩ;
            this.Ву = this.Ву + var1.Ву;
            this.В2 = this.В2 + var1.В2;
            this.АШ = this.АШ + var1.АШ;
         }
      }

      private TrapIndicator.TrapLocation Э_f85c13cc() {
         double var1 = this.АШ == 0 ? 1.0 : 1.0 / this.АШ;
         return new TrapIndicator.TrapLocation(new Vec3(this.ВЩ * var1, this.Ву * var1, this.В2 * var1), this.ги());
      }

      private TrapIndicator.TrapAccumulator Э_21b48286() {
         TrapIndicator.TrapAccumulator var1 = new TrapIndicator.TrapAccumulator();
         var1.Э(this);
         return var1;
      }
   }
}
