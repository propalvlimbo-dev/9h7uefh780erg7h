package sg.render.effects;

import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.shaders.UniformType;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import java.util.ArrayList;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;
import settings.render.ClientResourceIds;
import sg.events.impl.WorldEffectsEvent;
import sg.modules.impl.render.TNTEffect;
import sg.render.pipeline.RenderPipelineBuilder;
import sg.render.pipeline.SharedGeometryBuffer;
import sg.settings.BooleanSetting;
import sg.settings.ChoiceOption;
import sg.settings.FloatSetting;
import sg.settings.SingleChoiceSetting;
import sg.theme.ThemeColorSlot;
import sg.util.time.ThemeEditor;

/** Port of the supplied EnergyUtils TNT module, using the base's GPU pipelines. */
public final class TntBlastEffect {
    private static final Minecraft MC = Minecraft.getInstance();
    private final TNTEffect owner;
    private final FloatSetting lifetime = new FloatSetting("Время жизни", 1800, 400, 6000).withStep(100);
    private final FloatSetting radiusScale = new FloatSetting("Масштаб радиуса", 1, .3F, 4).withStep(.1F);
    private final BooleanSetting smoke = new BooleanSetting("Дым", true);
    private final FloatSetting smokeAmount = new FloatSetting("Количество дыма", 60, 10, 200).withStep(5);
    private final BooleanSetting flash = new BooleanSetting("Вспышка", true);
    private final BooleanSetting wave = new BooleanSetting("Волна в мире", true);
    private final BooleanSetting screenWave = new BooleanSetting("Волна по экрану", true);
    private final FloatSetting wavePower = new FloatSetting("Сила волны", 26, 0, 60).withStep(1);
    private final BooleanSetting shake = new BooleanSetting("Тряска", true);
    private final FloatSetting shakePower = new FloatSetting("Сила тряски", .55F, 0, 2).withStep(.05F);
    private final SingleChoiceSetting colorMode = new SingleChoiceSetting("Цвет", options(
            "Тема", "Оранжевый", "Красный", "Синий", "Зелёный", "Белый", "Фиолетовый"));
    private final SingleChoiceSetting filter = new SingleChoiceSetting("Тип взрывов", options("Все", "Только TNT"));
    private final ArrayList<Blast> blasts = new ArrayList<>();
    private TextureTarget capture;
    private Vec3 shakeOffset = Vec3.ZERO;
    private Object level;
    private final RenderPipeline groundPipeline = worldPipeline("tnt_blast_ground", BlendFunction.TRANSLUCENT);
    private final RenderPipeline flashPipeline = worldPipeline("tnt_blast_flash", BlendFunction.ADDITIVE);
    private final RenderPipeline screenPipeline = RenderPipelineBuilder.builder()
            .withLocation("rose/tnt_blast_screen")
            .withVertexShader(ClientResourceIds.getCoreId("tnt_blast_screen"))
            .withFragmentShader(ClientResourceIds.getCoreId("tnt_blast_screen"))
            .withVertexFormat(RenderPipelineBuilder.EMPTY_VERTEX_FORMAT, PrimitiveTopology.TRIANGLES)
            .withDepthTest(CompareOp.ALWAYS_PASS).withDepthWrite(false).withCull(false).withoutBlend()
            .withUniform("BlastScreen", UniformType.UNIFORM_BUFFER).withSampler("SceneSampler").build();

    private static ChoiceOption[] options(String... names) {
        return java.util.Arrays.stream(names).map(ChoiceOption::new).toArray(ChoiceOption[]::new);
    }
    private static RenderPipeline worldPipeline(String name, BlendFunction blend) {
        return RenderPipelineBuilder.builder().withLocation("rose/" + name)
                .withVertexShader(ClientResourceIds.getCoreId("tnt_blast_world"))
                .withFragmentShader(ClientResourceIds.getCoreId("tnt_blast_world"))
                .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, PrimitiveTopology.QUADS)
                .withUniform("BlastWorld", UniformType.UNIFORM_BUFFER)
                .withDepthTest(CompareOp.GREATER_THAN_OR_EQUAL).withDepthWrite(false).withCull(false)
                .withBlend(blend).build();
    }
    public TntBlastEffect(TNTEffect owner) {
        this.owner = owner;
        owner.addSettings(lifetime.visibleWhen(owner::usesImportedEffect), radiusScale.visibleWhen(owner::usesImportedEffect),
                smoke.visibleWhen(owner::usesImportedEffect), smokeAmount.visibleWhen(() -> owner.usesImportedEffect() && smoke.getValue()),
                flash.visibleWhen(owner::usesImportedEffect), wave.visibleWhen(owner::usesImportedEffect),
                screenWave.visibleWhen(owner::usesImportedEffect), wavePower.visibleWhen(() -> owner.usesImportedEffect() && screenWave.getValue()),
                shake.visibleWhen(owner::usesImportedEffect), shakePower.visibleWhen(() -> owner.usesImportedEffect() && shake.getValue()),
                colorMode.visibleWhen(owner::usesImportedEffect), filter.visibleWhen(owner::usesImportedEffect));
    }
    public void spawn(Vec3 position, float power, boolean tnt) {
        if (!owner.isEnabled() || !owner.usesImportedEffect() || MC.level == null) return;
        if (filter.isSelected("Только TNT") && !tnt) return;
        if (level != MC.level) { clear(); level = MC.level; }
        long now = System.currentTimeMillis();
        for (var blast : blasts) if (now - blast.start < 150 && blast.position.distanceToSqr(position) < .25) return;
        var blast = new Blast(position, Math.max(1.8F, power * 2 * radiusScale.getValue()), now);
        blasts.add(blast);
        if (blasts.size() > 12) blasts.removeFirst();
        if (smoke.getValue()) spawnSmoke(blast);
    }
    private void spawnSmoke(Blast blast) {
        var random = ThreadLocalRandom.current();
        int count = smokeAmount.getValue().intValue();
        float radius = blast.radius * .7F;
        for (int i = 0; i < count; i++) {
            double x = random.nextDouble(-1, 1), y = random.nextDouble(-.3, 1), z = random.nextDouble(-1, 1);
            double len = Math.sqrt(x * x + y * y + z * z);
            if (len < .001) len = 1;
            x /= len; y /= len; z /= len;
            double speed = .35 + random.nextDouble() * .55;
            particle(random.nextFloat() < .45F ? ParticleTypes.LARGE_SMOKE : ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    blast.position.add(x * radius * .35, y * radius * .35, z * radius * .35), x * speed, y * speed + .12, z * speed);
        }
        for (int i = 0; i < Math.max(6, count / 4); i++) {
            double x = random.nextDouble(-1, 1), y = random.nextDouble(-.2, 1), z = random.nextDouble(-1, 1);
            double speed = .25 + random.nextDouble() * .4;
            particle(random.nextBoolean() ? ParticleTypes.FLAME : ParticleTypes.LAVA,
                    blast.position.add(x * radius * .3, y * radius * .3, z * radius * .3), x * speed, y * speed + .1, z * speed);
        }
    }
    private static void particle(ParticleOptions type, Vec3 pos, double vx, double vy, double vz) {
        MC.particleEngine.createParticle(type, pos.x, pos.y, pos.z, vx, vy, vz);
    }
    public void tick() {
        if (MC.level == null || MC.level != level || !owner.usesImportedEffect()) { clear(); level = MC.level; return; }
        long now = System.currentTimeMillis();
        blasts.removeIf(b -> now - b.start > lifetime.getValue());
        if (blasts.isEmpty() || !shake.getValue()) { shakeOffset = Vec3.ZERO; return; }
        float sum = 0;
        for (var blast : blasts) { float t = progress(blast, now); sum += (1 - t) * (1 - t); }
        float amp = Math.min(1, sum) * shakePower.getValue() * .3F;
        float phase = now / 24F;
        shakeOffset = new Vec3(Mth.sin(phase) * amp, Mth.cos(phase * 1.37F) * amp, Mth.sin(phase * .83F) * amp);
    }
    public Vec3 cameraOffset() { return owner.isEnabled() && owner.usesImportedEffect() && MC.level == level ? shakeOffset : Vec3.ZERO; }
    public void clear() {
        blasts.clear(); shakeOffset = Vec3.ZERO;
        if (capture != null) { capture.destroyBuffers(); capture = null; }
    }
    private float progress(Blast blast, long now) { return Mth.clamp((now - blast.start) / lifetime.getValue(), 0, 1); }
    public void render(WorldEffectsEvent event) {
        if (!owner.usesImportedEffect() || blasts.isEmpty() || MC.level != level) return;
        long now = System.currentTimeMillis();
        blasts.removeIf(b -> progress(b, now) >= 1);
        if (blasts.isEmpty()) return;
        if (wave.getValue()) {
            var buffer = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_COLOR);
            for (var blast : blasts) ground(buffer, event, blast, progress(blast, now), resolveColor());
            drawWorld(buffer, groundPipeline, event);
        }
        if (flash.getValue()) {
            var buffer = SharedGeometryBuffer.getInstance().begin(PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_COLOR);
            for (var blast : blasts) fireball(buffer, event, blast, progress(blast, now));
            drawWorld(buffer, flashPipeline, event);
        }
        if (screenWave.getValue()) renderScreen(event, now);
    }
    private void ground(BufferBuilder buffer, WorldEffectsEvent event, Blast blast, float t, int color) {
        float radius = t * blast.radius, alpha = (float) Math.pow(1 - t, 1.6) * .85F;
        if (alpha <= .01F || radius <= .05F) return;
        for (int band = 0; band < 2; band++) {
            float width = band == 0 ? .30F : .75F;
            float a = band == 0 ? alpha : alpha * .35F;
            float inner = Math.max(.01F, radius - width), outer = radius + width;
            for (int s = 0; s < 96; s++) {
                float a0 = s * (float) Math.PI * 2 / 96, a1 = (s + 1) * (float) Math.PI * 2 / 96;
                vertex(buffer, event, blast, Mth.cos(a0) * inner, .04F, Mth.sin(a0) * inner, color, a);
                vertex(buffer, event, blast, Mth.cos(a0) * outer, .04F, Mth.sin(a0) * outer, color, a);
                vertex(buffer, event, blast, Mth.cos(a1) * outer, .04F, Mth.sin(a1) * outer, color, a);
                vertex(buffer, event, blast, Mth.cos(a1) * inner, .04F, Mth.sin(a1) * inner, color, a);
            }
        }
    }
    private void fireball(BufferBuilder buffer, WorldEffectsEvent event, Blast blast, float t) {
        float local = Mth.clamp(t * 2.5F, 0, 1);
        if (local >= 1) return;
        float radius = (.4F + local * 1.3F) * blast.radius * .32F;
        float alpha = (1 - local) * (1 - local) * .7F;
        if (alpha <= .01F) return;
        for (int band = 0; band < 12; band++) {
            float phi0 = (band / 12F - .5F) * (float) Math.PI, phi1 = ((band + 1) / 12F - .5F) * (float) Math.PI;
            float y0 = Mth.sin(phi0) * radius, y1 = Mth.sin(phi1) * radius;
            float r0 = Mth.cos(phi0) * radius, r1 = Mth.cos(phi1) * radius;
            float mix = Math.abs(band / 11F - .5F) * 2;
            int color = 0xFFFF0000 | Math.round(Mth.lerp(mix, .95F, .55F) * 255) << 8 | Math.round(Mth.lerp(mix, .8F, .12F) * 255);
            for (int s = 0; s < 36; s++) {
                float a0 = s * (float) Math.PI * 2 / 36, a1 = (s + 1) * (float) Math.PI * 2 / 36;
                vertex(buffer, event, blast, Mth.cos(a0) * r0, y0, Mth.sin(a0) * r0, color, alpha);
                vertex(buffer, event, blast, Mth.cos(a0) * r1, y1, Mth.sin(a0) * r1, color, alpha);
                vertex(buffer, event, blast, Mth.cos(a1) * r1, y1, Mth.sin(a1) * r1, color, alpha);
                vertex(buffer, event, blast, Mth.cos(a1) * r0, y0, Mth.sin(a1) * r0, color, alpha);
            }
        }
    }
    private static void vertex(BufferBuilder buffer, WorldEffectsEvent event, Blast blast, float x, float y, float z, int color, float alpha) {
        Vec3 rel = blast.position.subtract(event.getCameraPosition());
        buffer.addVertex((float) rel.x + x, (float) rel.y + y, (float) rel.z + z)
                .setColor((Math.round(alpha * 255) << 24) | (color & 0xFFFFFF));
    }
    private static void drawWorld(BufferBuilder buffer, RenderPipeline pipeline, WorldEffectsEvent event) {
        var mesh = buffer.build();
        if (mesh == null) return;
        var target = MC.gameRenderer.mainRenderTarget();
        try (mesh; var stack = MemoryStack.stackPush()) {
            var uniforms = Std140Builder.onStack(stack, 128).putMat4f(event.getProjectionMatrix()).putMat4f(event.getViewMatrix()).get();
            var state = mesh.drawState();
            var indices = RenderSystem.getSequentialBuffer(state.primitiveTopology());
            var indexBuffer = indices.getBuffer(state.indexCount());
            try (var vertices = RenderSystem.getDevice().createBuffer(() -> "TNT blast vertices", 32, mesh.vertexBuffer());
                 var matrixBuffer = RenderSystem.getDevice().createBuffer(() -> "TNT blast matrices", 128, uniforms);
                 var pass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "TNT blast world",
                         target.getColorTextureView(), Optional.empty(), target.getDepthTextureView(), OptionalDouble.empty())) {
                pass.setPipeline(pipeline);
                pass.setUniform("BlastWorld", matrixBuffer);
                pass.setVertexBuffer(0, vertices.slice());
                pass.setIndexBuffer(indexBuffer, indices.type());
                pass.drawIndexed(state.indexCount(), 1, 0, 0, 0);
            }
        }
    }
    private void renderScreen(WorldEffectsEvent event, long now) {
        RenderTarget target = MC.gameRenderer.mainRenderTarget();
        if (target == null || target.getColorTexture() == null) return;
        if (capture == null) capture = new TextureTarget("tnt-blast-scene", target.width, target.height, false, GpuFormat.RGBA8_UNORM);
        else if (capture.width != target.width || capture.height != target.height) capture.resize(target.width, target.height);
        float scale = (float) MC.getWindow().getGuiScale();
        var encoder = RenderSystem.getDevice().createCommandEncoder();
        for (var blast : blasts) {
            float st = Mth.clamp(progress(blast, now) * 1.8F, 0, 1);
            float strength = wavePower.getValue() * (1 - st) * (1 - st);
            if (st >= 1 || strength <= .2F) continue;
            Vec3 rel = blast.position.subtract(event.getCameraPosition());
            Vector4f clip = new Vector4f((float) rel.x, (float) rel.y, (float) rel.z, 1);
            event.getViewMatrix().transform(clip);
            event.getProjectionMatrix().transform(clip);
            if (clip.w <= .05F) continue;
            float nx = clip.x / clip.w, ny = clip.y / clip.w;
            if (Math.abs(nx) > 1.6F || Math.abs(ny) > 1.6F) continue;
            encoder.copyTextureToTexture(target.getColorTexture(), capture.getColorTexture(), 0, 0, 0, 0, 0, target.width, target.height);
            try (var stack = MemoryStack.stackPush()) {
                var data = Std140Builder.onStack(stack, 32)
                        .putVec4(target.width, target.height, (nx * .5F + .5F) * target.width, (ny * .5F + .5F) * target.height)
                        .putVec4(st * Math.max(target.width, target.height) * .85F, (90 + 140 * (1 - st)) * scale, strength, 0).get();
                try (GpuBuffer uniforms = RenderSystem.getDevice().createBuffer(() -> "TNT screen wave uniforms", 128, data);
                     var pass = encoder.createRenderPass(() -> "TNT screen wave", target.getColorTextureView(), Optional.empty())) {
                    pass.setPipeline(screenPipeline);
                    pass.setUniform("BlastScreen", uniforms);
                    pass.bindTexture("SceneSampler", capture.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
                    pass.draw(3, 1, 0, 0);
                }
            }
        }
    }
    private int resolveColor() {
        return switch (colorMode.getSelectedOption().getName()) {
            case "Оранжевый" -> 0xFFFF8C1A;
            case "Красный" -> 0xFFFF4444;
            case "Синий" -> 0xFF4444FF;
            case "Зелёный" -> 0xFF44FF44;
            case "Белый" -> 0xFFFFFFFF;
            case "Фиолетовый" -> 0xFFA080FF;
            default -> ThemeEditor.getColorArgb(ThemeColorSlot.MAIN) | 0xFF000000;
        };
    }
    private record Blast(Vec3 position, float radius, long start) { }
}
