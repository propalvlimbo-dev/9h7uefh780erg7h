package sg.modules.impl.render;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import settings.render.ClientResourceIds;
import sg.events.impl.PlayerTickEvent;
import sg.events.EventListener;
import sg.events.impl.WorldEffectsEvent;
import sg.render.pipeline.CatPetRenderer;
import sg.render.pipeline.CatPetModel;
import sg.render.pipeline.RenderPipelineBuilder;
import sg.animation.Easings;
import sg.animation.FloatAnimation;
import sg.client.RoseClient;
import sg.modules.ClientModule;
import sg.modules.ModuleCategory;
import sg.modules.ModuleRegistry;
import sg.modules.impl.combat.Aura;
import sg.modules.impl.combat.Aimbot;
import sg.events.impl.AttackEntityEvent;
import sg.modules.impl.init.CatPetAnimator;
import sg.modules.impl.init.CatPetMovement;
import sg.settings.ChoiceOption;
import sg.settings.FloatSetting;
import sg.settings.SingleChoiceSetting;
import sg.modules.impl.init.SoundEffectPreset;

public final class CatPetModule extends ClientModule {
   private static final float п2 = 0.0625F;
   private final SingleChoiceSetting С;
   private final SingleChoiceSetting з;
   private final FloatSetting Я3;
   private final FloatSetting Яр;
   private final FloatSetting Я6;
   private final FloatSetting Я_;
   private final FloatSetting ЯШ;
   private final RenderPipeline сФ;
   private final RenderPipeline сЙ;
   @SerializedName("В")
   private final CatPetMovement В_c573d20e;
   @SerializedName("В")
   private final CatPetAnimator В_ff98ba8f;
   @SerializedName("В")
   private final CatPetModel В_2c146a0f;
   @SerializedName("В")
   private final CatPetRenderer В_107c6675;
   private final FloatAnimation с4;
   private final EventListener<PlayerTickEvent> Яв;
   private final EventListener<AttackEntityEvent> Яъ;
   private final EventListener<WorldEffectsEvent> ЯМ;
   @SerializedName("4Ж")
   private int field_4Ж;
   private static final String ЯЦЩ = "CatPet";
   private static final String ЯЦу = "Кот-помощник Мачкин";
   private static final String ЯЦ2 = "Окрас";
   private static final String ЯЦф = "Machkin";
   private static final String ЯЦи = "Jellie";
   private static final String ЯЦ9 = "Tabby";
   private static final String ЯЦо = "Black";
   private static final String ЯЦO = "Red";
   private static final String ЯЦ7 = "Siamese";
   private static final String ЯЦР = "British";
   private static final String ЯЦ3 = "Calico";
   private static final String ЯЦр = "Persian";
   private static final String ЯЦ6 = "Ragdoll";
   private static final String ЯЦ_ = "White";
   private static final String ЯЦШ = "All Black";
   private static final String ЯЦы = "Голос";
   private static final String ЯЦй = "Скорость";
   private static final float ЯЦЖ = 0.15F;
   private static final float ЯЦм = 0.05F;
   private static final float ЯЦК = 0.3F;
   private static final float ЯЦI = 0.01F;
   private static final String ЯЦТ = "Размер";
   private static final float ЯЦО = 0.95F;
   private static final float ЯЦФ = 0.3F;
   private static final float ЯЦЙ = 1.5F;
   private static final float ЯЦк = 0.05F;
   private static final String ЯЦг = "Дистанция";
   private static final float ЯЦ0 = 1.5F;
   private static final float ЯЦх = 0.5F;
   private static final float ЯЦУ = 4.0F;
   private static final float ЯЦЗ = 0.25F;
   private static final String ЯЦЕ = "Ударов для мяу";
   private static final float ЯЦП = 5.0F;
   private static final float ЯЦ西 = 20.0F;
   private static final String ЯЦд = "Громкость";
   private static final float ЯЦБ = 0.05F;
   private static final String ЯЦС = "rose/cat_pet_skin";
   private static final String ЯЦз = "cat_pet";
   private static final String ЯЦц = "cat_pet_skin";
   private static final String ЯЦя = "Sampler0";
   private static final String Ячс = "rose/cat_pet_shadow";
   private static final String ЯчВ = "cat_pet";
   private static final String ЯчЯ = "cat_pet_shadow";
   private static final float ЯчХ = 2.6F;
   private static final float Яче = 0.02F;
   private static final float ЯчА = 0.0625F;
   private static final float Яч4 = 0.86F;
   private static final float Ячп = 0.14F;
   private static final float ЯчД = 20.0F;
   private static final String ЯчЪ = "Jellie";
   private static final String Ячт = "Tabby";
   private static final String Яча = "Black";
   private static final String ЯчЮ = "Red";
   private static final String ЯчЧ = "Siamese";
   private static final String ЯчЛ = "British";
   private static final String Яч诶 = "Calico";
   private static final String Яч1 = "Persian";
   private static final String ЯчЬ = "Ragdoll";
   private static final String Яч衣 = "White";
   private static final String Ячш = "All Black";
   private static final String ЯчИ = "minecraft";
   private static final String Ячь = "textures/entity/cat/cat_jellie.png";
   private static final String Ячж = "minecraft";
   private static final String Яч弟 = "textures/entity/cat/cat_tabby.png";
   private static final String Ячб = "minecraft";
   private static final String ЯчЫ = "textures/entity/cat/cat_black.png";
   private static final String Ячl = "minecraft";
   private static final String Яч8 = "textures/entity/cat/cat_red.png";
   private static final String Яч5 = "minecraft";
   private static final String Ячв = "textures/entity/cat/cat_siamese.png";
   private static final String Ячъ = "minecraft";
   private static final String ЯчМ = "textures/entity/cat/cat_british_shorthair.png";
   private static final String ЯчН = "minecraft";
   private static final String Ячн = "textures/entity/cat/cat_calico.png";
   private static final String ЯчЦ = "minecraft";
   private static final String Ячч = "textures/entity/cat/cat_persian.png";
   private static final String ЯчЭ = "minecraft";
   private static final String Яч必 = "textures/entity/cat/cat_ragdoll.png";
   private static final String Ячщ = "minecraft";
   private static final String ЯчГ = "textures/entity/cat/cat_white.png";
   private static final String Ячл = "minecraft";
   private static final String Ячэ = "textures/entity/cat/cat_all_black.png";
   private static final String Ячю = "rose";
   private static final String ЯчЩ = "textures/entity/cat/machkin.png";
   private static final double Ячу = 0.5;
   private static final float Яч2 = 0.55F;
   private static final float Ячф = 0.45F;
   private static final float Ячи = 15.0F;

   private Identifier Р_c635a640() {
      return switch (this.С.getSelectedOption().getName()) {
         case ЯчЪ -> Identifier.fromNamespaceAndPath(ЯчИ, Ячь);
         case Ячт -> Identifier.fromNamespaceAndPath(Ячж, Яч弟);
         case Яча -> Identifier.fromNamespaceAndPath(Ячб, ЯчЫ);
         case ЯчЮ -> Identifier.fromNamespaceAndPath(Ячl, Яч8);
         case ЯчЧ -> Identifier.fromNamespaceAndPath(Яч5, Ячв);
         case ЯчЛ -> Identifier.fromNamespaceAndPath(Ячъ, ЯчМ);
         case Яч诶 -> Identifier.fromNamespaceAndPath(ЯчН, Ячн);
         case Яч1 -> Identifier.fromNamespaceAndPath(ЯчЦ, Ячч);
         case ЯчЬ -> Identifier.fromNamespaceAndPath(ЯчЭ, Яч必);
         case Яч衣 -> Identifier.fromNamespaceAndPath(Ячщ, ЯчГ);
         case Ячш -> Identifier.fromNamespaceAndPath(Ячл, Ячэ);
         default -> Identifier.fromNamespaceAndPath(Ячю, ЯчЩ);
      };
   }

   private void Д(AttackEntityEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         if (var1.getPhase() == AttackEntityEvent.Phase.POST && var1.getAttacker() == MINECRAFT_CLIENT.player) {
            if (++this.field_4Ж >= Math.round(this.Я_.getValue())) {
               this.field_4Ж = 0;
               this.В_ff98ba8f.Ж5();
               SoundEffectPreset.ъ(this.з.getSelectedOption().getName()).К(this.ЯШ.getValue());
            }
         }
      }
   }

   public CatPetModule() {
      super(ЯЦЩ, ЯЦу, ModuleCategory.RENDER);
      this.С = new SingleChoiceSetting(
         ЯЦ2,
         new ChoiceOption(ЯЦф),
         new ChoiceOption(ЯЦи),
         new ChoiceOption(ЯЦ9),
         new ChoiceOption(ЯЦо),
         new ChoiceOption(ЯЦO),
         new ChoiceOption(ЯЦ7),
         new ChoiceOption(ЯЦР),
         new ChoiceOption(ЯЦ3),
         new ChoiceOption(ЯЦр),
         new ChoiceOption(ЯЦ6),
         new ChoiceOption(ЯЦ_),
         new ChoiceOption(ЯЦШ)
      );
      this.з = new SingleChoiceSetting(ЯЦы, SoundEffectPreset.ъ());
      this.Я3 = new FloatSetting(ЯЦй, ЯЦЖ, ЯЦм, ЯЦК).withStep(ЯЦI);
      this.Яр = new FloatSetting(ЯЦТ, ЯЦО, ЯЦФ, ЯЦЙ).withStep(ЯЦк);
      this.Я6 = new FloatSetting(ЯЦг, ЯЦ0, ЯЦх, ЯЦУ).withStep(ЯЦЗ);
      this.Я_ = new FloatSetting(ЯЦЕ, ЯЦП, 1.0F, ЯЦ西).withStep(1.0F);
      this.ЯШ = new FloatSetting(ЯЦд, 1.0F, 0.0F, 2.0F).withStep(ЯЦБ);
      this.сФ = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(ЯЦС)
         .withVertexShader(ClientResourceIds.getCoreId(ЯЦз))
         .withFragmentShader(ClientResourceIds.getCoreId(ЯЦц))
         .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
         .withDepthWrite(true)
         .withCull(false)
         .withBlend(BlendFunction.TRANSLUCENT)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .withSampler(ЯЦя)
         .build();
      this.сЙ = RenderPipelineBuilder.builder(RenderPipelineBuilder.PROJECTION_SNIPPET)
         .withLocation(Ячс)
         .withVertexShader(ClientResourceIds.getCoreId(ЯчВ))
         .withFragmentShader(ClientResourceIds.getCoreId(ЯчЯ))
         .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL)
         .withDepthWrite(false)
         .withCull(false)
         .withBlend(BlendFunction.TRANSLUCENT)
         .withVertexFormat(DefaultVertexFormat.POSITION_TEX_COLOR, PrimitiveTopology.QUADS)
         .build();
      this.В_c573d20e = new CatPetMovement();
      this.В_ff98ba8f = new CatPetAnimator();
      this.В_2c146a0f = new CatPetModel();
      this.В_107c6675 = new CatPetRenderer();
      this.с4 = new FloatAnimation(0.0F, ЯчХ, Easings.CUBIC_OUT);
      this.Яв = this::ТП;
      this.Яъ = this::Д;
      this.ЯМ = this::о;
      this.addSettings(this.С, this.з, this.Я3, this.Яр, this.Я6, this.Я_, this.ЯШ);
   }

   private boolean method_8(LivingEntity var1) {
      return var1 != null && var1 != MINECRAFT_CLIENT.player && var1.isAlive() && !var1.isRemoved();
   }

   private LivingEntity Р_95bedd6e() {
      if (RoseClient.getInstance() != null && RoseClient.getInstance().getServices() != null) {
         ModuleRegistry var1 = RoseClient.getInstance().getServices().getModules();
         if (var1 == null) {
            return null;
         }

         Aimbot var2 = var1.getAimbot();
         Aura var3 = var1.getAura();
         return var3 != null && var3.isEnabled() && this.method_8(var3.Ж_95bedd6e()) ? var3.Ж_95bedd6e() : null;
      } else {
         return null;
      }
   }

   private void о(WorldEffectsEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null && this.В_c573d20e.lЯ()) {
         this.с4.updateTarget(1.0F);
         float var2 = this.с4.getValue();
         if (!(var2 <= Яче)) {
            float var3 = var1.getDeltaTracker().getGameTimeDeltaPartialTick(true);
            CatPetModule.PetRenderContext var4 = new CatPetModule.PetRenderContext(
               var1.getPoses().last().pose(),
               MINECRAFT_CLIENT.gameRenderer.mainCamera().position(),
               var3,
               ЯчА * this.Яр.getValue() * (Яч4 + Ячп * var2),
               var2,
               ((float)MINECRAFT_CLIENT.level.getGameTime() + var3) / ЯчД,
               this.ТЩ(var3)
            );
            this.西(CatPetRenderer.CoatVariant.SHADOW, this.сЙ, var4, null);
            this.西(CatPetRenderer.CoatVariant.SKIN, this.сФ, var4, this.ч_595eceb3());
         }
      }
   }

   private AbstractTexture ч_595eceb3() {
      return MINECRAFT_CLIENT.getTextureManager().getTexture(this.Р_c635a640());
   }

   private void 西(CatPetRenderer.CoatVariant var1, RenderPipeline var2, CatPetModule.PetRenderContext var3, AbstractTexture var4) {
      this.В_107c6675.д(var1, var3.matrix(), var3.show(), var3.luminance());

      try {
         this.В_107c6675
            .Х_de5a6f78(
               (float)(this.В_c573d20e.ф(var3.delta()) - var3.camera().x),
               (float)(this.В_c573d20e.в(var3.delta()) - var3.camera().y),
               (float)(this.В_c573d20e.т(var3.delta()) - var3.camera().z)
            );
         this.В_107c6675.ыь(this.В_c573d20e.Т(var3.delta()));
         this.В_107c6675.ы6(var3.scale());
         this.В_2c146a0f.й(this.В_107c6675, this.В_ff98ba8f, var3.delta(), var3.time());
      } finally {
         this.В_107c6675.д(var2, var4);
      }
   }

   private float ТЩ(float var1) {
      BlockPos var2 = BlockPos.containing(this.В_c573d20e.ф(var1), this.В_c573d20e.в(var1) + Ячу, this.В_c573d20e.т(var1));
      int var3 = MINECRAFT_CLIENT.level.getBrightness(LightLayer.BLOCK, var2);
      int var4 = MINECRAFT_CLIENT.level.getBrightness(LightLayer.SKY, var2);
      return Яч2 + Ячф * (Math.max(var3, var4) / Ячи);
   }

   @Override
   public void onEnable() {
      this.field_4Ж = 0;
      this.В_c573d20e.method_3衣();
      this.В_ff98ba8f.Ж必();
      this.с4.setImmediately(0.0F);
   }

   @Override
   public void onDisable() {
      this.В_c573d20e.method_3衣();
   }

   private void ТП(PlayerTickEvent var1) {
      if (MINECRAFT_CLIENT.player != null && MINECRAFT_CLIENT.level != null) {
         this.В_ff98ba8f.ЖГ();
         double var2 = this.В_c573d20e.l(this.Р_95bedd6e(), this.Я3.getValue(), this.Я6.getValue());
         this.В_ff98ba8f.с(var2, this.Я3.getValue());
      }
   }

   private record PetRenderContext(Matrix4f matrix, Vec3 camera, float delta, float scale, float show, float time, float luminance) {
   }
}
